package alma.irm.aq;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.ITestResult;

import alma.irm.aq.AQPageObject;

public class SearchTest {
	private WebDriver driver;
	private String target_url;
	private DesiredCapabilities capability = new DesiredCapabilities();
	private AQPageObject aq;

	@Parameters({ "platform", "browser", "test_url", "url", "selenium_path" })
	@BeforeTest (alwaysRun=true)
	public void setup(String platform, String browser, String test_url, String url, String selenium_path)
			throws Exception {

		//Browsers
		if(browser.equalsIgnoreCase("Internet Explorer")){
			capability.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
		}

		if(browser.equalsIgnoreCase("Firefox")){
			capability.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
		}

		if(browser.equalsIgnoreCase("Chrome")){
			capability.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
		}

		//OS
		if(platform.equalsIgnoreCase("Windows"))
			capability.setPlatform(org.openqa.selenium.Platform.WINDOWS);

		if(platform.equalsIgnoreCase("MAC"))
			capability.setPlatform(org.openqa.selenium.Platform.MAC);

		if(platform.equalsIgnoreCase("linux"))
			capability.setPlatform(org.openqa.selenium.Platform.LINUX);

		//For local test
		if (!selenium_path.equals("${selenium_path}")) {
			System.out.println("Selenium target: "+selenium_path);
			System.setProperty(
					"webdriver.chrome.driver",
					selenium_path);
			driver = new ChromeDriver();
		}
		else {
			System.out.println("Selenium target: Test farm");
			driver =new RemoteWebDriver(new URL("http://testfarm.sco.alma.cl:4444/wd/hub"), capability);
		}

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		if (!test_url.equals("${test_url}"))
			this.target_url = test_url;
		else
			this.target_url = url;
		this.aq = new AQPageObject(driver, this.target_url);

		System.out.println("Test target: "+this.target_url);

	}

	@BeforeMethod
	public void setupTest() {
		this.aq.goHome();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Test Cases
	////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test//(enabled=false)
	public void test_invalid_search_spectral_resolution() throws Exception {
		assertTrue(this.aq.enter_spectral_resolution("1"));
		assertTrue(this.aq.submit_search());
		assertEquals(this.aq.getNum_of_result(), 0);
		//todo: verify alert box
		//Your search did not return any results.
	}

	@Test//(enabled=false)
	public void test_valid_search_observation_date() throws Exception {
		assertTrue(this.aq.enter_observation_date("01-01-2012 .. 01-01-2013"));
		assertTrue(this.aq.submit_search());
		//Production Sites
		assertEquals(this.aq.getNum_of_result(), 511, "Result Query for Production site is different then July test site");
		//Production Sites
		//assertEquals(this.aq.getNum_of_result(), 487);
		//todo: verify results are within range
	}

	@Test//(enabled=false)
	public void test_valid_search_multiple_sections() throws Exception {
		assertTrue(this.aq.enter_publication_year("2014"));
		assertTrue(this.aq.enter_pi_name("Luca"));
		assertTrue(this.aq.enter_frequency("100 .. 110"));
		assertTrue(this.aq.submit_search());
		assertEquals(this.aq.getNum_of_result(), 4);
		//todo: verify results are within range
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////


	@AfterTest
	public void tearDown() throws Exception {
		driver.quit();
	}

	@AfterMethod
	public void getRunTime(ITestResult tr) {
		long time = tr.getEndMillis() - tr.getStartMillis();
		System.out.println("Runtime for this test: "+time);
	}
}

