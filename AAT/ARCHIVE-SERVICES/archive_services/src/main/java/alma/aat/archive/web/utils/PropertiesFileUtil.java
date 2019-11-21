package alma.aat.archive.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

import static alma.aat.archive.web.utils.Environment.PHAA_BASE_URL;

@SuppressWarnings("unused")
public class PropertiesFileUtil {

    private static Logger log = LoggerFactory.getLogger(PropertiesFileUtil.class);
    private static String localConfDir = getProperty("src/test/resources/properties/", "aatConfig.properties", "local.configuration.dir");
    private static String localConfFile = getProperty("src/test/resources/properties/", "aatConfig.properties", "local.configuration.file");


    /**
     * Creates/overwrites a properties file.
     *
     * @param fileName  string containing the name of the properties file which will
     *                  be created/overwritten
     * @param propName  string containing the property name
     * @param propValue string containing the property value
     */
    public static void writePropertiesFile(String fileName, String propName, String propValue) {
        try {
            Properties properties = new Properties();
            properties.setProperty(propName, propValue);
            File file = new File("src/test/resources/properties/"
                    + fileName + ".properties");
            FileOutputStream fileOut = new FileOutputStream(file);
            properties.store(fileOut, "Temporary properties");
            fileOut.close();
        } catch (FileNotFoundException e) {
            log.error("Exception occurred", e.getMessage());
        } catch (IOException e) {
            log.error("Exception occurred", e.getCause());
        }
    }


    public static String getProperty(String file, String propName) {
        // Fist look into the local configuration (passwords should alway been here)
        String localValue = getLocalProperty(propName);
        // If not found, take the default values provided by the suite (passwords should never be found here)
        return (localValue == null ? getProperty("src/test/resources/properties/", file, propName) : localValue);
    }


    private static String getLocalProperty(String propName) {
        String localValue = getProperty(localConfDir, localConfFile, propName);
        log.info("Requested local property: " + propName + ". Found=" + (localValue != null));
        return localValue;

    }

    private static String getProperty(String dir, String file, String propName) {
        String propertyValue = "";
        try (FileReader reader = new FileReader(dir + file)) {
            Properties properties = new Properties();
            properties.load(reader);
            propertyValue = properties.getProperty(propName);
            log.info("Requested property: " + propName + ". Found in " + file);
        } catch (IOException e) {
            log.info(String.format("'%s' couldn't be found within the '%s.properties' file!", propName, file));
        }
        return propertyValue;
    }


    public static String getPropertyInDir(String dir, String fileName, String propName) {
        String propertyValue = "";
        try (FileReader reader = new FileReader(dir + fileName)) {
            Properties properties = new Properties();
            properties.load(reader);
            propertyValue = properties.getProperty(propName);
        } catch (IOException e) {
            log.info(String.format("'%s' couldn't be found within the '%s.properties' file!", propName, fileName));
        }
        return propertyValue;
    }


    /**
     * Returns a specific element id, class name, css selector xpath etc.
     *
     * @param fileName string containing the properties file name
     * @param url      string containing the specific url
     * @return string containing the specific url for the requested property name
     */
    public static String getNavigationURL(String fileName, String url) {
        String propertyValue = "";
        try (FileReader reader = new FileReader("src/test/resources/properties/"
                + fileName + ".url")) {
            Properties properties = new Properties();
            properties.load(reader);
            propertyValue = properties.getProperty(url);
        } catch (IOException e) {
            log.error("Error occurred: ", e.getMessage());
        }
        return propertyValue;
    }


    public static String getServiceURL(String phase, String release, String service) {
        return getTestEnvironmentURL(phase, release) + "/" + ("PHAA".equals(phase) ? service + "-ARCHIVE-" + release.toUpperCase() : service);
    }

    public static String getServiceURL(String phase, String release, String service, String endpoint) {
        return getTestEnvironmentURL(phase, release) + "/" + ("PHAA".equals(phase) ? service + "-ARCHIVE-" + release.toUpperCase() :
                service) + "/" + getProperty("src/test/resources/properties/", "navigation.properties", endpoint);
    }

    public static String getTestEnvironmentURL(String phase, String release) {
        return "PHAA".equals(phase) ? PHAA_BASE_URL.getValue() : "https://" + release + ".asa-test.alma.cl";
    }

    public static String getNavigationURL(String identifier) {
        return getNavigationURL("navigationURLs", identifier);
    }
}

