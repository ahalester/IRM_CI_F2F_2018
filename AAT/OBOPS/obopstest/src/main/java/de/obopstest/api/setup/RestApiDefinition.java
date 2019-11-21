package de.obopstest.api.setup;


import static de.obopstest.web.utils.PropertiesFileUtil.getElementName;

import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.response.Response;
import cucumber.api.DataTable;
import de.obopstest.web.utils.EncryptUtil;
import de.obopstest.web.utils.PropertiesFileUtil;

import java.util.Random;

import de.obopstest.web.utils.enums.EnvironmentURL;

import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;
//import static com.jayway.restassured.RestAssured.given;

//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.equalTo;

import  de.obopstest.common.SessionStateHandler;
import de.obopstest.api.pojos.OusEntityXtss;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.obopstest.api.utils.ApiUtils.encodeOusUid;


public class RestApiDefinition {
    private String user;
    private String password;
    private String url;

    private static Logger LOG = LoggerFactory.getLogger(RestApiDefinition.class);

    public RestApiDefinition() {


        user = PropertiesFileUtil.getProperty("username");
        password = EncryptUtil.decrypt(PropertiesFileUtil.getProperty("password"));

        String envPhase = System.getProperty("envPhase");
        String testEnv = System.getProperty("testEnv").toUpperCase();


        if (envPhase.equalsIgnoreCase("PHAB")) {
            this.url = PropertiesFileUtil.getApiURL(EnvironmentURL.API_PHB, testEnv);
        } else if (envPhase.equalsIgnoreCase("PHAA")) {
            this.url = PropertiesFileUtil.getApiURL(EnvironmentURL.API_PHA, testEnv);
        }else if (envPhase.equalsIgnoreCase("PHAD")) {
            this.url = PropertiesFileUtil.getApiURL(EnvironmentURL.API_DEV, testEnv);
        }
        LOG.info("API URL: " + this.url);

        RestAssured.baseURI = this.url;//RestApiConfiguration.REST_API_BASE_URL;
    }

    public String getUrl() {
        return url;
    }



//    public void test() {
////        Response response =
////                given().
////                        contentType("application/json").
////                when().
////                        get("/iss-now/").
////                then().
////                        body(containsString("iss_position")).
////                        body(containsString("message")).
////                        body(containsString("timestamp")).
////                        body(("message"), equalTo("success")).
////                extract().response();
////    }


    public String findObsUnitSetsWithStateAndSubstate(DataTable table) throws Throwable {
        String ousStatusEntityId = null;
        List<List<String>> details = table.raw();
        String state = getElementName(details.get(0).get(0));
        String substate = getElementName(details.get(0).get(1));
        Response response =
                given().
                        auth().basic(user, password).
                        contentType("application/json").
                        parameters("state",state, "substate", substate).
                        when().
                        get("/service/api/obs-unit-sets").
                        then().
                        statusCode(200).
//                        body(containsString("ousStatusEntityId")).
                        extract().response();

        OusEntityXtss[] allOusEntityXtss = response.getBody().as(OusEntityXtss[].class);
        ArrayList<OusEntityXtss> list = new ArrayList(Arrays.asList(allOusEntityXtss));
        Random rand = new Random();
        if(!list.isEmpty()){
            LOG.info("Found [" + list.size() + "] Obs Unit Sets With State and Substate [" + state + "][" + substate + "]" );
            ousStatusEntityId = list.get(rand.nextInt(list.size())).getOusStatusEntityId();
            LOG.info("Getting Obs Unit Set With UID: " + ousStatusEntityId);
        } else {
            LOG.error("OUS with State and Substate [" + state + "][" + substate + "] not found");
        }

        // verify if we have a Json Objsct or Json Array
//        String firstChar = String.valueOf(response.getBody().asString().charAt(0));
//        if (firstChar.equalsIgnoreCase("[")) {
//            //json array
//        }else{
//            //json object
//        }

        return ousStatusEntityId;
    }

    public void findObsUnitSetsWithState(DataTable table) throws Throwable {
        List<List<String>> details = table.raw();
        String state = getElementName(details.get(0).get(0));
        Response response =
                given().
                        auth().basic(user, password).
                        contentType("application/json").
                        parameters("state",state).
                        when().
                        get("/service/api/obs-unit-sets").
                        then().
                        statusCode(200).
                        extract().response();

        OusEntityXtss[] allOusEntityXtss = response.getBody().as(OusEntityXtss[].class);

    }

    public String getObsUnitSetWithState(DataTable table) throws Throwable {
        String ousStatusEntityId = null;
        List<List<String>> details = table.raw();
        String state = getElementName(details.get(0).get(0));

        LOG.info("Calling service: " + this.getUrl() + "/service/api/obs-unit-sets/?state=" + state);
        Response response =
                given().
                        auth().basic(user, password).
                        contentType("application/json").
                        parameters("state",state).
                        when().
                        get("/service/api/obs-unit-sets").
                        then().
                        statusCode(200).
                        extract().response();

//        List<OusEntityXtss> allOusEntityXtss = response.getBody().jsonPath().getList("", OusEntityXtss.class);
//        for (OusEntityXtss item : allOusEntityXtss) {
//            System.out.println(item);
//        }
        OusEntityXtss[] allOusEntityXtss = response.getBody().as(OusEntityXtss[].class);
        ArrayList<OusEntityXtss> list = new ArrayList(Arrays.asList(allOusEntityXtss));
        Random rand = new Random();
        if(!list.isEmpty()){
            ousStatusEntityId = list.get(rand.nextInt(list.size())).getOusStatusEntityId();
            LOG.info("Found [" + list.size() + "] Obs Unit Sets With State [" + state + "]" );
            LOG.info("Getting Obs Unit Set With UID: " + ousStatusEntityId);
        } else {
            LOG.error("OUS with state [" + state + "] not found");
        }

        return ousStatusEntityId;
    }

    public void transitionObsUnitSetFromState(DataTable table) throws Throwable {

        List<List<String>> details = table.raw();
        String uid = SessionStateHandler.getValue(getElementName(details.get(0).get(0)));
        String targetState = getElementName(details.get(0).get(1));
//        String targetSubstate = getElementName(details.get(0).get(2));

        String encodeOusUid = encodeOusUid(uid);

//        Map<String,String> param = new HashMap<>();
//        param.put("targetState", targetState);
//        param.put("targetSubstate", targetSubstate);

        LOG.info("Calling service: " + this.getUrl() + "/service/api/transition/obs-unit-set/" + encodeOusUid);
        Response response =
                given().
                        auth().basic(user, password).
                        contentType("application/json").
                        parameters("targetState",targetState).
                        parameters("stateChangeComment","auto test").
//                        body(param).
                        when().
                        put("/service/api/transition/obs-unit-set/" + encodeOusUid).
                        then().
                        statusCode(200).
                        extract().response();

        LOG.info("response code: " + response.getStatusCode());
        LOG.info("response : " +response.getBody().prettyPrint());

    }

}
