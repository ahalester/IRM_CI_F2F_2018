package de.project.web.browser.setup;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import de.project.web.delegates.WebDriverExtended;
import org.apache.commons.lang.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

import static de.project.web.utils.CaptureScreenshotUtil.captureScreenshot;
import static de.project.web.utils.EncryptUtil.decrypt;
import static de.project.web.utils.PropertiesFileUtil.getNavigationURL;
import static de.project.web.utils.PropertiesFileUtil.getProperty;
import static de.project.web.utils.StringUtil.keyValidator;
import static de.project.web.utils.StringUtil.dateConverter;
import static de.project.web.utils.WaitUtil.DEFAULT_WAIT_FOR_ELEMENT;
import static de.project.web.utils.WaitUtil.DEFAULT_WAIT_FOR_PAGE_LOAD;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.Platform.ANY;

/**
 * Created by bdumitru on 7/19/2017.
 */
public class Selenium3DockerMultiBrowserAbstractSetup {

    public static WebDriverExtended driver;
    public static Boolean interruption = false;

    private String baseUrl;
    //private String childClassName = this.getClass().getName();
    private long startTime;

    private static Logger LOG = LoggerFactory.getLogger(Selenium3DockerMultiBrowserAbstractSetup.class);
    //private static Logger LOG_TIME = LoggerFactory.getLogger(Selenium3DockerMultiBrowserAbstractSetup.class);

    private DesiredCapabilities dc = new DesiredCapabilities();

    @Before("@MultiBrowserTesting")
    public void setUp(Scenario scenario) {

        startTime = System.currentTimeMillis();
        String grid = System.getProperty("grid");
        grid = (grid == null) ? "no" : grid;
        String docker = System.getProperty("docker");
        String browser = System.getProperty("browser");
        String selenium = System.getProperty("seleniumVersion");

        LOG.info("****************** NEW TEST SCENARIO ******************");
//        LOG.info(((char) 27 + "[31m****************** NEW TEST SCENARIO ******************"
//                + (char) 27 + "[0m"));
        LOG.info(String.format("%s", scenario.getName()));
        LOG.info("****************** Running on Selenium Grid: " + grid + " ******************");

        WebDriver baseWebDriver = null;
        try {
            switch (browser.toLowerCase()) {
                case "chrome": {
                    baseWebDriver = chromeSetup(grid, docker, selenium);
                    break;
                }
                case "firefox": {
                    baseWebDriver = firefoxSetup(grid, docker, selenium);
                    break;
                }
                case "safari": {
                    baseWebDriver = safariSetup(grid, docker, selenium);
                    break;
                }
                case "opera": {
                    baseWebDriver = operaSetup(grid, docker, selenium);
                    break;
                }
                case "ie": {
                    baseWebDriver = ieSetup(grid, docker, selenium);
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

            baseUrl = getNavigationURL("url");
            if ((dateConverter(keyValidator()))
                    .before(dateConverter(decrypt(getProperty("license", "key"))))) {
                driver = new WebDriverExtended(baseWebDriver, baseUrl);
            } else {
                ipRights();
            }
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_FOR_ELEMENT, SECONDS);
            driver.manage().timeouts().pageLoadTimeout(DEFAULT_WAIT_FOR_PAGE_LOAD, SECONDS);
            LOG.info(String.format("Test scenario: '%s'", scenario.getName()));

        } catch (MalformedURLException e) {
            LOG.error(e.getMessage());
        }
    }

    @After("@MultiBrowserTesting")
    public void tearDown(Scenario scenario) {
        String grid = System.getProperty("grid");
        final String docker = System.getProperty("docker");
        if (scenario.isFailed()) {
            String scenarioName = scenario.getName();
            if ("yes".equalsIgnoreCase(grid)) {
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

        if ("yes".equalsIgnoreCase(grid) && "yes".equalsIgnoreCase(docker)) {
//            try {
//                if (dc.getBrowserName().equalsIgnoreCase("chrome")) {
//                    Runtime.getRuntime().exec("pkill chrome");
//                } else if (dc.getBrowserName().equalsIgnoreCase("firefox")) {
//                    Runtime.getRuntime().exec("pkill -f firefox");
//                }
//            } catch (IOException e) {
//                LOG.info("Couldn't kill the existing browser instances!");
//            }
        }
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
    private WebDriver seleniumGridSetup(String docker, DesiredCapabilities dc) throws MalformedURLException {
        WebDriver baseWebDriver;
        LOG.info("***************** Running tests on " + dc.getBrowserName().toUpperCase()
                + " *****************");
        dc.setBrowserName(dc.getBrowserName());
        //Selenium Docker is the host for the hub and node/s in this case
        //Set the $(docker-machine ip) as the RemoteWebDriver URL
        if ("yes".equalsIgnoreCase(docker)) {
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
            LOG.info("****************** Tests running on the Selenium Docker host under "
                    + SystemUtils.OS_NAME + " ******************");
        } else {
            //Selenium Grid is used in this case
            baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
            LOG.info("****************** Tests running on the Selenium Grid under "
                    + SystemUtils.OS_NAME + " ******************");
        }
        return baseWebDriver;
    }

    @NotNull
    private WebDriver firefoxSetup(String grid, String docker, String selenium) throws MalformedURLException {
        WebDriver baseWebDriver;
        DesiredCapabilities fdc = DesiredCapabilities.firefox();
        dc = fdc;
        fdc.setCapability("platform", ANY);
        fdc.setCapability("nativeEvents", true);

        if ("3".equals(selenium)) {
            setWebDriverBinaryPath("webdriver.gecko.driver",
                    getProperty("webdriver.firefox.windows"),
                    getProperty("webdriver.firefox.mac"),
                    getProperty("webdriver.firefox.linux"));
            System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
        } else {
            //                    TODO - add the webdriver v2.0 path
        }

        if ("yes".equalsIgnoreCase(grid)) {
            baseWebDriver = seleniumGridSetup(docker, fdc);
        } else {
            baseWebDriver = new FirefoxDriver(dc);
            baseWebDriver.manage().window().maximize();
            LOG.info("****************** Running tests on the localhost under "
                    + SystemUtils.OS_NAME + " on " + fdc.getBrowserName().toUpperCase()
                    + " ******************");
        }
        return baseWebDriver;
    }

    @NotNull
    private WebDriver chromeSetup(String grid, String docker, String selenium) throws MalformedURLException {
        WebDriver baseWebDriver;
        DesiredCapabilities cdc = DesiredCapabilities.chrome();
        dc = cdc;
        final ChromeOptions options = new ChromeOptions();
        cdc.setCapability("platform", ANY);
        cdc.setCapability("version", ANY);
        cdc.setCapability("webdriver.chrome.args", Collections.singletonList("--whitelisted-ips=127.0.0.1"));
        cdc.setCapability("chrome.switches", Collections.singletonList("--start-maximized"));
        cdc.setCapability("excludeSwitches", Collections.singletonList("test-type"));
        cdc.setCapability("nativeEvents", true);
        options.addArguments("--start-fullscreen");
        options.addArguments("--start-maximized");
        cdc.setCapability(ChromeOptions.CAPABILITY, options);

        if ("3".equals(selenium)) {
            setWebDriverBinaryPath("webdriver.chrome.driver",
                    getProperty("webdriver.chrome.windows"),
                    getProperty("webdriver.chrome.mac"),
                    getProperty("webdriver.chrome.linux"));
        } else {
            //                    TODO - add the webdriver v2.0 path
        }

        if ("yes".equalsIgnoreCase(grid)) {
            baseWebDriver = seleniumGridSetup(docker, cdc);
        } else {
            baseWebDriver = new ChromeDriver(dc);
            //baseWebDriver = new ChromeDriver(options);
            LOG.info("****************** Running tests on the localhost under "
                    + SystemUtils.OS_NAME + " on " + cdc.getBrowserName().toUpperCase()
                    + " ******************");
        }
        return baseWebDriver;
    }

    @NotNull
    private WebDriver safariSetup(String grid, String docker, String selenium) throws MalformedURLException {
        WebDriver baseWebDriver = null;
//        TODO - to be implemented
        return baseWebDriver;
    }

    @NotNull
    private WebDriver operaSetup(String grid, String docker, String selenium) throws MalformedURLException {
        WebDriver baseWebDriver = null;
//        TODO - to be implemented
        return baseWebDriver;
    }

    @NotNull
    private WebDriver ieSetup(String grid, String docker, String selenium) throws MalformedURLException {
        WebDriver baseWebDriver = null;
//        TODO - to be implemented
        return baseWebDriver;
    }

    @SuppressWarnings("unused")
    @NotNull
    private WebDriver chromeGridSetup(String docker, DesiredCapabilities dc) throws MalformedURLException {
        WebDriver baseWebDriver;
        LOG.info("***************** Running tests on " + DesiredCapabilities.chrome()
                .getBrowserName().toUpperCase() + " " + DesiredCapabilities.chrome().getVersion()
                + " *****************");
        dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
        //Selenium Docker is the host for the hub and node/s in this case
        //Set the $(docker-machine ip) as the RemoteWebDriver URL
        if ("yes".equalsIgnoreCase(docker)) {
            if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_WINDOWS) {
                baseWebDriver = new RemoteWebDriver(new URL("http://192.168.99.100:4444/wd/hub"), dc);
            } else {
                baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
            }
            LOG.info("****************** Running tests on Selenium Docker host under "
                    + SystemUtils.OS_NAME + " ******************");
        } else {
            //Selenium Grid is used in this case
            baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
            LOG.info("****************** Running tests on Selenium Grid under "
                    + SystemUtils.OS_NAME + " ******************");
        }
        return baseWebDriver;
    }

    @SuppressWarnings("unused")
    @NotNull
    private WebDriver firefoxGridSetup(String docker, DesiredCapabilities dc) throws MalformedURLException {
        WebDriver baseWebDriver;
        LOG.info("***************** Running tests on " + DesiredCapabilities.firefox()
                .getBrowserName().toUpperCase() + " " + DesiredCapabilities.firefox().getVersion()
                + " *****************");
        dc.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
        if ("yes".equalsIgnoreCase(docker)) {
            if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_WINDOWS) {
                baseWebDriver = new RemoteWebDriver(new URL("http://192.168.99.100:4444/wd/hub"), dc);
            } else {
                baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
            }
            LOG.info("****************** Running tests on Selenium Docker host under "
                    + SystemUtils.OS_NAME + " ******************");
        } else {
            //Selenium Grid is used in this case
            baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
            LOG.info("****************** Running tests on Selenium Grid under "
                    + SystemUtils.OS_NAME + " ******************");
        }
        return baseWebDriver;
    }

    private void ipRights() {
        //        final JPanel panel = new JPanel();
        //        JOptionPane.showMessageDialog(panel, decrypt(getProperty("license",
        //                "message")), "Warning", JOptionPane.WARNING_MESSAGE);
        //        LOG.info(((char) 27 + "[31m" + decrypt(getProperty("license", "message"))
        //                + (char) 27 + "[0m"));
        LOG.info(decrypt(getProperty("license", "fail")));
        Assert.fail(decrypt(getProperty("license", "fail")));
    }
}
