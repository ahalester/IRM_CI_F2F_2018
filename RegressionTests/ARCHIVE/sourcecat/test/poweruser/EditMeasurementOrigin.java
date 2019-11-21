package poweruser;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.EditMeasurementsIDs;
import util.PowerEditTestBase;


public class EditMeasurementOrigin extends PowerEditTestBase {

	String oldOrigin = null;
	
	@Test(enabled = false)
	//test is ignored since Origin is not displayed in the result table
	public void Edit_Measurement_OriginValue_Successfully() throws Exception {
		//Collect current value
		oldOrigin = advancedResultTableUtil.getCellValue("Origin");
		
		// Arrange
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.originId);
		queryFormUtil.insertValue(EditMeasurementsIDs.originId, "BLA");*/
		
		// Act
		advancedSelectorUtil.clickOnSaveButton();

		// Assert
		/*String newOrigin = advancedResultTableUtil.getCellValue("Origin");
		Assert.assertEquals(newOrigin, "BLA", "The Frequency value in UI " + newOrigin
				+ " is different than the expected value: BLA");*/
		
		//Restore the old value
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.originId);
		queryFormUtil.insertValue(EditMeasurementsIDs.originId, oldOrigin);*/
		advancedSelectorUtil.clickOnSaveButton();
	}
}
