package cl.apa.web.pageobjects;

import cl.apa.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bdumitru on 7/13/2017.
 */
public class QA0Page {

    public static WebElement qa0MainView(WebDriverExtended driver) {
        if (driver.getCurrentUrl().contains("eso.org")) {
            return GeneralPage.headerMenuTab(driver, "QA0");
        } else {
            return GeneralPage.label(driver, "QA0 AQUA");
        }
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
        List<WebElement> elemList = GeneralPage.visibleElements(driver, "z-listbox-body");
        return (elemList.get(0).findElements(By.className("z-listitem")))
                .get(Integer.parseInt(index)).findElement(By.className("z-listcell-content"));
    }

    public static WebElement selectFirstEB(WebDriverExtended driver) {
        return selectEBByIndex(driver, "0");
    }

    public static List<WebElement> allAvailableEBs(WebDriverExtended driver) {
        return GeneralPage.visibleElements(driver, "z-listbox-body")
                .get(0).findElements(By.tagName("tr"));
    }

    public static boolean elementFromEBSummaryView(WebDriverExtended driver, String elementType,
                                                   String elementName) {
        boolean exists = false;
        List<WebElement> elements = new ArrayList<>();
        switch (elementType.toLowerCase()) {
            case ("label"): {
                elements = EBSummaryPage.ebSummaryDom(driver)
                        .findElements(By.className("z-label"));
                break;
            }
            default:
                break;
        }
        for (WebElement element : elements) {
            if (element.getAttribute("innerHTML").contains(elementName)) {
                exists = true;
                break;
            }
        }
        return exists;
    }
}
