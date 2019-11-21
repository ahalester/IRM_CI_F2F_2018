package de.obopstest.web.pageobjects.snoopi;

import de.obopstest.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrjAndSbPage {
    private static Logger LOG = LoggerFactory.getLogger(PrjAndSbPage.class);


    public static WebElement projectCodeLabel(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("tree-code"));
    }

    public static WebElement treePrjCode(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("tree-prj-code"));
    }

    public static WebElement arcNodeLink(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("tree-node-link"));
    }

    // @ getText() -> "Contact scientist: <name>."
    public static WebElement contactScientistLabel(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("tree-cs-name"));
    }

    public static WebElement downloadProposalButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("tree-proposal-link"));
    }

    public static WebElement prjReportButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("tree-pr-report-link"));
    }


}


