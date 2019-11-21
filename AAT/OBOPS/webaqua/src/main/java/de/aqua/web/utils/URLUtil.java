package de.aqua.web.utils;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class URLUtil {

    public static String getApiUrl(String url, String[] args) {
        return getApiUrl(url, false, args);
    }

    public static String getApiUrl(String url, boolean absolute, String[] args) {
        String hostPlusPort = absolute ? "" : urlTemplate(args);
        return hostPlusPort + url;
    }

    private static String urlTemplate(String[] args) {
        String urlPattern = System.getProperty("apiUrlTemplate");
        String relativeUrl = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("apiHost")) {
                relativeUrl = urlPattern.replaceAll("api_host", System.getProperty("apiHost"));
            }
            if (args[i].equals("apiPort")) {
                relativeUrl = relativeUrl.replaceAll("api_port", System.getProperty("apiPort"));
            }
            if (args[i].equals("apiRelativeUrl")) {
                relativeUrl = relativeUrl.replaceAll("api_relative_path",
                        System.getProperty("apiRelativeUrl"));
            }
        }
        return relativeUrl;
    }
}
