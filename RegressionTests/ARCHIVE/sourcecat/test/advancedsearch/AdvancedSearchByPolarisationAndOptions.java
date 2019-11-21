package advancedsearch;

import org.testng.annotations.Test;

import constants.OptionsComponentIDs;
import constants.PolarisationComponentIDs;
import util.TestBase;

/*NOTE
 * We do not have yet values for Polarisation Degree or angle
 */

public class AdvancedSearchByPolarisationAndOptions extends TestBase{

	@Test
	public void Search_FilterByAllCriteriaInPolarisationAndOptions_ReturnsSameNumberOfResultsAsInDB() {
		// polarisation elements
		queryFormUtil.insertValue(PolarisationComponentIDs.degreeId,
				properties.getValidDegree());
		queryFormUtil.insertValue(PolarisationComponentIDs.angleId,
				properties.getValidAngle());

		// options elements
		queryFormUtil.selectOptionInMultiSelectDropDown(
				OptionsComponentIDs.cataloguesId,
				properties.getValidCatalogue());

		queryFormUtil.pressSearchBut();

		resultTableUtil.checkIfResultsAreRetrievedInResultTable("Polarisation & Catalogue");
	}

	@Test
	public void Search_FilterByAllCriteriaInPolarisationANDOptions_ReturnsNOResults() {
		// polarisation elements
		queryFormUtil.insertValue(PolarisationComponentIDs.degreeId,
				properties.getInvalidDegree());
		queryFormUtil.insertValue(PolarisationComponentIDs.angleId,
				properties.getInvalidAngle());

		// options elements
		queryFormUtil.selectOptionInMultiSelectDropDown(
				OptionsComponentIDs.cataloguesId,
				properties.getInvalidCatalogue());

		queryFormUtil.pressSearchBut();

		// get the text from the pop-up
		queryFormUtil.checkNoResultsMessage();
	}
}
