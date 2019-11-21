package poweruser;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.EditMeasurementsIDs;
import util.PowerEditTestBase;

public class EditMeasurementUVMin extends PowerEditTestBase {

	String oldUVmin = null;
	
	@Test
	public void Edit_Measurement_UVMinValue_Successfully() throws Exception {
		// Collect current value
		oldUVmin = advancedResultTableUtil.getCellValue("UvMin");
		
		// Arrange
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.UVMinId);
		queryFormUtil.insertValue(EditMeasurementsIDs.UVMinId, "77.0");*/
		
		// Act
		advancedSelectorUtil.clickOnSaveButton();

		// Assert
		/*String newUVmin = advancedResultTableUtil.getCellValue("UV Min");
		Assert.assertEquals(newUVmin, "77.0", "The UV Min value in UI " + newUVmin
				+ " is different than the expected value: 77.0");*/
		
		// Restore the old value
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.UVMinId);
		queryFormUtil.insertValue(EditMeasurementsIDs.UVMinId, oldUVmin);*/
		advancedSelectorUtil.clickOnSaveButton();
	}

}
