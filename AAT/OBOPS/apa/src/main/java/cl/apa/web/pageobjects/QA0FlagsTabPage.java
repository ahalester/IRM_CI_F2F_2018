package cl.apa.web.pageobjects;

import cl.apa.web.delegates.WebDriverExtended;
import org.openqa.selenium.WebElement;

/**
 * Created by bdumitru on 8/4/2017.
 */
@SuppressWarnings("unused")
public class QA0FlagsTabPage {

    public static WebElement textArea(WebDriverExtended driver, String content) {
        return GeneralPage.specificWebElemByContainedText(driver, "z-textbox", content);
    }
}
