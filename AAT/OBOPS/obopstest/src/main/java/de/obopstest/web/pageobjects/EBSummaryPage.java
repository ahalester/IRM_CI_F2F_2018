package de.obopstest.web.pageobjects;

import de.obopstest.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by bdumitru on 7/25/2017.
 */
@SuppressWarnings("unused")
public class EBSummaryPage {

    public static WebElement summary(WebDriverExtended driver) {
        return GeneralPage.specificWebElemFromList(driver, "z-tab-text", "ExecBlock Summary");
    }

    private static WebElement ebSummaryDom(WebDriverExtended driver) {
        return driver.findElement(By.id("zk_c_360"));
    }

    public static List<WebElement> ebButtons(WebDriverExtended driver) {
        return driver.findElements(By.className("z-button"));
    }

    public static WebElement button(WebDriverExtended driver, String buttonName) {
        return GeneralPage.specificWebElemFromList(driver, "z-button", buttonName);
    }

    public static WebElement individualDataRetrievedAfterReRunAOSCheck(WebDriverExtended driver,
                                                                       String tagLabel) {
        return GeneralPage.identifyDifferentTypeElementByTagLabelFromZrow(driver, "z-label",
                "z-listbox-body", tagLabel);
    }

    public static WebElement dataGridRetrievedAfterReRunAOSCheck(WebDriverExtended driver,
                                                                 String labelName) {
        return GeneralPage.specificWebElemByContainedText(driver, "z-grid", labelName)
                .findElement(By.className("z-grid-body"));
    }

    public static WebElement doQA0Form(WebDriverExtended driver) {
        return driver.findElement(By.xpath(".//*[@class='z-window z-window-noborder "
                + "z-window-overlapped z-window-shadow']"));
    }

    public static WebElement setQA0Status(WebDriverExtended driver) {
        return doQA0Form(driver).findElement(By.className("z-combobox-button"));
    }
}
