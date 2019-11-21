/*
 * NAME: ShiftlogToolAutotestSuite1.java
 * AUTHOR: Kyoko Nakamura
 * DATE: 2015-09-14
 * 
 * PURPOSE: 
 *  Verification of WebSLT in general.
 *  
 * PRE- AND POST-CONDITIONS:
 *  The webshiftlog.war is running on a CAS server, 
 *  and a valid user can login the webshiftlog.
 *  
 * MAINTENANCE:
 * - Definitions class maintains the local user information and URLs.
 * - Each test case is invoked in order, see OrderedRunner class.
 * - The execution order is managed by the defined annotation "Order".
 * - Initialize class executes the initialization.
 * - CommonTools class handles the WebDriver.
 * - BaseWindowProcedure and PopupProcedure classes call methods in CommonTools
 *  in order to manage web elements.   
 * 
 * ----------------------------------------------------------------
 * AMENDEMENTS
 * <date>		<name>	<what & why>
 * 2016-06-10	knaka	Updated for R2015.8
 * 2017-03-09	knaka	Updated for FEB2017
 * 2017-04-17	knaka	Put light documentation on header
 * 2017-05-18	knaka	Changed assertions for titles (cas,almaportal and aqua)
 * 2017-07-13	knaka	Added one parameter into sortList()
 * 2017-09-04	knaka	Ignored unnecessary assertions in statusPage()
 * 2017-09-14	knaka	Implemented refresh()
 * 2018-05-21	knaka	Changed to use Typenum to get Types' position
* ----------------------------------------------------------------
 */
package alma.irm.webshiftlog;

import java.io.File;
import java.nio.file.Path;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertThat;	
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static alma.irm.webshiftlog.Definitions.*;

@RunWith(OrderedRunner.class)
public class ShiftlogToolAutotestSuite1 {
	// Class variable
	// These variables are set in setUp()
	private static WebDriver driver;			// Web driver
	private static PopupProcedure popProc;		// Instance for PopupProcedure class
	private static CommonTools cmt;				// Instance for CommonTools class
	private static Path capturePath;			// Capture directory
	
	// Local variables
	private final String sltDeniedTitle = "WebSLT - Access Denied";		// SLT title with no auth
	private final String partOfCasTitle = "Central Authentication Service";	// Part of CAS title
	private final String loginFailure = "Access Denied";				// Login rejection
	private final String statusTitle = "WebSLT status";					// Status title
	private final String partOfPortalTitle = "Science Portal";			// ALMA portal title
	private final String helpTitle = "SLT queries";						// Help for search title
	private final String[] helpLinks = new String[]{					// Section links in the help page
		    			"Introduction","Search fields","Search Expressions",
		    			"Reproducibility","Additional Notes"};
	private final String zclassGrid = "z-grid";							// zk class in status.zul 
	private final String zclassRow = "z-row-content";					// zk class in status.zul, 15-11-11 updated
	private final int statusBuildPos = 1;								// Build Information position in status.zul
	
	private final String selectedInterval = "Other";

	/*
	 * Create instance HERE
	 * TODO(2015-06-10) I'm not sure but test doesn't work as expected in case the following is put into setUp
	 */
	private final BaseWindowProcedure baseProc = BaseWindowProcedure.open(driver,BASEURL);
	
	/*--------------------------------------------------------------------------------
	 *				setUp() and tearDown()
	 *--------------------------------------------------------------------------------*/	
	@BeforeClass
	public static void setUp(){
		System.out.println("A Web Driver is OPENED!");
		
		// Create and initialize the web driver and a log directory
		Initialize initialize = new Initialize();
		
		// Class variables are set here
		driver = initialize.getDriver();
		capturePath = initialize.getCapturePath();
		String names[] = new Throwable().getStackTrace()[0].getClassName().split("\\.");
		LOGNAME = capturePath.toString().concat("/").concat(names[names.length-1]).concat(".log");
		File file = new File(LOGNAME);
		if(file.exists()) file.delete();			// Newly create a log file. If that filename exists, delete it

		// Create instances
		popProc = new PopupProcedure();
		cmt = new CommonTools(driver);

		log("----- " + names[names.length-1] + " just starts -----");
		log("Initialization just finished.");
		log("Directory for outputs: " + capturePath.toString());
		log("Name of logfile: " + LOGNAME);
	}

	@AfterClass
	public static void tearDown() {	
       // Close the browser        
		driver.quit();
		log("----- All tests just finished -----");
		System.out.println("The Web Driver and the Browser were CLOSED. Bye!");
	}
	
	/*-------------------------------------------------------------------------------  
	 * All test cases 
	 * 2015-03-23 Note that all tests are done one after another with oder in order.
	 *-------------------------------------------------------------------------------*/
	@Test
	@Order(order=1)
	public void testCase001() { loginWithAuth(); }
	@Test
	@Order(order=2)
	public void testCase002() { loginWithNoAuth(); }
	@Test
	@Order(order=3)
	public void testCase003() { statusPage(); }
	@Test
	@Order(order=4) 
	public void testCase004() { searchPopup(); }
	@Test
	@Order(order=5)
	public void testCase005() { reportPopup(); }
	@Test
	@Order(order=6)
	public void testCase006() { helpPage(); }
	@Test
	@Order(order=7)
	public void testCase007() { almaPage(); }
	@Test
	@Order(order=8)
	public void testCase008() { refresh(); } 
	@Test
	@Order(order=9)
	public void testCase009() { goAndBackPage(); } 
	@Test
	@Order(order=10)
	public void testCase010() { sortColumns(); } 
	@Ignore
	@Test
	@Order(order=11)
	public void testCase011() { linkToAqua(); } 
	@Ignore
	@Test
	@Order(order=12)
	public void testCase012() { testForChangingAppearance(); } 

	
	/*--------------------------------------------------------------------------------
	 * 			Each of the test cases 
	 *--------------------------------------------------------------------------------*/

	/*
	 * Login-Logout(cancel)-Logout
	 */
	public void loginWithAuth(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String screenshotName = "Default.png";

		// Procedure: logging in
		baseProc.login(cmt,WEBTITLE,USERNAME,PASSWORD);
		
		// Report: Page title and URL
		log(" title: " + driver.getTitle());
	    log(" URL: " + driver.getCurrentUrl());
    	baseProc.saveCurrentScreen(cmt,capturePath,screenshotName);
    	
	    // TEST1: Verify the title of WebSLT
    	log(TUT + "1: Verify the WebSLT title");
    	doAssertThat(driver.getTitle(),is(WEBTITLE));
 		
	    // Procedure: cancel logging out
	    baseProc.logoutCancel(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,Z_DIALOG_BTN,LOGOUT_NO,LOGOUT_DIALOG);

	    // TEST2: Verify the logout dialog is closed
	    log(TUT + "2: Verify the logout dialog box is closed");
	    doAssertThat(baseProc.checkText(cmt,LOGOUT_DIALOG),is(equalTo(false)));

	    // Procedure: logging out
		baseProc.logout(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,Z_DIALOG_BTN,LOGOUT_YES,LOGOUT_DIALOG,LOGOUT_DONE);
		
		// TEST3: Verify the CAS title
		log(TUT + "3: Verify logout succeeded and the CAS title is shown");
		log(" expected: including \"" + partOfCasTitle + "\"");
		log(" obtained: \"" + driver.getTitle() + "\"");
	    doAssertThat(driver.getTitle().matches(".*"+partOfCasTitle+".*"),is(equalTo(true)));
		
		log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Login is access denied due to non authentific user
	 */
	public void loginWithNoAuth(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: logging in with an invalid user
		baseProc.loginWithNoAuth(cmt,sltDeniedTitle,USERNAME_WO_AUTH,PASSWORD_WO_AUTH);	

		// TEST1: Verify the logging in is denied
		log(TUT + "Verify login with access denied");
	    doAssertThat(driver.getTitle(),is(sltDeniedTitle));
	    doAssertThat(baseProc.checkText(cmt,loginFailure),is(equalTo(true)));

	    // Procedure: logout enforcedly
	    baseProc.logoutEnforced(cmt,LOGOUT_DONE,ENFORCED_LOGOUT_URL);
	    
	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * status.zul
	 */
	public void statusPage(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String screenshotName = "Status.png";

		// Procedure: logging in and go to the status page
	    baseProc.login(cmt,WEBTITLE,USERNAME,PASSWORD);
		baseProc.goToStatusPage(cmt,statusTitle,STATUSURL);
		
		// Report: Page title and some information
	    log(" Page Title: " + driver.getTitle());
  		popProc.getText(cmt,zclassGrid,zclassRow,statusBuildPos,1,STATUSPAGE);
	    popProc.getText(cmt,zclassGrid,zclassRow,statusBuildPos,3,STATUSPAGE);

	    // Report: Get a screenshot
	   	baseProc.saveCurrentScreen(cmt,capturePath,screenshotName);
	   	
	    // TEST1: Verify title and page contents
		log(TUT + "1: Verify title and page contents");
	    doAssertThat(driver.getTitle(),is(statusTitle));
//		(baseProc.checkText(cmt,"User Information"),is(equalTo(true)));
	    doAssertThat(baseProc.checkText(cmt,"Build Information"),is(equalTo(true)));
//	    doAssertThat(baseProc.checkText(cmt,"Runtime information"),is(equalTo(true)));
	    doAssertThat(baseProc.checkText(cmt,"System information"),is(equalTo(true)));
	    doAssertThat(baseProc.checkText(cmt,"Database sources"),is(equalTo(true)));
//	    doAssertThat(baseProc.checkText(cmt,"Session Factories"),is(equalTo(true)));
	    doAssertThat(baseProc.checkText(cmt,"ZK Server Statistics for /webshiftlog"),is(equalTo(true)));
//	    doAssertThat(baseProc.checkText(cmt,"Beans used by /webshiftlog"),is(equalTo(true)));
	    
	    // Procedure: Back to the previous page
		baseProc.backToPreviousPage(cmt,WEBTITLE);

		// TEST2: Verify the title of WebSLT
		log(TUT + "2: Verify the WebSLT title");
	    doAssertThat(driver.getTitle(),is(WEBTITLE));

	    // Procedure: Logging out
	    baseProc.logout(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,
				             Z_DIALOG_BTN,LOGOUT_YES,LOGOUT_DIALOG,LOGOUT_DONE);

		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	/*
	 * Search popup contents and cancel button
	 */
	public void searchPopup(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Declaration
		String msgBefore;										// Entry message before action
		String msgAfter;										// Entry message after action
		String missMsg = "Miss getting the entry message";		// Missing for the entry message

		// Procedure: logging in and get entry summary message
	    baseProc.login(cmt,WEBTITLE,USERNAME,PASSWORD);

	    // Procedure: if no doRefresh(), isNoEntry() fails to get an entry message 
    	// Added at 17-07-24
    	baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
		if((msgBefore = baseProc.getEntryMsg(cmt,Z_ENTRY_INFO)) == null) msgBefore = missMsg;

		// Procedure: open search popup
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		
		// Test1: Verify the contents of search popup
		log(TUT + "1: Verify contents in search popup");
	    doAssertThat(baseProc.checkText(cmt,"Interval"),is(equalTo(true)));
	    doAssertThat(baseProc.checkText(cmt,"General"),is(equalTo(true)));
	    doAssertThat(baseProc.checkText(cmt,"Execution Information"),is(equalTo(true)));
	    doAssertThat(baseProc.checkText(cmt,"Engineering"),is(equalTo(true)));
	    doAssertThat(baseProc.checkText(cmt,"Observing Cycle"),is(equalTo(false)));// Verify Execution Inf. doesn't open
	    doAssertThat(baseProc.checkText(cmt,"Groups"),is(equalTo(false)));			// Verify Enfgineering doesn't open
	    
		// Procedure: cancel search
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
	    	    
    	// Procedure: if no doRefresh(), isNoEntry() fails to get an entry message 
    	// Added at 17-06-05
    	baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);

    	// Test2: Verify the entry summary message is identical with the previous one
		log(TUT + "2: Verify the summary message is identical with the previous one");
		if((msgAfter = baseProc.getEntryMsg(cmt,Z_ENTRY_INFO)) == null) msgAfter = missMsg;
		doAssertThat(msgAfter,is(msgBefore));
	    
	    // Procedure: logging out
		baseProc.logout(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,Z_DIALOG_BTN,LOGOUT_YES,LOGOUT_DIALOG,LOGOUT_DONE);

	    log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	/*
	 * Report popup contents and cancel button
	 */
	public void reportPopup(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: logging in
	    baseProc.login(cmt,WEBTITLE,USERNAME,PASSWORD);

	    // Procedure: open search popup
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_REPORT,CHECK_POPUP_REP);
		
		// Test1: Verify the contents
		log(TUT + "1: Verify report contents");
	    doAssertThat(baseProc.checkText(cmt,"Format"),is(equalTo(true)));
	    doAssertThat(baseProc.checkText(cmt,"Type of report"),is(equalTo(true)));
		
		// Procedure: cancel search
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,REPORT_CANCEL,CHECK_POPUP_REP);

	    // Test2: Verify the popup is deleted
	    log(TUT + "2: Verify the popup is closed");
	    doAssertThat(baseProc.checkText(cmt,CHECK_POPUP_REP),is(equalTo(false)));
	    
	    // Procedure: logging out
		baseProc.logout(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,Z_DIALOG_BTN,LOGOUT_YES,LOGOUT_DIALOG,LOGOUT_DONE);

	    log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Help page for "About search..."
	 */
	public void helpPage(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String winHandle = null;
		String winHandleNew = null;
		
		// Procedure: logging in
	    baseProc.login(cmt,WEBTITLE,USERNAME,PASSWORD);

	    // Procedure: Get the window handle
	    winHandle = driver.getWindowHandle();
		log(" Present window handle: " + winHandle);

		// Procedure: Show Help in another window
		baseProc.showHelpPage(cmt,Z_TOOLBAR_BTN,BASE_HELP);

		// Test1: Verify the new window is appeared. 
		log(TUT + "1: Verify the new window is appeared");
		doAssertThat(driver.getWindowHandles().size(),is(2));
		winHandleNew = baseProc.getNewWindowHandle(cmt,winHandle);
		
		// Procedure: Change the focus to the new window
		baseProc.goToAnotherWindow(cmt,winHandleNew,helpTitle);
		
		// TEST2: Verify the title and contents
		log(TUT + "2: Verify the title and help contents");
	    doAssertThat(driver.getTitle(),is(helpTitle));
	    doAssertThat(baseProc.checkText(cmt,"Introduction"),is(equalTo(true)));
	    doAssertThat(baseProc.checkText(cmt,"Search fields"),is(equalTo(true)));
	    doAssertThat(baseProc.checkText(cmt,"Search Expressions"),is(equalTo(true)));
	    doAssertThat(baseProc.checkText(cmt,"Reproducibility"),is(equalTo(true)));
	    doAssertThat(baseProc.checkText(cmt,"Additional Notes"),is(equalTo(true)));
	    
	    // Procedure: Click links of section names one by one
	    for(int i = 0; i < helpLinks.length; i++){
	    	log("[" + helpLinks[i] + "]");
	    	popProc.clickLink(cmt, helpLinks[i]);
	    	popProc.waitAppearance(cmt,helpLinks[i]);

	    	// Note that url likes "...#Introduction", so split at "#" and use the second string(=[1])
	    	String[] urlStr = driver.getCurrentUrl().split("#",0);
	    	// Note that space is set by "%20" in the url string, thus change is to " " for the comparison
	    	urlStr[1] = urlStr[1].replace("%20", " ");
	    	
	    	// TEST3: Verify the url has its link name
	    	log(TUT + "3: Verify the linked position is shown in url");
	    	doAssertThat(urlStr[1],is(helpLinks[i]));

	    	// Procedure: Back to the page top
	    	baseProc.backToPreviousPage(cmt,helpTitle);
	    }
	    
	    // Procedure: Close the help window and back to the original window
	    driver.close();											// CLose the new window
		driver.switchTo().window(winHandle);					// Focus the original window
	    doAssertThat(driver.getTitle(),is(WEBTITLE));				// confirmation
	    
	    // Procedure: logging out
		baseProc.logout(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,Z_DIALOG_BTN,LOGOUT_YES,LOGOUT_DIALOG,LOGOUT_DONE);

	    log(TU + "Finished");				
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * Go to ALMA Portal page and then back to WebSLT
	 */
	public void almaPage(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String screenshotName = "AlmaPortal.png";

		// Procedure: logging in and click the button
	    baseProc.login(cmt,WEBTITLE,USERNAME,PASSWORD);
		baseProc.goToAlmaPage(cmt,Z_TOOLBAR_BTN,BASE_ALMAPORTAL,partOfPortalTitle);

		log(" Page Title: " + driver.getTitle());
	    log(" URL: " + driver.getCurrentUrl());
	    baseProc.saveCurrentScreen(cmt,capturePath,screenshotName);
	    
    	// TEST1: Verify the title
		log(TUT + "1: Verify the title of Alma Portal");
		log(" expected: including \"" + partOfPortalTitle + "\"");
		log(" obtained: \"" + driver.getTitle() + "\"");
	    doAssertThat(driver.getTitle().matches(".*"+partOfPortalTitle+".*"),is(equalTo(true)));

	    // Procedure: Back to the previous page
		baseProc.backToPreviousPage(cmt,WEBTITLE);
		
	    // TEST2: Verify the title of WebSLT
		log(TUT + "2: Verify the WebSLT title");
	    doAssertThat(driver.getTitle(),is(WEBTITLE));
		
	    // Procedure: Logging out
		baseProc.logout(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,Z_DIALOG_BTN,LOGOUT_YES,LOGOUT_DIALOG,LOGOUT_DONE);

	    log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Refresh the entry list
	 */
	public void refresh(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");

        int line = 0;                   // Use first line of the entry list
        int cnt = 0;
        String[] column = new String[] {"","",""};

        // Procedure: logging in and open search popup
        baseProc.login(cmt,WEBTITLE,USERNAME,PASSWORD);
        baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

        // Procedure: Set some criteria (interval=Other,type=SHIFT)
        popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,selectedInterval);
        popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
        popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);

        // Procedure: Click SHIFT
        popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,Typenum.SHIFT.getId(),SELECT);

        // Procedure: do search and get the summary message
        popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);

        // Procedure: Get timestamp(A) of the 1st entry and keep it
        column[cnt] = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,line,CLM_TS,CLMS[CLM_TS]);

        // Procedure: Click a column header of Timestamp and get timestamp(B)
        baseProc.sortList(cmt,Z_PANELCHILD,Z_LISTHEAD,Z_SORTCARET,CLM_TS);
        cnt++;                  // Increment to save a value to the second location
        column[cnt] = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,line,CLM_TS,CLMS[CLM_TS]);

        // TEST1: Verify if timestamp(A) and timestamp(B) is different
        log(" timestamp(A): " + column[0] + ", (B): " + column[1]);
        doAssertThat(column[0].compareToIgnoreCase(column[1]) != 0,is(equalTo(true)));

        // Procedure: Click refresh, then sorted list should be aligned to the default one
        // Do twice because need to wait enough time to get updated strings at 17-09-20
        baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
        baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);

        // Procedure: Get timestamp(C) of the 1st entry and keep it
        cnt++;                  // Increment to save a value to the third location
        column[cnt] = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,line,CLM_TS,CLMS[CLM_TS]);

        // TEST2: Verify if timestamp(C) == timestamp(A)
        log(" timestamp(C): " + column[2]);
        doAssertThat(column[0],is(column[2]));

        // Procedure: Logging out
        baseProc.logout(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,Z_DIALOG_BTN,LOGOUT_YES,LOGOUT_DIALOG,LOGOUT_DONE);

        log(TU + "Finished");
        log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
        assertThat(ngAssert,is(0));                                                     // Show the error in case of NG
	}

	/*
	 * Go to another page and back
	 */
	public void goAndBackPage(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: logging in and open search popup
	    baseProc.login(cmt,WEBTITLE,USERNAME,PASSWORD);
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

	    // Procedure: Set some criteria (interval=Other,type=SHIFT)
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,selectedInterval);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
		popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,Typenum.SHIFT.getId(),SELECT);

		// TEST1: Verify the input data is shown
		log(TUT + "1: Verify the selected interval and the check box");
		doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,0),is(selectedInterval));
		doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,Typenum.SHIFT.getId()),is(equalTo(true)));
		
    	// Procedure: do search and get the summary message
    	popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
    	String details = "";
    	if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)){
	    	// Procedure: Click an entry and show information in details area
			baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,0,0,"Details");
			details = baseProc.getDetails(cmt,Z_DETAILS,Z_PANELCHILD,0,0);
    	}

    	// Procedure: Go to another web page
    	baseProc.goToAnotherWeb(cmt,driver,SAMPLEURL,SAMPLEURL_TITLE);
    	
    	// TEST2: Verify the new page is loaded
    	log(TUT + "2: Verify the title is expected");
    	doAssertThat(driver.getTitle(),is(SAMPLEURL_TITLE));
    	
	    // Procedure: Back to WebSLT and get details
		baseProc.backToPreviousPage(cmt,WEBTITLE);
		String detailsAfter = baseProc.getDetails(cmt,Z_DETAILS,Z_PANELCHILD,0,0);
  	
		// TEST3: Verify the information in details is identical
		log(TUT + "3: Verify the shown details is identical with the previous one");
		doAssertThat(details,is(detailsAfter));
		
	    // Procedure: Logging out
		baseProc.logout(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,Z_DIALOG_BTN,LOGOUT_YES,LOGOUT_DIALOG,LOGOUT_DONE);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Go to another page and back
	 */
	public void sortColumns(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		
		int asc = 0;			// Use in ascending order (1,2,3.../A,B,C...)
		int des = 1;			// Use in descending order (3,2,1.../Z,Y,X...)
		int first = 0;			// Use at the first column
		int second = 1;			// Use at the second column
		int getEnd = 1;			// Use to get timestamp, this case we get the end timestamp
		String[][] column = new String[][] {{"",""},{"",""}};
		
		// Procedure: logging in and open search popup
	    baseProc.login(cmt,WEBTITLE,USERNAME,PASSWORD);
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

	    // Procedure: Set some criteria (interval=Other,type=SHIFT)
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,selectedInterval);
//	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
//	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, ACT_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, ACT_END);

	    // Procedure: Click WEATH and then click "Invert", because many WEATH entries hide other entries
	    popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,10,SELECT);// Click WEATH
	    popProc.clickLink(cmt,TYPE_INVERT);
	    
    	// Procedure: do search and get the summary message
    	popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
		
    	// Procedure: if no doRefresh(), isNoEntry() fails to get an entry message 
    	// Added at 17-03-08
        // Do twice because need to wait enough time to get updated strings at 17-09-20
    	baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
    	baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);

		//  In case of no entry, abort test
    	if(baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)) log("Abort the test due to no entries");
        else{
    		// Procedure: Click a column header one by one
    		for(int i = 0; i < CLMS.length - 2; i++){
    			log(" Sort by [" + CLMS[i] + "]");
    	
    			// Procedure: Sort the entry list and get the top and tail column
    			for(int sort = asc; sort <= des; sort++){
    				baseProc.sortList(cmt,Z_PANELCHILD,Z_LISTHEAD,Z_SORTCARET,i);
    				column[sort][first] = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,first,i,CLMS[i]);
    				column[sort][second] = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,second,i,CLMS[i]);
    				// Get one Timestamp in case of durations
    				if(i == CLM_TS){
    					for(int pos = 0; pos < 2; pos++){
    						String type = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,pos,CLM_TYPE,CLMS[CLM_TYPE]);
    						column[sort][pos] = baseProc.getBeginOrEndTS(cmt,column[sort][pos],type,getEnd);
    					}
    				}	   			
    				// TEST1: Verify the order is [0] < [1] in ascending, [0] > [1] in descending
    				// Note that "T" > "r" in Des. order on WebSLT, but Java seems to think "T" < "r". 
    				//  In dictionary, "T" > "r" is right, so ignore "Case", then java can check it correctly
    				log(TUT + "1: Verify columns are alphabetically sorted");
    				log(" First: " + column[sort][first] + ", Second: " + column[sort][second]); // 17-05-23 Added for verification
    				if(sort == asc) doAssertThat(column[sort][first].compareToIgnoreCase(column[sort][second]) <= 0,is(equalTo(true)));
    				else            doAssertThat(column[sort][first].compareToIgnoreCase(column[sort][second]) >= 0,is(equalTo(true)));  
    			}	
    			// TEST2: Verify the top of the ascending and descending columns
    			log(TUT + "2: Verify columns are alphabetical sorting totally");
				log(" First: " + column[asc][first] + ", Second: " + column[des][second]); // 17-07-13 Added for verification
    			doAssertThat(column[asc][first].compareTo(column[des][first]) <= 0,is(equalTo(true)));
    		}
    	}
    	
	    // Procedure: Logging out
		baseProc.logout(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,Z_DIALOG_BTN,LOGOUT_YES,LOGOUT_DIALOG,LOGOUT_DONE);

    	log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Go to WebAQUA with the link on WebSLT
	 */
	public void linkToAqua(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String label = "ExecBlock UID";
		String linkName = "In AQUA";
		String partOfAquaTitle = "AQUA";
		
		// Procedure: logging in and open search popup
	    baseProc.login(cmt,WEBTITLE,USERNAME,PASSWORD);
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

	    // Procedure: Set some criteria (interval=Other,type=SHIFT)
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,selectedInterval);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);

	    // Procedure: Click SBEX
	    popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,8,SELECT);
  
    	// Procedure: do search and get the summary message
    	popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
    	//  In case of no entry, abort test
    	if(baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)) log("Abort the test due to no entries");
    	else{
    		// Procedure: Get the window handle
    		String winHandle = driver.getWindowHandle();
    		log(" Present window handle: " + winHandle);

    		// Procedure: Get one row in Details and check ExecBlockUID
    		String members[] = new String[]{"",""};
    		baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,0,0,label);
    		int rowMax = baseProc.getNumbersOfRow(cmt,Z_DETAILS,Z_ROW,0);		// Get numbers of row
    		int row;
    		for(row = 0; row < rowMax;row++){
    			String rowStr = baseProc.getDetails(cmt,Z_DETAILS,Z_ROW,0,row);	// Get one row
    			members = rowStr.split("\n");
    			if(members[0].contains(label)) break;		// In case the expected label is appeared, get out from the loop
    		}
    		if(row == rowMax) log("Cannot get EB uid. Test is aborted!");
    		else{
    			// TEST1: Verify the link
    			String tmpStrs[] = new String[]{"",""};
    			tmpStrs = members[1].split(" ",2);				// Split "uid://... In AQUA" to "uid://..." and "In AQUA"
    			String expectedUID = tmpStrs[0]; 
    			String linkPath = tmpStrs[1];
    			log(TUT + " Verify the link: [" + linkPath + "]");
    			doAssertThat(linkPath,is(linkName));

    			// Procedure: Click the link
    			baseProc.showWebAqua(cmt,linkPath);
   	
    			// Test2: Verify the new window is appeared and go there 
    			log(TUT + "2 Verify the new window is appeared");
    			doAssertThat(driver.getWindowHandles().size(),is(2));

    			// Procedure: Get new window's handle and go there
    			String winHandleNew = baseProc.getNewWindowHandle(cmt,winHandle);
    			baseProc.goToAnotherWindowByText(cmt,winHandleNew,expectedUID);
		
    			// TEST3: Verify the title and EB uid are shown
    			log(TUT + "3: Verify the title and EB uid of [" + expectedUID + "]");
    			log(" expected: including \"" + partOfAquaTitle + "\"");
    			log(" obtained: \"" + driver.getTitle() + "\"");
    		    doAssertThat(driver.getTitle().matches(".*"+partOfAquaTitle+".*"),is(equalTo(true)));
    			doAssertThat(baseProc.checkText(cmt,expectedUID),is(equalTo(true)));
	    
    			// Procedure: Close the help window and back to the original window
    			driver.close();												// CLose the new window
    			baseProc.goToAnotherWindowByText(cmt,winHandle,WEBTITLE);
    			doAssertThat(driver.getTitle(),is(WEBTITLE));				// confirmation
    		}
    	}
    	
	    // Procedure: Logging out
		baseProc.logout(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,Z_DIALOG_BTN,LOGOUT_YES,LOGOUT_DIALOG,LOGOUT_DONE);

    	log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * * * * * N O T I C E * * * * *
	 * (2015-07-17) THIS IS A TEST FOR MOVING HORIZONAL BAR
	 * (2015-07-24) This does NOT work because the bar is not fixed at the new position.
	 *              I've not found the solution yet, moreover, I found the same issue in Google.
	 *              I thinks it seems to have any trouble to handle it, so that it should be pending at the moment.
	 */
	public void testForChangingAppearance(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		
		// Procedure: logging in and open search popup
	    baseProc.login(cmt,WEBTITLE,USERNAME,PASSWORD);
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

	    // Procedure: Set Interval, marks all types except WEATH, then do search
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,selectedInterval);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
	    popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,10,SELECT);// Click WEATH
	    popProc.clickLink(cmt,TYPE_INVERT);
    	popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);

    	// Procedure: In case of no entry, abort the test
    	if(baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)) log("Abort the test due to no entries");
    	else{
    		// Procedure: Move the horizontal bar down
    		cmt.testForMovingBar();
    	}
    	
    	// Procedure: Logging out
		baseProc.logout(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,Z_DIALOG_BTN,LOGOUT_YES,LOGOUT_DIALOG,LOGOUT_DONE);

    	log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	    
}
