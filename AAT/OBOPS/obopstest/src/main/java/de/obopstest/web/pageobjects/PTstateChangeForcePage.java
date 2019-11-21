package de.obopstest.web.pageobjects;

import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.utils.Wait;
import de.obopstest.web.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class PTstateChangeForcePage {

    private static Logger LOG = LoggerFactory.getLogger(PTstateChangeForcePage.class);


    public static void waitForPageLoaded(WebDriverExtended driver) {
        WaitUtil.waitForElementIsDisplayed(driver, By.xpath("//div[@class=\"z-window-header\"][contains(text(),\"Manual state changer\")]"), 30);
    }

    /**
     * @param driver
     * @param entity   {project, ous, sb}
     * @param atribute { id, target_state, flag, subsystem, action}
     * @return WebElement {input, select}
     */
    public static WebElement getEntityAtribute(WebDriverExtended driver, String entity, String atribute) {
        String entityLabel = null;

        switch (entity) {
            case "project":
                entityLabel = "Project";
                break;
            case "ous":
                entityLabel = "ObsUnitSet";
                break;
            case "sb":
                entityLabel = "SchedBlocks";
                break;
        }

        String xpath = "//span[@class=\"z-label\"][contains(text(),\"" + entityLabel + "\")]/../../../td/div/*";
        Wait.waitForElement(driver, By.xpath(xpath));
        List<WebElement> options = driver.findElementsAndWait(By.xpath(xpath));
        WebElement elem = null;

        switch (atribute) {
            case "id":
                elem = options.get(1);
                break;
            case "target_state":
                elem = options.get(2);
                break;
            case "flag":
                elem = options.get(3);
                break;
            case "subsystem":
                elem = options.get(4);
                break;
            case "action":
                elem = options.get(5);
                break;
        }
        return elem;
    }

    public static void selectOusTargetStateOption(WebDriverExtended driver, String option) {
        getEntityAtribute(driver, "ous", "target_state").click();
        Select dropDown = new Select(getEntityAtribute(driver, "ous", "target_state"));
        dropDown.selectByVisibleText(option);

    }

    public static void selectOusFlag(WebDriverExtended driver, String option) {
        getEntityAtribute(driver, "ous", "flag").click();
        Select dropDown = new Select(getEntityAtribute(driver, "ous", "flag"));
        dropDown.selectByVisibleText(option);

    }

    public static WebElement ousChangeButton(WebDriverExtended driver){
        return getEntityAtribute(driver,"ous","action");
    }

    public static void messageBoxWindowOk(WebDriverExtended driver){
        //class="z-messagebox-window  z-window z-window-highlighted z-window-shadow"
        String xpathSuccess = "//span[@class=\"z-label\"][contains(text(),\"Successfully changed state\")]";
        String xpathOkButton = "//button[contains(text(),\"OK\")]";

        WaitUtil.waitForElementIsDisplayedNoException(driver, By.xpath(xpathSuccess),30);
//        WaitUtil.sleep(10000);
        driver.findElementAndWait(By.xpath(xpathOkButton)).click();

    }
}
