package util;

import java.net.MalformedURLException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import constants.EnergyComponentIDs;
import constants.OptionsComponentIDs;

public class PowerEditTestBase extends PowerCreateTestBase {

	@Parameters({ "platform", "browser"})
	@BeforeMethod
	@Override
	public void setUp(String platform, String browser)
			throws MalformedURLException, Exception {
		super.setUp(platform, browser);

		queryFormUtil.selectOptionInMultiSelectDropDown(
				OptionsComponentIDs.cataloguesId,
				properties.getValidCatalogue());
		queryFormUtil.selectOptionInMultiSelectDropDown(EnergyComponentIDs.bandId,
				properties.getValidBand());
		
		queryFormUtil.pressSearchBut();
	}
}
