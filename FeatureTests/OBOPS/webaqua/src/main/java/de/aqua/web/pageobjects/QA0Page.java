package de.aqua.web.pageobjects;

import de.aqua.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by bdumitru on 7/13/2017.
 */
public class QA0Page {

    public static WebElement qa0Page(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_35"));
    }

    public static WebElement userLabel(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_25"));
    }

    public static WebElement qa2Page(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_37"));
    }

    public static WebElement qa0SearchResults(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_40-cave"));
    }

    public static WebElement searchEBTabs(WebDriverExtended driver, String tabName) {
        WebElement element = null;
        List<WebElement> tabs = driver.findElements(By.className("z-tab"));
        for (WebElement tab : tabs) {
            if (tab.findElement(By.tagName("a")).findElement(By.tagName("span")).getAttribute("innerHTML")
                    .equalsIgnoreCase
                            (tabName)) {
                element = tab;
                break;
            }
        }
        return element;
    }

    private static List<WebElement> checkBoxes(WebDriverExtended driver) {
        return driver.findElements(By.className("z-checkbox"));
    }

    public static WebElement checkBox(WebDriverExtended driver, String labelName) {
        WebElement element = null;
        for (WebElement elem : checkBoxes(driver)) {

            if (elem.findElement(By.className("z-checkbox-content"))
                    .getAttribute("innerHTML").equalsIgnoreCase(labelName)) {
                element = elem.findElement(By.xpath(".//*[@type='checkbox']"));
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

    private static List<WebElement> comboBoxes(WebDriverExtended driver) {
        return driver.findElements(By.className("z-combobox"));
    }

    public static WebElement comboBox(WebDriverExtended driver, String value) {
        WebElement element = null;
        for (WebElement elem : comboBoxes(driver)) {

            if (elem.findElement(By.className("z-combobox-input"))
                    .getAttribute("value").contains(value)) {
                element = elem.findElement(By.className("z-combobox-input"));
                break;
            }
        }
        return element;
    }

    private static List<WebElement> zRows(WebDriverExtended driver) {
        return driver.findElements(By.className("z-row"));
    }

    public static WebElement textBox(WebDriverExtended driver, String labelName) {
        WebElement element = null;
        for (WebElement elem : zRows(driver)) {

            if (elem.findElement(By.className("z-label"))
                    .getAttribute("innerHTML").equalsIgnoreCase(labelName)) {
                element = elem.findElement(By.className("z-textbox"));
                break;
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
        return driver.findElement(By.id("zk_c_40-rows")).
                findElements(By.tagName("tr")).get(Integer.parseInt(index));
    }

    public static WebElement selectFirstEB(WebDriverExtended driver) {
        return selectEBByIndex(driver, "0");
    }

}
