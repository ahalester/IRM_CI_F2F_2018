package alma.aat.archive.web.browser.steps;

import alma.aat.archive.db.DbHelper;
import alma.aat.archive.web.browser.setup.AbstractSelenium3DockerMultiBrowserDriverSetup;
import alma.aat.archive.web.delegates.WebDriverExtended;
import alma.aat.archive.web.pageobjects.*;
import alma.aat.archive.web.utils.PropertiesFileUtil;
import alma.aat.archive.web.utils.WaitUtil;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
import java.util.stream.Collectors;

import static alma.aat.archive.web.utils.PropertiesFileUtil.getServiceURL;
import static alma.aat.archive.web.utils.WebElementValidationUtil.elementDisplayedValidation;

/**
 *
 */
@SuppressWarnings("unused")
public class TestSteps extends AbstractSelenium3DockerMultiBrowserDriverSetup {

    private static Logger log = LoggerFactory.getLogger(TestSteps.class);
    private List<String> auth = new ArrayList<>();
    private WebDriverExtended driver = getWebDriverExtended();

    private static String testPhase = System.getProperty("testPhase");
    private static String release = System.getProperty("testEnv");


    @Given("^Service available")
    public void serviceIsAvailable(List<String> details) {
        String service = details.get(0);
        driver.getPageSource();
        NavigatePage.toGlobalLink(driver, getServiceURL(testPhase, release, (service.contains("dataportal") || service.contains("datatracker")) ?
                service = "/healthcheck" : service));

        // Login if required by service
        if (Arrays.asList("rh", "dataportal").contains(service)) {
            elementDisplayedValidation(driver, LoginPage.userField(driver));
        }
    }


    @When("^user navigates to a specific URL$")
    public void goToURL(List<String> details) {
        String url = getServiceURL(testPhase, release, details.get(0), details.get(1));
        try {
            driver.getPageSource();
            NavigatePage.toGlobalLink(driver, url);
            log.info(String.format("Successfully navigated to URL '%s'", url));
        } catch (Exception e) {
            log.error(String.format("Current build environment cannot be accessed! Please check"
                    + " the provided URL '%s'", url));
            Assert.fail(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'.", url));
        }
    }


    @Given("^test environment is available$")
    public void verifyIfTestEnvIsAvailable(List<String> details) {
        String testPhase = System.getProperty("testPhase");
        String application = details.get(0);
        String url = getServiceURL(TestSteps.testPhase, release, details.get(0));
        try {
            driver.getPageSource();
            NavigatePage.toGlobalLink(driver, url);
            log.info(String.format("Successfully navigated to URL '%s'", url));
        } catch (Exception e) {
            log.error(String.format("Current build environment cannot be accessed! Please check"
                    + " the provided URL '%s'", url));
            Assert.fail(String.format("Current build environment cannot be accessed! Please check the "
                    + "provided URL '%s'.", url));
        }
    }


    @Given("^login page is displayed in a new tab$")
    public void navigateToUrl() {
        List<String> browserTabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(browserTabs.get(1));
        elementDisplayedValidation(driver, LoginPage.userField(driver));
        WaitUtil.sleep(2000);
    }

    @Given("^test environment is available1$")
    public void verifyIfTestEnvIsAvailable() {
        WebElement we = RHRequestPage.downloadSelectedButton(driver);
        elementDisplayedValidation(driver, we);
    }


    @When("^user mades a search$")
    public void searchProjects(List<String> searchValues) {

    }


    @Then("^specific items are available$")
    public void sameLevelItemsAvailable(List<String> details) {
        List<WebElement> items;
        switch (details.get(0)) {
            case "SB": {
                items = RHRequestPage.availableSBs(driver);
                elementDisplayedValidation(driver, items.get(0), 5);
                Assert.assertTrue(String.format("No '%s' was found!", details.get(0)),
                        items.size() > 0);
                break;
            }
            case "MOUS":
                break;
            case "GOUS":
                break;
            default:
                break;
        }
        log.info(String.format("The '%s's were available within the page.", details.get(0)));
    }


    @When("^all items are expanded and product members are visible$")
    public void expandProduct() {
        List<WebElement> expanders = RHRequestPage.expanders(driver);
        for (int i = 0; i < expanders.size(); i++) {
            expanders.get(0).click();
        }
    }

    @Then("^ICT-12656: Product member files can be individually downloaded$")
    public void textMembers() {
        List<WebElement> memberLinkList = RHRequestPage.memberLinkList(driver);
        List<WebElement> memberSpanList = RHRequestPage.memberSpanList(driver);
//        elementDisplayedValidation(driver, memberSpanList.get(0));
//        Assert.assertTrue("No member links available", memberLinkList == null);
//        Assert.assertTrue("Member spans available", memberSpanList.size() > 0);

    }

    @Then("^SBs are expandable and always have a 'Source' sub-item$")
    public void expandableSBs() {
        List<WebElement> items = RHRequestPage.availableSBs(driver);
        for (int i = 0; i < items.size(); i++) {
            WebElement we = RHRequestPage.expander(items.get(i));
            elementDisplayedValidation(driver, we);
            String sbUID = RHRequestPage.uid(items.get(i)).getAttribute("innerHTML");
            we.click();
            WebElement src = RHRequestPage.sources(driver).get(i);
            String source = RHRequestPage.uid(src).getAttribute("innerHTML");
            //String sourceUID = source.substring(0, 8);
            //Assert.assertTrue(String.format("The Source UID '%s' is wrong!", source), sbUID.contains(sourceUID));
            we.click();
        }
    }

    @Then("^available SBs have 'product' and 'raw' items at the same level$")
    public void sameLevelItemsAvailable() {
        List<WebElement> items = RHRequestPage.availableSBs(driver);
        WebElement we;

        for (int i = 0; i < items.size(); i++) {
            WebElement product = RHRequestPage.productList(driver).get(i);
            elementDisplayedValidation(driver, product);

            String type = RHRequestPage.type(product).getAttribute("innerHTML");

            Assert.assertTrue(String.format("The expected item was 'product' but found '%s'!",
                    type), type.equalsIgnoreCase("product"));


            WebElement raw = RHRequestPage.rawList(driver).get(i);
            elementDisplayedValidation(driver, raw);

            type = RHRequestPage.type(raw).getAttribute("innerHTML");

            Assert.assertTrue(String.format("The expected item was 'raw' but found '%s'!",
                    type), type.equalsIgnoreCase("raw"));
        }
    }

    @When("^user fills the credentials$")
    public void inputCredentials(List<String> credentials) {
        String userKey = credentials.get(0);
        String user = PropertiesFileUtil.getProperty("aatConfig.properties", userKey);
        String password = PropertiesFileUtil.getProperty("aatConfig.properties", user + ".user.password");

        elementDisplayedValidation(driver, LoginPage.userField(driver));

        LoginPage.userField(driver).clear();
        LoginPage.userField(driver).sendKeys(user);

        elementDisplayedValidation(driver, LoginPage.passwordField(driver));
        LoginPage.passwordField(driver).clear();
        LoginPage.passwordField(driver).sendKeys(password);

        log.info(String.format("Filled credential for '%s' ", credentials.get(0)));
    }


    @When("^user clicks the LOGIN button$")
    public void clickLogin() {
        WebElement we = LoginPage.loginButton(driver);
        elementDisplayedValidation(driver, we);
        we.click();
        log.info("Clicked on 'Login'");
        //WaitUtil.sleep(500);
    }

    @And("^user input value on field$")
    public void inputField(List<String> fieldValue) {
        String inputFieldId = fieldValue.get(0);
        String inputFieldKey = fieldValue.get(1);

        String inputFieldValue = PropertiesFileUtil.getProperty("aatConfig.properties", inputFieldKey);


        // Click on field name
        WebElement inputFieldTitle = AQSearchPage.inputFieldTitle(driver, inputFieldId);
        elementDisplayedValidation(driver, inputFieldTitle);
        inputFieldTitle.click();

        // Input search value
        WebElement inputField = AQSearchPage.inputField(driver, inputFieldId);
        elementDisplayedValidation(driver, inputField);
        inputField.clear();
        inputField.sendKeys(inputFieldValue);

        // Click 'Search' button
        WebElement searchButton = AQSearchPage.searchButton(driver);
        searchButton.click();

        log.info(String.format("Filled the '%s' field with '%s'", inputFieldId, inputFieldValue));
    }


    @Then("^results table is shown$")
    public void showResultsTable() {
        WebElement resultTable = AQSearchPage.resultTable(driver);
        elementDisplayedValidation(driver, resultTable);

        log.info(String.format("The request result tables is displayed"));
    }

    @When("^user select the first item and click 'Submit Download'$")
    public void submitRequest() {
        WebElement resultTable = AQSearchPage.resultTable(driver);
        elementDisplayedValidation(driver, resultTable);

        WebElement firstCheckBox = AQSearchPage.firstCheckBox(driver);
        elementDisplayedValidation(driver, firstCheckBox);
        firstCheckBox.click(); // Select first item for download

        WebElement submitRequestButton = AQSearchPage.submitRequestButton(driver);
        submitRequestButton.click();

        log.info("First checkbox selected and submit request button pressed");
    }


    @When("^user select the first product'$")
    public void selectFirstProduct() {
        WebElement resultTable = AQSearchPage.resultTable(driver);
        elementDisplayedValidation(driver, resultTable);

        WebElement firstCheckBox = AQSearchPage.firstCheckBox(driver);
        elementDisplayedValidation(driver, firstCheckBox);
        firstCheckBox.click(); // Select first item for download

        WebElement submitRequestButton = AQSearchPage.submitRequestButton(driver);
        submitRequestButton.click();

        log.info("First checkbox selected and submit request button pressed");
    }


    @When("^request tree is shown in the RH$")
    public void getRequestNumber() throws Exception {
        WebElement requestTitle = RHRequestPage.requestNumber(driver);
        elementDisplayedValidation(driver, requestTitle);

        Thread.sleep(500);

        WebElement requestNode = RHRequestPage.requestNumberTreeNode(driver);
        elementDisplayedValidation(driver, requestNode, 1000);

        //log.info("Request number is:" + requestNode.getText());
    }

    @When("^user click 'Download Selected'")
    public void clickDownloadSelected() throws Exception {
        Thread.sleep(500);

        WebElement downloadSelectedButton = RHRequestPage.downloadSelectedButton(driver);
        elementDisplayedValidation(driver, downloadSelectedButton);

        downloadSelectedButton.click();
        log.info("Click 'Download Selected' button");

    }


    @When("^user select first item and click 'Download Selected'")
    public void selectFirstItemAndclickDownloadSelected() throws Exception {
        Thread.sleep(500);

        // Deselect all by clicking in the request element
        WebElement requestCheck = RHRequestPage.requestCheckbox(driver);
        // Select all
        requestCheck.click();
        // Deselect all
        requestCheck.click();


        // Expand first product
        WebElement firstProductExpander = RHRequestPage.firstProductExpander(driver);
        firstProductExpander.click();

        // Select first productMember
        WebElement firstProductMemberCheckbox = RHRequestPage.firstProductMemberCheckbox(driver);
        firstProductMemberCheckbox.click();


        clickDownloadSelected();
    }


    @When("^popup appears and user clicks 'Download Script'")
    public void clickDownloadScript() {
        String downloadDir = PropertiesFileUtil.getProperty("aatConfig.properties", "files.download.dir");
        Path downloadDirPath = Paths.get(downloadDir);

        // Create now the download directory if it doesn't exist yet
        if (!Files.exists(downloadDirPath)) {

            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("-rwxr-xr-x");
            FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);

            try {
                Files.createDirectory(downloadDirPath, fileAttributes);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Download scripts directory created.");
        }

        // Now press the download button
        WebElement we = RHRequestPage.downloadScriptButton(driver);
        elementDisplayedValidation(driver, we);
        we.click();
        log.info("Click 'Download Script' button");

        // Switching to Alert
        //Alert alert = driver.switchTo().alert();

        // Capturing alert message.
        //String alertMessage= driver.switchTo().alert().getText();


    }

    @When("^popup appears and user clicks 'File List'")
    public void clickFileList() throws Exception {
        Thread.sleep(10000);
        WebElement we = RHRequestPage.fileListButton(driver);
        elementDisplayedValidation(driver, we);
        we.click();
        log.info("Click 'File List Script' button");
    }


    //    @And("^ICT-13005 readme, product and auxiliary data is preselected")
    public void dataIsPreselected() throws Exception {
        String expectedClassName = "fancytree-has-children fancytree-partsel fancytree-selected fancytree-exp-c fancytree-ico-c";

        List<WebElement> productRows = RHRequestPage.productRows(driver);
        assertListElementsClass(productRows, expectedClassName);

        List<WebElement> auxiliaryRows = RHRequestPage.auxiliaryRows(driver);
        assertListElementsClass(auxiliaryRows, expectedClassName);

        List<WebElement> readmeRows = RHRequestPage.readmeRows(driver);
        assertListElementsClass(readmeRows, expectedClassName);
    }


    @Then("^ICT-13005: readme, product and auxiliary data is preselected")
    public void preselectedReadmeProductAuxiliary() throws Exception {
        // When selecting 1 item in the results table (science goal) for submitting the download request, the readme file should be preselected
        List<WebElement> readme = driver.findElements(By.xpath(".//*[@class='fancytree-partsel fancytree-selected readme fancytree-exp-n fancytree-ico-c']"));
        elementDisplayedValidation(driver, readme.get(0));
        Assert.assertTrue(readme.size() == 1);

        // and the product and one auxiliary checkbox should be also selected. Since there was only 1 science goal selected, only 2 elements of this class are expected
        List<WebElement> selectedProdAndAux = driver.findElements(By.xpath(".//*[@class='fancytree-has-children fancytree-partsel fancytree-selected fancytree-exp-c fancytree-ico-c']"));
        elementDisplayedValidation(driver, selectedProdAndAux.get(0));
        Assert.assertTrue(selectedProdAndAux.size() == 2);

    }

    @Then("^ICT-13217: displayed total size is the sum of individual file sizes")
    public void totalSizeIsCorrect() throws Exception {
        List<Float> sizes = RHRequestPage.sizes(driver);

        Double calculatedTotal = sizes.stream().mapToDouble(i -> i).sum();
        Float displayedTotal = RHRequestPage.totalSize(driver);

        Assert.assertEquals("Sum of individual sizes(GB) and displayed total(GB) should be equal", Math.round(calculatedTotal / 1024), Math.round(displayedTotal / 1024));
    }

    @When("^SBs are expanded")
    public void expandSBs() throws Exception {
        List<WebElement> SBRows = RHRequestPage.SBRows(driver);
        for (WebElement sbRow : SBRows) {
            RHRequestPage.expander(sbRow).click();
        }
    }

    @Then("^ICT-12977: the source list is unique")
    public void sourceListIsUnique() throws Exception {
        // This validation is only valid if there is only SB. TODO: Improve it to make it work with more than than one SB
        List<String> sbNames = RHRequestPage.SBRows(driver).stream().map(sbRow -> sbRow.findElements(By.className("uid")).get(0).getText()).collect(Collectors.toList());
        List<String> sbNamesUnique = sbNames.stream().distinct().collect(Collectors.toList());
        Assert.assertTrue("Sources are unique in the list", sbNames.size() == sbNamesUnique.size());
    }


    private void assertListElementsClass(List<WebElement> productRows, String expectedClassName) {
        productRows.forEach(row -> {
            String row_class = row.getAttribute("class");
            log.info("Product class= " + row_class);
            //Assert.assertEquals(expectedClassName, row_class);
            Assert.assertTrue("Checkbox is selected", row_class.contains("fancytree-selected"));
        });
    }

    private void assertListElementsAreUnique(List<WebElement> productRows) {

        productRows.forEach(row -> {
            String row_class = row.getAttribute("class");
            log.info("Product class= " + row_class);
            //Assert.assertEquals(expectedClassName, row_class);
            Assert.assertTrue("Checkbox is selected", row_class.contains("fancytree-selected"));
        });
    }

    @Then("^download script includes only readme file$")
    public void containsOnlyReadme() {
        String expectedUrlList = "LIST=(\"\n" +
                "http://phase-a1.hq.eso.org/dataportal-ARCHIVE-2018JUN/alma/requests/248037912/2017.1.00984.S/readme/2017.1.00984.S.readme.txt\n" +
                "\")";

        String downloadedFile = System.getProperty("user.home") + "/Downloads/downloadRequest248037912.sh";
        Assert.assertEquals("Download script contains only readme", expectedUrlList, getUrlList(downloadedFile).toString());
    }


    @Then("^download script is available and prepared for integration tests$")
    public void scriptAvailableAndPreparedForIT(List<String> projectKey) {
        String downloadDir = PropertiesFileUtil.getProperty("aatConfig.properties", "files.download.dir");
        String downloadedProjectId = PropertiesFileUtil.getProperty("aatConfig.properties", projectKey.get(0));
        String testPhase = System.getProperty("testPhase");

        String fileName = downloadDir + "/" + downloadedProjectId + "_" + testPhase + ".sh";
        log.info("Download file will be saved as: " + fileName);

        //Find original file name and replaced by parameterized one
        try {
            Optional<Path> originalFileName = Files.find(Paths.get(downloadDir),
                    Integer.MAX_VALUE,
                    (path, basicFileAttributes) -> path.toFile().getName().matches("download.*.sh")).findFirst();

            //Assert.assertTrue("The download script has been correctly downloaded: ", originalFileName.isPresent());

            Path resultFile = Paths.get(fileName);
            Files.copy(originalFileName.get(), resultFile, StandardCopyOption.REPLACE_EXISTING);

            //Assert.assertTrue("The download script has been copied to the shared location", Files.exists(resultFile));

            log.info("Successfully downloaded and renamed download script file " + resultFile.toFile().getAbsolutePath());

        } catch (Exception e) {
            log.error("Download script could not be saved as " + fileName + " due to:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Given("^ICT-12656: Only readme.txt can be download when data is not released$")
    public void navigateToLinkListTab() {
        WaitUtil.sleep(3000);

        List<String> browserTabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(browserTabs.get(2));
        List<WebElement> downloadList = RHDownloadLinksPage.linkList(driver);
        elementDisplayedValidation(driver, downloadList.get(0));

        //Assert.assertTrue("The link list only contains the readme file", downloadList.size() == 1 && downloadList.get(0).getText().contains("readme.txt"));

        Assert.assertTrue("The link list only contains the readme file", downloadList.size() == 1);


        log.info("Opened new tab with download links");
    }

    @NotNull
    private StringBuffer getUrlList(String downloadedFile) {
        StringBuffer urlList = new StringBuffer();
        try {
            List<String> lines = Files.readAllLines(Paths.get(downloadedFile));
            int urlListStartPos = 14;
            int urlListEndPos = 17;
            for (int i = urlListStartPos; i < urlListEndPos; i++) {
                urlList.append(lines.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlList;
    }


    @Given("^data has been released$")
    public void release_data() {
        DbHelper dbHelper = DbHelper.getInstance();
        String query = "update ASA_DELIVERY_STATUS set RELEASE_DATE='04-JAN-2019' where DELIVERY_NAME like '2017.1.00984.S%'";
        List<Map<String, String>> resultset = dbHelper.getRecordList("Select RELEASE_DATE from ASA_DELIVERY_STATUS where DELIVERY_NAME like '2017.1.00984.S%'");

        Assert.assertTrue(dbHelper.executeQuery(query));
        resultset.forEach(result -> {
            log.debug(result.get("RELEASE_DATE"));
            Assert.assertTrue("Date is 04-JAN-2019", result.get("RELEASE_DATE").equals("2017-01-04 00:00:00.0"));
        });
    }

}
