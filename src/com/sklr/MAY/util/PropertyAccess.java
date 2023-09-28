package com.sklr.MAY.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton class to access the properties file from anywhere in MAY
 *
 * Code from: https://gist.github.com/eliasnogueira/df8edc58c792d176c1f0c730cd73f054
 */
public class PropertyAccess {

    private static PropertyAccess instance;
    private static Properties properties;
    private static String versionedName = null;

    private PropertyAccess() {
        properties = new Properties();

        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("com/sklr/MAY/config.properties");
            properties.load(in);

            versionedName = properties.getProperty("NAME") + "/" + properties.getProperty("VERSION");
        } catch (IOException e) {
            Logger.log(e.getMessage());
        }
    }

    public static synchronized PropertyAccess getInstance() {
        if (instance == null) {
           instance = new PropertyAccess();
        }

        return instance;
    }

    public String getValue(String key) {
        return properties.getProperty(key, String.format("The key %s does not exist", key));
    }

    public String getVersionedName() {
        return versionedName;
    }



}
