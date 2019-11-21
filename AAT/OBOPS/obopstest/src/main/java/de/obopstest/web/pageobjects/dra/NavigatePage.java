package de.obopstest.web.pageobjects.dra;

import de.obopstest.web.delegates.WebDriverExtended;

public class NavigatePage {

    public static void toGlobalLink(WebDriverExtended driver, String url) {
        driver.navigate().to(url);
    }

    public static void openUrl(WebDriverExtended driver, String url) {
        driver.get(url);
    }
}
