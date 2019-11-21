package de.obopstest.db.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.And;
import de.obopstest.db.setup.DbConfiguration;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import alma.aat.common.db.service.DbService;

public class DbSteps {

    private static Logger LOG = LoggerFactory.getLogger(DbSteps.class);

    private DbService dbService = new DbService(DbConfiguration.DB_CONNECTION_URL, DbConfiguration.USER, DbConfiguration.PASSWORD);

    @Given("^Verify Enabled Client ID for Oracle DB connection$")
    public void test_db(DataTable table) throws Throwable {

        List<List<String>> details = table.raw();
        String query = details.get(0).get(0);
        List<Map<String, String>> myListOfMaps = new ArrayList<Map<String, String>>();

        myListOfMaps =  dbService.getRecordList(query);

        Assert.assertTrue("No results found !!!",!myListOfMaps.isEmpty());

//        for (int i = 0 ; i < myListOfMaps.size() ; i++) {
//            StringBuffer sb = new StringBuffer();
//            Map<String, String> myMap = myListOfMaps.get(i);
//            LOG.info(String.format("%s", "Data For Row [" + i + "]:"));
//            for (Map.Entry<String, String> entrySet : myMap.entrySet()) {
//                sb.append("[" + entrySet.getKey() + " , " + entrySet.getValue() + "] ");
//            }
//            LOG.info(String.format("%s", sb));
//        }
    }

    @Given("^Verify xml_schedblock_entities and bmmv_schedblock are consistent$")
    public void test_db_xml_schedblock_entities_vs_bmmv_schedblock(DataTable table) throws Throwable {

        List<List<String>> details = table.raw();
        String query = details.get(0).get(0);
        List<Map<String, String>> myListOfMaps = new ArrayList<Map<String, String>>();

        myListOfMaps =  dbService.getRecordList(query);

        for (int i = 0 ; i < myListOfMaps.size() ; i++) {
            StringBuffer sb = new StringBuffer();
            Map<String, String> myMap = myListOfMaps.get(i);
            LOG.info(String.format("%s", "Data For Row [" + i + "]:"));
            for (Map.Entry<String, String> entrySet : myMap.entrySet()) {
                sb.append(entrySet.getValue());
            }
            LOG.info(String.format("%s", sb));
            Assert.assertEquals("Results do not match !!!","0", sb.toString());
        }
    }

    @And("^Change user password digest$")
    public void db_change_user_pass(DataTable table) throws Throwable {

        List<List<String>> details = table.raw();
        String id = details.get(0).get(0).trim();
        String digest = details.get(0).get(1).trim();

        dbService.changeUserPasswordDigest(id,digest);

    }

}
