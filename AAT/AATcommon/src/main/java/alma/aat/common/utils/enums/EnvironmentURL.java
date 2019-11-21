package alma.aat.common.utils.enums;

public enum EnvironmentURL {

    BASE_URL("url"),
    PROTRACK("protrack_uri"),
    ENV_VERSION("testEnv"),
    ENV_VERSION_ROTATE("env_version_rotate"),
    URL_E2E("url_e2e"),
    MAIL_HOG_URL("mail_hog_url"),
    PHA_URL("pha_url"),
    PHA_DEV("pha_dev_url"),
    PHB_URL("phb_url"),

    PHB_PT_STATE_CHANGE_URL("phb_pt_state_change_url"),
    PHA_PT_STATE_CHANGE_URL("pha_pt_state_change_url"),

    PHB_DRA_URL("phb_dra_url"),
    PHA_DRA_URL("pha_dra_url"),
    DEV_DRA_URL("dev_dra_url"),

    API_DEV("rest_api_dev_url"),
    API_PHA("rest_api_pha_url"),
    API_PHB("rest_api_phb_url"),

    PHA_SNOOPI_URL("pha_snoopi_url"),
    PHB_SNOOPI_URL("phb_snoopi_url"),



    MAIL_HOG_E2E_URL("mail_hog_e2e_url");
    private final String value;

    EnvironmentURL(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}