/*
 * NAME: ShiftlogToolAutotestSuite4.java
 * AUTHOR: Kyoko Nakamura
 * DATE: 2015-09-14
 * 
 * PURPOSE: 
 *  Verification of WebSLT with reports from entries.
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
 * 2017-09-22	knaka	#301 Fixed a bug in case of no entries
 * 2018-05-16	knaka	#301 Changed to confirm a menu appearance
 * 2018-05-21	knaka	Changed to use Typenum to get Types' position
 * 2018-06-01	knaka	Changed the procedure of #301 and #302
 * ----------------------------------------------------------------
 */
package alma.irm.webshiftlog;

import java.io.File;
import java.nio.file.Path;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertThat;	
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;

import static alma.irm.webshiftlog.Definitions.*;

@RunWith(OrderedRunner.class)

public class ShiftlogToolAutotestSuite4 {
	// Class variables
	// These variables are set in setUp()
	private static WebDriver driver;			// Web driver
	private static PopupProcedure popProc;		// Instance for PopupProcedure
	private static CommonTools cmt;				// Instance for CommonTools class
	private static Path capturePath;			// Capture directory

	String zClassMenuP = "z-menupopup";			// Menu popup
	String zClassMenu = "z-menu";				// Menu item which open the second menu
	String zClassMenuItem = "z-menuitem";		// Menu item on the menu popup

	private final String selectedInterval = "Other";	// Interval "Other" is needed for testing

	// Menu items on the menu popups
	//  the first menu
	int topPopup = 0;									// the number of the menu popup which is appeared as first
	private final String[] labelTopPopup = new String[] {"Produce PDF report","Produce HTML report","Produce CSV report",
										  			   "Produce TWiki report","Daily Report"};

	//  children menu
	private final String[][] labelChildPopup = new String[][]{
			     				{"Selected entries","All entries","Selected shift","Downtime statistics","End of Night"},
			     				{"Selected entries","All entries","Selected shift","Downtime statistics","End of Night"},
			     				{"Downtime statistics"},
			     				{"End of Night"}
				 				};

	// Availableness of each menu except SHIFT (SHIFT is all available)
	private final Boolean[][] judgeChildGeneral = new Boolean[][]{
								{true,true,false,false,false},
			  					{true,true,false,false,false},
			  					{false},
			  					{false},
								};

	// Strings in <a href> on each menu item
	// Full strings are begin with:"ShiftlogReportCreatorServlet?"
	String cssStr[] = new String[]{
			"a[href*='selentries']",			// reportType=GENERIC&reportFormat=PDF(HTML)&sidx=selentries
			"a[href*='allentries']",			// reportType=GENERIC&reportFormat=PDF(HTML)&sidx=allentries
			"a[href*='SHIFT']",					// reportType=SHIFT&reportFormat=PDF(HTML)
			"a[href*='ENGINEERING_DOWNTIME']",	// reportType=ENGINEERING_DOWNTIME&reportFormat=PDF(HTML,CSV)
			"a[href*='END_OF_NIGHT']",			// reportType=END_OF_NIGHT&reportFormat=PDF(HTML,TWIKI)
			"a[href*='DAILY']"					// reportType=DAILY&reportFormat=TXT
	};
	int cssGenOne = 0;
	int cssGenAll = 1;
	int cssShift = 2;
	int cssEngDown = 3;
	int cssEoN = 4;
	int cssDaily = 5;
	
	// Created report shows its url on the browser like below (the string is a part of it)
	String reportUrl = BASEURL.concat("ShiftlogReportCreatorServlet?reportType=");
	String format[] = new String[]{"PDF","HTML"};				// Format name

	// Title texts (or the first sentence) in the html page
	String htmlTexts[] = new String[]{"Timeline",								// Generic report
									  "Shift report",							// Shift report
									  "ENGINEERING DOWNTIME STATISTICS REPORT",	// Downtime statistics report
									  "End of Night Report",					// EoN report
	  								  "Daily Report"};							// Daily report
	int htmlGeneric = 0;
	int htmlShift = 1;
	int htmlDown = 2;
	int htmlEoN = 3;
	int htmlDaily = 4;
	
	// The title of a report with PDF format is always the below one:
	String pdfTitle = "ShiftlogReportCreatorServlet (application/pdf Object)";
	
	// Row on Entry list
	int rowFirst = 0;							// the first row
	int rowSecond = 1;					// the second row
	
	/*
	 * Create instance HERE
	 * TODO(2015-06-10) I'm not sure but test doesn't work as expected in case this instance is put in setUp
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
		/*
		 * See note in Suite3, there is the same behavior.
		 */
		// Close the browser     
		driver.quit();
		log("----- All tests just finished -----");
		System.out.println("The Web Driver and the Browser were CLOSED. Bye!");
	}

	/*--------------------------------------------------------------------------------
	 *				Do login/logout explicitly before/after testing 
	 *--------------------------------------------------------------------------------*/
	@Test
	@Order(order=0)
	public void preparation(){ loginAsFirst(); }
	@Test
	@Order(order=999)
	public void finishing(){ logoutAtLast(); }

	/*--------------------------------------------------------------------------------
	 *				All test cases
	 *--------------------------------------------------------------------------------*/
	@Test
	@Order(order=301)
	public void testCase301(){ sortsOfReport(); }
	@Test
	@Order(order=302)
	public void testCase302(){ genericReports(); }
	@Test
	@Order(order=303)
	public void testCase303(){ shiftReport(); }
	@Test
	@Order(order=304)
	public void testCase304(){ dailyReport(); }
	@Test
	@Order(order=305)
	public void testCase305(){ downtimeReport(); }
	@Test
	@Order(order=306)
	public void testCase306(){ nightReport(); }
	// (2016-02-03)Note that "downtimeReportIgnored()" must run the last. 
	// Because after that, any created reports are shown on a tab, not a new window
	@Test
	@Order(order=307)
	public void testCase307(){ downtimeReportIgnored(); }

	/*--------------------------------------------------------------------------------
	 * 			Each of the test cases 
	 *--------------------------------------------------------------------------------*/

	/*
	 * Before testing, do login
	 * Note that baseProc.login() is a non-static method, thus, it cannot be put into setUp()
	 */
	public void loginAsFirst(){
		log("========== Before testing, do login as first ==========");
		String screenshotName = "Suite3Default.png";

		// Logging in here at first
		baseProc.login(cmt,WEBTITLE,USERNAME,PASSWORD);

		// Report: Page title and URL
	    log("TITLE: " + driver.getTitle());
	    log("URL: " + driver.getCurrentUrl());
		// Report: the initial screenshot
	    baseProc.saveCurrentScreen(cmt,capturePath,screenshotName);
	}

	/*
	 * After all tests were done, do logout
	 * Note that baseProc.logout() is a non-static method, thus, it cannot be put into tearDown()
	 */
	public void logoutAtLast(){
		log("========== All tests have been done, it's time to logout ==========");

		// Logging out here
		baseProc.logout(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,Z_DIALOG_BTN,LOGOUT_YES,LOGOUT_DIALOG,LOGOUT_DONE);

	}

	/*
	 * Default appearance of menu popups 
	 */
	public void sortsOfReport(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		Boolean judge = false;
		
	    for(int typeId = 0; typeId < TYPE_NAME.length; typeId++){   // Look into a type one by one
			// Procedure: Open search popup and set interval for testing
	    	// 2018-06-01 Note that the following 5 statements need to put here, because many exceptions
	    	// occur and an execution sometimes hangs up if they are put outside for efficiency.
			baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
			popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,selectedInterval);
		    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
		    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);

		    // Procedure: Click one type and do search
		    log("[" + TYPE_NAME[typeId] + "]");
	    	popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,typeId,SELECT);
		    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
	    
		    // Procedure: Check summary entry and if no entry, this test has no meaning so skip it
	    	if(baseProc.isNoEntry(cmt, Z_ENTRY_INFO, NO_ENTRY)){
	    		log("Cannot test due to no entries. Skipped,");
	    		continue;
	    	}
	    	else{
	    		// Procedure: Click the top entry as first and then right click on it to show the menu popup
	    		baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,"Details");
	    		baseProc.showPopupMenu(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE);
			
	    		// TEST1: Verify the label on the first popup menu
	    		// Note that isEnabled() does NOT work for verifying the menu is clickable or not (all returns "true")
	    		//  On the other hand, in case of disablement, class name includes "-disd" like "z-menuitem-disd"
	    		//  So, check there is "-disd" in class attribution and judge it's enable or not.
	    		// 	2015-11-12 The above label "-disd" has been changed to "-disabled". 
	    		log(TUT + "1: Verify label names on the top popup");
	    		int n;
	    		for(n = 0; n < labelTopPopup.length-1; n++){	// last one is "menuitem" and not open any popup
	    			if(baseProc.checkText(cmt, labelTopPopup[n])){ // Wait until a menu is shown at 18-05-16
	    				judge = baseProc.isMenuSelectable(cmt,zClassMenuP,zClassMenu,topPopup,n,"class");
	    				doAssertThat(judge,is(equalTo(true)));
	    			}
	    		}
    			if(baseProc.checkText(cmt, labelTopPopup[n])){ // Wait until a menu is shown at 18-05-16
    				judge = baseProc.isMenuSelectable(cmt,zClassMenuP,zClassMenuItem,topPopup,0,"class");
    				if(typeId == Typenum.SHIFT.getId()) doAssertThat(judge,is(equalTo(true)));	// Only SHIFT is available
    				else					  			doAssertThat(judge,is(equalTo(false)));
    			}
    				
	    		// Procedure: Open the child popup menus
	    		int childId = 1;
	    		for(int i = 0; i < labelTopPopup.length-1; i++){	// Looping except the last one
	    			// Procedure: Hover the mouse on the top popup to show a child one
	    			baseProc.showSecondPopupMenu(cmt,zClassMenuP,zClassMenu,topPopup,i,labelChildPopup[i][0]);
	    			// TEST2: Verify the label on a child popup menu
	    			log(TUT + "2: Verify label names on a child popup");
	    			int j = 0;
	    			for(String expected: labelChildPopup[i]){
		    			if(baseProc.checkText(cmt, expected)){ // Wait until a menu is shown at 18-05-16
		    				judge = baseProc.isMenuSelectable(cmt,zClassMenuP,zClassMenuItem,childId,j,"class");
		    				if(typeId == Typenum.SHIFT.getId()) doAssertThat(judge,is(equalTo(true)));	// Only SHIFT is available
		    				else					  			doAssertThat(judge,is(equalTo(judgeChildGeneral[i][j])));
		    				j++;
		    			}
	    			}
	    			childId++;	// Needs to increment the child popup ID, which goes 2,3, and then 4
	    		}
	    	} // else{}
	    } // for{typeId}

	    // Procedure: Do Refresh to avoid a hanging up and then open search popup 
	    baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

		// Procedure: load defaults and close the search popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Create generic reports with PDF/HTML formats
	 */
	public void genericReports(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String ssHead = "GenericReport";			// The header of a screen shot filename
		String ssTail = ".png";						// The tail of a screen shot filename
		String screenshotName = "";					// Store the full filename for a screen shot
		String expectedUrl = reportUrl.concat("GENERIC&reportFormat=");
		String entries[] = new String[]{"selentries","allentries"};	// Generic report is created with one entry and all
		
    	// Procedure: Get the present window handle first of all
    	String winHandle = driver.getWindowHandle();
    	log(" Present window handle: " + winHandle);

	    // Procedure: Click a type one by one
	    for(int typeId = 0; typeId < TYPE_NAME.length; typeId++){
		    // Procedure: Open search popup and set interval for testing
			baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
			popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,selectedInterval);
		    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
		    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);

		    // Procedure: Click a type then do search 
		    log("[" + TYPE_NAME[typeId] + "]");
		    popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,typeId,SELECT);
		    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);

		    // Procedure: Check summary entry and if no entry, this test has no meaning so skip it
	    	if(baseProc.isNoEntry(cmt, Z_ENTRY_INFO, NO_ENTRY)){
	    		log("Cannot test due to no entries. Skipped,");
	    		continue;
	    	}
	    	
			int childId = 1;	// Second menu popup
			for(int fmt = 0; fmt < 2; fmt++){ // On the top menu, "0" is PDF format and "1" is HTML
				log("Format is: " + ((fmt == 0)? "PDF" : "HTML"));
				for(int id = 0; id < 2; id ++){	// On a child menu, "0" is selected entry and "1" is all
					// Procedure: Make up the full filename for the screen shot
					screenshotName = ssHead.concat(TYPE_NAME[typeId]).concat(format[fmt]).concat(entries[id]).concat(ssTail);

					// Procedure: Click the top entry as first and then right click on it to show a child
					baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,"Details");
					baseProc.showPopupMenu(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE);
					baseProc.showSecondPopupMenu(cmt,zClassMenuP,zClassMenu,topPopup,fmt,labelChildPopup[fmt][0]);
					String str = popProc.getText(cmt,zClassMenuP,zClassMenuItem,childId,id,REPPOPUP);
					log(" Report of: [" + str + "]");
					baseProc.createReport(cmt,zClassMenuP,cssStr[id],childId,0);

					// Procedure: Check a new window appeared and get the window handle
					doAssertThat(driver.getWindowHandles().size(),is(2));
					String winHandleNew = baseProc.getNewWindowHandle(cmt,winHandle);

					// Procedure: Go to the new window and wait until report is appeared
					if(fmt == 1) baseProc.goToAnotherWindowByText(cmt,winHandleNew,htmlTexts[htmlGeneric]);	// Check text on the loading page
					else 		 baseProc.goToAnotherWindow(cmt,winHandleNew,pdfTitle);			// Check title

					// Procedure: In case of HTML, get a screen shot
					//            Note that PDF stores a blank report, so don't handle it at the moment
					// Note that "org.openqa.selenium.WebDriverException" occurs if too much entries are shown
					// In case it happens, tune the interval to be shorter to get less entries.
					if(fmt == 1) baseProc.saveCurrentScreen(cmt,capturePath,screenshotName);

					// TEST1: Verify the contents here, but not enough yet. So a screenshot is gotten to check at the moment
					// TODO(2015-07-23) Consider to mature this test according to the each type!
			    	log(TUT + "1: Verify contents of Generic report");
			    	String expected = expectedUrl.concat(format[fmt]).concat("&sidx=").concat(entries[id]);
			    	log(" Expected url is: " + expected);
			    	doAssertThat(driver.getCurrentUrl(),is(expected));

			    	// Check only with HTML format (Test cannot on PDF)
			    	if(fmt == 1 && typeId != Typenum.WEATH.getId()){	// WEATH does not show any entries, so ignore it
				    	log(TUT + "1: Verify contents of Generic report of HTML");
			    		assertThat(baseProc.checkText(cmt,TYPE_NAME[typeId]),is(equalTo(true)));
			    	}

			    	// Procedure: Close the new window and back to the original one
					baseProc.backFromNewWindow(cmt,winHandle,WEBTITLE);
					doAssertThat(driver.getTitle(),is(WEBTITLE));	
				}
				childId++;	// Increment the child popup id
			} // for{fmt}
	    } // for{intId}

	    // Procedure: Do Refresh to avoid a hanging up and then open search popup 
	    baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

		// Procedure: load defaults and close the search popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * Create Shift Report
	 */
	public void shiftReport(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		int shiftId = 2;	// SHIFT report is on the third menu item
		String screenshotName = "ShiftReport.png";
		String expectedUrl = reportUrl.concat("SHIFT&reportFormat=");
		
    	// Procedure: Get the present window handle
    	String winHandle = driver.getWindowHandle();
    	log(" Present window handle: " + winHandle);

		// Procedure: Open search popup and set interval for testing, then check SHIFT and do search
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,selectedInterval);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
	    popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,Typenum.SHIFT.getId(),SELECT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);

	    // Procedure: Check summary entry and if no entry, this test has no meaning so skip it
	    if(baseProc.isNoEntry(cmt, Z_ENTRY_INFO, NO_ENTRY)) log("Cannot test due to no entries. Skipped,");
	    else{
	    	int childId = 1;	// Second menu popup
	    	for(int fmt = 0; fmt < 2; fmt++){ // Id on the first menu for PDF/HTML format
	    		// Procedure: Click the top entry as first and then right click on it to show the menu popups
	    		log("Format is: " + ((fmt == 0)? "PDF" : "HTML"));
	    		baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,"Details");
	    		baseProc.showPopupMenu(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE);
	    		baseProc.showSecondPopupMenu(cmt,zClassMenuP,zClassMenu,topPopup,fmt,labelChildPopup[fmt][0]);
	    		String str = popProc.getText(cmt,zClassMenuP,zClassMenuItem,childId,shiftId,REPPOPUP);
	    		log(" Report of: [" + str + "]");

	    		// Procedure: generate a report and verify the new window is appeared
	    		baseProc.createReport(cmt,zClassMenuP,cssStr[cssShift],childId,0);
	    		doAssertThat(driver.getWindowHandles().size(),is(2));
	    		String winHandleNew = baseProc.getNewWindowHandle(cmt,winHandle);
			
	    		// Procedure: Change the focus to the new window and wait until report is appeared
	    		if(fmt == 1) baseProc.goToAnotherWindowByText(cmt,winHandleNew,htmlTexts[htmlShift]);	// Check text on the loading page
	    		else 		 baseProc.goToAnotherWindow(cmt,winHandleNew,pdfTitle);			// Check title

	    		// TEST1: Verify the contents here, but not enough yet. So a screenshot is gotten to check at the moment
	    		// TODO(07-23) Consider to mature this test!
	    		baseProc.saveCurrentScreen(cmt,capturePath,screenshotName);
	    		log(TUT + "1: Verify contents of Shift report");
	    		doAssertThat(driver.getCurrentUrl(),is(expectedUrl.concat(format[fmt])));
	    		if(fmt == 1){	// Check only with HTML format (Test cannot on PDF)
	    			doAssertThat(baseProc.checkText(cmt,"Crew"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Software Information"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Antenna Information"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Weather Information"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Downtimes:"),is(equalTo(true)));
	    		}
		
	    		// Procedure: Close the new window and back to the original window
	    		baseProc.backFromNewWindow(cmt,winHandle,WEBTITLE);
	    		doAssertThat(driver.getTitle(),is(WEBTITLE));				// confirmation

	    		childId++;	// Increment the second menu popup id
	    	}
	    }
	    
		// Procedure: Refresh and open search popup again, load defaults then close it by Cancel
		baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * Create Daily Report
	 */
	public void dailyReport(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String screenshotName = "DailyReport.png";
		
    	// Procedure: Get the present window handle
    	String winHandle = driver.getWindowHandle();
    	log(" Present window handle: " + winHandle);

		// Procedure: Open search popup and set interval for testing, then check SHIFT and do search
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,selectedInterval);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
	    popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,Typenum.SHIFT.getId(),SELECT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);

    	// Procedure: Just in case doRefresh() here to avoid isNoEntry()'s failure 
    	// Added at 17-03-09
    	baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);

    	// Procedure: Check summary entry and if no entry, this test has no meaning so skip it
	    if(baseProc.isNoEntry(cmt, Z_ENTRY_INFO, NO_ENTRY)) log("Cannot test due to no entries. Skipped,");
	    else{
	    	// Procedure: Click the top entry and then right click on it to show the menu popup, then select a report
	    	baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,"Details");
	    	baseProc.showPopupMenu(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE);
	    	baseProc.createReport(cmt,zClassMenuP,cssStr[cssDaily],topPopup,0);
		
	    	// Procedure: Check a new window is appeared and then go to that window
	    	doAssertThat(driver.getWindowHandles().size(),is(2));
	    	String winHandleNew = baseProc.getNewWindowHandle(cmt,winHandle);
	    	baseProc.goToAnotherWindowByText(cmt,winHandleNew,htmlTexts[htmlDaily]);
		
	    	// TEST1: Verify the contents here, but not enough yet. So a screenshot is gotten to check at the moment
	    	// TODO(07-23) Consider to mature this test!
	    	baseProc.saveCurrentScreen(cmt,capturePath,screenshotName);
	    	log(TUT + "1: Verify contents of Daily report");
	    	doAssertThat(baseProc.checkText(cmt,"Shift Log:"),is(equalTo(true)));
	    	doAssertThat(baseProc.checkText(cmt,"OPERATING CONDITIONS"),is(equalTo(true)));
	    	doAssertThat(baseProc.checkText(cmt,"OBSERVATIONS"),is(equalTo(true)));
	    	doAssertThat(baseProc.checkText(cmt,"Technical Downtime:"),is(equalTo(true)));
	    	doAssertThat(baseProc.checkText(cmt,"Weather Downtime:"),is(equalTo(true)));
	    	doAssertThat(baseProc.checkText(cmt,"Execution Efficiency:"),is(equalTo(true)));
	    	doAssertThat(baseProc.checkText(cmt,"12-m Array"),is(equalTo(true)));
	    	doAssertThat(baseProc.checkText(cmt,"7-m Array"),is(equalTo(true)));
	    	doAssertThat(baseProc.checkText(cmt,"TP Array"),is(equalTo(true)));
	    	doAssertThat(baseProc.checkText(cmt,"PROBLEMS"),is(equalTo(true)));
    	
	    	// Procedure: Close the new window and back to the original window
	    	baseProc.backFromNewWindow(cmt,winHandle,WEBTITLE);
	    	doAssertThat(driver.getTitle(),is(WEBTITLE));				// confirmation
	    }
	    
    	// Procedure: Refresh and open search popup again, load defaults then close it by Cancel
		baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Downtime statistics Report is not selected with two entries
	 */
	public void downtimeReportIgnored(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;					// Initialize counters
		int pos_2ndPopup = 3;						// The location of Downtime report on the second popup
		int pos_1stPopup = 2;						// The location of Downtime report on the first popup
		Boolean judge = true;

		// Procedure: Open search popup and set interval for testing, then check SHIFT and do search
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,selectedInterval);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
	    popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,Typenum.SHIFT.getId(),SELECT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);

    	// Procedure: Just in case doRefresh() here to avoid isNoEntry()'s failure 
    	// Added at 17-03-09
    	baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);

    	// Procedure: Check summary entry and if no entry, this test has no meaning so skip it
	    if(baseProc.isNoEntry(cmt, Z_ENTRY_INFO, NO_ENTRY)) log("Cannot test due to no entries. Skipped,");
	    else{
	    	// Procedure: Click two items and see PDF/HTML reports
	    	baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,"Details");	// Need this click as first!
	    	baseProc.clickTwoEntries(cmt,Z_LISTITEM,rowFirst,rowSecond);
	    	baseProc.showPopupMenu(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE);
	    	int childId = 1;
	    	for(int fmt = 0; fmt < 2; fmt++){	// Check only PDF and HTML formats
	    		baseProc.showSecondPopupMenu(cmt,zClassMenuP,zClassMenu,topPopup,fmt,labelChildPopup[fmt][0]);

	    		// TEST1: Verify the label is unavailable
	    		judge = baseProc.isMenuSelectable(cmt,zClassMenuP,zClassMenuItem,childId,pos_2ndPopup,"class");
	    		log(TUT + "1: Verify [" + labelChildPopup[fmt][pos_2ndPopup] + "] on [" + labelTopPopup[fmt] + "] is unavailbale");
	    		doAssertThat(judge,is(equalTo(false)));	// not available with two entries 
	    		childId++;
	    	}
	    	// Procedure: Go to the CSV report
	    	baseProc.showSecondPopupMenu(cmt,zClassMenuP,zClassMenu,topPopup,pos_1stPopup,labelChildPopup[pos_1stPopup][0]);

	    	// TEST1: Verify the label is unavailable
	    	judge = baseProc.isMenuSelectable(cmt,zClassMenuP,zClassMenuItem,childId,0,"class");	// The menu popup have only one item
	    	log(TUT + "1: Verify [" + labelChildPopup[pos_1stPopup][0] + "] on [" + labelTopPopup[pos_1stPopup] + "] is unavailbale");
	    	doAssertThat(judge,is(equalTo(false)));	// not available with two entries 
	    }
	    
	    // Procedure: Refresh and open search popup again, load defaults then close it by Cancel
		baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Create Downtime statistics Report
	 */
	public void downtimeReport(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;					// Initialize counters
		int pos_2ndPopup = 3;						// The location of Downtime report on the second popup
		int pos_1stPopup = 2;						// The location of Downtime report on the first popup
		String expectedUrl = reportUrl.concat("ENGINEERING_DOWNTIME&reportFormat=");
		String screenshotName = "DowntimeStatisticsReport.png";
		
    	// Procedure: Get the present window handle
    	String winHandle = driver.getWindowHandle();
    	log(" Present window handle: " + winHandle);

    	// Procedure: Open search popup and set interval for testing, then check SHIFT and do search
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,selectedInterval);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
	    popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,Typenum.SHIFT.getId(),SELECT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);

    	// Procedure: Just in case doRefresh() here to avoid isNoEntry()'s failure 
    	// Added at 17-03-09
    	baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);

    	// Procedure: Check summary entry and if no entry, this test has no meaning so skip it
	    if(baseProc.isNoEntry(cmt, Z_ENTRY_INFO, NO_ENTRY)) log("Cannot test due to no entries. Skipped,");
	    else{
	    	int childId = 1;
	    	for(int fmt = 0; fmt < 2; fmt++){	// Check only PDF and HTML formats
	    		// Procedure: Click the top entry as first and then right click on it to show the menu popups
	    		log("Format is: " + ((fmt == 0)? "PDF" : "HTML"));
	    		baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,"Details");
	    		baseProc.showPopupMenu(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE);
	    		baseProc.showSecondPopupMenu(cmt,zClassMenuP,zClassMenu,topPopup,fmt,labelChildPopup[fmt][0]);
	    		String str = popProc.getText(cmt,zClassMenuP,zClassMenuItem,childId,pos_2ndPopup,REPPOPUP);
	    		log(" Report of: [" + str + "]");

	    		// Procedure: Create a report and check a new window appeared, then get the window handle
	    		baseProc.createReport(cmt,zClassMenuP,cssStr[cssEngDown],childId,0);
	    		doAssertThat(driver.getWindowHandles().size(),is(2));
	    		String winHandleNew = baseProc.getNewWindowHandle(cmt,winHandle);

	    		// Procedure: Go to the new window and wait until report is appeared
	    		if(fmt == 1) baseProc.goToAnotherWindowByText(cmt,winHandleNew,htmlTexts[htmlDown]);// Check text on the loading page
	    		else 		 baseProc.goToAnotherWindow(cmt,winHandleNew,pdfTitle);			// Check title

	    		// TEST1: Verify the contents here, but not enough yet. So a screenshot is gotten to check at the moment
	    		// TODO(07-29) Consider to mature this test!
	    		baseProc.saveCurrentScreen(cmt,capturePath,screenshotName);
	    		log(TUT + "1: Verify contents of Downtime statistics report");
	    		doAssertThat(driver.getCurrentUrl(),is(expectedUrl.concat(format[fmt])));
	    		if(fmt == 1){	// Check only with HTML format (Test cannot on PDF)
	    			doAssertThat(baseProc.checkText(cmt,"Overall times "),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Array times"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Antennas"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Detail on failures"),is(equalTo(true)));
	    		}
		
	    		// Procedure: Close the new window and back to the original window
	    		baseProc.backFromNewWindow(cmt,winHandle,WEBTITLE);
	    		doAssertThat(driver.getTitle(),is(WEBTITLE));				// confirmation
				
	    		childId++;
	    	}
	   
	    	// Procedure: Do refresh and then select EoN report of TWIKI format
	    	baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
	    	log("Format is: CSV");
	    	baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,"Details");
	    	baseProc.showPopupMenu(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE);
	    	baseProc.showSecondPopupMenu(cmt,zClassMenuP,zClassMenu,topPopup,pos_1stPopup,labelChildPopup[pos_1stPopup][0]);
	    	// It needs childId is 1
	    	childId = 1;
	    	String str = popProc.getText(cmt,zClassMenuP,zClassMenuItem,childId,0,REPPOPUP);
	    	log(" Report of: [" + str + "]");
	    	baseProc.createReport(cmt,zClassMenuP,cssStr[cssEngDown],childId,0);
	    }
	    
	    // Procedure: Refresh and open search popup again, load defaults then close it by Cancel
		baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Create End of Night Report
	 */
	public void nightReport(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;					// Initialize counters
		int pos_2ndPopup = 4;						// The location of EoN report on the second popup
		int pos_1stPopup = 3;						// The location of EoN report on the first popup
		String expectedUrl = reportUrl.concat("END_OF_NIGHT&reportFormat=");
		String screenshotName = "NightReport.png";

    	// Procedure: Get the present window handle
    	String winHandle = driver.getWindowHandle();
    	log(" Present window handle: " + winHandle);

		// Procedure: Open search popup and set interval for testing, then check SHIFT and do search
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,selectedInterval);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
	    popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,Typenum.SHIFT.getId(),SELECT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);

	    // Procedure: Check summary entry and if no entry, this test has no meaning so skip it
	    if(baseProc.isNoEntry(cmt, Z_ENTRY_INFO, NO_ENTRY)) log("Cannot test due to no entries. Skipped,");
	    else{
	    	int childId = 1;
	    	for(int fmt = 0; fmt < 2; fmt++){	// Check only PDF and HTML formats
	    		// Procedure: Click the top entry as first and then right click on it to show the menu popups
	    		log("Format is: " + ((fmt == 0)? "PDF" : "HTML"));
	    		baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,"Details");
	    		baseProc.showPopupMenu(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE);
	    		baseProc.showSecondPopupMenu(cmt,zClassMenuP,zClassMenu,topPopup,fmt,labelChildPopup[fmt][0]);
	    		String str = popProc.getText(cmt,zClassMenuP,zClassMenuItem,childId,pos_2ndPopup,REPPOPUP);
	    		log(" Report of: [" + str + "]");

	    		// Procedure: Create a report and check a new window appeared, then get the window handle
	    		baseProc.createReport(cmt,zClassMenuP,cssStr[cssEoN],childId,0);
	    		doAssertThat(driver.getWindowHandles().size(),is(2));
	    		String winHandleNew = baseProc.getNewWindowHandle(cmt,winHandle);

	    		// Procedure: Go to the new window and wait until report is appeared
	    		if(fmt == 1) baseProc.goToAnotherWindowByText(cmt,winHandleNew,htmlTexts[htmlEoN]);// Check text on the loading page
	    		else 		 baseProc.goToAnotherWindow(cmt,winHandleNew,pdfTitle);			// Check title

	    		// TEST1: Verify the contents here, but not enough yet. So a screenshot is gotten to check at the moment
	    		// TODO(07-29) Consider to mature this test!
	    		baseProc.saveCurrentScreen(cmt,capturePath,screenshotName);
	    		log(TUT + "1: Verify contents of End of Night report");
	    		doAssertThat(driver.getCurrentUrl(),is(expectedUrl.concat(format[fmt])));
	    		if(fmt == 1){	// Check only with HTML format (Test cannot on PDF)
	    			doAssertThat(baseProc.checkText(cmt,"End of Night"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Staff"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Weather and Antenna Availability"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Timeline"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"JIRA Tickets"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Executives Information"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Calibration"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Downtime"),is(equalTo(true)));
	    			doAssertThat(baseProc.checkText(cmt,"Weather Graphics"),is(equalTo(true)));
	    		}
		
	    		// Procedure: Close the new window and back to the original window
	    		baseProc.backFromNewWindow(cmt,winHandle,WEBTITLE);
	    		doAssertThat(driver.getTitle(),is(WEBTITLE));				// confirmation
				
	    		childId++;
	    	}
	   
	    	// Procedure: Do refresh and then select EoN report of TWIKI format
	    	baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
	    	log("Format is: TWIKI");
	    	baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,"Details");
	    	baseProc.showPopupMenu(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE);
	    	baseProc.showSecondPopupMenu(cmt,zClassMenuP,zClassMenu,topPopup,pos_1stPopup,labelChildPopup[pos_1stPopup][0]);
	    	// It needs childId is 1
	    	childId = 1;
	    	String str = popProc.getText(cmt,zClassMenuP,zClassMenuItem,childId,0,REPPOPUP);
	    	log(" Report of: [" + str + "]");
	    	baseProc.createReport(cmt,zClassMenuP,cssStr[cssEoN],childId,0);
	    }
	    
	    // Procedure: Refresh and open search popup again, load defaults then close it by Cancel
		baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
}
