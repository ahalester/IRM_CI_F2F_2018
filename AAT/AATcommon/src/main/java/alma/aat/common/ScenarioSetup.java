package alma.aat.common;

import alma.aat.common.api.sourcecat.SCService;
import alma.aat.common.db.DbService;

/**
 * Created by ldoming on 25/10/2018
 * Public interface to be used by testing project for setting up the different tests cases
 **/
public class ScenarioSetup {

    private SCService scService;
    private DbService dbService;


    public ScenarioSetup(String envPhase, String testEnv, String dbURL, String dbUser, String dbPassword, String restUser, String restPassword) {
        this.scService = new SCService(envPhase, testEnv, restUser, restPassword);
        this.dbService = new DbService(dbURL,dbUser,dbPassword);
    }


    /**
     * Low level routine preparing initial state data for TC01
     * 1.
     * 2.
     * 3.
     */
    public static void prepareTC01() {


    }

    /**
     * Low level routine preparing initial state data for TC02
     * 1.
     * 2.
     * 3.
     */
    public static void prepareTC02() {

    }


    /**
     * Low level routine preparing initial state data for TC03
     * 1.
     * 2.
     * 3.
     */
    public static void prepareTC03() {

    }


}
