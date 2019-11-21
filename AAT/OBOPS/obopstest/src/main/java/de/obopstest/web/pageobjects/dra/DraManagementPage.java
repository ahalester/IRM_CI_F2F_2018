package de.obopstest.web.pageobjects.dra;

import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DraManagementPage {

    private static Logger LOG = LoggerFactory.getLogger(DraManagementPage.class);

//    public static WebElement nodeName(WebDriverExtended driver) {
//        return driver.findElementAndWait(By.xpath("//label[contains(text(),'Node name')]/parent::td/following-sibling::td[1]"));
//    }

    // Qualifications
    //"Manual Calibration"
    //"Manual Imaging"
    //"WebLog Review"
    //"QA2 Approval"
    public static WebElement qualificationCheckbox(WebDriverExtended driver, String qualification) {
        WebElement elem = driver.findElementAndWait(By.xpath("//sui-checkbox/label[contains(.,'" + qualification + "')]"));
        //driver.findElementAndWait(By.xpath("//label[contains(text(),'" + qualification + "')]/parent::*/input"));
        return elem;
    }

    public static WebElement searchNameIdTextbox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//input[@placeholder='DR name/id']"));
    }

    // Data Reducer ARC Options
    //{ NA, EA, EU, JAO }
    public static WebElement searchDataReducerArcDropdown(WebDriverExtended driver, String option) {
        WebElement dropdown = driver.findElementAndWait(By.xpath("//label[contains(text(),'Data Reducer ARC')]/following-sibling::sui-select"));
        dropdown.click();
        WaitUtil.sleep(200);
        return dropdown.findElement(By.xpath("//parent::*//span[contains(text(),'" + option + "')]/parent::sui-select-option"));
//        return driver.findElementAndWait(By.xpath("//label[contains(text(),'Data Reducer ARC')]/parent::*//span[contains(text(),'" + option + "')]/parent::sui-select-option"));
    }


    public static WebElement searchButton(WebDriverExtended driver) {
//        WebElement elem = driver.findElementAndWait(By.xpath("//i[@class='search icon']/parent::button[contains(text(),'Search')]"));
        WebElement elem = driver.findElementAndWait(By.xpath("//i[@class='search icon']/parent::button"));
        return elem;
    }

    public static WebElement findSearchResultByText(WebDriverExtended driver, String text) {
        WebElement elem = driver.findElementAndWait(By.xpath("//td[contains(text(),'" + text + "')]/parent::tr"));
        return elem;
    }

    public static Map<String,String> getDrDetailsInTable(WebDriverExtended driver, String id) {
        Map<String, String> actual = new HashMap<>();

        WaitUtil.sleep(500);
        //row for the specific id
        List<WebElement> cells = driver.findElementsAndWait(By.xpath("//td[text()[normalize-space(.)='" + id + "']]/parent::tr/td"));

        actual.put("name",cells.get(0).getText());
        actual.put("id",cells.get(1).getText());
        actual.put("email",cells.get(2).getText());
        actual.put("ins_arc",cells.get(3).getText());
        actual.put("node",cells.get(4).getText());
        actual.put("arc",cells.get(5).getText());
        actual.put("manual_calibration",cells.get(6).getText());
        actual.put("manual_imaging",cells.get(7).getText());
        actual.put("weblog_review",cells.get(8).getText());
        actual.put("qa2_approval",cells.get(9).getText());

        return actual;
    }

    public static WebElement getRowSearchResultById(WebDriverExtended driver, String id){
//        WaitUtil.sleep(500);
        WebElement elem =  driver.findElementAndWait(By.xpath("//td[text()[normalize-space(.)='" + id + "']]"));
        return elem;
    }

}
