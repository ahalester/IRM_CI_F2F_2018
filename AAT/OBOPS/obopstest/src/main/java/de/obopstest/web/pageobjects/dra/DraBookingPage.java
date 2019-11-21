package de.obopstest.web.pageobjects.dra;

import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DraBookingPage {

    private static Logger LOG = LoggerFactory.getLogger(DraBookingPage.class);

    public static WebElement addPeriodButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//button[@class='ui compact icon blue button']"));
    }

    // Data Reducer ARC Options
    //{ Unset, Available, Away, If needed }
    public static WebElement availabilityPeriodDropdownOption(WebDriverExtended driver, String option) {
        WebElement dropdown = driver.findElementAndWait(By.xpath("//label[contains(text(),'Availability')]/following-sibling::sui-select"));
        dropdown.click();
        WaitUtil.sleep(200);
        return dropdown.findElement(By.xpath("//parent::*//span[contains(text(),'" + option + "')]/parent::sui-select-option"));
//        return driver.findElementAndWait(By.xpath("//label[contains(text(),'Data Reducer ARC')]/parent::*//span[contains(text(),'" + option + "')]/parent::sui-select-option"));
    }


    public static WebElement startDate(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//label[contains(text(),'Start')]/following-sibling::div//input"));
    }

    public static WebElement endDate(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//label[contains(text(),'End')]/following-sibling::div//input"));
    }

    public static WebElement okButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//button[@class='ui green button']"));
    }

    public static WebElement cancelButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//button[@class='ui red button']"));
    }

    public static WebElement availabilityRemoveIconButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("button[id*='booDeleteButton']"));
    }

}
