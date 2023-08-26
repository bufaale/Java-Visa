package com.petstore.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigReader {
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static Properties properties;
    private static final Logger logger = Logger.getLogger(ConfigReader.class.getName());

    // Static block to load properties from the configuration file
    static {
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
            properties = new Properties();
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            // Log the exception instead of just printing the stack trace
            logger.log(Level.SEVERE, "Error loading configuration properties", e);
        }
    }

    /**
     * Get the value associated with the given key from the configuration properties.
     *
     * @param key The key to retrieve the value for
     * @return The value associated with the key, or null if not found
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }
}
