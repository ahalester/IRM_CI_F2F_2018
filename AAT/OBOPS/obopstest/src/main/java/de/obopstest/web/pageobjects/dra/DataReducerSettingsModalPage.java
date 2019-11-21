package de.obopstest.web.pageobjects.dra;

import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataReducerSettingsModalPage {

    private static Logger LOG = LoggerFactory.getLogger(DataReducerSettingsModalPage.class);


    public static WebElement getModalPage(WebDriverExtended driver){
        return driver.findElementAndWait(By.cssSelector("div[class*='ui modal transition active visible normal']"));
    }

    public static WebElement addSelectArcDropdown(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#drDetArcSelect"));
    }

    // ARC Options
    //{ NA, EA, EU, JAO }
    public static WebElement addSelectArcDropdownOption(WebDriverExtended driver, String option) {
        WebElement dropdown = addSelectArcDropdown(driver);
        dropdown.click();
        WaitUtil.sleep(200);

        return dropdown.findElement(By.xpath("//*[@id='drDetArcSelect']//sui-select-option[contains(.,'"+ option +"')]"));
//        return dropdown.findElement(By.xpath("//sui-select-option[contains(.,'" + option + "')]"));
    }

    public static WebElement addSelectNodeDropdown(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#drDetNodeSelect"));
    }

    // Node Options
    //{ Germany, Sweden, Czech Republic, France, UK, Netherlands, Italy, Portugal, ESO}
    public static WebElement addSelectNodeDropdownOption(WebDriverExtended driver, String option) {
        WebElement dropdown = addSelectNodeDropdown(driver);
        dropdown.click();
        WaitUtil.sleep(200);
        return dropdown.findElement(By.xpath("//*[@id='drDetNodeSelect']//sui-select-option[contains(.,'"+ option +"')]"));
//        return dropdown.findElement(By.xpath("./sui-select-option[contains(.,'" + option + "')]"));
    }

    public static WebElement manualCalibrationCheckbox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#drDetMCChB"));
    }

    public static WebElement manualImagingCheckbox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#drDetMIChB"));
    }

    public static WebElement weblogReviewCheckbox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#drDetWRChB"));
    }

    public static WebElement qa2ApprovalCheckbox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#drDetQA2AChB"));
    }

    public static WebElement addSaveButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#addSaveButton"));
    }

    public static WebElement manSaveButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#manSaveButton"));
    }

    public static WebElement addDeleteButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#addDeleteButton"));
    }

    public static WebElement manDeleteButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("#manDeleteButton"));
    }


    //
    public static WebElement getId(WebDriverExtended driver) {
        WebElement elem = driver.findElementAndWait(By.xpath("//td[contains(text(),'ALMA ID')]/following-sibling::td"));
        return elem;
    }

    public static WebElement getName(WebDriverExtended driver) {
        WebElement elem = driver.findElementAndWait(By.xpath("//td[contains(text(),'Name')]/following-sibling::td"));
        return elem;
    }

    public static WebElement getInstArc(WebDriverExtended driver) {
        WebElement elem = driver.findElementAndWait(By.xpath("//td[contains(text(),'Institution ARC')]/following-sibling::td"));
        return elem;
    }

    public static WebElement getRoles(WebDriverExtended driver) {
        WebElement elem = driver.findElementAndWait(By.xpath("//td[contains(text(),'Roles')]/following-sibling::td"));
        return elem;
    }

    //test with contains
    public static String getDrArcSelectedValue(WebDriverExtended driver) {
        WebElement elem = driver.findElementAndWait(By.xpath("//sui-select[@id='drDetArcSelect']/div[@class='text']//b/parent::span"));
        return elem.getText();
    }

    //test with contains
    public static String getNodeSelectedValue(WebDriverExtended driver) {
        WebElement elem = driver.findElementAndWait(By.xpath("//sui-select[@id='drDetNodeSelect']/div[@class='text']//b/parent::span"));
        return elem.getText();
    }
}
