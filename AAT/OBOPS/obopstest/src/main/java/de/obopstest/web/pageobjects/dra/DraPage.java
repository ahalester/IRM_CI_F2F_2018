package de.obopstest.web.pageobjects.dra;

import de.obopstest.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DraPage {

    public static WebElement loggedInUserIcon(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("i[class='user icon']"));
    }

    public static WebElement drmPageText(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//b[contains(text(),'DRM Too')]"));
    }

    public static WebElement menuTab(WebDriverExtended driver, String name) {
        return driver.findElementAndWait(By.xpath(".//a[@routerlink='/" + name + "']"));
    }
}
