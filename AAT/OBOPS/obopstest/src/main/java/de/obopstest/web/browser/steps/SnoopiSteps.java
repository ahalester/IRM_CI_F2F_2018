package de.obopstest.web.browser.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.obopstest.common.SessionStateHandler;
import de.obopstest.web.browser.setup.AbstractSelenium3DockerMultiBrowserDriverSetup;
import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.pageobjects.snoopi.*;
import de.obopstest.web.utils.PropertiesFileUtil;
import de.obopstest.web.utils.StringUtil;
import de.obopstest.web.utils.WaitUtil;
import de.obopstest.web.utils.WebElementValidationUtil;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import de.obopstest.web.pageobjects.*;

import static de.obopstest.web.utils.PropertiesFileUtil.getProperty;
import static de.obopstest.web.utils.WebElementValidationUtil.elementDisplayedValidation;
import static de.obopstest.web.utils.enums.EnvironmentURL.*;

public class SnoopiSteps extends AbstractSelenium3DockerMultiBrowserDriverSetup {

    private static Logger LOG = LoggerFactory.getLogger(SnoopiSteps.class);
    private List<String> auth = new ArrayList<>();
    private WebDriverExtended driver = getWebDriverExtended();


    public static String getElementName(String propName) {
        if (propName.equalsIgnoreCase("username")) {
            return getProperty(propName);
        } else {
            return getProperty("elements", propName);
        }
    }

    @Given("^go to Snoopi test environment$")
    public void gotoSnoopiTestEnv() {
        String url = "";
        String envPhase = System.getProperty("envPhase");
        String testEnv = System.getProperty("testEnv").toUpperCase();


        try {
            if (envPhase.equalsIgnoreCase("PHAB")) {
                url = PropertiesFileUtil.getNavigationURLsnoopi(PHB_SNOOPI_URL, System.getProperty("testEnv"));
            } else if (envPhase.equalsIgnoreCase("PHAA")) {
                url = PropertiesFileUtil.getNavigationURLsnoopi(PHA_SNOOPI_URL, System.getProperty("testEnv").toUpperCase());
            }
            LOG.info("The URL is: " + url);
            SessionStateHandler.setValue("url_snoopi",url);
            de.obopstest.web.pageobjects.NavigatePage.toGlobalLink(driver, url);
        }catch (Exception e) {
            LOG.error(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
            Assert.fail(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'", url));
        }

    }

    @When("^the user clicks the LOGIN button Snoopi$")
    public void clickLoginSnoopi() {
        WebElement we = LoginPage.loginButton(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'Login'");
        WaitUtil.sleep(3000);
    }

    @Then("^the Snoopi view is displayed$")
    public void snoopiViewPage() {
        WebElement we = HeaderPage.snoopiTitle(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we, WaitUtil.DEFAULT_WAIT_FOR_ELEMENT);

        WaitUtil.sleep(5000);
    }

    @Then("^generic user is authenticated to Snoopi$")
    public void genericUserAuthenticatedToSnoopi() {
        WebElement we = HeaderPage.loggedInUserIcon(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we, WaitUtil.DEFAULT_WAIT_FOR_ELEMENT);
    }

    @And("^User info contains$")
    public void user_info_contains(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String text = details.get(0).get(0);
        if (text.equalsIgnoreCase("ous_session_data")) {
            String session_data = SessionStateHandler.getValue("ous_session_data").toString();
            Assert.assertTrue("User info does NOT contain: " + session_data ,HeaderPage.headerUserInfo(driver).getText().equalsIgnoreCase(session_data.trim()));
            LOG.info("User info contains: " + session_data);
        } else {
            Assert.assertTrue("User info does NOT contain: " + text ,HeaderPage.headerUserInfo(driver).getText().contains(text.trim()));
            LOG.info("User info contains: " + text);
        }
    }

    @When("^Select contact scientist checkbox Snoopi$")
    public void contactScientistCheckboxSnoopi() {
        WebElement we = HeaderPage.headerContactScientistCheckbox(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'Contact scientist' checkbox");
        WaitUtil.sleep(1000);
    }

    @When("^Validate Export button is displayed Snoopi$")
    public void validate_export_button_displayed_snoopi() {
        WaitUtil.sleep(1000);
        WebElement we = HeaderPage.headerExportButton(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'Export' button");
        HeaderPage.headerExportCvsOption(driver);
        HeaderPage.headerExportTxtOption(driver);
    }

    @When("^Go to My Projects Snoopi$")
    public void go_to_my_prj_snoopi() {
        WebElement we = SidebarPage.projectsLink(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'My Projects' menu");
        WaitUtil.sleep(1000);
    }

    @When("^Validate home page Snoopi$")
    public void validate_home_page_snoopi() {
        WaitUtil.sleep(1000);

        WebElementValidationUtil.elementDisplayedValidation(driver, HeaderPage.headerHelpButton(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, HeaderPage.loggedInUserIcon(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, HomePage.piProjectsWidget(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, HomePage.coIprojectsWidget(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, HomePage.piSbsWidget(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, HomePage.coIsbsWidget(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, HomePage.moreNewsButton(driver));

        WebElementValidationUtil.elementDisplayedValidation(driver, HomePage.searchBox(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, HomePage.searchButton(driver));

        WebElementValidationUtil.elementDisplayedValidation(driver, HomePage.searchDropdown(driver));

        HomePage.searchDropdown(driver).click();
        WebElementValidationUtil.elementDisplayedValidation(driver, HomePage.searchDropdownOptionPrj(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, HomePage.searchDropdownOptionSbs(driver));
        HomePage.searchDropdown(driver).click();

    }

    @When("^Go to My SBs Snoopi$")
    public void go_to_my_sbs_snoopi() {
        WebElement we = SidebarPage.sbsLink(driver);
        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'My SBs' menu");
        WaitUtil.sleep(1000);
    }

    @When("^Validate My Projects page Snoopi$")
    public void validatemy_projects_snoopi() {
        WaitUtil.sleep(1000);

        WebElementValidationUtil.elementDisplayedValidation(driver, MyProjectsPage.projectsPageLabel(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, MyProjectsPage.projectsSearchBox(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, MyProjectsPage.projectsSearchButton(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, MyProjectsPage.piTab(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, MyProjectsPage.coiTab(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, MyProjectsPage.projectsTable(driver));
        MyProjectsPage.coiTab(driver).click();
        WaitUtil.sleep(1000);
        Assert.assertTrue(!MyProjectsPage.projectsTableRows(driver).isEmpty());
    }

    @When("^Validate My SBs page Snoopi$")
    public void validatemy_sbs_snoopi() {
        WaitUtil.sleep(1000);

        WebElementValidationUtil.elementDisplayedValidation(driver, MySbsPage.allSbsCheckBox(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, MySbsPage.sbsPageLabel(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, MySbsPage.sbsTable(driver));

        WebElementValidationUtil.elementDisplayedValidation(driver, MySbsPage.piTab(driver));
        WebElementValidationUtil.elementDisplayedValidation(driver, MySbsPage.coiTab(driver));

        MySbsPage.coiTab(driver).click();
        Assert.assertTrue(!MySbsPage.sbsTableRows(driver).isEmpty());

    }

    @When("^Go to Home page Snoopi$")
    public void go_to_home_snoopi() {
        WebElement we = SidebarPage.homeLink(driver);
//        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'Home' menu");
        WaitUtil.sleep(1000);
    }

    @When("^Click PI Projects widget Snoopi$")
    public void go_to_pi_prj_widget_snoopi() {
        WebElement we = HomePage.piProjectsWidget(driver);
//        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'PI Projects' widget");
        WaitUtil.sleep(1000);
        WebElementValidationUtil.elementDisplayedValidation(driver, MyProjectsPage.piTab(driver));
        Assert.assertTrue("PI tab not active",MyProjectsPage.isPiTabSelected(driver));
    }

    @When("^Click Co-I Projects widget Snoopi$")
    public void go_to_coi_prj_widget_snoopi() {
        WebElement we = HomePage.coIprojectsWidget(driver);
//        WebElementValidationUtil.elementDisplayedValidation(driver, we);
        we.click();
        LOG.info("Clicked on 'Co-I Projects' widget");
        WaitUtil.sleep(1000);
        MyProjectsPage.projectsTableRows(driver);
        Assert.assertTrue("PI tab not active",MyProjectsPage.isCoiTabSelected(driver));
    }

    @When("^Click PI SBS widget Snoopi$")
    public void go_to_pi_sbs_widget_snoopi() {
        WebElement we = HomePage.piSbsWidget(driver);
        we.click();
        LOG.info("Clicked on 'PI Scheduling blocks' widget");
        WaitUtil.sleep(1000);
        WebElementValidationUtil.elementDisplayedValidation(driver, MySbsPage.piTab(driver));
        Assert.assertTrue("PI tab not active",MySbsPage.isPiTabSelected(driver));
    }

    @When("^Click Co-I SBS widget Snoopi$")
    public void go_to_coi_sbs_widget_snoopi() {
        WebElement we = HomePage.coIsbsWidget(driver);
        we.click();
        LOG.info("Clicked on 'PI Scheduling blocks' widget");
        WaitUtil.sleep(1000);
        WebElementValidationUtil.elementDisplayedValidation(driver, MySbsPage.coiTab(driver));
        Assert.assertTrue("Co-I tab not active",MySbsPage.isCoiTabSelected(driver));
    }

    @When("^Select random SB Snoopi$")
    public void select_sb_snoopi() {
        List<String> sbNames = MySbsPage.getAllSbsNames(driver);
        int index = StringUtil.generateRandomNo(sbNames.size()-1);
        MySbsPage.getTableCellByColRowLink(driver,MySbsPage.SBS_NAME,index).click();
        WaitUtil.sleep(1000);
        WebElementValidationUtil.elementDisplayedValidation(driver, PrjAndSbPage.treePrjCode(driver));
    }

    @When("^Select random Project Snoopi$")
    public void select_project_snoopi() {
        List<String> pCodes = MyProjectsPage.getAllPrjCodes(driver);
        int index = StringUtil.generateRandomNo(pCodes.size()-1);
        MyProjectsPage.getTableCellByColRowLink(driver,MyProjectsPage.PRJ_CODE,index).click();
        WaitUtil.sleep(1000);
        WebElementValidationUtil.elementDisplayedValidation(driver, PrjAndSbPage.treePrjCode(driver));
    }

}
