package poweruser;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.EnergyComponentIDs;
import constants.OptionsComponentIDs;
import constants.PositionComponentIDs;
import util.PositionParameters.PositionalSearchParameters;
import util.PowerCreateTestBase;
import util.PositionParameters;

public class SearchForMeasurement extends PowerCreateTestBase {
		
	@Test
	public void Search_ForMeasurement_ReturnsSameResultsAsInDB() throws Exception{
		//Arrange
		
		queryFormUtil.selectOptionInMultiSelectDropDown(
				OptionsComponentIDs.cataloguesId,
				properties.getValidCatalogue());
		queryFormUtil.insertValue(PositionComponentIDs.sourceNameId, properties.getValidSourceNameSESAME());
		queryFormUtil.waitForAutocomplete(PositionComponentIDs.raId);
		queryFormUtil.waitForAutocomplete(PositionComponentIDs.decId);
		queryFormUtil.waitForAutocomplete(PositionComponentIDs.searchRadiusId);
		queryFormUtil.selectOptionInMultiSelectDropDown(EnergyComponentIDs.bandId,
				properties.getValidBand());
		
		double ra = positionParam.hhmmss2decimalsRA(queryFormUtil.extractRAValue());
		double dec = positionParam.hhmmss2decimalsDEC(queryFormUtil.extractDecValue());
		double radius = Double.parseDouble(queryFormUtil.extractRadiusValue());
		PositionalSearchParameters params = PositionParameters
				.calculatePositionalSearchParameters(ra, dec, radius);
		double ra_radians = Math.toRadians(ra);
		double dec_radians = Math.toRadians(dec);
		final double toRadians = Math.PI / 180.0;
		double radiusS = Math.sin(Math.toRadians(radius / 2.0));
		String query = "SELECT count(measurement_id) "
				+ " FROM "
				+ "  (SELECT q.*, "
				+ "    row_number() over (partition BY source_id, bandName order by date_observed DESC, measurement_id DESC) source_band_rownum                                                                                                                     "
				+ "  FROM                                                                                                                                                                                                                                           "
				+ "    ( SELECT DISTINCT measurement_id, catalogue_id, m1.source_id, ra, ra_uncertainty, DEC, dec_uncertainty, frequency, flux, flux_uncertainty,                                                                                                   "
				+ "      degree, degree_uncertainty, angle, angle_uncertainty, extension,fluxratio,origin,date_observed,date_created, valid,uvmin,uvmax, COALESCE(r.range_name, 'non-ALMA Band') AS bandName                                                        "
				+ "    FROM measurements m1                                                                                                                                                                                                                         "
				+ "    LEFT JOIN source_type t                                                                                                                                                                                                                      "
				+ "    ON t.source_id = m1.source_id                                                                                                                                                                                                                "
				+ "    LEFT JOIN source_name n                                                                                                                                                                                                                      "
				+ "    ON n.source_id = m1.source_id                                                                                                                                                                                                                "
				+ "    LEFT JOIN names USING (name_id)                                                                                                                                                                                                              "
				+ "    LEFT JOIN ranges r                                                                                                                                                                                                                           "
				+ "    ON m1.frequency BETWEEN r.frequency_min AND r.frequency_max                                                                                                                                                                                  "
				+ "    WHERE (1          = 1)"
				+ "    AND (type_id      = " + dbUtil.getSourceTypeId() + ")                                                                                                                                "
				+ "    AND ((ra          >= " + params.raMin1 + "                                                                                                                 "
				+ "    AND ra            <= " + params.raMax1 + " )                                                                                                                    "
				+ "    OR (ra            >= " + params.raMin2 + "                                                                                                                                 "
				+ "    AND ra            <= " + params.raMax2 + " ))                                                                                                               "
				+ "    AND DEC           >= " + params.decMin + "                                                                                                                "
				+ "    AND DEC           <= " + params.decMax + "                                                                                                                      "
				+ "    AND SQRT( POWER(SIN((" + dec_radians + " - DEC * " + toRadians + ") / 2), 2) + COS(" + dec_radians + ") * COS(DEC * " + toRadians + ") * POWER(SIN((" + ra_radians +" - ra * " + toRadians + ") / 2), 2) ) <= " + radiusS +" "
                + "AND ((1 = 0) OR (r.range_name =" + "'" + properties.getValidBand() + "'" + "))"
				+ "    ) q                                                                                                                                                                                                                                          "
				+ "  ) part                                                                                                                                                                                                                                         "
				+ "WHERE (date_observed         > to_date('01-01-2013', 'DD-MM-YYYY')                                                                                                                                                                               "
				+ "OR ((part.bandName          != 'ALMA-Band 3'                                                                                                                                                                                                     "
				+ "AND part.source_band_rownum <= 1)                                                                                                                                                                                                                "
				+ "OR (part.bandName            = 'ALMA-Band 3'                                                                                                                                                                                                     "
				+ "AND part.source_band_rownum <= 2)))                                                                                                                                                                                                              "
				+ "AND ((part.bandName         != 'ALMA-Band 3'                                                                                                                                                                                                     "
				+ "AND part.source_band_rownum <= 1)                                                                                                                                                                                                                 "
				+ "OR (part.bandName            = 'ALMA-Band 3'                                                                                                                                                                                                      "
				+ "AND part.source_band_rownum <= (1 * 2)))                                                                                                                                                                                                          "
				+ "ORDER BY flux DESC";
			
		//Act
		queryFormUtil.pressSearchBut();
		int resultsInDB = dbUtil.getIntScalar(query);
		int resultsInUI = resultTableUtil.getNumberOfResults();
		
		//Assert
		   Assert.assertEquals(
				resultsInUI,
				resultsInDB,
				"The number of results return in UI "
						+ resultsInUI
						+ " doesn't match the number of results retrived via DB "
						+ resultsInDB); 
	}
}