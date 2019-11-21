package de.obopstest.web.pageobjects.snoopi;

import de.obopstest.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class MySbsPage {

    public static final int SBS_PRJ_CODE = 1;
    public static final int SBS_NAME = 2;
    public static final int SBS_UID = 3;
    public static final int SBS_STATUS = 4;
    public static final int SBS_EXECUTION = 5;

    public static WebElement sbsPageLabel(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//div[@class=\"widget-header\"]/div[contains(text(),\"Scheduling blocks\")]"));
    }

    public static WebElement piTab(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//a[@ng-reflect-router-link=\"/sched-blocks/pi/\"]/.."));
    }

    public static WebElement coiTab(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//a[@ng-reflect-router-link=\"/sched-blocks/coi/\"]/.."));
    }

    public static WebElement allSbsCheckBox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("sbfiltercheckbox"));
    }

    public static WebElement sbsSearchBox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("sbl-search-text"));
    }

    public static WebElement sbsSearchButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("sbl-search-btn"));
    }


    //table
    public static WebElement sbsTable(WebDriverExtended driver) {
        return driver.findElementAndWait(By.name("schedBlocksTable"));
    }

    public static List<WebElement> sbsTableRows(WebDriverExtended driver) {
        return driver.findElementsAndWait(By.xpath("//*[@name='schedBlocksTable']/tbody/tr"));
    }

    public static List<WebElement> getTableCellsByColumn(WebDriverExtended driver, int column){
        String xpath = "//*[@name='schedBlocksTable']/tbody/tr/td["+ column +"]";
        return driver.findElementsAndWait(By.xpath(xpath));
    }

    public static WebElement getTableCellByColRow(WebDriverExtended driver, int column, int row){
        String xpath = "//*[@name='schedBlocksTable']/tbody/tr/td["+ column +"]";
        return driver.findElementsAndWait(By.xpath(xpath)).get(row);
    }

    public static WebElement getTableCellByColRowLink(WebDriverExtended driver, int column, int row){
        String xpath = "//*[@name='schedBlocksTable']/tbody/tr/td["+ column +"]";
        return driver.findElementsAndWait(By.xpath(xpath)).get(row).findElement(By.xpath("a"));
    }

    public static List<String> getAllSbsNames(WebDriverExtended driver){
        List<WebElement> elems = getTableCellsByColumn(driver,SBS_NAME);
        List<String> codes = new ArrayList<String>();

        for(WebElement e: elems){
            codes.add(e.findElement(By.xpath("a/span")).getText());
        }
        return codes;
    }

    public static List<String> getAllSbsUids(WebDriverExtended driver){
        List<WebElement> elems = getTableCellsByColumn(driver,SBS_UID);
        List<String> codes = new ArrayList<String>();

        for(WebElement e: elems){
            codes.add(e.findElement(By.xpath("a/span")).getText());
        }
        return codes;
    }

    public static int getRowIndexBySbsName(WebDriverExtended driver, String name){
        List<String> codes = getAllSbsNames(driver);
        int index = 0;
        for(String c: codes){
            if(c.equalsIgnoreCase(name))
                index++;
        }
        return index;
    }

    public static int getRowIndexBySbsUid(WebDriverExtended driver, String uid){
        List<String> codes = getAllSbsUids(driver);
        int index = 0;
        for(String c: codes){
            if(c.equalsIgnoreCase(uid))
                index++;
        }
        return index;
    }

    public static boolean isPiTabSelected(WebDriverExtended driver) {
        return (piTab(driver).getAttribute("class").contains("active"));
    }

    public static boolean isCoiTabSelected(WebDriverExtended driver) {
        return (coiTab(driver).getAttribute("class").toString().contains("active"));
    }




}


