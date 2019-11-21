package cl.apa.web.pageobjects;

import cl.apa.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by bdumitru on 7/12/2017.
 */
public class LoginPage {

    public static WebElement userField(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("username"));
    }

    public static WebElement passwordField(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("password"));
    }

    public static WebElement loginButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.className("btn-submit"));
    }

    public static WebElement warnCheckbox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("warn"));
    }

}
