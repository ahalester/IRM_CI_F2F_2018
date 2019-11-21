package de.aqua.web.pageobjects;

import de.aqua.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bdumitru on 7/25/2017.
 */
@SuppressWarnings("unused")
public class GeneralPage {

    public static WebElement loadingProgress(WebDriverExtended driver) {
        return driver.findElement(By.className("z-apply-loading-indicator"));
    }

    public static WebElement dialogBox(WebDriverExtended driver) {
        try {
            return driver.findElement(By.xpath(".//*[@class='z-messagebox-window  z-window z-window-noheader " +
                    "z-window-highlighted z-window-shadow']"));
        } catch (Exception e) {
            return driver.findElement(By.xpath(".//*[@class='z-messagebox-window  z-window z-window-highlighted z-window-shadow']"));
        }
    }

    public static WebElement dialogBoxActionButton(WebDriverExtended driver, String btnName) {
        WebElement element = null;
        List<WebElement> buttons = dialogBox(driver).findElements(By.className("z-messagebox-button"));
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
     * List of visible elements from QA2 OUS Summary
     * Hardcodded area index - should not be used for other areas
     *
     * @param driver
     * @param pageAreaToLookAfterElem class name of the page area where the element should be located
     * @param elementLocator          element class name
     * @return a list containing the visible elements located by a specific class name
     */
    public static List<WebElement> visibleElements(WebDriverExtended driver,
                                                   String pageAreaToLookAfterElem, String elementLocator) {
        List<WebElement> allElements = driver.findElements(By.className(pageAreaToLookAfterElem)).get(driver
                .findElements(By.className(pageAreaToLookAfterElem)).size() - 1)
                .findElements(By.className(elementLocator));
        List<WebElement> visibleElements = new ArrayList<>();
        for (WebElement option : allElements) {
            if (option.isDisplayed())
                visibleElements.add(option);
        }
        return visibleElements;
    }

    /**
     * List of visible elements
     *
     * @param driver
     * @param elementLocator element class name
     * @return a list containing the visible elements located by a specific class name
     */
    public static List<WebElement> visibleElements(WebDriverExtended driver, String elementLocator) {
        List<WebElement> allElements = driver.findElements(By.className(elementLocator));
        List<WebElement> visibleElements = new ArrayList<>();
        for (WebElement option : allElements) {
            if (option.isDisplayed())
                visibleElements.add(option);
        }
        return visibleElements;
    }

    /**
     * Label WebElement identified by its static tag label
     *
     * @param driver
     * @param pageAreaToLookAfterElem class name of the page area where the element should be located
     * @param elementLocator          element class name
     * @param tagLabel                the label that defines the dynamic label element
     * @return WebElement
     */
    public static WebElement identifySameTypeElementByTagLabel(WebDriverExtended driver, String pageAreaToLookAfterElem,
                                                               String elementLocator, String tagLabel) {
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
     * Label WebElement identified by its static tag label
     *
     * @param driver
     * @param elementLocator element class name
     * @param tagLabel       the label that defines the dynamic label element
     * @return WebElement
     */
    public static WebElement identifySameTypeElementByTagLabel(WebDriverExtended driver, String elementLocator, String
            tagLabel) {
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
     * WebElement identified by its static tag label
     *
     * @param driver
     * @param labelLocator         label class name
     * @param toFindElementLocator element to be found class name
     * @param tagLabelName         the label that is linked with the element that needs to be found
     * @return WebElement
     */
    public static WebElement identifyDifferentTypeElementByTagLabel(WebDriverExtended driver, String labelLocator,
                                                                    String toFindElementLocator, String tagLabelName) {
        Boolean found = false;
        WebElement element = null;
        List<WebElement> rows = visibleElements(driver, "z-row");
        for (WebElement row : rows) {
            List<WebElement> labels = row.findElements(By.className(labelLocator));
            for (int i = 0; i < labels.size(); i++) {
                if (labels.get(i).getAttribute("innerHTML").contains(tagLabelName)) {
                    List<WebElement> toFindElems = row.findElements(By.className(toFindElementLocator));
                    element = toFindElems.get(i);
                    found = true;
                    break;
                }
            }
            if (found)
                break;
        }
        return element;
    }

    public static WebElement specificWebElemFromList(WebDriverExtended driver, List<WebElement> elemList, String
            elemName) {
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
//            FIXME - NOT SAFE
//            .contains(elemName) is a workaround for the elements that have empty spaces into their names
            if (elem.getAttribute("innerHTML").equalsIgnoreCase(elemName) ||
                    elem.getAttribute("innerHTML").contains(elemName)) {
                element = elem;
                break;
            }
        }
        return element;
    }

    public static WebElement specificWebElemByContainedText(WebDriverExtended driver, String elemLocator,
                                                            String elemName) {
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

    public static WebElement headerMenuTab(WebDriverExtended driver, String tabName) {
        return GeneralPage.specificWebElemFromList(driver, "z-tab-text", tabName);
    }
}
