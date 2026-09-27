package edu.qsp.restorent_management_system.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try {

            FileInputStream fileInputStream =
                    new FileInputStream(
                            "src/test/resources/config.properties"
                    );

            properties.load(fileInputStream);

            fileInputStream.close();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to load config.properties",
                    e
            );
        }
    }

    public static String getProperty(String key) {

        return properties.getProperty(key);
    }
}