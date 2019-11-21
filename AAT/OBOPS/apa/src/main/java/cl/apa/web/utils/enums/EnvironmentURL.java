package cl.apa.web.utils.enums;

/**
 * Created by bdumitru on 8/8/2017.
 */
public enum EnvironmentURL {

    BASE_URL("url"),
    PHA_URL("pha_url"),
    QA0("qa0_uri"),
    QA2("qa2_uri"),
    APA("apa_url"),
    ENV_VERSION("testEnv"),
    PROTRACK("protrack_uri"),
    ENV_VERSION_ROTATE("env_version_rotate"),
    URL_E2E("url_e2e"),
    MAIL_HOG_URL("mail_hog_url"),
    MAIL_HOG_E2E_URL("mail_hog_e2e_url");

    private final String value;

    EnvironmentURL(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
