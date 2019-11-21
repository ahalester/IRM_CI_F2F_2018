package cl.apa.web.pageobjects;

import cl.apa.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ApaWeblogPage {

    public static WebElement weblogSummary(WebDriverExtended driver) {
        return driver.findElement(By.xpath(".//*[@summary='Measurement Set Summaries']"));
    }
}
