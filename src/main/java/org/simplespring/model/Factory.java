package org.simplespring.model;

import lombok.Data;
import org.simplespring.annotation.Autowired;

@Data
public class Factory {

    @Autowired
    private Person person;

    @Autowired
    private Machine machine;
}