package alma.aat.common.api.sourcecat;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.StreamSupport;

/**
 * Based on HttpClient
 */
public class SCService {
    private String user;
    private String password;
    private String url;


    private static Logger LOG = LoggerFactory.getLogger(SCService.class);


    public SCService(String envPhase, String testEnv, String user, String password) {

        this.user = user;
        this.password = password;

        String sc_phaa_url = "http://phase-a.hq.eso.org:10000/sc";
        String sc_asa_url = "https://deployment_version.asa-test.alma.cl/sc";


        if (envPhase.equalsIgnoreCase("PHAB")) {
            this.url = sc_asa_url.replace("deployment_version", testEnv);
        } else if (envPhase.equalsIgnoreCase("PHAA")) {
            this.url = sc_phaa_url;
        }
        LOG.info("API URL: " + this.url);
    }


    public void updateMeasurementsInRange(String date) throws Throwable {

        JsonNode node = getMeasurementsInRange(date);

        StreamSupport.stream(node.spliterator(), false /* or whatever */).forEach(element -> {
            try {
                updateMeasurement(element.get("id").toString());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });


    }

    public int updateMeasurement(String measurementId) throws Throwable {
        return postRequest(this.url + "/rest/measurements/" + measurementId + "/update?ra=1", this.user, this.password);
    }


    public JsonNode getMeasurementsInRange(String date) {
        return getRequest("/rest/measurements?date=" + date, user, password);
    }


    private int postRequest(String path, String user, String password) throws AuthenticationException, IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(path);

        UsernamePasswordCredentials creds
                = new UsernamePasswordCredentials(user, password);

        httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));

        CloseableHttpResponse response = client.execute(httpPost);

        client.close();
        return response.getStatusLine().getStatusCode();
    }

    public JsonNode getRequest(String path, String user, String password) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(this.url + path);

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user, password);
        JsonNode node = null;

        try {

            httpGet.addHeader(new BasicScheme().authenticate(creds, httpGet, null));

            CloseableHttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();


            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    entity.getContent()));

            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }

            System.out.println(builder);


            ObjectMapper mapper = new ObjectMapper();
            node = mapper.readTree(builder.toString());

            response.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return node;

    }


}
