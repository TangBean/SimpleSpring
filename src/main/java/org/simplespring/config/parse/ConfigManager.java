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

    /**
     * 解析配置文件
     */
    public static Map<String, Bean> getConfig(String path) {
        // 获取配置文件输入流
        InputStream xmlConfigFile = ConfigManager.class.getResourceAsStream(path);
        // 创建 XML 文件解析器
        SAXReader reader = new SAXReader();
        // 存放需要被配置的 Bean 的配置信息
        Map<String, Bean> configs = new HashMap<>();
        Document doc = null;
        try {
            doc = reader.read(xmlConfigFile);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("配置文件有误");
        }
        if (doc != null) {
            // 获取配置文件中的所有 bean 标签
            List<Element> beans = doc.selectNodes("//bean");
            // 根据配置文件中的配置信息构造 configs
            for (Element bElement : beans) {
                Bean bean = setBean(bElement);
                configs.put(bean.getName(), bean);
            }
        }
        return configs;
    }

    /**
     * 解析 bean 标签
     */
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

    /**
     * 解析 constructor-arg 标签
     */
    private static void setBeanConstructorArg(Element bElement, Bean bean) {
        List<Element> elements = bElement.elements("constructor-arg");
        if (elements != null) {
            for (Element element : elements) {
                ConstructorArg constructor = new ConstructorArg();
                parseValueAndRefForConstructor(element, bean, constructor);

                String typeAttr = element.attributeValue("type");
                String indexAttr = element.attributeValue("index");
                // 优先解析 index 属性，将解析结果放入 bean 的 Map 集合中
                if (indexAttr != null) {
                    try {
                        int index = Integer.parseInt(indexAttr);
                        if (StringUtils.hasLength(typeAttr)) {
                            constructor.setType(typeAttr);
                        }
                        if (bean.getIndexConstructorArgs().containsKey(index)) {
                            throw new RuntimeException("Ambiguous constructor-arg entries for index " + indexAttr);
                        } else {
                            constructor.setIndex(index);
                            bean.getIndexConstructorArgs().put(index, constructor);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Attribute 'index' of tag 'constructor-arg' must be an integer");
                    }
                } else {
                    // 如果该标签没有 index 属性，将解析结果放入 bean 的 List 集合中
                    if (StringUtils.hasLength(typeAttr)) {
                        constructor.setType(typeAttr);
                    }
                    bean.getGenericConstructorArgs().add(constructor);
                }
            }
        }
    }

    /**
     * 解析 constructor-arg 的 value 或 ref 属性值
     */
    private static void parseValueAndRefForConstructor(Element element, Bean bean, ConstructorArg constructor) {
        boolean hasValueAttribute = false, hasRefAttribute = false;
        String valueAttr = element.attributeValue("value");
        String refAttr = element.attributeValue("ref");
        if (valueAttr != null) {
            hasValueAttribute = true;
        }
        if (refAttr != null) {
            hasRefAttribute = true;
        }

        if (hasValueAttribute && hasRefAttribute) {
            throw new RuntimeException(bean.getName() +
                    " is only allowed to contain either 'ref' attribute OR 'value' attribute");
        }

        if (hasValueAttribute) {
            constructor.setValueAttr(valueAttr);
        }
        if (hasRefAttribute) {
            constructor.setRefAttr(refAttr);
        }
    }

    /**
     * 解析 property 标签
     */
    private static void setBeanProperties(Element bElement, Bean bean) {
        List<Element> properties = bElement.elements("property");
        if (properties != null) {
            for (Element pElement : properties) {
                Property property = new Property();
                String pName = pElement.attributeValue("name");
                property.setName(pName);
                parseValueAndRefForProperty(pElement, bean, property);
                bean.getProperties().add(property);
            }
        }
    }

    /**
     * 解析 property 的 value 或 ref 属性值
     */
    private static void parseValueAndRefForProperty(Element element, Bean bean, Property property) {
        boolean hasValueAttribute = false, hasRefAttribute = false;
        String valueAttr = element.attributeValue("value");
        String refAttr = element.attributeValue("ref");
        if (valueAttr != null) {
            hasValueAttribute = true;
        }
        if (refAttr != null) {
            hasRefAttribute = true;
        }

        if (hasValueAttribute && hasRefAttribute) {
            throw new RuntimeException(bean.getName() +
                    " is only allowed to contain either 'ref' attribute OR 'value' attribute");
        }

        if (hasValueAttribute) {
            property.setValue(valueAttr);
        }
        if (hasRefAttribute) {
            property.setRef(refAttr);
        }
    }

}
