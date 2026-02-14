package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Reads configuration from properties file
 * This class handles all configuration settings for tests
 */
public class ConfigReader {
    
    private static Properties properties;
    private static final String CONFIG_PATH = "config.properties";
    
    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream(CONFIG_PATH);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            System.out.println("Config file not found. Using default values.");
            properties = new Properties();
            // Set default values
            properties.setProperty("base.url", "http://localhost:5173");
            properties.setProperty("browser", "chrome");
            properties.setProperty("implicit.wait", "10");
            properties.setProperty("explicit.wait", "15");
            properties.setProperty("headless", "false");
        }
    }
    
    public static String getBaseUrl() {
        return properties.getProperty("base.url", "http://localhost:5173");
    }
    
    public static String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }
    
    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait", "10"));
    }
    
    public static int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait", "15"));
    }
    
    public static boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
