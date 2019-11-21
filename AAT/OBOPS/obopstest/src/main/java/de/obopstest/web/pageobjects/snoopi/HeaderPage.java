package de.obopstest.web.pageobjects.snoopi;

import de.obopstest.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HeaderPage {

    public static WebElement loggedInUserIcon(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("img[src='assets/img/avatar.png']"));
    }

    public static WebElement snoopiTitle(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//p[@class=\"title\"][text()=\"SnooPI\"]"));
    }

    public static WebElement headerHelpButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("statusIconsButton"));
    }

    public static WebElement headerContactScientistCheckbox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("header-cs-chbx"));
    }

    public static WebElement headerExportButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("exportButton"));
    }

    public static WebElement headerExportTxtOption(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("exportTXT"));
    }

    public static WebElement headerExportCvsOption(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("exportCVS"));
    }

    public static WebElement headerUserInfo(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("header-user-info"));
    }

//    public static WebElement headerUserInfoExecutive(WebDriverExtended driver) {
//        return driver.findElementAndWait(By.xpath("//p[@id='header-user-info']/span"));
//    }

//    public static WebElement headerUserInfoArc(WebDriverExtended driver) {
//        return driver.findElementAndWait(By.xpath("//p[@id='header-user-info']/span"));
//    }


}
