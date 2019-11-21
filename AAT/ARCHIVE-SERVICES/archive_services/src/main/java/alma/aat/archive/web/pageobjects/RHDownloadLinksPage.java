package alma.aat.archive.web.pageobjects;

import alma.aat.archive.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class RHDownloadLinksPage {

    public static List<WebElement> linkList(WebDriverExtended driver) {
          return driver.findElementsAndWait(
                By.xpath("//a[@href]"));
    }

}
