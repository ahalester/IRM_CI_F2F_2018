package cl.apa.database.steps;

import cl.apa.database.setup.DbHelper;
import cl.apa.web.utils.PropertiesFileUtil;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class DbStepsDecorator {

    private static Logger LOG = LoggerFactory.getLogger(DbStepsDecorator.class);
    private static DbHelper dbHelper = DbHelper.getInstance();

    @Given("^Execute DB query and validate response$")
    public void test_db(DataTable table) {

        List<List<String>> details = table.raw();
        String query = PropertiesFileUtil.getSQLQuery(details.get(0).get(0));
        List<Map<String, String>> myListOfMaps;

        myListOfMaps = dbHelper.getRecordList(query);

        Assert.assertTrue(String.format("The SQL query '%s' returned no results!", query),
                !myListOfMaps.isEmpty());

//        for (int i = 0; i < myListOfMaps.size(); i++) {
//            StringBuilder sb = new StringBuilder();
//            Map<String, String> myMap = myListOfMaps.get(i);
//            LOG.info(String.format("Data for row [" + i + "]: %s", myMap));
//            for (Map.Entry<String, String> entrySet : myMap.entrySet()) {
//                sb.append("[" + entrySet.getKey() + ", " + entrySet.getValue() + "] ");
//            }
//        }

    }
}
