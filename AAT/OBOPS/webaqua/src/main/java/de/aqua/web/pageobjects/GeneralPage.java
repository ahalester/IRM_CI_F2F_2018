package de.aqua.web.pageobjects;

import de.aqua.web.delegates.WebDriverExtended;
import jline.internal.Nullable;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.aqua.web.utils.PropertiesFileUtil.getElementName;
import static de.aqua.web.utils.WaitUtil.sleep;


/**
 * Created by bdumitru on 7/25/2017.
 */
@SuppressWarnings("unused")
public class GeneralPage {

    private static Logger LOG = LoggerFactory.getLogger(GeneralPage.class);

    public static List<WebElement> divBlockerList(WebDriverExtended driver) {
        return driver.findElements(By.className("z-modal-mask"));
    }

    public static WebElement divBlocker(WebDriverExtended driver, int index) {
        return driver.findElements(By.className("z-modal-mask")).get(index);
    }

    public static WebElement divBlocker(WebDriverExtended driver) {
        return divBlocker(driver, 0);
    }

    public static WebElement loadingProgress(WebDriverExtended driver) {
        return driver.findElement(By.className("z-apply-loading-indicator"));
    }

    public static int loadingProgressItems(WebDriverExtended driver) {
        return driver.findElements(By.className("z-apply-loading-indicator")).size();
    }

    public static WebElement qaTab(WebDriverExtended driver, String tabName) {
        return specificWebElemFromList(driver, "z-tab-text", tabName);
    }

    public static WebElement westPageArea(WebDriverExtended driver) {
        return driver.findElement(By.className("z-west"));
    }

    public static WebElement centralPageArea(WebDriverExtended driver) {
        return driver.findElement(By.className("z-center"));
    }

    public static WebElement dialogBox(WebDriverExtended driver, int dialogIndexNo) {
        return visibleElements(driver, "z-window-highlighted").get(dialogIndexNo);
    }

    public static WebElement dialogBox(WebDriverExtended driver) {
//        try {
//            return driver.findElement(By.xpath(".//*[@class='z-messagebox-window  "
//                    + "z-window z-window-noheader z-window-highlighted z-window-shadow']"));
//        } catch (Exception e) {
//            try {
//                return driver.findElement(By.xpath(".//*[@class='z-messagebox-window  "
//                        + "z-window z-window-highlighted z-window-shadow']"));
//            } catch (Exception e1) {
//                return driver.findElement(
//                        By.xpath(".//*[@class='z-window z-window-highlighted z-window-shadow']"));
//            }
//        }
        return dialogBox(driver, 0);
    }

    public static WebElement dialogBoxActionButton(WebDriverExtended driver, String btnName) {
        WebElement element = null;
        List<WebElement> buttons = dialogBox(driver)
                .findElements(By.className("z-messagebox-button"));
        for (WebElement btn : buttons) {
            if (btn.getAttribute("innerHTML").equalsIgnoreCase(btnName)) {
                element = btn;
                break;
            }
        }
        return element;
    }

    public static WebElement dialogBoxMessage(WebDriverExtended driver) {
        return dialogBox(driver).findElement(By.className("z-label"));
    }

    /**
     * List of visible elements from QA2 OUS Summary.
     * Hardcodded area index - should not be used for other areas
     *
     * @param driver                  WebDriver
     * @param pageAreaToLookAfterElem class name of the page area where the element is located
     * @param elementLocator          element class name
     * @return a list containing the visible elements located by a specific class name
     */
    public static List<WebElement> visibleElements(WebDriverExtended driver,
                                                   String pageAreaToLookAfterElem,
                                                   String elementLocator) {
        List<WebElement> allElements = driver.findElements(By.className(pageAreaToLookAfterElem))
                .get(driver.findElements(By.className(pageAreaToLookAfterElem)).size() - 1)
                .findElements(By.className(elementLocator));
        List<WebElement> visibleElements = new ArrayList<>();
        for (WebElement option : allElements) {
            if (option.isDisplayed()) {
                visibleElements.add(option);
            }
        }
        return visibleElements;
    }

    /**
     * List of visible elements.
     *
     * @param driver         WebDriver
     * @param locateBy       locate element by id, class, xpath etc.
     * @param tagName        Parent tag name
     * @param elementLocator element class name
     * @return a list containing the visible elements located by a specific class name
     */
    public static List<WebElement> visibleElements(WebDriverExtended driver, String locateBy,
                                                   String tagName, String elementLocator) {
        List<WebElement> allElements = driver.findElements(By.xpath(".//" + tagName + "[@class='"
                + elementLocator + "']"));
        List<WebElement> visibleElements = new ArrayList<>();
        for (WebElement option : allElements) {
            if (option.isDisplayed()) {
                visibleElements.add(option);
            }
        }
        return visibleElements;
    }

    /**
     * List of visible elements.
     *
     * @param driver         WebDriver
     * @param elementLocator element class name
     * @return a list containing the visible elements located by a specific class name
     */
    public static List<WebElement> visibleElements(WebDriverExtended driver,
                                                   String elementLocator) {
        List<WebElement> allElements = driver.findElementsAndWait(By.className(elementLocator));
        List<WebElement> visibleElements = new ArrayList<>();
        for (WebElement option : allElements) {
            if (option.isDisplayed()) {
                visibleElements.add(option);
            }
        }
        return visibleElements;
    }

    /**
     * List of visible elements from specific page area.
     *
     * @param driver         WebDriver
     * @param elementLocator element class name
     * @return a list containing the visible elements located on a specific page area
     */
    public static List<WebElement> visibleElements(WebDriverExtended driver,
                                                   WebElement pageAreaLocator,
                                                   String elementLocator) {
        List<WebElement> allElements = pageAreaLocator.findElements(By.className(elementLocator));
        List<WebElement> visibleElements = new ArrayList<>();
        for (WebElement option : allElements) {
            if (option.isDisplayed()) {
                visibleElements.add(option);
            }
        }
        return visibleElements;
    }

    public static List<WebElement> sameNameVisibleElements(WebDriverExtended driver,
                                                           String elementLocator, String elementName) {
        List<WebElement> allElements = driver.findElements(By.className(elementLocator));
        List<WebElement> visibleElements = new ArrayList<>();
        for (WebElement option : allElements) {
            if (option.isDisplayed()
                    && option.getAttribute("innerHTML").equalsIgnoreCase(elementName)) {
                visibleElements.add(option);
            }
        }
        return visibleElements;
    }

    public static List<WebElement> sameNameVisibleButtons(WebDriverExtended driver,
                                                          String elementName) {
        return sameNameVisibleElements(driver, "z-button", elementName);
    }

    /**
     * Label WebElement identified by its static tag label.
     *
     * @param driver         Webdriver
     * @param elementLocator element class name
     * @param tagLabel       the label that defines the dynamic label element
     * @return WebElement
     */
    public static WebElement identifySameTypeElementByTagLabelExactText(WebDriverExtended driver,
                                                                        String elementLocator,
                                                                        String tagLabel) {
        WebElement element = null;
        List<WebElement> elements = visibleElements(driver, elementLocator);
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getAttribute("innerHTML").equalsIgnoreCase(tagLabel)) {
                element = elements.get(i + 1);
                break;
            }
        }
        return element;
    }

    /**
     * Label WebElement identified by its static tag label.
     *
     * @param driver                  WebDriver
     * @param pageAreaToLookAfterElem class name of the page area where the element should be located
     * @param elementLocator          element class name
     * @param tagLabel                the label that defines the dynamic label element
     * @return WebElement
     */
    public static WebElement identifySameTypeElementByTagLabelContainingText(WebDriverExtended driver,
                                                                             String pageAreaToLookAfterElem,
                                                                             String elementLocator,
                                                                             String tagLabel) {
        WebElement element = null;
        List<WebElement> elements = visibleElements(driver, pageAreaToLookAfterElem, elementLocator);
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getAttribute("innerHTML").contains(tagLabel)) {
                element = elements.get(i + 1);
                break;
            }
        }
        return element;
    }

    /**
     * Label WebElement identified by its static tag label.
     *
     * @param driver         WebDriver
     * @param elementLocator element class name
     * @param tagLabel       the label that defines the dynamic label element
     * @return WebElement
     */
    public static WebElement identifySameTypeElementByTagLabelContainingText(WebDriverExtended driver,
                                                                             String elementLocator,
                                                                             String tagLabel) {
        WebElement element = null;
        List<WebElement> elements = visibleElements(driver, elementLocator);
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getAttribute("innerHTML").contains(tagLabel)) {
                element = elements.get(i + 1);
                break;
            }
        }
        return element;
    }

    /**
     * WebElement identified by its static tag label.
     *
     * @param driver               WebDriver
     * @param labelLocator         label class name
     * @param toFindElementLocator element to be found class name
     * @param tagLabelName         the label that is linked with the element that needs to be found
     * @return WebElement
     */
    public static WebElement identifyDifferentTypeElementByTagLabelFromZrow(WebDriverExtended driver,
                                                                            String labelLocator,
                                                                            String toFindElementLocator,
                                                                            String tagLabelName) {
        Boolean found = false;
        WebElement element = null;
        List<WebElement> rows = visibleElements(driver, "z-row");
        for (WebElement row : rows) {
            List<WebElement> labels = row.findElements(By.className(labelLocator));
            for (int i = 0; i < labels.size(); i++) {
                if (labels.get(i).getAttribute("innerHTML").equalsIgnoreCase(tagLabelName)
                        || labels.get(i).getAttribute("innerHTML").contains(tagLabelName)) {
                    List<WebElement> toFindElems = row.findElements(By.className(toFindElementLocator));
                    if (toFindElems.size() > 0) {
                        element = toFindElems.get(i);
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                break;
            }
            labels.clear();
        }
        return element;
    }

    /**
     * WebElement identified by its static tag label.
     *
     * @param driver               WebDriver
     * @param labelLocator         label class name
     * @param toFindElementLocator element to be found class name
     * @param tagLabelName         the label that is linked with the element that needs to be found
     * @return WebElement
     */
    public static WebElement identifyDifferentTypeElementByTagLabelFromZhBox(WebDriverExtended
                                                                                     driver,
                                                                             String labelLocator,
                                                                             String toFindElementLocator,
                                                                             String tagLabelName) {
        Boolean found = false;
        WebElement element = null;
        List<WebElement> rows = visibleElements(driver, "z-hbox");
        for (WebElement row : rows) {
            List<WebElement> labels = row.findElements(By.className(labelLocator));
            for (int i = 0; i < labels.size(); i++) {
                if (labels.get(i).getAttribute("innerHTML").equalsIgnoreCase(tagLabelName)
                        || labels.get(i).getAttribute("innerHTML").contains(tagLabelName)) {
                    List<WebElement> toFindElems = row.findElements(By.className(toFindElementLocator));
                    if (toFindElems.size() > 0) {
                        try {
                            element = toFindElems.get(i);
                        } catch (Exception e) {
                            element = toFindElems.get(i - 1);
                        }
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                break;
            }
            labels.clear();
        }
        return element;
    }

    /**
     * WebElement identified by its static tag label.
     *
     * @param driver               WebDriver
     * @param labelLocator         label class name
     * @param toFindElementLocator element to be found class name
     * @param tagLabelName         the label that is linked with the element that needs to be found
     * @return WebElement
     */
    public static WebElement identifyDifferentTypeElementByTagLabelFromZvBox(WebDriverExtended
                                                                                     driver,
                                                                             String labelLocator,
                                                                             String toFindElementLocator,
                                                                             String tagLabelName) {
        Boolean found = false;
        WebElement element = null;
        List<WebElement> rows = visibleElements(driver, "z-vbox");
        for (WebElement row : rows) {
            List<WebElement> labels = row.findElements(By.className(labelLocator));
            for (int i = 0; i < labels.size(); i++) {
                if (labels.get(i).getAttribute("innerHTML").equalsIgnoreCase(tagLabelName)
                        || labels.get(i).getAttribute("innerHTML").contains(tagLabelName)) {
                    List<WebElement> toFindElems = row.findElements(By.className(toFindElementLocator));
                    if (toFindElems.size() > 0) {
                        try {
                            element = toFindElems.get(i);
                        } catch (Exception e) {
                            element = toFindElems.get(i - 1);
                        }
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                break;
            }
            labels.clear();
        }
        return element;
    }

    public static WebElement identifyDifferentTypeElementByTagLabel(WebDriverExtended driver,
                                                                    String parentContainerLocator,
                                                                    String labelLocator,
                                                                    String toFindElementLocator,
                                                                    String tagLabelName) {
        Boolean found = false;
        WebElement element = null;
        List<WebElement> rows = visibleElements(driver, parentContainerLocator);
        for (WebElement row : rows) {
            List<WebElement> labels = row.findElements(By.className(labelLocator));
            for (int i = 0; i < labels.size(); i++) {
                if (labels.get(i).getAttribute("innerHTML").equalsIgnoreCase(tagLabelName)
                        || labels.get(i).getAttribute("innerHTML").contains(tagLabelName)) {
                    List<WebElement> toFindElems = row.findElements(By.className(toFindElementLocator));
                    if (toFindElems.size() > 0) {
                        try {
                            element = toFindElems.get(i);
                        } catch (Exception e) {
                            element = toFindElems.get(i - 1);
                        }
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                break;
            }
            labels.clear();
        }
        return element;
    }

    public static WebElement specificWebElemFromList(WebDriverExtended driver,
                                                     List<WebElement> elemList, String elemName) {
        WebElement element = null;
        for (WebElement elem : elemList) {
            if (elem.getAttribute("innerHTML").equalsIgnoreCase(elemName)) {
                element = elem;
                break;
            }
        }
        return element;
    }

    public static WebElement specificWebElemFromList(WebDriverExtended driver, String elemLocator,
                                                     String elemName) {
        WebElement element = null;
        List<WebElement> elemList = visibleElements(driver, elemLocator);
        for (WebElement elem : elemList) {
            // FIXME - DUPLICATED by specificWebElemByContainedText
            // .contains(elemName) is a workaround for the elements that have empty spaces into
            // their names
            if (elem.getAttribute("innerHTML").equalsIgnoreCase(elemName)
                    || elem.getAttribute("innerHTML").contains(elemName)) {
                element = elem;
                break;
            }
        }
        return element;
    }

    public static WebElement childWebElemFromWebElement(WebDriverExtended driver, WebElement we,
                                                        String elemLocator, String elemName) {
        WebElement element = null;
        List<WebElement> elemList = we.findElements(By.className(elemLocator));
        for (WebElement elem : elemList) {
            if (elem.getAttribute("innerHTML").equalsIgnoreCase(elemName)
                    || elem.getAttribute("innerHTML").contains(elemName)) {
                element = elem;
                break;
            }
        }
        return element;
    }

    public static WebElement specificLabel(WebDriverExtended driver, String elemName) {
        return specificWebElemFromList(driver, "z-label", elemName);
    }

    public static WebElement specificWebElemByContainedText(WebDriverExtended driver,
                                                            String elemLocator, String elemName) {
        WebElement element = null;
        List<WebElement> elemList = visibleElements(driver, elemLocator);
        for (WebElement elem : elemList) {
            if (elem.getAttribute("innerHTML").contains(elemName)) {
                element = elem;
                break;
            }
        }
        return element;
    }

    public static List<WebElement> specificWebElemListByContainedText(WebDriverExtended driver,
                                                                      String elemLocator,
                                                                      String elemName) {
        WebElement element = null;
        List<WebElement> elements = new ArrayList<>();
        List<WebElement> elemList = visibleElements(driver, elemLocator);
        for (WebElement elem : elemList) {
            if (elem.getAttribute("innerHTML").contains(elemName)) {
                elements.add(elem);
            }
        }
        return elements;
    }

    public static List<WebElement> collectGridColumnElements(WebDriverExtended driver,
                                                             String columnName) {
        int index = 0;
        WebElement we = null;
        List<WebElement> ce = new ArrayList<>();
        List<WebElement> elements = visibleElements(driver, "z-grid");

        for (WebElement element : elements) {
            if (element.findElement(By.className("z-grid-header"))
                    .getAttribute("innerHTML").contains(columnName)) {
                we = element;
                break;
            }
        }

        if (we != null) {
            elements.clear();
            elements = we.findElement(By.className("z-grid-header"))
                    .findElements(By.className("z-column-content"));
        } else {
            Assert.fail("Failed to find the specific header!");
        }

        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getAttribute("innerHTML").contains(columnName)) {
                index = i;
                break;
            }
        }

        elements.clear();
        elements = we.findElement(By.className("z-grid-body"))
                .findElements(By.className("z-row"));

        for (WebElement element : elements) {
            List<WebElement> visible = new ArrayList<>();
            for (WebElement label : element.findElements(By.className("z-label"))) {
                if (label.isDisplayed()) {
                    visible.add(label);
                }
            }
            ce.add(visible.get(index));
        }

        return ce;
    }

    public static List<WebElement> gridColumnElements(WebDriverExtended driver,
                                                      String columnName) {
        int index = 0;
        WebElement we = null;
        List<WebElement> ce = new ArrayList<>();
        List<WebElement> elements = visibleElements(driver, "z-grid");

        for (WebElement element : elements) {
            if (element.findElement(By.className("z-grid-header"))
                    .getAttribute("innerHTML").contains(columnName)) {
                we = element;
                break;
            }
        }

        if (we != null) {
            elements.clear();
            elements = we.findElement(By.className("z-grid-header"))
                    .findElements(By.className("z-column-content"));
        } else {
            Assert.fail("Failed to find the specific header!");
        }

        elements.clear();
        elements = we.findElement(By.className("z-grid-body"))
                .findElements(By.className("z-row"));
        return elements;
    }

    public static List<WebElement> collectListColumnElements(WebDriverExtended driver,
                                                             String columnName, String searchTab) {
        int index = 0;
        int newAdded = 0;
        WebElement we = null;
        List<WebElement> ce = new ArrayList<>();
        List<WebElement> elements = visibleElements(driver, "z-listbox");

        for (WebElement element : elements) {
            if (element.findElement(By.className("z-listhead"))
                    .getAttribute("innerHTML").contains(columnName)) {
                we = element;
                break;
            }
        }

        if (we != null) {
            elements.clear();
            elements = we.findElement(By.className("z-listhead"))
                    .findElements(By.className("z-listheader-content"));
        } else {
            Assert.fail("Failed to find the specific header!");
        }

        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getAttribute("innerHTML").contains(columnName)) {
                index = i;
                break;
            }
        }

        elements.clear();
        elements = we.findElement(By.className("z-listbox-body"))
                .findElements(By.className("z-listitem"));

        for (WebElement element : elements) {
            ce.add(element.findElements(By.className("z-listcell")).get(index)
                    .findElement(By.className("z-label")));
            newAdded++;
            if (searchTab.equalsIgnoreCase(getElementName("advanced_ous_search")
            )) {
                if (newAdded % 5 == 0 && newAdded + 5 < elements.size()) {
                    driver.executeScript("arguments[0].scrollIntoView(true);",
                            elements.get(newAdded + 5));
                    sleep(500);
                }
            } else {
                if (newAdded % 10 == 0 && newAdded + 10 < elements.size()) {
                    driver.executeScript("arguments[0].scrollIntoView(true);",
                            elements.get(newAdded + 10));
                    sleep(500);
                }
            }
        }
        return ce;
    }

    public static WebElement getDynamicLabelContentByTagLabelExactText(WebDriverExtended driver,
                                                                       String tagName) {
        return identifySameTypeElementByTagLabelExactText(driver, "z-label", tagName);
    }

    public static WebElement getDynamicLabelContentByTagLabelContainingText(WebDriverExtended driver,
                                                                            String tagName) {
        return identifySameTypeElementByTagLabelContainingText(driver, "z-label", tagName);
    }

    public static WebElement getDynamicLabelByTagLabel(WebDriverExtended driver, String tagName) {
        return identifySameTypeElementByTagLabelContainingText(driver, "z-center",
                "z-label", tagName);
    }

    public static WebElement headerMenuTab(WebDriverExtended driver, String tabName) {
        return specificWebElemFromList(driver, "z-tab-text", tabName);
    }

    public static WebElement button(WebDriverExtended driver, String buttonName) {
        return specificWebElemFromList(driver, "z-button", buttonName);
    }

    public static WebElement toolbarButton(WebDriverExtended driver, String buttonName) {
        return specificWebElemFromList(driver, "z-toolbarbutton-content", buttonName);
    }

    public static WebElement textBox(WebDriverExtended driver, String tagLabelName) {
        return identifyDifferentTypeElementByTagLabelFromZrow(driver, "z-label",
                "z-textbox", tagLabelName);
    }

    public static WebElement layoutRow(WebDriverExtended driver, String tagLabelName) {
        return identifyDifferentTypeElementByTagLabelFromZrow(driver, "z-label",
                "z-hlayout", tagLabelName);
    }

    public static WebElement multipleLayoutRows(WebDriverExtended driver, String tagLabelName) {
        return identifyDifferentTypeElementByTagLabelFromZrow(driver, "z-label",
                "z-hlayout", tagLabelName);
    }

    public static WebElement globalTextBox(WebDriverExtended driver) {
        return driver.findElement(By.className("z-textbox"));
    }

    public static WebElement captionContent(WebDriverExtended driver, String captionName) {
        return dialogBox(driver).findElement(By.className("z-caption"));
    }

    public static WebElement captionTextBox(WebDriverExtended driver, String labelName) {
        WebElement field = null;
        List<WebElement> visibleFields = GeneralPage.visibleElements(driver, "z-textbox");
        for (WebElement vf : visibleFields) {
            if (vf.getTagName().equals("textarea")
                    && Integer.parseInt(vf.getAttribute("rows")) >= 2) {
                field = vf;
                break;
            }
        }
        return field;
        // return GeneralPage.identifyDifferentTypeElementByTagLabelFromZrow(driver,
        // "z-caption-content", "z-textbox", labelName);
    }

    public static WebElement doubleBox(WebDriverExtended driver, String tagLabelName) {
        return identifyDifferentTypeElementByTagLabelFromZhBox(driver, "z-label",
                    "z-doublebox", tagLabelName);
    }

    public static WebElement link(WebDriverExtended driver, String linkName) {
        return specificWebElemFromList(driver, "z-a", linkName);
    }

    public static WebElement label(WebDriverExtended driver, String labelName) {
        return specificWebElemFromList(driver, "z-label", labelName);
    }

    public static WebElement treeCell(WebDriverExtended driver, String treeCellName) {
        return specificWebElemFromList(driver, "z-treecell-text", treeCellName);
    }

    public static WebElement listCellOption(WebDriverExtended driver, String listCellContent) {
        return specificWebElemFromList(driver, "z-listcell", listCellContent);
    }

    public static WebElement radioButton(WebDriverExtended driver, String radioBtnName) {
        return specificWebElemFromList(driver, "z-radio-content", radioBtnName);
    }

    public static WebElement comboBox(WebDriverExtended driver, String labelName) {
        try {
            return identifyDifferentTypeElementByTagLabelFromZrow(driver, "z-label",
                    "z-combobox-button", labelName);
        } catch (Exception e) {
            try {
                return identifyDifferentTypeElementByTagLabelFromZhBox(driver, "z-label",
                        "z-combobox-button", labelName);
            } catch (Exception ex) {
                return identifyDifferentTypeElementByTagLabelFromZvBox(driver, "z-label",
                        "z-combobox-button", labelName);
            }
        }
    }

    private static List<WebElement> dateBoxes(WebDriverExtended driver) {
        return driver.findElements(By.className("z-datebox"));
    }

    public static WebElement dateBox(WebDriverExtended driver, String value) {
        WebElement element = null;
        for (WebElement elem : dateBoxes(driver)) {
            if (elem.findElement(By.className("z-datebox-input"))
                    .getAttribute("value").contains(value) && StringUtils.isNotBlank(value)) {
                element = elem.findElement(By.className("z-datebox-input"));
                break;
            } else {
                element = dateBoxes(driver).get(1);
            }
        }
        return element;
    }

    public static WebElement checkBox(WebDriverExtended driver, String labelName) {
        WebElement we = null;
//        try {
//            we = identifyDifferentTypeElementByTagLabelFromZrow(driver, "z-checkbox-content",
//                    "z-checkbox", labelName)
//                    .findElement(By.xpath(".//*[@type='checkbox']"));
//        } catch (Exception e) {
//            we = identifyDifferentTypeElementByTagLabelFromZhBox(driver, "z-checkbox-content",
//                    "z-checkbox", labelName)
//                    .findElement(By.xpath(".//*[@type='checkbox']"));
//        }
//        return we;
        Map<WebElement, String> properties = visibleCheckboxesProperties(driver);
        for (Map.Entry<WebElement, String> entry : properties.entrySet()) {
            String actName = entry.getKey().findElement(By.tagName("label")).getText();
            String expName = getElementName(labelName);
            if (actName.equals(expName)) {
                we = entry.getKey().findElement(By.tagName("input"));
                break;
            }
        }
        return we;
    }

    public static List<WebElement> visibleCheckboxes(WebDriverExtended driver) {
        return visibleElements(driver, "z-checkbox");
    }

    public static Map<WebElement, String> visibleCheckboxesProperties(WebDriverExtended driver) {
        List<WebElement> checkboxList = visibleElements(driver, "z-checkbox");
        HashMap checkboxProp = new HashMap();
        for (WebElement cb : checkboxList) {
            String status = cb.findElement(By.tagName("input")).getAttribute("checked");
            //            String name = cb.findElement(By.tagName("label")).getText();
            if (StringUtils.isBlank(status)) {
                status = "false";
                checkboxProp.put(cb, status);
            } else {
                checkboxProp.put(cb, status);
            }
        }
        return checkboxProp;
    }

    public static WebElement selectComboBoxItem(WebDriverExtended driver, String itemName) {
        WebElement element = null;
        List<WebElement> visible = visibleElements(driver, "z-combobox-content");
        List<WebElement> elements = visible.get(0).findElements(By.className("z-comboitem-text"));
        if (elements.size() == 0) {
            elements = visibleElements(driver, "z-comboitem-text");
        }
        for (WebElement elem : elements) {
            if ((elem.getAttribute("innerHTML")).replaceAll("&nbsp;", " ")
                    .replaceAll("&gt;", ">").equalsIgnoreCase(itemName)) {
                element = elem;
                break;
            }
        }
        return element;
    }

    public static boolean comboBoxItemAvailability(WebDriverExtended driver, String itemName) {
        boolean exists = false;
        List<WebElement> elements = driver.findElement(By.className("z-combobox-content"))
                .findElements(By.className("z-comboitem-text"));
        if (elements.size() == 0) {
            elements = visibleElements(driver, "z-comboitem-text");
        }
        for (WebElement elem : elements) {
            if ((elem.getAttribute("innerHTML")).replaceAll("&nbsp;", " ")
                    .replaceAll("&gt;", ">").equalsIgnoreCase(itemName)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public static WebElement reloadButton(WebDriverExtended driver) {
        WebElement element = null;
        for (WebElement btn : visibleElements(driver, "z-toolbarbutton")) {
            if (btn.getAttribute("title").equalsIgnoreCase("Reload")) {
                element = btn;
                break;
            }
        }
        return element;
    }

    public static WebElement listHeaderTab(WebDriverExtended driver, String tabName) {
        return specificWebElemByContainedText(driver, "z-listheader", tabName);
    }

    public static String expectedValueFromContainer(WebDriverExtended driver,
                                                    String module, String labelName) {
        StringBuilder range = new StringBuilder();
        List<WebElement> labels = new ArrayList<>();
        switch (module) {
            case "QA0": {
                labels = GeneralPage
                        .identifyDifferentTypeElementByTagLabelFromZrow(driver, "z-label",
                                "z-zhbox", labelName)
                        .findElements(By.className("z-label"));
                break;
            }
            case "QA2": {
                labels = GeneralPage
                        .identifyDifferentTypeElementByTagLabelFromZrow(driver, "z-label",
                                "z-cell", labelName)
                        .findElements(By.className("z-label"));
                break;
            }
            default: {
                break;
            }
        }
        for (WebElement label : labels) {
            range.append(label.getAttribute("innerHTML"));
        }
        return range.toString();
    }


    /**
     * Label WebElement identified by its label name.
     *
     * @param driver                  WebDriver
     * @param pageAreaToLookAfterElem class name of the page area where the element should be located
     * @param elementLocator          element class name
     * @param tagLabel                the label that defines the element
     * @return WebElement
     */
    public static WebElement identifyElementFromSpecificPageArea(WebDriverExtended driver,
                                                                 String pageAreaToLookAfterElem,
                                                                 String elementLocator,
                                                                 String tagLabel) {
        WebElement element = null;
        List<WebElement> elements = visibleElements(driver, pageAreaToLookAfterElem, elementLocator);
        for (WebElement element1 : elements) {
            if (element1.getAttribute("innerHTML").contains(tagLabel)) {
                element = element1;
                break;
            }
        }
        return element;
    }

    public static WebElement elementFromSpecificPageArea(WebDriverExtended driver, String pageAreaName,
                                                         String tagLabel) {
        WebElement element = null;
        switch (pageAreaName) {
            case ("ObsUnitSet Summary"): {
                element = identifySameTypeElementByTagLabelContainingText(driver,
                        "z-center", "z-label", tagLabel);
                break;
            }
            default: {
                break;
            }
        }
        return element;
    }

    public static String twoLabelsPosition(WebDriverExtended driver, String pageArea,
                                           String firstLabelName, String secondLabelName) {
        int firstRowIndex = 0;
        int secondRowIndex = 0;
        int firstLabelIndex = 0;
        int secondLabelIndex = 0;
        boolean found = false;
        String position = "";
        List<WebElement> rows = new ArrayList<>();
        switch (pageArea) {
            case "ObsUnitSet Summary": {
                rows = visibleElements(driver, "z-center",
                        "z-row");
                break;
            }
            case "Advanced OUS Search": {
                rows = visibleElements(driver, "z-west",
                        "z-row");
                break;
            }
            default: {
                break;
            }
        }
        for (int i = 0; i < rows.size(); i++) {
            List<WebElement> labels = rows.get(i).findElements(By.className("z-label"));
            for (int j = 0; j < labels.size(); j++) {
                try {
                    if (labels.get(j).getAttribute("innerHTML")
                            .equalsIgnoreCase(secondLabelName)) {
                        secondRowIndex = i;
                        secondLabelIndex = j;
                        found = true;
                        break;
                    }
                } catch (Exception e) {
                    LOG.info("-");
                }
            }
            if (found) {
                found = false;
                break;
            }
            labels.clear();
        }
        for (int i = 0; i < rows.size(); i++) {
            List<WebElement> labels = rows.get(i).findElements(By.className("z-label"));
            for (int j = 0; j < labels.size(); j++) {
                try {
                    if (labels.get(j).getAttribute("innerHTML")
                            .equalsIgnoreCase(firstLabelName)) {
                        firstRowIndex = i;
                        firstLabelIndex = j;
                        found = true;
                        break;
                    }
                } catch (Exception e) {
                    LOG.info("-");
                }
            }
            if (found) {
                break;
            }
            labels.clear();
        }

        if (firstRowIndex > secondRowIndex) {
            position = "under";
            if (secondRowIndex == firstRowIndex - 1) {
                position = "beneath";
            }
        }

        if (firstRowIndex < secondRowIndex) {
            position = "above";
            if (secondRowIndex == firstRowIndex + 1) {
                position = "right above";
            }
        }

        if (firstRowIndex == secondRowIndex && firstLabelIndex < secondLabelIndex) {
            position = "on the right hand side";
        }

        if (firstRowIndex == secondRowIndex && firstLabelIndex > secondLabelIndex) {
            position = "on the left hand side";
        }

        return position;
    }

    public static WebElement elementContainingSpecificText(WebDriverExtended driver,
                                                           String elementLocator, String text) {
        WebElement we = null;
        List<WebElement> elements = visibleElements(driver, elementLocator);
        for (WebElement element : elements) {
            if (element.getAttribute("innerHTML").contains(text)) {
                we = element;
                break;
            }
        }
        return we;
    }

    public static WebElement doQAXForm(WebDriverExtended driver) {
        return driver.findElement(By.xpath(".//*[@class='z-window z-window-noborder "
                + "z-window-overlapped z-window-shadow']"));
    }

    public static WebElement setQAXStatus(WebDriverExtended driver) {
        return doQAXForm(driver).findElement(By.className("z-combobox-button"));
    }

    public static WebElement doQAXFormStatusReason(WebDriverExtended driver) {
        return doQAXForm(driver).findElement(By.className("z-chosenbox"));
    }

    /**
     * Specific reason status option selected by list index.
     *
     * @param driver WebDriver
     * @param index  item index
     * @return reason status option by its specific list index
     */
    public static WebElement doQAXFormReasonStatusOptionPosition(WebDriverExtended driver,
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
    public static WebElement doQAXFormReasonStatusOptionPosition(WebDriverExtended driver) {
        return doQAXFormReasonStatusOptionPosition(driver, "0");
    }

    public static WebElement doQAXFormEC(WebDriverExtended driver) {
        return doQAXForm(driver).findElement(By.className("z-doublebox"));
    }

    public static WebElement doQAXFormComment(WebDriverExtended driver, String labelName) {
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

    public static WebElement doQAXFormStatusReasonList(WebDriverExtended driver) {
        return GeneralPage.visibleElements(driver, "z-chosenbox-select").get(0);
        // return driver.findElement(By.className("z-chosenbox-select"));
    }

    public static WebElement doQAXFormStatusReasonDisplayed(WebDriverExtended driver, String
            statusName) {
        WebElement we = null;
        List<WebElement> statusList = doQAXFormStatusReasonList(driver)
                .findElements(By.className("z-chosenbox-option"));
        for (WebElement status : statusList) {
            if (status.getAttribute("innerHTML").contains(statusName)) {
                we = status;
                break;
            }
        }
        return we;
    }

    @Nullable
    public static WebElement getTableHeader(WebDriverExtended driver, String gridName) {
        List<WebElement> elements = visibleElements(driver, "z-vlayout-inner");
        WebElement we = null;
        for (WebElement element : elements) {
            if (element.getAttribute("innerHTML").contains(gridName)) {
                we = element.findElement(By.className("z-grid-header"));
                break;
            }
        }
        return we;
    }

    public static WebElement selectedFieldOption(WebDriverExtended driver, String labelName) {
        return GeneralPage.identifyDifferentTypeElementByTagLabelFromZrow(driver, "z-label",
                "z-chosenbox", labelName);
    }

    public static WebElement toolBarButton(WebDriverExtended driver, String btnName) {
        return specificWebElemByContainedText(driver, "z-toolbarbutton-content", btnName);
    }

    public static WebElement column(WebDriverExtended driver, String text) {
        return specificWebElemByContainedText(driver, "z-column", text);
    }

    public static WebElement searchButton(WebDriverExtended driver) {
        List<WebElement> buttons = GeneralPage.visibleElements(driver, "z-button");
        return GeneralPage.specificWebElemFromList(driver, buttons, "Search");
    }

    public static WebElement canvasPlot(WebDriverExtended driver) {
        return driver.findElementAndWait(By.className("jqplot-event-canvas"));
    }

    public static WebElement plotTitle(WebDriverExtended driver, String plotTitle) {
        return elementContainingSpecificText(driver, "jqplot-title", plotTitle);
    }

    public static WebElement noItemDisplayed(WebDriverExtended driver) {
        return driver.findElementAndWait(By.className("z-listbox-emptybody")).findElement(By
                .tagName("td"));
    }

    public static WebElement collapseView(WebDriverExtended driver) {
        try {
            return GeneralPage.visibleElements(driver, "z-icon-caret-up").get(0);
        } catch (Exception e) {
            return GeneralPage.visibleElements(driver, "z-icon-angle-up").get(0);
        }
    }

    public static WebElement expandView(WebDriverExtended driver) {
        return GeneralPage.visibleElements(driver, "z-icon-caret-down").get(0);
    }

    public static WebElement image(WebDriverExtended driver, String imgName) {
        WebElement we = null;
        List<WebElement> images = GeneralPage.visibleElements(driver, "z-image");
        for (WebElement image : images) {
//            if (image.getAttribute("src").contains("/webaqua/images/" + imgName)) {
            if (image.getAttribute("src").contains(imgName)) {
                we = image;
                break;
            }
        }
        return we;
    }
}