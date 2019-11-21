package poweruser;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.EditMeasurementsIDs;
import util.PowerEditTestBase;

public class EditMeasurementDEC extends PowerEditTestBase {
	String oldDEC = null;
	String oldDECd = null;
	String oldDECm = null;
	String oldDECs = null;
	
	@Test
	public void Edit_Measurement_DECValue_Successfully() throws Exception {
		// Collect current value
		oldDEC = advancedResultTableUtil.getCellValue("DEC");
		String[] parts = oldDEC.split(":");
		oldDECd = parts[0];
		oldDECm = parts[1];
		oldDECs = parts[2];
		
		// Arrange
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.decDId);
		queryFormUtil.clearValue(EditMeasurementsIDs.decMId);
		queryFormUtil.clearValue(EditMeasurementsIDs.decSId);
		queryFormUtil.insertValue(EditMeasurementsIDs.decDId, "22");
		queryFormUtil.insertValue(EditMeasurementsIDs.decMId, "22");
		queryFormUtil.insertValue(EditMeasurementsIDs.decSId, "22.220");*/
		
		// Act
		advancedSelectorUtil.clickOnSaveButton();

		// Assert
		/*String newDEC = advancedResultTableUtil.getCellValue("DEC");
		Assert.assertEquals(newDEC, "+22:22:22.220", "The DEC value in UI " + newDEC
				+ " is different than the expected value: +22:22:22.220");*/
		
		// Restore the old value
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.decDId);
		queryFormUtil.clearValue(EditMeasurementsIDs.decMId);
		queryFormUtil.clearValue(EditMeasurementsIDs.decSId);
		queryFormUtil.insertValue(EditMeasurementsIDs.decDId, oldDECd);
		queryFormUtil.insertValue(EditMeasurementsIDs.decMId, oldDECm);
		queryFormUtil.insertValue(EditMeasurementsIDs.decSId, oldDECs);*/
		
		advancedSelectorUtil.clickOnSaveButton();
	}

}
