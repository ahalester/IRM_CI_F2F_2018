package util;

import java.net.MalformedURLException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class PowerCreateTestBase extends TestBase {
	protected AuthenticationUtils authenticationUtil;
	protected AdvancedSelectorUtils advancedSelectorUtil;
	protected AdvancedResultTableUtils advancedResultTableUtil;
	protected TearDownUtils tearDownUtil;

	@Parameters({ "platform", "browser"})
	@BeforeMethod
	@Override
	public void setUp(String platform, String browser)
			throws MalformedURLException, Exception {
		super.setUp(platform, browser);

		authenticationUtil = new AuthenticationUtils(webDriver);
		authenticationUtil.loginPowerUser();

		advancedSelectorUtil = new AdvancedSelectorUtils(webDriver);
		tearDownUtil = new TearDownUtils(webDriver);
		advancedResultTableUtil = new AdvancedResultTableUtils(webDriver);
	}
}
