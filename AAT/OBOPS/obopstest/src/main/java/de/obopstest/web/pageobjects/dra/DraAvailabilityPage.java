package de.obopstest.web.pageobjects.dra;

import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DraAvailabilityPage {

    private static Logger LOG = LoggerFactory.getLogger(DraAvailabilityPage.class);


    public static WebElement manualCalibrationCheckbox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#avMCChB input"));
    }

    public static WebElement manualImagingCheckbox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#avMIChB input"));
    }

    public static WebElement weblogReviewCheckbox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#avWRChB input"));
    }

    public static WebElement qa2ApprovalCheckbox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#avQA2AChB input"));
    }

    public static WebElement avSearchButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#avSearchButton"));
    }

    public static WebElement avResetButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#avResetButton"));
    }

    public static WebElement avSelectArcDropdown(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#avSelectArc"));
    }

    // ARC Options
    //{ NA, EA, EU, JAO }
    public static WebElement avSelectArcDropdownOption(WebDriverExtended driver, String option) {
        WebElement dropdown = avSelectArcDropdown(driver);
        dropdown.click();
        WaitUtil.sleep(200);
        return dropdown.findElement(By.xpath("//sui-select-option[contains(.,'" + option + "')]"));
    }

    public static WebElement avSelectNodeDropdown(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#avSelectNode"));
    }

    // Node Options
    //{ Germany, Sweden, Czech Republic, France, UK, Netherlands, Italy, Portugal, ESO}
    public static WebElement avSelectNodeDropdownOption(WebDriverExtended driver, String option) {
        WebElement dropdown = avSelectNodeDropdown(driver);
        dropdown.click();
        WaitUtil.sleep(200);
        return dropdown.findElement(By.xpath("//sui-select-option[contains(.,'" + option + "')]"));
    }

    public static WebElement findAvailableBookeingFor(WebDriverExtended driver, String name) {
        return driver.findElementAndWait(By.xpath("//td[contains(.,'" + name + "')]"));
    }

}
