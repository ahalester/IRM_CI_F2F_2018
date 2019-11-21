package de.obopstest.web.browser.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.obopstest.common.SessionStateHandler;
import de.obopstest.web.browser.setup.AbstractSelenium3DockerMultiBrowserDriverSetup;
import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.pageobjects.dra.*;
import de.obopstest.web.utils.*;
import de.obopstest.web.utils.enums.EnvironmentURL;
import org.junit.Assert;
import org.junit.Assume;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.obopstest.web.utils.PropertiesFileUtil.getProperty;
import static de.obopstest.web.utils.WebElementValidationUtil.elementDisplayedValidation;
import static de.obopstest.web.utils.enums.EnvironmentURL.*;

public class DraSteps extends AbstractSelenium3DockerMultiBrowserDriverSetup {

    private static Logger LOG = LoggerFactory.getLogger(DraSteps.class);
    private List<String> auth = new ArrayList<>();
    private WebDriverExtended driver = getWebDriverExtended();


    public static String getElementName(String propName) {
        if (propName.equalsIgnoreCase("username")) {
            return getProperty(propName);
        } else {
            return getProperty("elements", propName);
        }
    }

    @Given("^the DRA test environment is available$")
    public void verifyIfDraTestEnvIsAvailable() {
        String url = "";
        String envPhase = System.getProperty("envPhase");
        String testEnv = System.getProperty("testEnv").toUpperCase();


        try {
            if (envPhase.equalsIgnoreCase("PHAB")) {
                url = PropertiesFileUtil.getURL(EnvironmentURL.PHB_DRA_URL, testEnv);
            } else if (envPhase.equalsIgnoreCase("PHAA")) {
                url = PropertiesFileUtil.getURL(EnvironmentURL.PHA_DRA_URL, testEnv);
            } else if (envPhase.equalsIgnoreCase("PHAD")) {
                url = PropertiesFileUtil.getURL(EnvironmentURL.DEV_DRA_URL, testEnv);
            }
            LOG.info("DRM URL: " + url);
            NavigatePage.toGlobalLink(driver, url);
            //ToDo page object for DRM page
            WebElement we = LoginPage.userField(driver);
            elementDisplayedValidation(driver, we);
        }catch (Exception e) {
            LOG.error(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
            Assert.fail(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
        }

    }

    @Given("^go to DRA test environment$")
    public void gotoDraTestEnv() {
        String url = "";
        String envPhase = System.getProperty("envPhase");
        String testEnv = System.getProperty("testEnv").toUpperCase();


        try {
            if (envPhase.equalsIgnoreCase("PHAB")) {
                url = PropertiesFileUtil.getNavigationURLdra(PHB_DRA_URL, System.getProperty("testEnv"));
            } else if (envPhase.equalsIgnoreCase("PHAA")) {
                url = PropertiesFileUtil.getNavigationURLdra(PHA_DRA_URL, System.getProperty("testEnv").toUpperCase());
            }
            LOG.info("The URL is: " + url);
            SessionStateHandler.setValue("url_dra",url);
            de.obopstest.web.pageobjects.NavigatePage.toGlobalLink(driver, url);
        }catch (Exception e) {
            LOG.error(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
            Assert.fail(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
        }

    }

//    @Given("^CAS login page is displayed$")
//    public void casLogin() {
//        WebElementValidationUtil.elementDisplayedValidation(driver, LoginPage.userField(driver));
////        WaitUtil.sleep(2000);
//    }

//    @When("^the user fills the credentials$")
//    public void inputCredentials(List<String> credentials) {
//        auth.add(credentials.get(0));
//        auth.add(credentials.get(1));
//        WebElementValidationUtil.elementDisplayedValidation(driver, LoginPage.userField(driver));
//        LoginPage.userField(driver).clear();
//        LoginPage.userField(driver).sendKeys(PropertiesFileUtil.getProperty(credentials.get(0)));
//        LOG.info(String.format("Filled the '%s' field with '%s'", credentials.get(0),
//                PropertiesFileUtil.getProperty(credentials.get(0))));
//
//        WebElementValidationUtil.elementDisplayedValidation(driver, LoginPage.passwordField(driver));
//        LoginPage.passwordField(driver).clear();
//        LoginPage.passwordField(driver)
//                .sendKeys(EncryptUtil.decrypt(PropertiesFileUtil.getProperty(credentials.get(1))));
//        LOG.info(String.format("Filled the '%s' field with '%s' (encrypted)", credentials.get(1),
//                PropertiesFileUtil.getProperty(credentials.get(1))));
//    }

    @When("^the user clicks the LOGIN button DRA$")
    public void clickLoginDra() {
        WebElement we = LoginPage.loginButton(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'Login'");
        WaitUtil.sleep(3000);
    }

//    @Then("^the user is authenticated$")
//    public void userAuthenticated(List<String> user) {
//        String userName;
//        if (user.get(0).equalsIgnoreCase("username")) {
//            userName =  getProperty(user.get(0));
//        } else {
//            userName = user.get(0);
//        }
//        WebElement we = DraSPage.loggedInUserIcon(driver, userName);
//        WebElementValidationUtil.elementDisplayedValidation(driver, we, getElementName(user.get(0)),
//                WaitUtil.DEFAULT_WAIT_FOR_ELEMENT);
//        String loggedUser = we.getText();
//        Assert.assertTrue(String.format("The logged user %s is different by the mentioned one,"
//                        + " %s!", loggedUser, PropertiesFileUtil.getProperty(user.get(0)),
//                loggedUser.contains(PropertiesFileUtil.getProperty(user.get(0))));
//    }

    @Then("^generic user is authenticated$")
    public void genericUserAuthenticated() {
        WebElement we = DraPage.loggedInUserIcon(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we, WaitUtil.DEFAULT_WAIT_FOR_ELEMENT);
    }

    @Then("^the DRA view is displayed$")
    public void draViewPage() {
        WebElement we = DraPage.loggedInUserIcon(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we, WaitUtil.DEFAULT_WAIT_FOR_ELEMENT);
    }

    @When("^validate tab exists$")
    public void validateTabExists(List<String> credentials) {
        String tab = credentials.get(0);
        WebElementValidationUtil.elementDisplayedValidation(driver, DraPage.menuTab(driver, tab));
    }

    @When("^validate qualification status$")
    public void verifyQualifications(List<String> params) {
        String qualification = params.get(0);
        String status = params.get(1);
        Assert.assertEquals("Qualification status is wrong !!!",status,DraAddRemovePage.getQualificationStatus(driver,qualification));
    }

    @When("^go to view$")
    public void goToView(List<String> credentials) {
        String tab = credentials.get(0);
        WebElementValidationUtil.elementDisplayedValidation(driver, DraPage.menuTab(driver, tab));
        DraPage.menuTab(driver, tab).click();
//        WaitUtil.sleep(5000);
    }


    @When("^validate user id in Add/Remove$")
    public void validateUserIdInAddRemove(List<String> params) {
        String uid = null;
        if(params.get(0).equalsIgnoreCase("username")){
            uid = PropertiesFileUtil.getProperty(params.get(0));
        } else {
            uid = params.get(0);
        }
        WaitUtil.sleep(500);
        String actual_uid = DraAddRemovePage.findElemInTableByText(driver,uid).getText();
        Assert.assertEquals("UID is wrong !!!",uid,actual_uid);
    }


    @When("^search account in Add/Remove$")
    public void searchAccountinAddRemove(List<String> params) {
        String uid = null;
        if(params.get(0).equalsIgnoreCase("username")){
            uid = PropertiesFileUtil.getProperty(params.get(0));
        } else {
            uid = params.get(0);
        }
        DraAddRemovePage.accountSearch(driver).sendKeys(uid);
        DraAddRemovePage.accountSearch(driver).sendKeys(Keys.RETURN);
    }

    @When("^select data reducer ARC$")
    public void selectDataReducerArc(List<String> params) {
        String arc = params.get(0);
        DraManagementPage.searchDataReducerArcDropdown(driver,arc).click();
//        WaitUtil.sleep(5000);
    }

    @When("^select qualification in management view$")
    public void selectQualificationMgmt(List<String> params) {
        String qualification = params.get(0).trim();
        DraManagementPage.qualificationCheckbox(driver,qualification).click();
//        WaitUtil.sleep(2000);
    }

    @When("^click Search button - Management$")
    public void searchButtonClickMgmt() {
        DraManagementPage.searchButton(driver).click();
        WaitUtil.sleep(1000);
    }

    @When("^validate user is found - Management$")
    public void userIdInSearchResultMgmt(List<String> params) {
        String user = PropertiesFileUtil.getProperty(params.get(0));
        WebElementValidationUtil.elementDisplayedValidation(driver, DraManagementPage.findSearchResultByText(driver, user));

    }

    @When("^enter id in search box - Management$")
    public void enterUserIdInSearchBoxMgmt(List<String> params) {
        String user = null;
        if(params.get(0).equalsIgnoreCase("username")) {
            user = PropertiesFileUtil.getProperty(params.get(0));
        } else {
            user = params.get(0);
        }
        DraManagementPage.searchNameIdTextbox(driver).sendKeys(user);
//        WaitUtil.sleep(8000);
    }

    @When("^open availability booking view$")
    public void availabilitBookingOpenView() {
        DraBookingPage.addPeriodButton(driver).click();
        WaitUtil.sleep(1000);
    }

    @When("^add availability booking$")
    public void addAvailabilityBookingOption(List<String> option) {
        String availability = option.get(0);
        DraBookingPage.availabilityPeriodDropdownOption(driver, availability).click();
//        WaitUtil.sleep(5000);
    }

    @When("^add start date availability booking$")
    public void addBookingStartDate(List<String> text) {
        String date = text.get(0);
        if(text.get(0).equalsIgnoreCase("tomorrow")){
            DraBookingPage.startDate(driver).clear();
            DraBookingPage.startDate(driver).sendKeys(DateUtil.getCurrentDateplusDays(1));
            DraBookingPage.startDate(driver).sendKeys(Keys.RETURN);
        }else{
            DraBookingPage.startDate(driver).clear();
            DraBookingPage.startDate(driver).sendKeys(date);
            DraBookingPage.startDate(driver).sendKeys(Keys.RETURN);
        }
//        WaitUtil.sleep(5000);
    }

    @When("^add end date availability booking$")
    public void addBookingEndDate(List<String> text) {
        String date = text.get(0);
        if(text.get(0).equalsIgnoreCase("tomorrow")){
            DraBookingPage.endDate(driver).clear();
            DraBookingPage.endDate(driver).sendKeys(DateUtil.getCurrentDateplusDays(1));
            DraBookingPage.endDate(driver).sendKeys(Keys.RETURN);
        }else{
            DraBookingPage.endDate(driver).clear();
            DraBookingPage.endDate(driver).sendKeys(date);
            DraBookingPage.endDate(driver).sendKeys(Keys.RETURN);
        }
//        WaitUtil.sleep(5000);
    }

    @When("^submit availability booking ok$")
    public void submitAvailabilityBookingOk() {
        DraBookingPage.okButton(driver).click();
//        WaitUtil.sleep(3000);
    }

    @When("^select data reducer ARC in availability view$")
    public void selectDataReducerArcInAvailability(List<String> params) {
        String arc = params.get(0).trim();
        DraAvailabilityPage.avSelectArcDropdownOption(driver,arc).click();
//        WaitUtil.sleep(2000);
    }

    @When("^select data reducer Node in availability view$")
    public void selectReducerNodeInAvailability(List<String> params) {
        String node = params.get(0).trim();
        DraAvailabilityPage.avSelectNodeDropdownOption(driver,node).click();
//        WaitUtil.sleep(2000);
    }

    @When("^click availability search button$")
    public void searchAvailabilityButtonClick() {
        DraAvailabilityPage.avSearchButton(driver).click();
//        WaitUtil.sleep(10000);
    }

    //
    @When("^verify availability for$")
    public void verifyAvInAvailability(List<String> params) {
        String name = params.get(0).trim();
        WebElementValidationUtil.
                elementDisplayedValidation(driver,DraAvailabilityPage.findAvailableBookeingFor(driver, name));
//        WaitUtil.sleep(2000);
    }

    //
    @When("^remove booking availability$")
    public void removeBookingAvailabilityInBooking() {
        WaitUtil.sleep(500);
        WebElement we = DraBookingPage.availabilityRemoveIconButton(driver);
        we.click();
        WaitUtil.waitForLocatedElementIsNotDisplayed(driver,we,2);
    }

    @When("^remove data reducer$")
    public void removeDataReducer(List<String> params) {
        String user = null;
        if(params.get(0).equalsIgnoreCase("username")){
            user = PropertiesFileUtil.getProperty(params.get(0));
        } else {
            user = params.get(0);
        }
        //ToDo select specific user if there are multiple search results

        Assume.assumeTrue("User is Not data reducer !!!",DraAddRemovePage.isAddIconDisabled(driver));
        WaitUtil.sleep(500);
        DraAddRemovePage.removeIcon(driver).click();
        WaitUtil.sleep(500);
        Assert.assertTrue("Data reducer status was not updated !!!",DraAddRemovePage.isRemoveIconDisabled(driver));
    }

    @When("^remove data reducer if it exists$")
    public void removeDataReducerIf(List<String> params) {
        String user = null;
        if(params.get(0).equalsIgnoreCase("username")){
            user = PropertiesFileUtil.getProperty(params.get(0));
        } else {
            user = params.get(0);
        }
        //ToDo select specific user if there are multiple search results

        if(DraAddRemovePage.isAddIconDisabled(driver)){
            WaitUtil.sleep(500);
            DraAddRemovePage.removeIcon(driver).click();
            WaitUtil.sleep(500);
            Assert.assertTrue("Data reducer status was not updated !!!",DraAddRemovePage.isRemoveIconDisabled(driver));
        }

    }

    @When("^add default data reducer$")
    public void addDefaultDataReducer(List<String> params) {
        String user = null;
        if(params.get(0).equalsIgnoreCase("username")){
            user = PropertiesFileUtil.getProperty(params.get(0));
        } else {
            user = params.get(0);
        }
        //ToDo select specific user if there are multiple search results
//        boolean addDisabled = DraAddRemovePage.isAddIconDisabled(driver);
//        boolean removeDisabled = DraAddRemovePage.isRemoveIconDisabled(driver);

        Assume.assumeTrue("User is already data reducer !!!",DraAddRemovePage.isRemoveIconDisabled(driver));
        WaitUtil.sleep(500);
        DraAddRemovePage.addIcon(driver).click();
        WaitUtil.sleep(500);

        DataReducerSettingsModalPage.addSelectArcDropdownOption(driver,"EU").click();
        WaitUtil.sleep(200);
        DataReducerSettingsModalPage.addSelectNodeDropdownOption(driver,"Germany").click();
        WaitUtil.sleep(500);

        DataReducerSettingsModalPage.manualCalibrationCheckbox(driver).click();
        DataReducerSettingsModalPage.manualImagingCheckbox(driver).click();
        DataReducerSettingsModalPage.weblogReviewCheckbox(driver).click();
        DataReducerSettingsModalPage.qa2ApprovalCheckbox(driver).click();

        DataReducerSettingsModalPage.addSaveButton(driver).click();

        Assert.assertTrue("Data reducer status was not updated !!!",DraAddRemovePage.isAddIconDisabled(driver));
    }

    @When("^add data reducer$")
    public void addDataReducer(DataTable params) {
        List<List<String>> data = params.raw();
        String user = null;
        if(data.get(1).get(0).equalsIgnoreCase("username")){
            user = PropertiesFileUtil.getProperty(data.get(1).get(0));
        } else {
            user = data.get(1).get(0);
        }
        //ToDo select specific user if there are multiple search results
//        boolean addDisabled = DraAddRemovePage.isAddIconDisabled(driver);
//        boolean removeDisabled = DraAddRemovePage.isRemoveIconDisabled(driver);

        Map<String, String> map = new HashMap<>();
        map.put("uesrname", user);
        map.put("arc", data.get(1).get(1));
        map.put("node", data.get(1).get(2));
        map.put("manual_calibration", data.get(1).get(3));
        map.put("manual_imaging", data.get(1).get(4));
        map.put("weblog_review", data.get(1).get(5));
        map.put("qa2_approval", data.get(1).get(6));


        Assume.assumeTrue("User is already data reducer !!!",DraAddRemovePage.isRemoveIconDisabled(driver));
        WaitUtil.sleep(500);
        DraAddRemovePage.addIcon(driver).click();
        WaitUtil.sleep(500);



        DataReducerSettingsModalPage.addSelectArcDropdownOption(driver,map.get("arc")).click();
        WaitUtil.sleep(200);
        if(!(map.get("node").equalsIgnoreCase("skip"))){
            DataReducerSettingsModalPage.addSelectNodeDropdownOption(driver,map.get("node")).click();
            WaitUtil.sleep(500);
        }

        if(map.get("manual_calibration").equalsIgnoreCase("yes"))
            DataReducerSettingsModalPage.manualCalibrationCheckbox(driver).click();
        if(map.get("manual_imaging").equalsIgnoreCase("yes"))
            DataReducerSettingsModalPage.manualImagingCheckbox(driver).click();
        if(map.get("weblog_review").equalsIgnoreCase("yes"))
            DataReducerSettingsModalPage.weblogReviewCheckbox(driver).click();
        if(map.get("qa2_approval").equalsIgnoreCase("yes"))
            DataReducerSettingsModalPage.qa2ApprovalCheckbox(driver).click();

        DataReducerSettingsModalPage.addSaveButton(driver).click();

        Assert.assertTrue("Data reducer status was not updated !!!",DraAddRemovePage.isAddIconDisabled(driver));
    }

    @When("^modify data reducer$")
    public void modifyDataReducer(DataTable params) {
        List<List<String>> data = params.raw();
        String user = null;
        if(data.get(1).get(0).equalsIgnoreCase("username")){
            user = PropertiesFileUtil.getProperty(data.get(1).get(0));
        } else {
            user = data.get(1).get(0);
        }

        Map<String, String> map = new HashMap<>();
        map.put("uesrname", user);
        map.put("arc", data.get(1).get(1));
        map.put("node", data.get(1).get(2));
        map.put("manual_calibration", data.get(1).get(3));
        map.put("manual_imaging", data.get(1).get(4));
        map.put("weblog_review", data.get(1).get(5));
        map.put("qa2_approval", data.get(1).get(6));

        WaitUtil.sleep(200);
        DataReducerSettingsModalPage.addSelectArcDropdownOption(driver,map.get("arc")).click();
        WaitUtil.sleep(200);
        DataReducerSettingsModalPage.addSelectNodeDropdownOption(driver,map.get("node")).click();
        WaitUtil.sleep(200);

        if(map.get("manual_calibration").equalsIgnoreCase("yes"))
            DataReducerSettingsModalPage.manualCalibrationCheckbox(driver).click();
        if(map.get("manual_imaging").equalsIgnoreCase("yes"))
            DataReducerSettingsModalPage.manualImagingCheckbox(driver).click();
        if(map.get("weblog_review").equalsIgnoreCase("yes"))
            DataReducerSettingsModalPage.weblogReviewCheckbox(driver).click();
        if(map.get("qa2_approval").equalsIgnoreCase("yes"))
            DataReducerSettingsModalPage.qa2ApprovalCheckbox(driver).click();

        DataReducerSettingsModalPage.manSaveButton(driver).click();
    }

    @When("^verify data reducer details table - Management$")
    public void verifyDataReducerMgmt(DataTable params) {
        List<List<String>> data = params.raw();
        String user = null;
        if(data.get(1).get(0).equalsIgnoreCase("username")){
            user = PropertiesFileUtil.getProperty(data.get(1).get(0));
        } else {
            user = data.get(1).get(0);
        }

        Map<String, String> map = new HashMap<>();
        map.put("username", user);
        map.put("arc", data.get(1).get(1));
        map.put("node", data.get(1).get(2));
        map.put("manual_calibration", data.get(1).get(3));
        map.put("manual_imaging", data.get(1).get(4));
        map.put("weblog_review", data.get(1).get(5));
        map.put("qa2_approval", data.get(1).get(6));

//        WaitUtil.sleep(2000);
        WaitUtil.waitForAngularLoad(driver);
        //Actual data from mgmt table
        Map<String, String> actual = DraManagementPage.getDrDetailsInTable(driver,user);

        Assert.assertEquals("username / id ",map.get("username").trim().toLowerCase(), actual.get("id").trim().toLowerCase());
        Assert.assertEquals("dr arc ",map.get("arc").trim().toLowerCase(), actual.get("arc").trim().toLowerCase());
        if(!(map.get("node").equalsIgnoreCase("skip"))){
            Assert.assertEquals("node ",map.get("node").trim().toLowerCase(), actual.get("node").trim().toLowerCase());
        }


        Assert.assertEquals("manual_calibration ",map.get("manual_calibration").trim().toLowerCase(), actual.get("manual_calibration").trim().toLowerCase());
        Assert.assertEquals("manual_imaging ",map.get("manual_imaging").trim().toLowerCase(), actual.get("manual_imaging").trim().toLowerCase());
        Assert.assertEquals("weblog_review ",map.get("weblog_review").trim().toLowerCase(), actual.get("weblog_review").trim().toLowerCase());
        Assert.assertEquals("qa2_approval ",map.get("qa2_approval").trim().toLowerCase(), actual.get("qa2_approval").trim().toLowerCase());

    }

    @When("^click on search result - Management$")
    public void clickOnSearchResultManagement(List<String> params) {
        String user = null;
        if(params.get(0).equalsIgnoreCase("username")){
            user = PropertiesFileUtil.getProperty(params.get(0));
        } else {
            user = params.get(0);
        }
        DraManagementPage.getRowSearchResultById(driver,user).click();
        WaitUtil.sleep(500);
    }
}
