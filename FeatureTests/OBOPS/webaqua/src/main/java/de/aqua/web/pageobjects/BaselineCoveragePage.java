package de.aqua.web.pageobjects;

import de.aqua.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by bdumitru on 8/4/2017.
 */
@SuppressWarnings("unused")
public class BaselineCoveragePage {

    private static List<WebElement> legendItems(WebDriverExtended driver) {
        return GeneralPage.visibleElements(driver, "jqplot-table-legend");
    }

    public static WebElement legendLabel(WebDriverExtended driver, String labelName) {
        WebElement element = null;
        for (WebElement elem : legendItems(driver)) {
            if (elem.findElement(By.className("jqplot-table-legend-label")).getText().equals(labelName)) {
                element = elem;
                break;
            }
        }
        return element;
    }

    public static WebElement legendSwatch(WebDriverExtended driver, String labelName) {
        WebElement element = null;
        for (WebElement elem : legendItems(driver)) {
            if (elem.findElement(By.className("jqplot-table-legend-label")).getText().equals(labelName)) {
                element = elem.findElement(By.xpath(".//div[@class='jqplot-table-legend-swatch']"));
                break;
            }
        }
        return element;
    }
}
