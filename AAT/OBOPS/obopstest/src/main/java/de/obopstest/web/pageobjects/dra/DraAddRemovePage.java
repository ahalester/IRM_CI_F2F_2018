package de.obopstest.web.pageobjects.dra;

import de.obopstest.web.delegates.WebDriverExtended;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class DraAddRemovePage {

    private static Logger LOG = LoggerFactory.getLogger(DraAddRemovePage.class);
    public static WebElement analystName(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//td[contains(text(),'Analyst name')]/following-sibling::td[1]"));
    }

    public static WebElement almaId(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//label[contains(text(),'Alma Id')]/parent::td/following-sibling::td[1]"));
    }

    public static WebElement arc(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//label[contains(text(),'ARC')]/parent::td/following-sibling::td[1]"));
    }

    public static WebElement nodeName(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//label[contains(text(),'Node name')]/parent::td/following-sibling::td[1]"));
    }

    public static String getQualificationStatus(WebDriverExtended driver, String qualification) {
//        WebElement selector = null;
        String status = null;
        switch (qualification) {
            case "Manual Calibration" :
//                selector = analystName(driver);
                status = driver.findElementAndWait(By.xpath("//td[contains(text(),'Analyst name')]/following-sibling::td[3]")).getText();
                break;
            case "Manual Imaging":
//                selector = almaId(driver);
//                status = ((WebDriverExtended)selector).findElementAndWait(By.xpath("")).getText();
                status = driver.findElementAndWait(By.xpath("//label[contains(text(),'Alma Id')]/parent::td/following-sibling::td[3]")).getText();
                break;
            case "WebLog Review":
//                selector = arc(driver);
                status = driver.findElementAndWait(By.xpath("//label[contains(text(),'ARC')]/parent::td/following-sibling::td[3]")).getText();
                break;
            case "QA2 Approval":
//                selector = nodeName(driver);
                status = driver.findElementAndWait(By.xpath("//label[contains(text(),'Node name')]/parent::td/following-sibling::td[3]")).getText();
                break;
            default:
                Assert.assertTrue("Wrong qualification specified",false);
                break;
        }
        return status;
    }

//    public static WebElement qualifications(WebDriverExtended driver, String name) {
//        return driver.findElementAndWait(By.xpath(".//a[@routerlink='/" + name + "']"));
//    }


//
    public static WebElement accountSearch(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("input[placeholder='Account Search...']"));
    }

    public static WebElement addIcon(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("button[id*='addButton']"));
    }

    public static WebElement removeIcon(WebDriverExtended driver) {
        return driver.findElementAndWait(By.cssSelector("button[id*='deleteButton']"));
    }

    public static WebElement findElemInTableByText(WebDriverExtended driver, String text) {
        return driver.findElementAndWait(By.xpath("//tbody/tr/td[text()='" + text + "']"));
    }

    public static WebElement findAccountRowByText(WebDriverExtended driver, String text) {
        return driver.findElementAndWait(By.xpath("//td[text()='" + text + "']/parent::tr"));
    }

    public static WebElement searchTextBox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//input[@placeholder='Account Search...']"));
    }

    public static WebElement searchIcon(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//i[@class='search icon']"));
    }

    public static boolean isAddIconDisabled(WebDriverExtended driver) {
        driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS) ;
        boolean ret = driver.isElementPresent(By.cssSelector("button[id*='addButton'][disabled]"));
        driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS) ;
        return ret;
    }

    public static boolean isRemoveIconDisabled(WebDriverExtended driver) {
        driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS) ;
        boolean ret = driver.isElementPresent(By.cssSelector("button[id*='deleteButton'][disabled]"));
        driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS) ;
        return ret;
    }


}
