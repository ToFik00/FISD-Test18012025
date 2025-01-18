package org.piva.fisdtest18012025.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    Properties properties;

    public PropertiesReader() {

        try (InputStream inputStream = PropertiesReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

}
