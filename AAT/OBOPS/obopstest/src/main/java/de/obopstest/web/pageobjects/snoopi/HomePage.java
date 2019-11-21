package de.obopstest.web.pageobjects.snoopi;

import de.obopstest.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HomePage {

    public static WebElement searchBox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("home-search-txt"));
    }

    public static WebElement searchButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("home-search-btn"));
    }

    public static WebElement moreNewsButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("more-news-button"));
    }

    public static WebElement searchDropdown(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("dropdownMenu1"));
    }

    public static WebElement searchDropdownOptionPrj(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("home-search-prjs"));
    }

    public static WebElement searchDropdownOptionSbs(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("home-search-sbs"));
    }

    // Widgets
    public static WebElement piProjectsWidget(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("stat-widget-pi-projects"));
    }

    public static WebElement piSbsWidget(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("stat-widget-pi-sbs"));
    }

    public static WebElement coIprojectsWidget(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("stat-widget-coi-projects"));
    }

    public static WebElement coIsbsWidget(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("stat-widget-coi-sbs"));
    }

    public static String getWidgetValue(WebDriverExtended driver, String widget) {
        String value = null;
        switch (widget) {
            case "pi-projects":
                value = piProjectsWidget(driver).findElement(By.tagName("info-widget")).getAttribute("ng-reflect-title");
                break;
            case "pi-sbs":
                 value = piSbsWidget(driver).findElement(By.tagName("info-widget")).getAttribute("ng-reflect-title");
                 break;
            case "coi-projects":
                value = coIprojectsWidget(driver).findElement(By.tagName("info-widget")).getAttribute("ng-reflect-title");
                break;
            case "coi-sbs":
                value = coIsbsWidget(driver).findElement(By.tagName("info-widget")).getAttribute("ng-reflect-title");
                break;
        }
        return value;
    }
}


