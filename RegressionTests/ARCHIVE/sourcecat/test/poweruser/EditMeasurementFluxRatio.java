package poweruser;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.EditMeasurementsIDs;
import util.PowerEditTestBase;

public class EditMeasurementFluxRatio extends PowerEditTestBase {

	String oldFluxRatio = null;
		
	@Test(enabled = false)
	//test is ignored since flux ration doesn't appear anymore in UI
	public void Edit_Measurement_FluxRatioValue_Successfully() throws Exception {
		// Collect current value
		oldFluxRatio = advancedResultTableUtil.getCellValue("Flux ratio");
		
		// Arrange
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.fluxRatioId);
		queryFormUtil.insertValue(EditMeasurementsIDs.fluxRatioId, "88.0");*/
		
		// Act
		advancedSelectorUtil.clickOnSaveButton();

		// Assert
		/*String newFluxRatio = advancedResultTableUtil.getCellValue("Flux ratio");
		Assert.assertEquals(newFluxRatio, "88.0", "The Flux ratio value in UI " + newFluxRatio
				+ " is different than the expected value: 88.0");*/
		
		//Restore the old value
		advancedResultTableUtil.doubleClick();
		/*queryFormUtil.clearValue(EditMeasurementsIDs.fluxRatioId);
		queryFormUtil.insertValue(EditMeasurementsIDs.fluxRatioId, oldFluxRatio);
		advancedSelectorUtil.clickOnSaveButton();*/
	}
}
