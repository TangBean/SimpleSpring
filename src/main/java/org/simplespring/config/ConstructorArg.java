package org.simplespring.config;

/**
 * 存放 constructor-arg 标签信息
 */
public class ConstructorArg {
    /* constructor-arg 标签的两个属性 */
    private String type;
    private int index;

    /* constructor-arg 标签的两个子标签 */
    private String value;
    private String ref;

    public ConstructorArg() {
    }

    @Override
    public String toString() {
        return "ConstructorArg{" +
                "type='" + type + '\'' +
                ", index=" + index +
                ", value='" + value + '\'' +
                ", ref='" + ref + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
