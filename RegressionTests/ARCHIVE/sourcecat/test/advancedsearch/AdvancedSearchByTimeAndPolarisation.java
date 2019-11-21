package advancedsearch;

import org.testng.annotations.Test;

import constants.PolarisationComponentIDs;
import constants.TimeComponentIDs;
import util.TestBase;

/*NOTE
 * We do not have yet values for Polarisation Degree or angle
 */

public class AdvancedSearchByTimeAndPolarisation extends TestBase {

	@Test
	public void Search_FilterByAllCriteriaInTimeANDPolarisation_ReturnsSameNumberOfResultsAsInDB() {
		// Arrange
		queryFormUtil.insertValue(TimeComponentIDs.timeAfterId,
				properties.getValidTimeAfter());
		queryFormUtil.insertValue(TimeComponentIDs.timeBeforeId,
				properties.getValidTimeBefore());

		queryFormUtil.insertValue(PolarisationComponentIDs.degreeId,
				properties.getValidDegree());

		// Act
		queryFormUtil.pressSearchBut();

		// Assert
		resultTableUtil
				.checkIfResultsAreRetrievedInResultTable("Time & Polarisation");
	}

	@Test
	public void Search_FilterByAllCriteriaInTimeANDPolarisation_ReturnsNOResults() {
		// Arrange
		queryFormUtil.insertValue(TimeComponentIDs.timeAfterId,
				properties.getInvalidTimeAfter());
		queryFormUtil.insertValue(TimeComponentIDs.timeBeforeId,
				properties.getInvalidTimeBefore());

		queryFormUtil.insertValue(PolarisationComponentIDs.degreeId,
				properties.getInvalidDegree());
		queryFormUtil.insertValue(PolarisationComponentIDs.angleId,
				properties.getInvalidAngle());

		// Act
		queryFormUtil.pressSearchBut();

		// Assert
		queryFormUtil.checkNoResultsMessage();
	}
}
