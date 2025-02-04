package org.example.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigurationReader {
    private static final Properties properties;
    static {
        properties = new Properties();
        try (InputStream input = ConfigurationReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (Exception e) {
            System.out.println("Sorry, unable to find config.properties");
        }
    }
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
