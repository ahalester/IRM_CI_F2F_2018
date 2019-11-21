package de.obopstest.web.utils;

import de.obopstest.web.utils.enums.EnvironmentURL;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import static de.obopstest.web.utils.enums.EnvironmentURL.*;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class PropertiesFileUtil {

    private static Logger LOG = LoggerFactory.getLogger(PropertiesFileUtil.class);

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
            LOG.error("Exception occurred", e.getMessage());
        } catch (IOException e) {
            LOG.error("Exception occurred", e.getCause());
        }
    }

    /**
     * Returns a specific property value from a specific properties file.
     *
     * @param fileName string containing the properties file name
     * @param propName string containing the specific property name
     * @return string containing the specific property value for the requested property name
     */
    public static String getProperty(String fileName, String propName) {
        String propertyValue;
        try (FileReader reader = new FileReader("src/test/resources/properties/"
                + fileName + ".properties")) {
            Properties properties = new Properties();
            properties.load(reader);
            propertyValue = properties.getProperty(propName);
        } catch (IOException e) {
            LOG.info(String.format("The element '%s' couldn't be found within the 'elements"
                    + ".properties' file!", propName));
            propertyValue = propName;
        }
        if (StringUtils.isBlank(propertyValue)) {
            propertyValue = propName;
        }
        return propertyValue;
    }

    public static String getProperty(String propName) {
        return getProperty("project", propName);
    }

    public static String getElementName(String propName) {
        if (propName.equalsIgnoreCase("username")) {
            return getProperty(propName);
        } else {
            return getProperty("elements", propName);
        }
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
            LOG.error("Error occurred: ", e.getMessage());
        }
        return propertyValue;
    }

    public static String getNavigationURL(String identifier) {
        return getNavigationURL("navigationURLs", identifier);
    }

    public static String getNavigationURL(EnvironmentURL identifier) {
        return getNavigationURL(identifier.getValue());
    }

    public static String getNavigationURL(EnvironmentURL uri, EnvironmentURL version) {
        return getNavigationURL(uri.getValue()).replace("deployment_version",
                System.getProperty(version.getValue()));
    }

    public static String getNavigationURL(String uri, EnvironmentURL version) {
        return getNavigationURL(uri).replace("deployment_version",
                System.getProperty(version.getValue()));
    }

    @NotNull
    public static String getNavigationURL(EnvironmentURL uri, String version) {
        if (uri.getValue().equalsIgnoreCase("pha_url")) {
            return getNavigationURL(PHA_URL.getValue()).replace("deployment_version", version);
        } else if (uri.getValue().equalsIgnoreCase("phb_url")){
            return getNavigationURL(PHB_URL.getValue()).replace("deployment_version", version);
//                    + getNavigationURL(uri.getValue());
        } else {
            return getNavigationURL(PHA_DEV.getValue());
        }
    }

    @NotNull
    public static String getNavigationURLptcsf(EnvironmentURL uri, String version) {
        if (uri.getValue().contains("pha_")) {
            return getNavigationURL(PHA_PT_STATE_CHANGE_URL.getValue()).replace("deployment_version", version);
        } else if (uri.getValue().contains("phb_")){
            return getNavigationURL(PHB_PT_STATE_CHANGE_URL.getValue()).replace("deployment_version", version);
//                    + getNavigationURL(uri.getValue());
        }
        else return null;
    }

    @NotNull
    public static String getNavigationURLdra(EnvironmentURL uri, String version) {
        if (uri.getValue().contains("pha_")) {
            return getNavigationURL(PHA_DRA_URL.getValue()).replace("deployment_version", version);
        } else if (uri.getValue().contains("phb_")){
            return getNavigationURL(PHB_DRA_URL.getValue()).replace("deployment_version", version);
//                    + getNavigationURL(uri.getValue());
        }
        else return null;
    }

    @NotNull
    public static String getNavigationURLsnoopi(EnvironmentURL uri, String version) {
        if (uri.getValue().contains("pha_")) {
            return getNavigationURL(PHA_SNOOPI_URL.getValue()).replace("deployment_version", version);
        } else if (uri.getValue().contains("phb_")){
            return getNavigationURL(PHB_SNOOPI_URL.getValue()).replace("deployment_version", version);
//                    + getNavigationURL(uri.getValue());
        }
        else return null;
    }

    @NotNull
    public static String getApiURL(EnvironmentURL uri, String version) {
        if (uri.getValue().equalsIgnoreCase("rest_api_pha_url")) {
            return getNavigationURL(API_PHA.getValue());
        } else if (uri.getValue().equalsIgnoreCase("rest_api_phb_url")){
            return getNavigationURL(API_PHB.getValue()).replace("deployment_version", version);
//                    + getNavigationURL(uri.getValue());
        } else {
            return getNavigationURL(API_DEV.getValue());
        }
    }


    @NotNull
    public static String getNavigationURL(EnvironmentURL baseUrl, EnvironmentURL uri,
                                          String version) {
        return getNavigationURL(baseUrl.getValue()).replace("deployment_version", version)
                + getNavigationURL(uri.getValue());
    }

    @NotNull
    public static String getURL(EnvironmentURL uri, String version) {
        if (uri.getValue().equalsIgnoreCase("pha_url")) {
            return getNavigationURL(EnvironmentURL.PHA_URL.getValue());
        } else if (uri.getValue().equalsIgnoreCase("phb_url")){
            return getNavigationURL(EnvironmentURL.PHB_URL.getValue()).replace("deployment_version", version);
        } else {
            return getNavigationURL(EnvironmentURL.PHA_DEV.getValue());
        }
    }

    @NotNull
    public static String getDraURL(EnvironmentURL uri, String version) {
        if (uri.getValue().equalsIgnoreCase("pha_dra_url")) {
            return getNavigationURL(EnvironmentURL.PHA_DRA_URL.getValue());
        } else if (uri.getValue().equalsIgnoreCase("phb_dra_url")){
            return getNavigationURL(EnvironmentURL.PHB_DRA_URL.getValue()).replace("deployment_version", version);
        } else {
            return getNavigationURL(EnvironmentURL.DEV_DRA_URL.getValue());
        }
    }
}

