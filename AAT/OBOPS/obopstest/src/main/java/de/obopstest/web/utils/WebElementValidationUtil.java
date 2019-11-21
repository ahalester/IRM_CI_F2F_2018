package de.obopstest.web.utils;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.obopstest.web.delegates.WebDriverExtended;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class WebElementValidationUtil {

    private static Logger LOG = LoggerFactory.getLogger(WebElementValidationUtil.class);

    public static void elementDisplayedValidation(WebDriverExtended driver, WebElement we,
                                                  final int waitInSeconds) {
        try {
            WaitUtil.waitUntilVisible(driver, we, waitInSeconds);
        } catch (Exception e) {
            LOG.error("The web element couldn't be found on the page!");
            Assert.fail("The web element couldn't be found on the page!");
        }
    }

    public static void elementDisplayedValidation(WebDriverExtended driver, WebElement we,
                                                  String elementName, final int waitInSeconds) {
        try {
            WaitUtil.waitUntilVisible(driver, we, waitInSeconds);
        } catch (Exception e) {
            LOG.error(String.format("The web element '%s' couldn't be found on the page!",
                    elementName));
            Assert.fail(String.format("The web element '%s' couldn't be found on the page!",
                    elementName));
        }
    }

    public static void elementDisplayedValidation(WebDriverExtended driver, WebElement we) {
        try {
            WaitUtil.waitUntilVisible(driver, we);
            WaitUtil.sleep(500);
        } catch (Exception e) {
            LOG.error("The web element couldn't be found on the page!");
            Assert.fail("The web element couldn't be found on the page!");
        }
    }

    public static void elementDisplayedValidation(WebDriverExtended driver, WebElement we,
                                                  String elementName) {
        try {
            WaitUtil.waitUntilVisible(driver, we);
            WaitUtil.sleep(500);
        } catch (Exception e) {
            LOG.error(String.format("The web element '%s' couldn't be found on the page!",
                    elementName));
            Assert.fail(String.format("The web element '%s' couldn't be found on the page!",
                    elementName));
        }
    }

    public static void elementDisplayedValidationNativeDriver(WebDriver driver, WebElement we) {
        try {
            WaitUtil.waitUntilVisible(driver, we);
        } catch (Exception e) {
            LOG.error("The web element couldn't be found on the page!");
            Assert.fail("The web element couldn't be found on the page!");
        }
    }
}
