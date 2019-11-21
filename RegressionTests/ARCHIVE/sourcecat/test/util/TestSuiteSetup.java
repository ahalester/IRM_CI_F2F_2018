package util;

import java.util.Map;

import org.testng.annotations.BeforeSuite;

public class TestSuiteSetup {
	
	@BeforeSuite(alwaysRun = true)
	public void setupSuite() throws Exception {
		LoadPropFile properties = new LoadPropFile();
		DatabaseUtils dbUtil = new DatabaseUtils();
		String sourceNameALMA = properties.getValidSourceNameALMA();
		String catalog = properties.getValidCatalogue();
		String band = properties.getValidBand();
		String sourceType = properties.getSourceType();
		String sourceTypeForAutomation = properties.getSourceTypeForAutomation();
		
		String querySource = "SELECT * FROM SOURCE_NAME sn INNER JOIN NAMES n on sn.name_id=n.name_id WHERE n.SOURCE_NAME ='"
				+ sourceNameALMA + "'";
		String queryCatalog = "SELECT * FROM CATALOGUES WHERE CATALOGUE_NAME ='" + catalog + "'";
		String queryBand = "SELECT * FROM RANGES WHERE RANGE_NAME='" + band + "'";
		String querySourceTypeId="SELECT * FROM types WHERE type_name ='" + sourceType + "'";
		String querySourceTypeForAutomationId="SELECT * FROM types WHERE type_name ='" + sourceTypeForAutomation + "'";
		
		Map<String, Object> resultSource = dbUtil.getRow(querySource);
		Map<String, Object> resultCatalog = dbUtil.getRow(queryCatalog);
		Map<String, Object> resultBand = dbUtil.getRow(queryBand);
		Map<String, Object> resultSourceType = dbUtil.getRow(querySourceTypeId);
		Map<String, Object> resultSourceTypeForAutomation = dbUtil.getRow(querySourceTypeForAutomationId);
		
		if (resultSource == null || resultSource.isEmpty()) {
			throw new Exception(
					"Test suite prerequisites not met: the source name specified cannot be found in DB - running tests aborted..");
		}	
		if (resultCatalog == null || resultCatalog.isEmpty()) {
			throw new Exception(
					"Test suite prerequisites not met: the catalog name specified cannot be found in DB - running tests aborted..");
		}
		if (resultBand == null || resultBand.isEmpty()){
			throw new Exception(
					"Test suite prerequisites not met: the band name specified cannot be found in DB - running tests aborted..");
		}	
		if (resultSourceType == null || resultSourceType.isEmpty()) {
			throw new Exception(
					"Test suite prerequisites not met: the new source type specified cannot be found in DB - running tests aborted..");
		}
		if  (resultSourceTypeForAutomation == null || resultSourceTypeForAutomation.isEmpty()) {
			throw new Exception(
					"Test suite prerequisites not met: the source type for automation specified cannot be found in DB - running tests aborted..");
		}
	}
}
