package de.obopstest.web.pageobjects;

import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.utils.Wait;
import de.obopstest.web.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProtrackEntityDetailsPage {

    private static Logger LOG = LoggerFactory.getLogger(ProtrackEntityDetailsPage.class);


    public static  WebElement ousHistoryLastPage(WebDriverExtended driver){
        String xpath = "//div[contains(text(),\"OUS Status History\")]/../../../../../..//i[@class='z-paging-icon z-icon-angle-double-right']";
        Wait.waitForElement(driver, By.xpath(xpath) );
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        driver.executeScript("arguments[0].scrollIntoView(true);", elem);
        Wait.sleep(500);
        return driver.findElementAndWait(By.xpath(xpath));
    }

    public static  WebElement ousHistoryLastPageIfPresent(WebDriverExtended driver){
        String xpath = "//div[contains(text(),\"OUS Status History\")]/../../../../../..//i[@class='z-paging-icon z-icon-angle-double-right']";
        WebElement elem = null;
        try{
            Wait.waitForElement(driver, By.xpath(xpath), 2 );
            elem = driver.findElementAndWait(By.xpath(xpath));
            driver.executeScript("arguments[0].scrollIntoView(true);", elem);
            Wait.sleep(500);
            LOG.info("Pagination found");
        }catch(Exception e){
            LOG.info("No Pagination found");
            elem = null;
        }

        return elem;
    }

    public static  WebElement ousDetailsChildEntityLink(WebDriverExtended driver){
        String xpath = "//tr[@title='Click on a child entity to navigate to it in Protrack']/td[1]";
        Wait.waitForElement(driver, By.xpath(xpath), 2 );
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
//        driver.executeScript("arguments[0].scrollIntoView(true);", elem);
        return elem;
    }

    public static  WebElement sbNavigateToParentButton(WebDriverExtended driver){
        String xpath = "//button[@title='Navigate to the parent MOUS in Protrack']";
        Wait.waitForElement(driver, By.xpath(xpath), 2 );
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static  boolean checkCurrentObsUnitSetStatusUid(WebDriverExtended driver, String uid){
        String xpath = "//span[@class='z-label'][contains(text(),'"+ uid +"')]";
        return (WaitUtil.waitForElement(driver,By.xpath(xpath),5)!=null);
    }

    public static  WebElement ousInAquaLink(WebDriverExtended driver){
        String xpath = "//a[contains(text(),\"In AQUA\")]";
        Wait.waitForElement(driver, By.xpath(xpath), 2 );
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

}
