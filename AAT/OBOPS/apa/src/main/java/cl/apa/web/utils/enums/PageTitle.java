package cl.apa.web.utils.enums;

/**
 * Created by bdumitru on 8/16/2017.
 */
@SuppressWarnings("unused")
public enum PageTitle {

    QA0_REPORT("QA0 HTML Report"),
    QA2_REPORT(""),
    AQUA("AQUA"),
    RELEASE_NOTES("Welcome to the Science Portal at NRAO"),
    QA0("QA0 AQUA"),
    QA2("QA2 AQUA"),
    PROTRACK("ALMA Project Tracker");

    private final String value;

    PageTitle(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
