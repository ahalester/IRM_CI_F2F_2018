package de.aqua.web.pageobjects;

import de.aqua.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by bdumitru on 7/13/2017.
 */
public class QA2Page {

    public static WebElement qa2MainView(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_166"));
    }

    public static WebElement genericCheckbox(WebDriverExtended driver, String locator, String
            name) {
        return driver.findElement(By.xpath(".//*[@" + locator + "='" + name + "']"));
    }

    public static WebElement arrayCheckbox(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_208-real"));
    }

    public static WebElement tableElement(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_209-real"));
    }

    public static WebElement TMCheckbox(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_210-real"));
    }

    public static WebElement SevenMCheckbox(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_211-real"));
    }

    public static WebElement noObsUnitSetDisplayed(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_167-empty"));
    }

    public static WebElement searchButton(WebDriverExtended driver) {
        List<WebElement> buttons = GeneralPage.visibleElements(driver, "z-button");
        return GeneralPage.specificWebElemFromList(driver, buttons, "Search");
    }

    public static WebElement selectObsUnitSet(WebDriverExtended driver, String elemDetails) {
        WebElement elem = null;
        List<WebElement> elements = driver.findElement(By.id("zk_c_167-rows")).
                findElements(By.xpath(".//div[@class='z-listcell-content']"));
        for (WebElement element : elements) {
            if (element.findElement(By.className("z-label")).getAttribute("innerHTML")
                    .equalsIgnoreCase(elemDetails)) {
                elem = element;
                break;
            }
        }
        return elem;
    }

    public static WebElement selectObsUnitSetByIndex(WebDriverExtended driver, String index) {
        return driver.findElement(By.id("zk_c_167-rows")).
                findElements(By.tagName("tr")).get(Integer.parseInt(index));
    }

    public static WebElement selectFirstObsUnitSet(WebDriverExtended driver) {
        return selectObsUnitSetByIndex(driver, "0");
    }

    private static List<WebElement> zRows(WebDriverExtended driver) {
        return driver.findElements(By.className("z-row"));
    }

    public static WebElement textBox(WebDriverExtended driver, String labelName) {
        WebElement element = null;
        for (WebElement elem : zRows(driver)) {

            if (elem.findElement(By.className("z-row-content")).findElement(By.className("z-label"))
                    .getAttribute("innerHTML").equalsIgnoreCase(labelName)) {
                element = elem.findElement(By.className("z-textbox"));
                break;
            }
        }
        return element;
    }

    private static WebElement searchTabPanel(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_184"));
    }

    public static WebElement ousStateFlagField(WebDriverExtended driver, String labelName) {
        return GeneralPage.identifyDifferentTypeElementByTagLabel(driver, "z-label", "z-chosenbox", labelName);
    }

    public static WebElement ousStateFlagOptionList(WebDriverExtended driver) {
        return GeneralPage.visibleElements(driver, "z-chosenbox-select").get(0);
    }

    public static WebElement ousStateFlagOption(WebDriverExtended driver, String optionName) {
        WebElement element = null;
        List<WebElement> options = ousStateFlagOptionList(driver).findElements(By.className("z-chosenbox-option"));
        for (WebElement option : options) {
            if (option.getAttribute("innerHTML").equalsIgnoreCase(optionName)) {
                element = option;
                break;
            }
        }
        return element;
    }

    public static WebElement selectedOUSStateFlagOption(WebDriverExtended driver, String labelName) {
        return ousStateFlagField(driver, labelName).findElement(By.className("z-chosenbox"));
    }

    public static WebElement advancedOUSTextField(WebDriverExtended driver, String fieldName) {
        return GeneralPage.identifyDifferentTypeElementByTagLabel(driver, "z-label", "z-textbox", fieldName);
    }

}
