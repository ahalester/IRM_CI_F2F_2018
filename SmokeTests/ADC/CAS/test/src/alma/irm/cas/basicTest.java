package alma.irm.cas;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class basicTest {
	private WebDriver driver;
	private DesiredCapabilities capability = new DesiredCapabilities();

	@Parameters({ "platform", "browser", "url"})
	@BeforeMethod (alwaysRun=true)
	public void setup(String platform, String browser, String url) throws Exception {
				
		//Browsers
		if(browser.equalsIgnoreCase("internet explorer")){
			capability.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
		}

		if(browser.equalsIgnoreCase("firefox")){
			capability.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
		}
		
		if(browser.equalsIgnoreCase("chrome")){
			capability.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
		}
		
		//OS
		if(platform.equalsIgnoreCase("windows"))
			capability.setPlatform(org.openqa.selenium.Platform.WINDOWS);
		
		if(platform.equalsIgnoreCase("mac"))
			capability.setPlatform(org.openqa.selenium.Platform.MAC);
		
		if(platform.equalsIgnoreCase("linux"))
			capability.setPlatform(org.openqa.selenium.Platform.LINUX);
		
		driver =new RemoteWebDriver(new URL("http://10.200.114.96:4444/wd/hub"), capability);

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(url);
	}
	
	@Parameters({ "username", "password" })
	@Test
	public void testLogin(String username, String password) throws Exception {

		// Find the username field element by its name
		WebElement element = driver.findElement(By.name("username"));
		// Enter the provided username
        element.sendKeys(username);
        // Find the username field element by its name
     	element = driver.findElement(By.name("password"));
     	// Enter the provided username
     	element.sendKeys(password);
     	element.submit();
     	//assertEquals(true, (driver.getPageSource().contains("Log In Successful")));
     	Assert.assertEquals(true, driver.getPageSource().contains("Log In Successful"));
	}
	
	@Parameters({ "username", "wrongpassword" })
	@Test
	public void testWrongLogin(String username, String wrongpassword) throws Exception {

		// Find the username field element by its name
		WebElement element = driver.findElement(By.name("username"));
		// Enter the provided username
        element.sendKeys(username);
        // Find the username field element by its name
     	element = driver.findElement(By.name("password"));
     	// Enter the provided username
     	element.sendKeys(wrongpassword);
     	element.submit();
     	//assertEquals(true, (driver.getPageSource().contains("Log In Successful")));
     	Assert.assertEquals(true, driver.getPageSource().contains("The credentials you provided cannot be determined to be authentic."));
	}
	
	 @AfterMethod
	  public void tearDown() throws Exception {
	    driver.quit();
	  }	
}
