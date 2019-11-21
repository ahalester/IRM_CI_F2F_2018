package alma.aat.archive.web.pageobjects;

import alma.aat.archive.web.delegates.WebDriverExtended;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class NavigatePage {

    public static void toGlobalLink(WebDriverExtended driver, String url) {
        driver.navigate().to(url);
    }

    public static void openUrl(WebDriverExtended driver, String url) {
        driver.get(url);
    }
}
