package de.aqua.web.browser.setup;

import de.aqua.web.delegates.WebDriverExtended;

/**
 * Created by bdumitru on 7/18/2017.
 */
public abstract class AbstractSelenium3DockerChromeDriverSetup {
    public WebDriverExtended getWebDriverExtended() {
        return Selenium3DockerChromeAbstractSetup.driver;
    }
}
