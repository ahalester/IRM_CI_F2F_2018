package alma.aat.common.db;

import alma.aat.common.db.utils.DbHelper;
import alma.aat.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import alma.lifecycle.StateSystem;

public class DbService {

    private static Logger LOG = LoggerFactory.getLogger(DbService.class);
    private DbHelper dbHelper;
    private String url;
    private String user;
    private String password;

    /**
     * @param url
     * @param user
     * @param password
     */
    public DbService(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.dbHelper = DbHelper.getInstance(this.url, this.user, this.password);
        int a = 1;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

//    public String getPassword() {
//        return password;
//    }

    /**
     * @param status
     * @param year
     * @return - List<String> - list of OUSs UIDs
     */
    public List<String> getOUSsWithStatusFromYear(String status, String year) {
//        String query = "SELECT mv_obsproject.prj_code, filtered_mous.ousstatusref FROM " +
//                "(SELECT * FROM mv_obsunitset INNER JOIN obs_unit_set_status " +
//                "ON obs_unit_set_status.domain_entity_state = '{}' " +
//                "AND obs_unit_set_status.domain_entity_id = mv_obsunitset.partid " +
//                "WHERE mv_obsunitset.name like 'Member OUS%') filtered_mous " +
//                "INNER JOIN mv_obsproject ON mv_obsproject.prj_archive_uid = filtered_mous.obs_project_id where mv_obsproject.prj_code like '{}.%'";

        String query = "SELECT filtered_mous.ousstatusref FROM " +
                "(SELECT * FROM mv_obsunitset INNER JOIN obs_unit_set_status " +
                "ON obs_unit_set_status.domain_entity_state = '{}' " +
                "AND obs_unit_set_status.domain_entity_id = mv_obsunitset.partid " +
                "WHERE mv_obsunitset.name like 'Member OUS%') filtered_mous " +
                "INNER JOIN mv_obsproject ON mv_obsproject.prj_archive_uid = filtered_mous.obs_project_id where mv_obsproject.prj_code like '{}.%'";

        List<Map<String, String>> mapList = dbHelper.getRecordList(StringUtil.format(query, status, year));

//        return StringUtil.getListOfValuesFromMapForSpecificKeys(mapList,"OUSSTATUSREF");
        return StringUtil.getListOfValuesFromMapList(mapList);
    }

    /**
     * @param status
     * @param year
     * @return OUS UID
     */
    public String getRandomOUSWithStatusFromYear(String status, String year){
        return StringUtil.getRandomStringFromList(getOUSsWithStatusFromYear(status, year));
    }

    /**
     * @param id
     * @return password digest
     */
    private String getUserPasswordDigest(String id){
        String query = "select password_digest as PASSWORD from account where account_id = '{}'";
        return dbHelper.getSingleRecord(StringUtil.format(query, id)).get("PASSWORD").toString();
    }

    /**
     * @param id
     * @param digest
     * @return password digest
     */
    public String changeUserPasswordDigest(String id, String digest){
        String query = "update account  set password_digest = '{}' where account_id = '{}'";
        dbHelper.executeQuery(StringUtil.format(query, digest, id));
        String digest_updated = getUserPasswordDigest(id);
        LOG.info("Updated password digest [id digest]: [" + id + "  " + digest_updated + "]"   );
        return digest_updated;
    }


    public List<Map<String, String>> getRecordList(String query){
        return dbHelper.getRecordList(StringUtil.format(query));
    }

    /**
     * @param year to search s
     * @param
     * @return
     */
    public String getTC01EB(String year){
        // Get candidate data
        String query = "select EXECBLOCKUID as EB from AQUA_V_EXECBLOCK where STATUS='SUCCESS' and QA0STATUS = 'Pass' and EXECFRACTION = 1 and OBSPROJECTCODE like '{}%' AND ROWNUM <= 100";
        List<Map<String, String>> mapList = dbHelper.getRecordList(StringUtil.format(query, year));

        // Iterate over the list setting QA0 status to fail
        List<String> list = StringUtil.getListOfValuesFromMapList(mapList);
        query = "update AQUA_EXECBLOCK set QA0STATUS = 'Unset' where EXECBLOCKUID = '{}'";
        for(String eb:list){
            // Unset QA0 status
            LOG.debug(String.format("Attempting with EB: %s", eb));
            dbHelper.executeQuery(StringUtil.format(query, eb));

            // Expected Execution Count
            query = "select EXECUTION_COUNT as EC from MV_SCHEDBLOCK where SB_ARCHIVE_UID=(select SCHEDBLOCKUID from AQUA_V_EXECBLOCK where EXECBLOCKUID='{}')";
            Map<String, String> expectedCount = dbHelper.getSingleRecord(StringUtil.format(query, eb));

            // Check the new Execution
            query = "select SUM(EXECFRACTION) as NEC from AQUA_V_EXECBLOCK where SCHEDBLOCKUID=(select SCHEDBLOCKUID from AQUA_V_EXECBLOCK where EXECBLOCKUID='{}') AND QA0STATUS = 'Pass'";
            Map<String, String> execCount = dbHelper.getSingleRecord(StringUtil.format(query, eb));

            Double diff = Double.parseDouble(expectedCount.get("EC")) - Double.parseDouble(execCount.get("NEC"));
            LOG.debug(String.format("EC - NEC = %s", diff));

            if (diff <= 1 & diff > 0){
                LOG.info(String.format("Attempt with EB: %s, was successful", eb));
                return eb;
            }
        }

        return "null";
    }

    /**
     * @param year
     * @return
     */
    public String unsetQA0status(String year) {
        String query = "select EXECBLOCKUID as EB from AQUA_V_EXECBLOCK where STATUS='SUCCESS' and QA0STATUS = 'Pass' and EXECFRACTION = 1 and OBSPROJECTCODE like '{}%' AND ROWNUM <= 1";
        String eb = dbHelper.getSingleRecord(StringUtil.format(query, year)).get("EB");

        query = "update AQUA_EXECBLOCK set QA0STATUS = 'Unset' where EXECBLOCKUID = '{}'";
        dbHelper.executeQuery(StringUtil.format(query, eb));
        LOG.info(eb);

        return eb;
    }

    /**
     * @param mousUID to be setup for ingestion
     * @param projectCode project code of the mousUID
     * @return
     */
    public Boolean setupMOUSToBeIngested(String mousUID, String projectCode) {
        String query;

        String delivery_name = projectCode + "_" + mousUID.replace('/', '_').replace(':', '_');

        // Delete entries from asa_product_files
        query = "delete from ASA_PRODUCT_FILES where ASA_OUS_ID = '{}'";
        dbHelper.executeQuery(StringUtil.format(query, mousUID));

        // Delete asa_delivery_asdm_ous
        query = "delete from ASA_DELIVERY_ASDM_OUS where DELIVERABLE_NAME = '{}'";
        dbHelper.executeQuery(StringUtil.format(query, delivery_name));

        // Delete asa_delivery_status
        query = "delete from ASA_DELIVERY_STATUS where DELIVERY_NAME='{}'";

        // assert should be performed here to see whether the rows where deleted or not
        LOG.info(StringUtil.format("DELETE queries executed successfully for %s", mousUID));


        // Unset QA2 status
        query = "update AQUA_OUS set QA2STATUS = 'Unset' where OUS_STATUS_ENTITY_ID = '{}'";
        dbHelper.executeQuery(StringUtil.format(query, mousUID));

        LOG.info(StringUtil.format("UPDATE QA2STATUS executed successfully for %s", mousUID));

        return Boolean.TRUE;
    }

}
