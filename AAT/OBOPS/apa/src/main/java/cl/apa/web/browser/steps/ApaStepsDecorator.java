package cl.apa.web.browser.steps;

import cl.apa.web.browser.setup.AbstractSelenium3DockerMultiBrowserDriverSetup;
import cl.apa.web.delegates.WebDriverExtended;

import cl.apa.web.pageobjects.*;
import cl.apa.web.utils.PropertiesFileUtil;
import cl.apa.web.utils.WebElementValidationUtil;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static cl.apa.web.utils.WaitUtil.*;
import static cl.apa.web.pageobjects.ApaHeaderMenuPage.*;

public class ApaStepsDecorator extends AbstractSelenium3DockerMultiBrowserDriverSetup {

    private static Logger LOG = LoggerFactory.getLogger(AquaStepsDecorator.class);
    private List<String> auth = new ArrayList<>();
    private WebDriverExtended driver = getWebDriverExtended();

    @Given("^APA header title is displayed$")
    public void verifyIfHeaderTitleIsDisplayed() {
        ApaHeaderMenuPage.headerTitle(driver).isDisplayed();
        LOG.info(String.format("The header title '%s' is displayed.", headerTitle(driver).getText()));
    }

    @When("^the user clicks on a specific header tab$")
    public void clickOnHeaderTab(List<String> locatorAlias) {
        ApaHeaderMenuPage.headerMenuButton(driver, PropertiesFileUtil.getApaLocator(locatorAlias.get(0))).click();
        LOG.info(String.format("Clicked on '%s' header tab",
                ApaHeaderMenuPage.headerMenuButton(driver, PropertiesFileUtil.getApaLocator(locatorAlias.get(0))).getText()));
    }

    @Then("^the available pipeline runs are displayed$")
    public void pipelineRunsDisplayed() {
        WebElementValidationUtil.elementDisplayedValidation(driver, ApaPipelinePage.pipelineRuns(driver),
                "Pipeline runs");
        LOG.info(String.format("The '%s' view is displayed", ApaPipelinePage.pipelineRuns(driver)));
    }

    @Then("^the MOUS is in a specific state$")
    public void mousInSpecificState(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            WebElementValidationUtil.elementDisplayedValidation(driver,
                    ApaPipelinePage.specificRowCell(driver, detail.get(0), 0), detail.get(0));
            String mousState = ApaPipelinePage.rowCell(driver, detail.get(1)).getText();
            Assert.assertTrue(String.format("The expected state for MOUS '%s' is '%s' and it should be '%s'!",
                    detail.get(0), mousState, detail.get(1)), mousState.equalsIgnoreCase(detail.get(1)));
        }
    }

    @When("^the user clicks the info icon for a specific MOUS$")
    public void clickInfo(List<String> details) {
        WebElementValidationUtil.elementDisplayedValidation(driver,
                ApaPipelinePage.specificRowInfo(driver, details.get(0), 3), details.get(0));
        ApaPipelinePage.specificRowInfo(driver, details.get(0), 3).click();
        LOG.info(String.format("Clicked on the info icon for '%s'", details.get(0)));
    }

    @Then("^the info pipeline run pop-up is displayed$")
    public void pipelineRunPopupDisplayed() {
        WebElementValidationUtil.elementDisplayedValidation(driver,
                ApaPipelinePage.popupHeader(driver), "Info pipeline run");
        LOG.info(String.format("The '%s' pop-up is displayed", "Info pipeline run"));
    }

    @When("^the user clicks on Open Weblog$")
    public void clickOpenWeblog() {
        WebElementValidationUtil.elementDisplayedValidation(driver,
                ApaPipelinePage.openWeblog(driver), "Open Weblog");
        ApaPipelinePage.openWeblog(driver).click();
        LOG.info(String.format("Clicked on '%s'", "Open Weblog"));
        sleep(1000);
    }

    @Then("^the Weblog content is displayed$")
    public void weblogContentDisplayed() {
        WebElementValidationUtil.elementDisplayedValidation(driver,
                ApaWeblogPage.weblogSummary(driver), "Weblog Summary");
        LOG.info(String.format("The '%s' page is displayed", "Weblog Summary"));
    }
}
