package alma.aat.common.api.xtss.service;


import alma.aat.common.api.xtss.pojos.OusEntityXtss;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static alma.aat.common.utils.ApiUtils.encodeOusUid;

import alma.aat.common.utils.PropertiesFileUtil;
import alma.aat.common.utils.enums.EnvironmentURL;

//import static com.jayway.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.equalTo;


public class XtssService {
    private String user;
    private String password;
    private String url;

    private static Logger LOG = LoggerFactory.getLogger(XtssService.class);

    /**
     *
     * @param user
     * @param pass
     * @param testEnv
     * @param envPhase
     */
    public XtssService(String user, String pass, String testEnv, String envPhase ) {


        this.user = user;
        this.password = pass;

        if (envPhase.equalsIgnoreCase("PHAB")) {
            this.url = PropertiesFileUtil.getApiURL(EnvironmentURL.API_PHB, testEnv);
        } else if (envPhase.equalsIgnoreCase("PHAA")) {
            this.url = PropertiesFileUtil.getApiURL(EnvironmentURL.API_PHA, testEnv);
        }else if (envPhase.equalsIgnoreCase("PHAD")) {
            this.url = PropertiesFileUtil.getApiURL(EnvironmentURL.API_DEV, testEnv);
        }
        LOG.info("API URL: " + this.url);

        RestAssured.baseURI = this.url;
    }

    public String getUrl() {
        return url;
    }


    public String findObsUnitSetsWithStateAndSubstate(String state, String substate) {
        String ousStatusEntityId = null;

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

//    public void findObsUnitSetsWithState(DataTable table) throws Throwable {
//        List<List<String>> details = table.raw();
//        String state = getElementName(details.get(0).get(0));
//        Response response =
//                given().
//                        auth().basic(user, password).
//                        contentType("application/json").
//                        parameters("state",state).
//                        when().
//                        get("/service/api/obs-unit-sets").
//                        then().
//                        statusCode(200).
//                        extract().response();
//
//        OusEntityXtss[] allOusEntityXtss = response.getBody().as(OusEntityXtss[].class);
//
//    }

    public String getObsUnitSetWithState(String state) {
        String ousStatusEntityId = null;

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


    public void transitionObsUnitSetFromState(String uid, String targetState) {

//        List<List<String>> details = table.raw();
//        String uid = SessionStateHandler.getValue(getElementName(details.get(0).get(0)));
//        String targetState = getElementName(details.get(0).get(1));
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
