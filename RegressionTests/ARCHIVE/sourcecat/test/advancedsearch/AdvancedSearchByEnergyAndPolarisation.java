package advancedsearch;

import org.testng.annotations.Test;

import util.TestBase;

import constants.PolarisationComponentIDs;
import constants.EnergyComponentIDs;


/*NOTE
 * We do not have yet values for Polarisation Angle
 */

public class AdvancedSearchByEnergyAndPolarisation extends TestBase {

	@Test
	public void Search_FilterByEnergyAndPolarisation_ReturnsSameResultsAsInDB() {
		// Arrange
		queryFormUtil.selectOptionInMultiSelectDropDown(
				EnergyComponentIDs.bandId, properties.getValidBand());
		queryFormUtil.insertValue(EnergyComponentIDs.frequencyId,
				properties.getValidFrequency());
		queryFormUtil.insertValue(EnergyComponentIDs.fluxId,
				properties.getValidFlux());

		queryFormUtil.insertValue(PolarisationComponentIDs.degreeId,
				properties.getValidDegree());
		queryFormUtil.insertValue(PolarisationComponentIDs.angleId,
				properties.getValidAngle());

		// Act
		queryFormUtil.pressSearchBut();

		// Assert
		// I have to do the check -after our db will be populated with data for
		// polarisation elements
		resultTableUtil
				.checkIfResultsAreRetrievedInResultTable("Energy & Polarisation");
	}

	@Test
	public void Search_FilterByEnergyAndPolarisation_ReturnsNOResults() {
		// Arrange
		queryFormUtil.selectOptionInMultiSelectDropDown(
				EnergyComponentIDs.bandId, properties.getInvalidBand());
		queryFormUtil.insertValue(EnergyComponentIDs.frequencyId,
				properties.getInvalidFrequency());
		queryFormUtil.insertValue(EnergyComponentIDs.fluxId,
				properties.getInvalidFlux());

		queryFormUtil.insertValue(PolarisationComponentIDs.degreeId,
				properties.getInvalidDegree());
		queryFormUtil.insertValue(PolarisationComponentIDs.angleId,
				properties.getInvalidAngle());

		// Act
		queryFormUtil.pressSearchBut();

		// Assert
		// I have to do the check -after our db will be populated with data for
		// polarisation elements
	}
}
