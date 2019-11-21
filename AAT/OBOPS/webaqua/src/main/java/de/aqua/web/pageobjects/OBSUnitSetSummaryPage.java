package de.aqua.web.pageobjects;

import de.aqua.web.delegates.WebDriverExtended;
import de.aqua.web.utils.PropertiesFileUtil;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static de.aqua.web.pageobjects.GeneralPage.identifyDifferentTypeElementByTagLabelFromZhBox;
import static de.aqua.web.pageobjects.GeneralPage.visibleElements;
import static de.aqua.web.utils.PropertiesFileUtil.getElementName;

/**
 * Created by bdumitru on 7/14/2017.
 */
@SuppressWarnings("unused")
public class OBSUnitSetSummaryPage {

    public static WebElement summary(WebDriverExtended driver) {
        return GeneralPage.specificWebElemFromList(driver, "z-tab-text",
                getElementName("ous_summary"));
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
        return GeneralPage.identifySameTypeElementByTagLabelContainingText(driver, "z-center",
                "z-label", tagName);
    }

    private static List<WebElement> ousButtons(WebDriverExtended driver) {
        return driver.findElements(By.className("z-button"));
    }

    public static WebElement button(WebDriverExtended driver, String buttonName) {
        return GeneralPage.specificWebElemFromList(driver, "z-button", buttonName);
    }

    public static WebElement doQA2Form(WebDriverExtended driver) {
        return driver.findElement(By.xpath(".//*[@class='z-window z-window-noborder "
                + "z-window-overlapped z-window-shadow']"));
    }

    public static WebElement setQA2Status(WebDriverExtended driver) {
        return doQA2Form(driver).findElement(By.className("z-combobox-button"));
    }

    public static WebElement selectStatus(WebDriverExtended driver, String text) {
        WebElement element = null;
//        List<WebElement> elements = driver.findElementAndWait(By.className("z-combobox-content"))
//                .findElements(By.className("z-comboitem-text"));
        List<WebElement> elements = GeneralPage.visibleElements(driver,
                "z-combobox-content").get(0).findElements(By.className("z-comboitem-text"));
        for (WebElement elem : elements) {
            if ((elem.getAttribute("innerHTML")).replaceAll("&nbsp;", " ")
                    .contains(text)) {
                //  if (StringEscapeUtils.unescapeHtml(elem.getAttribute("innerHTML")).equalsIgnoreCase(text)) {
                element = elem;
                break;
            }
        }
        return element;
    }

    public static WebElement doQA2FormButton(WebDriverExtended driver, String buttonName) {
        //  return GeneralPage.specificWebElemFromList(driver, "z-button", buttonName);
        return GeneralPage.childWebElemFromWebElement(driver, GeneralPage.doQAXForm(driver),
                "z-button", buttonName);
    }

    public static WebElement doQA2FormStatusReason(WebDriverExtended driver) {
        return doQA2Form(driver).findElement(By.className("z-chosenbox"));
    }

    public static WebElement doQA2FormStatusReasonList(WebDriverExtended driver) {
        return driver.findElement(By.className("z-combobox-content"));
    }

    /**
     * Reason status option by its specific name.
     *
     * @param driver     WebDriver
     * @param statusName status name
     * @return reason status option by its specific name
     */
    public static WebElement doQA2FormReasonStatusOptionName(WebDriverExtended driver,
                                                             String statusName) {
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
     * Specific reason status option selected by list index.
     *
     * @param driver WebDriver
     * @param index  item index
     * @return reason status option by its specific list index
     */
    public static WebElement doQA2FormReasonStatusOptionPosition(WebDriverExtended driver,
                                                                 String index) {
        WebElement element = null;
        List<WebElement> visibleOptions = GeneralPage.visibleElements(driver,
                "z-chosenbox-option");
        for (int i = 0; i < visibleOptions.size(); i++) {
            if (i == Integer.parseInt(index)) {
                element = visibleOptions.get(i);
                break;
            }
        }
        return element;
    }

    /**
     * First reason status option.
     *
     * @param driver WebDriver
     * @return first reason status option
     */
    public static WebElement doQA2FormReasonStatusOptionPosition(WebDriverExtended driver) {
        return doQA2FormReasonStatusOptionPosition(driver, "0");
    }

    public static WebElement doQA2FormEC(WebDriverExtended driver) {
        return doQA2Form(driver).findElement(By.className("z-doublebox"));
    }

    public static WebElement doQA2FormECOriginalValue(WebDriverExtended driver) {
        return GeneralPage.identifySameTypeElementByTagLabelContainingText(driver, "z-label",
                "Original value:");
    }

    public static WebElement doQA2FormComment(WebDriverExtended driver, String labelName) {
        WebElement field = null;
        List<WebElement> visibleFields = GeneralPage.visibleElements(driver, "z-textbox");
        for (WebElement vf : visibleFields) {
            if (vf.getTagName().equals("textarea")
                    && Integer.parseInt(vf.getAttribute("rows")) >= 10) {
                field = vf;
                break;
            }
        }
        return field;
    }

    public static WebElement stateChangeMessageBox(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_19509-real"));
    }

    public static WebElement stateChangeMessageBoxButton(WebDriverExtended driver, String btnName) {
        WebElement element = null;
        List<WebElement> buttons = stateChangeMessageBox(driver)
                .findElements(By.className("z-messagebox-button"));
        for (WebElement btn : buttons) {
            if (btn.getAttribute("innerHTML").equalsIgnoreCase(btnName)) {
                element = btn;
                break;
            }
        }
        return element;
    }

    public static WebElement confirmationBoxMessage(WebDriverExtended driver) {
        return GeneralPage.dialogBox(driver).findElement(By.className("z-label"));
    }

    public static WebElement confirmationBoxMessage(WebDriverExtended driver, WebElement we) {
        return we.findElement(By.className("z-label"));
    }

    public static WebElement infoECValue(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_19711"));
    }

    public static WebElement infoECokBtn(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_19706"));
    }

    public static WebElement toolBarButton(WebDriverExtended driver, String btnName) {
        return GeneralPage
                .specificWebElemByContainedText(driver, "z-toolbarbutton-content", btnName);
    }

    public static WebElement clickToAdd(WebDriverExtended driver, String labelLocator) {
        return GeneralPage.identifyDifferentTypeElementByTagLabelFromZrow(driver, "z-label",
                "z-toolbarbutton", labelLocator);
    }

    public static WebElement searchDataReducerIcon(WebDriverExtended driver) {
        return driver.findElement(By.className("z-icon-search"));
    }

    public static WebElement searchDataReducerTextField(WebDriverExtended driver) {
        return driver.findElement(By.className("z-bandbox-input"));
    }

    private static List<WebElement> dataReducerListBox(WebDriverExtended driver) {
        return GeneralPage.visibleElements(driver, "z-listbox");
    }

    @Nullable
    public static WebElement selectDataReducerByIndex(WebDriverExtended driver, String index) {
        List<WebElement> drl = dataReducerListBox(driver);
        for (WebElement we : drl) {
            if (we.getAttribute("innerHTML").contains("Lastname")
                    && we.getAttribute("innerHTML").contains("Firstname")) {
                return we.findElements(By.className("z-listitem")).get(Integer.parseInt(index));
            }
        }
        return null;
    }


    public static WebElement selectDataReducer(WebDriverExtended driver, String dr) {
        List<WebElement> cells = GeneralPage.visibleElements(driver, "z-listcell-content");
        WebElement we = null;
        for (WebElement cell : cells) {
            if (cell.getAttribute("innerHTML").contains(dr)) {
                we = cell;
                break;
            }
        }
        return we;
    }

    public static WebElement selectFirstDataReducer(WebDriverExtended driver) {
        return selectDataReducerByIndex(driver, "0");
    }

    public static WebElement plProcessingExec(WebDriverExtended driver, String tagLabel) {
        return GeneralPage.identifyElementFromSpecificPageArea(driver, "z-center",
                "z-label", tagLabel);
    }

    public static WebElement editComment(WebDriverExtended driver, String commentToEdit) {
        return GeneralPage.elementContainingSpecificText(driver, "z-row", commentToEdit)
                .findElement(By.xpath(".//*[@class='z-image' and "
                        + "@src='/qa2/images/edit-16.png']"));
    }

    public static WebElement weblogDownload(WebDriverExtended driver, String weblog) {
        return driver.findElement(By.xpath(".//*[@title='" + weblog + "']"));
    }

    public static WebElement dataReducerPopup(WebDriverExtended driver) {
        WebElement element = null;
        List<WebElement> popupList = GeneralPage.visibleElements(driver, "z-popup-content");
        for (WebElement popup : popupList) {
            if (popup.getAttribute("innerHTML").contains("Data Reducer")) {
                element = popup;
                break;
            }
        }
        return element;
    }

    public static WebElement commentRecipientPopup(WebDriverExtended driver) {
        WebElement element = null;
        List<WebElement> popupList = GeneralPage.visibleElements(driver, "z-popup-content");
        for (WebElement popup : popupList) {
            if (popup.getAttribute("innerHTML").contains("New recipient")) {
                element = popup;
                break;
            }
        }
        return element;
    }

    public static WebElement dataReducerPopupButton(WebDriverExtended driver, String buttonName) {
        return GeneralPage.childWebElemFromWebElement(driver, dataReducerPopup(driver),
                "z-button", buttonName);
    }

    public static WebElement commentRecipientsPopupButton(WebDriverExtended driver, String
            buttonName) {
        return GeneralPage.childWebElemFromWebElement(driver, commentRecipientPopup(driver),
                "z-button", buttonName);
    }
    public static WebElement drm(WebDriverExtended driver, String labelName) {
        List<WebElement> cells = GeneralPage.visibleElements(driver, "z-cell");
        WebElement we = null;
        for (WebElement cell : cells) {
            if (cell.findElement(By.className("z-label")).getAttribute("innerHTML").contains("DRM")) {
                we = cell.findElement(By.className("z-toolbarbutton"));
                break;
            }
        }
        return we;
    }

}
