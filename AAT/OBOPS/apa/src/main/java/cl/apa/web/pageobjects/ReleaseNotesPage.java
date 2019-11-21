package cl.apa.web.pageobjects;

import cl.apa.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ReleaseNotesPage {

    public static WebElement almaReleaseNotes(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("parent-fieldname-title"));
    }
}
