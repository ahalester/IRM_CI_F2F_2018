package poweruser;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.EditMeasurementsIDs;
import util.PowerEditTestBase;


public class EditMeasurementPolarisationDegree extends PowerEditTestBase {

	String oldPolDegree = null;

	@Test(enabled = false)
	public void Edit_Measurement_PolarisationDegreeValue_Successfully() throws Exception {
		// Collect current value
		advancedResultTableUtil.addColumnToTable("Pol. degree");
		oldPolDegree = advancedResultTableUtil.getCellValue("Pol. degree");
		
		// Arrange
		advancedResultTableUtil.doubleClick();
		queryFormUtil.clearValue(EditMeasurementsIDs.decId);
		queryFormUtil.insertValue(EditMeasurementsIDs.decId, "55.00000000000001");
		
		// Act
		advancedSelectorUtil.clickOnSaveButton();

		// Assert
		String newPolarisationDegree = advancedResultTableUtil.getCellValue("Pol. degree");
		Assert.assertEquals(newPolarisationDegree, "55.00000000000001", "The Pol. Degree value in UI " + newPolarisationDegree
				+ " is different than the expected value: 55.00000000000001");
		
		// Restore the old value
		advancedResultTableUtil.doubleClick();
		//queryFormUtil.clearValue(EditMeasurementsIDs.decId);
		advancedSelectorUtil.clickOnSaveButton();
	}
			
}
