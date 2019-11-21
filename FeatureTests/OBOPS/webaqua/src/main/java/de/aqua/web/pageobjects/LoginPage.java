package de.aqua.web.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import de.aqua.web.delegates.WebDriverExtended;

/**
 * Created by bdumitru on 7/12/2017.
 */
public class LoginPage {

    public static WebElement userField(WebDriverExtended driver) {
        return driver.findElement(By.id("username"));
    }

    public static WebElement passwordField(WebDriverExtended driver) {
        return driver.findElement(By.id("password"));
    }

    public static WebElement loginButton(WebDriverExtended driver) {
        return driver.findElement(By.className("btn-submit"));
    }

    public static WebElement warnCheckbox(WebDriverExtended driver) {
        return driver.findElement(By.id("warn"));
    }

}
