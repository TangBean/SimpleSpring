package org.simplespring.config;

import java.util.*;

public class Bean {
    private String name;

    private String className;

    private String scope = "singleton";

    /** 用来存储通过索引定位的构造函数参数 */
    private final Map<Integer, ConstructorArg> indexConstructorArgs = new HashMap<>();

    /** 用来存储不索引定位的构造函数参数 */
    private final List<ConstructorArg> genericConstructorArgs = new ArrayList<>();

    private final List<Property> properties = new ArrayList<>();

    @Override
    public String toString() {
        return "Bean{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", scope='" + scope + '\'' +
                ", indexConstructorArgs=" + indexConstructorArgs +
                ", genericConstructorArgs=" + genericConstructorArgs +
                ", properties=" + properties +
                '}';
    }

    /**
     * 获取索引为 paramIndex 的构造函数入参，先尝试通过 index 获取，如果该参数未设置 index 属性，再尝试通过 type 获取
     */
    public ConstructorArg getArgumentValue(int paramIndex, Class<?> paramType, Set<ConstructorArg> usedConstructorArg) {
        if (paramIndex < 0) {
            throw new RuntimeException("索引不能为负");
        }
        ConstructorArg res = null;
        res = this.indexConstructorArgs.get(paramIndex);
        if (res == null) {
            for (ConstructorArg constructorArg : this.genericConstructorArgs) {
                if (constructorArg.getType().equals(paramType.getCanonicalName())) {
                    res = constructorArg;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * 获取 genericConstructorArgs 中第一个没有使用的参数
     */
    public ConstructorArg getGenericArgumentValue(Set<ConstructorArg> usedConstructorArg) {
        for (ConstructorArg constructorArg : this.genericConstructorArgs) {
            if (usedConstructorArg != null && usedConstructorArg.contains(constructorArg)) {
                continue;
            }
            return constructorArg;
        }
        return null;
    }

    public Map<Integer, ConstructorArg> getIndexConstructorArgs() {
        return indexConstructorArgs;
    }

    public List<ConstructorArg> getGenericConstructorArgs() {
        return genericConstructorArgs;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
