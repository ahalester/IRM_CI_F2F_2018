package cl.apa.web.pageobjects;

import cl.apa.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ApaPipelinePage {

    public static WebElement pipelineRuns(WebDriverExtended driver) {
        return driver.findElement(By.className("indented"));
    }

    private static List<WebElement> tableRows(WebDriverExtended driver) {
        return driver.findElements(By.xpath(".//*[@data-is='dynamic-table-row']"));
    }

    private static WebElement specificTableRow(WebDriverExtended driver, String cellContent) {
        List<WebElement> rows = tableRows(driver);
        WebElement specificRow = null;
        for (WebElement row : rows) {
            if (row.getAttribute("innerHTML").contains(cellContent)) {
                specificRow = row;
                break;
            }
        }
        return specificRow;
    }

    public static WebElement specificRowCell(WebDriverExtended driver, String cellContent, int index) {
        WebElement specificCell = null;
        WebElement tableRow = specificTableRow(driver, cellContent);
        List<WebElement> cells = tableRow.findElements(By.tagName("td"));
        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i).getAttribute("innerHTML").contains(cellContent)) {
                specificCell = cells.get(i + index);
                break;
            }
        }
        return specificCell;
    }

    public static WebElement specificRowInfo(WebDriverExtended driver, String cellContent, int index) {
        WebElement specificCell = null;
        WebElement tableRow = specificTableRow(driver, cellContent);
        List<WebElement> cells = tableRow.findElements(By.tagName("td"));
        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i).getAttribute("innerHTML").contains(cellContent)) {
                specificCell = cells.get(i + index).findElement(By.tagName("span"));
                break;
            }
        }
        return specificCell;
    }

    public static WebElement rowCell(WebDriverExtended driver, String cellContent) {
        WebElement specificCell = null;
        WebElement tableRow = specificTableRow(driver, cellContent);
        List<WebElement> cells = tableRow.findElements(By.tagName("td"));
        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i).getAttribute("innerHTML").contains(cellContent)) {
                specificCell = cells.get(i);
                break;
            }
        }
        return specificCell;
    }

    public static WebElement popupHeader(WebDriverExtended driver) {
        return driver.findElement(By.className("pop-up-header"));
    }

    public static WebElement openWeblog(WebDriverExtended driver) {
        return driver.findElements(By.xpath(".//*[@class='glyphicon glyphicon-new-window']")).get(1);
    }
}
