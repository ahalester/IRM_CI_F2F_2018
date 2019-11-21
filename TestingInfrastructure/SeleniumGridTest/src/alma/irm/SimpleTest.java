package alma.irm;
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
	
	@Test
	public void testGoogle() throws Exception {
		// go to google.com
		//driver.get(baseUrl + "");
		// Find the text input element by its name
		WebElement element = driver.findElement(By.name("q"));
		// Enter something to search for
        element.sendKeys("Cheese!");

        // Now submit the form. WebDriver will find the form for us from the element
        element.submit();
        
        // Check the title of the page (print instead of assert as of now
        System.out.println("Page title is: " + driver.getTitle() + " on " + driver );
        
        // Google's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("cheese!");
            }
        });
        
        // Should see: "cheese! - Google Search"
        //System.out.println("Page title is: " + driver.getTitle() + " on " + driver);

	}
	
	  @AfterTest
	  public void tearDown() throws Exception {
	    driver.quit();
	  }	
	
}
