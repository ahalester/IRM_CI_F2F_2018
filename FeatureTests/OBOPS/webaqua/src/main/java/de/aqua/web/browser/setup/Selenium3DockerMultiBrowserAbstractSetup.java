package de.aqua.web.browser.setup;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import de.aqua.web.delegates.WebDriverExtended;
import org.apache.commons.lang.SystemUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

import static de.aqua.web.utils.CaptureScreenshotUtil.captureScreenshot;
import static de.aqua.web.utils.PropertiesFileUtil.getNavigationURL;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.Platform.ANY;

/**
 * Created by bdumitru on 7/19/2017.
 */
public class Selenium3DockerMultiBrowserAbstractSetup {

    public static WebDriverExtended driver;

    private String baseUrl;
    private String childClassName = this.getClass().getName();
    private long startTime;

    private static Logger LOG = LoggerFactory.getLogger(Selenium3DockerMultiBrowserAbstractSetup.class);
//    private static Logger LOG_TIME = LoggerFactory.getLogger(Selenium3DockerMultiBrowserAbstractSetup.class);

    @Before("@MultiBrowserTesting")
    public void setUp() {

        startTime = System.currentTimeMillis();
        String grid = System.getProperty("grid");
        grid = (grid == null) ? "no" : grid;
        String docker = System.getProperty("docker");
        String browser = System.getProperty("browser");

        LOG.info("****************** Selenium Grid = " + grid + " ******************");

        WebDriver baseWebDriver = null;
        try {
            if ("chrome".equalsIgnoreCase(browser)) {
                DesiredCapabilities dc = DesiredCapabilities.chrome();
                dc.setCapability("platform", ANY);
                dc.setCapability("version", ANY);
                dc.setCapability("webdriver.chrome.args", Collections.singletonList("--whitelisted-ips=127.0.0.1"));
                dc.setCapability("chrome.switches", Collections.singletonList("--start-maximized"));
                dc.setCapability("excludeSwitches", Collections.singletonList("test-type"));

                System.setProperty("webdriver.chrome.driver",
                        "D://Work//SeleniumGrid//Selenium3.4.0//chromedriver.exe");

                if ("yes".equalsIgnoreCase(grid)) {
                    dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
                    LOG.info("***************** Running tests on " + DesiredCapabilities.chrome()
                            .getBrowserName().toUpperCase() + " *****************");
                    //Selenium Docker is the host for the hub and node/s in this case
                    //Set the $(docker-machine ip) as the RemoteWebDriver URL
                    if ("yes".equalsIgnoreCase(docker)) {
                        if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_WINDOWS)
                            baseWebDriver = new RemoteWebDriver(new URL("http://192.168.99.100:4444/wd/hub"), dc);
                        else
                            baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
                        LOG.info("****************** Running tests on Selenium Docker host under "
                                + SystemUtils.OS_NAME + " ******************");
                    } else {
                        //Selenium Grid is used in this case
                        baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
                        LOG.info("****************** Running tests on Selenium Grid under "
                                + SystemUtils.OS_NAME + " ******************");
                    }
                } else {
                    baseWebDriver = new ChromeDriver(dc);
                    LOG.info("****************** Running tests on the localhost under "
                            + SystemUtils.OS_NAME + " on " + DesiredCapabilities.chrome()
                            .getBrowserName().toUpperCase() + " ******************");
                }
            } else if ("firefox".equalsIgnoreCase(browser)) {
                FirefoxProfile ffProfile = new FirefoxProfile();
                ffProfile.setEnableNativeEvents(true);
                DesiredCapabilities dc = DesiredCapabilities.firefox();
                dc.setCapability("platform", ANY);

                System.setProperty("webdriver.gecko.driver",
                        "D://Work//SeleniumGrid//Selenium3.4.0//geckodriver.exe");

                if ("yes".equalsIgnoreCase(grid)) {
                    LOG.info("***************** Running tests on " + DesiredCapabilities.firefox()
                            .getBrowserName().toUpperCase() + " *****************");
                    dc.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
                    if ("yes".equalsIgnoreCase(docker)) {
                        if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_WINDOWS)
                            baseWebDriver = new RemoteWebDriver(new URL("http://192.168.99.100:4444/wd/hub"), dc);
                        else
                            baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
                        LOG.info("****************** Running tests on Selenium Docker host under "
                                + SystemUtils.OS_NAME + " ******************");
                    } else {
                        //Selenium Grid is used in this case
                        baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
                        LOG.info("****************** Running tests on Selenium Grid under "
                                + SystemUtils.OS_NAME + " ******************");
                    }
                } else {
                    baseWebDriver = new FirefoxDriver(ffProfile);
                    LOG.info("****************** Running tests on the localhost under "
                            + SystemUtils.OS_NAME + " on " + DesiredCapabilities.firefox()
                            .getBrowserName().toUpperCase() + " ******************");
                }
            }

            baseWebDriver.manage().window().maximize();
            ((JavascriptExecutor) baseWebDriver).executeScript("window.focus();");
            baseUrl = getNavigationURL("url");

            driver = new WebDriverExtended(baseWebDriver, baseUrl);
            driver.manage().timeouts().implicitlyWait(10, SECONDS);
            driver.manage().timeouts().pageLoadTimeout(15, SECONDS);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @After("@MultiBrowserTesting")
    public void tearDown(Scenario scenario) {
        String grid = System.getProperty("grid");
        if (scenario.isFailed()) {
            String scenarioName = scenario.getName();
            if ("yes".equalsIgnoreCase(grid)) {
                //do capture screenshots when running on Grid or Docker
                captureScreenshot(driver, scenarioName, scenario);
            }

            //do capture screenshots when running on the localhost
//            captureScreenshot(driver, scenarioName, scenario);
        }

//        if (scenario.isFailed()) {
//            final byte[] screenshot = driver.getScreenshot();
//            scenario.embed(screenshot, "target/surefire-reports/screenshot-" + childClassName + "_" + System.currentTimeMillis() + ".png"); //stick it in the report
//        }

//        try {
//            Runtime.getRuntime().exec("pkill chrome");
//            Runtime.getRuntime().exec("pkill firefox");
//        } catch (IOException e) {
//        }

        driver.close();
        LOG.info(childClassName + ": " + (System.currentTimeMillis() - startTime) + " ms");
    }
}
