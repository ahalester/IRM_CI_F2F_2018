package util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtils {
	LoadPropFile properties = new LoadPropFile();
	public String sourceName = "Source for Test Automation";
	
	private Connection getConnection() throws Exception {

		Connection conn = null;
		try {
			Class.forName(properties.getJdbcDriverClassName());

			String url = properties.getJdbcURL();
			String username = properties.getJdbcUsername();
			String password = properties.getJdbcPassword();

			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			System.err.println("Create db connection error: " + e.getMessage());
			throw e;
		}
		return conn;
	}

	private ResultSet getResultSet(Connection conn, String query)
			throws Exception {
		Statement stm;
		ResultSet result;
		stm = getConnection().createStatement();
		result = stm.executeQuery(query);
		return result;
	}

	public List<Map<String, Object>> getTable(String query) throws Exception {
		Connection conn = null;
		ResultSet rs = null;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		try {
			conn = getConnection();
			rs = getResultSet(conn, query);

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
				Map<String, Object> columns = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i), rs.getObject(i));
				}

				result.add(columns);
			}
		} catch (Exception e) {
			System.err.println("Error retrieving scalar for query: " + query
					+ " \r\nError: " + e.getMessage());
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (conn != null)
				conn.close();
		}
		return result;
	}

	public Map<String, Object> getRow(String query) throws Exception {
		List<Map<String, Object>> table = getTable(query);
		return table.isEmpty() ? null : table.get(0);

	}

	public String getStringScalar(String query) throws Exception {
		Map<String, Object> row = getRow(query);
		return row.isEmpty() ? null : row.values().toString();
	}

	public int getIntScalar(String query) throws Exception {
		Map<String, Object> row = getRow(query);
		return (int) (row.isEmpty() ? 0 : ((BigDecimal) row.values().iterator()
				.next()).intValue());
	}

	public int getCatalogueId() throws Exception {
		String queryCatalogueId = "SELECT catalogue_id FROM catalogues WHERE catalogue_name = '"
				+ properties.getValidCatalogue() + "'";
		int catalogueId = getIntScalar(queryCatalogueId);
		return catalogueId;

	}

	public int getSourceTypeId() throws Exception {
		String querySourceTypeId = "SELECT type_id FROM types WHERE type_name ='"
				+ properties.getSourceType() + "'";
		int sourceTypeId = getIntScalar(querySourceTypeId);
		return sourceTypeId;
	}
	
	public int getBandId() throws Exception {
		String queryBandId = "SELECT range_id FROM ranges WHERE  range_name='" + properties.getValidBand() + "'";
		int bandId = getIntScalar(queryBandId);
		return bandId;
	}
	
	public int getSourceNameId() throws Exception{
		String querySourceNameId="select n.NAME_ID from names n inner join source_name sn on n.NAME_ID=sn.NAME_ID where source_name='" + sourceName + "'" ;
		int sourceNameId = getIntScalar(querySourceNameId);
		return sourceNameId;
	}
		
	public int getSourceId() throws Exception{
		String querySourceId="select s.source_id from sources s inner join source_name sn on s.source_id=sn.source_id inner join names n on sn.name_id=n.name_id where n.source_name='"+ properties.getValidSourceNameALMA() + "'" ;
		int sourceId = getIntScalar(querySourceId);
		return sourceId;
	}

	public double GhzToHz(String value) {
		double valueGHz = Double.parseDouble(value);
		double valueHz = valueGHz * 1E9;
		return valueHz;
	}
	
	public void removeNewCreatedSource(int sourceId) throws Exception{
		Statement stm = null;
		String queryRemoveNewSource = "delete from sources where source_id=" + sourceId;
		stm = getConnection().createStatement();
		stm.execute(queryRemoveNewSource);
	}

	public int getSourceTypeForAutomationId() throws Exception {
		String querySourceTypeId = "SELECT type_id FROM types WHERE type_name ='"
				+ properties.getSourceTypeForAutomation() + "'";
		int sourceTypeForAutomationId = getIntScalar(querySourceTypeId);
		return sourceTypeForAutomationId;
	}
}
