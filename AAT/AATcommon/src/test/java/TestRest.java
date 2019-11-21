import alma.aat.common.ScenarioSetup;
import alma.aat.common.api.sourcecat.SCService;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;

/**
 * Created by ldoming on 24/10/2018
 **/
public class TestRest {


    public static void main(String[] args) {
        SCService sc = new SCService("PHAB", "e2e6", "obops", "!Es0s0");

        try {


            ScenarioSetup.prepareTC01();



            JsonNode node = sc.getMeasurementsInRange("2018-08-10");
            System.out.println(node.get(0));


            sc.updateMeasurementsInRange("2018-08-10");

            int statusCode = sc.updateMeasurement("768447");

            Assert.assertTrue("Mesuarements updated", 200 == statusCode);

//            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//            nvps.add(new BasicNameValuePair("ra", "1"));
//            nvps.add(new BasicNameValuePair("password", "secret"));
//
//            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}

