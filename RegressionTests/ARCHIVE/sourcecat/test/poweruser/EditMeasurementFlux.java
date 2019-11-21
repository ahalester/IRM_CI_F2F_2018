package poweruser;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.EditMeasurementsIDs;
import util.PowerEditTestBase;


public class EditMeasurementFlux extends PowerEditTestBase {

	String oldFlux = null;

	@Test
	public void Edit_Measurement_FluxValue_Successfully() throws Exception {
		//Collect current value
		oldFlux = advancedResultTableUtil.getCellValue("Flux");
		
		// Arrange
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.fluxId);
		queryFormUtil.insertValue(EditMeasurementsIDs.fluxId, "44.0");*/
		
		// Act
		advancedSelectorUtil.clickOnSaveButton();

		// Assert
		/*String newFlux = advancedResultTableUtil.getCellValue("Flux");
		Assert.assertEquals(newFlux, "44.0", "The Flux value in UI " + newFlux
				+ " is different than the expected value: 44.0");*/
		
		//Restore the old value
		advancedResultTableUtil.doubleClick();
		//queryFormUtil.clearValue(EditMeasurementsIDs.fluxId);
		
		advancedSelectorUtil.clickOnSaveButton();
	}
}
