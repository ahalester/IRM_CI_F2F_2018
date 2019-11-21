package de.obopstest.web.pageobjects;

import de.obopstest.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by bdumitru on 7/13/2017.
 */
public class QA0Page {

    public static WebElement qa0MainView(WebDriverExtended driver) {
        return GeneralPage.label(driver, "QA0 AQUA");
    }

    public static WebElement qa2Tab(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_37"));
    }

    public static WebElement searchEBTabs(WebDriverExtended driver, String tabName) {
        WebElement element = null;
        List<WebElement> tabs = driver.findElements(By.className("z-tab"));
        for (WebElement tab : tabs) {
            if (tab.findElement(By.tagName("a")).findElement(By.tagName("span"))
                    .getAttribute("innerHTML").equalsIgnoreCase(tabName)) {
                element = tab;
                break;
            }
        }
        return element;
    }

    private static List<WebElement> dateBoxes(WebDriverExtended driver) {
        return driver.findElements(By.className("z-datebox"));
    }

    public static WebElement dateBox(WebDriverExtended driver, String value) {
        WebElement element = null;
        for (WebElement elem : dateBoxes(driver)) {

            if (elem.findElement(By.className("z-datebox-input"))
                    .getAttribute("value").contains(value)) {
                element = elem.findElement(By.className("z-datebox-input"));
                break;
            } else {
                element = dateBoxes(driver).get(1);
            }
        }
        return element;
    }

    private static List<WebElement> buttons(WebDriverExtended driver) {
        return driver.findElements(By.className("z-button"));
    }

    public static WebElement button(WebDriverExtended driver, String buttonName) {
        WebElement element = null;
        for (WebElement elem : buttons(driver)) {

            if (elem.getAttribute("innerHTML").equalsIgnoreCase(buttonName)) {
                element = elem;
                break;
            }
        }
        return element;
    }

    public static WebElement selectEBByIndex(WebDriverExtended driver, String index) {
        return driver.findElement(By.className("z-listbox-body"))
                .findElements(By.tagName("tr")).get(Integer.parseInt(index));
    }

    public static WebElement selectFirstEB(WebDriverExtended driver) {
        return selectEBByIndex(driver, "0");
    }
}
