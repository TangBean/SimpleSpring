package org.simplespring.main.impl;


import lombok.val;
import org.simplespring.annotation.*;
import org.simplespring.main.BeanFactory;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


/**
 * @program: SimpleSpring
 * @description: 注解
 * @author: cuzz
 * @create: 2019-03-03 23:46
 **/
public class AnnotationConfigApplicationContext implements BeanFactory {

    // ioc 容器
    private Map<String, Object> ioc = new LinkedHashMap<>();
    // 扫描的类
    private List<String> classNames = new ArrayList<>();

    private Class<?> configClazz;

    public AnnotationConfigApplicationContext(Class<?> configClazz) {
        this.configClazz = configClazz;
        String[] packageNames = this.getPackageName(configClazz);
        for (String packageName : packageNames) {
            doScanner(packageName);
        }
        doInstance();
        doAutowired();
    }



    @Override
    public Object getBean(String beanName) {
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            if (entry.getKey().equals(beanName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Object getBean(Class<?> clazz) {
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            if (entry.getValue().getClass() == clazz) {
                return entry.getValue();
            }
        }
        return null;
    }




    private void doScanner(String packageName) {
        File classDir = new File(this.getClass().getResource("/" + packageName.replaceAll("\\.", "/")).getPath());
        if (classDir.getPath().contains("test-")) {
            classDir = new File(classDir.getPath().replace("test-", ""));
        }
        // 递归扫描
        for (File file : classDir.listFiles()) {
            if (file.isDirectory()) {
                doScanner(packageName + "." + file.getName());
            } else {
                String className = packageName + "." + file.getName().replace(".class", "");
                classNames.add(className);
            }

        }
    }

    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }

        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                // 进行实例化, 只对有注解的才实例化

                // 1. 默认从业类名的首字母小写
                // 2. 如果自己定义类名字的话, 优先使用自己定义的名字
                // 3. 根据类名来配置, 利用接口作为key
                if (clazz.isAnnotationPresent(Configuration.class)) {
                    String config = lowerFirstString(clazz.getSimpleName());
                    Object obj = clazz.newInstance();
                    ioc.put(config, obj);
                    // 扫描方法有@Bean注解添加到容器中
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Bean.class)) {
                            Object bean = method.invoke(obj);
                            String beanName = method.getName();
                            // 注入@value中的属性
                            injectVal(bean);
                            ioc.put(beanName, bean);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void injectVal(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Value.class)) {

                Value value = field.getAnnotation(Value.class);
                String strVal = value.value().trim();
                String typeString = field.getGenericType().toString();
                field.setAccessible(true);
                try {
                    if (typeString.endsWith("int") || typeString.endsWith("Integer")) {
                        int intVal = Integer.parseInt(strVal);
                        field.set(bean, intVal);
                    } else if (typeString.endsWith("String")) {
                        field.set(bean, strVal);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            // 暴力反射 private也能拿到
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String beanName = autowired.value().trim();
                    // 是一个接口
                    if ("".equals(beanName)) {
                        String str = field.getType().getName(); // org.simplespring.Person
                        String simpleName = str.substring(str.lastIndexOf(".") + 1); // Person
                        beanName = lowerFirstString(simpleName); // person
                    }

                    // 私有的话 强制访问
                    field.setAccessible(true);
                    try {
                        field.set(entry.getValue(), ioc.get(beanName));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

        }
    }
    private String lowerFirstString(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return new String(chars);
    }

    /**
     * 获取包名字符串数组
     * @param configClazz
     * @return
     */
    private String[] getPackageName(Class<?> configClazz) {
        if (configClazz.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = configClazz.getAnnotation(ComponentScan.class);
            return componentScan.value();
        }
        return new String[]{};
    }

}
