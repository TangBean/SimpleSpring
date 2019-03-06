package org.simplespring.main.impl;

import org.junit.Test;
import org.simplespring.config.MainConfig;
import org.simplespring.model.Factory;
import org.simplespring.model.Machine;
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

        Machine machine = (Machine) context.getBean(Machine.class);
        System.out.println(machine);

        Factory factory = (Factory) context.getBean(Factory.class);
        System.out.println(factory);
    }
}
