package alma.aat.archive.web.utils;

/**
 * Created by ldoming on 30/05/2018
 **/
public enum Environment {

    PHAA_BASE_URL("http://phase-a1.hq.eso.org");

    private final String value;

    Environment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
