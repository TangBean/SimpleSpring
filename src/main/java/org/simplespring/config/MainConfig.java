package org.simplespring.config;

import org.simplespring.annotation.Bean;
import org.simplespring.annotation.ComponentScan;
import org.simplespring.annotation.Configuration;
import org.simplespring.model.Factory;
import org.simplespring.model.Machine;
import org.simplespring.model.Person;

/**
 * @program: SimpleSpring
 * @description: 配置类
 * @author: cuzz
 * @create: 2019-03-04 00:09
 **/
@Configuration
@ComponentScan("org.simplespring")
public class MainConfig {

    @Bean
    public Person person(){
        return new Person("cuzz", 18);
    }

    @Bean
    public Machine machine() {
        return new Machine();
    }

    @Bean
    public Factory factory() {
        return new Factory();
    }
}
