package de.obopstest.web.pageobjects;

import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.utils.Wait;
import de.obopstest.web.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static de.obopstest.web.utils.RuntimeBufferUtil.bufferMap;


public class ProtrackPage {

    private static Logger LOG = LoggerFactory.getLogger(ProtrackPage.class);

    public static WebElement userField(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("username"));
    }

    public static Double sumEF(WebDriverExtended driver, String columnName) {
        Double sum = 0.0;
        List<WebElement> items = GeneralPage.collectGridColumnElements(driver, columnName);
        bufferMap.put("N_EB", String.valueOf(items.size()));
        for (WebElement item : items) {
            sum += Double.parseDouble(item.getAttribute("innerHTML"));
        }
        return sum;
    }

    public static List<WebElement> searchProjectListItem(WebDriverExtended driver){
        return driver.findElements(By.className("z-listitem"));
    }

    public static void closePageConfirm(WebDriverExtended driver){
        ( ( JavascriptExecutor ) driver )
                .executeScript( "window.onbeforeunload = function(e){};" );
    }

    public static  WebElement selectCellByText(WebDriverExtended driver, String text){
        String xpath = "//div[@class=\"z-listcell-content\"][contains(text(),\"" + text + "\")]";
        Wait.waitForElement(driver, By.xpath(xpath) );
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        driver.executeScript("arguments[0].scrollIntoView(true);", elem);
        Wait.sleep(500);
        return driver.findElementAndWait(By.xpath(xpath));
    }

    public static void waitForFinishedLoadingInProgress(WebDriverExtended driver){
//        Wait.waitForElementIsNotPresentCustom(driver,By.className("z-loading-indicator"),2);
        WaitUtil.waitForElementIsNotDisplayed(driver,By.className("z-loading-indicator"),30);
    }

    public static  WebElement protrackSearchOUSButton(WebDriverExtended driver){
        String css = "a[class='z-toolbarbutton'][title='Advanced ObsUnitSet search']";
        Wait.waitForElement(driver, By.cssSelector(css) );
        WebElement elem = driver.findElementAndWait(By.cssSelector(css));
        return elem;
    }

    public static  WebElement protrackSearchSBButton(WebDriverExtended driver){
        String css = "a[class='z-toolbarbutton'][title='Advanced Sched Block search']";
        Wait.waitForElement(driver, By.cssSelector(css) );
        WebElement elem = driver.findElementAndWait(By.cssSelector(css));
        return elem;
    }

    public static void waitForProtrackOusSearchTab(WebDriverExtended driver){
        WaitUtil.waitForElementIsDisplayed(driver,By.xpath("//div[@class=\"z-caption-content\"][contains(text(),\" ObsUnitSet search\")]"),30);
    }

    public static  WebElement ousStatusUidField(WebDriverExtended driver){
        String css = "input[class='z-textbox'][title*='Search by OUS Status UID']";
        Wait.waitForElement(driver, By.cssSelector(css) );
        WebElement elem = driver.findElementAndWait(By.cssSelector(css));
        return elem;
    }

    public static  WebElement ousStatusSbIntervalFrom(WebDriverExtended driver){
        String css = "input[class='z-datebox-input']";
        Wait.waitForElement(driver, By.cssSelector(css) );
        List<WebElement> elems = driver.findElementsAndWait(By.cssSelector(css));
        WebElement elem = elems.get(2);
        return elem;
    }

    public static  WebElement ousStatusSbIntervalTo(WebDriverExtended driver){
        String css = "input[class='z-datebox-input']";
        Wait.waitForElement(driver, By.cssSelector(css) );
        List<WebElement> elems = driver.findElementsAndWait(By.cssSelector(css));
        WebElement elem = elems.get(3);
        return elem;
    }

    public static  WebElement ousSearchButton(WebDriverExtended driver){
        String xpath = "//button[@class='z-button'][contains(text(),'Search')]";
        Wait.waitForElement(driver, By.xpath(xpath) );
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static  WebElement sbSearchButton(WebDriverExtended driver){
        String xpath = "//button[@class='z-button'][contains(text(),'Search')]";
        Wait.waitForElement(driver, By.xpath(xpath) );
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static  List<WebElement> sbSearchResults(WebDriverExtended driver){
        String css = "tr[class='z-listitem']";
        Wait.waitForElement(driver, By.cssSelector(css) );
        List<WebElement> elems = driver.findElementsAndWait(By.cssSelector(css));
        return elems;
    }

//    public static void waitForFinishedSearchingInProgress(WebDriverExtended driver){
////        Wait.waitForElementIsNotPresentCustom(driver,By.className("z-loading-indicator"),2);
//        WaitUtil.waitForElementIsNotDisplayed(driver,By.className("z-searching-indicator"),30);
//    }

    public static  WebElement ousUidOnEntityPage(WebDriverExtended driver, String uid){
        String xpath = "//span[@class='z-label'][contains(text(),'" + uid + "')]";
        Wait.waitForElement(driver, By.xpath(xpath) );
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static  WebElement ousStateInProtrackElement(WebDriverExtended driver){

        WebElement elem = GeneralPage.identifySameTypeElementByTagLabelExactText(driver,"z-label","State");
        return elem;
    }

    public static WebElement ousChamgeStateIcon(WebDriverExtended driver){
        String xpath = "//img[@class='z-image'][contains(@src,'/image/Settings_16x16.png')]";
        Wait.waitForElement(driver, By.xpath(xpath) );
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static WebElement ousStateTransitionInProtrack
            (WebDriverExtended driver, String initialState, String targetState){
        String xpath = "//span[@class='z-menuitem-text'][contains(text(),'" + initialState + "')][contains(text(),'" + targetState + "')]";
        Wait.waitForElement(driver, By.xpath(xpath) );
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static  WebElement ousStateChangeCommentInProtrackTextarea(WebDriverExtended driver){
        String xpath = "//textarea[@class='z-textbox']";
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static  WebElement ousStateChangeConfirmOK(WebDriverExtended driver){
        String xpath = "//button[@class='z-button'][contains(text(),'OK')]";
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }

    public static  String ousStateInProtrackText(WebDriverExtended driver){
        WebElement stateElem = ousStateInProtrackElement(driver);
        WebElement text = stateElem.findElement(By.xpath("//span[@class='z-label']"));
        return text.getText();
    }

    public static WebElement ousStateInProtrack(WebDriverExtended driver, String state){
        String xpath = "//span[@class='z-label'][contains(text(),'" + state + "')]";
        Wait.waitForElement(driver, By.xpath(xpath) );
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
}

    public static WebElement ousStateHistoryInProtrack(WebDriverExtended driver, String text){
        String xpath = "//div[@class='z-listcell-content'][contains(text(),'" + text + "')]";
        Wait.waitForElement(driver, By.xpath(xpath) );
        WebElement elem = driver.findElementAndWait(By.xpath(xpath));
        return elem;
    }



}
