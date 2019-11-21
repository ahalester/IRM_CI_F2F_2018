package advancedsearch;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.EnergyComponentIDs;
import constants.PositionComponentIDs;
import util.TestBase;
import util.PositionParameters;
import util.PositionParameters.PositionalSearchParameters;

public class AdvancedSearchByPositionAndEnergy extends TestBase{

	@Test
	public void Search_FilterByAllCriteriaInPositionAndEnergy_ReturnsSameResultsAsInDB() throws Exception {
		// Arrange
		queryFormUtil.insertValue(PositionComponentIDs.sourceNameId,
				properties.getValidSourceNameSESAME());
		queryFormUtil.waitForAutocomplete(PositionComponentIDs.searchRadiusId);
		queryFormUtil.insertValue(PositionComponentIDs.uvId,
				properties.getValidUV());

		queryFormUtil.selectOptionInMultiSelectDropDown(
				EnergyComponentIDs.bandId, properties.getValidBand());
		queryFormUtil.insertValue(EnergyComponentIDs.frequencyId,
				properties.getValidFrequency());
		queryFormUtil.insertValue(EnergyComponentIDs.fluxId,
				properties.getValidFlux());
		
		double ra = positionParam.hhmmss2decimalsRA(queryFormUtil.extractRAValue());
		double dec = positionParam.hhmmss2decimalsDEC(queryFormUtil.extractDecValue());
		double radius = Double.parseDouble(queryFormUtil.extractRadiusValue());
		PositionalSearchParameters params = PositionParameters
				.calculatePositionalSearchParameters(ra, dec, radius);
		
		double ra_radians = Math.toRadians(ra);
		double dec_radians = Math.toRadians(dec);
		final double toRadians = Math.PI / 180.0;
		double radiusS = Math.sin(Math.toRadians(radius / 2.0));
		
		String freq = properties.getValidFrequency();
		double freqMin = dbUtil.GhzToHz(properties.min(freq));
		double freqMax = dbUtil.GhzToHz(properties.max(freq));

		String validFlux =properties.getValidFlux();
		String fluxMin = properties.min(validFlux);
		String fluxMax = properties.max(validFlux);
		
		String validUv = properties.getValidUV();
		String uvMin = properties.min(validUv);
		String uvMax = properties.max(validUv);
		
		String query = "SELECT count(measurement_id) FROM "
				+ " (SELECT q.*, row_number() over (partition by source_id, bandName order by date_observed desc, measurement_id desc) source_band_rownum"
				+ " FROM "
				+ "  ( SELECT distinct  measurement_id, catalogue_id, m1.source_id, s.ra_deg, s.ra_deg_uncertainty, s.dec_deg, s.dec_deg_uncertainty, ra,"
                + "                   ra_uncertainty,DEC,dec_uncertainty, frequency, flux, flux_uncertainty, degree, degree_uncertainty, angle, angle_uncertainty,"
                + "                   extension,origin,date_observed,date_created,valid,b.uvmin,b.uvmax,b.spectral_index, COALESCE(r.range_name, 'non-ALMA Band') AS bandName " 
				+ " FROM measurements m1"
				+ " INNER JOIN sources s on s.source_id = m1.source_id "
				+ " LEFT JOIN source_type t on t.source_id = s.source_id "
				+ " LEFT JOIN sourcename n on n.source_id = s.source_id "
				+ " LEFT JOIN ranges r on m1.frequency between r.frequency_min and r.frequency_max "
				+ " LEFT JOIN source_band b on b.band_id = r.range_id and b.source_id = s.source_id "
				+ " WHERE (1 = 1) "
				+ " AND valid = 1 "
				+ "    AND (type_id      = 1 )                                                                                                                                "
				+ "    AND ((ra          >= " + params.raMin1 + "                                                                                                                 "
				+ "    AND ra            <= " + params.raMax1 + " )                                                                                                                    "
				+ "    OR (ra            >= " + params.raMin2 + "                                                                                                                                 "
				+ "    AND ra            <= " + params.raMax2 + " ))                                                                                                               "
				+ "    AND DEC           >= " + params.decMin + "                                                                                                                "
				+ "    AND DEC           <= " + params.decMax + "                                                                                                                      "
				+ "    AND SQRT( POWER(SIN((" + dec_radians + " - DEC * " + toRadians + ") / 2), 2) + COS(" + dec_radians + ") * COS(DEC * " + toRadians + ") * POWER(SIN((" + ra_radians +" - ra * " + toRadians + ") / 2), 2) ) <= " + radiusS +" "
				+ "    AND ((1 = 0) OR (r.range_name =" + "'" + properties.getValidBand() + "'" + ")"
				+ "    OR (frequency               >= " + freqMin + "  "
                + "    AND frequency                <= " + freqMax + " ) ) "
                + "    AND flux                      >= "+ fluxMin + "      "
				+ "	   AND flux                     <= " + fluxMax + "       "
				+ "	   AND b.uvmax                    >= " + uvMin + "       "   
				+ "	   AND b.uvmin                    <= " + uvMax + "      " 
				+ "	   AND b.uvmin                    >= 0.0       "
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

		// Act
		queryFormUtil.pressSearchBut();
		int resultsInUI = resultTableUtil.getNumberOfResults();
		int resultsInDB = dbUtil.getIntScalar(query);

		// Assert
		Assert.assertEquals(
				resultsInUI,
				resultsInDB,
				"The number of results return in UI "
						+ resultsInUI
						+ " doesn't match the number of results retrived via DB "
						+ resultsInDB);

	}

	@Test
	public void Search_FilterByAllCriteriaInPositionANDEnergy_ReturnsNOResults() {
		// Arrange
		queryFormUtil.insertValue(PositionComponentIDs.sourceNameId,
				properties.getInvalidSourceNameSESAME());
		queryFormUtil.waitForAutocomplete(PositionComponentIDs.searchRadiusId);
		queryFormUtil.insertValue(PositionComponentIDs.uvId,
				properties.getInvalidUV());

		queryFormUtil.selectOptionInMultiSelectDropDown(
				EnergyComponentIDs.bandId, properties.getInvalidBand());
		queryFormUtil.insertValue(EnergyComponentIDs.frequencyId,
				properties.getInvalidFrequency());
		queryFormUtil.insertValue(EnergyComponentIDs.fluxId,
				properties.getInvalidFlux());
		
		// Act
		queryFormUtil.pressSearchBut();

		// Assert
		queryFormUtil.checkNoResultsMessage();
	}
}
