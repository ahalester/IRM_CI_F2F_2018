package simplesearch;

import org.testng.annotations.Test;

import constants.PolarisationComponentIDs;
import util.TestBase;

/*NOTE
 * We do not have yet values for Polarisation Angle
 */

public class SimpleSearchByPolarisationAngle extends TestBase{

	@Test
	public void Search_FilterByPolarisationAngle_ReturnsSameNumberOfRowsAsInDB() {
		// Arrange
		queryFormUtil.insertValue(PolarisationComponentIDs.angleId,
				properties.getValidAngle());

		// Act
		queryFormUtil.pressSearchBut();

		// Assert
		resultTableUtil
				.checkIfResultsAreRetrievedInResultTable("polarisation angle min & max");
	}

	@Test
	public void Search_FilterByPolarisationAngleMinMax_ReturnsNOResults() {
		// Arrange
		queryFormUtil.insertValue(PolarisationComponentIDs.angleId,
				properties.getValidAngle());

		// Act

		// Assert
		// a check should be added after we'll have some date in db
	}
}
