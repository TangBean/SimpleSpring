package org.simplespring.main.impl;


import org.simplespring.annotation.Bean;
import org.simplespring.annotation.Configuration;
import org.simplespring.main.BeanFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @program: SimpleSpring
 * @description: 注解
 * @author: cuzz
 * @create: 2019-03-03 23:46
 **/

public class AnnotationConfigApplicationContext implements BeanFactory {

    // ioc 容器
    private Map<String, Object> ioc = new HashMap<>();
    // 扫描的类
    private List<String> classNames = new ArrayList<>();

    public AnnotationConfigApplicationContext(Class<?> clazz){
        doScanner("org.simplespring");
        doInstance();
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
                            ioc.put(beanName, bean);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String lowerFirstString(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return new String(chars);
    }


}
