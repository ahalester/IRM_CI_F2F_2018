package de.obopstest.web.browser.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import de.obopstest.common.SessionStateHandler;
import de.obopstest.web.browser.setup.AbstractSelenium3DockerMultiBrowserDriverSetup;
import de.obopstest.web.delegates.WebDriverExtended;
import de.obopstest.web.pageobjects.IntegrationAquaOusPage;
import de.obopstest.web.pageobjects.ProtrackEntityDetailsPage;
import de.obopstest.web.pageobjects.ProtrackOusSearchPage;
import de.obopstest.web.pageobjects.ProtrackPage;
import de.obopstest.web.utils.Wait;
import de.obopstest.web.utils.WaitUtil;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class ProtrackSteps extends AbstractSelenium3DockerMultiBrowserDriverSetup {

    private static Logger LOG = LoggerFactory.getLogger(ProtrackSteps.class);
    private List<String> auth = new ArrayList<>();
    private WebDriverExtended driver = getWebDriverExtended();

    @And("^protrack project search results found$")
    public void protrack_prj_search_results_found() {
//        try {
//            WebElement we = GeneralPage.loadingProgress(driver);
//            WaitUtil.waitForLocatedElementIsNotDisplayed(driver, we, WaitUtil.DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
//        }catch(NoSuchElementException e){
//            LOG.info("loadingProgress did not appear");
//        }
        WaitUtil.sleep(1000);
        ProtrackPage.waitForFinishedLoadingInProgress(driver);
        Assert.assertTrue("No results found !!!", !ProtrackPage.searchProjectListItem(driver).isEmpty());
    }

    @And("^select protrack project by text$")
    public void select_prj_by_name(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = details.get(0).get(0);

        ProtrackPage.waitForFinishedLoadingInProgress(driver);
        Wait.sleep(1000);
//        driver.executeScript("arguments[0].scrollIntoView(true);", ProtrackPage.selectCellByText(driver, fieldName));
        ProtrackPage.selectCellByText(driver, fieldName).click();

        Wait.sleep(1000);

    }

    @And("^select protrack project by state$")
    public void select_prj_by_state(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = details.get(0).get(0);

//        driver.executeScript("arguments[0].scrollIntoView(true);", ProtrackPage.selectCellByText(driver, fieldName));
        ProtrackPage.selectCellByText(driver, fieldName).click();

        Wait.sleep(1000);
    }

    @And("^open OUS search tab$")
    public void open_ous_search_tab() {
        ProtrackPage.protrackSearchOUSButton(driver).click();
        ProtrackPage.waitForProtrackOusSearchTab(driver);
        LOG.info("Opened Protrack OUS search tab");

//        Wait.sleep(1000);
    }

    @And("^open SB search tab$")
    public void open_sb_search_tab() {
        ProtrackPage.protrackSearchSBButton(driver).click();
        ProtrackPage.protrackSearchSBButton(driver);
//        Wait.sleep(1000);
    }

    @And("^enter OUS Status UID in search field$")
    public void enter_ous_uid_search(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = details.get(0).get(0);
        if (fieldName.equalsIgnoreCase("ous_session_data")) {
            String session_data = SessionStateHandler.getValue("ous_session_data").toString();
            ProtrackPage.ousStatusUidField(driver).sendKeys(session_data);
            LOG.info("Input OUS UID  " + session_data);
        } else {
            ProtrackPage.ousStatusUidField(driver).sendKeys(fieldName);
            LOG.info("Input OUS UID  " + fieldName);
        }
    }

    @And("^enter SB Schedulable dates interval selection From To$")
    public void enter_sb_schedule_interval_search(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldFrom = details.get(0).get(0);
        String fieldTo = details.get(0).get(1);

        ProtrackPage.ousStatusSbIntervalFrom(driver).sendKeys(fieldFrom);
        ProtrackPage.ousStatusSbIntervalTo(driver).sendKeys(fieldTo);
    }

    @And("^click search for SB$")
    public void click_search_for_sb() {
        ProtrackPage.sbSearchButton(driver).click();
        Wait.sleep(1000);
    }

    @And("^verify SB search results are found$")
    public void verify_sb_search_results_found() {
        WaitUtil.sleep(1000);
        List<WebElement> elems = ProtrackPage.sbSearchResults(driver);
        Assert.assertTrue("SB search results not found",
                elems != null);
    }

    @And("^click search for OUS$")
    public void click_search_for_ous() {
        ProtrackPage.ousSearchButton(driver).click();
        LOG.info("Clicked Protrack OUS search");
        Wait.sleep(1000);
    }


    @And("^verify OUS UID on Entity page$")
    public void verify_ous_on_entity_page(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = details.get(0).get(0);
        if (fieldName.equalsIgnoreCase("ous_session_data")) {
            ProtrackPage.ousUidOnEntityPage(driver, SessionStateHandler.getValue("ous_session_data"));
            LOG.info("Found OUS UID on Entity page  " + SessionStateHandler.getValue("ous_session_data"));
        } else {
            ProtrackPage.ousUidOnEntityPage(driver, fieldName);
            LOG.info("Found OUS UID on Entity page  " + fieldName);
        }
        WaitUtil.sleep(1000);
    }

    @And("^verify state for OUS in Protrack$")
    public void verify_ous_state_in_protrack(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = details.get(0).get(0);
        Assert.assertTrue("OUS state not found",
                ProtrackPage.ousStateInProtrack(driver, fieldName) != null);
    }

    @And("^verify OUS status history info contains$")
    public void verify_ous_status_history_protrack(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = details.get(0).get(0);
        try {
            ProtrackEntityDetailsPage.ousHistoryLastPageIfPresent(driver).click();
        } catch (Exception e) {

        }

        WaitUtil.sleep(1000);
        Assert.assertTrue("OUS state history not found",
                ProtrackPage.ousStateHistoryInProtrack(driver, fieldName) != null);
    }

    @And("^change OUS state in Protrack$")
    public void change_ous_state_in_protrack_to(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String initialState = details.get(0).get(0);
        String targetState = details.get(0).get(1);

        ProtrackPage.ousChamgeStateIcon(driver).click();
        WaitUtil.sleep(1000);

        ProtrackPage.ousStateTransitionInProtrack(driver, initialState, targetState).click();
        WaitUtil.sleep(1000);

        ProtrackPage.ousStateChangeCommentInProtrackTextarea(driver).sendKeys("auto test");
        ProtrackPage.ousStateChangeConfirmOK(driver).click();

        WaitUtil.sleep(1000);
        ProtrackPage.waitForFinishedLoadingInProgress(driver);
    }

    @And("^navigate to Child SchedBlock on Entity page$")
    public void navigateToChildSchedBlockOnEntityPage() {
        WaitUtil.sleep(500);
        ProtrackEntityDetailsPage.ousDetailsChildEntityLink(driver).click();
    }

    @And("^go to parent OUS on Entity page$")
    public void goToParentOUSonEntityPage() {
        WaitUtil.sleep(500);
        ProtrackEntityDetailsPage.sbNavigateToParentButton(driver).click();
    }

    @And("^Select OUS status in OUS search page$")
    public void select_ous_status_search(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = details.get(0).get(0);
        if (fieldName.equalsIgnoreCase("ous_session_data")) {
            String session_data = SessionStateHandler.getValue("ous_session_data").toString();
            //click only if it is not already selected
            if (!ProtrackOusSearchPage.isOusStatusHistoryCheckboxChecked(driver, session_data)) {
                ProtrackOusSearchPage.ousStatusHistoryCheckbox(driver, session_data).click();
            }
        } else {
            if (!ProtrackOusSearchPage.isOusStatusHistoryCheckboxChecked(driver, fieldName)) {
                ProtrackOusSearchPage.ousStatusHistoryCheckbox(driver, fieldName).click();
            }

        }
    }

    @And("^Save OUS search$")
    public void save_ous_search(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = details.get(0).get(0);

        ProtrackOusSearchPage.favouriteSearchesTextbox(driver).clear();
        ProtrackOusSearchPage.favouriteSearchesTextbox(driver).sendKeys(fieldName);
        WaitUtil.sleep(500);

        ProtrackOusSearchPage.favouriteSearchesSave(driver).click();
        WaitUtil.sleep(500);
        ProtrackOusSearchPage.messageBoxOKbutton(driver).click();
        WaitUtil.sleep(500);
        Assert.assertFalse("Save Failed !!!", ProtrackOusSearchPage.favouriteSearchesTextbox(driver).getText().trim().matches(".+"));
    }

    @And("^Reset OUS search$")
    public void reset_ous_search() throws Throwable {
        WaitUtil.sleep(500);
        ProtrackOusSearchPage.ousSearchResetButton(driver).click();
        WaitUtil.sleep(500);
    }

    @And("^Perform OUS search$")
    public void perform_ous_search() throws Throwable {
        WaitUtil.sleep(500);
        ProtrackOusSearchPage.ousSearchButton(driver).click();
        WaitUtil.sleep(500);
    }

    @And("^Select OUS saved search$")
    public void select_ous_saved_search(DataTable table) throws Throwable {
        WaitUtil.sleep(1000);
        List<List<String>> details = table.raw();
        String fieldName = details.get(0).get(0);

        if (fieldName.equalsIgnoreCase("ous_session_data")) {
            String session_data = SessionStateHandler.getValue("ous_session_data").toString();
            ProtrackOusSearchPage.favouriteSearchesDropdownArrow(driver).click();
            ProtrackOusSearchPage.favouriteSearchesDropdownOption(driver, session_data).click();
        } else {
            ProtrackOusSearchPage.favouriteSearchesDropdownArrow(driver).click();
            ProtrackOusSearchPage.favouriteSearchesDropdownOption(driver, fieldName).click();
        }
        WaitUtil.sleep(500);

        Assert.assertFalse("load saved search Failed !!!", ProtrackOusSearchPage.favouriteSearchesTextbox(driver).getText().trim().matches(fieldName));

        WaitUtil.sleep(500);
    }

    @And("^click OUS AQUA link$")
    public void click_ous_aqua_link() {
        WaitUtil.sleep(500);
        ProtrackEntityDetailsPage.ousInAquaLink(driver).click();
        LOG.info("Clicked on OUS AQUA link");
        WaitUtil.sleep(1000);
    }

    @And("^wait for AQUA OUS page to load$")
    public void wait_for_aqua_ous_page_load() {
        WaitUtil.sleep(500);
        IntegrationAquaOusPage.waitForPageLoaded(driver);
        LOG.info("AQUA OUS page loaded");
    }

    @And("^switch to second tab$")
    public void switch_to_second_tab() {
        WaitUtil.sleep(500);
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        LOG.info("Switch to second tab");
    }

}
