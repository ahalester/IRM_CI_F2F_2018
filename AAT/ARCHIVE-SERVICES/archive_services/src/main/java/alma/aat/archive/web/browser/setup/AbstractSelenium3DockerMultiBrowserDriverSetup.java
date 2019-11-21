package alma.aat.archive.web.browser.setup;

import alma.aat.archive.web.delegates.WebDriverExtended;

/**
 * Created by bdumitru on 7/19/2017.
 */
@SuppressWarnings("unused")
public abstract class AbstractSelenium3DockerMultiBrowserDriverSetup {
    public WebDriverExtended getWebDriverExtended() {
        return Selenium3DockerMultiBrowserAbstractSetup.driver;
    }
}
