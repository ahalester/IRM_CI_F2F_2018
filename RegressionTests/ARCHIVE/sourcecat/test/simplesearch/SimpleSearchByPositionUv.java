package simplesearch;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.PositionComponentIDs;
import util.TestBase;

public class SimpleSearchByPositionUv extends TestBase {

	@Test
	public void Search_FilterByUv_ReturnsSameResultsAsInDB() throws Exception {
		// Arrange
		String validUV = properties.getValidUV();
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
				+ "    AND (type_id = 1 ) "
				+ "     AND b.uvmax   >= " + properties.min(validUV) + "                                                                                                                                                    "
				+ "     AND b.uvmin   <= " + properties.max(validUV) + "                                                                                                                                                   "
				+ "     AND b.uvmin   >= 0.0                                                                                                                                                    "
				+ "     ) q                                                                                                                                                                   "
				+ "   ) part                                                                                                                                                                  "
				+ " WHERE (date_observed         > to_date('01-01-2013', 'DD-MM-YYYY')                                                                                                        "
				+ " OR ((part.bandName          != 'ALMA-Band 3'                                                                                                                              "
				+ " AND part.source_band_rownum <= 1)                                                                                                                                         "
				+ " OR (part.bandName            = 'ALMA-Band 3'                                                                                                                              "
				+ " AND part.source_band_rownum <= 2)))                                                                                                                                       "
				+ " AND ((part.bandName         != 'ALMA-Band 3'                                                                                                                              "
				+ " AND part.source_band_rownum <= 1)                                                                                                                                         "
				+ " OR (part.bandName            = 'ALMA-Band 3'                                                                                                                             "
				+ " AND part.source_band_rownum <= (1 * 2)))                                                                                                                                 "
				+ " ORDER BY flux DESC";
		queryFormUtil.insertValue(PositionComponentIDs.uvId,
				properties.getValidUV());

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
	public void Search_FilterByUvMinUv_ReturnsNOResults() {
		// Arrange
		queryFormUtil.insertValue(PositionComponentIDs.uvId,
				properties.getInvalidUV());

		// Act
		queryFormUtil.pressSearchBut();

		// Assert
		queryFormUtil.checkNoResultsMessage();
	}
}
