package de.obopstest.web.browser.setup;

import de.obopstest.web.delegates.WebDriverExtended;

/**
 * Created by bdumitru on 7/19/2017.
 */
@SuppressWarnings("unused")
public abstract class AbstractSelenium3DockerMultiBrowserDriverSetup {
    public WebDriverExtended getWebDriverExtended() {
        return Selenium3DockerMultiBrowserAbstractSetup.driver;
    }
}
