package poweruser;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.EditMeasurementsIDs;
import util.PowerEditTestBase;

public class EditMeasurementFrequency extends PowerEditTestBase {

	String oldFreq = null;
	
	@Test
	public void Edit_Measurement_FrequencyValue_Successfully() throws Exception {
		// Collect old value
		oldFreq = advancedResultTableUtil.getCellValue("Freq.");
		
		// Arrange
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.freqId);
		queryFormUtil.insertValue(EditMeasurementsIDs.freqId, "33.0");*/
		
		// Act
		advancedSelectorUtil.clickOnSaveButton();

		// Assert
		/*String newFreq = advancedResultTableUtil.getCellValue("Freq.");
		Assert.assertEquals(newFreq, "33.0", "The Frequency value in UI " + newFreq
				+ " is different than the expected value: 33.0");*/
		
		//Restore the old value
		advancedResultTableUtil.doubleClick();
		//queryFormUtil.clearValue(EditMeasurementsIDs.freqId);
		advancedSelectorUtil.clickOnSaveButton();
	}
}
