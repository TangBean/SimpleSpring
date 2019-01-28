package org.simplespring.main.impl;

import org.junit.Test;
import org.simplespring.main.BeanFactory;
import org.simplespring.model.A;
import org.simplespring.model.B;
import org.simplespring.model.C;

import static org.junit.Assert.*;

public class ClassPathXmlApplicationContextTest {

    @Test
    public void getBean() {
        String path = "/applicationContext.xml";
        BeanFactory factory = new ClassPathXmlApplicationContext(path);
        A a1 = (A) factory.getBean("A");
        B b1 = (B) factory.getBean("B");
        C c1 = (C) factory.getBean("C");

        A a2 = (A) factory.getBean("A");
        C c2 = (C) factory.getBean("C");

        System.out.println(a1);
        System.out.println(b1);
        System.out.println(c1);
        System.out.println(a2);
        System.out.println(c2);
    }
}