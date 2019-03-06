package org.simplespring.model;

import lombok.Data;

/**
 * @program: SimpleSpring
 * @description: Person
 * @author: cuzz
 * @create: 2019-03-04 00:13
 **/
@Data
public class Person {
    private String name;
    private Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
