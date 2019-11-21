package util;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class TestBase {
	protected LoadPropFile properties = new LoadPropFile();
	protected WebDriver webDriver;
	protected QueryFormUtils queryFormUtil;
	protected ResultTableUtils resultTableUtil;
	protected DatabaseUtils dbUtil;
	protected PositionParameters positionParam;
	String AppURL = properties.getURL();

	@Parameters({ "platform", "browser" })
	@BeforeMethod
	public void setUp(String platform, String browser) 
			throws MalformedURLException, Exception {

		DesiredCapabilities caps = new DesiredCapabilities();

		// platforms
		if (platform.equalsIgnoreCase("Windows"))
			caps.setPlatform(Platform.WINDOWS);
		if (platform.equalsIgnoreCase("Mac"))
			caps.setPlatform(Platform.MAC);
		if (platform.equalsIgnoreCase("Linux"))
			caps.setPlatform(Platform.LINUX);

		// browser
		if (browser.equalsIgnoreCase("Firefox"))
			caps = DesiredCapabilities.firefox();
		if (browser.equalsIgnoreCase("Chrome"))
			caps = DesiredCapabilities.chrome();
		if (browser.equalsIgnoreCase("Internet Explorer")) {
			caps = DesiredCapabilities.internetExplorer();
			caps.setCapability("ignoreZoomSetting", true);
		}

		// caps.setVersion(version);
		try {
			webDriver = new RemoteWebDriver(new URL(
					"http://134.171.18.124:4444/wd/hub"), caps);
		} catch (UnreachableBrowserException e) {
			System.err.println("Please check the status of the HUB and NODES: "
					+ e.getMessage());
		}

		// webDriver = new RemoteWebDriver(new
		// URL("http://10.200.114.96:4444/wd/hub"), caps);
		webDriver.navigate().to(AppURL);

		queryFormUtil = new QueryFormUtils(webDriver);
		resultTableUtil = new ResultTableUtils(webDriver);
		dbUtil = new DatabaseUtils();
		positionParam = new PositionParameters();
	}

	@AfterMethod
	public void closeBrowser() throws MalformedURLException {
		webDriver.quit();
	}
}
