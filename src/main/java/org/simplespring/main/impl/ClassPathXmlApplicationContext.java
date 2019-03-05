package org.simplespring.main.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.simplespring.config.Bean;
import org.simplespring.config.Property;
import org.simplespring.config.parse.ConfigManager;
import org.simplespring.main.BeanFactory;
import org.simplespring.support.ConstructorResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassPathXmlApplicationContext implements BeanFactory {
    /** Bean 的配置信息 */
    private Map<String, Bean> configs;
    /** Bean 的容器，存放初始化好的 singleton bean */
    private Map<String, Object> beanMap = new ConcurrentHashMap<>();

    public ClassPathXmlApplicationContext(String path) {
        configs = ConfigManager.getConfig(path);
        if (configs != null) {
            for (Bean beanInfo : configs.values()) {
                // 多例 bean 不初始化，容器缓存中已经存在也不用初始化
                if (!beanInfo.getScope().equals("prototype") && !beanMap.containsKey(beanInfo.getName())) {
                    Object object = createBean(beanInfo);
                    beanMap.put(beanInfo.getName(), object);
                }
            }
        }
    }

    private Object createBean(Bean beanInfo) {
        Object object = null;
        try {
            Class beanClass = Class.forName(beanInfo.getClassName());
            // 创建对象
            object = newObject(beanInfo, beanClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("配置文件中的bean初始化异常");
        }
        // 设置为对象注入属性
        if (object != null) {
            List<Property> properties = beanInfo.getProperties();
            for (Property property : properties) {
                // 当前属性为 value 属性
                if (property.getValue() != null) {
                    try {
                        BeanUtils.setProperty(object, property.getName(), property.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("配置文件中的bean的value属性注入异常");
                    }
                }
                // 当前属性为 ref 属性
                if (property.getRef() != null) {
                    Object propertyObj = beanMap.get(property.getRef());
                    if (propertyObj == null) {
                        propertyObj = createBean(configs.get(property.getRef()));
                    }
                    try {
                        BeanUtils.setProperty(object, property.getName(), propertyObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("配置文件中的bean的ref属性注入异常");
                    }
                }
            }
        }
        return object;
    }

    /**
     * 执行合适的构造函数创建对象
     */
    public Object newObject(Bean beanInfo, Class beanClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (beanInfo.getIndexConstructorArgs().isEmpty() && beanInfo.getGenericConstructorArgs().isEmpty()) {
            return beanClass.newInstance();
        }

        ConstructorResolver.ArgumentsHolder matchedConstructor =
                ConstructorResolver.matchConstructor(beanInfo, beanClass, this);
        if (matchedConstructor == null) {
            throw new RuntimeException("No proper constructor.");
        }

        Constructor<?> constructor = matchedConstructor.getConstructor();
        Object[] paramArgs = matchedConstructor.getArguments();
        return constructor.newInstance(paramArgs);
    }

    @Override
    public Object getBean(String beanName) {
        Bean beanInfo = configs.get(beanName);
        if (beanInfo.getScope().equals("prototype") || !beanMap.containsKey(beanName)) {
            return createBean(beanInfo);
        }
        return beanMap.get(beanName);
    }
}
