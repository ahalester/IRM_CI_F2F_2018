package alma.irm.protrack;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;




public class SimpleTest {
	private WebDriver driver;
	private DesiredCapabilities capability = new DesiredCapabilities();

	@Parameters({ "platform", "browser", "url" })
	@BeforeTest (alwaysRun=true)
	public void setup(String platform, String browser, String url) throws Exception {

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

		driver =new RemoteWebDriver(new URL("http://testfarm.sco.alma.cl:4444/wd/hub"), capability);

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(url);
	}

	@Parameters({ "url" })
	@Test
	public void testCheckPageTitle() throws Exception {
		assertTrue(driver.getTitle().contains("ALMA Central Authentication Service"));
	}

	@AfterTest
	public void tearDown() throws Exception {
		driver.quit();
	}

}
