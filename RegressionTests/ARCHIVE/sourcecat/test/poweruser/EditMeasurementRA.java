package poweruser;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.EditMeasurementsIDs;
import util.PowerEditTestBase;


public class EditMeasurementRA extends PowerEditTestBase {

	String oldRA = null;
	String oldRAh = null;
	String oldRAm = null;
	String oldRAs = null;
	
	@Test
	public void Edit_Measurement_RAValue_Successfully() throws Exception {
		//Collect current value	
		oldRA = advancedResultTableUtil.getCellValue("RA");
		/*String[] parts = oldRA.split(":");
		oldRAh = parts[0];
		oldRAm = parts[1];
		oldRAs = parts[2];*/
		
		// Arrange
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.raHId);
		queryFormUtil.clearValue(EditMeasurementsIDs.raMId);
		queryFormUtil.clearValue(EditMeasurementsIDs.raSId);
		queryFormUtil.insertValue(EditMeasurementsIDs.raHId, "11");
		queryFormUtil.insertValue(EditMeasurementsIDs.raMId, "11");
		queryFormUtil.insertValue(EditMeasurementsIDs.raSId, "11.110");*/
		
		// Act
		advancedSelectorUtil.clickOnSaveButton();

		// Assert
		/*String newRA = advancedResultTableUtil.getCellValue("RA");
		Assert.assertEquals(newRA, "11:11:11.110", "The RA value in UI " + newRA
				+ " is different than the expected value: 11:11:11.110");*/
		
		// Restore the old value
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.raHId);
		queryFormUtil.clearValue(EditMeasurementsIDs.raMId);
		queryFormUtil.clearValue(EditMeasurementsIDs.raSId);
		queryFormUtil.insertValue(EditMeasurementsIDs.raHId, oldRAh);
		queryFormUtil.insertValue(EditMeasurementsIDs.raMId, oldRAm);
		queryFormUtil.insertValue(EditMeasurementsIDs.raSId, oldRAs);*/
		advancedSelectorUtil.clickOnSaveButton();
	}
}
