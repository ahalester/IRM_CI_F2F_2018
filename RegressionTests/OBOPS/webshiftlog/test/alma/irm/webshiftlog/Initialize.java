/*
 * NAME: Initialize.java
 * AUTHOR: Kyoko Nakamura
 * DATE: 2015-09-14
 * 
 * PURPOSE: 
 *  Initialization before starting tests, get the WebDriver,
 *   set a log directory and so on. 
 *  
 * PRE- AND POST-CONDITIONS:
 *  
 * MAINTENANCE:
 *   
 * ----------------------------------------------------------------
 * AMENDEMENTS
 * <date>		<name>	<what & why>
 * 2016-06-10	knaka	Updated for R2015.8
 * 2017-03-09	knaka	Updated for FEB2017
 * 2017-04-17	knaka	Put light documentation on header
 * ----------------------------------------------------------------
 */
package alma.irm.webshiftlog;

import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class Initialize {
	// Variables
	private WebDriver driver;								// web driver
	private int waitsec = 5;								// Implicit waiting time [seconds] for findElement(s), 5 sec seems good
	private Path capturePath;								// Path for captures
	private String logDir = "/tmp/webslt";					// Directory to save log files	
	private String captureTs = "'/tmp/webslt/'yyyyMMdd";	// Sub directory's format for captures
//	private String captureTs = "'/tmp/webslt/'yyyyMMddHHmmss";
	
	// Getter for driver
	public WebDriver getDriver() {
		return driver;
	}

	// Getter for logDir
	public String getLogDir() {
		return logDir;
	}

	// Getter for capturePath
	public Path getCapturePath() {
		return capturePath;
	}

	// Constructor
	Initialize(){
		// Get a web driver with preferences
		driver = new FirefoxDriver(setMyProfile(logDir));

		// Set "implicit wait" for finding web elements (findElement(s)) at any time.
		// In case of no setting, findElement(s) occurs error FREQUENTLY!!
		driver.manage().timeouts().implicitlyWait(waitsec, TimeUnit.SECONDS);

		try{
			capturePath = Paths.get(new SimpleDateFormat(captureTs).format(new Date()));
			Files.createDirectories(capturePath);
		}catch(AccessDeniedException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*
	 * @brief Set a profile to save a log file of webdriver and set a download automatically
	 * 		  Note that this does NOT save logs which System.out.println() shows,
	 * 		  But only stored the webdriver's logs
	 * TODO(04-23) Add logs from System.out.println() if possible
	 * TODO(04-30) Download directory should be specified by date
	 */
	private FirefoxProfile setMyProfile(String logdir){
		FirefoxProfile profile = new FirefoxProfile();

		// Set the log file of webdriverx
		profile.setPreference("webdriver.log.file", logdir + "/webslt.log");
		
		// Set the download policy, that is, save a file AUTOMATICALLY (A dialog popup doesn't open)
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.dir", logdir);
		profile.setPreference("browser.download.useDownloadDir", true);
		// The down loaded files are a plain text, a csv file and a zip file 
		// Note that the mime type for zip here is not "application/zip", but "compressed" one
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/plain,text/csv,application/x-zip-compressed");
		
		return profile;
	}

}
