package org.simplespring.model;

/**
 * 测试scope="prototype"
 */
public class C {
    private String strData;

    public C() {
        System.out.println("创建C对象一次");
    }

    @Override
    public String toString() {
        return "C{" +
                "strData='" + strData + '\'' +
                '}';
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }
}
