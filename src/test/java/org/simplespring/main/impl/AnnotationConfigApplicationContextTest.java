package org.simplespring.main.impl;

import org.junit.Test;
import org.simplespring.config.MainConfig;
import org.simplespring.model.Person;

/**
 * @program: SimpleSpring
 * @description: test
 * @author: cuzz
 * @create: 2019-03-03 23:59
 **/
public class AnnotationConfigApplicationContextTest {

    @Test
    public void getBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        Person person = (Person) context.getBean(Person.class);
        System.out.println(person);
        Person person1 = (Person) context.getBean("person");
        System.out.println(person1);
        System.out.println(person == person1);
    }
}
