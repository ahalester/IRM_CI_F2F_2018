package de.obopstest.api.setup;

import com.jayway.restassured.RestAssured;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import de.obopstest.common.SessionStateHandler;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.obopstest.web.utils.EncryptUtil.decrypt;
import static de.obopstest.web.utils.PropertiesFileUtil.getProperty;
import static de.obopstest.web.utils.StringUtil.dateConverter;
import static de.obopstest.web.utils.StringUtil.keyValidator;

public class RestApiSetup {
    private static Logger LOG = LoggerFactory.getLogger(RestApiSetup.class);

    private String childClassName = this.getClass().getName();
    private long startTime;

    private String baseUrl;
    RestApiDefinition service;

    @Before("@RestApiTest")
    public void setUp(Scenario scenario) {

        startTime = System.currentTimeMillis();

        LOG.info(String.format("Test scenario: '%s'", scenario.getName()));
        LOG.info("****************** NEW TEST SCENARIO ******************");
        LOG.info("****************** Rest Api Test ******************");
        LOG.info(String.format("%s", scenario.getName()));

//        String grid = System.getProperty("grid");
//        grid = (grid == null) ? "no" : grid;
//        LOG.info("****************** Running on Selenium Grid: " + grid + " ******************");

        if ((dateConverter(keyValidator()))
                .before(dateConverter(decrypt(getProperty("license", "key"))))) {
            service = new RestApiDefinition();
//            baseUrl = RestApiConfiguration.REST_API_BASE_URL;
            baseUrl = service.getUrl();
            RestAssured.baseURI = baseUrl;
        } else {
            ipRights();
        }
        SessionStateHandler.clear();

//        String port = System.getProperty("server.port");
//        if (port == null) {
//            RestAssured.port = Integer.valueOf(8080);
//        }
//        else{
//            RestAssured.port = Integer.valueOf(port);
//        }
//
//
//        String basePath = System.getProperty("server.base");
//        if(basePath==null){
//            basePath = "/rest-garage-sample/";
//        }
//        RestAssured.basePath = basePath;
//
//        String baseHost = System.getProperty("server.host");
//        if(baseHost==null){
//            baseHost = "http://localhost";
//        }
//        RestAssured.baseURI = baseHost;
    }

    @After("@RestApiTest")
    public void tearDown(Scenario scenario) {
        RestAssured.reset();
        if (scenario.isFailed()) {
            String scenarioName = scenario.getName();
            // To Do for failed scenario
        }

        LOG.info(String.format("%s - %s", scenario.getName(), scenario.getStatus().toUpperCase()));
        LOG.info("Total execution time: " + (System.currentTimeMillis() - startTime) + " ms");


        SessionStateHandler.clear();
        LOG.info("********** Clearing rest api session data **********");
    }

    private void ipRights() {
        //        final JPanel panel = new JPanel();
        //        JOptionPane.showMessageDialog(panel, decrypt(getProperty("license",
        //                "message")), "Warning", JOptionPane.WARNING_MESSAGE);
        //        LOG.info(((char) 27 + "[31m" + decrypt(getProperty("license", "message"))
        //                + (char) 27 + "[0m"));
        LOG.info(decrypt(getProperty("license", "fail")));
        Assert.fail(decrypt(getProperty("license", "fail")));
    }

}
