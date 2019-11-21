package de.aqua.web.browser.setup;

import de.aqua.web.delegates.WebDriverExtended;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public abstract class AbstractSelenium2FirefoxWebDriverSetup {

    public WebDriverExtended getWebDriverExtended() {
        return Selenium2FirefoxAbstractSetup.driver;
    }
}
