package org.simplespring.config.parse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.simplespring.config.Bean;
import org.simplespring.config.ConstructorArg;
import org.simplespring.config.Property;
import org.simplespring.util.StringUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    public static Map<String, Bean> getConfig(String path) {
        // 获取配置文件输入流
        InputStream xmlConfigFile = ConfigManager.class.getResourceAsStream(path);
        // 创建XML文件解析器
        SAXReader reader = new SAXReader();
        // 存放需要被配置的Bean的配置信息
        Map<String, Bean> configs = new HashMap<>();
        Document doc = null;
        try {
            doc = reader.read(xmlConfigFile);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("配置文件有误");
        }
        if (doc != null) {
            // 获取配置文件中的所有bean标签
            List<Element> beans = doc.selectNodes("//bean");
            // 根据配置文件中的配置信息构造configs
            for (Element bElement : beans) {
                Bean bean = setBean(bElement);
                configs.put(bean.getName(), bean);
            }
        }
        return configs;
    }

    private static Bean setBean(Element bElement) {
        Bean bean = new Bean();
        String bName = bElement.attributeValue("name");
        String bClass = bElement.attributeValue("class");
        String bScope = bElement.attributeValue("scope");
        bean.setName(bName);
        bean.setClassName(bClass);
        if (bScope != null) {
            bean.setScope(bScope);
        }
        setBeanConstructorArg(bElement, bean);
        setBeanProperties(bElement, bean);
        return bean;
    }

    private static void setBeanConstructorArg(Element bElement, Bean bean) {
        List<Element> constructorArgs = bElement.elements("constructor-arg");
        if (constructorArgs != null) {
            for (Element constructorArg : constructorArgs) {
                ConstructorArg constructor = new ConstructorArg();
                String typeAttr = constructorArg.attributeValue("type");
                String valueAttr = constructorArg.element("value").getText();
                String refAttr = constructorArg.element("ref").getText();
                String indexAttr = constructorArg.attributeValue("index");
                if (indexAttr != null) {
                    try {
                        int index = Integer.parseInt(indexAttr);
                        if (StringUtils.hasLength(typeAttr)) {
                            constructor.setType(typeAttr);
                        }
                        // todo: 设置 value 和 ref
                        if (bean.getIndexConstructorArgs().containsKey(index)) {
                            throw new RuntimeException("Ambiguous constructor-arg entries for index " + indexAttr);
                        } else {
                            bean.getIndexConstructorArgs().put(index, constructor);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Attribute 'index' of tag 'constructor-arg' must be an integer");
                    }
                } else {
                    if (StringUtils.hasLength(typeAttr)) {
                        constructor.setType(typeAttr);
                    }
                    // todo: 设置 value 和 ref
                    bean.getGenericConstructorArgs().add(constructor);
                }



            }
        }
    }

    // todo: 写 value 和 ref 的设置函数

    private static void setBeanProperties(Element bElement, Bean bean) {
        List<Element> properties = bElement.elements("property");
        if (properties != null) {
            for (Element pElement : properties) {
                Property property = new Property();
                String pName = pElement.attributeValue("name");
                String pValue = pElement.attributeValue("value");
                String pRef = pElement.attributeValue("ref");
                property.setName(pName);
                property.setValue(pValue);
                property.setRef(pRef);
                bean.getProperties().add(property);
            }
        }
    }


}
