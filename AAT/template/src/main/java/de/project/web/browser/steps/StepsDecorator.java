package de.project.web.browser.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import de.project.web.browser.setup.AbstractSelenium3DockerMultiBrowserDriverSetup;
import de.project.web.delegates.WebDriverExtended;
import de.project.web.pageobjects.GooglePage;
import de.project.web.pageobjects.NavigatePage;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static de.project.web.utils.PropertiesFileUtil.getNavigationURL;
import static de.project.web.utils.PropertiesFileUtil.getProperty;
import static de.project.web.utils.WaitUtil.sleep;
import static de.project.web.utils.WebElementValidationUtil.elementDisplayedValidation;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class StepsDecorator extends AbstractSelenium3DockerMultiBrowserDriverSetup {

    private static Logger LOG = LoggerFactory.getLogger(StepsDecorator.class);
    private List<String> auth = new ArrayList<>();
    private WebDriverExtended driver = getWebDriverExtended();

    @When("^the user navigates to a specific URL$")
    public void goToURL(List<String> details) {
        String url = getNavigationURL(details.get(0));
        try {
            driver.getPageSource();
            NavigatePage.toGlobalLink(driver, url);
            LOG.info(String.format("Successfully navigated to URL '%s'", url));
        } catch (Exception e) {
            LOG.error(String.format("Current build environment cannot be accessed! Please check"
                    + " the provided URL '%s'", url));
            Assert.fail(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'.", url));
        }
    }

    @Given("^the test environment is available$")
    public void verifyIfTestEnvIsAvailable() {
        WebElement we = GooglePage.googleLogo(driver);
        elementDisplayedValidation(driver, we);
    }

    @Given("^the login page is displayed$")
    public void navigateToUrl() {
        sleep(2000);
    }

    @When("^the user inserts text in the user search field$")
    public void inputCredentials(List<String> credentials) {
        elementDisplayedValidation(driver, GooglePage.googleTextField(driver));
        GooglePage.googleTextField(driver).clear();
        GooglePage.googleTextField(driver).sendKeys(getProperty(credentials.get(0)));
        LOG.info(String.format("Filled the text field with '%s'", credentials.get(0)));
        GooglePage.googleLogo(driver).click();
        sleep(500);
    }


    @When("^the user clicks on a specific button$")
    public void googleSearch(List<String> buttonName) {
        WebElement we = GooglePage.googleSearch(driver, buttonName.get(0));
        elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'Google Search'");
    }

}
