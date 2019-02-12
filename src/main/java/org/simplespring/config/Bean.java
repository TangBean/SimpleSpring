package org.simplespring.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
