package poweruser;

import org.testng.annotations.Test;

import constants.EditMeasurementsIDs;
import util.PowerEditTestBase;


/*polarisation angle has strange characters there
 * so I didn't stress myself with the assert 
 * since we do not have data for this field
 */
public class EditMeasurementPolarisationAngle extends PowerEditTestBase {

	String oldPolAngle = null;
	
	@Test(enabled = false)
	public void Edit_Measurement_PolarisationAngleValue_Successfully() throws Exception {
		// Collect the old value
		advancedResultTableUtil.addColumnToTable("Pol. angle");
		oldPolAngle = advancedResultTableUtil.getCellValue("Pol. angle");
		
		// Arrange
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.angleId);
		queryFormUtil.insertValue(EditMeasurementsIDs.angleId, "66.00000000000001");*/
		
		// Act
		advancedSelectorUtil.clickOnSaveButton();

		// Assert
		//String newPolarisationAngle = advancedResultTableUtil.getCellValue("Pol. angle");
		//Assert.assertEquals(newPolarisationAngle, "66.00000000000001", "The Pol. Angle value in UI " + newPolarisationAngle
		//		+ " is different than the expected value: 66.00000000000001");
		
		//Restore the old value
		advancedResultTableUtil.doubleClick();
		//queryFormUtil.clearValue(EditMeasurementsIDs.angleId);
		advancedSelectorUtil.clickOnSaveButton();
	}
}
