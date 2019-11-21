package de.project.web.pageobjects;

import de.project.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class GooglePage {

    public static WebElement googleLogo(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("hplogo"));
    }

    public static WebElement googleTextField(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("lst-ib"));
    }

    public static WebElement googleSearch(WebDriverExtended driver, String buttonName) {
        return driver.findElementAndWait(By.xpath(".//*[@value='" + buttonName + "']"));
    }
}
