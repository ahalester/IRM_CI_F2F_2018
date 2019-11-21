package de.obopstest.db.setup;

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

public class DbSetup {
    private static Logger LOG = LoggerFactory.getLogger(DbSetup.class);

    private String childClassName = this.getClass().getName();
    private long startTime;

    private String dbURL;
    DbConfiguration service;

    @Before("@DbTest")
    public void setUp(Scenario scenario) {

        startTime = System.currentTimeMillis();

        LOG.info(String.format("Test scenario: '%s'", scenario.getName()));
        LOG.info("****************** NEW TEST SCENARIO ******************");
        LOG.info(String.format("%s", scenario.getName()));

        if ((dateConverter(keyValidator()))
                .before(dateConverter(decrypt(getProperty("license", "key"))))) {
//            dbURL = DbConfiguration.DB_CONNECTION_URL;
        } else {
            ipRights();
        }
        SessionStateHandler.clear();
    }

    @After("@DbTest")
    public void tearDown(Scenario scenario) {
        RestAssured.reset();
        if (scenario.isFailed()) {
            String scenarioName = scenario.getName();
            // To Do for failed scenario
        }

        LOG.info(String.format("%s - %s", scenario.getName(), scenario.getStatus().toUpperCase()));
        LOG.info("Total execution time: " + (System.currentTimeMillis() - startTime) + " ms");


        SessionStateHandler.clear();
    }

    private void ipRights() {
        LOG.info(decrypt(getProperty("license", "fail")));
        Assert.fail(decrypt(getProperty("license", "fail")));
    }

}
