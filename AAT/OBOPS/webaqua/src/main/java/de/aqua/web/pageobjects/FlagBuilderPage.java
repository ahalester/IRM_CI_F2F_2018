package de.aqua.web.pageobjects;

import de.aqua.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class FlagBuilderPage {

    public static WebElement flagBuilderPopup(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath(".//*[@class='z-window z-window-noborder "
                + "z-window-modal z-window-shadow']"));
    }

    public static WebElement groupBox3D(WebDriverExtended driver, String labelName) {
        WebElement element = null;
        List<WebElement> boxes = GeneralPage.visibleElements(driver, "z-window-modal",
                "z-groupbox-header");
        for (WebElement box : boxes) {
            if (box.getAttribute("innerHTML").contains(labelName)) {
                element = box;
                break;
            }
        }
        return element;
    }

    public static WebElement comboBoxByLabelAndIndex(WebDriverExtended driver, String labelName,
                                                     int index) {
        WebElement element = null;
        List<WebElement> boxes = GeneralPage.visibleElements(driver, "z-window-modal",
                "z-groupbox-3d");
        for (WebElement box : boxes) {
            if (box.getAttribute("innerHTML").contains(labelName)) {
                element = box.findElements(By.className("z-combobox-button")).get(index);
                break;
            }
        }
        return element;
    }

    public static List<WebElement> savedFlagsByLabelAndIndex(WebDriverExtended driver, String
            labelName) {
        List<WebElement> flags = new ArrayList<>();
        List<WebElement> boxes = GeneralPage.visibleElements(driver, "z-window-modal",
                "z-groupbox-3d");
        for (WebElement box : boxes) {
            if (box.getAttribute("innerHTML").contains(labelName)) {
                flags = box.findElement(By.className("z-groupbox-content"))
                        .findElements(By.className("z-listitem"));
                break;
            }
        }
        return flags;
    }

    public static WebElement deleteFlagByLabelAndIndex(WebDriverExtended driver, String
            labelName) {
        WebElement flag = null;
        List<WebElement> boxes = GeneralPage.visibleElements(driver, "z-window-modal",
                "z-groupbox-3d");
        for (WebElement box : boxes) {
            if (box.getAttribute("innerHTML").contains(labelName)) {
                List<WebElement> flags = box.findElement(By.className("z-groupbox-content"))
                        .findElements(By.className("z-listitem"));
                flag = flags.get(flags.size() - 1)
                        .findElement(By.xpath(".//*[@class='z-toolbarbutton' and @title='Remove flag']"));
                break;
            }
        }
        return flag;
    }

    public static WebElement comboBoxItemByLabelAndIndex(WebDriverExtended driver, String labelName,
                                                         int index) {
        WebElement element = null;
        List<WebElement> boxes = GeneralPage.visibleElements(driver, "z-window-modal",
                "z-groupbox-3d");
        for (WebElement box : boxes) {
            if (box.getAttribute("innerHTML").contains(labelName)) {
                element = box.findElements(By.xpath(".//*[@class='item z-checkbox']")).get(index);
                break;
            }
        }
        return element;
    }

    public static WebElement comboBoxItemByValue(WebDriverExtended driver, String value) {
        WebElement element = null;
        List<WebElement> comboItems = GeneralPage.visibleElements(driver, "z-comboitem");
        for (WebElement item : comboItems) {
            if (item.getAttribute("innerHTML").contains(value)) {
                element = item;
                break;
            }
        }
        return element;
    }

    public static WebElement spwDiv(WebDriverExtended driver, String index) {
        WebElement element = null;
        List<WebElement> comboItems = GeneralPage.visibleElements(driver, "xpath", "*",
                "item z-div");
        for (WebElement item : comboItems) {
            if (item.getAttribute("innerHTML").contains(index)) {
                element = item;
                break;
            }
        }
        return element;
    }

    public static WebElement spwDivCheckBox(WebDriverExtended driver, String index) {
        return spwDiv(driver, index).findElement(By.className("z-checkbox"));
    }

    public static WebElement spwDivToolbarBtn(WebDriverExtended driver, String index) {
        return spwDiv(driver, index).findElement(By.className("z-toolbarbutton"));
    }

    public static WebElement popupContent(WebDriverExtended driver) {
        return GeneralPage.visibleElements(driver, "z-popup-content").get(0);
    }

    public static List<WebElement> intBoxFields(WebDriverExtended driver) {
        return popupContent(driver).findElements(By.className("z-intbox"));
    }

    public static WebElement builderAreaOption(WebDriverExtended driver, String index) {
        WebElement element = null;
        List<WebElement> comboItems = GeneralPage.visibleElements(driver, "xpath", "*",
                "item z-checkbox");
        for (WebElement item : comboItems) {
            if (item.getAttribute("innerHTML").contains(index)) {
                element = item.findElement(By.tagName("input"));
                break;
            }
        }
        return element;
    }
}
