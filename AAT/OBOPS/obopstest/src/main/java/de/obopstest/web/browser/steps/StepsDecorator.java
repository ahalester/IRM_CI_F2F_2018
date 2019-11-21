package de.obopstest.web.browser.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.obopstest.common.SessionStateHandler;
import de.obopstest.web.browser.setup.AbstractSelenium3DockerMultiBrowserDriverSetup;
import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.pageobjects.*;
import de.obopstest.web.utils.*;
import de.obopstest.web.utils.enums.EnvironmentURL;
import de.obopstest.web.utils.enums.PageTitle;
import de.obopstest.web.utils.enums.Pipelines;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static de.obopstest.web.utils.PropertiesFileUtil.*;
import static de.obopstest.web.utils.enums.EnvironmentURL.*;



/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class StepsDecorator extends AbstractSelenium3DockerMultiBrowserDriverSetup {

    private static Logger LOG = LoggerFactory.getLogger(StepsDecorator.class);
    private List<String> auth = new ArrayList<>();
    private WebDriverExtended driver = getWebDriverExtended();

    @Given("^test step debug$")
    public void testStepDebug(){
        WebElement txtBox = driver.findElementAndWait(By.xpath("//span[@class[contains(.,'titleLabel)] and text()[contains(.,'Alliance Consulting')]]"));
    }

    @Given("^the \"([^\"]*)\" test environment is available$")
    public void verifyIfTestEnvIsAvailable(String toolName) {
        try {
            driver.getPageSource();
            String url = "";
            switch (toolName.toUpperCase()) {
                case "PROTRACK": {
                    url = PropertiesFileUtil.getNavigationURL(EnvironmentURL.PROTRACK, System.getProperty("testEnv"));
                    break;
                }
                default: {
                    break;
                }
            }
            SessionStateHandler.setValue("url_protrack",url);
            NavigatePage.toGlobalLink(driver, url);
            WebElementValidationUtil.elementDisplayedValidation(driver, LoginPage.userField(driver));
        } catch (Exception e) {
            LOG.error("Current build environment cannot be accessed! Please check the provided "
                    + "URL.");
            Assert.fail("Current build environment cannot be accessed! Please check the provided URL.");
        }
    }



    @Given("^Protrack test environment is available$")
    public void verifyIfProtrackTestEnvIsAvailable() {
        try {
            driver.getPageSource();
            String url = PropertiesFileUtil.getNavigationURL(EnvironmentURL.PROTRACK, System.getProperty("testEnv"));
            NavigatePage.toGlobalLink(driver, url);
            WebElementValidationUtil.elementDisplayedValidation(driver, LoginPage.userField(driver));
        } catch (Exception e) {
            LOG.error("Current build environment cannot be accessed! Please check the provided URL.");
            Assert.fail("Current build environment cannot be accessed! Please check the provided URL.");
        }
    }

    @Given("^the Protrack test environment is available$")
    public void verifyIfTestEnvIsAvailable() {
        String url = "";
        String envName = System.getProperty("envPhase");
        try {
            driver.getPageSource();
            if (envName.equalsIgnoreCase("PHAB")) {
                url = PropertiesFileUtil.getNavigationURL(PHB_URL, System.getProperty("testEnv"));
            } else if (envName.equalsIgnoreCase("PHAA")) {
                url = getNavigationURL(PHA_URL, System.getProperty("testEnv").toUpperCase());
            }else if (envName.equalsIgnoreCase("PHADEV")) {
                url = getNavigationURL(PHA_DEV);
            }
            NavigatePage.toGlobalLink(driver, url);
//            elementDisplayedValidation(driver, LoginPage.userField(driver));
        } catch (Exception e) {
            LOG.error(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
            Assert.fail(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
        }
    }

    @When("^the user navigates to a specific URL$")
    public void goToURL(List<String> details) {
        String url = PropertiesFileUtil.getNavigationURL(details.get(0), EnvironmentURL.ENV_VERSION);
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

    @Given("^Protrack login page is displayed$")
    public void PtLoginPageIsDisplayed() {
        WebElementValidationUtil.elementDisplayedValidation(driver, LoginPage.userField(driver));
        WaitUtil.sleep(2000);
    }

    @Given("^CAS login page is displayed$")
    public void CasLoginPageIsDisplayed() {
        WebElementValidationUtil.elementDisplayedValidation(driver, LoginPage.userField(driver));
        WaitUtil.sleep(2000);
    }

    @When("^the user fills the credentials$")
    public void inputCredentials(List<String> credentials) {
        auth.add(credentials.get(0));
        auth.add(credentials.get(1));
        WebElementValidationUtil.elementDisplayedValidation(driver, LoginPage.userField(driver));
        LoginPage.userField(driver).clear();
        LoginPage.userField(driver).sendKeys(PropertiesFileUtil.getProperty(credentials.get(0)));
        LOG.info(String.format("Filled the '%s' field with '%s'", credentials.get(0),
                PropertiesFileUtil.getProperty(credentials.get(0))));

        WebElementValidationUtil.elementDisplayedValidation(driver, LoginPage.passwordField(driver));
        LoginPage.passwordField(driver).clear();
        LoginPage.passwordField(driver)
                .sendKeys(EncryptUtil.decrypt(PropertiesFileUtil.getProperty(credentials.get(1))));
        LOG.info(String.format("Filled the '%s' field with '%s' (encrypted)", credentials.get(1),
                PropertiesFileUtil.getProperty(credentials.get(1))));
    }

    @When("^the user checks the warn checkbox$")
    public void checkWarn() {
        WebElement we = LoginPage.warnCheckbox(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        if (!we.isSelected()) {
            we.click();
            LOG.info(String.format("Clicked on '%s'", we.getAttribute("id")));
        }
    }

    @When("^the user unchecks the warn checkbox$")
    public void unCheckWarn() {
        WebElement we = LoginPage.warnCheckbox(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        if (we.isSelected()) {
            LOG.info(String.format("Clicked on '%s'", we.getAttribute("id")));
            we.click();
        }
    }

    @When("^the user clicks the LOGIN button$")
    public void clickLogin() {
        WebElement we = LoginPage.loginButton(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'Login'");
        WaitUtil.sleep(3000);

    }

    @Then("^the user is authenticated$")
    public void userAuthenticated(List<String> user) {
        String userName = getElementName(user.get(0));
        WebElement we = GeneralPage.html(driver, userName);
        WebElementValidationUtil.elementDisplayedValidation(driver, we, getElementName(user.get(0)),
                WaitUtil.DEFAULT_WAIT_FOR_ELEMENT);
        String loggedUser = we.getText();
        Assert.assertTrue(String.format("The logged user %s is different by the mentioned one,"
                        + " %s!", loggedUser, user.get(0)),
                loggedUser.contains(PropertiesFileUtil.getProperty(user.get(0))));
    }

    @Then("^the Protrack view is displayed$")
    public void protrackViewAvailable(List<String> details) {
//        WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.img(driver, getElementName(details.get(0))), WaitUtil.DEFAULT_WAIT_FOR_ELEMENT);
        Wait.waitForElement(driver,GeneralPage.img(driver, getElementName(details.get(0))));
    }







    // ############################################################################################
    @When("^the user clicks on QA2 tab$")
    public void qa2TabClick() {
        WebElement we = QA0Page.qa2Tab(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'QA2' tab");
        WaitUtil.sleep(2000);
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
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info(String.format("Clicked on '%s'", search));
        Assert.assertTrue(String.format("The tab %s is not selected!", searchTab.get(0)),
                we.getAttribute("class").contains("selected"));
    }

    @Then("^the specific elements are available$")
    public void specificElementAvailable(DataTable table) {
        WaitUtil.sleep(500);
        List<List<String>> elements = table.raw();
        for (List<String> element : elements) {
            String elem = element.get(1);
            String elementName = "";
            switch (elem.toLowerCase()) {
                case "label": {
                    elementName = getElementName(element.get(0));
                    WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.label(driver, elementName),
                            elementName);
                    break;
                }
                case "checkbox": {
                    elementName = getElementName(element.get(0));
                    WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.checkBox(driver, elementName),
                            elementName);
                    break;
                }
                case "datebox": {
                    elementName = getElementName(element.get(2));
                    WebElementValidationUtil.elementDisplayedValidation(driver, QA0Page.dateBox(driver, elementName),
                            elementName);
                    break;
                }
                case "textbox": {
                    elementName = getElementName(element.get(0));
                    WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.textBox(driver, elementName),
                            elementName);
                    break;
                }
                case "combobox": {
                    elementName = getElementName(element.get(0));
                    WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.comboBox(driver, elementName),
                            elementName);
                    break;
                }
                case "button": {
                    elementName = getElementName(element.get(0));
                    WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.button(driver, elementName),
                            elementName);
                    break;
                }
                case "doublebox": {
                    elementName = getElementName(element.get(0));
                    WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.doubleBox(driver, elementName),
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
        WebElementValidationUtil.elementDisplayedValidation(driver, QA2Page.qa2MainView(driver));
    }

    @Then("^the user checks/un-checks a specific checkbox$")
    public void checkboxEvent(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String name = getElementName(detail.get(0));
            String action = detail.get(1);
            WebElement we = GeneralPage.checkBox(driver, name);
            switch (action) {
                case "check": {
                    WebElementValidationUtil.elementDisplayedValidation(driver, we, name);
                    we.click();
                    break;
                }
                case "uncheck": {
                    WebElementValidationUtil.elementDisplayedValidation(driver, we, name);
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
                    WebElementValidationUtil.elementDisplayedValidation(driver, we);
                    if (entry.getValue().equals("false")) {
                        we.findElement(By.tagName("input")).click();
                        LOG.info(String.format("Clicked on '%s'", name));
                    }
                    break;
                }
                case "uncheck": {
                    WebElementValidationUtil.elementDisplayedValidation(driver, we);
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
            WaitUtil.waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                    WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        }
    }

    @When("^the user selects one of the available OUS")
    public void selectObsUnitSet(List<String> details) {
        WebElement we = QA2Page.selectObsUnitSet(driver, details.get(0));
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on one of the available OUSes");
        WaitUtil.waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @When("^the user selects the first available OUS")
    public void selectFirstObsUnitSet() {
        try {
            if (GeneralPage.noItemDisplayed(driver)
                    .getCssValue("display").equalsIgnoreCase("none")) {
                WebElement we = QA2Page.selectFirstObsUnitSet(driver);
                try {
                    WebElementValidationUtil.elementDisplayedValidation(driver, we, "first OUS");
                    we.click();
                    WaitUtil.waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                            WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                } catch (Exception ex) {
                    Assert.fail("No OUS to be displayed based on the provided filter!");
                }
            } else {
                Assert.fail("No OUS to be displayed based on the provided filter!");
            }
        } catch (Exception e) {
            WebElement we = QA2Page.selectFirstObsUnitSet(driver);
            try {
                WebElementValidationUtil.elementDisplayedValidation(driver, we, "first OUS");
                we.click();
                WaitUtil.waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                        WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
            } catch (Exception ex) {
                Assert.fail("No OUS to be displayed based on the provided filter!");
            }
        }
        LOG.info("Clicked on the first available OUS");
    }

    @Then("^the OUS Summary details page is displayed$")
    public void ousSummaryDisplayed() {
        WaitUtil.sleep(500);
        WebElementValidationUtil.elementDisplayedValidation(driver, OBSUnitSetSummaryPage.summary(driver), "OUS Summary");
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
        String data = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, tagLabelName);
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
            WaitUtil.sleep(2000);
            OBSUnitSetSummaryPage.searchDataReducerIcon(driver).click();
            LOG.info("Clicked on 'search' icon");
            WaitUtil.sleep(1000);
            OBSUnitSetSummaryPage.selectFirstDataReducer(driver).click();
            LOG.info("Clicked on first displayed data reducer");
            WaitUtil.sleep(2000);
            OBSUnitSetSummaryPage.dataReducerPopupButton(driver, "save").click();
            LOG.info("Clicked on 'Save'");
            WaitUtil.sleep(1000);
            //  waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
            //  DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        }
        RuntimeBufferUtil.bufferMap.put("Data reducer",
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
            WaitUtil.sleep(2000);
            OBSUnitSetSummaryPage.dataReducerPopupButton(driver, "save").click();
            LOG.info("Clicked on 'Save'");
            WaitUtil.sleep(2000);
        }
        //  Assign Data reducer
        OBSUnitSetSummaryPage.clickToAdd(driver, tagLabelName).click();
        //  OBSUnitSetSummaryPage.searchDataReducerIcon(driver).click();
        LOG.info("Clicked on 'Data Reducer'");
        WaitUtil.sleep(2000);
        OBSUnitSetSummaryPage.searchDataReducerTextField(driver).sendKeys(details.get(0).get(2));
        LOG.info(String.format("Filled the text field with '%s'", details.get(0).get(2)));
        WaitUtil.sleep(2000);
        OBSUnitSetSummaryPage.selectFirstDataReducer(driver).click();
        LOG.info("Clicked on first displayed data reducer");
        WaitUtil.sleep(2000);
        OBSUnitSetSummaryPage.dataReducerPopupButton(driver, "save").click();
        LOG.info("Clicked on 'Save'");
        try {
            WebElement element = GeneralPage.loadingProgress(driver);
            //  waitForLocatedElementIsNotDisplayed(driver, element,
            //  DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        } catch (Exception e) {
            LOG.info("'Loading progress' not displayed");
        }
        RuntimeBufferUtil.bufferMap.put("Data reducer",
                OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, tagLabelName)
                        .getAttribute("innerHTML"));
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
        WaitUtil.sleep(2000);
        OBSUnitSetSummaryPage.searchDataReducerTextField(driver).sendKeys(details.get(0).get(1));
        LOG.info(String.format("Filled the text field with '%s'", details.get(0).get(1)));
        WaitUtil.sleep(2000);
        OBSUnitSetSummaryPage.selectFirstDataReducer(driver).click();
        LOG.info("Clicked on first displayed recipient");
        WaitUtil.sleep(2000);
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
        String dr = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, getElementName(details.get(0)));
        WebElementValidationUtil.elementDisplayedValidation(driver, OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver,
                dr));
    }

    @Then("^the specific information is displayed$")
    public void dynamicLabelContentDisplayed(List<String> details) {
        String dr = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, details.get(0));
        WebElementValidationUtil.elementDisplayedValidation(driver, OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver,
                dr));
    }

    @Then("^the specific label is displayed$")
    public void specificLabelContentDisplayed(List<String> details) {
        for (String labelName : details) {
            if (labelName.equalsIgnoreCase("comment")) {
                labelName = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, labelName);
            } else {
                labelName = getElementName(labelName);
            }
            WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.specificLabel(driver, labelName), labelName);
        }
    }

    @Then("^the Data reducer is no longer displayed$")
    public void dataReducerNotDisplayed(List<String> details) {
        String tagLabelName = getElementName(details.get(0));
        String dr = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, tagLabelName);
        Assert.assertTrue(String.format("Data reducer %s is still displayed!", tagLabelName),
                OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, dr) == null);
    }

    @Then("^the specific element is no longer displayed$")
    public void elementNotDisplayed(List<String> details) {
        Boolean exists = true;
        String labelName = getElementName(details.get(0));
        try {
            //  TODO - to be implemented for a specific element
            GeneralPage.label(driver, labelName);
        } catch (Exception e) {
            exists = false;
        }
        Assert.assertFalse(String.format("The element %s is still displayed!", labelName), exists);
    }

    @When("^the user clicks on a specific button$")
    public void clickOnButton(List<String> details) {
        String buttonName = getElementName(details.get(0));
        List<WebElement> saveButtons = GeneralPage.sameNameVisibleButtons(driver, buttonName);
        if (saveButtons.size() < 2) {
            try {
                WebElement we = GeneralPage.button(driver, buttonName);
                WebElementValidationUtil.elementDisplayedValidation(driver, we, buttonName);
                we.click();
            } catch (Exception e) {
                WebElement we = OBSUnitSetSummaryPage.button(driver, buttonName);
                WebElementValidationUtil.elementDisplayedValidation(driver, we, buttonName);
                we.click();
            }
            if (buttonName.equalsIgnoreCase("DO QA2")) {
                WaitUtil.sleep(1000);
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
                            WaitUtil.waitForLocatedElementIsNotDisplayed(driver, we,
                                    WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                        }
                    } catch (Exception e1) {
                        WaitUtil.sleep(5000);
                    }
                }
            }
        } else {
            saveButtons.get(1).click();
        }
        LOG.info(String.format("Clicked on '%s'", buttonName));
    }

    @When("^the user clicks on a specific link$")
    public void clickOnLink(List<String> details) {
        WebElement we = GeneralPage.link(driver, getElementName(details.get(0)));
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
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
                WebElementValidationUtil.elementDisplayedValidation(driver, we, elementName);
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
                WebElementValidationUtil.elementDisplayedValidation(driver, we, buttonName);
                we.click();
            } catch (Exception e) {
                WebElement we = GeneralPage.button(driver, buttonName);
                WebElementValidationUtil.elementDisplayedValidation(driver, we, buttonName);
                we.click();
            }
        } else {
            saveButtons.get(1).click();
        }
        LOG.info(String.format("Clicked on '%s'", buttonName));
        WaitUtil.sleep(5000);
    }

    @When("^the user clicks on a specific toolbar button$")
    public void clickOnToolbarButton(List<String> details) {
        WebElement we = GeneralPage.toolBarButton(driver, getElementName(details.get(0)));
        WebElementValidationUtil.elementDisplayedValidation(driver, we, getElementName(details.get(0)));
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
            WaitUtil.sleep(5000);
            try {
                SmartRobotUtil robot = new SmartRobotUtil();
                robot.type(PropertiesFileUtil.getProperty("username"));
                robot.keyPress(KeyEvent.VK_TAB);
                robot.type(EncryptUtil.decrypt(PropertiesFileUtil.getProperty("password")));
                robot.keyPress(KeyEvent.VK_ENTER);
            } catch (Exception AWTException) {
                LOG.info("Exception \n" + AWTException.getMessage());
            }
            WaitUtil.sleep(3000);
        }
//        }
//        Wait.sleep(4000);
    }

    @Then("^the Do QA2 pop-up form is displayed$")
    public void doQA2Displayed() {
        WaitUtil.sleep(500);
        WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.doQAXForm(driver), "pop-up form");
    }

    @Then("^the Do QA0 pop-up form is displayed$")
    public void doQA0Displayed() {
        WaitUtil.sleep(500);
        WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.doQAXForm(driver));
    }

    @When("^the user clicks on the Set QA0 Status$")
    public void clickQA2Status() {
        WebElement we = GeneralPage.setQAXStatus(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'Set QA0 Status'");
    }

    @When("^the user clicks on the Set QA2 Status$")
    public void clickQA0Status() {
        WebElement we = GeneralPage.setQAXStatus(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'Set QA2 Status'");
    }

    @When("^the user selects specific status$")
    public void selectQA2Status(List<String> details) {
        WebElement we = OBSUnitSetSummaryPage.selectStatus(driver, getElementName(details.get(0)));
//        elementDisplayedValidation(driver, we, getElementName(details.get(0)));
        we.click();
        LOG.info(String.format("Clicked on '%s'", getElementName(details.get(0))));
    }

    @Then("^the confirmation pop-up is displayed$")
    public void confirmationPopupDisplayed() {
        WaitUtil.sleep(1500);
        WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.dialogBox(driver),
                "confirmation dialog");
    }

    @When("^the user clicks one of the popup's buttons$")
    public void clickPopupButton(List<String> details) {
        WaitUtil.sleep(500);
//        WaitUtil.waitForElement(driver, By.className(""), 10);
        WebElement we = GeneralPage.dialogBoxActionButton(driver, getElementName(details.get(0)));
        WebElementValidationUtil.elementDisplayedValidation(driver, we, getElementName(details.get(0)));
        we.click();
        LOG.info(String.format("Clicked on '%s'", details.get(0)));
        try {
            GeneralPage.loadingProgress(driver).isDisplayed();
            WaitUtil.waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                    WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        } catch (Exception e) {
            WaitUtil.sleep(3000);
        }
    }

    @When("^the user clicks on a specific button on Do QA2 page$")
    public void clickOnDoQA2Button(List<String> details) {
        WebElement we = OBSUnitSetSummaryPage.doQA2FormButton(driver, getElementName(details.get(0)));
        WebElementValidationUtil.elementDisplayedValidation(driver, we, getElementName(details.get(0)));
        we.click();
        LOG.info(String.format("Clicked on '%s'", details.get(0)));
    }

    @When("^the user clicks on QA0 Status Reason$")
    public void clickOnQA0StatusReason() {
        WaitUtil.sleep(2000);
        WebElement we = GeneralPage.doQAXFormStatusReason(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we, "Status Reason");
        we.click();
        LOG.info("Clicked on 'QA0 Status Reason'");
    }

    @When("^the user clicks on QA2 Status Reason$")
    public void clickOnQA2StatusReason() {
        WaitUtil.sleep(2000);
        WebElement we = GeneralPage.doQAXFormStatusReason(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we, "Status Reason");
        we.click();
        LOG.info("Clicked on 'QA2 Status Reason'");
    }

    @Then("^the QA0 Status Reason options are displayed$")
    public void qa0StatusReasonListDisplayed() {
        WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.doQAXFormStatusReasonList(driver));
    }

    @Then("^the QA2 Status Reason options are displayed$")
    public void qa2StatusReasonListDisplayed() {
        WaitUtil.sleep(1500);
        WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.doQAXFormStatusReasonList(driver),
                "QA2 Status Reason options");
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
            WebElementValidationUtil.elementDisplayedValidation(driver, we);
            we.click();
        } else {
            WebElement we = GeneralPage.doQAXFormReasonStatusOptionPosition(driver);
            WebElementValidationUtil.elementDisplayedValidation(driver, we);
            we.click();
        }
        LOG.info(String.format("Clicked on 'QA0 Reason status' from position '%s'",
                details.get(0)));
    }

    @Then("^the user selects one QA2 Reason status by position$")
    public void qa2StatusReasonStatusSelectByPosition(List<String> details) {
        if (details.size() > 0 && StringUtils.isNumeric(details.get(0))
                && StringUtils.isNotBlank(details.get(0))) {
            WebElement we = GeneralPage.doQAXFormReasonStatusOptionPosition(driver, details.get(0));
            WebElementValidationUtil.elementDisplayedValidation(driver, we, details.get(0));
            we.click();
        } else {
            WebElement we = GeneralPage.doQAXFormReasonStatusOptionPosition(driver);
            WebElementValidationUtil.elementDisplayedValidation(driver, we, details.get(0));
            we.click();
        }
        LOG.info(String.format("Clicked on 'QA2 Reason status' from position '%s'",
                details.get(0)));
    }

    @Then("^the user sets an EC value$")
    public void setManualECValue(List<String> details) {
        WebElement we = GeneralPage.doQAXFormEC(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we, "QA form");
        String currentEC = we.getAttribute("value");
        we.clear();
//        String newEC = String.valueOf(
//                Double.parseDouble(OBSUnitSetSummaryPage.doQA2FormECOriginalValue(driver)
//                        .getAttribute("innerHTML")) - Double.parseDouble(details.get(0)));
        String newEC = String.valueOf(Double.parseDouble(currentEC) - Double.parseDouble(details.get(0)));
//        bufferMap.put("EC_old", OBSUnitSetSummaryPage.doQA2FormECOriginalValue(driver)
//                .getAttribute("innerHTML"));
        RuntimeBufferUtil.bufferMap.put("EC_old", currentEC);
        RuntimeBufferUtil.bufferMap.put("EC_new", newEC);
        RuntimeBufferUtil.bufferMap.put("EF_old", "1");
        we.sendKeys(newEC);
    }

    @Then("^the state confirmation message box is displayed$")
    public void changeStateDialogBoxDisplayed() {
        WebElementValidationUtil.elementDisplayedValidation(driver, OBSUnitSetSummaryPage.stateChangeMessageBox(driver));
    }

    @Then("^the dialog box with specific message is displayed$")
    public void infoECBoxDisplayed(List<String> details) {
        WebElement we = OBSUnitSetSummaryPage.infoECValue(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        Assert.assertTrue(String.format("The message displayed into the EC info box is %s and it "
                        + "should be %s!", we.getAttribute("innerHTML"), details.get(0)),
                Objects.equals(we.getAttribute("innerHTML"), details.get(0)));
    }

    @When("^the user clicks OK on the EC dialog box$")
    public void confirmECBox() {
        WebElement we = OBSUnitSetSummaryPage.infoECokBtn(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'OK'");
    }

    @Then("^the OUS state is changed$")
    public void ousStateIsChanged(DataTable table) {
        List<List<String>> details = table.raw();
        LOG.info("Verifying the OUS state in AQUA");
        String tagLabelName = getElementName(details.get(0).get(0));
        String newStatus = getElementName(details.get(0).get(1));
        WebElement we = OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, tagLabelName);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        Assert.assertTrue(String.format("The OUS state is %s and it should be %s!",
                we.getAttribute("innerHTML"), newStatus),
                we.getAttribute("innerHTML").contains(newStatus));
    }

    @Then("^verify OUS state")
    public void ousStateVerify(DataTable table) {
        List<List<String>> details = table.raw();
        LOG.info("Verifying the OUS state in AQUA");
        String newStatus = getElementName(details.get(0).get(0));
        WebElement we = OBSUnitSetSummaryPage.getOusStatusLabel(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        Assert.assertTrue(String.format("The OUS state is %s and it should be %s!",
                we.getText(), newStatus),
                we.getText().trim().contains(newStatus.trim()));
    }

    @Then("^the OUS state is not changed$")
    public void ousStateIsNotChanged(List<String> details) {
        String tagLabelName = getElementName(details.get(0));
        WebElement we = OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, tagLabelName);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        Assert.assertTrue(String.format("The OUS state is %s and it should be %s!",
                we.getAttribute("innerHTML"), RuntimeBufferUtil.buffer),
                Objects.equals(we.getAttribute("innerHTML"), RuntimeBufferUtil.buffer));
    }

    @Then("^the confirmation dialog contains the specific message$")
    public void validateTextOnConfirmationDialog(List<String> details) {
        WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.dialogBox(driver));
        WebElement we = OBSUnitSetSummaryPage.confirmationBoxMessage(driver);
//        Assert.assertTrue(String.format("The text '%s' on the confirmation dialog doesn't "
//                        + "contain '%s'!", we.getAttribute("innerHTML"), details.get(0)),
//                we.getAttribute("innerHTML").contains(details.get(0).trim()));

        String actual = we.getAttribute("innerHTML").toString();
        String expected = details.get(0);
        LOG.info("Dialog box message: " + StringUtil.removeBreakingWhitespaceIgnoring(actual,"<br>"));
//        String actual = StringUtil.removeBreakingWhitespace(we.getAttribute("innerHTML").toString());
//        String expected = StringUtil.removeBreakingWhitespace(details.get(0));
        Assert.assertTrue(String.format("The text '%s' on the confirmation dialog doesn't "
                        + "contain '%s'!", actual, expected),
                StringUtil.compareStringsIgnoring(actual,expected,"<br>"));
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
                    WebElementValidationUtil.elementDisplayedValidation(driver, we, "first EB");
                    we.click();
                    WaitUtil.waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                            WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                } catch (Exception ex) {
                    Assert.fail("No EB to be displayed based on the provided filter!");
                }
            } else {
                Assert.fail("No EB to be displayed based on the provided filter!");
            }
        } catch (Exception e) {
            WebElement we = QA0Page.selectFirstEB(driver);
            try {
                WebElementValidationUtil.elementDisplayedValidation(driver, we, "first EB");
                we.click();
                WaitUtil.waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                        WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
            } catch (Exception ex) {
                Assert.fail("No EB to be displayed based on the provided filter!");
            }
        }
        LOG.info("Clicked on the first available EB");
    }

    @Then("^the EB Summary details page is displayed$")
    public void ebSummaryDisplayed() {
        WebElementValidationUtil.elementDisplayedValidation(driver, EBSummaryPage.summary(driver));
        if (GeneralPage.loadingProgressItems(driver) > 1) {
            WaitUtil.waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                    WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        }
    }

    @Then("^wait for the loading progress to be completed$")
    public void loadingProgressAvailability() {
        WaitUtil.waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @Then("^the information dialog box is displayed$")
    public void informationDialogDisplayed() {
        WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.dialogBox(driver));
    }

    @Then("^the information dialog box message is consistent$")
    public void validateDialogMessage(List<String> details) {
        WebElement we = GeneralPage.dialogBoxMessage(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we, details.get(0));
        String msg = we.getAttribute("innerHTML");
        Assert.assertTrue(String.format("The text '%s' on the information dialog does't contain "
                + " the substring '%s'!", msg, details.get(0)), msg.contains(details.get(0)));
    }

    @When("^the user clicks on a specific button on the the information dialog box$")
    public void clickDialogBoxButton(List<String> details) {
        WebElement we = GeneralPage.dialogBoxActionButton(driver, getElementName(details.get(0)));
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info(String.format("Clicked on '%s'", details.get(0)));
        try {
            WebElement lp = GeneralPage.loadingProgress(driver);
            if (lp.isDisplayed()) {
                WaitUtil.waitForLocatedElementIsNotDisplayed(driver, lp,
                        WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
            }
        } catch (Exception e1) {
            WaitUtil.sleep(5000);
        }
    }

    @When("^the user clicks on a specific field$")
    public void clickOnSpecificField(DataTable table) {
        List<List<String>> details = table.raw();
        WebElement we = QA2Page.ousStateFlagField(driver, getElementName(details.get(0).get(0)));
        WebElementValidationUtil.elementDisplayedValidation(driver, we, getElementName(details.get(0).get(0)));
        we.click();
        LOG.info(String.format("Clicked on '%s'", getElementName(details.get(0).get(0))));
    }

    @Then("^the available options are displayed$")
    public void specificListOptionsDisplayed(List<String> details) {
        WebElementValidationUtil.elementDisplayedValidation(driver, QA2Page.ousOptionList(driver));
    }

    @When("^the user selects a specific option$")
    public void selectSpecificListOption(DataTable table) {
        List<List<String>> details = table.raw();
        WebElement we = QA2Page.ousStateFlagOption(driver, getElementName(details.get(0).get(1)));
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info(String.format("Clicked on '%s'", getElementName(details.get(0).get(1))));
    }

    @When("^the specific field option is selected$")
    public void specificFieldOptionIsSelected(DataTable table) {
        List<List<String>> details = table.raw();
        WaitUtil.sleep(1000);
        WebElement we = GeneralPage.selectedFieldOption(driver, getElementName(details.get(0)
                .get(0)));
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
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
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.clear();
        String commentBody = details.get(0).get(1) + " " + System.currentTimeMillis();
        RuntimeBufferUtil.bufferMap.put("comment", commentBody);
        we.sendKeys(commentBody);
    }

    @Then("^the user adds a QA2 comment$")
    public void addQA2Comment(DataTable table) {
        List<List<String>> details = table.raw();
        WebElement we = GeneralPage.doQAXFormComment(driver,
                getElementName(details.get(0).get(0)));
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.clear();
        String commentBody = details.get(0).get(1) + " " + System.currentTimeMillis();
        RuntimeBufferUtil.bufferMap.put("comment", commentBody);
        we.sendKeys(commentBody);
        LOG.info(String.format("Filled the '%s' field with '%s'", getElementName(details.get(0).get(0)),
                commentBody));
    }

    @Then("^the user inserts a value into a specific field$")
    public void populateTextField(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = getElementName(details.get(0).get(0));
        String inputText = getElementName(details.get(0).get(1));
//        WebElement we = QA2Page.advancedOUSTextField(driver, fieldName);
        WebElement we = GeneralPage.findTextFieldInZToolbar(driver, fieldName);
        if (inputText.equalsIgnoreCase("project_code")
                || details.get(0).get(1).equalsIgnoreCase("project_code")) {
            inputText = ReadWriteTextToFileUtil.readFile("project_code");
            WebElementValidationUtil.elementDisplayedValidation(driver, we);
            we.sendKeys(inputText);
        } else if (fieldName.equalsIgnoreCase("sb_names")
                || fieldName.equalsIgnoreCase("sb_name")
                || details.get(0).get(1).equalsIgnoreCase("sb_name")
                || details.get(0).get(1).equalsIgnoreCase("sb_names")) {
            inputText = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, inputText);
            WebElementValidationUtil.elementDisplayedValidation(driver, we);
            we.sendKeys(getElementName(inputText));
        } else if (inputText.equalsIgnoreCase("user_search")) {
            inputText = inputText + "_" + StringUtil.generateTimeStamp();
            RuntimeBufferUtil.userSearchName = inputText;
            WebElementValidationUtil.elementDisplayedValidation(driver, we);
            we.sendKeys(getElementName(inputText));
        } else {
            WebElementValidationUtil.elementDisplayedValidation(driver, we);
            we.sendKeys(getElementName(inputText));
        }
        LOG.info(String.format("Filled the '%s' field with '%s'", fieldName, inputText));
        WaitUtil.sleep(1000);
    }

    @Then("^the user enters a value into a specific field$")
    public void enterTextField(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = getElementName(details.get(0).get(0));
        String inputText = getElementName(details.get(0).get(1));
//        WebElement we = QA2Page.advancedOUSTextField(driver, fieldName);
        WebElement we = GeneralPage.findTextFieldInZToolbar(driver, fieldName);
        if (inputText.equalsIgnoreCase("project_code")
                || details.get(0).get(1).equalsIgnoreCase("project_code")) {
            inputText = ReadWriteTextToFileUtil.readFile("project_code");
            WebElementValidationUtil.elementDisplayedValidation(driver, we);
            we.sendKeys(inputText);
        } else if (fieldName.equalsIgnoreCase("sb_names")
                || fieldName.equalsIgnoreCase("sb_name")
                || details.get(0).get(1).equalsIgnoreCase("sb_name")
                || details.get(0).get(1).equalsIgnoreCase("sb_names")) {
            inputText = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, inputText);
            WebElementValidationUtil.elementDisplayedValidation(driver, we);
            we.sendKeys(getElementName(inputText));
        } else if (inputText.equalsIgnoreCase("user_search")) {
            inputText = inputText + "_" + StringUtil.generateTimeStamp();
            RuntimeBufferUtil.userSearchName = inputText;
            WebElementValidationUtil.elementDisplayedValidation(driver, we);
            we.sendKeys(getElementName(inputText));
        } else {
            WebElementValidationUtil.elementDisplayedValidation(driver, we);
            we.sendKeys(getElementName(inputText));
        }
        LOG.info(String.format("Filled the '%s' field with '%s'", fieldName, inputText));
        we.sendKeys(Keys.ENTER);
    }

    @Then("^the user sets the 'Favourite searches' name$")
    public void populateComboField(DataTable table) {
        List<List<String>> details = table.raw();
        String fieldName = getElementName(details.get(0).get(0));
        String inputText = getElementName(details.get(0).get(1));
        WebElement we = QA2Page.advancedOUSComboField(driver, fieldName);
        String text = inputText + "_" + StringUtil.currentDate(System.currentTimeMillis());
        //  userSearchName = text;
        WebElementValidationUtil.elementDisplayedValidation(driver, we, fieldName);
        we.sendKeys(getElementName(text));
        WaitUtil.sleep(1000);
    }

    @Then("^the user inserts a value into a specific caption field$")
    public void populateCaptionTextBox(DataTable table) {
        List<List<String>> details = table.raw();
        WebElement we = GeneralPage.captionTextBox(driver, getElementName(details.get(0).get(0)));
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        driver.executeScript("arguments[0].scrollIntoView(true);", we);
        String commentBody = details.get(0).get(1) + " " + System.currentTimeMillis();
        RuntimeBufferUtil.bufferMap.put("comment", commentBody);
        we.sendKeys(commentBody);
    }

    @Then("^the user inserts a value into a specific doublebox field$")
    public void populateDoubleBoxField(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String elementName = getElementName(detail.get(0));
            WebElement we = GeneralPage.doubleBox(driver, elementName);
            WebElementValidationUtil.elementDisplayedValidation(driver, we, elementName);
            we.clear();
            we.sendKeys(detail.get(1));
            LOG.info(String.format("Filled the '%s' field with '%s'", elementName,
                    detail.get(1)));
        }
    }

    @Then("^the specific data is collected for validation$")
    public void collectDataForValidation(DataTable table) {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String tagLabelName = getElementName(detail.get(0));
            WebElement we = GeneralPage.getDynamicLabelByTagLabel(driver, tagLabelName);
            WebElementValidationUtil.elementDisplayedValidation(driver, we, tagLabelName);
            String innerValue = we.getAttribute("innerHTML");
            RuntimeBufferUtil.bufferMap.put(tagLabelName, innerValue);
            RuntimeBufferUtil.buffer = innerValue;
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
                WaitUtil.waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                        WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                WaitUtil.sleep(10000);
                WebElementValidationUtil.elementDisplayedValidation(driver, EBSummaryPage.summary(driver));
                //  FIXME - sometimes the page is loaded very fast
                WaitUtil.waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                        WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                WaitUtil.sleep(10000);
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
                WaitUtil.sleep(10000);
                WebElementValidationUtil.elementDisplayedValidation(driver, EBSummaryPage.summary(driver),
                        getElementName(details.get(0)));
//                waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
//                        DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                WaitUtil.sleep(10000);
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
            WaitUtil.waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                    WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        }
    }

    @Then("^the specific button is displayed")
    public void specificButtonDisplayed(List<String> details) {
        for (String detail : details) {
            WaitUtil.sleep(2000);
            WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.button(driver, getElementName(detail)),
                    getElementName(detail), 10);
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
            Assert.assertFalse(String.format("The button %s is still available within the "
                    + "page!", buttonName), exists);
        }
    }

    @Then("^the specific button is enabled")
    public void specificButtonEnabled(List<String> details) {
        Boolean failed = false;
        for (String detail : details) {
            WaitUtil.sleep(2000);
            WebElement we = GeneralPage.button(driver, getElementName(detail));
            WebElementValidationUtil.elementDisplayedValidation(driver, we, 10);
            Assert.assertTrue(String.format("The button %s is disabled!", we), we.isEnabled());
        }
    }

    @Then("^the specific button is disabled")
    public void specificButtonDisabled(List<String> details) {
        Boolean failed = false;
        for (String detail : details) {
            WaitUtil.sleep(2000);
            WebElement we = GeneralPage.button(driver, getElementName(detail));
            WebElementValidationUtil.elementDisplayedValidation(driver, we, 10);
            Assert.assertTrue(String.format("The button %s is still enabled!", we), !we.isEnabled());
        }
    }

    @When("^the user clicks on a specific tab")
    public void clickOnSpecificTab(List<String> details) {
        String tabName = getElementName(details.get(0));
        if (tabName.contains("Pipeline") || tabName.contains("Manual")) {
            String action = "";
            for (Pipelines pl : Pipelines.values()) {
                if (pl.toString().equalsIgnoreCase(tabName)) {
                    action = pl.getValue();
                    break;
                }
            }
            WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.headerMenuTab(driver, action), action,
                    2);
            GeneralPage.headerMenuTab(driver, action).click();
//        TODO - sometimes the "Metadata Check" takes too long to stop spinning
//        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
//                DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        } else {
            WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.headerMenuTab(driver, tabName), tabName);
            GeneralPage.headerMenuTab(driver, tabName).click();
//        TODO - sometimes the "Metadata Check" takes too long to stop spinning
//        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
//                DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        }
        LOG.info(String.format("Clicked on '%s'", details.get(0)));
    }

    @Then("^the QA0 Flags view is displayed")
    public void qa0ViewIsDisplayed(List<String> details) {
        String eb = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, getElementName(details.get(0)));
        WebElementValidationUtil.elementDisplayedValidation(driver, QA0FlagsTabPage.textArea(driver, eb));
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
                legendSwatch = StringUtil.rgbToHex(legendLabel, legendSwatch);
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
        WebElementValidationUtil.elementDisplayedValidation(driver, QA2ReportPage.pageAnchor(driver),
                WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @Then("^the release Notes page is displayed$")
    public void releaseNotesPageDisplayed() {
        BrowserUtil.switchWindowByTitle(driver, PageTitle.RELEASE_NOTES.getValue());
        WebElementValidationUtil.elementDisplayedValidation(driver, ReleaseNotesPage.almaReleaseNotes(driver), "Release notes",
                WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
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
            LOG.info("New browser tab not available!");
        }
        BrowserUtil.switchWindowByTitle(driver, details.get(0));
        //        elementDisplayedValidation(driver, GeneralPage.label(driver, details.get(0)),
        //                60);
    }

    @Then("^the QA2 Report html page contains the specific data$")
    public void htmlQA2PageContainsSpecificData(List<String> details) {
        String dataHint = getElementName(details.get(0));
        String data = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, dataHint).replace(".0 expected",
                " expected");
        String htmlQA2Report = QA2ReportPage.pageAnchor(driver).getAttribute("innerHTML");
        Assert.assertTrue(String.format("The %s %s from the HTML QA2 Report doesn't match!",
                dataHint, data), htmlQA2Report.contains(data));
    }

    @When("^the user switches back to the main page$")
    public void switchBackToMainPage(List<String> details) {
        BrowserUtil.switchWindowByTitle(driver, getElementName(details.get(0)));
    }

    @Then("^the specific data is written into the tmp file$")
    public void writeDataToFile(List<String> details) {
        WebElement we = OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver,
                getElementName(details.get(0)));
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        String innerValue = we.getAttribute("innerHTML");
        ReadWriteTextToFileUtil.writeFile(innerValue, "project_code");
    }

    @When("^the user clicks on a specific tree cell$")
    public void clickOnTreeCell(List<String> details) {
        String cn = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, getElementName(details.get(0)));
        WebElement we = GeneralPage.treeCell(driver, cn);
        driver.executeScript("arguments[0].scrollIntoView(true);", we);
        WaitUtil.sleep(2000);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info(String.format("Clicked on '%s'", details.get(0)));
        WaitUtil.sleep(3000);
//        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
//                DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @Then("^the specific data is displayed within the page$")
    public void pageContainsData(DataTable table) {
        List<List<String>> details = table.raw();
        for (List<String> detail : details) {
            String expectedValue = getElementName(detail.get(0));
            String value = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, expectedValue);
            if (StringUtils.isBlank(value)) {
                value = getElementName(detail.get(1));
            }
            WebElement we = GeneralPage
                    .getDynamicLabelContentByTagLabelContainingText(driver,
                            getElementName(detail.get(1)));
            WebElementValidationUtil.elementDisplayedValidation(driver, we, value);
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
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info(String.format("Clicked on '%s'", details.get(0)));
        WaitUtil.sleep(500);
    }

    @When("^the user selects a specific combobox item$")
    public void selectComboBoxItem(List<String> details) {
        String elementName = "";
        if (StringUtils.isNotBlank(details.get(0))) {
            elementName = getElementName(details.get(0));
        }
        if (details.get(0).equalsIgnoreCase("user_search")) {
            //  elementName = userSearchName;
            elementName = getElementName(details.get(0)) + "_" + StringUtil.currentDate(System.currentTimeMillis());
        }
        WebElement we = GeneralPage.selectComboBoxItem(driver, elementName);
        WebElementValidationUtil.elementDisplayedValidation(driver, we, elementName);
        we.click();
        LOG.info(String.format("Clicked on '%s'", elementName));
    }

    @Then("^the EF values are reduced according to the formula$")
    public void calculateEFBasedOnFormula(List<String> formula) throws Throwable {
        List<String> variables = StringUtil.split(formula.get(1));
        String newFormula = "";
        for (int i = 0; i < variables.size(); i++) {
            if (i == 0) {
                RuntimeBufferUtil.buffer = formula.get(0).replace(variables.get(i), StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap,
                        variables.get(i)));
            } else {
                newFormula = RuntimeBufferUtil.buffer.replace(variables.get(i), StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap,
                        variables.get(i)));
                RuntimeBufferUtil.buffer = newFormula;
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
        WaitUtil.sleep(5000);
        WebElement we = GeneralPage.reloadButton(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we, "Refresh");
        we.click();
        LOG.info("Clicked on 'Refresh' state button");
        WaitUtil.waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver),
                WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
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

    @Then("^the specific data was changed$")
    public void elementUpdated(DataTable table) {
        List<List<String>> data = table.raw();
        String tagLabelName = getElementName(data.get(0).get(0));
        String newState = getElementName(data.get(0).get(1));
        WaitUtil.sleep(5000);
        WebElement we = GeneralPage
                .getDynamicLabelContentByTagLabelExactText(driver, tagLabelName);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
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
            WebElementValidationUtil.elementDisplayedValidation(driver, we, elementName);
            driver.executeScript("arguments[0].scrollIntoView(true);", we);
            String range = GeneralPage.expectedValueFromContainer(driver,
                    detail.get(0), elementName).replace(" [arcsec]", "")
                    .replace(" ", "");

            StringUtil.checkIfStringMatchesPattern(range, detail.get(2));
        }
    }

    @Then("^a specific element is displayed on specific page area$")
    public void elementDisplayedOnSpecificPageArea(DataTable table) {
        List<List<String>> details = table.raw();
        WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.elementFromSpecificPageArea(driver,
                getElementName(details.get(0).get(1)), getElementName(details.get(0).get(0))));
    }

    @Then("^a specific element is displayed$")
    public void elementDisplayed(DataTable table) {
        List<List<String>> details = table.raw();
        WebElement we = GeneralPage.elementContainingSpecificText(driver,
                getElementName(details.get(0).get(0)), getElementName(details.get(0).get(1)));
        WebElementValidationUtil.elementDisplayedValidation(driver, we, getElementName(details.get(0).get(1)));
        driver.executeScript("arguments[0].scrollIntoView(true);", we);
    }

    @Then("the label is positioned as required$")
    public void elementPosition(DataTable table) {
        List<List<String>> details = table.raw();
        String currentPosition = GeneralPage.twoLabelsPosition(driver,
                getElementName(details.get(0).get(0)), getElementName(details.get(0).get(1)),
                getElementName(details.get(0).get(3)));
        Assert.assertTrue(String.format("The label '%s' should be positioned '%s' the label '%s' "
                        + " but is '%s'!", getElementName(details.get(0).get(1)),
                details.get(0).get(2), getElementName(details.get(0).get(3)),
                currentPosition), Objects.equals(details.get(0).get(2), currentPosition));
    }

    @Then("^the specific email notification is available$")
    public void emailNotificationSent(DataTable table) {
        List<List<String>> details = table.raw();
        String prefix = details.get(0).get(0);
        String pc = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, details.get(1).get(0));
        String sb = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, details.get(2).get(0));
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
        Assert.assertTrue("The email notification was not sent!",
                emailSubject.contains(pc) && emailSubject.contains(sb));
    }

    @Then("^the specific cancellation email notification was sent$")
    public void cancellationEmailNotificationSent(DataTable table) {
        List<List<String>> details = table.raw();
        String dr = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, details.get(0).get(0));
        String suffix = details.get(3).get(0);
        if (dr.contains("(")) {
            dr = StringUtil.extractSubstringUntilIndexOf(dr, "(");
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
        String pc = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, details.get(0).get(0));
        String sb = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, details.get(1).get(0));
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
        String dr = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, details.get(0).get(0));
        String suffix = details.get(3).get(0);
        if (dr.contains("(")) {
            dr = StringUtil.extractSubstringUntilIndexOf(dr, "(");
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
        String pc = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, details.get(0).get(0));
        String sb = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, details.get(1).get(0));
        MailHogPage.email(driver, pc).click();
        LOG.info(String.format("Clicked on '%s'", pc));
    }

    @Then("^the previously set comment is displayed within the comment textbox$")
    public void commentBodyAvailable(DataTable table) {
        List<List<String>> details = table.raw();
        String comment = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, details.get(0).get(1));
        String currentComment = OBSUnitSetSummaryPage
                .doQA2FormComment(driver, details.get(0).get(0)).getText();
        Assert.assertTrue("The comment within the text area is different by the one set "
                + "initially!", Objects.equals(comment, currentComment));
    }

    @Then("^the email body contains the specific comment text$")
    public void emailBodyContainsComment(List<String> details) {
        String comment = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, details.get(0));
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
            String dr = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, text);
            if (dr.contains("(")) {
                dr = StringUtil.extractSubstringUntilIndexOf(dr, "(");
            }
            Assert.assertTrue(String.format("The email body doesn't contain the '%s' '%s'!",
                    text, dr), emailBody.contains(dr));
        }
    }

    @Then("^collect the status of the Awaiting decision by DRM label$")
    public void qa2DecisionLabelStatus() {
        try {
            GeneralPage.specificLabel(driver,
                    getElementName("awaiting_decision_label")).isDisplayed();
            RuntimeBufferUtil.awaitingDecision = true;
        } catch (Exception e) {
            RuntimeBufferUtil.awaitingDecision = false;
        }
    }

    @Then("^the user edits the comment$")
    public void editComment(List<String> details) {
        String comment = StringUtil.extractMapValue(RuntimeBufferUtil.bufferMap, details.get(0));
        WebElement we = OBSUnitSetSummaryPage.editComment(driver, comment);
        WebElementValidationUtil.elementDisplayedValidation(driver, we, comment);
        we.click();
        LOG.info(String.format("Clicked on 'Edit comment' for comment '%s'", comment));
        //  GeneralPage.globalTextBox(driver).clear();
        WaitUtil.sleep(2000);
        String commentBody = details.get(0) + " " + System.currentTimeMillis();
        GeneralPage.globalTextBox(driver).sendKeys(commentBody);
        RuntimeBufferUtil.bufferMap.put("updated comment", commentBody);
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
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on an available EB UIDs");
        WaitUtil.sleep(5000);
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
        WebElementValidationUtil.elementDisplayedValidation(driver, we, getElementName(details.get(0)));
    }

    @Then("^the specific list item is not available$")
    public void listItemNotAvailable(List<String> details) {
        String elementName = getElementName(details.get(0)) + "_" + StringUtil.currentDate(System.currentTimeMillis());
        Assert.assertFalse(String.format("The user favourite search '%s' was not deleted!",
                elementName), GeneralPage.comboBoxItemNotAvailable(driver, elementName));
    }

    @Then("^the antenna positions plot is available$")
    public void antennaPositionsPlotAvailable() throws Throwable {
        WebElementValidationUtil.elementDisplayedValidation(driver, BaselineCoveragePage.antennaPositionsPlot(driver),
                "'Antenna Positions'");
    }

    @Then("^the pointing offsets plot is available$")
    public void pointingOffsetsPlotAvailable() throws Throwable {
        WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.canvasPlot(driver), "'Pointing Offsets'");
    }

    @Then("^the mosaic coverage plot is available$")
    public void mosaicCoveragePlotAvailable() throws Throwable {
        WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.canvasPlot(driver), "'Pointing Offsets'");
    }

    @Then("^the phase RMS coverage plot is available$")
    public void phaseRMSCoveragePlotAvailable() throws Throwable {
        WebElementValidationUtil.elementDisplayedValidation(driver, GeneralPage.canvasPlot(driver), "'Pointing Offsets'");
    }

    @Then("^scroll to specific label$")
    public void scrollToSpecificLabel(List<String> details) throws Throwable {
        String labelName = getElementName(details.get(0));
        WebElement we = GeneralPage.label(driver, labelName);
        driver.executeScript("arguments[0].scrollIntoView(true);", we);
    }
}
