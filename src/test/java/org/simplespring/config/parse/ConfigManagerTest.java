package org.simplespring.config.parse;

import org.junit.Test;
import org.simplespring.config.Bean;

import java.util.Map;

import static org.junit.Assert.*;

public class ConfigManagerTest {

    @Test
    public void getConfig() {
        String path = "/applicationContext.xml";
        Map<String, Bean> configs = ConfigManager.getConfig(path);
        for (Map.Entry<String, Bean> entry : configs.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            System.out.println(entry.getValue());
        }
    }
}