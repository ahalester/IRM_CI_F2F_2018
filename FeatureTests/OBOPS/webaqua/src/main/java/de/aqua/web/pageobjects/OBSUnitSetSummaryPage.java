package de.aqua.web.pageobjects;

import de.aqua.web.delegates.WebDriverExtended;
import org.apache.commons.lang.StringEscapeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bdumitru on 7/14/2017.
 */
@SuppressWarnings("unused")
public class OBSUnitSetSummaryPage {

    public static WebElement summary(WebDriverExtended driver) {
        return GeneralPage.specificWebElemFromList(driver, "z-tab-text", "ObsUnitSet Summary");
    }

    private static WebElement ousSummaryDom(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_382"));
    }

    private static List<WebElement> ousLabels(WebDriverExtended driver) {
        return GeneralPage.visibleElements(driver, "z-label");
    }

    public static WebElement label(WebDriverExtended driver, String labelName) {
        WebElement element = null;
        for (WebElement elem : ousLabels(driver)) {

            if (elem.getAttribute("innerHTML").equalsIgnoreCase(labelName)) {
                element = elem;
                break;
            }
        }
        return element;
    }

    public static WebElement getDynamicLabelByTagLabel(WebDriverExtended driver, String tagName) {
        return GeneralPage.identifySameTypeElementByTagLabel(driver, "z-tabpanel",
                "z-label", tagName);
    }

    private static List<WebElement> ousButtons(WebDriverExtended driver) {
//        return ousSummaryDom(driver).findElements(By.className("z-button"));
        return driver.findElements(By.className("z-button"));
    }

    public static WebElement button(WebDriverExtended driver, String buttonName) {
        return GeneralPage.specificWebElemFromList(driver, "z-button", buttonName);
    }

    public static WebElement doQA2Form(WebDriverExtended driver) {
        return driver.findElement(By.xpath(".//*[@class='z-window z-window-noborder z-window-overlapped " +
                "z-window-shadow']"));
    }

    public static WebElement setQA2Status(WebDriverExtended driver) {
        return doQA2Form(driver).findElement(By.className("z-combobox-button"));
    }

    public static WebElement selectStatus(WebDriverExtended driver, String text) {
        WebElement element = null;
        List<WebElement> elements = driver.findElement(By.className("z-combobox-content"))
                .findElements(By.className("z-comboitem-text"));
        for (WebElement elem : elements) {
            if ((elem.getAttribute("innerHTML")).replaceAll("&nbsp;", " ").equalsIgnoreCase(text)) {
//            if (StringEscapeUtils.unescapeHtml(elem.getAttribute("innerHTML")).equalsIgnoreCase(text)) {
                element = elem;
                break;
            }
        }
        return element;
    }

    public static WebElement doQA2FormButton(WebDriverExtended driver, String buttonName) {
//        WebElement element = null;
//        List<WebElement> options = doQA2Form(driver).findElements(By.className("z-button"));
//        for (WebElement option : options) {
//            if (option.getAttribute("innerHTML").equalsIgnoreCase(btnName)) {
//                element = option;
//                break;
//            }
//        }
//        return element;
        return GeneralPage.specificWebElemFromList(driver, "z-button", buttonName);
    }

    public static WebElement doQA2FormStatusReason(WebDriverExtended driver) {
        return doQA2Form(driver).findElement(By.className("z-chosenbox"));
    }

    public static WebElement doQA2FormStatusReasonList(WebDriverExtended driver) {
//        return driver.findElement(By.id("zk_c_2680"));
        return driver.findElement(By.className("z-combobox-content"));
    }

    /**
     * Reason status option by its specific name
     *
     * @param driver
     * @param statusName
     * @return reason status option by its specific name
     */
    public static WebElement doQA2FormReasonStatusOptionName(WebDriverExtended driver, String statusName) {
        WebElement element = null;
        List<WebElement> options = driver.findElements(By.className("z-chosenbox-option"));
        for (WebElement option : options) {
            if (option.getAttribute("innerHTML").contains(statusName)) {
                element = option;
                break;
            }
        }
        return element;
    }

    /**
     * Specific reason status option selected by list index
     *
     * @param driver
     * @param index
     * @return reason status option by its specific list index
     */
    public static WebElement doQA2FormReasonStatusOptionPosition(WebDriverExtended driver, String index) {
        WebElement element = null;
        List<WebElement> visibleOptions = GeneralPage.visibleElements(driver, "z-chosenbox-option");
        for (int i = 0; i < visibleOptions.size(); i++) {
            if (i == Integer.parseInt(index)) {
                element = visibleOptions.get(i);
                break;
            }
        }
        return element;
    }

    /**
     * First reason status option
     *
     * @param driver
     * @return first reason status option
     */
    public static WebElement doQA2FormReasonStatusOptionPosition(WebDriverExtended driver) {
        return doQA2FormReasonStatusOptionPosition(driver, "0");
    }

    public static WebElement doQA2FormEC(WebDriverExtended driver) {
        return doQA2Form(driver).findElement(By.className("z-doublebox"));
    }

    public static WebElement doQA2FormECOriginalValue(WebDriverExtended driver) {
//        WebElement label = null;
//        List<WebElement> visibleLabels = GeneralPage.visibleElements(driver, "z-label");
//        for (int i = 0; i < visibleLabels.size(); i++) {
//            if (visibleLabels.get(i).getAttribute("innerHTML").contains("Original value:")) {
//                label = visibleLabels.get(i + 1);
//                break;
//            }
//        }
//        return label;
        return GeneralPage.identifySameTypeElementByTagLabel(driver, "z-label", "Original value:");
    }

    public static WebElement doQA2FormComment(WebDriverExtended driver, String labelName) {
        WebElement field = null;
        List<WebElement> visibleFields = GeneralPage.visibleElements(driver, "z-textbox");
        for (WebElement vf : visibleFields) {
            if (vf.getTagName().equals("textarea") &&
                    Integer.parseInt(vf.getAttribute("rows")) >= 10) {
                field = vf;
                break;
            }
        }
        return field;
//        return GeneralPage.identifyDifferentTypeElementByTagLabel(driver, "z-label", "z-textbox",
//                labelName);
    }

    public static WebElement stateChangeMessageBox(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_19509-real"));
    }

    public static WebElement stateChangeMessageBoxButton(WebDriverExtended driver, String btnName) {
        WebElement element = null;
        List<WebElement> buttons = stateChangeMessageBox(driver).findElements(By.className("z-messagebox-button"));
        for (WebElement btn : buttons) {
            if (btn.getAttribute("innerHTML").equalsIgnoreCase(btnName)) {
                element = btn;
                break;
            }
        }
        return element;
    }

    public static WebElement confirmationBoxMessage(WebDriverExtended driver) {
//        return driver.findElement(By.id("zk_c_19914"));
        return GeneralPage.dialogBox(driver).findElement(By.className("z-label"));
    }

    public static WebElement infoECValue(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_19711"));
    }

    public static WebElement infoECokBtn(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_19706"));
    }

    public static WebElement stateOUS(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_1508"));
    }

}
