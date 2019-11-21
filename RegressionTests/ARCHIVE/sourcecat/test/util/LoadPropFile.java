package util;

import java.io.FileInputStream;
import java.util.Properties;

public class LoadPropFile {
	static Properties props;

	public Properties getProperties() {
		props = new Properties();
		String dir = System.getProperty("user.dir");
		String file = "/config/locators.properties";
		try {
			FileInputStream fis = new FileInputStream(dir + file);

			props.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return props;
	}

	public String getURL() {
		return getProperties().getProperty("URL");
	}

	public String getJdbcDriverClassName() {
		return getProperties().getProperty("jdbc.driverClassName");
	}

	public String getJdbcURL() {
		return getProperties().getProperty("jdbc.url");
	}

	public String getJdbcUsername() {
		return getProperties().getProperty("jdbc.username");
	}

	public String getJdbcPassword() {
		return getProperties().getProperty("jdbc.password");
	}

	public String getUsername() {
		return getProperties().getProperty("Username");
	}

	public String getPassword() {
		return getProperties().getProperty("Password");
	}

	public String getValidSourceNameALMA() {
		return getProperties().getProperty("ValidSourceNameALMA");
	}

	public String getValidSourceNameSESAME() {
		return getProperties().getProperty("ValidSourceNameSESAME");
	}

	public String getValidRA() {
		return getProperties().getProperty("ValidRA");
	}

	public String getValidDec() {
		return getProperties().getProperty("ValidDec");
	}

	public String getValidSearchRadius() {
		return getProperties().getProperty("ValidSearchRadius");
	}

	public String getValidUV() {
		return getProperties().getProperty("ValidUV");
	}

	public String getValidBand() {
		return getProperties().getProperty("ValidBand");
	}

	public String getValidFrequency() {
		return getProperties().getProperty("ValidFrequency");
	}

	public String getValidFlux() {
		return getProperties().getProperty("ValidFlux");
	}

	public String min(String element) {
		String[] parts = element.split("\\..");
		return parts[0];
	}

	public String max(String element) {
		String[] parts = element.split("\\..");
		return parts[1];
	}

	public String getValidTimeAfter() {
		return getProperties().getProperty("ValidTimeAfter");
	}

	public String getValidTimeBefore() {
		return getProperties().getProperty("ValidTimeBefore");
	}

	public String getValidDegree() {
		return getProperties().getProperty("ValidDegree");
	}

	public String getValidAngle() {
		return getProperties().getProperty("ValidAngle");
	}

	public String getValidCatalogue() {
		return getProperties().getProperty("ValidCatalogue");
	}

	public String getInvalidSourceNameALMA() {
		return getProperties().getProperty("InvalidSourceNameALMA");
	}

	public String getInvalidSourceNameSESAME() {
		return getProperties().getProperty("InvalidSourceNameSESAME");
	}

	public String getInvalidRA() {
		return getProperties().getProperty("InvalidRA");
	}

	public String getInvalidDec() {
		return getProperties().getProperty("InvalidDec");
	}

	public String getInvalidSearchRadius() {
		return getProperties().getProperty("InvalidSearchRadius");
	}

	public String getInvalidUV() {
		return getProperties().getProperty("InvalidUV");
	}

	public String getInvalidBand() {
		return getProperties().getProperty("InvalidBand");
	}

	public String getInvalidFrequency() {
		return getProperties().getProperty("InvalidFrequency");
	}

	public String getInvalidFlux() {
		return getProperties().getProperty("InvalidFlux");
	}

	public String getInvalidTimeAfter() {
		return getProperties().getProperty("InvalidTimeAfter");
	}

	public String getInvalidTimeBefore() {
		return getProperties().getProperty("InvalidTimeBefore");
	}

	public String getInvalidDegree() {
		return getProperties().getProperty("InvalidDegree");
	}

	public String getInvalidAngle() {
		return getProperties().getProperty("InvalidAngle");
	}

	public String getInvalidCatalogue() {
		return getProperties().getProperty("InvalidCatalogue");
	}

	public String getSourceType() {
		return getProperties().getProperty("SourceType");
	}

	public String getNewSourceType() {
		return getProperties().getProperty("NewSourceType");
	}

	public String getSourceTypeForAutomation() {
		return getProperties().getProperty("SourceTypeForAutomation");
	}
}
