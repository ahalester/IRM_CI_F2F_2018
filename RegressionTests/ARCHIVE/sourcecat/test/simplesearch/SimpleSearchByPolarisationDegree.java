package simplesearch;

import org.testng.annotations.Test;

import constants.PolarisationComponentIDs;
import util.TestBase;

/*NOTE
 * We do not have yet values for Polarisation Degree
 */

public class SimpleSearchByPolarisationDegree extends TestBase {

	@Test
	public void Search_FilterByPolarisationDegree_ReturnsSameNumberOfResultsAsInDB() {
		// Arrange
		queryFormUtil.insertValue(PolarisationComponentIDs.degreeId,
				properties.getValidDegree());
		// Act
		queryFormUtil.pressSearchBut();

		// Assert
		resultTableUtil
				.checkIfResultsAreRetrievedInResultTable("polarisation degree min & max");
	}

	@Test
	public void Search_FilterByPolarisationDegreeMinMax_ReturnsNoResults() {
		//Arrange
		queryFormUtil.insertValue(PolarisationComponentIDs.degreeId,
				properties.getInvalidDegree());
		//Act
		
		//Assert
		// a check should be added after we'll have some date in db
	}
}
