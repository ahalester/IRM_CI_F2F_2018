package alma.aat.archive.web.browser.setup;

import alma.aat.archive.web.delegates.WebDriverExtended;
import alma.aat.archive.web.utils.FileUtil;
import alma.aat.archive.web.utils.PropertiesFileUtil;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.commons.lang.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

import static alma.aat.archive.web.utils.CaptureScreenshotUtil.captureScreenshot;
import static alma.aat.archive.web.utils.EncryptUtil.decrypt;
import static alma.aat.archive.web.utils.PropertiesFileUtil.getNavigationURL;
import static alma.aat.archive.web.utils.PropertiesFileUtil.getProperty;
import static alma.aat.archive.web.utils.StringUtil.dateConverter;
import static alma.aat.archive.web.utils.StringUtil.keyValidator;
import static alma.aat.archive.web.utils.WaitUtil.DEFAULT_WAIT_FOR_ELEMENT;
import static alma.aat.archive.web.utils.WaitUtil.DEFAULT_WAIT_FOR_PAGE_LOAD;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.Platform.ANY;

public class Selenium3DockerMultiBrowserAbstractSetup {

    public static WebDriverExtended driver;


    private long startTime;

    private static Logger LOG = LoggerFactory.getLogger(Selenium3DockerMultiBrowserAbstractSetup.class);

    private static String downloadDir = PropertiesFileUtil.getProperty("aatConfig.properties", "files.download.dir");
    private static String grid = System.getProperty("grid");
    private static Boolean isGrid = "yes".equalsIgnoreCase(grid = (grid == null) ? "no" : grid);
    private static Boolean isDocker = "yes".equalsIgnoreCase(System.getProperty("docker"));
    private static String seleniumVersion = System.getProperty("seleniumVersion");
    private static String browser = System.getProperty("browser");

    @Before("@MultiBrowserTesting")
    public void setUp(Scenario scenario) {
        startTime = System.currentTimeMillis();

        WebDriver baseWebDriver = null;
        try {
            switch (browser.toLowerCase()) {
                case "chrome": {
                    baseWebDriver = chromeSetup();
                    break;
                }
                case "firefox": {
                    baseWebDriver = firefoxSetup();
                    break;
                }
                case "safari": {
                    baseWebDriver = safariSetup();
                    break;
                }
                case "opera": {
                    baseWebDriver = operaSetup();
                    break;
                }
                case "ie": {
                    baseWebDriver = ieSetup();
                    break;
                }
                default: {
                    break;
                }
            }
            if (baseWebDriver != null) {
//                if (SystemUtils.IS_OS_WINDOWS && "firefox".equalsIgnoreCase(browser)) {
//                    baseWebDriver.manage().window().maximize();
//                } else if (!SystemUtils.IS_OS_WINDOWS) {
//                    baseWebDriver.manage().window().maximize();
//                }
                ((JavascriptExecutor) baseWebDriver).executeScript("window.focus();");
            } else {
                Assert.fail("Unable to instantiate the WebDriver!");
            }

            String baseUrl = getNavigationURL("url");
            if ((dateConverter(keyValidator())).before(dateConverter(decrypt(getProperty("license.properties", "key"))))) {
                driver = new WebDriverExtended(baseWebDriver, baseUrl);
            } else {
                ipRights();
            }
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_FOR_ELEMENT, SECONDS);
            driver.manage().timeouts().pageLoadTimeout(DEFAULT_WAIT_FOR_PAGE_LOAD, SECONDS);


            LOG.info("****************** NEW TEST SCENARIO ******************");
            LOG.info("Name: " + scenario.getName());


        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    @After("@MultiBrowserTesting")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            String scenarioName = scenario.getName();
            if (isGrid) {
                //do capture screenshots when running on Grid or Docker
                captureScreenshot(driver, scenarioName, scenario);
            }
            //do capture screenshots when running on the localhost
            //captureScreenshot(driver, scenarioName, scenario);
        }

        // driver.close();
        driver.quit();
        LOG.info(String.format("%s - %s", scenario.getName(), scenario.getStatus().toUpperCase()));
        LOG.info("Total execution time: " + (System.currentTimeMillis() - startTime) + " ms");

    }


    private void setWebDriverBinaryPath(String wdPropName, String windows, String mac, String linux) {
        if (SystemUtils.IS_OS_WINDOWS) {
            System.setProperty(wdPropName, windows);
        } else if (SystemUtils.IS_OS_MAC) {
            System.setProperty(wdPropName, mac);
        } else if (SystemUtils.IS_OS_LINUX) {
            System.setProperty(wdPropName, linux);
        }
    }

    @NotNull
    private WebDriver seleniumGridSetup(DesiredCapabilities dc) throws MalformedURLException {
        WebDriver baseWebDriver;

        dc.setBrowserName(dc.getBrowserName());
        //Selenium Docker is the host for the hub and node/s in this case
        //Set the $(docker-machine ip) as the RemoteWebDriver URL
        if (isDocker) {
            if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_WINDOWS) {
                //local VM for MacOS & Windows
                baseWebDriver = new RemoteWebDriver(new URL("http://192.168.99.100:4444/wd/hub"), dc);
                //VM - aqua1
                //baseWebDriver = new RemoteWebDriver(new URL("http://134.171.18.8:4444/wd/hub"), dc);
                //VM - aqua2
                //baseWebDriver = new RemoteWebDriver(new URL("http://134.171.18.13:4444/wd/hub"), dc);
                //VM - aqua3
                //baseWebDriver = new RemoteWebDriver(new URL("http://134.171.18.22:4444/wd/hub"), dc);
            } else {
                baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
                //VM - aqua1
                //baseWebDriver = new RemoteWebDriver(new URL("http://134.171.18.8:4444/wd/hub"),dc);
                //VM - aqua2
                //baseWebDriver = new RemoteWebDriver(new URL("http://134.171.18.13:4444/wd/hub"), dc);
                //VM - aqua3
                //baseWebDriver = new RemoteWebDriver(new URL("http://134.171.18.22:4444/wd/hub"), dc);
            }

        } else {
            //Selenium Grid is used in this case
            baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
        }


        LOG.info("****************** Tests running on the Selenium " + (isDocker ? "Docker" : "Grid") +
                ", " + dc.getBrowserName().toUpperCase() +
                ", " + SystemUtils.OS_NAME + " ******************");

        return baseWebDriver;
    }

    @NotNull
    private WebDriver firefoxSetup() throws Exception {
        WebDriver baseWebDriver;
        DesiredCapabilities fdc = DesiredCapabilities.firefox();
        String allowedMime = FileUtil.getContent("src/test/resources/allowed-mime.txt");

        fdc.setCapability("platform", ANY);
        fdc.setCapability("nativeEvents", true);


        if ("3".equals(seleniumVersion)) {
            setWebDriverBinaryPath("webdriver.gecko.driver",
                    getProperty("project.properties", "webdriver.firefox.windows"),
                    getProperty("project.properties", "webdriver.firefox.mac"),
                    getProperty("project.properties", "webdriver.firefox.linux"));
            System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
        } else {
            //                    TODO - add the webdriver v2.0 path
        }


        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("browser.download.panel.shown", false);
        options.addPreference("browser.download.animateNotifications", false);
        options.addPreference("browser.download.manager.skipWinSecurityPolicyChecks", true);
        options.addPreference("browser.helperApps.neverAsk.saveToDisk", allowedMime);

        options.addPreference("browser.helperApps.alwaysAsk.force", false);
        options.addPreference("browser.download.manager.showWhenStarting", false);
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.dir", downloadDir);


//        FirefoxProfile firefoxProfile = new FirefoxProfile();
//        firefoxProfile.setPreference("browser.download.panel.shown", false);
//        firefoxProfile.setPreference("browser.download.animateNotifications", false);
//
//
//        //Set Location to store files after downloading.
//        firefoxProfile.setPreference("browser.download.dir", downloadDir);
//        firefoxProfile.setPreference("browser.download.folderList", 2);
//
//        //Set Preference to not show file download confirmation dialogue using MIME types Of different file extension types.
//        firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", allowedMime);
//        firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
//        firefoxProfile.setPreference("pdfjs.disabled", true);
//
//        fdc.setCapability(FirefoxDriver.PROFILE, firefoxProfile); // Profile is based on "options"


        fdc.setCapability("browser.download.panel.shown", false);
        fdc.setCapability("browser.download.animateNotifications", false);


        //Set Location to store files after downloading.
        fdc.setCapability("browser.download.dir", downloadDir);
        fdc.setCapability("browser.download.folderList", 2);

        //Set Preference to not show file download confirmation dialogue using MIME types Of different file extension types.
        fdc.setCapability("browser.helperApps.neverAsk.saveToDisk", allowedMime);
        fdc.setCapability("browser.download.manager.showWhenStarting", false);
        fdc.setCapability("pdfjs.disabled", true);


        return isGrid ? seleniumGridSetup(fdc) : new FirefoxDriver(options);
    }

    @NotNull
    private WebDriver chromeSetup() throws MalformedURLException {

        DesiredCapabilities cdc = DesiredCapabilities.chrome();
        final ChromeOptions options = new ChromeOptions();
        cdc.setCapability("platform", ANY);
        cdc.setCapability("version", ANY);
        cdc.setCapability("webdriver.chrome.args", Collections.singletonList("--whitelisted-ips=127.0.0.1"));
        cdc.setCapability("chrome.switches", Collections.singletonList("--start-maximized"));
        cdc.setCapability("excludeSwitches", Collections.singletonList("test-type"));
        cdc.setCapability("nativeEvents", true);
        options.addArguments("--start-fullscreen");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-web-security=true");
//        options.addArguments("--disable-infobars");

        // The following options should disable the browser native dialog to confirm potentially dangerous downloads,
        // however, it does not work
        //options.addArguments("--safebrowsing-disable-download-protection=true");
        //options.addArguments("--allow-running-insecure-content=true");
        //options.addArguments("--allow-unchecked-dangerous-downloads=true");
//        cdc.setCapability(ChromeOptions.CAPABILITY, options);


        if ("3".equals(seleniumVersion)) {
            setWebDriverBinaryPath("webdriver.chrome.driver",
                    getProperty("project.properties", "webdriver.chrome.windows"),
                    getProperty("project.properties", "webdriver.chrome.mac"),
                    getProperty("project.properties", "webdriver.chrome.linux"));
        } else {
            // TODO - add the webdriver v2.0 path
        }

        LOG.info("****************** Running tests on " +
                SystemUtils.OS_NAME + " on " +
                cdc.getBrowserName().toUpperCase() + " ******************");

        return isGrid ? seleniumGridSetup(cdc) : new ChromeDriver(options);

    }

    @NotNull
    private WebDriver safariSetup() throws MalformedURLException {
        WebDriver baseWebDriver = null;
//        TODO - to be implemented
        return baseWebDriver;
    }

    @NotNull
    private WebDriver operaSetup() throws MalformedURLException {
        WebDriver baseWebDriver = null;
//        TODO - to be implemented
        return baseWebDriver;
    }

    @NotNull
    private WebDriver ieSetup() throws MalformedURLException {
        WebDriver baseWebDriver = null;
//        TODO - to be implemented
        return baseWebDriver;
    }

    @SuppressWarnings("unused")
    @NotNull
    private WebDriver chromeGridSetup(DesiredCapabilities dc) throws MalformedURLException {
        WebDriver baseWebDriver;
        dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
        //Selenium Docker is the host for the hub and node/s in this case
        //Set the $(docker-machine ip) as the RemoteWebDriver URL
        if (isDocker) {
            if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_WINDOWS) {
                baseWebDriver = new RemoteWebDriver(new URL("http://192.168.99.100:4444/wd/hub"), dc);
            } else {
                baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
            }

        } else {
            baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
        }
        return baseWebDriver;
    }

    @SuppressWarnings("unused")
    @NotNull
    private WebDriver firefoxGridSetup(DesiredCapabilities dc) throws MalformedURLException {
        WebDriver baseWebDriver;
        dc.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
        if (isDocker) {
            if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_WINDOWS) {
                baseWebDriver = new RemoteWebDriver(new URL("http://192.168.99.100:4444/wd/hub"), dc);
            } else {
                baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
            }
        } else {
            baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
        }
        return baseWebDriver;
    }

    private void ipRights() {
        //        final JPanel panel = new JPanel();
        //        JOptionPane.showMessageDialog(panel, decrypt(getProperty("license",
        //                "message")), "Warning", JOptionPane.WARNING_MESSAGE);
        //        LOG.info(((char) 27 + "[31m" + decrypt(getProperty("license", "message"))
        //                + (char) 27 + "[0m"));
        LOG.info(decrypt(getProperty("license.properties", "fail")));
        Assert.fail(decrypt(getProperty("license.properties", "fail")));
    }
}
