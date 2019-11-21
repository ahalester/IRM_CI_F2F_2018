package de.obopstest.web.browser.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import de.obopstest.common.SessionStateHandler;
import de.obopstest.web.browser.setup.AbstractSelenium3DockerMultiBrowserDriverSetup;
import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.pageobjects.*;
import de.obopstest.web.utils.PropertiesFileUtil;
import de.obopstest.web.utils.Wait;
import de.obopstest.web.utils.WaitUtil;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static de.obopstest.web.utils.PropertiesFileUtil.getNavigationURL;
import static de.obopstest.web.utils.enums.EnvironmentURL.*;


public class ProtrackChangeStateForceSteps extends AbstractSelenium3DockerMultiBrowserDriverSetup {

    private static Logger LOG = LoggerFactory.getLogger(ProtrackChangeStateForceSteps.class);
    private List<String> auth = new ArrayList<>();
    private WebDriverExtended driver = getWebDriverExtended();

    @Given("^go to Protrack State Change test environment$")
    public void verifyIfTestEnvIsAvailable() {
        String url = "";
        String envPhase = System.getProperty("envPhase");
        String testEnv = System.getProperty("testEnv").toUpperCase();


        try {
            if (envPhase.equalsIgnoreCase("PHAB")) {
                url = PropertiesFileUtil.getNavigationURLptcsf(PHB_PT_STATE_CHANGE_URL, System.getProperty("testEnv"));
            } else if (envPhase.equalsIgnoreCase("PHAA")) {
                url = PropertiesFileUtil.getNavigationURLptcsf(PHA_PT_STATE_CHANGE_URL, System.getProperty("testEnv").toUpperCase());
            }
            LOG.info("The URL is: " + url);
            SessionStateHandler.setValue("url_change_state_force",url);
            NavigatePage.toGlobalLink(driver, url);
        }catch (Exception e) {
            LOG.error(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
            Assert.fail(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
        }

    }

    @Given("^the Protrack State Change test environment is available$")
    public void verifyIfPTChangeStateForceTestEnvIsAvailable(){
        PTstateChangeForcePage.waitForPageLoaded(driver);
    }

    @And("^enter OUS Status UID$")
    public void enter_ous_uid_search(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = details.get(0).get(0);
        PTstateChangeForcePage.getEntityAtribute(driver,"ous","id").click();
        if (fieldName.equalsIgnoreCase("ous_session_data")) {
            String session_data = SessionStateHandler.getValue("ous_session_data").toString();
            PTstateChangeForcePage.getEntityAtribute(driver,"ous","id").sendKeys(session_data);
            LOG.info("Input OUS UID  " + session_data);
        } else {
            PTstateChangeForcePage.getEntityAtribute(driver,"ous","id").sendKeys(fieldName);
            LOG.info("Input OUS UID  " + fieldName);
        }
        WaitUtil.sleep(1000);
    }

    @And("^select OUS target state$")
    public void select_ous_target_state(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = details.get(0).get(0);
        if (fieldName.equalsIgnoreCase("ous_session_data")) {
            String session_data = SessionStateHandler.getValue("ous_session_data").toString();
            PTstateChangeForcePage.selectOusTargetStateOption(driver,session_data);
            LOG.info("Selected OUS target state  " + session_data);
        } else {
            PTstateChangeForcePage.selectOusTargetStateOption(driver,fieldName);
            LOG.info("Selected OUS target state  " + fieldName);
        }

        WaitUtil.sleep(1000);
    }

    @And("^select OUS flag$")
    public void select_ous_flag(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = details.get(0).get(0);
        if (fieldName.equalsIgnoreCase("ous_session_data")) {
            String session_data = SessionStateHandler.getValue("ous_session_data").toString();
            PTstateChangeForcePage.selectOusFlag(driver,session_data);
            LOG.info("Selected OUS flag  " + session_data);
        } else {
            PTstateChangeForcePage.selectOusFlag(driver,fieldName);
            LOG.info("Selected flag  " + fieldName);
        }

        WaitUtil.sleep(1000);
    }

    @Given("^click OUS Change button$")
    public void clickOUSChangeButton(){
        PTstateChangeForcePage.ousChangeButton(driver).click();
        LOG.info("Clicked on OUS Change button");
    }

    @Given("^successful state change$")
    public void stateChangeMessage(){
        PTstateChangeForcePage.messageBoxWindowOk(driver);
        LOG.info("Message box: change state successful");
    }




}
