package de.obopstest.web.pageobjects;

import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.utils.StringUtil;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by bdumitru on 8/8/2017.
 */
@SuppressWarnings("unused")
public class PLCalibrationPage {

    public static WebElement ebOverview(WebDriverExtended driver, String elemName) {
        return GeneralPage.specificWebElemByContainedText(driver, "z-label", elemName);
    }

    public static WebElement ebUID(WebDriverExtended driver) {
        List<WebElement> uidList = GeneralPage.specificWebElemListByContainedText(driver,
                "z-toolbarbutton-content", "uid://");
        return uidList.get(StringUtil.generateRandomNo(uidList.size()));
    }
}
