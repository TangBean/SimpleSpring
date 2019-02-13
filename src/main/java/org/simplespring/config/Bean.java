package org.simplespring.config;

import java.util.*;

public class Bean {
    private String name;

    private String className;

    private String scope = "singleton";

    private final Map<Integer, ConstructorArg> indexConstructorArgs = new HashMap<>();

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
