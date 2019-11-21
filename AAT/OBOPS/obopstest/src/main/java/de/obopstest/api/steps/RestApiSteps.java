package de.obopstest.api.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import de.obopstest.api.setup.RestApiDefinition;

import static com.jayway.restassured.RestAssured.given;

import  de.obopstest.common.SessionStateHandler;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RestApiSteps {

    RestApiDefinition service;
    private static Logger LOG = LoggerFactory.getLogger(RestApiSteps.class);

    @Given("^Init xtss rest$")
    public void init_xtss_rest() throws Throwable {
        service = new RestApiDefinition();
    }

    @Then("^find ObsUnitSets with State and Substate$")
    public void ous_with_state_substate(DataTable table) throws Throwable {
        String id = service.findObsUnitSetsWithStateAndSubstate(table);
        if(id !=null){
            SessionStateHandler.setValue("ous_session_data_state_substate",id);
            LOG.info("Adding OUS ID to SessionStateHandler: " + (String)SessionStateHandler.getValue("ous_session_data_state_substate"));
        } else {
            Assert.fail("OUS with specific state and substate not found");
        }
    }

    @Then("^find ObsUnitSet with State$")
    public void ous_with_state(DataTable table) throws Throwable {
//        service.findObsUnitSetsWithState(table);
        String id = service.getObsUnitSetWithState(table);
        if(id !=null){
            SessionStateHandler.setValue("ous_session_data",id);
            LOG.info("Adding OUS ID to SessionStateHandler: " + (String)SessionStateHandler.getValue("ous_session_data"));
        } else {
            Assert.fail("OUS with specific sttate not found");
        }

    }

    @Then("^transition OUS state$")
    public void transition_ous_state(DataTable table) throws Throwable {
        service.transitionObsUnitSetFromState(table);
    }

}
