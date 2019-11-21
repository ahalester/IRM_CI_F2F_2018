package de.aqua.web.utils;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.aqua.web.delegates.WebDriverExtended;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class WebElementValidationUtil {

    public static void elementDisplayedValidation(WebDriverExtended driver, WebElement we) {
        WaitUtil.waitUntilVisible(driver, we);
        Assert.assertTrue(String.format("The web element identified as '%s' could not be found on the page!", we),
                we.isDisplayed());
    }

    public static void elementDisplayedValidationNativeDriver(WebDriver driver, WebElement we) {
        WaitUtil.waitUntilVisible(driver, we);
        Assert.assertTrue(String.format("The web element identified as '%s' could not be found on the page!", we),
                we.isDisplayed());
    }
}
