package org.simplespring.config;

/**
 * 存放 constructor-arg 标签信息
 */
public class ConstructorArg {
    /* constructor-arg 标签的两个属性 */
    private String type;
    private int index;

    /* constructor-arg 标签的两个子标签 */
    private String valueAttr;
    private String refAttr;

    private boolean isConvert;

    public ConstructorArg() {
    }

    public String getValueStr() {
        if (isConvert) {
            return valueAttr;
        }
        return refAttr;
    }

    public boolean isConvert() {
        return isConvert;
    }

    public void setConvert(boolean convert) {
        isConvert = convert;
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

    public String getValueAttr() {
        return valueAttr;
    }

    public void setValueAttr(String valueAttr) {
        this.valueAttr = valueAttr;
        isConvert = true;
    }

    public String getRefAttr() {
        return refAttr;
    }

    public void setRefAttr(String refAttr) {
        this.refAttr = refAttr;
        isConvert = false;
    }

    @Override
    public String toString() {
        return "ConstructorArg{" +
                "type='" + type + '\'' +
                ", index=" + index +
                ", valueAttr='" + valueAttr + '\'' +
                ", refAttr='" + refAttr + '\'' +
                ", isConvert=" + isConvert +
                '}';
    }
}
