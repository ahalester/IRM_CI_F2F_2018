package de.aqua.web.browser.steps;

import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.aqua.web.browser.setup.AbstractSelenium3DockerMultiBrowserDriverSetup;
import de.aqua.web.browser.setup.Selenium3DockerMultiBrowserAbstractSetup;
import de.aqua.web.delegates.WebDriverExtended;
import de.aqua.web.pageobjects.*;
import de.aqua.web.utils.*;
import de.aqua.web.utils.enums.PageTitle;
import de.aqua.web.utils.enums.Pipelines;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.event.KeyEvent;
import java.util.*;

import static de.aqua.web.utils.PropertiesFileUtil.*;
import static de.aqua.web.utils.ReadWriteTextToFileUtil.readFile;
import static de.aqua.web.utils.ReadWriteTextToFileUtil.writeFile;
import static de.aqua.web.utils.RuntimeUtil.*;
import static de.aqua.web.utils.StringUtil.*;
import static de.aqua.web.utils.WaitUtil.*;
import static de.aqua.web.utils.WebElementValidationUtil.elementDisplayedValidation;
import static de.aqua.web.utils.enums.EnvironmentURL.*;


/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class StepsDecorator extends AbstractSelenium3DockerMultiBrowserDriverSetup {

    private static Logger LOG = LoggerFactory.getLogger(StepsDecorator.class);
    private List<String> auth = new ArrayList<>();
    private WebDriverExtended driver = getWebDriverExtended();

    @Given("^the test environment is available$")
    public void verifyIfTestEnvIsAvailable(List<String> details) {
        bufferMap.clear();
        String url = "";
        String envName = System.getProperty("envPhase");
        String appVersion = details.get(0);
        try {
            driver.getPageSource();
            if (envName.equalsIgnoreCase("PHAB")) {
                switch (appVersion.toUpperCase()) {
                    case "QA0": {
                        url = getNavigationURL(QA0, System.getProperty("testEnv"));
                        break;
                    }
                    case "QA2": {
                        url = getNavigationURL(QA2, System.getProperty("testEnv"));
                        break;
                    }
                    default: {
                        break;
                    }
                }
            } else if (envName.equalsIgnoreCase("PHAA")) {
                url = getNavigationURL(PHA_URL, System.getProperty("testEnv").toUpperCase());
            }
            NavigatePage.toGlobalLink(driver, url);
            elementDisplayedValidation(driver, LoginPage.userField(driver), "Userid", 1);
        } catch (Exception e) {
            LOG.error(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
            Assert.fail(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
        }
    }

    @Given("^the \"([^\"]*)\" test environment is available$")
    public void verifyIfTestEnvIsAvailable(String envName) {
        String url = "";
        try {
            driver.getPageSource();
            switch (envName.toUpperCase()) {
                case "QA0": {
                    url = getNavigationURL(QA0, System.getProperty("testEnv"));
                    break;
                }
                case "QA2": {
                    url = getNavigationURL(QA2, System.getProperty("testEnv"));
                    break;
                }
                case "PHASEA": {
                    url = getNavigationURL(PHA_URL, System.getProperty("testEnv").toUpperCase());
                    break;
                }
                default: {
                    break;
                }
            }
            NavigatePage.toGlobalLink(driver, url);
            elementDisplayedValidation(driver, LoginPage.userField(driver));
        } catch (Exception e) {
            LOG.error(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
            Assert.fail(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
        }
    }

    @Given("^QA0 test environment is available$")
    public void verifyIfQA0TestEnvIsAvailable() {
        String url = null;
        try {
            driver.getPageSource();
            url = getNavigationURL(QA0, System.getProperty("testEnv"));
            NavigatePage.toGlobalLink(driver, url);
            elementDisplayedValidation(driver, LoginPage.userField(driver));
            LOG.info(String.format("Successfully navigated to URL '%s'", url));
        } catch (Exception e) {
            LOG.error(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
            Assert.fail(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
        }
    }

    @Given("^QA2 test environment is available$")
    public void verifyIfQA2TestEnvIsAvailable() {
        String url = null;
        try {
            driver.getPageSource();
            url = getNavigationURL(QA2, System.getProperty("testEnv"));
            NavigatePage.toGlobalLink(driver, url);
            elementDisplayedValidation(driver, LoginPage.userField(driver));
            LOG.info(String.format("Successfully navigated to URL '%s'", url));
        } catch (Exception e) {
            LOG.error(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
            Assert.fail(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
        }
    }

    @When("^the user navigates to a specific URL$")
    public void goToURL(List<String> details) {
        String url = getNavigationURL(details.get(0), ENV_VERSION);
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

    @Given("^AQUA login page is displayed$")
    public void navigateToUrl() {
        elementDisplayedValidation(driver, LoginPage.userField(driver));
        sleep(2000);
    }

    @When("^the user fills the credentials$")
    public void inputCredentials(List<String> credentials) {
        auth.add(credentials.get(0));
        auth.add(credentials.get(1));
        elementDisplayedValidation(driver, LoginPage.userField(driver));
        LoginPage.userField(driver).clear();
        LoginPage.userField(driver).sendKeys(getProperty(credentials.get(0)));
        LOG.info(String.format("Filled the '%s' field with '%s'", credentials.get(0),
                getProperty(credentials.get(0))));

        elementDisplayedValidation(driver, LoginPage.passwordField(driver));
        LoginPage.passwordField(driver).clear();
        LoginPage.passwordField(driver)
                .sendKeys(EncryptUtil.decrypt(getProperty(credentials.get(1))));
        LOG.info(String.format("Filled the '%s' field with '%s' (encrypted)", credentials.get(1),
                getProperty(credentials.get(1))));
    }

    @When("^the user checks the warn checkbox$")
    public void checkWarn() {
        WebElement we = LoginPage.warnCheckbox(driver);
        elementDisplayedValidation(driver, we);
        if (!we.isSelected()) {
            we.click();
            LOG.info(String.format("Clicked on '%s'", we.getAttribute("id")));
        }
    }

    @When("^the user unchecks the warn checkbox$")
    public void unCheckWarn() {
        WebElement we = LoginPage.warnCheckbox(driver);
        elementDisplayedValidation(driver, we);
        if (we.isSelected()) {
            LOG.info(String.format("Clicked on '%s'", we.getAttribute("id")));
            we.click();
        }
    }

    @When("^the user clicks the LOGIN button$")
    public void clickLogin() {
        WebElement we = LoginPage.loginButton(driver);
        elementDisplayedValidation(driver, we);
        we.click();
        waitForLoadingProgressNotDisplayed(driver);
        LOG.info("Clicked on 'Login'");
        sleep(3000);
    }

    @Then("^the QA0 view is displayed$")
    public void qa0ViewAvailable() {
        WebElement we = QA0Page.qa0MainView(driver);
        elementDisplayedValidation(driver, we, "QA0 view",
                DEFAULT_WAIT_FOR_ELEMENT);
    }

    @Then("^the user is authenticated$")
    public void userAuthenticated(List<String> user) {
        //        WebElement we = GeneralPage.toolBarButton(driver, getElementName(user.get(0)));
        //        elementDisplayedValidation(driver, we, getElementName(user.get(0)));
        //        WebElement we = QA0Page.userLabel(driver);
        String userName = getProperty(user.get(0));
        WebElement we = null;
        try {
            we = GeneralPage.label(driver, userName);
        } catch (Exception e) {
            Assert.fail("Something went wrong during the authentication process!");
        }
        elementDisplayedValidation(driver, we, getElementName(user.get(0)),
                DEFAULT_WAIT_FOR_ELEMENT);
        String loggedUser = we.getText();
        Assert.assertTrue(String.format("The logged user %s is different by the mentioned one,"
                        + " %s!", loggedUser, user.get(0)),
                loggedUser.equalsIgnoreCase(getProperty(user.get(0))));
    }

    @When("^the user clicks on QA2 tab$")
    public void qa2TabClick() {
//        WebElement we = QA0Page.qa2Tab(driver);
//        elementDisplayedValidation(driver, we);
//        we.click();
        clickOnSpecificTab(Collections.singletonList("QA2"));
//        LOG.info("Clicked on 'QA2' tab");
        sleep(2000);
    }

    @When("^the user clicks on \"([^\"]*)\" tab$")
    public void qaTabClick(String tabName) {
        clickOnSpecificTab(Collections.singletonList(tabName));
        LOG.info(String.format("Clicked on '%s' tab", tabName));
        sleep(2000);
    }

    @When("^the \"([^\"]*)\" tab is not displayed$")
    public void toolTabNotDisplayed(String tabName) {
        try {
            GeneralPage.qaTab(driver, tabName).isDisplayed();
            Assert.fail(String.format("The '%s' tab is available on the page!", tabName));
        } catch (Exception e) {
            LOG.info(String.format("The '%s' tab is not available on the page anymore!", tabName));
        }
    }

    @When("^the specific search tab is selected$")
    public void searchEB(List<String> searchTab) {
        String search = getElementName(searchTab.get(0));
        WebElement we = QA0Page.searchEBTabs(driver, search);
        elementDisplayedValidation(driver, we, search);
        we.click();
        LOG.info(String.format("Clicked on '%s'", search));
        Assert.assertTrue(String.format("The tab %s is not selected!", searchTab.get(0)),
                we.getAttribute("class").contains("selected"));
    }

    @When("^the user clicks on a specific text tab$")
    public void clickOnTextTab(List<String> details) {
        String tabName = getElementName(details.get(0));
        WebElement we = GeneralPage.qaTab(driver, tabName);
        elementDisplayedValidation(driver, we);
        we.click();
        LOG.info(String.format("Clicked on '%s'", tabName));
    }

    @Then("^the specific elements are available$")
    public void specificElementAvailable(DataTable table) {
        sleep(500);
        List<List<String>> elements = table.raw();
        for (List<String> element : elements) {
            String elem = element.get(1);
            String elementName = "";
            switch (elem.toLowerCase()) {
                case "label": {
                    elementName = getElementName(element.get(0));
                    elementDisplayedValidation(driver, GeneralPage.label(driver, elementName),
                            elementName);
                    break;
                }
                case "checkbox": {
                    elementName = getElementName(element.get(0));
                    elementDisplayedValidation(driver, GeneralPage.checkBox(driver, elementName),
                            elementName);
                    break;
                }
                case "datebox": {
                    elementName = getElementName(element.get(2));
                    elementDisplayedValidation(driver, GeneralPage.dateBox(driver, elementName),
                            elementName);
                    break;
                }
                case "textbox": {
                    elementName = getElementName(element.get(0));
                    elementDisplayedValidation(driver, GeneralPage.textBox(driver, elementName),
                            elementName);
                    break;
                }
                case "combobox": {
                    elementName = getElementName(element.get(0));
                    elementDisplayedValidation(driver, GeneralPage.comboBox(driver, elementName),
                            elementName);
                    break;
                }
                case "button": {
                    elementName = getElementName(element.get(0));
                    elementDisplayedValidation(driver, GeneralPage.button(driver, elementName),
                            elementName);
                    break;
                }
                case "doublebox": {
                    elementName = getElementName(element.get(0));
                    elementDisplayedValidation(driver, GeneralPage.doubleBox(driver, elementName),
                            elementName);
                    break;
                }
                default: {
                    break;
                }
            }
            LOG.info(String.format("The element '%s' is available on the page", elementName));
        }
    }

    @Then("^the specific checkbox is checked/unchecked$")
    public void elementDefaultStatus(DataTable table) {
        List<List<String>> elements = table.raw();
        for (List<String> element : elements) {
            String checkBoxName = getElementName(element.get(0));
            WebElement we = GeneralPage.checkBox(driver, checkBoxName);
            if (element.get(1).equalsIgnoreCase("checked")
                    || element.get(1).equalsIgnoreCase("selected")
                    || element.get(1).equalsIgnoreCase("ticked")) {
                Assert.assertTrue(String.format("The default state of the checkbox %s is not %s!",
                        checkBoxName, element.get(1)), we.isSelected());
                LOG.info(String.format("The checkbox '%s' is checked", checkBoxName));
            } else if (element.get(1).equalsIgnoreCase("unchecked")
                    || element.get(1).equalsIgnoreCase("unselected")
                    || element.get(1).equalsIgnoreCase("unticked")) {
                Assert.assertTrue(String.format("The default state of the checkbox %s is not %s!",
                        checkBoxName, element.get(1)), !we.isSelected());
                LOG.info(String.format("The checkbox '%s' is unchecked", checkBoxName));
            }
        }
    }

    @Then("^the QA2 view is displayed$")
    public void qa2ViewAvailable() {
        elementDisplayedValidation(driver, QA2Page.qa2MainView(driver), "QA2 view");
    }

    @Then("^the user checks/un-checks a specific checkbox$")
    public void checkboxEvent(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            sleep(500);
            String name = getElementName(detail.get(0));
            String action = detail.get(1);
            WebElement we = GeneralPage.checkBox(driver, name);
            switch (action) {
                case "check": {
                    elementDisplayedValidation(driver, we, name);
                    we.click();
                    break;
                }
                case "uncheck": {
                    elementDisplayedValidation(driver, we, name);
                    we.click();
                    break;
                }
                default: {
                    break;
                }
            }
            LOG.info(String.format("Clicked on '%s'", name));
        }
    }

    @Then("^the user checks/un-checks all checkboxes$")
    public void allCheckboxesEvent(List<String> checkboxEvent) {
        Map<WebElement, String> properties = GeneralPage.visibleCheckboxesProperties(driver);
        for (Map.Entry<WebElement, String> entry : properties.entrySet()) {
            WebElement we = entry.getKey();
            String name = entry.getKey().findElement(By.tagName("label")).getText();
            switch (checkboxEvent.get(0)) {
                case "check": {
                    elementDisplayedValidation(driver, we);
                    if (entry.getValue().equals("false")) {
                        we.findElement(By.tagName("input")).click();
                        LOG.info(String.format("Clicked on '%s'", name));
                    }
                    break;
                }
                case "uncheck": {
                    elementDisplayedValidation(driver, we);
                    if (entry.getValue().equals("true")) {
                        we.findElement(By.tagName("input")).click();
                        LOG.info(String.format("Clicked on '%s'", name));
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    @Then("^the user checks if the OUS are displayed$")
    public void checkOUSListDisplayed() {
        if (GeneralPage.westPageArea(driver).getAttribute("innerHTML")
                .contains("Empty ObsUnitSet list")) {
            QA2Page.searchButton(driver).click();
            LOG.info("Clicked on 'Search'");
            if (driver.getCurrentUrl().contains("eso.org")) {
                sleep(3000);
            } else {
                waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                        DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
            }
        }
        GeneralPage.collapseView(driver).click();
        sleep(1000);
    }

    @When("^the user selects one of the available OUS")
    public void selectObsUnitSet(List<String> details) {
        WebElement we = QA2Page.selectObsUnitSet(driver, details.get(0));
        elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on one of the available OUSes");
        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @When("^the user selects the first available OUS")
    public void selectFirstObsUnitSet() {
        WebElement we = null;
        try {
            if (GeneralPage.noItemDisplayed(driver)
                    .getCssValue("display").equalsIgnoreCase("none")) {
                we = QA2Page.selectFirstObsUnitSet(driver);
                try {
                    elementDisplayedValidation(driver, we, "first OUS");
                    we.click();
                    waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                            DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                } catch (Exception ex) {
                    Assert.fail("No OUS to be displayed based on the provided filter!");
                }
            } else {
                Assert.fail("No OUS to be displayed based on the provided filter!");
            }
        } catch (Exception e) {
            we = QA2Page.selectFirstObsUnitSet(driver);
            try {
                elementDisplayedValidation(driver, we, "first OUS");
                we.click();
                waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                        DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
            } catch (Exception ex) {
                Assert.fail("No OUS to be displayed based on the provided filter!");
            }
        }
        elementDisplayedValidation(driver, we, "first OUS");
        LOG.info("Clicked on the first available OUS");
    }

    @Then("^the OUS Summary details page is displayed$")
    public void ousSummaryDisplayed() {
        if (driver.getCurrentUrl().contains("eso.org")) {
            sleep(5000);
        } else {
            sleep(5000);
        }
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.summary(driver), "OUS Summary");
    }

//    @When("^the user selects a specific OUS state$")
//    public void setOUSState(List<String> details) {
//        for (String detail : details) {
//            List<String> elements = split(detail);
//            WebElement we = QA2Page.textBox(driver, elements.get(0));
//            elementDisplayedValidation(driver, we);
//            we.sendKeys(elements.get(1));
//        }
//    }

//    @Then("^the Data reduction methods are displayed$")
//    public void dataReductionMethodsDisplayed(List<String> details) {
//        String rm = extractMapValue(bufferMap, details.get(0));
//        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver,
//                rm));
//    }

    @Then("^the user checks if the specific data is different$")
    public void validateDataIsDifferent(List<String> details) {
        String tagLabelName = getElementName(details.get(0));
        String data = extractMapValue(bufferMap, tagLabelName);
        String currentData = GeneralPage.getDynamicLabelByTagLabel(driver, tagLabelName)
                .getAttribute("innerHTML");
        Assume.assumeTrue("The displayed data is the same with the baseline!",
                !Objects.equals(data, currentData));
    }

    @Then("^the user checks if data reducer is assigned$")
    public void checkIfDataReducerIsAssigned(DataTable table) {
        List<List<String>> details = table.raw();
        String tagLabelName = getElementName(details.get(0).get(0));
        String nextLabel = getElementName(details.get(0).get(1));
        String dr = "";
        try {
            dr = OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, tagLabelName)
                    .getAttribute("innerHTML");
        } catch (Exception e) {
            LOG.error(String.format("Element '%s' was not found", tagLabelName));
        }
        if (dr.length() < 5 || dr.equalsIgnoreCase(nextLabel)) {
            //  Assign Data reducer
            OBSUnitSetSummaryPage.clickToAdd(driver, tagLabelName).click();
            LOG.info("Clicked on 'Data Reducer'");
            sleep(2000);
            OBSUnitSetSummaryPage.searchDataReducerIcon(driver).click();
            LOG.info("Clicked on 'search' icon");
            sleep(1000);
            OBSUnitSetSummaryPage.selectFirstDataReducer(driver).click();
            LOG.info("Clicked on first displayed data reducer");
            sleep(2000);
            OBSUnitSetSummaryPage.dataReducerPopupButton(driver, "save").click();
            LOG.info("Clicked on 'Save'");
            sleep(1000);
            //  waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
            //  DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        }
        bufferMap.put("Data reducer",
                OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, tagLabelName)
                        .getAttribute("innerHTML"));
    }

    @Then("^the user assigns a specific data reducer$")
    public void assignDataReducer(DataTable table) {
        List<List<String>> details = table.raw();
        String tagLabelName = getElementName(details.get(0).get(0));
        String nextLabel = getElementName(details.get(0).get(1));
        WebElement we = OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, tagLabelName);
        String dr = we.getAttribute("innerHTML");
        if (dr.contains(details.get(0).get(2))) {
            //  Clear the existing Data reducer
            OBSUnitSetSummaryPage.clickToAdd(driver, tagLabelName).click();
            LOG.info("Clicked on 'Data Reducer'");
            sleep(2000);
            OBSUnitSetSummaryPage.dataReducerPopupButton(driver, "save").click();
            LOG.info("Clicked on 'Save'");
            sleep(2000);
        }
        //  Assign Data reducer
        OBSUnitSetSummaryPage.clickToAdd(driver, tagLabelName).click();
        //  OBSUnitSetSummaryPage.searchDataReducerIcon(driver).click();
        LOG.info("Clicked on 'Data Reducer'");
        sleep(2000);
        OBSUnitSetSummaryPage.searchDataReducerTextField(driver).sendKeys(details.get(0).get(2));
        LOG.info(String.format("Filled the text field with '%s'", details.get(0).get(2)));
        sleep(2000);
        //OBSUnitSetSummaryPage.selectFirstDataReducer(driver).click();
        OBSUnitSetSummaryPage.selectDataReducer(driver, details.get(0).get(2)).click();
        LOG.info("Clicked on first displayed data reducer");
        sleep(2000);
        OBSUnitSetSummaryPage.dataReducerPopupButton(driver, "save").click();
        LOG.info("Clicked on 'Save'");
        try {
            WebElement element = GeneralPage.loadingProgress(driver);
            //  waitForLocatedElementIsNotDisplayed(driver, element,
            //  DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        } catch (Exception e) {
            LOG.info("'Loading progress' not displayed");
        }
        bufferMap.put("Data reducer",
                OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, tagLabelName)
                        .getAttribute("innerHTML"));
    }

    @Then("^the user assigns a specific drm$")
    public void assignDRM(DataTable table) {
        List<List<String>> details = table.raw();
        String tagLabelName = PropertiesFileUtil.getElementName(details.get(0).get(0));
        WebElement we = OBSUnitSetSummaryPage.drm(driver, tagLabelName);
        String drm = we.getAttribute("innerHTML");
        //  Assign DRM
        OBSUnitSetSummaryPage.clickToAdd(driver, tagLabelName).click();
        //  OBSUnitSetSummaryPage.searchDataReducerIcon(driver).click();
        LOG.info("Clicked on 'DRM'");
        WaitUtil.sleep(2000);
        OBSUnitSetSummaryPage.searchDataReducerTextField(driver).sendKeys(details.get(0).get(1));
        LOG.info(String.format("Filled the text field with '%s'", details.get(0).get(1)));
        WaitUtil.sleep(2000);
        OBSUnitSetSummaryPage.selectDataReducer(driver, details.get(0).get(1)).click();
        LOG.info("Clicked on first displayed DRM");
        WaitUtil.sleep(2000);
        OBSUnitSetSummaryPage.dataReducerPopupButton(driver, "save").click();
        LOG.info("Clicked on 'Save'");
        try {
            WebElement element = GeneralPage.loadingProgress(driver);
        } catch (Exception e) {
            LOG.info("'Loading progress' not displayed");
        }
    }

    @Then("^the user adds a specific comment recipient$")
    public void addCommentRecipient(DataTable table) {
        List<List<String>> details = table.raw();
        String buttonName = getElementName(details.get(0).get(0));
        WebElement we = OBSUnitSetSummaryPage.toolBarButton(driver, buttonName);
        driver.executeScript("arguments[0].scrollIntoView(true);", we);
        //  Add new comment recipient
        we.click();
        LOG.info(String.format("Clicked on '%s'", buttonName));
        sleep(2000);
        OBSUnitSetSummaryPage.searchDataReducerTextField(driver).sendKeys(details.get(0).get(1));
        LOG.info(String.format("Filled the text field with '%s'", details.get(0).get(1)));
        sleep(2000);
        OBSUnitSetSummaryPage.selectFirstDataReducer(driver).click();
        LOG.info("Clicked on first displayed recipient");
        sleep(2000);
        OBSUnitSetSummaryPage.commentRecipientsPopupButton(driver, "save").click();
        LOG.info("Clicked on 'Save'");
        try {
            WebElement element = GeneralPage.loadingProgress(driver);
            //  waitForLocatedElementIsNotDisplayed(driver, element,
            //  DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        } catch (Exception e) {
            LOG.info("'Loading progress' not displayed");
        }
    }

    @Then("^the Data reducer is displayed$")
    public void dataReducerDisplayed(List<String> details) {
        String dr = extractMapValue(bufferMap, getElementName(details.get(0)));
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver,
                dr));
    }

    @Then("^the specific information is displayed$")
    public void dynamicLabelContentDisplayed(List<String> details) {
        String dr = extractMapValue(bufferMap, details.get(0));
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver,
                dr));
    }

    @Then("^the specific label is displayed$")
    public void specificLabelContentDisplayed(List<String> details) {
        sleep(1000);
        for (String labelName : details) {
            if (labelName.equalsIgnoreCase("comment")) {
                labelName = extractMapValue(bufferMap, labelName);
            } else {
                labelName = getElementName(labelName);
            }
            elementDisplayedValidation(driver, GeneralPage.specificLabel(driver, labelName), labelName);
        }
    }

    @Then("^the source intent is alongside the source name$")
    public void labelTextLength(List<String> details) {
        sleep(1000);
        for (String labelName : details) {
            labelName = getElementName(labelName);
            WebElement we = GeneralPage.specificLabel(driver, labelName);
            elementDisplayedValidation(driver, we, labelName);
            Assert.assertTrue(String.format("The '%s' label is not followed by any other string!",
                    labelName), we.getAttribute("innerHTML").length() > labelName.length());
        }
    }

    @Then("^the Data reducer is no longer displayed$")
    public void dataReducerNotDisplayed(List<String> details) {
        String tagLabelName = getElementName(details.get(0));
        String dr = extractMapValue(bufferMap, tagLabelName);
        Assert.assertTrue(String.format("Data reducer %s is still displayed!", tagLabelName),
                OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, dr) == null);
    }

    @Then("^the specific element is no longer displayed$")
    public void labelNotDisplayed(List<String> details) {
        String labelName = getElementName(details.get(0));
        boolean displayed = false;
        try {
            //  TODO - to be implemented for a generic element not only a label
            WebElement we = GeneralPage.label(driver, labelName);
            driver.executeScript("arguments[0].scrollIntoView(true);", we);
            elementDisplayedValidation(driver, we, labelName);
        } catch (Exception e) {
            displayed = true;
        }
        Assert.assertTrue(String.format("The label '%s' is still available on the page!",
                labelName), displayed);
    }

    @Then("^the element is no longer displayed$")
    public void notDisplayed(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String elementType = detail.get(0);
            String elementName = getElementName(detail.get(1));
            boolean displayed = false;
            try {
                WebElement we = null;
                switch (elementType) {
                    case "label": {
                        we = GeneralPage.label(driver, elementName);
                        break;
                    }
                    case "icon": {
                        String comment = extractMapValue(bufferMap, detail.get(2));
                        we = OBSUnitSetSummaryPage.editComment(driver, comment);
                        break;
                    }
                    case "button": {
                        we = GeneralPage.button(driver, elementName);
                        break;
                    }
                }
                driver.executeScript("arguments[0].scrollIntoView(true);", we);
                elementDisplayedValidation(driver, we, elementName);
            } catch (Exception e) {
                displayed = true;
            }
            Assert.assertTrue(String.format("The element '%s' is still available on the page!",
                    elementName), displayed);
        }
    }

    @When("^the user clicks on a specific button$")
    public void clickOnButton(List<String> details) {
        String buttonName = getElementName(details.get(0));
        List<WebElement> saveButtons = GeneralPage.sameNameVisibleButtons(driver, buttonName);
        if (saveButtons.size() < 2) {
            try {
                WebElement we = GeneralPage.button(driver, buttonName);
                elementDisplayedValidation(driver, we, buttonName);
                we.click();
            } catch (Exception e) {
                WebElement we = OBSUnitSetSummaryPage.button(driver, buttonName);
                elementDisplayedValidation(driver, we, buttonName);
                we.click();
            }
            if (buttonName.equalsIgnoreCase("DO QA2")) {
                sleep(1000);
            } else {
                try {
                    WebElement we = GeneralPage.divBlocker(driver);
                    //                if (we.isDisplayed()) {
                    //                    waitForLocatedElementIsNotDisplayed(driver, we,
                    //                            DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                    //                }
                } catch (Exception e) {
                    try {
                        WebElement we = GeneralPage.loadingProgress(driver);
                        if (we.isDisplayed()) {
                            waitForLocatedElementIsNotDisplayed(driver, we,
                                    DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                        }
                    } catch (Exception e1) {
                        sleep(5000);
                    }
                }
            }
        } else {
            saveButtons.get(1).click();
        }
        if (details.get(0).equalsIgnoreCase("search"))
            GeneralPage.collapseView(driver).click();
        LOG.info(String.format("Clicked on '%s'", buttonName));
        sleep(500);
    }

    @When("^the user clicks on a specific link$")
    public void clickOnLink(List<String> details) {
        WebElement we = GeneralPage.link(driver, getElementName(details.get(0)));
        elementDisplayedValidation(driver, we);
        we.click();
        LOG.info(String.format("Clicked on '%s'", details.get(0)));
    }

    @When("^the user clicks on a specific checkbox$")
    public void clickOnCheckbox(List<String> details) {
        Map<WebElement, String> properties = GeneralPage.visibleCheckboxesProperties(driver);
        for (Map.Entry<WebElement, String> entry : properties.entrySet()) {
            WebElement we = entry.getKey();
            String name = entry.getKey().findElement(By.tagName("label")).getText();
            String elementName = getElementName(details.get(0));
            if (name.equals(elementName)) {
                elementDisplayedValidation(driver, we, elementName);
                we.findElement(By.tagName("input")).click();
                LOG.info(String.format("Clicked on '%s'", name));
            }
        }
    }

    @When("^the user clicks on a specific button without wait$")
    public void clickOnButtonNoWait(List<String> details) {
        String buttonName = getElementName(details.get(0));
        List<WebElement> saveButtons = GeneralPage.sameNameVisibleButtons(driver, buttonName);
        if (saveButtons.size() < 2) {
            try {
                WebElement we = OBSUnitSetSummaryPage.button(driver, buttonName);
                elementDisplayedValidation(driver, we, buttonName);
                we.click();
            } catch (Exception e) {
                WebElement we = GeneralPage.button(driver, buttonName);
                elementDisplayedValidation(driver, we, buttonName);
                we.click();
            }
        } else {
            saveButtons.get(1).click();
        }
        LOG.info(String.format("Clicked on '%s'", buttonName));
        sleep(5000);
    }

    @When("^the user clicks on a specific toolbar button$")
    public void clickOnToolbarButton(List<String> details) {
        WebElement we = GeneralPage.toolBarButton(driver, getElementName(details.get(0)));
        elementDisplayedValidation(driver, we, getElementName(details.get(0)));
        we.click();
        if (details.get(0).equalsIgnoreCase("release_info")) {
            LOG.info(String.format("Clicked on '%s'", we.getAttribute("innerHTML")));
        } else {
            LOG.info(String.format("Clicked on '%s'", getElementName(details.get(0))));
        }
//        if (details.get(0).equalsIgnoreCase("weblog")) {
//            BrowserUtil.switchWindow(driver);
//            String newURL = driver.getCurrentUrl();
//            newURL = getProperty("username") + ":" + EncryptUtil.decrypt(getProperty("password"))
//                    + "@" + newURL;
//            driver.gotoUrl(newURL);
//        }
//        try {
//            we.click();
//        } catch (Exception e) {
        if (details.get(0).equalsIgnoreCase("weblog")) {
            LOG.info("Authentication required pop-up has occurred!");
            sleep(5000);
            try {
                SmartRobotUtil robot = new SmartRobotUtil();
                robot.type(getProperty("username"));
                robot.keyPress(KeyEvent.VK_TAB);
                robot.type(EncryptUtil.decrypt(getProperty("password")));
                robot.keyPress(KeyEvent.VK_ENTER);
            } catch (Exception AWTException) {
                LOG.info("Exception \n" + AWTException.getMessage());
            }
            sleep(3000);
        }
//        }
    }

    @Then("^the Do QA2 pop-up form is displayed$")
    public void doQA2Displayed() {
        sleep(500);
        elementDisplayedValidation(driver, GeneralPage.doQAXForm(driver), "pop-up form");
    }

    @Then("^the Do QA0 pop-up form is displayed$")
    public void doQA0Displayed() {
        sleep(500);
        elementDisplayedValidation(driver, GeneralPage.doQAXForm(driver));
    }

    @When("^the user clicks on the Set QA0 Status$")
    public void clickQA2Status() {
        WebElement we = GeneralPage.setQAXStatus(driver);
        elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'Set QA0 Status'");
    }

    @When("^the user clicks on the Set QA2 Status$")
    public void clickQAXStatus() {
        WebElement we = GeneralPage.setQAXStatus(driver);
        elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'Set QA2 Status'");
    }

    @When("^the user selects specific status$")
    public void selectQA2Status(List<String> details) {
        WebElement we = OBSUnitSetSummaryPage.selectStatus(driver, getElementName(details.get(0)));
//        elementDisplayedValidation(driver, we, getElementName(details.get(0)));
        bufferMap.put("status", we.getAttribute("innerHTML"));
        we.click();
        LOG.info(String.format("Clicked on '%s'", getElementName(details.get(0))));
    }

    @Then("^the confirmation pop-up is displayed$")
    public void confirmationPopupDisplayed() {
        sleep(1500);
        elementDisplayedValidation(driver, GeneralPage.dialogBox(driver),
                "confirmation dialog");
    }

    @When("^the user clicks one of the popup's buttons$")
    public void clickPopupButton(List<String> details) {
        sleep(500);
        waitForElement(driver, By.className(""), 10);
        WebElement we = GeneralPage.dialogBoxActionButton(driver, getElementName(details.get(0)));
        elementDisplayedValidation(driver, we, getElementName(details.get(0)));
        we.click();
        LOG.info(String.format("Clicked on '%s'", details.get(0)));
        try {
            GeneralPage.loadingProgress(driver).isDisplayed();
            waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                    DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        } catch (Exception e) {
            sleep(3000);
        }
    }

    @When("^the user clicks on a specific button on Do QA2 page$")
    public void clickOnDoQA2Button(List<String> details) {
        WebElement we = OBSUnitSetSummaryPage.doQA2FormButton(driver, getElementName(details.get(0)));
        elementDisplayedValidation(driver, we, getElementName(details.get(0)));
        we.click();
        LOG.info(String.format("Clicked on '%s'", details.get(0)));
    }

    @When("^the user clicks on QA0 Status Reason$")
    public void clickOnQA0StatusReason() {
        sleep(2000);
        WebElement we = GeneralPage.doQAXFormStatusReason(driver);
        elementDisplayedValidation(driver, we, "Status Reason");
        we.click();
        LOG.info("Clicked on 'QA0 Status Reason'");
    }

    @When("^the user clicks on QA2 Status Reason$")
    public void clickOnQA2StatusReason() {
        sleep(2000);
        WebElement we = GeneralPage.doQAXFormStatusReason(driver);
        elementDisplayedValidation(driver, we, "Status Reason");
        we.click();
        LOG.info("Clicked on 'QA2 Status Reason'");
    }

    @Then("^the QA0 Status Reason options are displayed$")
    public void qa0StatusReasonListDisplayed() {
        elementDisplayedValidation(driver, GeneralPage.doQAXFormStatusReasonList(driver));
    }

    @Then("^the QA2 Status Reason options are displayed$")
    public void qa2StatusReasonListDisplayed() {
        sleep(1500);
        elementDisplayedValidation(driver, GeneralPage.doQAXFormStatusReasonList(driver),
                "QA2 Status Reason options");
    }

    @Then("^the QA2 Status Reason option is displayed$")
    public void qa2StatusReasonDisplayed(List<String> details) {
        sleep(1500);
        String status = getElementName(details.get(0));
        elementDisplayedValidation(driver, GeneralPage.doQAXFormStatusReasonDisplayed(driver,
                status), status);
    }

//    @Then("^the user selects one QA2 Reason status by name$")
//    public void qa2StatusReasonStatusSelectByName(List<String> status) {
//        WebElement we = OBSUnitSetSummaryPage.doQA2FormReasonStatusOptionName(driver, status.get(0));
//        elementDisplayedValidation(driver, we);
//        we.click();
//    }

    @Then("^the user selects one QA0 Reason status by position$")
    public void qa0StatusReasonStatusSelectByPosition(List<String> details) {
        if (details.size() > 0 && StringUtils.isNumeric(details.get(0))
                && StringUtils.isNotBlank(details.get(0))) {
            WebElement we = GeneralPage.doQAXFormReasonStatusOptionPosition(driver, details.get(0));
            elementDisplayedValidation(driver, we);
            we.click();
        } else {
            WebElement we = GeneralPage.doQAXFormReasonStatusOptionPosition(driver);
            elementDisplayedValidation(driver, we);
            we.click();
        }
        LOG.info(String.format("Clicked on 'QA0 Reason status' from position '%s'",
                details.get(0)));
    }

    @Then("^the user selects one QA2 Reason status by position$")
    public void qa2StatusReasonStatusSelectByPosition(List<String> details) {
        WebElement we;
        if (details.size() > 0 && StringUtils.isNumeric(details.get(0))
                && StringUtils.isNotBlank(details.get(0))) {
            we = GeneralPage.doQAXFormReasonStatusOptionPosition(driver, details.get(0));
            elementDisplayedValidation(driver, we, details.get(0));
        } else {
            we = GeneralPage.doQAXFormReasonStatusOptionPosition(driver);
            elementDisplayedValidation(driver, we, details.get(0));
        }
        bufferMap.put("reason", we.getAttribute("innerHTML"));
        we.click();
        LOG.info(String.format("Clicked on 'QA2 Reason status' from position '%s'",
                details.get(0)));
    }

    @Then("^the user sets an EC value$")
    public void setManualECValue(List<String> details) {
        WebElement we = GeneralPage.doQAXFormEC(driver);
        elementDisplayedValidation(driver, we, "QA form");
        String currentEC = we.getAttribute("value");
        we.clear();
//        String newEC = String.valueOf(
//                Double.parseDouble(OBSUnitSetSummaryPage.doQA2FormECOriginalValue(driver)
//                        .getAttribute("innerHTML")) - Double.parseDouble(details.get(0)));
        String newEC;
        if (Double.parseDouble(details.get(0)) < 1) {
            newEC = String.valueOf(Double.parseDouble(details.get(0)));
        } else {
            newEC = details.get(0);
        }
//        bufferMap.put("EC_old", OBSUnitSetSummaryPage.doQA2FormECOriginalValue(driver)
//                .getAttribute("innerHTML"));
        bufferMap.put("EC_old", currentEC);
        bufferMap.put("EC_new", newEC);
        bufferMap.put("EF_old", "1");
        we.sendKeys(newEC);
    }

    @Then("^the state confirmation message box is displayed$")
    public void changeStateDialogBoxDisplayed() {
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.stateChangeMessageBox(driver));
    }

    @Then("^the dialog box with specific message is displayed$")
    public void infoECBoxDisplayed(List<String> details) {
        WebElement we = OBSUnitSetSummaryPage.infoECValue(driver);
        elementDisplayedValidation(driver, we);
        Assert.assertTrue(String.format("The message displayed into the EC info box is %s and it "
                        + "should be %s!", we.getAttribute("innerHTML"), details.get(0)),
                Objects.equals(we.getAttribute("innerHTML"), details.get(0)));
    }

    @When("^the user clicks OK on the EC dialog box$")
    public void confirmECBox() {
        WebElement we = OBSUnitSetSummaryPage.infoECokBtn(driver);
        elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'OK'");
    }

    @Then("^the OUS state is changed$")
    public void ousStateIsChanged(DataTable table) {
        List<List<String>> details = table.raw();
        String tagLabelName = getElementName(details.get(0).get(0));
        String newStatus = getElementName(details.get(0).get(1));
        WebElement we = OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, tagLabelName);
        elementDisplayedValidation(driver, we);
        Assert.assertTrue(String.format("The OUS state is %s and it should be %s!",
                we.getAttribute("innerHTML"), newStatus),
                we.getAttribute("innerHTML").contains(newStatus));
    }

    @Then("^the OUS state is not changed$")
    public void ousStateIsNotChanged(List<String> details) {
        String tagLabelName = getElementName(details.get(0));
        WebElement we = OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, tagLabelName);
        elementDisplayedValidation(driver, we);
        Assert.assertTrue(String.format("The OUS state is %s and it should be %s!",
                we.getAttribute("innerHTML"), buffer),
                Objects.equals(we.getAttribute("innerHTML"), buffer));
    }

    @Then("^the confirmation dialog contains the specific message$")
    public void validateTextOnConfirmationDialog(List<String> details) {
        elementDisplayedValidation(driver, GeneralPage.dialogBox(driver));
        WebElement we = OBSUnitSetSummaryPage.confirmationBoxMessage(driver);
        Assert.assertTrue(String.format("The text '%s' on the confirmation dialog doesn't "
                        + "contain '%s'!", we.getAttribute("innerHTML"), details.get(0)),
                we.getAttribute("innerHTML").contains(details.get(0)));
    }

    @Then("^the confirmation dialog no. \"([^\"]*)\" contains the specific message$")
    public void validateTextOnConfirmationDialogNo(String index, List<String> details) {
        elementDisplayedValidation(driver, GeneralPage.dialogBox(driver, Integer.parseInt(index)));
        WebElement we = OBSUnitSetSummaryPage.confirmationBoxMessage(driver,
                GeneralPage.dialogBox(driver, Integer.parseInt(index)));
        Assert.assertTrue(String.format("The text '%s' on the confirmation dialog doesn't "
                        + "contain '%s'!", we.getAttribute("innerHTML"), details.get(0)),
                we.getAttribute("innerHTML").contains(details.get(0)));
    }

    @When("^the user selects the first available EB$")
    public void selectFirstEB() {
//        WebElement we = QA0Page.selectFirstEB(driver);
//        elementDisplayedValidation(driver, we, "first EB");
//        we.click();
//        LOG.info("Clicked on first available EB");
//        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
//                DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        try {
            if (GeneralPage.noItemDisplayed(driver)
                    .getCssValue("display").equalsIgnoreCase("none")) {
                WebElement we = QA0Page.selectFirstEB(driver);
                try {
                    elementDisplayedValidation(driver, we, "first EB");
                    we.click();
                    waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                            DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                } catch (Exception ex) {
                    Assert.fail("No EB to be displayed based on the provided filter!");
                }
            } else {
                Assert.fail("No EB to be displayed based on the provided filter!");
            }
        } catch (Exception e) {
            WebElement we = QA0Page.selectFirstEB(driver);
            try {
                elementDisplayedValidation(driver, we, "first EB");
                we.click();
                waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                        DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
            } catch (Exception ex) {
                Assert.fail("No EB to be displayed based on the provided filter!");
            }
        }
        LOG.info("Clicked on the first available EB");
    }

    @Then("^the EB Summary details page is displayed$")
    public void ebSummaryDisplayed() {
        elementDisplayedValidation(driver, EBSummaryPage.summary(driver), "ExecBloc Summary");
        if (!driver.getCurrentUrl().contains("eso.org")) {
            if (GeneralPage.loadingProgressItems(driver) > 1) {
                waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                        DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
            }
        } else {
            sleep(3000);
        }
    }

    @Then("^wait for the loading progress to be completed$")
    public void loadingProgressAvailability() {
        try {
            waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                    DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        } catch (Exception ignored){}
    }

    @Then("^wait for dialog box to be visible$")
    public void waitForElementToBeVisible() {
        waitUntilVisible(driver, GeneralPage.dialogBox(driver));
    }

    @Then("^wait for element to be enabled$")
    public void waitForElementToBeEnabled(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            switch (detail.get(0)) {
                case "button": {
                    waitForDivBlockerEnabled(driver, GeneralPage.button(driver,
                            getElementName(detail.get(1))));
                    break;
                }
                default:
                    break;
            }

        }
        waitUntilVisible(driver, GeneralPage.dialogBox(driver));
    }

    @Then("^wait for dialog box no. \"([^\"]*)\" to be visible$")
    public void waitForDialogToBeVisible(String index) {
        waitUntilVisible(driver, GeneralPage.dialogBox(driver, Integer.parseInt(index)));
    }

    @Then("^the information dialog box is displayed$")
    public void informationDialogDisplayed() {
        elementDisplayedValidation(driver, GeneralPage.dialogBox(driver));
    }

    @Then("^the information dialog box message is consistent$")
    public void validateDialogMessage(List<String> details) {
        WebElement we = GeneralPage.dialogBoxMessage(driver);
        elementDisplayedValidation(driver, we, details.get(0));
        String msg = we.getAttribute("innerHTML");
        Assert.assertTrue(String.format("The text '%s' on the information dialog does't contain "
                + "the substring '%s'!", msg, details.get(0)), msg.contains(details.get(0)));
    }

    @When("^the user clicks on a specific button on the the information dialog box$")
    public void clickDialogBoxButton(List<String> details) {
        WebElement we = GeneralPage.dialogBoxActionButton(driver, getElementName(details.get(0)));
        elementDisplayedValidation(driver, we);
        we.click();
        LOG.info(String.format("Clicked on '%s'", details.get(0)));
        try {
            WebElement lp = GeneralPage.loadingProgress(driver);
            if (lp.isDisplayed()) {
                waitForLocatedElementIsNotDisplayed(driver, lp,
                        DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
            }
        } catch (Exception e1) {
            sleep(5000);
        }
    }

    @When("^the user clicks on a specific field$")
    public void clickOnSpecificField(DataTable table) {
        List<List<String>> details = table.raw();
        sleep(1000);
        WebElement we = QA2Page.ousStateFlagField(driver, getElementName(details.get(0).get(0)));
        elementDisplayedValidation(driver, we, getElementName(details.get(0).get(0)));
        we.click();
        LOG.info(String.format("Clicked on '%s'", getElementName(details.get(0).get(0))));
    }

    @Then("^the available options are displayed$")
    public void specificListOptionsDisplayed(List<String> details) {
        elementDisplayedValidation(driver, QA2Page.ousOptionList(driver));
    }

    @When("^the user selects a specific option$")
    public void selectSpecificListOption(DataTable table) {
        List<List<String>> details = table.raw();
        WebElement we = QA2Page.ousStateFlagOption(driver, getElementName(details.get(0).get(1)));
        elementDisplayedValidation(driver, we);
        we.click();
        LOG.info(String.format("Clicked on '%s'", getElementName(details.get(0).get(1))));
    }

    @When("^the specific field option is selected$")
    public void specificFieldOptionIsSelected(DataTable table) {
        List<List<String>> details = table.raw();
        sleep(1000);
        WebElement we = GeneralPage.selectedFieldOption(driver, getElementName(details.get(0)
                .get(0)));
        elementDisplayedValidation(driver, we);
        String opt = we.getAttribute("innerHTML");
        Assert.assertTrue(String.format("The selected field option is %s and it should be %s!",
                opt, details.get(0).get(1)), opt.contains(getElementName(details.get(0).get(1))));
        LOG.info(String.format("The option '%s' was selected.", getElementName(details.get(0)
                .get(1))));
    }

    @Then("^the user adds a QA0 comment$")
    public void addQA0Comment(DataTable table) {
        List<List<String>> details = table.raw();
        WebElement we = GeneralPage.doQAXFormComment(driver,
                getElementName(details.get(0).get(0)));
        elementDisplayedValidation(driver, we);
        we.clear();
        String commentBody = details.get(0).get(1) + " " + System.currentTimeMillis();
        bufferMap.put("comment", commentBody);
        we.sendKeys(commentBody);
    }

    @Then("^the user adds a QA2 comment$")
    public void addQA2Comment(DataTable table) {
        List<List<String>> details = table.raw();
        sleep(1000);
        WebElement we = GeneralPage.doQAXFormComment(driver,
                getElementName(details.get(0).get(0)));
        elementDisplayedValidation(driver, we, getElementName(details.get(0).get(0)));
        we.clear();
        String commentBody = details.get(0).get(1) + " " + System.currentTimeMillis();
        bufferMap.put("comment", commentBody);
        we.sendKeys(commentBody);
        LOG.info(String.format("Filled the '%s' field with '%s'", getElementName(details.get(0).get(0)),
                commentBody));
    }

    @Then("^the user inserts a value into a specific field$")
    public void populateTextField(DataTable table) throws Throwable {
        List<List<String>> details = table.raw();
        String fieldName = getElementName(details.get(0).get(0));
        String inputText = getElementName(details.get(0).get(1));
        WebElement we = QA2Page.advancedOUSTextField(driver, fieldName);
        if (inputText.equalsIgnoreCase("project_code")
                || details.get(0).get(1).equalsIgnoreCase("project_code")) {
            inputText = readFile("project_code");
            elementDisplayedValidation(driver, we);
            we.sendKeys(inputText);
        } else if (fieldName.equalsIgnoreCase("sb_names")
                || fieldName.equalsIgnoreCase("sb_name")
                || details.get(0).get(1).equalsIgnoreCase("sb_name")
                || details.get(0).get(1).equalsIgnoreCase("sb_names")) {
            inputText = extractMapValue(bufferMap, inputText);
            elementDisplayedValidation(driver, we);
            we.sendKeys(getElementName(inputText));
        } else if (inputText.equalsIgnoreCase("user_search")) {
            inputText = inputText + "_" + generateTimeStamp();
            userSearchName = inputText;
            elementDisplayedValidation(driver, we);
            we.sendKeys(getElementName(inputText));
        } else {
            elementDisplayedValidation(driver, we);
            we.sendKeys(getElementName(inputText));
        }
        LOG.info(String.format("Filled the '%s' field with '%s'", fieldName, inputText));
        sleep(1000);
    }

    @Then("^the user sets the 'Favourite searches' name$")
    public void populateComboField(DataTable table) {
        List<List<String>> details = table.raw();
        String fieldName = getElementName(details.get(0).get(0));
        String inputText = getElementName(details.get(0).get(1));
        WebElement we = QA2Page.advancedOUSComboField(driver, fieldName);
        String text = inputText + "_" + currentDate(System.currentTimeMillis());
        //  userSearchName = text;
        elementDisplayedValidation(driver, we, fieldName);
        we.sendKeys(getElementName(text));
        sleep(1000);
    }

    @Then("^the user inserts a value into a specific caption field$")
    public void populateCaptionTextBox(DataTable table) {
        List<List<String>> details = table.raw();
        WebElement we = GeneralPage.captionTextBox(driver, getElementName(details.get(0).get(0)));
        elementDisplayedValidation(driver, we);
        driver.executeScript("arguments[0].scrollIntoView(true);", we);
        String commentBody = details.get(0).get(1) + " " + System.currentTimeMillis();
        bufferMap.put("comment", commentBody);
        we.sendKeys(commentBody);
    }

    @Then("^the user inserts a value into a specific doublebox field$")
    public void populateDoubleBoxField(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String elementName = getElementName(detail.get(0));
            WebElement we = GeneralPage.doubleBox(driver, elementName);
            if (we == null)
                we = GeneralPage.doQAXFormEC(driver);
            elementDisplayedValidation(driver, we, elementName);
            we.click();
            we.sendKeys(detail.get(1));
            LOG.info(String.format("Filled the '%s' field with '%s'", elementName,
                    detail.get(1)));
        }
    }

    @Then("^the specific data is collected for validation$")
    public void collectDataForValidation(DataTable table) {
        sleep(1000);
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String tagLabelName = getElementName(detail.get(0));
            WebElement we = GeneralPage.getDynamicLabelByTagLabel(driver, tagLabelName);
            elementDisplayedValidation(driver, we, tagLabelName);
            String innerValue = we.getAttribute("innerHTML");
            bufferMap.put(tagLabelName, innerValue);
            buffer = innerValue;
        }
    }

    @Then("^the data was successfully retrieved$")
    public void specificDataRetrievedAfterReRunAOSCheck(DataTable table) {
        List<List<String>> details = table.raw();
        Boolean exists = true;
        for (List<String> detail : details) {
            String tagLabelName = getElementName(detail.get(0));
            String nextLabel = getElementName(detail.get(1));
            WebElement we = EBSummaryPage
                    .individualDataRetrievedAfterReRunAOSCheck(driver, tagLabelName);
            try {
                we.isDisplayed();
            } catch (Exception e) {
                exists = false;
            }
            if (exists) {
                String retrievedData = we.getAttribute("innerHTML");
                Assume.assumeTrue(String.format("The %s area doesn't contain any information after "
                                + "re-running the AOS check!", tagLabelName),
                        !retrievedData.contains(nextLabel));
            } else {
                LOG.info(String.format("The %s area doesn't contain any information after "
                        + "re-running the AOS check!", tagLabelName));
            }
        }
    }

    @Then("^the data grid was successfully retrieved$")
    public void specificDataGridRetrievedAfterReRunAOSCheck(DataTable table) {
        List<List<String>> details = table.raw();
        String tagLabelName = getElementName(details.get(0).get(0));
        String nextLabel = getElementName(details.get(0).get(1));
        Boolean exists = true;
        //  scroll until find the WebElement
        WebElement we = EBSummaryPage.dataGridRetrievedAfterReRunAOSCheck(driver, tagLabelName);
        driver.executeScript("arguments[0].scrollIntoView(true);", we);
        try {
            we.isDisplayed();
        } catch (Exception e) {
            exists = false;
        }
        if (exists) {
            String retrievedData = we.getAttribute("innerHTML");
            Assume.assumeTrue(String.format("The %s grid area doesn't contain any information after"
                            + " re-running the " + "AOS check!", tagLabelName),
                    retrievedData.contains(nextLabel));
        } else {
            LOG.info(String.format("The %s grid area doesn't contain any information after "
                    + "re-running the AOS check!", tagLabelName));
        }
    }

    @Then("^the user selects the next EB if the Re-run AOS check was already triggered$")
    public void selectNextEbIfReRunAOSCheckTriggered(List<String> details) {
        Boolean exists = true;
        int index = 1;
        do {
            try {
                WebElement we = GeneralPage.button(driver, getElementName(details.get(0)));
                if (!we.isDisplayed()) {
                    exists = false;
                }
            } catch (Exception e) {
                exists = false;
            }
            if (exists) {
                QA0Page.selectEBByIndex(driver, String.valueOf(index)).click();
                //  FIXME - sometimes the page is loaded very fast
                waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                        DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                sleep(10000);
                elementDisplayedValidation(driver, EBSummaryPage.summary(driver));
                //  FIXME - sometimes the page is loaded very fast
                waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                        DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                sleep(10000);
                index++;
            }
        }
        while (exists);
        LOG.info(String.format("The user clicked on the EB no. %d", index));
    }

    @Then("^the user selects the next EB if the Re-run AOS check was not triggered already$")
    public void checkIfReRunAOSCheckTriggered(List<String> details) {
        Boolean exists = true;
        int index = 1;
        do {
            try {
                WebElement we = GeneralPage.button(driver, getElementName(details.get(0)));
                if (!we.isDisplayed()) {
                    exists = false;
                }
            } catch (Exception e) {
                exists = false;
            }
            if (!exists) {
                QA0Page.selectEBByIndex(driver, String.valueOf(index)).click();
//                waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
//                        DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                sleep(10000);
                elementDisplayedValidation(driver, EBSummaryPage.summary(driver),
                        getElementName(details.get(0)));
//                waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
//                        DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                sleep(10000);
                index++;
            }
        }
        while (!exists);
        LOG.info(String.format("The user clicked on the EB no. %d", index));
    }

    @Then("^the EBs are displayed")
    public void searchResultsEB() {
//        elementDisplayedValidation(driver, QA0Page.qa0SearchResults(driver));
    }

    @Then("^the user checks if the EBs are displayed$")
    public void checkEBsListDisplayed() {
        if (GeneralPage.westPageArea(driver).getAttribute("innerHTML")
                .contains("Empty ExecBlock list")) {
            GeneralPage.searchButton(driver).click();
            LOG.info("Clicked on 'Search'");
            waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                    DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        }
    }

    @Then("^the specific button is displayed")
    public void specificButtonDisplayed(List<String> details) {
        for (String detail : details) {
            sleep(2000);
            elementDisplayedValidation(driver, GeneralPage.button(driver, getElementName(detail)),
                    getElementName(detail), 10);
            sleep(3000);
        }
    }

    @Then("^the specific button is not displayed")
    public void specificButtonNotDisplayed(List<String> details) {
        for (String detail : details) {
            String buttonName = getElementName(detail);
            Boolean exists = true;
            try {
                GeneralPage.button(driver, buttonName).isDisplayed();
            } catch (Exception e) {
                exists = false;
            }
            Assert.assertFalse(String.format("The button '%s' is still available within the "
                    + "page!", buttonName), exists);
        }
    }

    @Then("^the specific button is enabled")
    public void specificButtonEnabled(List<String> details) {
        Boolean failed = false;
        for (String detail : details) {
            sleep(2000);
            WebElement we = GeneralPage.button(driver, getElementName(detail));
            elementDisplayedValidation(driver, we, 10);
            Assert.assertTrue(String.format("The button '%s' is disabled!", we), we.isEnabled());
        }
    }

    @Then("^the specific button is disabled")
    public void specificButtonDisabled(List<String> details) {
        Boolean failed = false;
        for (String detail : details) {
            sleep(2000);
            WebElement we = GeneralPage.button(driver, getElementName(detail));
            elementDisplayedValidation(driver, we, 10);
            Assert.assertTrue(String.format("The button '%s' is still enabled!", we), !we.isEnabled());
        }
    }

    @Then("^the specific label is not displayed")
    public void specificLabelNotDisplayed(List<String> details) {
        for (String detail : details) {
            String labelName = getElementName(detail);
            Boolean exists = true;
            try {
                GeneralPage.label(driver, labelName).isDisplayed();
            } catch (Exception e) {
                exists = false;
            }
            Assert.assertFalse(String.format("The label '%s' is still available within the "
                    + "page!", labelName), exists);
        }
    }


    @When("^the user clicks on a specific tab")
    public void clickOnSpecificTab(List<String> details) {
        String tabName = getElementName(details.get(0));
        sleep(1000);
        if (tabName.contains("Pipeline") || tabName.contains("Manual")) {
            String action = "";
            for (Pipelines pl : Pipelines.values()) {
                if (pl.toString().equalsIgnoreCase(tabName)) {
                    action = pl.getValue();
                    break;
                }
            }
            elementDisplayedValidation(driver, GeneralPage.headerMenuTab(driver, action), action,
                    2);
            GeneralPage.headerMenuTab(driver, action).click();
            //        TODO - sometimes the "Metadata Check" takes too long to stop spinning
            //        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
            //                DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        } else {
            elementDisplayedValidation(driver, GeneralPage.headerMenuTab(driver, tabName), tabName);
            GeneralPage.headerMenuTab(driver, tabName).click();
            //        TODO - sometimes the "Metadata Check" takes too long to stop spinning
            if (!driver.getCurrentUrl().contains("eso.org")
                    && !Arrays.asList("QA0", "QA2").contains(tabName)) {
//                waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
//                        DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                waitForLoadingProgressNotDisplayed(driver);
            } else {
                sleep(1000);
            }
        }
        LOG.info(String.format("Clicked on '%s'", details.get(0)));
        sleep(1000);
    }

    @Then("^the QA0 Flags view is displayed")
    public void qa0ViewIsDisplayed(List<String> details) {
        String eb = extractMapValue(bufferMap, getElementName(details.get(0)));
        elementDisplayedValidation(driver, QA0FlagsTabPage.textArea(driver, eb));
    }

    @Then("^the legend item is displayed")
    public void baselineCoverageLegendItemDisplayed(DataTable table) {
        List<List<String>> data = table.raw();
        Boolean exists = true;
        for (List<String> input : data) {
            String tagLabelName = getElementName(input.get(1));
            try {
                BaselineCoveragePage.legendLabel(driver, tagLabelName);
            } catch (Exception e) {
                exists = false;
            }
            if (exists) {
                String legendLabel = BaselineCoveragePage.legendLabel(driver, tagLabelName)
                        .getAttribute("innerHTML");
                String legendSwatch = BaselineCoveragePage.legendSwatch(driver, tagLabelName)
                        .getCssValue("background-color");
                legendSwatch = rgbToHex(legendLabel, legendSwatch);
                Assume.assumeTrue(String.format("The %s legend item is not available after "
                                + "re-running the AOS check!", tagLabelName),
                        legendLabel.contains(tagLabelName));
                Assert.assertTrue(String.format("The swatch color of the %s legend item is not %s!",
                        tagLabelName, input.get(0)),
                        legendSwatch.equalsIgnoreCase(input.get(0)));
            } else {
                LOG.info(String.format("The %s legend item is not available after re-running "
                        + "the AOS check!", tagLabelName));
            }
        }
    }

    @Then("^the QA2 Report html page is displayed$")
    public void htmlQA2PageDisplayed() {
        BrowserUtil.switchWindowByTitle(driver, PageTitle.QA2_REPORT.getValue());
        elementDisplayedValidation(driver, QAReportPage.pageAnchor(driver),
                DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @Then("^the QA0 Report html page is displayed$")
    public void htmlQA0PageDisplayed() {
        BrowserUtil.switchWindowByTitle(driver, PageTitle.QA2_REPORT.getValue());
        elementDisplayedValidation(driver, QAReportPage.pageAnchor(driver), "QA0 Report",
                DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @Then("^the release Notes page is displayed$")
    public void releaseNotesPageDisplayed() {
        BrowserUtil.switchWindowByTitle(driver, PageTitle.RELEASE_NOTES.getValue());
        elementDisplayedValidation(driver, ReleaseNotesPage.almaReleaseNotes(driver), "Release notes",
                DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @Then("^the new browser tab is available$")
    public void newBrowserTabAvailable(List<String> details) {
        try {
            if (details.size() > 1) {
                BrowserUtil.switchWindowByTitle(driver, details.get(1));
            }
            LoginPage.userField(driver).isDisplayed();
            inputCredentials(auth);
            clickLogin();
        } catch (Throwable throwable) {
            LOG.info("New browser tab not available or the user is already authenticated!");
        }
        BrowserUtil.switchWindowByTitle(driver, getElementName(details.get(0)));
        //        elementDisplayedValidation(driver, GeneralPage.label(driver, details.get(0)),
        //                60);
    }

    @Then("^the QA2 Report html page contains the specific data$")
    public void htmlQA2PageContainsSpecificData(List<String> details) {
        String dataHint = getElementName(details.get(0));
        String data = null;
        sleep(1000);
        if (bufferMap.size() > 0) {
            data = extractMapValue(bufferMap, dataHint).replace(".0 expected",
                    " expected");
        }
        if (StringUtils.isBlank(data)) {
            data = dataHint;
        }
        String htmlQA2Report = QAReportPage.pageAnchor(driver).getAttribute("innerHTML");
        Assert.assertTrue(String.format("The HTML QA2 Report doesn't contain '%s'!", data),
                htmlQA2Report.contains(data));
    }

    @Then("^the QA0 Report html page contains the specific data$")
    public void htmlQA0PageContainsSpecificData(List<String> details) {
        for (String detail : details) {
            String dataHint = getElementName(detail);
            String data;
            if (detail.equalsIgnoreCase("exec_block")) {
                data = extractMapValue(bufferMap, dataHint);
            } else {
                data = dataHint;
            }
            String htmlQA0Report = QAReportPage.pageAnchor(driver).getAttribute("innerHTML");
            Assert.assertTrue(String.format("The QA0 HTML Report doesn't contain '%s'!",
                    data), htmlQA0Report.contains(data));
        }
    }

    @When("^the user switches back to the main page$")
    public void switchBackToMainPage(List<String> details) {
        BrowserUtil.switchWindowByTitle(driver, getElementName(details.get(0)));
        BrowserUtil.switchWindowByTitle(driver, getElementName(details.get(0)));
    }

    @When("^the user switches back to the parent window$")
    public void switchBackToMainPage() {
        BrowserUtil.switchToParentWindow(driver);
    }

    @Then("^the specific data is written into the tmp file$")
    public void writeDataToFile(List<String> details) {
        WebElement we = OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver,
                getElementName(details.get(0)));
        elementDisplayedValidation(driver, we);
        String innerValue = we.getAttribute("innerHTML");
        writeFile(innerValue, "project_code");
    }

    @When("^the user clicks on a specific tree cell$")
    public void clickOnTreeCell(List<String> details) {
        String cn = extractMapValue(bufferMap, getElementName(details.get(0)));
        WebElement we = GeneralPage.treeCell(driver, cn);
        driver.executeScript("arguments[0].scrollIntoView(true);", we);
        sleep(2000);
        elementDisplayedValidation(driver, we);
        we.click();
        LOG.info(String.format("Clicked on '%s'", details.get(0)));
        sleep(3000);
//        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
//                DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @Then("^the specific data is displayed within the page$")
    public void pageContainsData(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String expectedValue = getElementName(detail.get(0));
            String value = extractMapValue(bufferMap, expectedValue);
            if (StringUtils.isBlank(value)) {
                value = getElementName(detail.get(1));
            }
            WebElement we = null;
            try {
                we = GeneralPage
                        .getDynamicLabelContentByTagLabelContainingText(driver,
                                getElementName(detail.get(1)));
            } catch (Exception e) {
                Assert.fail(String.format("'%s' couldn't be found within the page!", value));
            }
            elementDisplayedValidation(driver, we, value);
            Assert.assertTrue(String.format("The expected value for %s is %s and it should be %s!",
                    expectedValue, we.getAttribute("innerHTML"), value),
                    value.contains(we.getAttribute("innerHTML")));
        }
    }

    @Then("^the Protrack EF sum equals the Execution Count value$")
    public void sumEFValidation(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            Double efSum = ProtrackPage.sumEF(driver, detail.get(0));
            Double ec = Double.parseDouble(GeneralPage
                    .getDynamicLabelContentByTagLabelContainingText(driver, detail.get(1))
                    .getAttribute("innerHTML"));
            Assert.assertTrue(String.format("The EF sum is '%.0f' and it should be '%.0f'!",
                    efSum, ec), Objects.equals(efSum, ec));
        }
    }

    @When("^the user clicks on a specific combobox$")
    public void clickOnComboBox(List<String> details) {
        WebElement we = GeneralPage.comboBox(driver, getElementName(details.get(0)));
        elementDisplayedValidation(driver, we);
        we.click();
        LOG.info(String.format("Clicked on '%s'", getElementName(details.get(0))));
        sleep(500);
    }

    @When("^the user selects a specific combobox item$")
    public void selectComboBoxItem(List<String> details) {
        String elementName = "";
        if (StringUtils.isNotBlank(details.get(0))) {
            elementName = getElementName(details.get(0));
        }
        if (details.get(0).equalsIgnoreCase("user_search")) {
            //  elementName = userSearchName;
            elementName = getElementName(details.get(0)) + "_" + currentDate(System.currentTimeMillis());
        }
        WebElement we = GeneralPage.selectComboBoxItem(driver, elementName);
        elementDisplayedValidation(driver, we, elementName);
        we.click();
        LOG.info(String.format("Clicked on '%s'", elementName));
    }

    @Then("^the EF values are reduced according to the formula$")
    public void calculateEFBasedOnFormula(List<String> formula) throws Throwable {
        List<String> variables = split(formula.get(1));
        String newFormula = "";
        for (int i = 0; i < variables.size(); i++) {
            if (i == 0) {
                buffer = formula.get(0).replace(variables.get(i), extractMapValue(bufferMap,
                        variables.get(i)));
            } else {
                newFormula = buffer.replace(variables.get(i), extractMapValue(bufferMap,
                        variables.get(i)));
                buffer = newFormula;
            }
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object ef = engine.eval(newFormula);
        Double calculatedEF = Double.parseDouble(ef.toString());

//        OR
//        Expression ef = new ExpressionBuilder(formula.get(0))
//                .variables("EF_old", "EC_old", "EC_new", "N_EB")
//                .build()
//                .setVariable("EF_old", Double.parseDouble(extractMapValue(bufferMap, "EF_old")))
//                .setVariable("EC_old", Double.parseDouble(extractMapValue(bufferMap, "EC_old")))
//                .setVariable("EC_new", Double.parseDouble(extractMapValue(bufferMap, "EC_new")))
//                .setVariable("N_EB", Double.parseDouble(extractMapValue(bufferMap, "N_EB")));
//        Double calculatedEF = ef.evaluate();

        Double displayedEF = Double.parseDouble(GeneralPage.collectGridColumnElements(driver,
                "EF").get(0).getAttribute("innerHTML"));
        Assert.assertEquals(String.format("The new EF calculated based on formula %s is %.0f and "
                        + "it should be %.0f!", formula.get(0), calculatedEF, displayedEF),
                calculatedEF, displayedEF);
    }

    @When("^the user clicks on the refresh State button$")
    public void refreshState() {
        sleep(5000);
        WebElement we = GeneralPage.reloadButton(driver);
        elementDisplayedValidation(driver, we, "Refresh");
        we.click();
        LOG.info("Clicked on 'Refresh' state button");
        try {
            waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                    DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        } catch (Exception e) {
        }
    }

    @Then("^the displayed items are filtered by$")
    public void filteredItems(DataTable table) {
        Boolean failed = false;
        List<List<String>> data = table.raw();
        List<WebElement> items = GeneralPage.collectListColumnElements(driver, data.get(0).get(0),
                data.get(0).get(2));
        List<WebElement> projects = GeneralPage.collectListColumnElements(driver,
                "Project", data.get(0).get(2));
        for (int i = 0; i < items.size(); i++) {
            Assert.assertTrue(String.format("The filter does not match for project %s.",
                    projects.get(i).getAttribute("innerHTML")),
                    Objects.equals(items.get(i).getAttribute("innerHTML"), data.get(0).get(1)));

//            TODO - to be uncommented if the test should not fail on the first un-match
//            if (!Objects.equals(items.get(i).getAttribute("innerHTML"), data.get(0).get(1))) {
//                LOG.error(String.format("The filter does not match for project %s.",
//                        projects.get(i).getAttribute("innerHTML")));
//                failed = true;
//            }
        }
//        if (failed) {
//            Assert.fail("The filtering criteria were not met. Please check the logs for more "
//                    + "details.");
//        }
    }

    @Then("^collect all values from a specific column$")
    public void collectColumnValues(DataTable table) {
        List<List<String>> data = table.raw();
        String columnName = getElementName(data.get(0).get(0));
        String searchTab = getElementName(data.get(0).get(1));
        List<WebElement> items = GeneralPage.collectListColumnElements(driver, columnName,
                searchTab);
        for (WebElement item : items) {
            runtimeList.add(item.getAttribute("innerHTML"));
        }
    }

    @Then("^the list of filtered items contain$")
    public void filteredItemsContain(DataTable table) {
        List<List<String>> data = table.raw();
        List<String> itemNames = new ArrayList<>();
        String columnName = getElementName(data.get(0).get(0));
        String columnValue = getElementName(data.get(0).get(1));
        String searchTab = getElementName(data.get(0).get(2));
        List<WebElement> items = GeneralPage.collectListColumnElements(driver, columnName,
                searchTab);
        for (WebElement item : items) {
            itemNames.add(item.getAttribute("innerHTML"));
        }
        Assert.assertTrue(String.format("The list of filtered items does not contain '%s' '%s'.",
                columnName, columnValue), itemNames.contains(columnValue));
    }

    @Then("^the specific data was changed$")
    public void elementUpdated(DataTable table) {
        List<List<String>> data = table.raw();
        String tagLabelName = getElementName(data.get(0).get(0));
        String newState = getElementName(data.get(0).get(1));
        sleep(5000);
        WebElement we = GeneralPage
                .getDynamicLabelContentByTagLabelExactText(driver, tagLabelName);
        elementDisplayedValidation(driver, we);
        String currentValue = we.getAttribute("innerHTML");
        Assert.assertTrue(String.format("The %s should be %s but is %s!", tagLabelName, newState,
                currentValue), Objects.equals(newState, currentValue));
    }

    @Then("^the specific element matches specific pattern$")
    public void specificDataMatchesPattern(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String elementName = getElementName(detail.get(1));
            WebElement we = GeneralPage.label(driver, elementName);
            elementDisplayedValidation(driver, we, elementName);
            driver.executeScript("arguments[0].scrollIntoView(true);", we);
            String range = GeneralPage.expectedValueFromContainer(driver,
                    detail.get(0), elementName).replace(" [arcsec]", "")
                    .replace(" ", "");

            checkIfStringMatchesPattern(range, detail.get(2));
        }
    }

    @Then("^a specific element is displayed on specific page area$")
    public void elementDisplayedOnSpecificPageArea(DataTable table) {
        List<List<String>> details = table.raw();
        elementDisplayedValidation(driver, GeneralPage.elementFromSpecificPageArea(driver,
                getElementName(details.get(0).get(1)), getElementName(details.get(0).get(0))));
    }

    @Then("^a specific element is displayed$")
    public void elementDisplayed(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            WebElement we = GeneralPage.elementContainingSpecificText(driver,
                    getElementName(detail.get(0)), getElementName(detail.get(1)));
            elementDisplayedValidation(driver, we, getElementName(detail.get(1)));
            driver.executeScript("arguments[0].scrollIntoView(true);", we);
        }
    }

    @Then("the label is positioned as required$")
    public void elementPosition(DataTable table) {
        List<List<String>> details = table.raw();
        GeneralPage.expandView(driver).click();
        String currentPosition = GeneralPage.twoLabelsPosition(driver,
                getElementName(details.get(0).get(0)), getElementName(details.get(0).get(1)),
                getElementName(details.get(0).get(3)));
        Assert.assertTrue(String.format("The label '%s' should be positioned '%s' the label '%s' "
                        + " but it's '%s'!", getElementName(details.get(0).get(1)),
                details.get(0).get(2), getElementName(details.get(0).get(3)),
                currentPosition), Objects.equals(details.get(0).get(2), currentPosition));
    }

    @Then("^the specific email notification is available$")
    public void emailNotificationSent(DataTable table) {
        List<List<String>> details = table.raw();
        String prefix = details.get(0).get(0);
        String pc = extractMapValue(bufferMap, details.get(1).get(0));
        String sb = extractMapValue(bufferMap, details.get(2).get(0));
        String newState = "";
        if (details.size() > 3) {
            newState = details.get(3).get(0);
        }
        String emailSubject = "";
        try {
            MailHogPage.email(driver, prefix + " " + pc).isDisplayed();
            emailSubject = MailHogPage.email(driver, prefix + " " + pc)
                    .getAttribute("innerHTML");
        } catch (Exception e) {
            try {
                MailHogPage.email(driver, prefix + " " + sb).isDisplayed();
                emailSubject = MailHogPage.email(driver, prefix + " " + sb)
                        .getAttribute("innerHTML");
            } catch (Exception e1) {
                Assert.fail("The email notification was not sent!");
            }
        }
        if (details.size() < 4) {
            Assert.assertTrue("The email notification was not sent or the subject is "
                    + "inconsistent!", emailSubject.contains(pc) && emailSubject.contains(sb));
        } else {
            Assert.assertTrue("The email notification was not sent or the subject is "
                    + "inconsistent!", emailSubject.contains(pc) && emailSubject.contains(sb)
                    && emailSubject.contains(newState));
        }
    }

    @Then("^the email notification subject contains specific data$")
    public void emailSubjectContains(DataTable table) {
        List<List<String>> details = table.raw();
        String prefix = details.get(0).get(0);
        String pc = extractMapValue(bufferMap, details.get(1).get(0));
        String sb = extractMapValue(bufferMap, details.get(2).get(0));
        String substring;
        String emailSubject = "";
        try {
            MailHogPage.email(driver, prefix + " " + pc).isDisplayed();
            emailSubject = MailHogPage.email(driver, prefix + " " + pc)
                    .getAttribute("innerHTML");
        } catch (Exception e) {
            try {
                MailHogPage.email(driver, prefix + " " + sb).isDisplayed();
                emailSubject = MailHogPage.email(driver, prefix + " " + sb)
                        .getAttribute("innerHTML");
            } catch (Exception e1) {
                Assert.fail("The email notification was not sent!");
            }
        }
        if (details.size() < 4) {
            Assert.assertTrue(String.format("The email notification was not sent or the subject "
                    + "'%s' doesn't contain '%s'!", emailSubject, pc), emailSubject.contains(pc));
            Assert.assertTrue(String.format("The email notification was not sent or the subject "
                    + "'%s' doesn't contain '%s'!", emailSubject, sb), emailSubject.contains(sb));
        } else {
            if (details.size() > 3) {
                for (int i = 3; i < details.size(); i++) {
                    substring = details.get(i).get(0);
                    if (substring.equalsIgnoreCase("status")
                            || substring.equalsIgnoreCase("reason")) {
                        substring = extractMapValue(bufferMap, details.get(i).get(0));
                    }
                    Assert.assertTrue(String.format("The email notification was not sent or the subject "
                            + "'%s' doesn't contain '%s'!", emailSubject, pc), emailSubject.contains(pc));
                    Assert.assertTrue(String.format("The email notification was not sent or the subject "
                            + "'%s' doesn't contain '%s'!", emailSubject, sb), emailSubject.contains(sb));
                    Assert.assertTrue(String.format("The email notification was not sent or the subject "
                                    + "'%s' doesn't contain '%s'!", emailSubject, substring),
                            emailSubject.contains(substring));
                }
            }
        }
    }

    @Then("^the specific cancellation email notification was sent$")
    public void cancellationEmailNotificationSent(DataTable table) {
        List<List<String>> details = table.raw();
        String dr = extractMapValue(bufferMap, details.get(0).get(0));
        String suffix = details.get(3).get(0);
        if (dr.contains("(")) {
            dr = extractSubstringUntilIndexOf(dr, "(");
        }
        String et = "[AQUA] " + dr + suffix;
        if (MailHogPage.deliveryTimeExists(driver, details.get(1).get(0), details.get(2).get(0))) {
            try {
                MailHogPage.email(driver, et).isDisplayed();
            } catch (Exception e) {
                Assert.fail("The cancellation email notification was not sent!");
            }
        } else {
            Assert.fail("The cancellation email notification was not sent!");
        }
    }

    @Then("^the specific email notification was not sent$")
    public void emailNotificationNotSent(DataTable table) {
        List<List<String>> details = table.raw();
        Boolean sent = true;
        String pc = extractMapValue(bufferMap, details.get(0).get(0));
        String sb = extractMapValue(bufferMap, details.get(1).get(0));
        try {
            MailHogPage.email(driver, pc).isDisplayed();
            MailHogPage.email(driver, sb).isDisplayed();
        } catch (Exception e) {
            sent = false;
        }
        if (sent) {
            Assert.fail("An email notification was sent!");
        }
    }

    @Then("^the specific cancellation email notification was not sent$")
    public void cancellationEmailNotificationNotSent(DataTable table) {
        List<List<String>> details = table.raw();
        Boolean sent = true;
        String dr = extractMapValue(bufferMap, details.get(0).get(0));
        String suffix = details.get(3).get(0);
        if (dr.contains("(")) {
            dr = extractSubstringUntilIndexOf(dr, "(");
        }
        String et = "[AQUA] " + dr + suffix;
        if (MailHogPage.deliveryTimeExists(driver, details.get(1).get(0), details.get(2).get(0))) {
            try {
                MailHogPage.email(driver, et).isDisplayed();
                Assert.fail("The cancellation email notification was sent!");
            } catch (Exception e) {
                LOG.info("E-mail client page not displayed");
            }
        }
    }

    @When("^the user opens a specific email message$")
    public void openEmailMessage(DataTable table) {
        List<List<String>> details = table.raw();
        String pc = extractMapValue(bufferMap, details.get(0).get(0));
        String sb = extractMapValue(bufferMap, details.get(1).get(0));
        MailHogPage.email(driver, pc).click();
        LOG.info(String.format("Clicked on '%s'", pc));
    }

    @Then("^the previously set comment is displayed within the comment textbox$")
    public void commentBodyAvailable(DataTable table) {
        List<List<String>> details = table.raw();
        String comment = extractMapValue(bufferMap, details.get(0).get(1));
        String currentComment = OBSUnitSetSummaryPage
                .doQA2FormComment(driver, details.get(0).get(0)).getText();
        Assert.assertTrue("The comment within the text area is different by the one set "
                + "initially!", Objects.equals(comment, currentComment));
    }

    @Then("^the email body contains the specific comment text$")
    public void emailBodyContainsComment(List<String> details) {
        String comment = extractMapValue(bufferMap, details.get(0));
        String emailBody = MailHogPage.emailBody(driver).getText();
        Assert.assertTrue("The email notification doesn't contain or the comment is different "
                + "by the one set initially!", emailBody.contains(comment));
    }

    @Then("^the email body contains the specific text$")
    public void emailBodyContainsText(List<String> details) {
        String emailBody = MailHogPage.emailBody(driver).getText();
        for (String text : details) {
            Assert.assertTrue(String.format("The email body doesn't contain '%s'!", text),
                    emailBody.contains(text));
        }
    }

    @Then("^the email body contains the specific dynamic text$")
    public void emailBodyContainsDynamicText(List<String> details) {
        String emailBody = MailHogPage.emailBody(driver).getText();
        for (String text : details) {
            String partOfEmailBody = extractMapValue(bufferMap, text);
            if (partOfEmailBody.contains("(")) {
                partOfEmailBody = extractSubstringUntilIndexOf(partOfEmailBody, "(");
            }
            Assert.assertTrue(String.format("The email body doesn't contain the '%s' '%s'!",
                    text, partOfEmailBody), emailBody.contains(partOfEmailBody));
        }
    }

    @Then("^collect the status of the Awaiting decision by DRM label$")
    public void qa2DecisionLabelStatus() {
        try {
            GeneralPage.specificLabel(driver,
                    getElementName("awaiting_decision_label")).isDisplayed();
            awaitingDecision = true;
        } catch (Exception e) {
            awaitingDecision = false;
        }
    }

    @Then("^the user edits the comment$")
    public void editComment(List<String> details) {
        String comment = extractMapValue(bufferMap, details.get(0));
        WebElement we = OBSUnitSetSummaryPage.editComment(driver, comment);
        elementDisplayedValidation(driver, we, comment);
        we.click();
        LOG.info(String.format("Clicked on 'Edit comment' for comment '%s'", comment));
        //  GeneralPage.globalTextBox(driver).clear();
        sleep(2000);
        String commentBody = details.get(0) + " " + System.currentTimeMillis();
        GeneralPage.globalTextBox(driver).sendKeys(commentBody);
        bufferMap.put("updated comment", commentBody);
    }

    @When("^the user hovers a specific element$")
    public void mouseOverElement(List<String> details) {
        String elementName = getElementName(details.get(0));
//        WebElement we = OBSUnitSetSummaryPage.weblogDownload(driver, elementName);
//        elementDisplayedValidation(driver, we);
//        driver.executeScript("arguments[0].scrollIntoView(true);", we);
//        String jsHover = "var element = arguments[0];"
//                + "var mouseEventObj = document.createEvent('MouseEvents');"
//                + "mouseEventObj.initEvent( 'mouseover', true, true );"
//                + "element.dispatchEvent(mouseEventObj);";
//        String jsMouserOver = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');"
//                + "evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} "
//                + "else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
//        String jsMouseEvent = "var evObj = document.createEvent('MouseEvents');"
//                + "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
//                + "arguments[0].dispatchEvent(evObj);";
//        driver.executeScript(jsHover, we);
//        driver.executeScript(jsMouserOver, we);
//        driver.executeScript(jsMouseEvent, we);
    }

    @Then("^the tooltip is displayed$")
    public void tooltipDisplayed() {
        WebElement we = null;
        //  elementDisplayedValidation(driver, we);
    }

    @Then("^the user clicks on one available EB UIDs$")
    public void clickOnEBUID() {
        WebElement we = PLCalibrationPage.ebUID(driver);
        elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on an available EB UIDs");
        sleep(5000);
        BrowserUtil.switchWindow(driver);
    }

    @Then("^the specific label and data are displayed$")
    public void labelAndDataDisplayed(List<String> details) {
        for (String detail : details) {
            String tagLabelName = getElementName(detail);
            String currentData = "";
            try {
                currentData = GeneralPage.getDynamicLabelByTagLabel(driver, tagLabelName)
                        .getAttribute("innerHTML");
                Assert.assertTrue(String.format("The label '%s' is not displayed!", tagLabelName),
                        StringUtils.isNotBlank(currentData));
            } catch (Exception e) {
                Assert.fail(String.format("The label '%s' is not displayed!", tagLabelName));
            }
            LOG.info(String.format("[FOUND] '%s': '%s'", tagLabelName, currentData));
        }
    }

    @Then("^the specific label and data are displayed and contain the required details$")
    public void labelAndDataDisplayedAndContain(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String tagLabelName = getElementName(detail.get(0));
            String currentData = GeneralPage.getDynamicLabelByTagLabel(driver, tagLabelName)
                    .getAttribute("innerHTML");
            Assert.assertTrue(String.format("The label '%s' is not displayed!", tagLabelName),
                    currentData.contains(getElementName(detail.get(1))));
            LOG.info(String.format("[FOUND] '%s' is followed by: '%s'", tagLabelName,
                    getElementName(detail.get(1))));
        }
    }

    @Then("^the specific grid header contains data$")
    public void gridHeaderContainsData(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String gridName = getElementName(detail.get(0));
            WebElement we = GeneralPage.getTableHeader(driver, gridName);
            driver.executeScript("arguments[0].scrollIntoView(true);", we);
            String gridHeaderContent = we.getAttribute("innerHTML");
            Assert.assertTrue(String.format("The '%s' grid header doesn't contain '%s'!",
                    gridName, detail.get(1)), gridHeaderContent.contains(detail.get(1)));
            LOG.info(String.format("[FOUND] The '%s' grid header contains: '%s'", gridName,
                    detail.get(1)));
        }
    }

    @Then("^the specific toolbar button is displayed$")
    public void toolbarButtonDisplayed(List<String> details) {
        WebElement we = GeneralPage.toolBarButton(driver, getElementName(details.get(0)));
        elementDisplayedValidation(driver, we, getElementName(details.get(0)));
    }

    @Then("^the specific list item is available$")
    public void listItemAvailable(List<String> details) {
        String elementName;
        for (String item : details) {
            if (item.equalsIgnoreCase("user_search")) {
                elementName = getElementName(item) + "_" + currentDate(System.currentTimeMillis());
            } else {
                elementName = getElementName(item);
            }
            Assert.assertTrue(String.format("The specific list item '%s' is not available!",
                    elementName), GeneralPage.comboBoxItemAvailability(driver, elementName));
        }
    }

    @Then("^the specific list item is not available$")
    public void listItemNotAvailable(List<String> details) {
        String elementName;
        if (details.get(0).equalsIgnoreCase("user_search")) {
            elementName = getElementName(details.get(0)) + "_" + currentDate(System.currentTimeMillis());
        } else {
            elementName = getElementName(details.get(0));
        }
        Assert.assertFalse(String.format("The specific list item '%s' is still available!",
                elementName), GeneralPage.comboBoxItemAvailability(driver, elementName));
    }

    @Then("^the antenna positions plot is available$")
    public void antennaPositionsPlotAvailable() throws Throwable {
        elementDisplayedValidation(driver, BaselineCoveragePage.antennaPositionsPlot(driver),
                "'Antenna Positions'");
    }

    @Then("^the pointing offsets plot is available$")
    public void pointingOffsetsPlotAvailable() throws Throwable {
        elementDisplayedValidation(driver, GeneralPage.canvasPlot(driver),
                "'Pointing Offsets'");
    }

    @Then("^the mosaic coverage plot is available$")
    public void mosaicCoveragePlotAvailable() throws Throwable {
        elementDisplayedValidation(driver, GeneralPage.canvasPlot(driver),
                "'Mosaic coverage'");
    }

    @Then("^the phase RMS coverage plot is available$")
    public void phaseRMSCoveragePlotAvailable() throws Throwable {
        elementDisplayedValidation(driver, GeneralPage.canvasPlot(driver),
                "'RMS coverage'");
    }

    @Then("^the Source Coverage plot is available$")
    public void sourceCoveragePlotAvailable() throws Throwable {
        elementDisplayedValidation(driver, GeneralPage.canvasPlot(driver),
                "'RMS coverage'");
    }

    @Then("^the phase Tsys of the representative frequency plot is available$")
    public void tSysRepFreqPlotAvailable(List<String> details) {
        elementDisplayedValidation(driver, GeneralPage.plotTitle(driver, getElementName(details
                .get(0))), getElementName(details.get(0)));
    }

    @Then("^scroll to specific label$")
    public void scrollToSpecificLabel(List<String> details) {
        String labelName = getElementName(details.get(0));
        WebElement we = GeneralPage.label(driver, labelName);
        driver.executeScript("arguments[0].scrollIntoView(true);", we);
    }

    @Then("^scroll to specific element$")
    public void scrollToSpecificElement(DataTable table) {
        List<List<String>> details = table.raw();
        String elemType = getElementName(details.get(0).get(0));
        String elemName = getElementName(details.get(0).get(1));
        WebElement we = null;
        sleep(1000);
        switch (elemType) {
            case "label": {
                we = GeneralPage.label(driver, elemName);
                break;
            }
            case "button": {
                we = GeneralPage.button(driver, elemName);
                break;
            }
            case "toolBarButton": {
                we = GeneralPage.toolBarButton(driver, elemName);
                break;
            }
            case "column": {
                we = GeneralPage.column(driver, elemName);
                break;
            }
            default: {
                break;
            }
        }
        driver.executeScript("arguments[0].scrollIntoView(true);", we);
    }

    @Then("^select a specific suggestion$")
    public void selectSpecificSuggestion(List<String> details) {
        String suggestion = getElementName(details.get(0));
        WebElement we = GeneralPage.listCellOption(driver, suggestion);
        we.click();
    }

    @Then("^the user clicks on a specific image$")
    public void clickOnImage(List<String> details) {
//        if (driver.getCurrentUrl().contains("eso.org")) {
        sleep(5000);
//        }
        String imageName = getElementName(details.get(0));
        WebElement we = GeneralPage.image(driver, imageName);
        elementDisplayedValidation(driver, we, imageName, 3);
        we.click();
        sleep(2000);
    }

    @Then("^the particular row contains specific text$")
    public void layoutRowContainsText(List<String> details) {
        String tagLabelName = getElementName(details.get(0));
        WebElement we = GeneralPage.layoutRow(driver, tagLabelName);
        String rowContent = we.getAttribute("innerHTML");
        elementDisplayedValidation(driver, we, tagLabelName);
        for (int i = 1; i < details.size(); i++) {
            String item = getElementName(details.get(i));
            Assert.assertTrue(String.format("The '%s' row doesn't contain '%s'!",
                    tagLabelName, item), rowContent.contains(item));
        }
    }

    @Then("^interrupt the test without failure$")
    public void testInterruption(Scenario scenario) {
        Selenium3DockerMultiBrowserAbstractSetup.interruption = scenario.isFailed();
    }

    @Then("^check if the EB Summary view contains specific data$")
    public void checkIfEBSummaryContainsData(DataTable table) {
        List<List<String>> details = table.raw();
        List<WebElement> ebs = QA0Page.allAvailableEBs(driver);
        boolean displayed;
        boolean found = false;
        if (ebs.size() > 1) {
            for (int i = 0; i < ebs.size(); i++) {
                for (List<String> detail : details) {
                    String elementType = detail.get(0);
                    String elementName = getElementName(detail.get(1));
                    displayed = QA0Page.elementFromEBSummaryView(driver, elementType, elementName);
                    switch (detail.get(2).toLowerCase()) {
                        case "displayed": {
                            if (!displayed) {
                                QA0Page.selectEBByIndex(driver, String.valueOf(i)).click();
                                if (driver.getCurrentUrl().contains("eso.org")) {
                                    sleep(3000);
                                } else {
                                    waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                                            DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                                }
                                try {
                                    GeneralPage.dialogBoxActionButton(driver, "OK").click();
                                } catch (Exception e) {
                                }
                            } else {
                                itemIndex = i;
                                found = true;
                                break;
                            }
//                            if (!displayed) {
//                                itemIndex = i;
//                                found = true;
//                                break;
//                            }
                        }
                        case "not-displayed": {
                            if (displayed) {
                                QA0Page.selectEBByIndex(driver, String.valueOf(i)).click();
                                if (driver.getCurrentUrl().contains("eso.org")) {
                                    sleep(3000);
                                } else {
                                    waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                                            DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                                }
                                try {
                                    GeneralPage.dialogBoxActionButton(driver, "OK").click();
                                } catch (Exception e) {
                                }
                            } else {
                                itemIndex = i;
                                found = true;
                                break;
                            }
                        }
                    }
                }
                if (found)
                    break;
            }
        } else {
            Assert.fail("Only one EB available based on the provided filter and it doesn't "
                    + "meet the requirements to pass the test!");
        }
    }

    @Then("^find the right OUS ugly - workaround$")
    public void uglyWorkaround() {
        elementDisplayedValidation(driver, GeneralPage.dialogBox(driver));
        WebElement we = OBSUnitSetSummaryPage.confirmationBoxMessage(driver);
        if (we.getAttribute("innerHTML")
                .contains("Please enter the required QA2 parameters "
                        + "(achieved sensitivity and beam)")) {
            clickPopupButton(Collections.singletonList("ok"));
            clickOnButton(Collections.singletonList("cancel"));
            List<WebElement> ous = QA2Page.allAvailableOUSes(driver);
            if (ous.size() > 1) {
                for (int i = 1; i < ous.size(); i++) {
                    if (i == 1) {
                        recursive(ous, i);
                    } else {
                        we = OBSUnitSetSummaryPage.confirmationBoxMessage(driver);
                        if (we.getAttribute("innerHTML")
                                .contains("Please enter the required QA2 parameters "
                                        + "(achieved sensitivity and beam)")) {
                            recursive(ous, i);
                        } else {
                            break;
                        }
                    }
                }
            } else {
                Assert.fail("Only one OUS available based on the provided filter and it doesn't "
                        + "meet the requirements to pass the test!");
            }
        }
    }

    @When("^change MOUS state in ProTrack$")
    public void changeStateInProTrack(List<String> details) {
        String sbName = runtimeList.get(itemIndex);
        WebElement we = ProtrackPage.parentMOUS(driver, sbName);
        String newState = getElementName(details.get(0));
        String js = "var evt = document.createEvent('MouseEvents');"
                + "var RIGHT_CLICK_BUTTON_CODE = 2;"
                + "evt.initMouseEvent('contextmenu', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, RIGHT_CLICK_BUTTON_CODE, null);"
                + "arguments[0].dispatchEvent(evt)";
        driver.executeScript(js, we);
        String temp = ProtrackPage.selectMOUSState(driver, newState).getAttribute("innerHTML");
        ProtrackPage.selectMOUSState(driver, newState).click();
    }

    @Then("^the markers colour is set differently$")
    public void differentPlotMarkerColors(List<String> details) {
        List<String> colorList = AtmospherePage.legendColorList(driver, details.get(0));
        Assert.assertFalse("The colors are not unique!",
                AtmospherePage.hasDuplicate(colorList));
    }

    @Then("^the new pop-up is displayed and the options are available$")
    public void popupBoxesAvailable(List<String> details) {
        for (String detail : details) {
            String elementName = getElementName(detail);
            WebElement we = FlagBuilderPage.groupBox3D(driver, elementName);
            elementDisplayedValidation(driver, we, elementName);
            we.click();
            we.click();
        }
    }

    @Then("^expand the specific dropdown list$")
    public void dropDownAvailableAndInteractable(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String elementName = getElementName(detail.get(0));
            int comboIndex = Integer.parseInt(detail.get(1));
            WebElement we = FlagBuilderPage.comboBoxByLabelAndIndex(driver, elementName, comboIndex);
            elementDisplayedValidation(driver, we, elementName);
            we.click();
            we.click();
        }
    }

    @Then("^expand the specific dropdown list and select item$")
    public void expandDropDownListAndPickItem(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String elementName = getElementName(detail.get(0));
            String listItem = detail.get(2);
            int comboIndex = Integer.parseInt(detail.get(1));
            WebElement we = FlagBuilderPage.comboBoxByLabelAndIndex(driver, elementName, comboIndex);
            elementDisplayedValidation(driver, we, listItem);
            we.click();
            sleep(500);
            we = FlagBuilderPage.comboBoxItemByValue(driver, listItem);
            elementDisplayedValidation(driver, we, listItem);
            we.click();
        }
    }

    @Then("^the antennas with issues list is available$")
    public void antennasWithIssuesListDisplayed(List<String> details) {
        List<WebElement> antennas = GeneralPage.gridColumnElements(driver,
                getElementName(details.get(0)));
        Assert.assertTrue("The antennas with issues are not displayed!",
                antennas.size() > 0);
    }

    @Then("^select specific checkbox from the check list$")
    public void selectCheckListCheckBox(DataTable table) {
        List<List<String>> details = table.raw();
        int count = 0;
        sleep(500);
        for (List<String> detail : details) {
            String elementName = getElementName(detail.get(0));
            int comboIndex = Integer.parseInt(detail.get(1));
            WebElement checkList = FlagBuilderPage.groupBox3D(driver, elementName);
            if (count == 0) {
                elementDisplayedValidation(driver, checkList, elementName);
                checkList.click();
            }
            WebElement checkListItem = FlagBuilderPage.comboBoxItemByLabelAndIndex(driver, elementName,
                    comboIndex);
            elementDisplayedValidation(driver, checkListItem, elementName);
            checkListItem.click();
            if (count == details.size() - 1)
                checkList.click();
            count++;
        }
    }

    @Then("^collect the current number of saved flags$")
    public void collectCurrentNoOfSavedFlags(List<String> details) {
        List<WebElement> savedFlags = FlagBuilderPage.savedFlagsByLabelAndIndex(driver,
                getElementName(details.get(0)));
        itemIndex = savedFlags.size();
    }

    @Then("^the flag was saved/deleted$")
    public void checkIfTheFlagIsSaved(DataTable table) {
        List<List<String>> details = table.raw();
        List<WebElement> savedFlags = FlagBuilderPage.savedFlagsByLabelAndIndex(driver,
                getElementName(details.get(0).get(0)));
        switch (details.get(0).get(1).toLowerCase()) {
            case "saved": {
                Assert.assertTrue(String.format("The no. of the saved flags is '%d' and it should be "
                        + "'%d'!", savedFlags.size(), itemIndex), savedFlags.size() > itemIndex);
                break;
            }
            case "deleted": {
                Assert.assertTrue(String.format("The no. of the saved flags is '%d' and it should be "
                        + "'%d'!", savedFlags.size(), itemIndex), savedFlags.size() <= itemIndex);
                break;
            }
        }
    }

    @Then("^the user deletes the saved flag$")
    public void deleteFlag(List<String> details) {
        FlagBuilderPage.deleteFlagByLabelAndIndex(driver, getElementName(details.get(0))).click();
    }

    @Then("^the flag was deleted$")
    public void checkIfTheFlagIsDeleted(List<String> details) {
        List<WebElement> savedFlags = FlagBuilderPage.savedFlagsByLabelAndIndex(driver,
                getElementName(details.get(0)));
        Assert.assertTrue(String.format("The no. of the saved flags is '%d' and it should be "
                + "'%d'!", savedFlags.size(), itemIndex), savedFlags.size() < itemIndex);
    }

    @Then("^select a specific spectral window$")
    public void selectSpectralWindow(DataTable table) {
        List<List<String>> details = table.raw();
        int count = 0;
        for (List<String> detail : details) {
            String spw = getElementName(detail.get(0));
            String index = getElementName(detail.get(1));
            WebElement spwArea = FlagBuilderPage.groupBox3D(driver, spw);
            if (count == 0) {
                elementDisplayedValidation(driver, spwArea, spw);
                spwArea.click();
            }
            WebElement we = FlagBuilderPage.spwDiv(driver, index);
            elementDisplayedValidation(driver, we, index);
            we = FlagBuilderPage.spwDivCheckBox(driver, index);
            elementDisplayedValidation(driver, we, index);
            we.click();
            count++;
        }
    }

    @Then("^set the specific spectral window channel range$")
    public void setSpectralWindowChannelRange(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String index = detail.get(0);
            String start = detail.get(1);
            String end = detail.get(2);
            WebElement we = FlagBuilderPage.spwDivToolbarBtn(driver, index);
            elementDisplayedValidation(driver, we, index);
            we.click();
            we = FlagBuilderPage.popupContent(driver);
            elementDisplayedValidation(driver, we, index);
            FlagBuilderPage.intBoxFields(driver).get(0).sendKeys(start);
            FlagBuilderPage.intBoxFields(driver).get(1).sendKeys(end);
        }
    }


    @Then("^select a specific builder area checkbox$")
    public void selectBuilderAreaCheckbox(DataTable table) {
        List<List<String>> details = table.raw();
        int count = 0;
        for (List<String> detail : details) {
            String areaName = getElementName(detail.get(0));
            String checkboxName = getElementName(detail.get(1));
            WebElement area = FlagBuilderPage.groupBox3D(driver, areaName);
            if (count == 0) {
                elementDisplayedValidation(driver, area, areaName);
                area.click();
            }
            WebElement we = FlagBuilderPage.builderAreaOption(driver, checkboxName);
            elementDisplayedValidation(driver, we, checkboxName);
            we.click();
            if (count == details.size() - 1)
                area.click();
            count++;
        }
    }

    @Then("^resize the modal window$")
    public void resizeModalWindow() {
        WebElement we = FlagBuilderPage.flagBuilderPopup(driver);
        BrowserUtil.resize(driver, we, 50, 50);
    }

    @Then("^the user sets a specific QA3 substate$")
    public void checkIfTheFlagIsSaved(List<String> details) {
        String radioBtn;
        String radioBtnName;
        switch (getElementName(details.get(0)).toLowerCase()) {
            case "state": {
                radioBtn = extractMapValue(bufferMap, getElementName(details.get(0)));
                if (radioBtn.contains("User"))
                    radioBtnName = getElementName("user_initiated");
                else
                    radioBtnName = getElementName("observatory_initiated");
                break;
            }
            default: {
                radioBtnName = getElementName(details.get(0));
                break;
            }
        }
        GeneralPage.radioButton(driver, radioBtnName).click();
        LOG.info(String.format("'%s' QA3 substate was selected.", radioBtnName));
    }

//    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private void recursive(List<WebElement> ous, int index) {
        if (index > 1) {
            clickPopupButton(Collections.singletonList("ok"));
            clickOnButton(Collections.singletonList("cancel"));
        }
        ous.get(index).click();
        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        ousSummaryDisplayed();
        checkIfDataReducerIsAssigned(DataTable.create(
                Collections.singletonList(Arrays.asList("data_reducer", "data_reduction_methods"))));
        collectDataForValidation(DataTable.create(Arrays.asList(Collections.singletonList("sb_names"),
                Collections.singletonList("project_code"))));
        clickOnButton(Collections.singletonList("do_qa2"));
        doQA2Displayed();
        clickQAXStatus();
        selectQA2Status(Collections.singletonList("semi_pass_qa2"));
        clickOnQA2StatusReason();
        qa2StatusReasonListDisplayed();
        qa2StatusReasonStatusSelectByPosition(Collections.singletonList(""));
        addQA2Comment(DataTable.create(Collections.singletonList(
                Arrays.asList("qa2_comment", "Test comment"))));
        clickOnDoQA2Button(Collections.singletonList("save"));
    }

}
