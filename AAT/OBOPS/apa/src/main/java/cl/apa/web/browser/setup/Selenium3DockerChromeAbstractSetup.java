package cl.apa.web.browser.setup;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cl.apa.web.delegates.WebDriverExtended;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

import static cl.apa.web.utils.CaptureScreenshotUtil.captureScreenshot;
import static cl.apa.web.utils.PropertiesFileUtil.getNavigationURL;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.Platform.ANY;

/**
 * Created by bdumitru on 7/18/2017.
 */
public class Selenium3DockerChromeAbstractSetup {

    public static WebDriverExtended driver;

    private String baseUrl;
    private String childClassName = this.getClass().getName();
    private long startTime;

    private static Logger LOG = LoggerFactory.getLogger(Selenium3DockerChromeAbstractSetup.class);
    private static Logger LOG_TIME = LoggerFactory.getLogger(Selenium3DockerChromeAbstractSetup.class);

    @Before("@ChromeTesting")
    public void setUp() {

        startTime = System.currentTimeMillis();
        String grid = System.getProperty("grid");
        grid = (grid == null) ? "no" : grid;
        String docker = System.getProperty("docker");

        LOG.info("****************** Selenium Grid = " + grid + " ******************");
        LOG.info("****************** Running tests on " + System.getProperty("os.name") + " ******************");

        WebDriver baseWebDriver;
        try {
            DesiredCapabilities dc = DesiredCapabilities.chrome();
            dc.setCapability("platform", ANY);
            dc.setCapability("version", ANY);
            dc.setCapability("webdriver.chrome.args", Collections.singletonList("--whitelisted-ips=127.0.0.1"));
            dc.setCapability("chrome.switches", Collections.singletonList("--start-maximized"));
            dc.setCapability("excludeSwitches", Collections.singletonList("test-type"));

            System.setProperty("webdriver.chrome.driver",
                    "D://Work//SeleniumGrid//Selenium3.4.0//chromedriver.exe");

            if ("yes".equalsIgnoreCase(grid)) {
//                LOG.info("***************** Accessed the remote web driver! *****************");
                dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
                //Selenium Docker is the host for the hub and node/s in this case
                //Set the $(docker-machine ip) as the RemoteWebDriver URL
                if ("yes".equalsIgnoreCase(docker)) {
                    baseWebDriver = new RemoteWebDriver(new URL("http://192.168.99.100:4444/wd/hub"), dc);
                    LOG.info("****************** Running tests on Selenium Docker host ******************");
                } else {
                    //Selenium Grid is used in this case
                    baseWebDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
                    LOG.info("****************** Running tests on Selenium Grid ******************");
                }
            } else {
                baseWebDriver = new ChromeDriver();
                LOG.info("***************** Accessed the local driver! *****************");
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

    @After("@ChromeTesting")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            captureScreenshot(driver, childClassName, scenario);
        }

//        if (scenario.isFailed()) {
//            final byte[] screenshot = driver.getScreenshot();
//            scenario.embed(screenshot, "target/surefire-reports/screenshot-" + childClassName + "_" + System.currentTimeMillis() + ".png"); //stick it in the report
//        }

//        try {
//            Runtime.getRuntime().exec("pkill chrome");
//        } catch (IOException e) {
//        }

        driver.close();
        LOG_TIME.info(childClassName + ": " + (System.currentTimeMillis() - startTime));
    }
}
