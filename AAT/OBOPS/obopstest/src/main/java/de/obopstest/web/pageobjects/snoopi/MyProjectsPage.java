package de.obopstest.web.pageobjects.snoopi;

import de.obopstest.web.delegates.WebDriverExtended;
import jline.internal.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MyProjectsPage {
    private static Logger LOG = LoggerFactory.getLogger(MyProjectsPage.class);

    public static final int PRJ_CODE = 1;
    public static final int PRJ_ICON = 2;
    public static final int PRJ_TITLE = 3;
    public static final int PRJ_STATUS = 4;
    public static final int PRJ_GRADE = 5;


    public static WebElement projectsPageLabel(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//div[@class=\"widget-header\"]/div[contains(text(),\"Projects\")]"));
    }

    public static WebElement piTab(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("pl-pi-tab"));
    }

    public static WebElement coiTab(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("pl-coi-tab"));
    }

    public static WebElement allProjectsCheckBox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("prjfiltercheckbox"));
    }

    public static WebElement projectsSearchBox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("pl-search-txt"));
    }

    public static WebElement projectsSearchButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("pl-search-btn"));
    }

    //table
    public static WebElement projectsTable(WebDriverExtended driver) {
        return driver.findElementAndWait(By.name("projectsTable"));
    }

    public static List<WebElement> projectsTableRows(WebDriverExtended driver) {
        return driver.findElementsAndWait(By.xpath("//*[@name='projectsTable']/tbody/tr"));
    }

    /**
     * @param driver
     * @param column {PRJ_CODE, PRJ_ICON, PRJ_TITLE, PRJ_STATUS, PRJ_GRADE}
     * @return
     */
    public static List<WebElement> getTableCellsByColumn(WebDriverExtended driver, int column) {
        String xpath = "//*[@name='projectsTable']/tbody/tr/td[" + column + "]";
        return driver.findElementsAndWait(By.xpath(xpath));
    }

    public static List<String> getAllPrjCodes(WebDriverExtended driver) {
        List<WebElement> elems = getTableCellsByColumn(driver, PRJ_CODE);
        List<String> codes = new ArrayList<String>();

        for (WebElement e : elems) {
            codes.add(e.findElement(By.xpath("a/span")).getText());
        }
        return codes;
    }

    public static int getRowIndexByCode(WebDriverExtended driver, String code) {
        List<String> codes = getAllPrjCodes(driver);
        int index = 0;
        for (String c : codes) {
            if (c.equalsIgnoreCase(code))
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

    public static WebElement getTableCellByColRow(WebDriverExtended driver, int column, int row){
        String xpath = "//*[@name='projectsTable']/tbody/tr/td["+ column +"]";
        return driver.findElementsAndWait(By.xpath(xpath)).get(row);
    }

    public static WebElement getTableCellByColRowLink(WebDriverExtended driver, int column, int row){
        String xpath = "//*[@name='projectsTable']/tbody/tr/td["+ column +"]";
        return driver.findElementsAndWait(By.xpath(xpath)).get(row).findElement(By.xpath("a"));
    }

}


