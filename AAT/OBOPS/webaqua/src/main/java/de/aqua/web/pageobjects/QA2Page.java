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
        if (driver.getCurrentUrl().contains("eso.org")) {
            return GeneralPage.headerMenuTab(driver, "QA2");
        } else {
            return GeneralPage.label(driver, "QA2 AQUA");
        }
    }

    public static WebElement genericCheckbox(WebDriverExtended driver, String locator, String
            name) {
        return driver.findElement(By.xpath(".//*[@" + locator + "='" + name + "']"));
    }

    public static WebElement searchButton(WebDriverExtended driver) {
        List<WebElement> buttons = GeneralPage.visibleElements(driver, "z-button");
        return GeneralPage.specificWebElemFromList(driver, buttons, "Search");
    }

    public static WebElement selectObsUnitSet(WebDriverExtended driver, String elemDetails) {
        WebElement elem = null;
        List<WebElement> elements = driver.findElement(By.id("zk_c_167-rows"))
                .findElements(By.xpath(".//div[@class='z-listcell-content']"));
        for (WebElement element : elements) {
            if (element.findElement(By.className("z-label")).getAttribute("innerHTML")
                    .equalsIgnoreCase(elemDetails)) {
                elem = element;
                break;
            }
        }
        return elem;
    }

    public static List<WebElement> allAvailableOUSes(WebDriverExtended driver) {
        return GeneralPage.visibleElements(driver, "z-listbox-body")
                .get(0).findElements(By.tagName("tr"));
    }

    public static WebElement selectObsUnitSetByIndex(WebDriverExtended driver, String index) {
        List<WebElement> elemList = GeneralPage.visibleElements(driver, "z-listbox-body");
        return (elemList.get(0).findElements(By.className("z-listitem")))
                .get(Integer.parseInt(index)).findElement(By.className("z-listcell-content"));
    }

    public static WebElement selectFirstObsUnitSet(WebDriverExtended driver) {
        return selectObsUnitSetByIndex(driver, "0");
    }

    public static WebElement ousStateFlagField(WebDriverExtended driver, String labelName) {
        return GeneralPage.identifyDifferentTypeElementByTagLabelFromZrow(driver, "z-label",
                "z-chosenbox", labelName);
    }

    public static WebElement ousOptionList(WebDriverExtended driver) {
        return GeneralPage.visibleElements(driver, "z-chosenbox-select").get(0);
    }

    public static WebElement ousStateFlagOption(WebDriverExtended driver, String optionName) {
        WebElement element = null;
        List<WebElement> options = ousOptionList(driver)
                .findElements(By.className("z-chosenbox-option"));
        for (WebElement option : options) {
            if (option.getAttribute("innerHTML").equalsIgnoreCase(optionName)) {
                element = option;
                break;
            }
        }
        return element;
    }

    public static WebElement advancedOUSTextField(WebDriverExtended driver, String fieldName) {
        return GeneralPage.identifyDifferentTypeElementByTagLabelFromZrow(driver, "z-label",
                "z-textbox", fieldName);
    }

    public static WebElement advancedOUSComboField(WebDriverExtended driver, String fieldName) {
        return GeneralPage.identifyDifferentTypeElementByTagLabelFromZrow(driver, "z-label",
                "z-combobox-input", fieldName);
    }

}
