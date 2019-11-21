package de.obopstest.web.browser.setup;

import de.obopstest.web.delegates.WebDriverExtended;

/**
 * Created by bdumitru on 7/18/2017.
 */
public abstract class AbstractSelenium3DockerChromeDriverSetup {
    public WebDriverExtended getWebDriverExtended() {
        return Selenium3DockerChromeAbstractSetup.driver;
    }
}
