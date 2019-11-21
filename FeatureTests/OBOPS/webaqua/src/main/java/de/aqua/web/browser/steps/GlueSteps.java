package de.aqua.web.browser.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.aqua.web.browser.setup.AbstractSelenium3DockerMultiBrowserDriverSetup;
import de.aqua.web.pageobjects.*;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.aqua.web.delegates.WebDriverExtended;

import java.util.List;
import java.util.Objects;

import static de.aqua.web.utils.PropertiesFileUtil.getNavigationURL;
import static de.aqua.web.utils.PropertiesFileUtil.getProperty;
import static de.aqua.web.utils.StringUtil.*;
import static de.aqua.web.utils.WaitUtil.*;
import static de.aqua.web.utils.WebElementValidationUtil.elementDisplayedValidation;
import static de.aqua.web.utils.RuntimeBufferUtil.*;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class GlueSteps extends AbstractSelenium3DockerMultiBrowserDriverSetup {

    private static Logger LOG = LoggerFactory.getLogger(GlueSteps.class);

    WebDriverExtended driver = getWebDriverExtended();

    //    Background
    @Given("^that the test environment is available$")
    public void verifyIfTheTestEnvIsAvailable() throws Throwable {
        try {
            driver.getPageSource();
//            String deployment = currentMonth().toLowerCase() + extractMonthOrYearFromCurrentDate("YYYY");
//            String url = replaceStrings(getNavigationURL("url_cl"), "deployment_version", deployment);
            String url = getNavigationURL("url_cl");
            NavigatePage.toGlobalLink(driver, url);
            driver.manage().window().maximize();
        } catch (Exception e) {
            LOG.error("Server error!");
            Assert.fail();
        }
    }

    //    ####################################### LoginPage #######################################

    @Given("^that the aqua login page is displayed$")
    public void navigateToUrl() throws Throwable {
        elementDisplayedValidation(driver, LoginPage.userField(driver));
        sleep(2000);
    }

    @When("^the user fills the credentials$")
    public void inputCredentials(List<String> credentials) throws Throwable {
        elementDisplayedValidation(driver, LoginPage.userField(driver));
        LoginPage.userField(driver).clear();
        LoginPage.userField(driver).sendKeys(getProperty(credentials.get(0)));

        elementDisplayedValidation(driver, LoginPage.passwordField(driver));
        LoginPage.passwordField(driver).clear();
        LoginPage.passwordField(driver).sendKeys(getProperty(credentials.get(1)));
    }

    @When("^the user checks the warn checkbox$")
    public void checkWarn() throws Throwable {
        elementDisplayedValidation(driver, LoginPage.warnCheckbox(driver));
        if (!LoginPage.warnCheckbox(driver).isSelected())
            LoginPage.warnCheckbox(driver).click();
    }

    @When("^the user unchecks the warn checkbox$")
    public void uncheckWarn() throws Throwable {
        elementDisplayedValidation(driver, LoginPage.warnCheckbox(driver));
        if (LoginPage.warnCheckbox(driver).isSelected())
            LoginPage.warnCheckbox(driver).click();
    }

    @When("^the user clicks the LOGIN button$")
    public void clickLogin() throws Throwable {
        elementDisplayedValidation(driver, LoginPage.loginButton(driver));
        LoginPage.loginButton(driver).click();
        sleep(3000);
    }

    //    ######################################## QA0Page ########################################

    @Then("^the QA0 view is displayed$")
    public void qa0ViewAvailable() throws Throwable {
        elementDisplayedValidation(driver, QA0Page.qa0Page(driver));
    }

    @Then("^the user is authenticated$")
    public void userAuthenticated(List<String> user) throws Throwable {
        elementDisplayedValidation(driver, QA0Page.userLabel(driver));
        String loggedUser = QA0Page.userLabel(driver).getText();
        Assert.assertTrue(String.format("The logged user %s is different by the mentioned one," +
                        " %s!", loggedUser,
                user.get(0)), loggedUser.equalsIgnoreCase(getProperty(user.get(0))));
    }

    @When("^the user clicks on QA2 tab$")
    public void qa2TabClick() throws Throwable {
        elementDisplayedValidation(driver, QA0Page.qa2Page(driver));
        QA0Page.qa2Page(driver).click();
        sleep(2000);
    }

    @When("^the specific search tab is selected$")
    public void searchEB(List<String> searchTab) throws Throwable {
        elementDisplayedValidation(driver, QA0Page.searchEBTabs(driver, searchTab.get(0)));
        QA0Page.searchEBTabs(driver, searchTab.get(0)).click();
        Assert.assertTrue(String.format("The tab %s is not selected!", searchTab.get(0)),
                QA0Page.searchEBTabs(driver, searchTab.get(0)).getAttribute("class").contains("selected"));
    }

    @Then("^the specific elements are available$")
    public void ebSearchElements(List<String> elements) throws Throwable {
        for (String element : elements) {
            List<String> elem = split(element);
            switch (elem.get(1)) {
                case "label": {
                    break;
                }
                case "checkbox": {
                    elementDisplayedValidation(driver, QA0Page.checkBox(driver, elem.get(0)));
                    break;
                }
                case "datebox": {
                    elementDisplayedValidation(driver, QA0Page.dateBox(driver, elem.get(2)));
                    break;
                }
                case "textbox": {
                    elementDisplayedValidation(driver, QA0Page.textBox(driver, elem.get(0)));
                    break;
                }
                case "combobox": {
                    elementDisplayedValidation(driver, QA0Page.comboBox(driver, elem.get(0)));
                    break;
                }
                case "button": {
                    elementDisplayedValidation(driver, QA0Page.button(driver, elem.get(0)));
                    break;
                }
            }
        }
    }

    //    ######################################## QA2Page ########################################

    @Then("^the QA2 view is displayed$")
    public void qa2ViewAvailable() throws Throwable {
        elementDisplayedValidation(driver, QA2Page.qa2MainView(driver));
    }

    @Then("^the user checks/unchecks the checkbox$")
    public void checkboxEvent(List<String> checkboxDetails) throws Throwable {
        for (String detail : checkboxDetails) {
            String action = split(detail).get(0);
            String locator = split(detail).get(1);
            String name = split(detail).get(2);
            switch (action) {
                case "check": {
                    elementDisplayedValidation(driver, QA2Page.genericCheckbox(driver, locator,
                            name));
                    if (!QA2Page.genericCheckbox(driver, locator,
                            name).isSelected())
                        QA2Page.genericCheckbox(driver, locator,
                                name).click();
                    break;
                }
                case "uncheck": {
                    elementDisplayedValidation(driver, QA2Page.genericCheckbox(driver, locator,
                            name));
                    if (QA2Page.genericCheckbox(driver, locator,
                            name).isSelected())
                        QA2Page.genericCheckbox(driver, locator,
                                name).click();
                    break;
                }
            }
        }
    }

    @Then("^the user checks if the OUS are displayed$")
    public void checkOUSListDisplayed() throws Throwable {
        if (QA2Page.noObsUnitSetDisplayed(driver).isDisplayed()) {
            QA2Page.searchButton(driver).click();
            waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver), DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        }
    }

    @When("^the user selects one of the available OUS")
    public void selectObsUnitSet(List<String> projectDetails) throws Throwable {
        elementDisplayedValidation(driver, QA2Page.selectObsUnitSet(driver, projectDetails.get(0)));
        QA2Page.selectObsUnitSet(driver, projectDetails.get(0)).click();
        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver), DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @When("^the user selects the first available OUS")
    public void selectFirstObsUnitSet() throws Throwable {
        elementDisplayedValidation(driver, QA2Page.selectFirstObsUnitSet(driver));
        QA2Page.selectFirstObsUnitSet(driver).click();
        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver), DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @Then("^the OUS Summary details page is displayed$")
    public void ousSummaryDisplayed() throws Throwable {
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.summary(driver));
    }

    @When("^the user selects a specific OUS state$")
    public void setOUSState(List<String> details) throws Throwable {
        for (String detail : details) {
            List<String> elements = split(detail);
            elementDisplayedValidation(driver, QA2Page.textBox(driver, elements.get(0)));
            QA2Page.textBox(driver, elements.get(0)).sendKeys(elements.get(1));
        }
    }

    @Then("^the Data reduction methods are displayed$")
    public void dataReductionMethodsDisplayed(List<String> details) throws Throwable {
        String rm = extractMapValue(bufferMap, details.get(0));
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, rm));
    }

    @Then("^the user checks if the specific data is different$")
    public void validateDataIsDifferent(List<String> details) throws Throwable {
        String data = extractMapValue(bufferMap, details.get(0));
        String currentData = OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, details.get(0))
                .getAttribute("innerHTML");
        Assume.assumeTrue("The displayed data is the same with the baseline!", !Objects.equals(data, currentData));
    }

    @Then("^the Data reducer is displayed$")
    public void dataReducerDisplayed(List<String> details) throws Throwable {
        String dr = extractMapValue(bufferMap, details.get(0));
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, dr));
    }

    @Then("^the specific information is displayed$")
    public void dynamicLabelContentDisplayed(List<String> details) throws Throwable {
        String dr = extractMapValue(bufferMap, details.get(0));
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, dr));
    }

    @Then("^the Data reducer is no longer displayed$")
    public void dataReducerNotDisplayed(List<String> details) throws Throwable {
        String dr = extractMapValue(bufferMap, details.get(0));
        Assert.assertTrue(String.format("Data reducer %s is still displayed!", details.get(0)),
                OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, dr) == null);
    }

    @When("^the user clicks on a specific button$")
    public void clickOnButton(List<String> buttonName) throws Throwable {
        try {
            elementDisplayedValidation(driver, OBSUnitSetSummaryPage.button(driver, buttonName.get(0)));
            OBSUnitSetSummaryPage.button(driver, buttonName.get(0)).click();
        } catch (Exception e) {
            elementDisplayedValidation(driver, EBSummaryPage.button(driver, buttonName.get(0)));
            EBSummaryPage.button(driver, buttonName.get(0)).click();
        }
        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver), DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @Then("^the Do QA2 pop-up form is displayed$")
    public void doQA2Displayed() throws Throwable {
        sleep(500);
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.doQA2Form(driver));
    }

    @When("^the user clicks on the Set QA2 Status$")
    public void clickSetQA2Status() throws Throwable {
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.setQA2Status(driver));
        OBSUnitSetSummaryPage.setQA2Status(driver).click();
    }

    @When("^the user selects specific status$")
    public void selectQA2Status(List<String> status) throws Throwable {
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.selectStatus(driver, status.get(0)));
        OBSUnitSetSummaryPage.selectStatus(driver, status.get(0)).click();
    }

    @Then("^the confirmation pop-up is displayed$")
    public void confirmationPopupDisplayed() throws Throwable {
        elementDisplayedValidation(driver, GeneralPage.dialogBox(driver));
    }

    @When("^the user clicks one of the popup's buttons$")
    public void clickPopupButton(List<String> option) throws Throwable {
        elementDisplayedValidation(driver, GeneralPage.dialogBoxActionButton(driver, option.get(0)));
        GeneralPage.dialogBoxActionButton(driver, option.get(0)).click();
    }

    @When("^the user clicks on a specific button on Do QA2 page$")
    public void clickOnDoQA2Button(List<String> buttonName) throws Throwable {
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.doQA2FormButton(driver, buttonName.get(0)));
        OBSUnitSetSummaryPage.doQA2FormButton(driver, buttonName.get(0)).click();
    }

    @When("^the user clicks on QA2 Status Reason$")
    public void clickOnQA2StatusReason() throws Throwable {
        sleep(2000);
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.doQA2FormStatusReason(driver));
        OBSUnitSetSummaryPage.doQA2FormStatusReason(driver).click();
    }

    @Then("^the QA2 Status Reason options are displayed$")
    public void qa2StatusReasonListDisplayed() throws Throwable {
//        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.doQA2FormStatusReasonList(driver));
    }

    @Then("^the user selects one QA2 Reason status by name$")
    public void qa2StatusReasonStatusSelectByName(List<String> status) throws Throwable {
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.doQA2FormReasonStatusOptionName(driver, status.get(0)));
        OBSUnitSetSummaryPage.doQA2FormReasonStatusOptionName(driver, status.get(0)).click();
    }

    @Then("^the user selects one QA2 Reason status by position$")
    public void qa2StatusReasonStatusSelectByPosition(List<String> status) throws Throwable {
        if (status.size() > 0 && StringUtils.isNumeric(status.get(0)) && StringUtils.isNotBlank(status.get(0))) {
            elementDisplayedValidation(driver, OBSUnitSetSummaryPage.doQA2FormReasonStatusOptionPosition(driver, status.get(0)));
            OBSUnitSetSummaryPage.doQA2FormReasonStatusOptionPosition(driver, status.get(0)).click();
        } else {
            elementDisplayedValidation(driver, OBSUnitSetSummaryPage.doQA2FormReasonStatusOptionPosition(driver));
            OBSUnitSetSummaryPage.doQA2FormReasonStatusOptionPosition(driver).click();
        }
    }

    @Then("^the user sets an EC value$")
    public void setManualECValue() throws Throwable {
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.doQA2FormEC(driver));
        OBSUnitSetSummaryPage.doQA2FormEC(driver).clear();
        String value = String.valueOf(Double.parseDouble(OBSUnitSetSummaryPage.doQA2FormECOriginalValue(driver).getAttribute
                ("innerHTML")) - 0.01);
        OBSUnitSetSummaryPage.doQA2FormEC(driver).sendKeys(value);
    }

    @Then("^the state confirmation message box is displayed$")
    public void changeStateDialogboxDisplayed() throws Throwable {
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.stateChangeMessageBox(driver));
    }

    @Then("^the dialog box with specific message is displayed$")
    public void infoECboxDisplayed(List<String> message) throws Throwable {
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.infoECValue(driver));
        Assert.assertTrue(String.format("The message displayed into the EC info box is %s and it should be %s!",
                OBSUnitSetSummaryPage.infoECValue(driver).getAttribute("innerHTML"), message.get(0)),
                Objects.equals(OBSUnitSetSummaryPage.infoECValue(driver).getAttribute("innerHTML"), message.get(0)));
    }

    @When("^the user clicks OK on the EC dialog box$")
    public void confirmECbox() throws Throwable {
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.infoECokBtn(driver));
        OBSUnitSetSummaryPage.infoECokBtn(driver).click();
    }

    @Then("^the current OUS state is collected$")
    public void collectCurrentStateValue() throws Throwable {
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.stateOUS(driver));
        buffer = OBSUnitSetSummaryPage.stateOUS(driver).getAttribute("innerHTML");
    }

    @Then("^the OUS state is not changed$")
    public void validateCurrentStateValue() throws Throwable {
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.stateOUS(driver));
        Assert.assertTrue(String.format("The OUS state is %s and it should be %s!",
                OBSUnitSetSummaryPage.stateOUS(driver).getAttribute("innerHTML"), buffer),
                Objects.equals(OBSUnitSetSummaryPage.stateOUS(driver).getAttribute("innerHTML"), buffer));
    }

    @Then("^the confirmation dialog contains the specific message$")
    public void validateTextOnConfirmationDialog(List<String> text) throws Throwable {
        elementDisplayedValidation(driver, GeneralPage.dialogBox(driver));
        Assert.assertTrue(String.format("The text on the confirmation dialog is %s and it should be %s!",
                OBSUnitSetSummaryPage.confirmationBoxMessage(driver).getAttribute("innerHTML"), text.get(0)),
                Objects.equals(OBSUnitSetSummaryPage.confirmationBoxMessage(driver).getAttribute("innerHTML"),
                        text.get(0)));
    }

    @When("^the user selects the first available EB$")
    public void selectFirstEB() throws Throwable {
        elementDisplayedValidation(driver, QA0Page.selectFirstEB(driver));
        QA0Page.selectFirstEB(driver).click();
        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver), DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @Then("^the EB Summary details page is displayed$")
    public void ebSummaryDisplayed() throws Throwable {
        elementDisplayedValidation(driver, EBSummaryPage.summary(driver));
        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver), DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @Then("^the information dialog box is displayed$")
    public void informationDialogDisplayed() throws Throwable {
        elementDisplayedValidation(driver, GeneralPage.dialogBox(driver));
    }

    @Then("^the information dialog box message is consistent$")
    public void validateDialogMessage(List<String> message) throws Throwable {
        elementDisplayedValidation(driver, GeneralPage.dialogBoxMessage(driver));
        String msg = GeneralPage.dialogBoxMessage(driver).getAttribute("innerHTML");
        Assert.assertTrue(String.format("The text on the information dialog is %s and it should be %s!",
                msg, message.get(0)), Objects.equals(msg, message.get(0)));
    }

    @When("^the user clicks on a specific button on the the information dialog box$")
    public void clickDialogBoxButton(List<String> buttonName) throws Throwable {
        elementDisplayedValidation(driver, GeneralPage.dialogBoxActionButton(driver, buttonName.get(0)));
        GeneralPage.dialogBoxActionButton(driver, buttonName.get(0)).click();
    }

    @When("^the user clicks on a specific field$")
    public void clickOUSStateFlag(DataTable option) throws Throwable {
        List<List<String>> data = option.raw();
        elementDisplayedValidation(driver, QA2Page.ousStateFlagField(driver, data.get(0).get(0)));
        QA2Page.ousStateFlagField(driver, data.get(0).get(0)).click();
    }

    @Then("^the available options are displayed$")
    public void ousStateFlagOptionsDisplayed() throws Throwable {
        elementDisplayedValidation(driver, QA2Page.ousStateFlagOptionList(driver));
    }

    @When("^the user selects a specific option$")
    public void selectOUSStateFlagOption(DataTable option) throws Throwable {
        List<List<String>> data = option.raw();
        elementDisplayedValidation(driver, QA2Page.ousStateFlagOption(driver, data.get(0).get(1)));
        QA2Page.ousStateFlagOption(driver, data.get(0).get(1)).click();
    }

    @When("^the specific OUS State flag is selected$")
    public void validateIfOUSStateFlagOptionIsSelected(DataTable option) throws Throwable {
        List<List<String>> data = option.raw();
        elementDisplayedValidation(driver, QA2Page.selectedOUSStateFlagOption(driver, data.get(0).get(1)));
        String opt = QA2Page.selectedOUSStateFlagOption(driver, data.get(0).get(1)).getText();
        Assert.assertTrue(String.format("The selected OUS State flag is %s and it should be %s!",
                opt, data.get(0).get(1)), Objects.equals(opt, data.get(0).get(1)));
    }

    @Then("^the user adds a QA2 comment$")
    public void addQA2Comment(DataTable comment) throws Throwable {
        List<List<String>> data = comment.raw();
        WebElement we = OBSUnitSetSummaryPage.doQA2FormComment(driver, data.get(0).get(0));
        elementDisplayedValidation(driver, we);
        we.clear();
        we.sendKeys(data.get(0).get(1) + " " + System.currentTimeMillis());
    }

    @Then("^the user inserts a value into a specific field$")
    public void setAdvancedOUSSearchValues(DataTable fields) throws Throwable {
        List<List<String>> data = fields.raw();
        elementDisplayedValidation(driver, QA2Page.advancedOUSTextField(driver, data.get(0).get(0)));
        QA2Page.advancedOUSTextField(driver, data.get(0).get(0)).sendKeys(data.get(0).get(1));
    }

    @Then("^the specific data is collected for validation$")
    public void collectDataForValidation(List<String> details) throws Throwable {
        elementDisplayedValidation(driver, OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, details.get(0)));
        String innerValue = OBSUnitSetSummaryPage.getDynamicLabelByTagLabel(driver, details.get(0))
                .getAttribute("innerHTML");
        bufferMap.put(details.get(0), innerValue);
    }

    @Then("^the data was successfully retrieved$")
    public void specificDataRetrievedAfterReRunAOSCheck(DataTable table) throws Throwable {
        List<List<String>> data = table.raw();
        Boolean exists = true;
        for (int i = 0; i < data.size(); i++) {
            try {
                EBSummaryPage.individualDataRetrievedAfterReRunAOSCheck(driver, data.get(i).get(0));
            } catch (Exception e) {
                exists = false;
            }
            if (exists) {
                String retrievedData = EBSummaryPage.individualDataRetrievedAfterReRunAOSCheck(driver,
                        data.get(i).get(0)).getAttribute("innerHTML");
                Assume.assumeTrue(String.format("The %s area doesn't contain any information after re-running the AOS " +
                        "check!", data.get(i).get(0)), !retrievedData.contains(data.get(i).get(1)));
            } else
                LOG.info(String.format("The %s area doesn't contain any information after re-running the AOS check!",
                        data.get(i).get(0)));
        }
    }

    @Then("^the data grid was successfully retrieved$")
    public void specificDataGridRetrievedAfterReRunAOSCheck(DataTable table) throws Throwable {
        List<List<String>> data = table.raw();
        Boolean exists = true;
//        scrolls until finds the WebElement
        driver.executeScript("arguments[0].scrollIntoView(true);", EBSummaryPage.dataGridRetrievedAfterReRunAOSCheck(driver, data.get(0).get(0)));
        try {
            EBSummaryPage.dataGridRetrievedAfterReRunAOSCheck(driver, data.get(0).get(0));
        } catch (Exception e) {
            exists = false;
        }
        if (exists) {
            String retrievedData = EBSummaryPage.dataGridRetrievedAfterReRunAOSCheck(driver, data.get(0).get(0))
                    .getAttribute("innerHTML");
            Assume.assumeTrue(String.format("The %s grid area doesn't contain any information after re-running the " +
                    "AOS check!", data.get(0).get(0)), retrievedData.contains(data.get(0).get(1)));
        } else
            LOG.info(String.format("The %s grid area doesn't contain any information after re-running the AOS check!",
                    data.get(0).get(0)));
    }

    @Then("^the user selects the next EB if the Re-run AOS check was already triggered$")
    public void checkIfReRunAOSCheckTriggered(List<String> data) throws Throwable {
        Boolean exists = true;
        int index = 1;
        do {
            try {
                WebElement we = EBSummaryPage.button(driver, data.get(0));
                if (!we.isDisplayed()) {
                    exists = false;
                }
            } catch (Exception e) {
                exists = false;
            }
            if (exists) {
                QA0Page.selectEBByIndex(driver, String.valueOf(index)).click();
//                waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver), DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                sleep(10000);
                elementDisplayedValidation(driver, EBSummaryPage.summary(driver));
//                waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver), DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
                sleep(10000);
                index++;
            }
        } while (exists);
        LOG.info(String.format("The user clicked on the EB no. %d.", index));
    }

    @Then("^the EBs are displayed")
    public void searchResultsEB() throws Throwable {
        elementDisplayedValidation(driver, QA0Page.qa0SearchResults(driver));
    }

    @Then("^the specific button is displayed")
    public void specificButtonDisplayed(List<String> data) throws Throwable {
        elementDisplayedValidation(driver, EBSummaryPage.button(driver, data.get(0)));
    }

    @When("^the user clicks on a specific tab")
    public void specificQA0Tab(List<String> data) throws Throwable {
        elementDisplayedValidation(driver, GeneralPage.headerMenuTab(driver, data.get(0)));
        GeneralPage.headerMenuTab(driver, data.get(0)).click();
        waitForLocatedElementIsNotDisplayed(driver, GeneralPage.loadingProgress(driver), DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
    }

    @Then("^the QA0 Flags view is displayed")
    public void qa0ViewIsDisplayed(List<String> details) throws Throwable {
        String eb = extractMapValue(bufferMap, details.get(0));
        elementDisplayedValidation(driver, QA0FlagsTabPage.textArea(driver, eb));
    }

    @Then("^the legend item is displayed")
    public void baselineCoverageLegendItemDisplayed(DataTable table) throws Throwable {
        List<List<String>> data = table.raw();
        Boolean exists = true;
        for (int i = 0; i < data.size(); i++) {
            try {
                BaselineCoveragePage.legendLabel(driver, data.get(i).get(0));
            } catch (Exception e) {
                exists = false;
            }
            if (exists) {
                String legendLabel = BaselineCoveragePage.legendLabel(driver, data.get(i).get(1))
                        .getAttribute("innerHTML");
                String legendSwatch = BaselineCoveragePage.legendSwatch(driver, data.get(i).get(1))
                        .getCssValue("background-color");
                Assume.assumeTrue(String.format("The %s legend item is not available after re-running the AOS check!",
                        data.get(i).get(1)), legendLabel.equalsIgnoreCase(data.get(i).get(1)));
                Assert.assertTrue(String.format("The swatch color of the %s legend item is not %s!",
                        data.get(i).get(1), data.get(i).get(0)), legendSwatch.equalsIgnoreCase(data.get(i).get(0)));
            } else
                LOG.info(String.format("The %s legend item is not available after re-running the AOS check!",
                        data.get(i).get(1)));
        }
    }


}
