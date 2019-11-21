package de.aqua.web.browser.setup;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

import de.aqua.web.delegates.WebDriverExtended;

import static de.aqua.web.utils.CaptureScreenshotUtil.captureScreenshot;
import static de.aqua.web.utils.PropertiesFileUtil.getNavigationURL;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.Platform.ANY;

/**
 * Created by bdumitru on 7/12/2017.
 */
public class Selenium2FirefoxAbstractSetup {

    public static WebDriverExtended driver;

    private String baseUrl;
    private String childClassName = this.getClass().getName();
    private long startTime;

    private static Logger LOG = LoggerFactory.getLogger(Selenium2FirefoxAbstractSetup.class);
    private static Logger LOG_TIME = LoggerFactory.getLogger(Selenium2FirefoxAbstractSetup.class);

    @Before("@FirefoxTesting")
    public void setUp() {

        startTime = System.currentTimeMillis();
        String grid = System.getProperty("grid");
        grid = (grid == null) ? "no" : grid;

        LOG.info("****************** Selenium Grid = " + grid + " ******************");
        LOG.info("****************** Running tests on " + System.getProperty("os.name") + " ******************");

        FirefoxProfile ffProfile = new FirefoxProfile();
        ffProfile.setEnableNativeEvents(true);
        WebDriver baseWebDriver;
        try {
            DesiredCapabilities dc = DesiredCapabilities.firefox();
            dc.setCapability("platform", ANY);
            if ("yes".equalsIgnoreCase(grid)) {
                LOG.info("***************** Accessed the remote web driver! *****************");
                LOG.info("***************** Running tests on " + DesiredCapabilities.firefox()
                        .getBrowserName() + " *****************");
                dc.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
                //Selenium Grid
                baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
            } else {
                baseWebDriver = new FirefoxDriver(ffProfile);
                LOG.info("***************** Accessed the local driver! *****************");
            }

            baseWebDriver.manage().window().maximize();
            ((JavascriptExecutor) baseWebDriver).executeScript("window.focus();");
            baseUrl = getNavigationURL("url");

            driver = new WebDriverExtended(baseWebDriver, baseUrl);
            driver.manage().timeouts().implicitlyWait(10, SECONDS);
            driver.manage().timeouts().pageLoadTimeout(60, SECONDS);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @After("@FirefoxTesting")
    public void tearDown(Scenario scenario) {
        String grid = System.getProperty("grid"); //do not capture screenshots when running on the localhost
        if (scenario.isFailed()) {
            if ("yes".equalsIgnoreCase(grid)) {//do not capture screenshots when running on the localhost
                captureScreenshot(driver, childClassName, scenario);
            }
//            captureScreenshot(driver, childClassName, scenario);
        }

//        if (scenario.isFailed()) {
//            final byte[] screenshot = driver.getScreenshot();
//            scenario.embed(screenshot, "target/surefire-reports/screenshot-" + childClassName + "_" + System.currentTimeMillis() + ".png"); //stick it in the report
//        }

//        try {
//            Runtime.getRuntime().exec("pkill firefox");
//        } catch (IOException e) {
//        }

        driver.close();
        LOG_TIME.info(childClassName + ": " + (System.currentTimeMillis() - startTime));
    }
}

