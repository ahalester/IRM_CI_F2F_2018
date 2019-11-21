package cl.apa.web.utils;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class GetPropertiesUtil {

    public static String getProperty(String propertyKey) {
        return System.getProperty(propertyKey);
    }
}
