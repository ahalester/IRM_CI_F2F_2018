package simplesearch;

import java.net.MalformedURLException;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.EnergyComponentIDs;
import util.TestBase;

public class SimpleSearchByEnergyBand extends TestBase {

	@Test
	public void Search_FilterByBand_ReturnsSameNumberOfRowsAsInDB()
			throws Exception {
		// Arrange
		String query = "select count(measurement_id) from "
				+ " (select q.*, row_number() over (partition by source_id, bandName order by date_observed desc, measurement_id desc) source_band_rownum"
				+ " from "
				+ "( SELECT distinct  measurement_id, catalogue_id, m1.source_id, s.ra_deg, s.ra_deg_uncertainty, s.dec_deg, s.dec_deg_uncertainty, ra,"
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
				+ "AND (type_id = 1 ) "
				+ "AND ((1 = 0) OR (r.range_name =" + "'" + properties.getValidBand() + "'" + "))"
				+ " ) q"
				+ " ) part"
				+ " where (date_observed > to_date('01-01-2013', 'DD-MM-YYYY')"
				+ "   or ((part.bandName != 'ALMA-Band 3' and part.source_band_rownum <= 1)"
				+ "   or (part.bandName = 'ALMA-Band 3' and part.source_band_rownum <= 2))"
				+ "      )"
				+ "   and ((part.bandName != 'ALMA-Band 3' and part.source_band_rownum <= 1)"
				+ "    or (part.bandName = 'ALMA-Band 3' and part.source_band_rownum <= (1 * 2))"
				+ "       ) " + "ORDER BY flux DESC";
		queryFormUtil.selectOptionInMultiSelectDropDown(EnergyComponentIDs.bandId,
				properties.getValidBand());

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
	public void Search_FilterByBand_ReturnsNOResults()
			throws MalformedURLException {
		// Arrange
		queryFormUtil.selectOptionInMultiSelectDropDown(EnergyComponentIDs.bandId,
				properties.getInvalidBand());

		// Act
		queryFormUtil.pressSearchBut();
		
		// Assert
		queryFormUtil.checkNoResultsMessage();

	}
}
