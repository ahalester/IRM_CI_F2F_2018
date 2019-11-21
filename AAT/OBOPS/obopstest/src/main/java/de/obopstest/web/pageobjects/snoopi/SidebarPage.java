package de.obopstest.web.pageobjects.snoopi;

import de.obopstest.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SidebarPage {

    //Navigation section
    public static WebElement sidebarToggler(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("sidebar-toggler"));
    }

    public static WebElement homeLink(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("home-link"));
    }

    public static WebElement projectsLink(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("projects-link"));
    }

    public static WebElement sbsLink(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("sbs-link"));
    }


    //Links section

    public static WebElement userManualLink(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("manual-link"));
    }

    public static WebElement sciencePortalLink(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("scport-link"));
    }

    public static WebElement archiveQueryLink(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("arcq-link"));
    }

    public static WebElement helpdeskLink(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("helpdesk-link"));
    }



}
