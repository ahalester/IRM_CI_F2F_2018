//todo: This file will be removed
package alma.irm.aq;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import static org.testng.Assert.assertEquals;

public class SimpleTest {
	private WebDriver driver;
	private String target_url;
	private DesiredCapabilities capability = new DesiredCapabilities();

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
			System.out.println(">>Selenium target: Test farm");
			driver =new RemoteWebDriver(new URL("http://testfarm.sco.alma.cl:4444/wd/hub"), capability);
		}

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		if (!test_url.equals("${test_url}"))
			this.target_url = test_url;
		else
			this.target_url = url;
	}

	@BeforeMethod
	public void setupTest() {
		this.driver.get(this.target_url);
	}


	@Test
	public void testCheckPageTitle() throws Exception {
		assertTrue(driver.getTitle().contains("Alma Science Archive Query"));
	}

	@Test
	public void testCheckUIElements() throws Exception {
		Map<String, String> uiElements = new HashMap<String, String>();
		uiElements.put("source_name_resolver", "text");
		uiElements.put("source_name_alma", "text");
		uiElements.put("ra_dec", "text");
		uiElements.put("galactic", "text");
		uiElements.put("targetList", "file");
		uiElements.put("spatial_resolution", "text");
		uiElements.put("spatial_scale_max", "text");
		uiElements.put("frequency", "text");
		uiElements.put("bandwidth", "text");
		uiElements.put("spectral_resolution", "text");
		uiElements.put("band_list", "select-multiple");
		uiElements.put("start_date", "text");
		uiElements.put("integration_time", "text");
		uiElements.put("polarisation_type", "select-multiple");
		uiElements.put("line_sensitivity", "text");
		uiElements.put("continuum_sensitivity", "text");
		uiElements.put("water_vapour", "text");
		uiElements.put("project_code", "text");
		uiElements.put("project_title", "text");
		uiElements.put("pi_name", "text");
		uiElements.put("proposal_authors", "text");
		uiElements.put("project_abstract", "text");
		uiElements.put("publication_count", "text");
		uiElements.put("science_keyword", "select-multiple");
		uiElements.put("bibcode", "text");
		uiElements.put("pub_title", "text");
		uiElements.put("first_author", "text");
		uiElements.put("authors", "text");
		uiElements.put("pub_abstract", "text");
		uiElements.put("publication_year", "text");
		uiElements.put("public_data", "checkbox");
		uiElements.put("science_observations", "checkbox");

		for (Map.Entry<String, String> entry : uiElements.entrySet())
		{
			WebElement element = null;
			try
			{
				element = driver.findElement(By.id(entry.getKey()));
			}
			catch (NoSuchElementException e)
			{
				fail(e.toString());
			}

			if (element != null)
			{
				assertEquals(element.getAttribute("type"), entry.getValue());
			}
		}
	}


	@AfterTest
	public void tearDown() throws Exception {
		driver.quit();
	}

}
