package de.obopstest.web.pageobjects;

import de.obopstest.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by bdumitru on 8/8/2017.
 */
@SuppressWarnings("unused")
public class QA2ReportPage {

    public static WebElement pageAnchor(WebDriverExtended driver) {
        return driver.findElement(By.tagName("table"));
    }
}
