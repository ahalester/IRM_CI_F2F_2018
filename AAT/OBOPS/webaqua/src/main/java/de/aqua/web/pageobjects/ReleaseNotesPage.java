package de.aqua.web.pageobjects;

import de.aqua.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ReleaseNotesPage {

    public static WebElement almaReleaseNotes(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("parent-fieldname-title"));
    }
}
