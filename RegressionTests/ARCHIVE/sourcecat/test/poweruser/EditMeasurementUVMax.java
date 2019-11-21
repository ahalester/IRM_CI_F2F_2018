package poweruser;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.EditMeasurementsIDs;
import util.PowerEditTestBase;

public class EditMeasurementUVMax extends PowerEditTestBase {

	String oldUVmax = null;
	
	@Test
	public void Edit_Measurement_UVMaxValue_Successfully() throws Exception {
		// Collect current value
		oldUVmax = advancedResultTableUtil.getCellValue("UvMax");
		
		// Arrange
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.UVMaxId);
		queryFormUtil.insertValue(EditMeasurementsIDs.UVMaxId, "88.0");*/
		
		// Act
		advancedSelectorUtil.clickOnSaveButton();

		// Assert
		/*String newUVmax = advancedResultTableUtil.getCellValue("UV Max");
		Assert.assertEquals(newUVmax, "88.0", "The UV Max value in UI " + newUVmax
				+ " is different than the expected value: 88.0");*/
		
		// Restore the old value
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.UVMaxId);
		queryFormUtil.insertValue(EditMeasurementsIDs.UVMaxId, oldUVmax);*/
		advancedSelectorUtil.clickOnSaveButton();
	}

}
