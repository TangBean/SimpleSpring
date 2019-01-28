package org.simplespring.model;

/**
 * 测试ref属性注入
 */
public class B {
    private A refData;

    public B() {
        System.out.println("创建B对象一次");
    }

    @Override
    public String toString() {
        return "B{" +
                "refData=" + refData +
                '}';
    }

    public A getRefData() {
        return refData;
    }

    public void setRefData(A aRefData) {
        this.refData = aRefData;
    }
}
