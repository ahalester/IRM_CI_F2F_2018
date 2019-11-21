import alma.aat.common.api.xtss.service.XtssService;
import alma.aat.common.db.DbService;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Attempts {

    public static final String DB_CONNECTION_URL_PHASE_A = "jdbc:oracle:thin:@//ora12c2.hq.eso.org:1521/ALMA";
    public static final String USER_PHASE_A = "alma_obops";
    public static final String PASSWORD_PHASE_A = "I+ADPEdjLXFriXGyV/1N+w==";

    public static final String DB_CONNECTION_URL_PHASE_B = "jdbc:oracle:thin:@//orasw.apotest.alma.cl:1521/ALMAI1";
    public static final String USER_PHASE_B = "alma";
    public static final String PASSWORD_PHASE_B = "almafordev";

    private static Logger LOG = LoggerFactory.getLogger(Attempts.class);

    public static void main(String[] args) {
        DbService db_service = new DbService(DB_CONNECTION_URL_PHASE_B, USER_PHASE_B, PASSWORD_PHASE_B);
        //String execBlock = db_service.getTC01EB("2017");
        Boolean result = db_service.setupMOUSToBeIngested("uid://A001/X889/X2c5", "2016.1.00161.S");
        //String execBlock = db_service.unsetQA0status("2017");

        String user = "";
        String pass = "";
        String testEnv = "2018oct";
        String envPhase = "PHAB";

        XtssService service = new XtssService(user,pass,testEnv, envPhase);
        String id = service.getObsUnitSetWithState("ReadyForProcessing");
        }


}
