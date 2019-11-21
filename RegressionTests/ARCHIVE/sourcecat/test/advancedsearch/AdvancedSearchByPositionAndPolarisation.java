package advancedsearch;

import org.testng.annotations.Test;

import constants.PolarisationComponentIDs;
import constants.PositionComponentIDs;
import util.TestBase; 

/*NOTE
 * We do not have yet values for Polarisation Angle
 */

public class AdvancedSearchByPositionAndPolarisation extends TestBase {

	@Test
	public void Search_FilterByAllCriteriaInPositionANDPolarisation_ReturnsSameResultsAsInDB() {
		// Arrange
		queryFormUtil.insertValue(PositionComponentIDs.sourceNameId,
				properties.getValidSourceNameSESAME());
		queryFormUtil.waitForAutocomplete(PositionComponentIDs.searchRadiusId);
		queryFormUtil.insertValue(PositionComponentIDs.uvId,
				properties.getValidUV());

		// polarisation elements - we do not have values yet in db
		queryFormUtil.insertValue(PolarisationComponentIDs.degreeId,
				properties.getValidDegree());
		queryFormUtil.insertValue(PolarisationComponentIDs.angleId,
				properties.getValidAngle());
		
		// Act
		queryFormUtil.pressSearchBut();
		
		// Assert
		resultTableUtil.checkIfResultsAreRetrievedInResultTable("Position & Polarisation");
	}

	@Test
	public void Search_FilterByAllCriteriaInPositionANDPolarisation_ReturnsNOResults() {
		// Arrange
		queryFormUtil.insertValue(PositionComponentIDs.sourceNameId,
				properties.getInvalidSourceNameSESAME());
		queryFormUtil.waitForAutocomplete(PositionComponentIDs.searchRadiusId);
		queryFormUtil.insertValue(PositionComponentIDs.uvId,
				properties.getInvalidUV());

		// polarisation elements - we do not have values yet in db
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
