package cl.apa.web.pageobjects;

import cl.apa.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ApaHeaderMenuPage {

    public static WebElement headerMenuButton(WebDriverExtended driver, String locatorName) {
        return driver.findElement(By.xpath(".//*[@href='" + locatorName + "']"));
    }

    public static WebElement headerTitle(WebDriverExtended driver) {
        return driver.findElement(By.className("header-title"));
    }
}
