package org.simplespring.model;

import lombok.Data;
import org.simplespring.annotation.Value;

@Data
public class Machine {

    @Value("666")
    private int id;

    @Value("machineName")
    private String name;
}