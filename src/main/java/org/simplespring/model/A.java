package org.simplespring.model;

/**
 * 用来测试value属性注入
 */
public class A {
    private String strData;

    private int intData;

    public A() {
        System.out.println("创建A对象一次");
    }

    @Override
    public String toString() {
        return "A{" +
                "strData='" + strData + '\'' +
                ", intData=" + intData +
                '}';
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public int getIntData() {
        return intData;
    }

    public void setIntData(int intData) {
        this.intData = intData;
    }
}
