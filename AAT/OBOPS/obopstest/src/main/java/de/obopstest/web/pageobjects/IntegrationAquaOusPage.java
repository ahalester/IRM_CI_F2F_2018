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


public class IntegrationAquaOusPage {

    private static Logger LOG = LoggerFactory.getLogger(IntegrationAquaOusPage.class);


    public static void waitForPageLoaded(WebDriverExtended driver) {
        String xpath = "//button[text()=\"QA2 Status history\"]";
        //        WaitUtil.waitForElementIsDisplayedNoException(driver, By.xpath(xpath),30);
        WaitUtil.waitForElementIsDisplayed(driver, By.xpath(xpath), 30);
    }

    public static WebElement doQa2Button(WebDriverExtended driver){
        String xpath = "//button[text()=\"Do QA2\"]";
        return driver.findElementAndWait(By.xpath(xpath));
    }

}
