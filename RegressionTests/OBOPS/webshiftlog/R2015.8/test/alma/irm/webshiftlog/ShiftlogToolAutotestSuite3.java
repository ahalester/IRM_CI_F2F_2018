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

/*--------------------------------------------------------------------------------
 *					TestSuite 3: Verify WebSLT with DoReport
 *--------------------------------------------------------------------------------*/
@RunWith(OrderedRunner.class)

public class ShiftlogToolAutotestSuite3 {
	// Class variables
	// These variables are set in setUp()
	private static WebDriver driver;			// Web driver
	private static PopupProcedure popProc;		// Instance for PopupProcedure
	private static CommonTools cmt;				// Instance for CommonTools class
	private static String logDir;				// Log directory
	private static Path capturePath;			// Capture directory

	// Local variables
	// ZK Locators in report popup
	String ztypeRadio = "*//input[@type='radio']";	// For checking in "Format"
	String zclassLabel = "z-label";				// Label in General
	String zclassCaption = "z-caption";			// Popup title, Execution Information/Engineering bars
	String zclassRadioLabel = "z-radio-content";// Labels of Format, 15-11-11 updated
	String zclassComboBtn = "z-combobox-button";// Button of combo box, 15-11-12 updated
	String zclassCombo = "z-combobox-popup";	// Combo box of the Type menu, 15-11-11 updated
	String zclassComboItem = "z-comboitem";		// Item on Combo box in the Type menu
	String zclassList = "z-listbox-body";		// List box 
	String zclassListItem = "z-listitem";		// Items on the Project code list
	
	// Special position
	int popPos = 0;								// Position of zclassPopup, it's needed sometime
	int comboPPPos = 0;							// First position opening zclassCombo
	
	// Default
	String[] formatName = {"PDF","HTML","CSV"};
	int formatPDFPos = 0;
	int formatHTMLPos = 1;
	int formatCSVPos = 2;
	
	String[] typeOption = {"Project report","Weather report","Executions report"};
	int typeProjectPos = 0;						// Position of "Project report" in the Type menu
	int typeWeatherPos = 1;						// Position of "Weather report" in the Type menu
	int typeExecPos = 2;						// Position of "Executions report" in the Type menu

	// Label position
	int labelFormatPos = 0;						// "Format" label
	int labelTypePos = 1;						// "Type" label
	int labelProjectPos = 2;					// "Project Code" label
	int labelIntervalPos = 3;					// "Interval" label
	int labelSitePos = 5;						// "Site" label
	int labelLocationPos = 5;					// "Location" label

	// Menu(combo box) button position
	int menuTypeBtn = 0;						// Type
	int menuIntervalBtn = 1;					// Interval
	int menuSitePos = 2;						// Site
	int menuLocationPos = 3;					// Location

	// Text position
	int textTypePos = 0;						// Type 
	int textIntervalPos = 1;					// Interval
	int textBeginPos = 2;						// Begin
	int textEndPos = 3;							// End
	int textSitePos = 4;						// Site
	int textLocationPos = 4;					// Location
	
	// Project pane in popup
	int tablePos = 4;							// Position in z-tablechildren

	// Confirmation label
	String pcodeLabel = "Project Code:";		// Label to confirm "Project report" input area is opened
	String weatherLabel = "Site";				// Label to confirm "Weather report" input area is opened
	String execLabel = "Location";				// Label to confirm "Executions report" input area is opened

	// Menus
	// Note that Location menu is not a standard one, so do not define here.
	String[] intervalOption = {"Last 2 hours","Last 4 hours","Last 8 hours",
							   "Last day","Last week","Last month","Other"};
	String[] siteOption = {"AOS", "OSF", "OTHER"};
	int intervalOther = 6;

	// Graphical images in weather report
	String[] imgTitle = new String[]{
			"temperatures","humidity","pressure","windSpeed","windDirection","pwv"
		};

	// Others
	String execDateFormat = "yyyy_MM_dd'-'HH_mm_ss";
	String weatherHeading = typeOption[typeWeatherPos];
	int rowFirst = 0;										// the first row
	String titleOfPDF = "ShiftlogReportCreatorServlet (application/pdf Object)";

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
		logDir = initialize.getLogDir();
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
		 * Note(2015-04-24) There looks like a bug in Firefox. The following warning is shown at quitting 
		 *                  in case a new window was opened and then closed. This is resolved after
		 *                  Firefox 34.*. I'm not sure why it doesn't occur after showing Help page 
		 *                  on Test Suite 1.
		 *                  
		 *   Apr 24, 2015 2:03:39 AM org.openqa.selenium.os.UnixProcess$SeleniumWatchDog destroyHarder
		 *   INFO: Command failed to close cleanly. Destroying forcefully (v2). org.openqa.selenium.os.UnixProcess$SeleniumWatchDog@1e6a528
		 *   Apr 24, 2015 2:03:40 AM org.openqa.selenium.os.UnixProcess destroy
		 *   SEVERE: Unable to kill process with PID java.lang.UNIXProcess@13efefe
		 */

		// Close the browser     
		log("*** It's ignorable the message like \"Command failed to close cleanly\",\n" +
						   "*** which seems to be raised by a bug of Firefox.");
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
	@Order(order=201)
	public void testCase201(){ reportDefault(); }
	@Test
	@Order(order=202)
	public void testCase202(){ projectReport(); }
	@Test
	@Order(order=203)
	public void testCase203(){ projectReportMatching(); }
	@Test
	@Order(order=204)
	public void testCase204(){ projectReportMismatching(); }
	@Test
	@Order(order=205)
	public void testCase205(){ weatherReport(); }
	@Test
	@Order(order=206)
	public void testCase206(){ weatherReportWithOtherAndNoDetails(); }
	@Test
	@Order(order=207)
	public void testCase207(){ executionReport(); }
	
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
	 * Report popup in default appearance 
	 */
	public void reportDefault(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String screenshotName = "report.png";
		
		// Procedure: Save the date, then open report popup
		log(" Current date: " + popProc.getCurrentDate(cmt,FORMAT_DATE));
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_REPORT,CHECK_POPUP_REP);
		
		// Procedure: Get the initial screenshot
		baseProc.saveCurrentScreen(cmt,capturePath,screenshotName);
		
		// TEST1: Verify the default status
		log(TUT + "Verify the default status of the report popup");

		//--- Check1: the caption of the popup
		log(" Check1: the caption of the report popup");
		String caption;
		if((caption = popProc.getText(cmt,Z_POPUP,zclassCaption,0,0,REPPOPUP)) != "") 
			doAssertThat(caption,is("Report details"));
		
		//--- Check2: "Format" includes three labels and none of them are selected
		String format;
		log(" Check2: the format name and no radio buttons are selected");
		for(int i = 0; i < 3;i++){
			if((format = popProc.getText(cmt,Z_POPUP,zclassRadioLabel,popPos,i,REPPOPUP)) != "")
				doAssertThat(format,is(formatName[i]));
			doAssertThat(popProc.stateInSelection(cmt,Z_POPUP,ztypeRadio,popPos,i),is(equalTo(false)));
		}

		//--- Check3: "Type of report" selects nothing
		log(" Check3: no type is shown");
		String type = popProc.getInputText(cmt,Z_POPUP,Z_TYPETEXT,popPos,textTypePos);
		if(type == null) log(NOTEST);
		else			 doAssertThat(type,is(""));
		
		//--- Check4: The labels "Project Code" and "Interval" are not shown
		log(" Check4: Unexpected labels are not shown");
		doAssertThat(baseProc.checkText(cmt,"Project Code"),is(equalTo(false)));
		doAssertThat(baseProc.checkText(cmt,"Interval"),is(equalTo(false)));
		
		//--- Check5: [Generate Report] button is unavailable
		log(" Check5: \"Generate\" button is unavailable");
	    doAssertThat(popProc.getEnablement(cmt,Z_POPUP,Z_POPUP_BTN,REPORT_DONE),is(equalTo(false)));
		
		// Procedure: Close the report popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,REPORT_CANCEL,CHECK_POPUP_REP);

	    // Test2: Verify the logout button is enable because the popup was closed
		log(TUT + "2: Verify \"Log out\" button is enable");
	    doAssertThat(popProc.getEnablement(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT),is(equalTo(true)));
	    
	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * Project report in default
	 */
	public void projectReport(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String text;
		String list;
		
		// Procedure: Open report popup, open the menu of type and verify its names
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_REPORT,CHECK_POPUP_REP);
		popProc.clickImage(cmt,Z_POPUP,zclassComboBtn,popPos,menuTypeBtn);
		checkOption("Type",typeOption,comboPPPos);

		// Procedure: Select "Project report" from Type and confirmed it's appearance (TEST1)
		selectTypeAndConfirmed(comboPPPos,typeOption,typeProjectPos,textTypePos,labelProjectPos,pcodeLabel);
		
		// Procedure: Put a code and wait for the associated texts appeared, then select one from the list
		// Note that a code in the list needs to select for confirmation. 
		//           If not, [Generate Report] button doesn't become enable.
		popProc.putText(cmt,Z_TABLECHILD,Z_TYPETEXT,tablePos,rowFirst,PRJ_CODE);
		popProc.waitAppearance(cmt,PRJ_CODE);
		popProc.clickItem(cmt, Z_TABLECHILD, zclassListItem, tablePos, rowFirst);
		
		// Procedure: Get a shown project code in the input area and the list 
		text = popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,tablePos,rowFirst);
		list = popProc.getText(cmt,Z_TABLECHILD,zclassListItem,tablePos,rowFirst,REPPOPUP);
		
		// TEST2: Verify the project code and both of input areas are shown 
		log(TUT + "2: Verify the input project code is shown");
		if(text == null) log(NOTEST);
		else			 doAssertThat(text,is(PRJ_CODE));
		if(list == null) log(NOTEST);
		else			 doAssertThat(list,is(PRJ_CODE));

		// Procedure: Click CSV format and just wait a little. The check should be refused
		// TEST3: Verify the radio button and Generate button, which are not available
		selectFormatUnexpected(formatCSVPos);
		
    	// Procedure: Get the present window handle
    	String winHandle = driver.getWindowHandle();
    	log(" Present window handle: " + winHandle);
    	
    	// Procedure: Generate PDF and then HTML report
	    for(int pos = formatPDFPos; pos <= formatHTMLPos; pos++){
			// Procedure: Click HTML/PDF format and just wait a little. The check should be permitted
			// TEST4: Verify the radio button and Generate button, which are available
	    	selectFormat(pos);
		
	    	// Procedure: Click Generate Report button and go to the new window which shows a report
	    	generateReportOnNewWindow(formatName[pos],winHandle,PRJ_CODE);

			// TEST5: Verify the Project code
	    	log(TUT + "Verify Project code");
	    	doAssertThat(baseProc.checkText(cmt,PRJ_CODE),is(equalTo(true)));
		
	    	// Procedure: Close the new window and back to the original window
	    	baseProc.backFromNewWindow(cmt,winHandle,WEBTITLE);
	    	doAssertThat(driver.getTitle(),is(WEBTITLE));				// confirmation
	    }
	    	
	    // Procedure: Close the report popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,REPORT_CANCEL,CHECK_POPUP_REP);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * Project report with a gradually matched code
	 */
	public void projectReportMatching(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String text;
		String list;
		
		// Procedure: Open report popup and open the menu of type
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_REPORT,CHECK_POPUP_REP);
		popProc.clickImage(cmt,Z_POPUP,zclassComboBtn,popPos,menuTypeBtn);
		
		// Procedure: Select "Project report" from Type and confirmed it's appearance(TEST1)
		selectTypeAndConfirmed(comboPPPos,typeOption,typeProjectPos,textTypePos,labelProjectPos,pcodeLabel);

		// Procedure: Split the define code and then put them one by one
		//            and wait for the associated texts appeared
		// Note that "period" means any character by the regular expression, so needs to ignore it
		String[] pcodeArray = PRJ_CODE.split("\\.",0);
		String pcode = "";
		for(int i = 0; i < pcodeArray.length; i++){
			pcode = pcode + pcodeArray[i];
			log(" Putting string is: " + pcode);
			popProc.putText(cmt,Z_TABLECHILD,Z_TYPETEXT,tablePos,rowFirst,pcode);
			popProc.waitAppearance(cmt,pcode);
			// 2015-04-24: The following clickItem() *must* put here! Otherwise, 
			// cannot click a list item successfully. In case it's put outside this loop,
			// a WARNING happens at waitUntilEnablement(). I keep noticing this behavior for a while.
			popProc.clickItem(cmt, Z_TABLECHILD, zclassListItem, tablePos, rowFirst);
			pcode = pcode + ".";
		}
		
		// Procedure: Get the project code, input one and the shown one in the list 
		text = popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,tablePos,rowFirst);
		list = popProc.getText(cmt,Z_TABLECHILD,zclassListItem,tablePos,rowFirst,REPPOPUP);

		// TEST2: Verify the project code, input one and the shown one in the list 
		log(TUT + "2: Verify the input project code is shown");
		if(text == null) log(NOTEST);
		else			 doAssertThat(text,is(PRJ_CODE));
		if(list == null) log(NOTEST);
		else			 doAssertThat(list,is(PRJ_CODE));

		// Procedure: Get the present window handle
    	String winHandle = driver.getWindowHandle();
    	log(" Present window handle: " + winHandle);
    	
    	// Procedure: Generate PDF and then HTML report
	    for(int pos = formatPDFPos; pos <= formatHTMLPos; pos++){
			// Procedure: Click HTML/PDF format and just wait a little. The check should be permitted
			// TEST: Verify the radio button and Generate button, which are available
	    	selectFormat(pos);
		
	    	// Procedure: Click Generate Report button and go to the new window which shows a report
	    	generateReportOnNewWindow(formatName[pos],winHandle,PRJ_CODE);

			// TEST: Verify Project code
	    	log(TUT + "Verify Project code");
	    	doAssertThat(baseProc.checkText(cmt,PRJ_CODE),is(equalTo(true)));
		
	    	// Procedure: Close the new window and back to the original window
	    	baseProc.backFromNewWindow(cmt,winHandle,WEBTITLE);
	    	doAssertThat(driver.getTitle(),is(WEBTITLE));				// confirmation
	    }
	    	
	    // Procedure: Close the report popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,REPORT_CANCEL,CHECK_POPUP_REP);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Project report with no matched code
	 */
	public void projectReportMismatching(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String text;
		String list;
		
		// Procedure: Open report popup and open the menu of type
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_REPORT,CHECK_POPUP_REP);
		popProc.clickImage(cmt,Z_POPUP,zclassComboBtn,popPos,menuTypeBtn);
		
		// Procedure: Select "Project report" from Type and confirmed it's appearance (TEST1)
		selectTypeAndConfirmed(comboPPPos,typeOption,typeProjectPos,textTypePos,labelProjectPos,pcodeLabel);

		// Procedure: Click a PDF or HTML format. This is done without selecting any codes,
		//            so that the Generate button is still unavailable
		// TEST2: Verify the radio button is checked
		selectFormatOnly(formatPDFPos);

		// Procedure: Put a dummy project code which does not exist and wait before going ahead to the assertion
		String unexpected = "NOTEXISTED-" + PRJ_CODE;
		popProc.putTextWithWait(cmt,Z_TABLECHILD,Z_TYPETEXT,tablePos,rowFirst,unexpected);
		
		// Procedure: Get the project code and string in the list
		// Note that "z-listitem" is not shown due to no entry
		text = popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,tablePos,rowFirst);
		list = popProc.getText(cmt,Z_TABLECHILD,zclassList,tablePos,rowFirst,REPPOPUP);

		// TEST3: Verify project code is not shown and no codes in the list and Generate button is unavailable
		if(text == null || text == "") log(NOTEST);
		else						   doAssertThat(text,is(unexpected));
		if(list == null) log(NOTEST);
		else			 doAssertThat(list,is(""));
		doAssertThat(popProc.getEnablement(cmt,Z_POPUP,Z_POPUP_BTN,REPORT_DONE),is(equalTo(false)));
	
	    // Procedure: Close the report popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,REPORT_CANCEL,CHECK_POPUP_REP);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Weather report in default
	 */
	public void weatherReport(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String[] expectedTS = new String[2];	// expected Time stamps
		int comboPos = comboPPPos;			// Variable position of z-combobox-pp
		
		// Procedure: Open report popup and open the menu of type
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_REPORT,CHECK_POPUP_REP);
		popProc.clickImage(cmt,Z_POPUP,zclassComboBtn,popPos,menuTypeBtn);
		
		// Procedure: Select "Weather report" from Type and confirmed it shows "Site" (TEST1)
		selectTypeAndConfirmed(comboPos,typeOption,typeWeatherPos,textTypePos,labelSitePos,weatherLabel);
		comboPos++;							// Increment the position of zclassCombo for the next use
		
		// Procedure: Click Site and verify option names (TEST2)
		popProc.clickImage(cmt, Z_POPUP, zclassComboBtn, popPos, menuSitePos);
		popProc.waitAppearance(cmt,siteOption[1]);	//Wait for showing the site menu
		checkOption("Site",siteOption,comboPos);

		// Procedure: Click "mySite" from the site menu and verify the name shown in the textField (TEST3)
		selectOption(comboPos,siteOption,getPos(siteOption,MY_SITE),textSitePos);
		comboPos++;							// Increment the position of zclassCombo for the next use
		
		// Procedure: Click Interval and verify option names (TEST4), 
		//            then close the menu before going ahead the following loop
		popProc.clickImage(cmt, Z_POPUP, zclassComboBtn, popPos, menuIntervalBtn);
		checkOption("Interval",intervalOption,comboPos);
		popProc.clickImage(cmt, Z_POPUP, zclassComboBtn, popPos, menuIntervalBtn);

		// Procedure: Click CSV format and just wait a little. The check should be refused
		// TEST5: Verify the radio button and Generate button, which are not available
		selectFormatUnexpected(formatCSVPos);

    	// Procedure: Get the present window handle
    	String winHandle = driver.getWindowHandle();
    	log(" Present window handle: " + winHandle);

		// Procedure: Select one option from Interval, then generate reports
	    for(int i = 0;i < intervalOption.length;i++){	
	    	log("[" + intervalOption[i] + "]");
		    // Procedure: Open menu and change interval, then get Begin/End Timestamps (TEST6)
			popProc.clickImage(cmt, Z_POPUP, zclassComboBtn, popPos, menuIntervalBtn);
	    	selectOption(comboPos,intervalOption,i,textIntervalPos);
	    	
			expectedTS[0] = popProc.getDateWithFormat(cmt, Z_POPUP, Z_TYPETEXT, popPos, textBeginPos,FORMAT_REPORT,FORMAT_DATE);
			expectedTS[1] = popProc.getDateWithFormat(cmt, Z_POPUP, Z_TYPETEXT, popPos, textEndPos,FORMAT_REPORT,FORMAT_DATE);
			log(" Interval: [" + expectedTS[0] + " - " + expectedTS[1] + "]");

			// TODO(04-28) Need to check the date length, or it's doesn't need...?
			
	    	// Procedure: Generate PDF and then HTML report
			for(int pos = formatHTMLPos; pos <= formatHTMLPos; pos++){
				// Procedure: Click HTML/PDF format and then generate the report
		    	selectFormat(pos);
		    	generateReportOnNewWindow(formatName[pos],winHandle,weatherHeading);

				// TEST7: Verify strings in currentUrl because cannot confirm the contents of a PDF file
		    	log(TUT + "7: Verify the URL");
		    	String[] array = splitUrl();
		    	String beginDateOnReport = popProc.getUnixTimeDate(cmt,FORMAT_DATE,array[2]);
		    	String endDateOnReport = popProc.getUnixTimeDate(cmt,FORMAT_DATE,array[3]);
		    	log(" Interval on Report: [" + beginDateOnReport + "-" + endDateOnReport + "]");
		    	doAssertThat(array[0],is("WEATHER"));				// report type
		    	doAssertThat(array[1],is(formatName[pos]));		// report format
		    	doAssertThat(beginDateOnReport,is(expectedTS[0]));// Begin
		    	doAssertThat(endDateOnReport,is(expectedTS[1]));	// End
		    	doAssertThat(array[4],is("true"));				// details is "true"
		    	doAssertThat(array[5],is(MY_SITE));				// site
		    	
		    	// In case of HTML format, confirm its contents
		    	if(pos == formatHTMLPos){
			    	log(TUT + "7 additional: Verify the plottings");
		    		for(int n = 2;n < 8;n++){
		    			// Note that use XPath for pointing out the location
		    			String str = baseProc.getImgText(cmt,"//*/tr[5]/td[2]/table/tbody/tr[" + n + "]/td/img");
		    			String[] strParts = str.split("plotType=");
		    			doAssertThat(strParts[1],is(imgTitle[n-2]));
		    		}
		    	}
		    	
		    	// Procedure: Close the new window and back to the original window
		    	baseProc.backFromNewWindow(cmt,winHandle,WEBTITLE);
		    	doAssertThat(driver.getTitle(),is(WEBTITLE));				// confirmation
		    }
	    }

	    // Procedure: Close the report popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,REPORT_CANCEL,CHECK_POPUP_REP);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * Weather report with Other and no detail
	 */
	public void weatherReportWithOtherAndNoDetails(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String[] expectedTS = new String[2];	// expected Time stamps
		int comboPos = comboPPPos;				// Variable position of z-combobox-pp
		
		// Procedure: Open report popup and open the menu of type
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_REPORT,CHECK_POPUP_REP);
		popProc.clickImage(cmt,Z_POPUP,zclassComboBtn,popPos,menuTypeBtn);
		
		// Procedure: Select "Weather report" from Type and confirmed it shows "Site" 
		selectTypeAndConfirmed(comboPos,typeOption,typeWeatherPos,textTypePos,labelSitePos,weatherLabel);
		comboPos++;							// Increment the position of zclassCombo for the next use
		
		// Procedure: Open Site menu and Click "mySite" and verify the name shown in the textField
		popProc.clickImage(cmt, Z_POPUP, zclassComboBtn, popPos, menuSitePos);
		selectOption(comboPos,siteOption,getPos(siteOption,MY_SITE),textSitePos);
		comboPos++;							// Increment the position of zclassCombo for the next use
		
		// Procedure: Uncheck "Include details"
		popProc.clickCheckbox(cmt,Z_POPUP,Z_TYPECHKBOX,popPos,0,UNSELECT);
		
		// TEST1: Verify "Include details" is unchecked
		doAssertThat(popProc.stateInSelection(cmt,Z_POPUP,Z_TYPECHKBOX,popPos,0),is(false));

    	// Procedure: Get the present window handle
    	String winHandle = driver.getWindowHandle();
    	log(" Present window handle: " + winHandle);

	    // Procedure: Open menu and select "Other", then put Begin/End Timestamps
	    popProc.clickImage(cmt, Z_POPUP, zclassComboBtn, popPos, menuIntervalBtn);
	    selectOption(comboPos,intervalOption,intervalOther,textIntervalPos);
	    popProc.putDateOnReport(cmt,Z_POPUP, Z_TYPETEXT, popPos, textBeginPos, OTHER_BEGINREP);
	    popProc.putDateOnReport(cmt,Z_POPUP, Z_TYPETEXT, popPos, textEndPos, OTHER_ENDREP);
	    	
	    expectedTS[0] = popProc.getDateWithFormat(cmt, Z_POPUP, Z_TYPETEXT, popPos, textBeginPos,FORMAT_REPORT,FORMAT_DATE);
	    expectedTS[1] = popProc.getDateWithFormat(cmt, Z_POPUP, Z_TYPETEXT, popPos, textEndPos,FORMAT_REPORT,FORMAT_DATE);
	    log(" Interval: [" + expectedTS[0] + " - " + expectedTS[1] + "]");

	    // Procedure: Generate PDF and then HTML report
	    for(int pos = formatPDFPos; pos <= formatHTMLPos; pos++){
	    	// Procedure: Click HTML/PDF format and then generate the report
	    	selectFormat(pos);
	    	generateReportOnNewWindow(formatName[pos],winHandle,weatherHeading);

	    	// TEST2: Verify "details" is false and the others are not changed in currentUrl
	    	log(TUT + "Verify the URL");
	    	String[] array = splitUrl();
	    	String beginDateOnReport = popProc.getUnixTimeDate(cmt,FORMAT_DATE,array[2]);
	    	String endDateOnReport = popProc.getUnixTimeDate(cmt,FORMAT_DATE,array[3]);
	    	log(" Interval on Report: [" + beginDateOnReport + "-" + endDateOnReport + "]");
	    	doAssertThat(array[0],is("WEATHER"));				// report type
	    	doAssertThat(array[1],is(formatName[pos]));		// report format
	    	doAssertThat(beginDateOnReport,is(expectedTS[0]));// Begin
	    	doAssertThat(endDateOnReport,is(expectedTS[1]));	// End
	    	doAssertThat(array[4],is("false"));				// details is "false"
	    	doAssertThat(array[5],is(MY_SITE));				// site
		    	
	    	// Procedure: Close the new window and back to the original window
	    	baseProc.backFromNewWindow(cmt,winHandle,WEBTITLE);
	    	doAssertThat(driver.getTitle(),is(WEBTITLE));				// confirmation
	   }

	    // Procedure: Close thx[e report popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,REPORT_CANCEL,CHECK_POPUP_REP);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * Execution report in default
	 */
	public void executionReport(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		int comboPos = comboPPPos;				// Variable position of z-combobox-pp
		
		// Procedure: Open report popup and open the menu of type
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_REPORT,CHECK_POPUP_REP);
		popProc.clickImage(cmt,Z_POPUP,zclassComboBtn,popPos,menuTypeBtn);
		
		// Procedure: Select "Executions report" from Type and confirmed it shows "Location" (TEST1)
		selectTypeAndConfirmed(comboPos,typeOption,typeExecPos,textTypePos,labelLocationPos,execLabel);
		comboPos++;							// Increment the position of zclassCombo for the next use
		
		// Procedure: Click Location and get the position of myLocation in the menu, then select it (TEST2)
		// Note that members in the menu are not verified, because they are not stable
		popProc.clickImage(cmt, Z_POPUP, zclassComboBtn, popPos, menuSitePos);
		int locationOptionPos = getPositionInMenu(MY_LOCATION,comboPos);
		if(locationOptionPos == -1) log("Abort the test due to no location selected");
		else
			selectOption(comboPos,MY_LOCATION,locationOptionPos,textLocationPos);
		comboPos++;							// Increment the position of zclassCombo for the next use

		// Procedure: Click Interval and verify option names, then select Other with Begin/End timestamps
		popProc.clickImage(cmt, Z_POPUP, zclassComboBtn, popPos, menuIntervalBtn);
		checkOption("Interval",intervalOption,comboPos);
	    selectOption(comboPos,intervalOption,intervalOther,textIntervalPos);
	    popProc.putDateOnReport(cmt,Z_POPUP, Z_TYPETEXT, popPos, textBeginPos, OTHER_BEGINREP);
	    popProc.putDateOnReport(cmt,Z_POPUP, Z_TYPETEXT, popPos, textEndPos, OTHER_ENDREP);
		
		// Procedure: Click PDF/HTML format and just wait a little. The check should be refused
		// TEST3: Verify the radio button and Generate button, which are not available
		selectFormatUnexpected(formatHTMLPos); 
		selectFormatUnexpected(formatPDFPos); 

		// Procedure: Click CSV format and generate a report (TEST4)
		// Note that the file is saved on the default directory (See driver's preference at the top of this code) 
		selectFormat(formatCSVPos);
		int preFileTotal = baseProc.getTotalNumberOfFiles(cmt,logDir);	// Get the total file number at the log dir.
		popProc.generateReport(cmt,Z_POPUP,Z_POPUP_BTN,0,REPORT_DONE);

		// Wait for the new file will be appeared at the directory
		while(true){
			if(baseProc.getTotalNumberOfFiles(cmt,logDir) > preFileTotal) break;
			popProc.waiting(cmt,100);				// Need to wait at least 0.1 sec for creating of a file
		}

		// Procedure: Get the absolute file name which is expected
		// Note that file name includes the second which was produced and it's difficult to get it, 
		//           so check two seconds(current and current-1 second) just in case.
		// TODO(04-30) Need to check the contents of the generated file
		String execFilename[] = new String[]{"",""};
		execFilename[0] = logDir + "/shiftlog-executions-report-" + popProc.getCurrentDate(cmt,execDateFormat) + ".csv";
		execFilename[1] = logDir + "/shiftlog-executions-report-" + popProc.getNearDate(cmt,execDateFormat,-1) + ".csv";
		log(" exec filename: " + execFilename[0] + " / " + execFilename[1]);
		File file1 = new File(execFilename[0]);
		File file2 = new File(execFilename[1]);

		// TEST5: Verify the file, which is generated in the log dir.
		log(TUT + "5: Verify the absolute filename");
		doAssertThat(file1.exists() || file2.exists() ,is(equalTo(true)));
		
	    // Procedure: Close the report popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,REPORT_CANCEL,CHECK_POPUP_REP);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/***********************************************************************************************/

	/*
	 * @brief Get position of the label in a menu
	 * @param String[] menu
	 * @param String label, which is one of labels in the menu
	 */
	int getPos(String[] menu,String label){
		int pos;
		for(pos = 0; pos < menu.length; pos++) if(menu[pos].equals(label)) break;
		if(pos == menu.length){
			log(" Warning: [" + label + "] is not in the menu!");
			pos = 0;	// Put "0" as default
		}
		return pos;
	}
	
	/*
	 * @brief Get position of the label in a menu, which options are unknown
	 * @param String label
	 * @param int comboPos like comboTypePos, comboIntervalPos
	 */
	int getPositionInMenu(String label,int comboPos){
		int pos = 0;
		int flag = 0;
		String text;

		while((text = popProc.getText(cmt,zclassCombo,zclassComboItem,comboPos,pos,REPPOPUP)) != null){
			if(text.trim().equals(label)) break;	// trim() is needed due to a space at the head
			pos++;
			
			if(pos > 100){
				flag++;
				break;
			}
		}
		log(" The position of \"" + label + "\" is: " + pos);
		if(flag != 0) return -1;
		else		  return pos;
	}
	
	/*
	 * @brief Verify the option label in a menu. Attention the position of "zclassCombo" because 
	 *        it's not stable but variable. The position is 0 at the first opening of "zclassCombo",
	 *        and the second, the position is 1. It's not relates to the position of zclassComboBtn.
	 * @param String menuTitle like Type, Interval
	 * @param String[] menu like typeOption, intervalOption
	 * @param int comboPos like comboTypePos, comboIntervalPos
	 */
	void checkOption(String menuTitle, String[] menu, int comboPos){
		String text;
		log(TUT + " in checkOption(): Verify the option name of \"" + menuTitle + "\"");
		for(int i = 0; i < menu.length; i++){
			// Note that the combobox of the menu is OUTSIDE from 'zclassPopup', so don't use it!!
			text = popProc.getText(cmt,zclassCombo,zclassComboItem,comboPos,i,REPPOPUP);
			doAssertThat(text.trim(),is(menu[i]));	// trim() is needed due to a space at the head
		}
	}

	/*
	 * @brief Select an option from a menu. Attention the position of "zclassCombo" because 
	 *        it's not stable but variable. The position is 0 at the first opening of "zclassCombo",
	 *        and the second, the position is 1. It's not relates to the position of zclassComboBtn.
	 * @param int comboPos
	 * @param String[] menu like typeOption[], intervalOption[]
	 * @param int n, which is a position of menu
	 * @param textPos, which is a position of the displayed text
	 */
	void selectOption(int comboPos, String[] menu, int n, int textPos){
		String text;
		
		// Procedure: Select a type from the menu
		popProc.clickItem(cmt,zclassCombo,zclassComboItem,comboPos,n);

		// Procedure: Wait for showing timestamps of the selected menu
		// TODO(04-28): waitForElementToUnload() cannot work properly for confirming menu[n],
		// it's because the text in the text field is not visible. Therefore, use sleep for the moment.
		popProc.waiting(cmt,100);
	
		// TEST: Verify the option name which is shown in the text field
		log(TUT + " in selectOption(): Verify \"" + menu[n] + "\" text and its input area");
		text = popProc.getInputText(cmt,Z_POPUP,Z_TYPETEXT,0,textPos);
		if(text == null || text == "") log("Abort the test due to no type selected");
		else						   doAssertThat(text,is(menu[n]));
	}

	void selectOption(int comboPos, String expectedLabel, int n, int textPos){
		String text;
		
		// Procedure: Select a type from the menu
		popProc.clickItem(cmt,zclassCombo,zclassComboItem,comboPos,n);

		// Procedure: Wait for showing timestamps of the selected menu
		// TODO(04-28): waitForElementToUnload() cannot work properly for confirming menu[n],
		// it's because the text in the text field is not visible. Therefore, use sleep for the moment.
		popProc.waiting(cmt,100);
	
		// TEST: Verify the option name which is shown in the text field
		log(TUT + " in selectOption(): Verify \"" + expectedLabel + "\" text and its input area");
		text = popProc.getInputText(cmt,Z_POPUP,Z_TYPETEXT,0,textPos);
		if(text == null || text == "") log("Abort the test due to no type selected");
		else						   doAssertThat(text,is(expectedLabel));
	}
		
	/*
	 * @brief Select a type and confirm it's done successfully
	 * @param int comboPos
	 * @param String[] menu of typeOption
	 * @param int typePos like typeProjectPos
	 * @param int textPos like textTypePos
	 * @param int labelPos like labelProjectPos
	 * @param String label like "Project Code"
	 */
	void selectTypeAndConfirmed(int comboPos, String[] menu, int typePos, int textPos, 
				                int labelPos, String label){
		String text;
		
		// Procedure: Select an option from Type menu and then
		// TEST with type name in the text field
		selectOption(comboPos,menu,typePos,textPos);
		
		// Procedure: Wait for the closed area is open
		popProc.waitAppearance(cmt,label);

		// TEST: Verify a label of Project Code
		log(TUT + " in selectTypeAndConfirmed(): Verify the closed area is expand by confirming a label");
		if((text = popProc.getText(cmt,Z_POPUP,zclassLabel,0,labelPos,REPPOPUP)) != "")
			doAssertThat(text,is(label));
	}
		
	/*
	 * @brief Select a format and confirm that Generate Report button is clickable
	 * @param int formatPos
	 */
	void selectFormat(int formatPos){
		// Procedure: Click a format which should be permitted and wait for the checked mark is shown
		popProc.clickCheckbox(cmt, Z_POPUP, ztypeRadio, 0, formatPos,SELECT);
		popProc.waitEnable(cmt, Z_POPUP, Z_POPUP_BTN, 0, REPORT_DONE);

		// TEST: Verify the radio button and Generate button, which are available
		log(TUT + " in selectFormat(): Verify Format and \"Generate\" button are available now");
		doAssertThat(popProc.stateInSelection(cmt,Z_POPUP,ztypeRadio,0,formatPos),is(equalTo(true)));
		doAssertThat(popProc.getEnablement(cmt,Z_POPUP,Z_POPUP_BTN, REPORT_DONE),is(equalTo(true)));
	}
	
	/*
	 * @brief Select a format which is unpermitted
	 * @param int formatPos
	 */
	void selectFormatUnexpected(int formatPos){
		String formatName;
		if(formatPos == 0) 		formatName = "PDF";
		else if(formatPos == 1) formatName = "HTML";
		else					formatName = "CSV";
		
		// Procedure: Click a format which should be refused
		popProc.clickCheckbox(cmt, Z_POPUP, ztypeRadio, 0, formatPos,UNSELECT);
		
		// TEST: Verify the radio button and Generate button, which are not available
		log(TUT + " in selectFormatUnexpected(): Verify [" + formatName + "] is unexpected and \"Generate\" button is unavailable");
		doAssertThat(popProc.stateInSelection(cmt,Z_POPUP,ztypeRadio, 0, formatPos),is(equalTo(false)));
		doAssertThat(popProc.getEnablement(cmt,Z_POPUP,Z_POPUP_BTN, REPORT_DONE),is(equalTo(false)));
	}
	
	/*
	 * @brief Select a format and not check the Generate button
	 * @param int formatPos
	 */
	void selectFormatOnly(int formatPos){
		// Procedure: Click a format which should be marked
		popProc.clickCheckbox(cmt, Z_POPUP, ztypeRadio, 0, formatPos,SELECT);
	
		// TEST with the radio button , which is available, and Generate button is unavailable
		log(TUT + " in selectFormatOnly(): Verify Format is available and \"Generate\" button is unavailable");
		doAssertThat(popProc.stateInSelection(cmt,Z_POPUP,ztypeRadio, 0, formatPos),is(equalTo(true)));
		doAssertThat(popProc.getEnablement(cmt,Z_POPUP,Z_POPUP_BTN,REPORT_DONE),is(equalTo(false)));
	}
	
	/*
	 * @brief Split a current url string of PDF report. Here is a sample:
	 *        http://support:10080/webshiftlog/ShiftlogReportCreatorServlet?reportType=WEATHER&reportFormat=PDF&wstart=1430200449477&wend=1430207649477&details=true&site=OTHER
	 */
	String[] splitUrl(){
		String[] resultArray = new String[6];
		
		String url = driver.getCurrentUrl();
		String[] curUrlArray = url.split("\\?");				// First, split "http://...." by "?"
		String[] curUrlParts = curUrlArray[1].split("\\&");		// Split all members after "?"

		for(int i= 0; i < curUrlParts.length; i++){	
			String[] tmpArray = curUrlParts[i].split("=");		// Split by "="
			resultArray[i] = tmpArray[1];
		}
		
		return resultArray;
	}
	
	/*
	 * @brief Generate a report on the new window and go there
	 * @param String winHandle of the old window
	 */
	void generateReportOnNewWindow(String format,String winHandle,String text){
		// Procedure: Generate report
		popProc.generateReport(cmt,Z_POPUP,Z_POPUP_BTN,0,REPORT_DONE);

		// TEST with the new window appeared
		doAssertThat(driver.getWindowHandles().size(),is(2));
		String winHandleNew = baseProc.getNewWindowHandle(cmt,winHandle);
		
		// Procedure: Change the focus to the new window and wait until Project Code is appeared
		if(format.equals("HTML")) baseProc.goToAnotherWindowByText(cmt,winHandleNew,text);	// Check text on the loading page
		else 					  baseProc.goToAnotherWindow(cmt,winHandleNew,titleOfPDF);	// Check title
	}
			
}

