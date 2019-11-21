package de.obopstest.web.pageobjects;

import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.utils.Wait;
import de.obopstest.web.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ProtrackOusSearchPage {

    private static Logger LOG = LoggerFactory.getLogger(ProtrackOusSearchPage.class);


    // Start: ToDo remove dupplicats from ProtrackPage.java
    public static void waitForProtrackOusSearchTab(WebDriverExtended driver) {
        WaitUtil.waitForElementIsDisplayed(driver, By.xpath("//div[@class=\"z-caption-content\"][contains(text(),\" ObsUnitSet search\")]"), 30);
    }

    public static WebElement ousStatusUidField(WebDriverExtended driver) {
        String css = "input[class='z-textbox'][title*='Search by OUS Status UID']";
        Wait.waitForElement(driver, By.cssSelector(css));
        WebElement elem = driver.findElementAndWait(By.cssSelector(css));
        return elem;
    }

    public static WebElement ousStatusSbIntervalFrom(WebDriverExtended driver) {
        String css = "input[class='z-datebox-input']";
        Wait.waitForElement(driver, By.cssSelector(css));
        List<WebElement> elems = driver.findElementsAndWait(By.cssSelector(css));
        WebElement elem = elems.get(2);
        return elem;
    }

    public static WebElement ousStatusSbIntervalTo(WebDriverExtended driver) {
        String css = "input[class='z-datebox-input']";
        Wait.waitForElement(driver, By.cssSelector(css));
        List<WebElement> elems = driver.findElementsAndWait(By.cssSelector(css));
        WebElement elem = elems.get(3);
        return elem;
    }

    public static WebElement ousSearchButton(WebDriverExtended driver) {
        String xpath = "//button[@class='z-button'][contains(text(),'Search')]";
        Wait.waitForElement(driver, By.xpath(xpath));
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }
    //End remove dupplicats from ProtrackPage.java

    public static WebElement ousSearchResetButton(WebDriverExtended driver) {
        String xpath = "//button[contains(text(),\"Reset\")]";
        Wait.waitForElement(driver, By.xpath(xpath));
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static WebElement ousSearchCloseButton(WebDriverExtended driver) {
        String xpath = "//button[contains(text(),\"Close\")]";
        Wait.waitForElement(driver, By.xpath(xpath));
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    //OUS Status History
    public static WebElement ousStatusHistoryCheckbox(WebDriverExtended driver, String label) {
        String xpath = "//div[@class=\"z-listcell-content\"][contains(text(),\"" + label + "\")]/span[@class=\"z-listitem-checkable z-listitem-checkbox\"]";
        Wait.waitForElement(driver, By.xpath(xpath));
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static boolean isOusStatusHistoryCheckboxChecked(WebDriverExtended driver, String label){
        boolean isChecked = false;
        String xpath = "//div[@class=\"z-listcell-content\"][contains(text(),\"" + label + "\")]/span[@class=\"z-listitem-checkable z-listitem-checkbox\"]/../../..";
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        isChecked = GeneralPage.elementHasClass(elem,"z-listitem-selected");
        return isChecked;
    }

    //Favourite searches section
    public static WebElement favouriteSearchesTextbox(WebDriverExtended driver) {
        String xpath = "//div[@class=\"z-hlayout\"]//input[@class=\"z-combobox-input\"][@type=\"text\"]";
        Wait.waitForElement(driver, By.xpath(xpath));
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static WebElement favouriteSearchesSave(WebDriverExtended driver) {
        String xpath = "//button[contains(text(),\"Save\")]";
        Wait.waitForElement(driver, By.xpath(xpath));
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static WebElement favouriteSearchesDropdownArrow(WebDriverExtended driver) {
        String xpath = "//div[@class=\"z-hlayout\"]//input[@class=\"z-combobox-input\"][@type=\"text\"]/../a";
        Wait.waitForElement(driver, By.xpath(xpath));
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static WebElement favouriteSearchesDropdownOption(WebDriverExtended driver, String option) {
        String xpath = "//span[@class=\"z-comboitem-text\"][contains(text(),\""+ option +"\")]";
        Wait.waitForElement(driver, By.xpath(xpath));
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static WebElement messageBoxOKbutton(WebDriverExtended driver) {
        String xpath = "//button[@class=\"z-messagebox-button z-button\"][contains(text(),\"OK\")]";
        Wait.waitForElement(driver, By.xpath(xpath));
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    
}
