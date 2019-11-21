package alma.aat.archive.web.pageobjects;

import alma.aat.archive.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AQSearchPage {

    public static WebElement headerTitle(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath(".//*[@headertitle='Alma Science Archive']"));
    }

    public static WebElement searchButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("queryFormSubmit"));
    }

    public static WebElement submitRequestButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath(".//*[@value='Submit download request']"));
    }

    public static WebElement inputField(WebDriverExtended driver, String id) {
        return driver.findElementAndWait(By.id(id));
    }

    public static WebElement inputFieldTitle(WebDriverExtended driver, String id) {
        return driver.findElementAndWait(By.className(id));
    }

    public static WebElement projectCode(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath(".//*[@id='project_code']"));
    }

    public static WebElement resultTable(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("resultTable"));
    }

    public static List<WebElement> checkboxes(WebDriverExtended driver) {
        return driver.findElementsAndWait(By.xpath(".//*[@type='checkbox']"));
    }

    public static WebElement firstCheckBox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath(".//*[@class='_select_vov_1']"));
    }
}
