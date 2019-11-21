package alma.irm.webshiftlog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

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
 *					TestSuite 2: Verify WebSLT with search 
 *--------------------------------------------------------------------------------*/
@RunWith(OrderedRunner.class)
public class ShiftlogToolAutotestSuite2 {
	// Class variable
	// These variables are set in setUp()
	private static WebDriver driver;			// Web driver
	private static PopupProcedure popProc;		// Instance for PopupProcedure class
	private static CommonTools cmt;				// Instance for CommonTools class
	private static Path capturePath;			// Capture directory

	// Local variables
	String noEntry = "# Test was skipped due to no entries.";	// For log messages
	String OtherBeginDummy = "2000/01/01 01:00:00";				// Dummy to show the warning message, 15-11-12 updated
	int rowFirst = 0;											// the first row
	int chkCnt = 3;												// Set how many entry items are checked
	int onlyOne = 0;											// Use at getting only one member at findElements()
	int focusPos = 6;											// Use in order to appear a warn popup (Need to focus any area.)

	int[] textPos = new int[]{CLM_AUTHOR, CLM_SUBJECT, CLM_LOC};// Position in Entry list for "General"
	int[] textPos2 = new int[]{									// Position in Entry list for "Exec. Inf" (-1 means no position)
			CLM_PCODE,-1,-1,-1,CLM_SB,-1,CLM_EB,-1,-1,-1,-1};
	int dtActivityPos = 1;										// [SHIFT] Activity information in Details
	
	// ZK locators in the search popup
	String zclassSelectOption = "z-option";		// Interval menu: 0th to 6th (7 menus)
	String zclassText = "z-textbox";			// For text fields
	String zclassRow = "z-row-inner";			// Row in General area: 0th to 13th
	String zclassLabel = "z-label";				// Label in General
	String zclassCheckImg = "z-listitem-checkbox";	// Check boxes in Keywords, 15-11-11 updated
	String zclassBtnUpper = "z-spinner-up";		// Upper arrow image in Max. entries, 15-11-11 updated
	String zclassBtnLower = "z-spinner-down";	// Lower arrow image in Max. entries, 15-11-11 updated
	String zclassCaption = "z-caption";			// Popup title, Execution Information/Engineering bars
	
	// ZK locators in the Warning popup
	String zclassWarnPopup = "z-errorbox-open";		// Warning popup base, 15-11-11 updated
	String zclassPopupMsg = "z-errorbox-content";	// Message in the popup, 15-11-11 updated
	String zclassPopupClose = "z-icon-times";	// Close button image in the popup, 15-11-12 updated
		
	// [Interval]
	String[] intervalOption = new String[]{		// Option names in "Interval"
			"Last 2 hours", "Last 4 hours","Last 8 hours",
			"Last day", "Last week", "Last month", "Other"};
	int intervalDefaultPos = 2;					// Default option is "Last 8 hours"
	int intervalOtherPos = 6;					// "Other" is the 6th-options
	String endWarn = "End date is before Begin date";	// Warning message for "Interval" (End)
	String beginWarn = "Begin date is after End date";	// Warning message for "Interval" (Begin)

	// [Text fields]
	String[] textfieldLabel = new String[]{		// Text field in "General" area
			"Author","Subject","Location","Comment"};
	int authorPos = 0;
	int commentPos = 3;							// Comment position of above string[]

	// [Keywords]
	int rowKeyLabelPos = 10;					// Position of Keywords in "General" area
	int keywordPos = 1;							// Selected keywords are shown in the second label area in Keywords row
	String[] keywordName = new String[]{		// Labels of keywords
			"ANT_INTEGRATION", "FEATURES", "HANDOVER", "HW_TESTING",
			"REGRESSION", "SCIVER", "SIST_ENG", "SW_TESTING"};	
	int keywordTotal = keywordName.length;

	// [Shift Activity]
	String[] shiftOption = new String[]{ 		// Option names of Shift Activity
			"","EOC","Engineering","SciOps","AIV",
			"CSV","Operations","SIST"};

	// [Max. entries]
	int maxEntryDefault = 1000;							// Default value of Max. entries 
	int maxEntryMax = maxEntryDefault;					// Max. entries maximum value
	int maxEntryMin = 1;								// Max. entries minimum value
	String maxEntryWarnMsg = "Out of range: 1 - 1000";	// Warning popup message

	// Execution Information pane
	String execinfText = "Observing Cycle";				// Text to confirm Execution Inf is expanded

	// Menu: Observing Cycle
	String[] cycleMenu       = new String[]{"", "2011.0 (a.k.a Cycle0)", 
											"2012.1 (a.k.a Cycle1)", "2012.A", 
											"2013.1 (a.k.a Cycle2)", "2013.A"};
	String[] cycleYear       = new String[]{"", "2011.0", "2012.1", "2012.A", "2013.1", "2013.A"};
	String cycleTail         = " AND NOT CSV";
	String[][] cycleInterval = new String[][]{			// 15-11-12 updated due to date format changing
								{"",""},				// Dummy for the first empty option in the menu		
								{"2011-09-01 00:00:00","2012-12-31 23:59:59"},
								{"2013-01-01 00:00:00","2013-12-31 23:59:59"},
								{"2013-01-01 00:00:00","2014-05-31 23:59:59"},
								{"2014-06-01 00:00:00","2015-12-31 23:59:59"},
								{"2014-06-01 00:00:00","2015-12-31 23:59:59"},
								};

	// Menu: Except Observing cycle
	String[] calibMenu  = new String[]{"","Yes","No"};
	String[] arrayMenu  = new String[]{"","12 [m]","7 [m]","Total Power"};
	String[] corrMenu   = new String[]{"","NONE","BL","ACA"};
	String[] statusMenu = new String[]{"","ABORTED","FAIL","PARTIAL","RUNNING","SUCCESS","TIMEOUT"};
	String[] qa0Menu    = new String[]{"","Fail","SemiPass","Pass"};
	String[] bandMenu   = new String[]{"","ALMA_RB_01","ALMA_RB_02","ALMA_RB_03","ALMA_RB_04","ALMA_RB_05",
									   "ALMA_RB_06","ALMA_RB_07","ALMA_RB_08","ALMA_RB_09","ALMA_RB_10","UNDEFINED"};
						   
	String[] menulabelDetails = new String[]{"", "Calibration", "Array family", "Correlator",     
								 			 "Status", "QA0 Status", "Band name"};
	String[] menuLabelExecinf = new String[]{"Observing Cycle", "Calibration", "Array family", "Correlator type",
								 			 "Status", "QA0 Status", "Band name",};
	String[][] allMenus       = new String[][]{cycleMenu, calibMenu, arrayMenu, corrMenu, statusMenu, qa0Menu, bandMenu};
	int[] allMenusTotal       = new int[]{cycleMenu.length, calibMenu.length, arrayMenu.length, corrMenu.length, 
								 		  statusMenu.length, qa0Menu.length, bandMenu.length};

	// Text
	String[] textLabelExecinf = new String[]{"Project Code","Project","Project P.I.","Project",	// version is included in Project
											 "SchedBlock name", "SchedBlock UID", "ExecBlock UID", "Array name",
											 "Antennas", "Photonic Reference", "Executive(s)"};
	String prefTooltip = "Typical values are \"PhotonicReference1\", \"PhotonicReference2\", etc.," +
			             "\nbut simpler values like \"1\", \"2\", etc. can be given as well";
	String execTooltip = "One or more of the following: NA, EU, EA, CL, OTHER." +
			             "\n\"AND\" and \"OR\" syntax is allowed";
	// Note that tooltop locations of pref and exec are not the same, so that the position is below
	int prefTooltipPos = 13;	// Use "z-row" class, which includes not only texts but menus
	int execTooltipPos = 10;	// Use "z-textbox" class, which includes texts only
	
	// Engineering
	String engineerText = "Groups";				// Text to confirm Engineering is expanded
	String engGroupName[] = new String[]{"Antenna control","BackEnd","Correlator","FrontEnd","Software"};
	int rowEngLabelPos = 0;						// Label position in Engineering
	
	/*
	 * Create instance HERE
	 * TODO(2015-06-10) I'm not sure but test doesn't work as expected in case this instance is set in setUp
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
	 * 2015-03-23 Note that all tests are done one after another with oder in order.
	 *--------------------------------------------------------------------------------*/
	@Test
	@Order(order=101)
	public void testCase101(){ interval(); }
	@Test
	@Order(order=102)
	public void testCase102(){ intervalWithMillisec(); }
	@Test
	@Order(order=103)
	public void testCase103(){ intervalWarning(); }
	@Test
	@Order(order=104)
	public void testCase104(){ generalTypes(); }
	@Test
	@Order(order=105)
	public void testCase105(){ generalTypesWithDetails(); }
	@Test
	@Order(order=106)
	public void testCase106(){ generalTypesInvert(); }	
	@Test
	@Order(order=107)
	public void testCase107(){ generalTypesMultiple(); }
	@Test
	@Order(order=108)
	public void testCase108(){ generalText(); }
	@Test
	@Order(order=109)
	public void testCase109(){ generalTextWithNot(); }
	@Test
	@Order(order=110)
	public void testCase110(){ generalKeywords(); }
	@Test
	@Order(order=111)
	public void testCase111(){ generalKeywordsMultiple(); }
	@Test
	@Order(order=112)
	public void testCase112(){ generalShiftActivityNoSet(); }
	@Test
	@Order(order=113)
	public void testCase113(){ generalShiftActivity(); }
	@Test
	@Order(order=114)
	public void testCase114(){ maxEntries(); }
	@Test
	@Order(order=115)
	public void testCase115(){ maxEntriesWithBorder(); }
	@Test
	@Order(order=116)
	public void testCase116(){ execInf(); }
	@Test
	@Order(order=117)
	public void testCase117(){ execInfTooltip(); }
	@Test
	@Order(order=118)
	public void testCase118(){ execInfCycle(); }
	@Test
	@Order(order=119)
	public void testCase119(){ execInfMenu(); }
	@Test
	@Order(order=120)
	public void testCase120(){ execInfText(); }
	@Test
	@Order(order=121)
	public void testCase121(){ engineering(); }
	@Test
	@Order(order=122)
	public void testCase122(){ engineeringMultiple(); }
	@Test
	@Order(order=123)
	public void testCase123() { saveCurrent(); }
	@Test
	@Order(order=124)
	public void testCase124(){ searchWithAndOr(); }
	
	/*--------------------------------------------------------------------------------
	 * 			Each of the test cases 
	 *--------------------------------------------------------------------------------*/

	/*
	 * Before testing, do login
	 * Note that baseProc.login() is a non-static method, thus, it cannot be put into setUp()
	 */
	public void loginAsFirst(){
		log("========== Before testing, do login as first ==========");
		String screenshotName = "Suite2Default.png";

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
	 * Interval
	 */
	public void interval(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String screenshotName = "Interval.png";
		
		// Procedure: Save the date, then open search popup
		log(" Current date: " + popProc.getCurrentDate(cmt,FORMAT_DATE));
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		
		// Report: the initial screenshot
    	baseProc.saveCurrentScreen(cmt,capturePath,screenshotName);
		
		// TEST1: Verify the default value of Interval is "Last 8 hours"
		log(TUT + "1: Verify the default value of Interval");
		doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,0),is(intervalOption[intervalDefaultPos]));
		
		// TEST2: Verify names of the pulldown menu
		log(TUT + "2: Verify all items of Interval");
	    for(int i = 0; i < intervalOption.length; i++){	
	    	doAssertThat(popProc.getText(cmt,Z_TABLECHILD,zclassSelectOption,INTERVAL,i,INTERVALMENU),is(intervalOption[i]));			
	    }

 	    // Procedure: Select an option from the Interval menu one by one
	    for(int i = 0;i < intervalOption.length;i++){	
		    // Procedure: Change the interval and get Begin/End Timestamps
			popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[i]);
		    if(i == intervalOtherPos){	// In case of "Other", put begin/end dates
		    	popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
			    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
		    }
		    String curBegin = popProc.getDateWithFormat(cmt, Z_TABLECHILD, Z_TYPETEXT,BEGIN,FORMAT_SEARCH,FORMAT_DATE);
			String curEnd = popProc.getDateWithFormat(cmt, Z_TABLECHILD, Z_TYPETEXT,END,FORMAT_SEARCH,FORMAT_DATE);
			log(" Current option: [" + intervalOption[i] + "], Current interval: [" + curBegin + " - " + curEnd + "]");

			// TODO(04-28) Need to check the date length, or it's doesn't need...?

			// TEST3: Verify the interval name is selected one
			log(TUT + "3: Verify the interval name");
			doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,0),is(intervalOption[i]));
		
	    	// Procedure: Do search and get entry summary
	    	popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);

	    	// Procedure: Check there are some entries or not
	    	if(!baseProc.isNoEntry(cmt, Z_ENTRY_INFO, NO_ENTRY)){
	    		for(int cnt = 0; cnt < 2; cnt++){
	    			// Procedure: Sort the entry list and get the first TYPE and Timestamp
	    			baseProc.sortList(cmt, Z_PANELCHILD, Z_LISTHEAD, CLM_TS);
	    			String typeStr = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,CLMS[CLM_TYPE]);
	    			String tsStr = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TS,CLMS[CLM_TS]);
	    			String checkTsStr = baseProc.getBeginOrEndTS(cmt,tsStr,typeStr,cnt);	// Get "To" timestamp

	    			// Procedure: In case of missing to get strings, nothing is done and go to next option
	    			if(checkTsStr == null){
	    				log("Cannot test due to miss to get \"Type\" and/or \"Timestamp\"");
	    				break;
	    			}
	    			// TEST4: Verify the timestamp is in the range of the selected time interval
	        		log(TUT + "4: Verify the Timestamp [" + checkTsStr +"] is in the range");
	        		if(cnt == 0) doAssertThat(curBegin.compareTo(checkTsStr) <= 0,is(equalTo(true))); // Begin TS < Checked TS
	        		else		 doAssertThat(checkTsStr.compareTo(curEnd) <= 0,is(equalTo(true)));	 // Checked TS < End TS
	    		}
	    	}
			// Procedure: Open search popup and get the shown interval's name
			baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
			
			// TEST5: Verify the selected interval name remains 
			log(TUT + "5: Verify the interval name again");
			doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,0),is(intervalOption[i]));
	    }
		
		// Procedure: Open search popup again. load defaults, and get the interval's name 
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);

	    // TEST6: Verify the default interval name is "Last 8 hours"
		log(TUT + "6: Verify the default interval name");
		doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,0),is(intervalOption[intervalDefaultPos]));

	    // Procedure: Close the search popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}	
	
	/*
	 * Interval: dates with milliseconds
	 */
	public void intervalWithMillisec(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Get current timestamps, which are compared later
		String dateBegin = "";							
		String dateEnd = "";							
		dateBegin = popProc.getFormatTS(cmt,OTHER_BEGIN,FORMAT_SEARCH,FORMAT_DATE);
		dateEnd = popProc.getFormatTS(cmt,OTHER_END,FORMAT_SEARCH,FORMAT_DATE);

		// Procedure: Open search popup, select "Other" in Interval
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    
	    // Procedure: Put Begin/end dates with milliseconds and focus explicitly to show the fixed date
		popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, addMillisecToTs(OTHER_BEGIN));
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, addMillisecToTs(OTHER_END));
	    popProc.focusing(cmt,Z_TABLECHILD,END);
	    
    	// TEST1: Verify the appeared Begin/End dates does not show milliseconds
	    log(TUT + "1: Verify the Begin/End dates with no milliseconds");
	    doAssertThat(popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,BEGIN,0),is(OTHER_BEGIN));
	    doAssertThat(popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,END,0),is(OTHER_END));
	    
	    // Procedure: Do search
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
	    
	    // Procedure: Get entry summary
		//            In case of no entry, this test has no meaning so breaks the test here
    	if(baseProc.isNoEntry(cmt, Z_ENTRY_INFO, NO_ENTRY)) log("Abort the test due to no entries");
    	else{
    		// Procedure: Click Timestamp header. They are arranged in ascending order of date.
    		//			  The second click is changed the order in descending
    		for(int i = 0; i < 2; i++){
    			// Procedure: Sort the entry list by Timestamp
    			baseProc.sortList(cmt, Z_PANELCHILD, Z_LISTHEAD, CLM_TS);
    			String typeStr = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,CLMS[CLM_TYPE]);
    			String tsStr = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TS,CLMS[CLM_TS]);
    			String checkTsStr = baseProc.getBeginOrEndTS(cmt,tsStr,typeStr,i);	// Get "To" timestamp

    			if(checkTsStr == null){ // In case of missing time string, do nothing
    				log(NOTEST);
    				break;
    			}
    				
    			// TEST2: Verify the first/last entries are inside the time interval
    			log(TUT + "2: Verify the timestamp [" + checkTsStr + "] is in the range");
    			if(i == 0)
    				doAssertThat(dateBegin.compareTo(checkTsStr) <= 0,is(equalTo(true)));// Begin TS < Checked TS
    			else
    				doAssertThat(checkTsStr.compareTo(dateEnd) <= 0,is(equalTo(true)));	// Checked TS < End TS
    		}
    	}
    	
		// Procedure: Open search popup again
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
 
		// TEST3: Verify the set interval remains
		log(TUT + "3: Verify the set interval remains");
	    doAssertThat(popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,BEGIN,0),is(OTHER_BEGIN));
	    doAssertThat(popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,END,0),is(OTHER_END));
		
		// Procedure: Load defaults value and close the popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);

	    log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}	
	
	/*
	 * Interval: Warning with Others
	 */
	public void intervalWarning(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

	    String warnText;

		// Procedure: Open search popup, select "Other" in Interval
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);

		//----- Warning of End date -----
		// Procedure: Put End TS into Begin field, and Begin TS to End, that is, Begin is later than End 
		popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_END);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_BEGIN);

	    // Procedure: Focus explicitly in order to show the warning popup
	    popProc.focusing(cmt,Z_TABLECHILD,END);
	    
	    // Procedure: Get warning text. In case of miss it, the test fails immediately
	    warnText = popProc.getText(cmt,zclassWarnPopup,zclassPopupMsg,0,0,WARNDIALOG);
	    
	    // TEST1: Verify the warning message and unavailability of the Search button
	    log(TUT + "1: Verify warning message for \"End\" Timestamp and the status of the search button");
	    doAssertThat(warnText,is(endWarn));
	    doAssertThat(popProc.getEnablement(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE),is(equalTo(false)));
	    
	    // Procedure: close the warning 
	    popProc.closeWarnPopup(cmt, zclassWarnPopup, zclassPopupClose, warnText);
	   
	    //----- Warning of Begin date -----
		// Procedure: Put Begin TS into End field, then End TS to Begin
	    // Note that need to get the warning message related to Begin TS
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OtherBeginDummy); // Put old date enough at Begin 
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_BEGIN);		// Put End TS
		popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_END);		// Put Begin TS
	    
	    // Procedure: Focus explicitly in order to show the warning popup
	    popProc.focusing(cmt,Z_TABLECHILD,BEGIN);
	    
	    // Procedure: Get warning text. In case of miss it, the test fails immediately
	    warnText = popProc.getText(cmt,zclassWarnPopup,zclassPopupMsg,0,0,WARNDIALOG);

	    // TEST2: Verify the warning message and unavailability of the Search button
	    log(TUT + "2: Verify warning message for \"Begin\" Timestamp");
	    doAssertThat(warnText,is(beginWarn));
	    doAssertThat(popProc.getEnablement(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE),is(equalTo(false)));
	    
	    // Procedure: close the warning 
    	popProc.closeWarnPopup(cmt, zclassWarnPopup, zclassPopupClose, warnText);

	    // Procedure: Load default values and close the popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);

	    log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * General > Types
	 */
	public void generalTypes(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);

		// Procedure: get the check box's status 
		int[] typesNotChecked = {0,0,0,0,0,0,0,0,0,0,0};	// In default, all types are not checked
		int[] hitId = {0,0,0,0,0,0,0,0,0,0,0};	            // Initialization for checking below
		for(int i = 0;i < TYPE_NAME.length; i++){
			if(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i)){
				hitId[i] = 1;					// True means this check box is checked unexpectedly
				log(" types [" + TYPE_NAME[i] + "] is checked unexpectedly");
			}
		}
		
		// TEST1: Verify no types are checked
		log(TUT + "Verify all types are unchecked in default");
		doAssertThat(hitId,is(typesNotChecked));			
		
		// Procedure: Check one of types in turn
		for(int i = 0; i < TYPE_NAME.length; i++){
			// Procedure: Mark a check box
			log("[" + TYPE_NAME[i] + "]");
    		popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,i,SELECT);

    		// TEST2: Verify the clicked type is checked
    		log(TUT + "2: Verify the clicked type is checked");
    		doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i),is(equalTo(true)));
    		
			// Procedure: Do search and look into some entries if exist
		    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
		    if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY))
	    		lookSortedList(CLM_TYPE,TYPE_NAME[i],false,true);
	    	
			// Procedure: Open search popup again
			baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

			// TEST4: Verify the checked box remains
			log(TUT + "4: Verify the checked box remains");
      		doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i),is(equalTo(true)));

			// Procedure: Check the previous checked for clear
    		popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,i,UNSELECT);

			// TEST5: Verify the checked box is unchecled now
			log(TUT + "5: Verify the previous checked box is cleared");
       		doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i),is(equalTo(false)));
		}
		
		// Procedure: Load default values and close the popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * General > Types and its information in details
	 */
	public void generalTypesWithDetails(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		//--- Terms ---
		// Types for testing
		String[] testTypes = new String[]{"AOG","ARRAY","DOWN","MMEX","SBEX","SHIFT"};
	    // Wait until the following specific word is appeared in Details, used at clicking of an entry
	    String[] waitLabel = new String[]{"Subject","Antennas","Downtime type","Subject","Location","Activity"};
	    // Labels to be tested
	    String[] testLabel = new String[]{"Subject","Antennas","Downtime type","Subject","Subject","ALMA version"};
	    // Labels to be tested in Down
    	String[] kindOfDown = new String[]{ "Is power cut?", "Start/End" };
	    // Labels to be tested in MMEX and SBEX
	    String[] exLabel = new String[]{"ALMA version","Antennas","Array name","Array type","Array family","Correlator","Photonic Reference"};
	    // Labels to be tested in SBEX only
	    String[] sbexLabel = new String[]{"Band name","Representative frequency","Executive(s)"};
	    // Sentences of Subject in AOG
	    String[] aogSubject = new String[]{
				"Morning to afternoon shift", "Afternoon to night shift", "Night to morning shift",
				"Just chatting", "Coordination meeting summary"
		};
    	
		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);

		// Procedure: Check some types in turn
	    for(int tid = 0; tid < testTypes.length; tid++){
			// Procedure: Get the type's position
			int typeId;
			for(typeId = 0;typeId < TYPE_NAME.length; typeId++)
				if(testTypes[tid].equals(TYPE_NAME[typeId])) break;

			
			// Procedure: Mark the selected type's check box
			log("[" + TYPE_NAME[typeId] + "]");
    		popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,typeId,SELECT);

			// Procedure: Do search and look into some entries if exist
		    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
		    if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)){
		    	// Get the total number of entries and set counter for verifying entries
    			int total = baseProc.getTotalNumberOfEntry(cmt,Z_ENTRY_INFO);
    			int max = (total < rowFirst + 3)? total : rowFirst + 3;
    			switch(tid) {
		    	/* ---- [AOG] -----*/
		    	case 0:	
	    			log("[" + TYPE_NAME[typeId] + ": "+ testLabel[tid] + "]");
	    			int subId;
	    			String column = "";
	    			for(int k = rowFirst; k < max;k++){
	    				// Procedure: Get column of "Subject" 
	    				column = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,k,CLM_SUBJECT,CLMS[CLM_SUBJECT]);
	    				for(subId = 0; subId < aogSubject.length;subId++) if(column.equals(aogSubject[subId])) break;
	    				if(subId == aogSubject.length) log("Unknown subject: [" + column + "]");	// Nothing to match

	    				// TEST: Verify the sentence
	    				log(TUT + " (" + k + "): Verify the sentence of \"" + testLabel[tid] + "\"");
	    				doAssertThat(column,is(aogSubject[subId]));
	    			}
	    			break;
		    	/* ---- [ARRAY,DOWN,SHIFT] -----*/
			    case 1:
			    case 2:
			    case 5:
	    			log("[" + TYPE_NAME[typeId] + ": "+ testLabel[tid] + "]");
	    			for(int k = rowFirst; k < max;k++){
	    				// Procedure: Click one entry to show details and get information from that
	    				baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,k,0,waitLabel[tid]);
	    				String members[] = getRowInDetails(testLabel[tid]);

	    				// Procedure: In case of Down, check type and get another data which is specific to that kind
	    				if(tid == 2){
	    			    	int idDown;
	    					if(members[1].equals("Technical")) idDown = 0;
	    					else                               idDown = 1;
	    					String subMembers[] = getRowInDetails(kindOfDown[idDown]);
	        				log(TUT + " (" + k + "): Verify data");
	        				doAssertThat(subMembers[1].length() > 0,is(true));
	    				}
	    				else{
	    				// TEST: Check if the information of the label exists
	    				// TODO(06-30) Should its content be checked? Must use Regular expression.
	    				//  Refer to: http://hakobera.hatenablog.com/entry/20101225/1293296275
	    				log(TUT + " (" + k + "): Verify data");
	    				doAssertThat(members[1].length() > 0,is(true));
	    				}				
	    				baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
	    			}
			    	break;
	    		/* ---- [SBEX, MMEX] -----*/
		    	case 3:	
			    case 4:	
		    		log("[" + TYPE_NAME[typeId] + ": "+ testLabel[tid] + "]");
		    		for(int k = rowFirst; k < max;k++){
		    			// Procedure: Get column of "Subject" 
		    			column = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,k,CLM_SUBJECT,CLMS[CLM_SUBJECT]);

		    			// TEST: Verify subject
		    			// TODO(07-07) Maybe need to check the science script names
		    			log(TUT + " (" + k + "): Verify \"" + testLabel[tid] + "\"");
		    			if(column.length() == 0){
		    				log(" There is no texts. Test is ignored");
		    			}
		    			else{
		    				if(tid == 3) doAssertThat(column.length() <= 256,is(true));	// Verify length <= 256 chars
		    				else	     doAssertThat(column.contains(".py"),is(true));	// Verify science script
		    			}
		    			
		    			// Procedure: Click one entry to show details and get information from that
	    				baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,k,0,waitLabel[tid]);
	    				
	    				// TEST: Just verify the labels exist 
	    				// Note that not all labels are shown sometimes, so that just count "false" (= not shown)
	    				//  and logs a warn message.
	    				int falseN = 0;
	    				for(int n = 0;n < exLabel.length; n++){
		    				log(TUT + " (" + k + "): Verify data of " + exLabel[n]);
		    			    if(!baseProc.checkText(cmt,exLabel[n])) falseN++;
		    			}
	    				//       In case of SBEX, check moreover
	    				if(tid == 4){ // In case of SBEX, check moreover
	    					for(int n = 0;n < sbexLabel.length; n++){
	    						log(TUT + " (" + k + "): Verify data of " + sbexLabel[n]);
	    						if(!baseProc.checkText(cmt,sbexLabel[n])) falseN++;
	    					}
	    				}
	    				if(falseN > 0) log(" Warning: Some of labels are missing");

	    				baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
		    		}
			    	break;
		    	default:
		    		break;
		    	}	
    			
	    	}
	    	
			// Procedure: Open search popup again and unchecked the type above
			baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
    		popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,typeId,UNSELECT);

		}
		
		// Procedure: Load default values and close the popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * General > Types: Invert in default
	 */
	public void generalTypesInvert(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Open search popup and click Invert in Types
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
	    popProc.clickLink(cmt, TYPE_INVERT);
	    
	    // TEST1: Verify nothing is checked in Types
	    log(TUT + "1: Verify the checkboxes which are not checked at all in default");
	    for(int i = 0; i < TYPE_NAME.length; i++)
	    	doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i),is(equalTo(false)));
	    	
	    // Procedure: Click all types and then click Invert
		for(int i = 0; i < TYPE_NAME.length; i++)
    		popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,i,SELECT);
	    popProc.clickLink(cmt, TYPE_INVERT);
	    popProc.waitAfterClickLink(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,TYPE_NAME.length-1);	// Check the last one

	    // TEST2: Verify all is checked in Types
	    log(TUT + "2: Verify the checkboxes which are all checked");
	    for(int i = 0; i < TYPE_NAME.length; i++)
	    	doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i),is(equalTo(true)));
	    
		// Procedure: Load default values
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);

	    // TEST3: Verify nothing is checked in Types again
	    log(TUT + "3: Verify the checkboxes which are not checked as default");
	    for(int i = 0;i < TYPE_NAME.length;i++)
	    	doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i),is(equalTo(false)));

	    // Procedure: Close the popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
  
	}
	
	/*
	 * General > Types: Multiple selection
	 */
	public void generalTypesMultiple(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;							// Initialize counters
		final int testBoxes = 2;							// Number of the check boxes for testing  
		
		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);

	    // Procedure: Shuffling the number from 0 to 10 
	    int[] chkNum = shuffledNum(TYPE_NAME.length);
    	String expected[] = {TYPE_NAME[chkNum[0]],TYPE_NAME[chkNum[1]]};
	    log("Test types are: [" + expected[0] + ", " + expected[1] + "]");

	    // Procedure: Check two boxes according to the shuffled number
	    for(int i = 0;i < testBoxes;i++)
	    	popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,chkNum[i],SELECT);
    
	    // TEST1: Verify the shuffled numbers are checked, and the others are unchecked
	    log(TUT + "1: Verify the clicked types are checked");
	    for(int i = 0; i < TYPE_NAME.length; i++){		
	    	if(i == chkNum[0] || i == chkNum[1])		// should be "checked"
	    		doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i),is(equalTo(true)));
	    	else										// should be "unchecked"
	    		doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i),is(equalTo(false)));
	    }

	    // Procedure: Do search and look into some entries if exist
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
	    if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)){
	    	// TEST2: Verify the types in the entry list are the checked ones only
	    	log(TUT + "Verify \"Type\" in the list");
	    	lookSortedListWithTwoItems(CLM_TYPE,expected,true);
	    }

	    // Procedure: Open search popup again and verify the previous checkboxes are checked
	    baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

	    // TEST3: Verify the clicked types remain
	    log(TUT + "3: Verify the clicked types remain");
	    for(int i = 0;i < testBoxes; i++)
	    	doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,chkNum[i]),is(equalTo(true)));

	    // Procedure: Click Invert and wait for marking of unchecked box
	    popProc.clickLink(cmt, TYPE_INVERT);			// Invert the checking
	    for(int i = 0; i < testBoxes+1; i++){						// 
	    	if(i != chkNum[0] && i != chkNum[1]){
	    		popProc.waitAfterClickLink(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i);
	    		break;									// It's enough to see one check box
	    	}
	    }
	    
	    // TEST4: Verify the types above are unchecked
	    log(TUT + "4: Verify the checked ones are unchecked now");
	    for(int i = 0; i < TYPE_NAME.length; i++){		
	    	if(i == chkNum[0] || i == chkNum[1])		// should be "unchecked"
		    	doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i),is(equalTo(false)));
	    	else										// should be "checked"
		    	doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i),is(equalTo(true)));
	    }

	    // Procedure: Do search and look into some entries if exist
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
	    if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)){
	    	// TEST5: Verify the types above are not shown
	    	log(TUT + "Verify the previous selected types are not shown");
	    	lookSortedListWithTwoItems(CLM_TYPE,expected,false);
	    }

	    // Procedure: Open search popup again and verify the previous checkbox is unchecked
	    baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

	    // TEST6: Verify the shuffled numbers' types are still unchecked
	    log(TUT + "6: Verify the unchecked types remain");
	    for(int i = 0;i < testBoxes; i++)
	    	doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,chkNum[i]),is(equalTo(false)));
	    
	    // Procedure: Click Invert again
	    popProc.clickLink(cmt,TYPE_INVERT);
		popProc.waitAfterClickLink(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,chkNum[0]);
	    
	    // TEST7: Verify the checked types are inverted
	    log(TUT + "7: verify the types first checked are now marked again");
	    for(int i = 0; i < TYPE_NAME.length; i++){		
	    	if(i == chkNum[0] || i == chkNum[1])		// TEST with checked
		    	doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i),is(equalTo(true)));
	    	else										// TEST with unchecked
		    	doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,i),is(equalTo(false)));
	    }

	    // Procedure: Load default values and close the popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * General > Text
	 */
	public void generalText(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		
		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);

	    // Check four text fields in order
	    // Note that strings are compared in small letters here
 	    for(int i = 0; i < textfieldLabel.length; i++){
  	    	log("[" + textfieldLabel[i] + "]");
 	    	String expected = PARAM_GENERAL[i];				// Save the input text
 	    	
		   	// TEST1: Verify the text field is empty
 	    	// Note that "assertThat(...,is(nullValue())" doesn't work, so check the length and assert it's zero.
 	    	log(TUT + "1: Verify the text field in default is empty");
		    lookTextLength(GENERAL,i,0);

		    // Procedure: Input string in the text field
		    popProc.putText(cmt,Z_TABLECHILD,Z_TYPETEXT,GENERAL,i,expected);

		    // TEST2: Verify the input text is shown
 	    	log(TUT + "Verify the input text is appeared");
		    lookTextValue(GENERAL,i,expected);

		    // Procedure: Do search and look into some entries if exist
		    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
		    if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)){
			    if(i != commentPos){	// For Author, Subject, Location
			    	// Procedure: Sort the entry list twice and then
			    	// TEST3: Verify the text is shown on the entry list 
			    	lookSortedList(textPos[i],expected,true,true);
			    }else{					// For Comment
			    	// Procedure: Check the entry's total number and then
			    	// TEST3: Verify the text is shown in details panel
			    	int n = baseProc.getTotalNumberOfEntry(cmt,Z_ENTRY_INFO);
			    	int cellMax = n > chkCnt? chkCnt : n;			// The maximum number is not over entry numbers
			    	for(int cellId = 0; cellId < cellMax; cellId++){	// look a few entries
				    	// Procedure: Click an entry to show the detail info
			    		// TODO(2015-09-07) This "NOT" mechanism seems to check one of comments do NOT have the word.
			    		//  Need to clear up what "NOT" for comments requests, then improve the verification.
			    		//  Before that, please be careful to decide the word with "NOT"
			    		lookDetails(cellId,"Details","Comments",expected,true);
			    		
			    	}
			    }
	    	}
		     
		    // Procedure: Open search popup again
		    baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		    
		    // TEST4: Verify the text remains
		    log(TUT + "4: Verify the inputted text remains");
		    lookTextValue(GENERAL,i,expected);

		    // Procedure: clear the text field
		    popProc.clearText(cmt,Z_TABLECHILD,Z_TYPETEXT,GENERAL,i);
		    
		    // TEST5: Verify the inputted text is cleared now
		    log(TUT + "5: Verify the inputted text is cleared");
		    lookTextLength(GENERAL,i,0);
		    }
	    
	   	// Procedure: Load default values and close the popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	    
	/*
	 * General > Text with NOT
	 */
	public void generalTextWithNot(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);

	    // Check four text fields in order
	    // Note that strings are compared in small letters here
 	    for(int i = 0; i < textfieldLabel.length; i++){
 	    	String unexpected = PARAM_GENERAL[i];				// Save unexpected string
 	    	String inputted = "NOT " + unexpected;
	    	log("[" + inputted + "]");
	    	
		    // Procedure: Input string in the text field
		    popProc.putText(cmt,Z_TABLECHILD,Z_TYPETEXT,GENERAL,i,inputted);

		    // Procedure: Do search and look into some entries if exist
		    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
		    if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)){
			    if(i != commentPos){	// For Author, Subject, Location
			    	// Procedure: Sort the entry list twice and then
			    	// TEST1: Verify the text is not shown on the entry list 
			    	lookSortedList(textPos[i],unexpected,true,false);
			    }else{					// For Comment
			    	// Procedure: Check the entry's total number and then
			    	// TEST1: Verify the text is not shown in details panel
			    	int n = baseProc.getTotalNumberOfEntry(cmt,Z_ENTRY_INFO);
			    	int cellMax = n > chkCnt? chkCnt : n;			// The maximum number is not over entry numbers
			    	for(int cellId = 0; cellId < cellMax; cellId++){	// Do in a few entries
			    		
			    		// Procedure: Click an entry in order to show the detail info
			    		lookDetails(cellId,"Details","Comments",unexpected,false);
			    	}
			    }
	    	}
		     
		    // Procedure: Open search popup again and clear the text field
		    baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		    popProc.clearText(cmt,Z_TABLECHILD,Z_TYPETEXT,GENERAL,i);
 	    }
	    
	   	// Procedure: Load default values and close the popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	    
	/*
	 * General > Keywords
	 */
	public void generalKeywords(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);

		// Check the keyword name one by one
		keywordOneByOne(keywordName,GENERAL,rowKeyLabelPos,"Keywords");

		// Procedure: Load default values and closd the popup by Cancel
	    //            In case of not loading defaults, the last selected option remains. 
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * General > Keywords with multiple marked
	 */
	public void generalKeywordsMultiple(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);

		// Check keyword names
		keywordMultiple(keywordName,GENERAL,rowKeyLabelPos);

		// Procedure: Close the popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}


	/*
	 * General > Shift Activity with no selection
	 */
	public void generalShiftActivityNoSet(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		
		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
		
	    // TEST1: Verify no activity is set in default
		log(TUT + "1: Verify Shift Activity is not set in default");
		doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,GENERAL,0),is(equalTo("")));
		
		// Procedure: Check SHIFT in types and then do search
		popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,TYPES_SHIFT,SELECT);
		popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
	    	
		// Procedure: Look into entry summary message
		// Note that if there is no SHIFT entry, the test is meaningless so abort it right now.
	    if(baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)) log("Abort the test due to no entries");
	    else{
	    	// Procedure: In order to check the first/last entry, sort the entry
	    	for(int cnt = 0; cnt < 2; cnt++){
	    		baseProc.sortList(cmt, Z_PANELCHILD, Z_LISTHEAD, CLM_TS);
	    		baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,0,"Activity");

	    		// Procedure: Get the shift name
	    		String[] members = getRowInDetails("Activity");
	    		log(" Gotten shift activity: [" + members[1] + "]");
				
	    		// TEST2: Verify any of activities are shown
	    		log(TUT + "2: Verify the activity name");
	    		doAssertThat(members[0],is("Activity"));	// Check label exists
	    		int i;
	    		for(i = 0; i < shiftOption.length; i++){
	    			if(members[1].contains(shiftOption[i])) break;
	    		}
	    		if(i == shiftOption.length) log("Nothing was matched: [" + members[1] + "]");

	    		// Procedure: Need to refresh for out of focus from the clicked entry above
	    		//            If not, warning will be appeared at logging out!
	    		baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
	    	}
	    }
	    
		// Procedure: Open search popup and then load default values 
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);

	    // TEST3: Verify no activity is set in default
		log(TUT + "3: Verify Shift Activity is not set after loading defaults");
		doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,GENERAL,0),is(equalTo("")));

		// Procedure: Close the popup by Cancel
		popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}	

	/*
	 * General > Shift Activity
	 */
	public void generalShiftActivity(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String label = "Activity";
		
		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
		
	    // Procedure: Check SHIFT in types
		popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,TYPES_SHIFT,SELECT);
	    
		// TEST1: Verify all option names in Shift Activity
		log(TUT + "1: Verify all items of Shift Activity");
		for(int i = 0; i < shiftOption.length; i++){			// Verify all option's name in menu
	    	doAssertThat(popProc.getText(cmt,Z_TABLECHILD,zclassSelectOption,GENERAL,i,SHIFTMENU),is(shiftOption[i]));			
	    }

 	    // Procedure: Open popup and select one from menu, then 
	    // TEST with Shift Activity in Details info
	    for(int i = 1;i < shiftOption.length;i++){				// Ignore i == 0 because none is selected (all SHIFT entries will be shown)
		    log(" [" + shiftOption[i] + "]");

		    // Procedure: select one option from Shift Activity and get the text
			popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,GENERAL,shiftOption[i]);

	    	// TEST2: Verify the changed label name
			log(TUT + "2: Verify the selected Shift name is shown");
			doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,GENERAL,0),is(shiftOption[i]));
		
	    	// Procedure: do search and look into some entries if exist
	    	popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
		    if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)){
		    	// Procedure: Click the first entry and wait until the label is shown in Details, then get a row
				baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,0,shiftOption[i]);
				String members[] = getRowInDetails(label);

				// TEST3: Verify the selected shift is appeared
				log(TUT + "3: Verify the expected is shown");
				doAssertThat(members[0],is(label));
				doAssertThat(members[1],is(shiftOption[i] + " shift"));

				// Procedure: Need to refresh for out of focus from the clicked entry above
				//            If not, warning will be appeared at logging out!
				baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
		    }
		    // Procedure: Open search popup and get the text
		    baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,shiftOption[i]);
		    	
		    // TEST4: Verify the selected shift remains 
		    log(TUT + "4: Verify the previous selected shift is still shown");
			doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,GENERAL,0),is(shiftOption[i]));
	    }
		
		// Procedure: load defaults and close the search popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);

	    log(TU + "Finished");
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}	

	/*
	 * Max entries
	 */
	public void maxEntries(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
    	// Note that it needs to get entries exceeds the default number of max entries (=1000),
    	//  therefore, select the following interval carefully for getting much entries
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);

	    // TEST1: Verify Max.entries number is "1000" in default
    	log(TUT + "1: Verify the default value of Max. entries");
	    lookIntegerValue(MAX_ENTRIES,maxEntryDefault);
   	
    	// Procedure: do search and look into some entries if exist
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
	    if(baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)) log("Abort the test due to miss to get entries");
	    else{
	    	// TEST2: Verify the number of entries are not exceeds "1000"
	    	log(TUT + "2: Verify the total number of the entry list is equal to 1000");
	    	doAssertThat(baseProc.getTotalNumberOfEntry(cmt,Z_ENTRY_INFO),is(maxEntryDefault));
	    }
	    
	    // Procedure: Open search popup
	    baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

	    // TEST3: Verify Max.entries number remains
    	log(TUT + "3: Verify the default value of Max. entries remains");
	    lookIntegerValue(MAX_ENTRIES,maxEntryDefault);
	    
		// Procedure: Load default values and close the popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Max entries: maximum and minimum number
	 */
	public void maxEntriesWithBorder(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String warnText;
		
		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);

	    int[] entryNum = {maxEntryMax, maxEntryMin};
	    for(Integer num : entryNum){
	    	// Procedure: put the initial value (It's the same as the default one at the first loop)
	    	popProc.putText(cmt, Z_TABLECHILD, Z_TYPETEXT, MAX_ENTRIES, onlyOne, String.valueOf(num));

	    	// TEST1: Verify Max.entries are set to "1000"
	    	log(TUT + "1: Verify Max. entries is set");
		    lookIntegerValue(MAX_ENTRIES,num);

	    	// Procedure: click the upper/lower triangle for increasing/decreasing the number (Must be ignored.)
	    	if(num == maxEntryMax) popProc.clickImage(cmt, Z_TABLECHILD, zclassBtnUpper ,MAX_ENTRIES,onlyOne);
	    	else				   popProc.clickImage(cmt, Z_TABLECHILD, zclassBtnLower ,MAX_ENTRIES,onlyOne);
	    	
	    	// TEST2: Verify Max. entries is not changed
	    	log(TUT + "2: Verify Max. entries value is not changed");
		    lookIntegerValue(MAX_ENTRIES,num);
  	
	    	// Procedure: Input illegal number (maximum+1/minimum-1) into Max. entries and focus it, then get warning message
	    	int inputted = num == maxEntryMax? num + 1: num - 1;
	    	popProc.putText(cmt, Z_TABLECHILD, Z_TYPETEXT, MAX_ENTRIES, onlyOne, String.valueOf(inputted));
		    popProc.focusing(cmt,Z_TABLECHILD,focusPos);
		    warnText = popProc.getText(cmt,zclassWarnPopup,zclassPopupMsg,0,0,WARNDIALOG);

	    	// TEST3: Verify the warning message is shown
	    	log(TUT + "3: Verify the warning message of Max. entries appears");
	    	doAssertThat(warnText,is(maxEntryWarnMsg));
	    
	    	// Procedure: close the warning and put the initial num
	    	popProc.closeWarnPopup(cmt, zclassWarnPopup, zclassPopupClose, maxEntryWarnMsg);
	    	popProc.putText(cmt, Z_TABLECHILD, Z_TYPETEXT, MAX_ENTRIES, onlyOne, String.valueOf(num));

	    	// Procedure: click the lower/upper triangle for decreasing/increasing the number in the range
	    	log(" Change the max entry number by clicking the arrow image");
	    	if(num == maxEntryMax)  // -1
	    		popProc.clickImage(cmt, Z_TABLECHILD, zclassBtnLower ,MAX_ENTRIES,onlyOne);
	    	else					// +1
	    		popProc.clickImage(cmt, Z_TABLECHILD, zclassBtnUpper ,MAX_ENTRIES,onlyOne);

	    	// TEST4: Verify Max. entries number is changed
	    	log(TUT + "4: Verify Max. entries value is changed");
	    	int expected = num == maxEntryMax? num - 1: num + 1;	// Expected number is -1 or +1
	    	lookIntegerValue(MAX_ENTRIES,expected);

	    	// Procedure: Do search and look into some entries. In case of no entry, abort the test
		    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
		    if(baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)) log("Abort the test due to miss to get entries");
		    else{
		    	// TEST5: Verify the searched entries are equal to the Max entries
		    	log(TUT + "5: Verify the total number of the entry list is equal to the Max. entries");
		    	doAssertThat(baseProc.getTotalNumberOfEntry(cmt,Z_ENTRY_INFO),is(expected));
		    	}
		    
			// Procedure: Open search popup
			baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
	    }
	    
		// Procedure: Load default values
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    
	    // TEST6: Verify Max. entries are set to the default value
    	log(TUT + "5: Verify Max. entries value is set to the default value");
    	lookIntegerValue(MAX_ENTRIES,maxEntryDefault);
	    
    	// Procedure: close the popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * Execution Information
	 */
	public void execInf(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Open search popup and expand Execution Information pane
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.expandPane(cmt,Z_TABLECHILD,zclassCaption,CLOSED_PANE,EXECINF,execinfText);
		
		// Procedure: Put texts into all text fields 
 	    for(int i = 0; i < textLabelExecinf.length; i++){
 	    	log("[" + textLabelExecinf[i] + "]: \"" + PARAM_EXECINF[i] + "\"");
 	    	// TEST1: Verify the text field is empty
 	    	// Note that "assertThat(...,is(nullValue())" doesn't work, so check the length and assert it's zero.
 	    	log(TUT + "1: Verify the text field is empty in default");
 	    	lookTextLength(CLOSED_PANE,i,0);

		    // Procedure: Put a string into the text field
		    popProc.putText(cmt,Z_TABLECHILD,Z_TYPETEXT,CLOSED_PANE,i,PARAM_EXECINF[i]);

		    // TEST2: Verify the text of the text field
 	    	log(TUT + "2: Verify the text field is updated");
 	    	lookTextValue(CLOSED_PANE,i,PARAM_EXECINF[i]);
 	    }
 	    
		// Procedure: Select from all menus except "Observing cycle", which is handled another one
 	    for(int j = 1; j < allMenusTotal.length; j++){
 	    	log("[" + menuLabelExecinf[j] + "]: \"" + allMenus[j][1] + "\"");
 	    	// Procedure: Get the default value
	    	// Note that j=1, "Calibration" menu, doesn't set "selectedindex=0" in the code, so that 
 	    	//  getFirstSelectedOption() doesn't work and cause exception. Just ignore this test anyway. 
 	    	if(j != 1){
		    	// TEST3: Verify the default selected one is empty
		    	log(TUT + "3: Verify the default selected one is empty");
		    	doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,j),is(allMenus[j][0]));
 	    	}
	    	
	    	// Procedure: Select an option and get it
	    	popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,j,allMenus[j][1]);

	    	// TEST4: Verify the selected option
	    	log(TUT + "4: Verify the selected option");
	    	doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,j),is(allMenus[j][1]));
	    }

	    // Procedure: Close the popup by Cancel and then open search popup
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		
		// TEST5: Verify "Execution Information" pane is closed
		log(TUT + "5: Verify \"Execution Information\" pane is closed");
	    doAssertThat(baseProc.checkText(cmt,"Observinf Cycle"),is(equalTo(false)));
		
		// Procedure: Expand Execution Information pane
	    popProc.expandPane(cmt,Z_TABLECHILD,zclassCaption,CLOSED_PANE,EXECINF,execinfText);

	    // TEST6: Verify all text fields and menus (except Observing cycle) are empty
	    log(TUT + "6a: Verify all text fields are empty");
 	    for(int i = 0; i < textLabelExecinf.length; i++)
 	    	lookTextLength(CLOSED_PANE,i,0);
    	log(TUT + "6b: Verify all menus are not set");
 	    for(int j = 1; j < allMenusTotal.length; j++)
 	    	if(j != 1){
		    	doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,j),is(allMenus[j][0]));
		    }

 	    // Procedure: Close the popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Execution Information > Tooltip
	 */
	public void execInfTooltip(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Open search popup and expand Execution Information pane
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.expandPane(cmt,Z_TABLECHILD,zclassCaption,CLOSED_PANE,EXECINF,execinfText);

		// TEST1: verify the tooltip of PhotonicReference
		log(TUT + "1: Verify the tooltip of \"Phoronic Reference\"");
		doAssertThat(popProc.getTooltip(cmt, Z_TABLECHILD, "z-row", CLOSED_PANE, prefTooltipPos),
				   is(equalTo(prefTooltip)));

		// TEST2: verify the tooltip of Executive(s)
		log(TUT + "1: Verify the tooltip of \"Executive(s)\"");
		doAssertThat(popProc.getTooltip(cmt, Z_TABLECHILD, zclassText, CLOSED_PANE, execTooltipPos),
				   is(equalTo(execTooltip)));
		
		// Procedure: Close the popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
	
	/*
	 * Execution Information > Observing Cycle
	 */
	public void execInfCycle(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Open search popup and expand Execution Information pane
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.expandPane(cmt,Z_TABLECHILD,zclassCaption,CLOSED_PANE,EXECINF,execinfText);

		// TEST1: Verify the default Observing cycle is empty
		log(TUT + "1: Verify the default Observing Cycle");
    	doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,0),is(cycleMenu[0]));

		// Procedure: Select an option from menu, then do search
	    for(int n = 1;n < cycleMenu.length;n++){	// Ignore the first empty option 
	    	log(TUT + ": Verify the cycle name of [" + cycleMenu[n] + "] in the menu");
	    	doAssertThat(popProc.getText(cmt,Z_TABLECHILD,zclassSelectOption,CLOSED_PANE,n,OBSCYCLEMENU),is(cycleMenu[n]));			

	    	// Procedure: Select an option. Interval and Project code are updated automatically
			popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,cycleMenu[n]);
			popProc.waitAppearance(cmt,intervalOption[intervalOtherPos]);
			//			popProc.confirmLabel(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
			String curBegin = popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,BEGIN,0);
			String curEnd = popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,END,0);
			
			// TEST2: Verify texts in Observing Cycle, project code and begin/end time stamps
			log(TUT + "2: Verify texts in Observing Cycle, project code and begin/end time stamps");
	    	doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,0),is(cycleMenu[n]));
			doAssertThat(popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,CLOSED_PANE,0),is(cycleYear[n] + cycleTail));
			doAssertThat(curBegin,is(cycleInterval[n][0]));
			doAssertThat(curEnd,is(cycleInterval[n][1]));
			
	    	// Procedure: do search and look into some entries if exist
	    	popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
		    if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)){
	    		for(int i = 0; i < 2; i++){
	    			// Procedure: Sort the entry list and get the first Type/Timestamps/project Code
	    			baseProc.sortList(cmt, Z_PANELCHILD, Z_LISTHEAD, CLM_TS);
	    			String typeStr = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,CLMS[CLM_TYPE]);
	    			String tsStr = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TS,CLMS[CLM_TS]);
	    			String checkTsStr = baseProc.getBeginOrEndTS(cmt,tsStr,typeStr,i);	// Get "To" timestamp
	    			String pcodeStr = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_PCODE,CLMS[CLM_PCODE]);

	    			// TEST3: Verify project code and Timestamps
	    			log(TUT + "Verify Project Code [" + pcodeStr + "] and timestamps [" + checkTsStr + "]");
	    			doAssertThat(pcodeStr.contains(cycleYear[n]),is(equalTo(true)));	// Check the cycle year
	    			doAssertThat(pcodeStr.contains("CSV"),is (equalTo(false)));		// Check no CSV projects are gotten
	    			if(checkTsStr == null){
	    				log("Abort the test due to miss to get \"Type\" and/or \"Timestamp\"");			// Test is invalid
	    			}else{
	    				if(i == 0)
	    					doAssertThat(curBegin.compareTo(checkTsStr) <= 0,is(equalTo(true))); // Begin TS < Checked TS
	    				else
	    					doAssertThat(checkTsStr.compareTo(curEnd) <= 0,is(equalTo(true)));	// Checked TS < End TS
	    			}
	    		}
	    	}
			// Procedure: Open search popup and get the texts which are changed before
			baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
			curBegin = popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,BEGIN,0);
			curEnd = popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,END,0);
			
			// TEST4: Verify Cycle, Interval and Project code still remains 
			log(TUT + "4: Verify Cycle, Interval and Project code still remains");
	    	doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,0),is(cycleMenu[n]));
			doAssertThat(popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,CLOSED_PANE,0),is(cycleYear[n] + cycleTail));
			doAssertThat(curBegin,is(cycleInterval[n][0]));
			doAssertThat(curEnd,is(cycleInterval[n][1]));
	    }

		// Procedure: Load default values and get Cycle 
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);

		// TEST5: Verify the option in Observing Cycle, project code and Interval
    	doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,0),is(cycleMenu[0]));
    	doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,0),is(intervalOption[intervalDefaultPos]));
    	doAssertThat(popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,CLOSED_PANE,0),is(""));

		// Procedure: close the search popup
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

   	/*
	 * Execution Information > Several menus
	 */
	public void execInfMenu(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Open search popup and set intervals for testing, then expand Execution Information pane
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
		popProc.expandPane(cmt,Z_TABLECHILD,zclassCaption,CLOSED_PANE,EXECINF,execinfText);
	    
		String option;
		String expected;
		// Procedure: Change options of all menus
	    for(int j = 1; j < allMenusTotal.length; j++){
		// Procedure: Select an option from menu one by one, then do search
    		for(int n = 1; n < allMenusTotal[j]; n++){	// Ignore the first empty option
    			// Procedure: Set the expected label for testing and get an option from the menu
    			expected = allMenus[j][n];
    			option = popProc.getText(cmt,Z_TABLECHILD,Z_SELECT,zclassSelectOption,CLOSED_PANE,j,n,menuLabelExecinf[j]);
    			
    			// TEST1: Verify the option name is expected one
    			log(TUT + "1: Verify [" + expected + "] exists in [" + menuLabelExecinf[j] + "]");
    			doAssertThat(option,is(expected));

    			// Procedure: Select an option and get the selected one
    			popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,j,expected);
 
    			// TEST2: Verify the option name is shown rightly
     			doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,j),is(expected));
    			// Procedure: Do search and look into some entries if exist
    			popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
    		    if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)){
   					// Procedure: open Details and
    		    	// TEST3: Verify the selected option is shown
    		    	// Note that all menus are verified in Details
    		    	lookIntoDetails(rowFirst,menulabelDetails[j],expected);

    		    	// Procedure: In case of "Array family" and "Correlator type", check the type
    		    	if((j == 2) || (j == 3)){
    					// Procedure: Sort in ascending as first, the second is in descending and then
   		    			// TEST3a: Verify the type at selecting "Array family" and "Correlator type"
    		    		for(int cnt = 0; cnt < 2; cnt++){
    		    			baseProc.sortList(cmt, Z_PANELCHILD, Z_LISTHEAD,CLM_TYPE);
    		    			String str = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,CLMS[CLM_TYPE]);
    		    			doAssertThat(str.equals(TYPE_NAME[2]) || str.equals(TYPE_NAME[6]) || 
    		    					   str.equals(TYPE_NAME[7]) || str.equals(TYPE_NAME[8]),is(equalTo(true)));
    		    		}
    		    	}
    			}
    			// Procedure: Open search popup and get the appeared option name
    			baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
 			
    			// TEST4: Verify the selected option remains 
       			log(TUT + "4: Verify the selected option name remains");
       			doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,j),is(expected));
          	}
    		// Procedure: Put default after testing all options in a menu
			popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,j,allMenus[j][0]);
			expected = allMenus[j][0];

			// TEST5: Verify the default setting
  			log(TUT + "Verify the default set option");
   			doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,j),is(expected));
   		}
		// Procedure: Load default values and close the popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    
	    // Procedure: Check the all menus one by one
	    for(int i = 1; i < allMenusTotal.length; i++){	
	    	if(i == 1) continue;	// Ignore Calibration, see execInf()
		    // TEST6: Verify the default value
	    	log(TUT + "6: Verify the default value");
   			doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,CLOSED_PANE,i),is(allMenus[i][0]));
   	    }

	    // Procedure: close the search popup
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * Execution Information > Several texts
	 */
	public void execInfText(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Open search popup and set intervals for testing, then expand Execution Information pane
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
		popProc.expandPane(cmt,Z_TABLECHILD,zclassCaption,CLOSED_PANE,EXECINF,execinfText);

		// Procedure: Check text fields in order
	    // Note that strings are compared in small letters here
 	    for(int i = 0; i < textLabelExecinf.length; i++){
 	    	log("[" + textLabelExecinf[i] + "]");
 	    	String expected = PARAM_EXECINF[i];				// Save the input text
 
		    // Procedure: Input string in the text field
		    popProc.putText(cmt,Z_TABLECHILD,Z_TYPETEXT,CLOSED_PANE,i,expected);

		    // TEST1: Verify the string is shown
 	    	log(TUT + "1: Verify the text string is shown");
 	    	lookTextValue(CLOSED_PANE,i,expected);

		    // Procedure: Do search and look into some entries if exist
		    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
   		    if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)){
			    if(textPos2[i] != -1){	// For Project Code, SchedBlock Name, and ExecBlock UID
			    	// Procedure: Sort the entry list twice and
			    	// TEST2: Verify the text is shown
			    	// TODO(05-29) "SBname" and "EBuid" are delayed by something. Manage it as soon as possible
			    	lookSortedList(textPos2[i],expected,true,true);
			    }else{					
			    	// Procedure: Check the entry's total number
			    	int n = baseProc.getTotalNumberOfEntry(cmt,Z_ENTRY_INFO);
			    	int cellMax = n > chkCnt? chkCnt : n;		// The maximum number is not over entry numbers
			    	for(int cellId = 0; cellId < cellMax; cellId++){	// Do in a few entries
			    		// TEST2: Verify the text is shown
			    		// (2016-02-02) The entry clicking with "Antenna" causes "TimeoutException" always!
			    		lookIntoDetails(cellId,textLabelExecinf[i],expected);
			    	}

			    	// Procedure: In case of "SB uid" and "Antenna", check the type
    		    	if((i == 5) || (i == 8)){
    					// Procedure: Sort in ascending as first, the second is in descending and then
   		    			// TEST3a: Verify the type at selecting "SB uid" and "Antenna"
    		    		for(int cnt = 0; cnt < 2; cnt++){
    		    			baseProc.sortList(cmt, Z_PANELCHILD, Z_LISTHEAD,CLM_TYPE);
    		    			String str = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,CLM_TYPE,CLMS[CLM_TYPE]);
    		    			Boolean TorF;
    		    			if(i == 5) TorF = str.equals(TYPE_NAME[6]) || str.equals(TYPE_NAME[7]) || str.equals(TYPE_NAME[8]);
    		    			else	   TorF = str.equals(TYPE_NAME[2]) || str.equals(TYPE_NAME[6]) || 
	    				       	      	      str.equals(TYPE_NAME[7]) || str.equals(TYPE_NAME[8]);
    		    			doAssertThat(TorF,is(equalTo(true)));
    		    		}
    		    	}
			    }
	    	}
		    // Procedure: Open search popup again
		    baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

		    // TEST3: Verify the input text remains
		    log(TUT + "3: Verify the text remains");
		    doAssertThat(baseProc.checkText(cmt,"Observing Cycle"),is(equalTo(true)));// Verify Execution Inf. opens
		    lookTextValue(CLOSED_PANE,i,expected);

		    // Procedure: clear the text field
		    popProc.clearText(cmt,Z_TABLECHILD,Z_TYPETEXT,CLOSED_PANE,i);
		    
		    // TEST4: Verify the input text remains
		    log(TUT + "4: Verify the text is cleared");
 	    	lookTextLength(CLOSED_PANE,i,0);
 	    }
	    
		// Procedure: Load default values and close the popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * Engineering
	 */
	public void engineering(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
		popProc.expandPane(cmt,Z_TABLECHILD,zclassCaption,CLOSED_PANE,ENGINEER,engineerText);

		// Check the group name one by one
		keywordOneByOne(engGroupName,CLOSED_PANE,rowEngLabelPos,"Activity");
		
		// Procedure: Load default values and close the popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}
		
	/*
	 * Engineering > Multiple
	 */
	public void engineeringMultiple(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters

		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
		popProc.expandPane(cmt,Z_TABLECHILD,zclassCaption,CLOSED_PANE,ENGINEER,engineerText);

		// Check group names
		keywordMultiple(engGroupName,CLOSED_PANE,rowEngLabelPos);

		// Procedure: Close the popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
		log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * Save the modified search criteria
	 * Note that the saving data stores in 24 hours, that is, the search criteria revert to default after 24 hours.
	 * This test doesn't verify the behavior of 24 hours later. It should be verified in the exploratory test.
	 */
	public void saveCurrent(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		int typeHere = TYPES_ARRAY;
		
		// Procedure: Open search popup
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

	    // Procedure: Set some criteria (interval=Other,type=SHIFT,comment)
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
	    popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
		popProc.clickCheckbox(cmt, Z_TABLECHILD, Z_TYPECHKBOX, GENERAL,typeHere,SELECT);
	    popProc.putText(cmt,Z_TABLECHILD,Z_TYPETEXT,GENERAL,commentPos,PARAM_GENERAL[commentPos]);
	    
		// TEST1: Verify the input data is shown
		log(TUT + "1: Verify the changed data");
		doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,0),is(intervalOption[intervalOtherPos]));
		doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,typeHere),is(equalTo(true)));
		lookTextValue(GENERAL,commentPos,PARAM_GENERAL[commentPos]);
   
    	// Procedure: do search, see summary message and then get entry numbers
    	popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
    	int entryNum = 0;			// Save the entry total number
    	if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY))
    		entryNum = baseProc.getTotalNumberOfEntry(cmt,Z_ENTRY_INFO);
    	
	    // Procedure: Open search popup again
	    baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);

		// TEST2: Verify the input data remains
		log(TUT + "2: Verify the input data remains");
		doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,0),is(intervalOption[intervalOtherPos]));
		doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,typeHere),is(equalTo(true)));
	    lookTextValue(GENERAL,commentPos,PARAM_GENERAL[commentPos]);
	    
    	// Procedure: Click [Save current] button,confirm the dialog, and then close the search popup by Cancel
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_SAVE);
		popProc.confirmDialog(cmt,SAVECURRENT_DIALOG,Z_DIALOG_BTN,LOGOUT_YES);
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);
		
	    // Procedure: Logging out and then logging in
		baseProc.logout(cmt,Z_TOOLBAR_BTN,BASE_LOGOUT,Z_DIALOG_BTN,LOGOUT_YES,LOGOUT_DIALOG,LOGOUT_DONE);
		baseProc.openUrl(driver,BASEURL);
		baseProc.login(cmt,WEBTITLE,USERNAME,PASSWORD);
    
    	// Procedure: See summary message and then get entry numbers
    	int entryNumNew = 0;			// Save the entry total number
    	if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY))
    		entryNumNew = baseProc.getTotalNumberOfEntry(cmt,Z_ENTRY_INFO);
 
    	// TEST3: Verify the total entry number is identical to the previous one
		log(TUT + "3: Verify the total number of entry");
    	doAssertThat(entryNumNew,is(entryNum));
	    
    	// Procedure: Open search popup
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		
		// TEST4: Verify the input data remains
		log(TUT + "4: Verify the input data remains as default");
		doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,0),is(intervalOption[intervalOtherPos]));
		doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,typeHere),is(equalTo(true)));
	    lookTextValue(GENERAL,commentPos,PARAM_GENERAL[commentPos]);

		// Procedure: Load default values
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);

		// TEST5: Verify the input data is cleared
		log(TUT + "5: Verify the input data is cleared");
		doAssertThat(popProc.textInSelection(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,0),is(intervalOption[intervalDefaultPos]));
		doAssertThat(popProc.stateInSelection(cmt,Z_TABLECHILD,Z_TYPECHKBOX,GENERAL,typeHere),is(equalTo(false)));
	    lookTextValue(GENERAL,commentPos,"");

    	// Procedure: Click [Save current] button in order to revert to normal
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_SAVE);
		popProc.confirmDialog(cmt,SAVECURRENT_DIALOG,Z_DIALOG_BTN,LOGOUT_YES);

	    // Procedure: Close the popup by Search
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
	    
	    log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}

	/*
	 * Do search with AND,OR and NOT in text fields
	 */
	public void searchWithAndOr(){
		log(TU);
		log("## " + new Throwable().getStackTrace()[0].getMethodName() + " ##");
		totalAssert = ngAssert = 0;									// Initialize counters
		String testFile = "/users/knaka/searchStrings.txt";
		FileReader fr = null;
		BufferedReader br = null;
		String tItem = "";		// Title of the column (Location, Project code, etc.)
		int nCLM = -1;			// Position of the column(CLM_LOC,CLM_PCODE, etc.)
		int posText = -1;		// Position of the text field in the search criteria
		Boolean expectedJudge = true;	// In case of "NOT", expectedJudge=false, else true
		
		// Procedure: Open search popup and set intervals for testing
		baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
		popProc.selectOption(cmt,Z_TABLECHILD,Z_SELECT,INTERVAL,intervalOption[intervalOtherPos]);
		popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, BEGIN, OTHER_BEGIN);
		popProc.putDate(cmt,Z_TABLECHILD, Z_TYPETEXT, END, OTHER_END);
	
		// Procedure: Open the input file and read a line except comment lines.
		//			  Then put the text into the search criteria, do search and check the result
		try{
			fr = new FileReader(testFile);
			br = new BufferedReader(fr);
			String str = "";
			// Procedure: Read a line one by one
			while((str = br.readLine()) != null){
				if(str.matches("^#.*$")) continue;	// Ignore the comment
				log("Got a line from file: [" + str + "]");

				if(str.contains("item")){			// Get the name of search criteria
					// Procedure: Save the name and position of the target column head
					tItem = str.split("item=")[1];	// Get the name of item
					for(int i = 0; i < CLMS.length; i++){	// Get the position of the column
						if(tItem.equals(CLMS[i])){ nCLM = i; break;	}
					}
					for(int i = 0; i < textPos.length;i++){	// Get the position of the text field in General
						if(textPos[i] == nCLM){	posText = i; break;	}
					}
					continue;
				}

				if(tItem != "" && nCLM != -1){		// If tItem and column pos are ready, go ahead 
					// Procedure: Split "test=xxx exp=xxxx" into "xxx" and "xxxx" 
					String[] tBuf = str.split("test=")[1].split(" exp=");	// Get a line
					String inText = tBuf[0];		// Get string of "test"
					String outText = tBuf[1];		// Get string of "exp"
					if(inText.toLowerCase().contains("not")) expectedJudge = false;	// NOT's expectation is false
					else expectedJudge = true;		// AND/OR's expectation is true
					
					// Procedure: Input string in the text field
					popProc.putText(cmt,Z_TABLECHILD,Z_TYPETEXT,GENERAL,posText,inText);

					// TEST1: Verify the input text is shown
					log(TUT + "1: Verify the input text is appeared");
					lookTextValue(GENERAL,posText,inText);
		    
				    // Procedure: Do search and see if some entries are shown or not
				    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
				    Boolean entryJudge = baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY);

				    if(outText.equals("none")){ // No entry is expected
				    	log(TUT + "2: Verify if no entry is expected");
				    	doAssertThat(entryJudge,is(true));
				    }else{						// Check the entries
				    	log(TUT + "2: Verify the search result");
				    	if(entryJudge == true) log(" Test skip necause no entry is found"); // no test due to no entry
				    	else{
				    		if(outText.contains("|")) lookSortedListWithOr(nCLM,outText,expectedJudge); // There are multiple possibility
				    		else				      lookSortedList(nCLM,outText,true,expectedJudge);
				    	}
				    }
				}else{		// If tItem has not been gotten or column pos is not selected, the test cannot go ahead, so abort
					log("ERROR: \"item\" is not described in the input file.Test is aborted.");
					totalAssert++;
					ngAssert++;

					// Procedure: open the search popup to set defaults
					baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
					break;
				}
				// Procedure: Open the search popup again to set the next data or set defaults
				//			  and clear the text field
				baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
			    popProc.clearText(cmt,Z_TABLECHILD,Z_TYPETEXT,GENERAL,posText);
			}
		}catch(Exception exRead){
			exRead.printStackTrace();
		}finally{
			// Procedure: Close the text file
			try{
				br.close();
			}catch(Exception exClose){
				exClose.printStackTrace();
			}
		}
				
		// Procedure: Load default values and close the popup by Cancel
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_CANCEL,CHECK_POPUP_SEARCH);

	    log(TU + "Finished");		
		log(TUT + " result: NG/all Assertions = " + ngAssert + " / " + totalAssert);
		assertThat(ngAssert,is(0));							// Show the error in case of NG
	}


	/***********************************************************************************************/

	/*
	 * @brief Put any milliseconds into the timestamp\
	 * @param String inTs
	 */
	String addMillisecToTs(String inTs){
		String[] tmpStr = inTs.split(" ",0);
		return tmpStr[0] + " " + tmpStr[1] + ".000";
//		return tmpStr[0] + " " + tmpStr[1] + ".000" + " " + tmpStr[2];
	}
	
	/*
	 * @brief Shuffle the array of numbers and then return the first two numbers
	 * @param int total
	 */
	int[] shuffledNum(int total){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < total; i++){
			list.add(i);								// Arrange numbers in order 
		}
		Collections.shuffle(list);						// Shuffle numbers

		int[] rtnNums = {list.get(0), list.get(1)};		// Save the shuffled first two numbers

		if(rtnNums[0] > rtnNums[1]){					// Change the order in ascension 
			int tmp = rtnNums[0];
			rtnNums[0] = rtnNums[1];
			rtnNums[1] = tmp;
		}
		return rtnNums;
	}
	
	/*
     * @brief Get a text from a text field and verify it
     * @param int posP
     * @param int posC
     * @param String expected
     */
	void lookTextValue(int posP,int posC,String expected){
		String text;
		text = popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,posP,posC);
		if(text == null || text == "") log(NOTEST);
		else						   doAssertThat(text,is(expected));
	}
		
	/*
     * @brief Get a text from a text field and verify the length
     * @param int posP
     * @param int posC
     * @param int len
     */
	void lookTextLength(int posP,int posC,int len){
		String text;
		text = popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,posP,posC);
		if(text == null) log(NOTEST);
		else			 doAssertThat(text.length(),is(len));
	}
	
	/*
	 * @brief Get a text from a text field and verify it by integer
	 * @param int pos
	 * @param int expectedNum
	 */
	void lookIntegerValue(int pos,int expectedNum){
		String text;
		text = popProc.getInputText(cmt,Z_TABLECHILD,Z_TYPETEXT,pos,0);
		if(text == null || text == "") log(NOTEST);
		else						   doAssertThat(Integer.parseInt(text),is(expectedNum));
	}
	
	/*
	 * @brief Sort the entry list by a header column and verify the top entry
	 * @param pos instead of a header position like typePos
	 * @param String expected which is the expected string at assertion (typesName[i])
	 * @param boolean isIncluded
	 * @param boolean judge, which is used only when "isIncluded" is true
	 */
	void lookSortedList(int pos, String expected, boolean isIncluded, boolean judge){
		for(int done = 0; done < 2; done++){
			// Procedure: Sort in ascending as first, the second is in descending
			baseProc.sortList(cmt, Z_PANELCHILD, Z_LISTHEAD, pos);
			String str = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,pos,CLMS[pos]);

			// TEST with the type of the top item
			log(TUT + "Veryfy the top item in the list");
			if(str == null || str == ""){
				log(NOTEST);
				break;
			}

			// TEST: Verify types in the list
			log(TUT + " in lookSortedList(): Verify the sorted top entry is expected");
			if(isIncluded) doAssertThat(str.toLowerCase().contains(expected.toLowerCase()),is(equalTo(judge)));
			else		   doAssertThat(str,is(expected));
		}
	}
	
	/*
	 * @brief Sort the entry list by a header column and verify the top entry
	 * @param pos instead of a header position like typePos
	 * @param String expected which is the expected string at assertion (typesName[i])
	 * @param boolean judge, which is used only when "isIncluded" is true
	 */
	void lookSortedListWithOr(int pos, String expected, Boolean judge){
		for(int done = 0; done < 2; done++){
			// Procedure: Sort in ascending as first, the second is in descending
			baseProc.sortList(cmt, Z_PANELCHILD, Z_LISTHEAD, pos);
			String str = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,pos,CLMS[pos]);

			// TEST with the type of the top item
			log(TUT + "Veryfy the top item in the list");
			if(str == null || str == ""){
				log(NOTEST);
				break;
			}

			// TEST: Verify if the expected output texts are in the column
			log(TUT + " in lookSortedListWithOr(): Verify the sorted top entry includes the expected text");
			String[] buf = expected.split("\\|");	// Split the expected text by "|"
			int flag = 0;
			for(String s:buf){					// If the expected text is included, increment the flag
				log(" Tested text:[" + s + "]");
				if(str.toLowerCase().contains(s.toLowerCase())){ log(" Hit!"); flag++; } 
			}
			doAssertThat(flag > 0,is(judge));	// If flag is not zero, the expected text is in the column
		}
	}
	/*
	 * @brief Sort the entry list by a header column and verify the top entry which has at least one of types
	 * @param pos instead of a header position like typePos
	 * @param String[] expected, which has 2 members
	 * @param boolean isExpected
	 */
	void lookSortedListWithTwoItems(int pos, String[] expected, boolean isExpected){
		for(int done = 0; done < 2; done++){
			// Procedure: Sort in ascending as first, the second is in descending
			baseProc.sortList(cmt, Z_PANELCHILD, Z_LISTHEAD, pos);
			String str = baseProc.getColumn(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,pos,CLMS[pos]);

			// TEST with the type of the top item
			log(TUT + " in lookSortedListWithTwoItems(): Verify the top item in the list");
			if(str == null || str == "") log(NOTEST);
			else{
				if(isExpected) doAssertThat(str.equals(expected[0]) || str.equals(expected[1]),is(equalTo(true)));
				else	
					for(int i = 0; i < expected.length; i++) doAssertThat(str.equals(expected[i]),is(equalTo(false)));		
			}
		}
	}
		
	/*
	 * @brief Get 1 row from details area and check the label, if the label is expected, return its data
	 * @param String label, which is expected one to retrieve information
	 */
	String[] getRowInDetails(String label){
		String members[] = new String[]{"",""};

		// Get the number of rows
		int rowMax = baseProc.getNumbersOfRow(cmt,Z_DETAILS,Z_ROW,0);
		
		// Get one row in turn. In case of the expected label is appeared, stop the checking
		int cnt;
		for(cnt = 0; cnt < rowMax;cnt++){
			members = baseProc.getDetailsByRow(cmt,Z_DETAILS,Z_ROW,0,cnt);	// Get one row
			if(members[0].contains(label)) break;		// In case the expected label is appeared, get out from the loop
		}

		// Check information of the label if there are any characters or not
		if(cnt == rowMax) log("No information at " + label);
	
		return members;
	}

	/*
	 * @brief Open details and get all information, then verify the contents. 
	 * 	      This is used at verifying of comments.
	 * @param int listNum as the line number of the list, instead of cellId
	 * @param String waitLabel in order to wait until this label appeared
	 * @param String label as the label name which is expected in Details
	 * @param String expect as the expected text in Details
	 * @param boolean judge as the expected assertion
	 */
	void lookDetails(int listNum, String waitLabel, String label, String expected, boolean judge){
		// Procedure: Click the first column if the defined list entry 
		baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,listNum,0,waitLabel);
		String details = baseProc.getDetails(cmt,Z_DETAILS,Z_PANELCHILD,0,0);
		if(details != null){
			// TEST with contents
			log(TUT + " in lookDetails(): Verify the searched word is included");
			doAssertThat(details.contains(label),is(equalTo(true)));	// Check label exists
			doAssertThat(details.toLowerCase().contains(expected.toLowerCase()),is(equalTo(judge)));
		}	
		// Procedure: Need to refresh for out of focus from the clicked entry above
		//            If not, warning will be appeared at logging out!
		baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
	}

	/*
	 * @brief Open Details and get a row included expected label, then verify the label and data
	 * @param int lineN as a line number in the list, instead of cellId
	 * @param String label as the label name which is shown in Details
	 * @param String expect as the expected text in Details
	*/
	void lookIntoDetails(int lineN, String label, String expected){
		// Procedure: Get the current ID message in details if it is shown
		String entry_id_msg = null;
		if(baseProc.checkText(cmt,DETAIL_HEADER))
			entry_id_msg= popProc.getText(cmt,Z_DETAILS,Z_HEADER,0,0,DETAILAREA);
		
		// Procedure: Click the entry column and wait until the label is shown
		// (2016-02-02) Note that TimeoutException occurs in case of "Antennas" in execInfText().
		baseProc.clickEntryCheckingId(cmt,Z_LISTITEM,Z_LISTCELL,lineN,0,label,entry_id_msg);
		String members[] = getRowInDetails(label);

		// TEST with contents
		log(TUT + " in lookDetailsByRow(): Verify the searched word [" + expected + "] is included");
		doAssertThat(members[0].contains(label),is(equalTo(true)));	// Check label exists
		doAssertThat(members[1].toLowerCase().contains(expected.toLowerCase()),is(equalTo(true)));

		// Procedure: Need to refresh for out of focus from the clicked entry above
		//            If not, warning will be appeared at logging out!
		baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
	}

	/*
	 * @brief Check Keyword in a select box one by one
	 * @param String keynames instead of keywordName or engGroupName
	 * @param int tablePos instead of generalPos or closedPanePos
	 * @param int rowPos instead of rowKeyLabelPos or rowEngLabelPos
	 * @param String labelDetail as the label in Detail like "Keywords" or "Activity"
	 */
	void keywordOneByOne(String[] keynames,int tablePos,int rowPos,String labelDetail){
		// Procedure: get a name in keynames one by one
		for(int i = 0; i < keynames.length; i++){
	    	log(" [" + keynames[i] + "]");

	    	// TEST1: Verify no keywords are checked in default
	    	log(TUT + "1: Verify no labels are shown in the left area");
	    	doAssertThat(popProc.getText(cmt,Z_TABLECHILD,zclassRow,zclassLabel,tablePos,rowPos,keywordPos,KEYWPANEL),is(""));
	    	
	    	// Procedure: Click one keyword for checking and get a text from the list	
	    	popProc.clickItemAndWait(cmt, Z_TABLECHILD, zclassCheckImg, tablePos,i,ENABLE);
	    	
	    	// TEST2: Verify the checked keyword is shown 
	    	log(TUT + "2: Verify the checked label is shown in the left area");
	    	doAssertThat(popProc.getText(cmt,Z_TABLECHILD,zclassRow,zclassLabel,tablePos,rowPos,keywordPos,KEYWPANEL),is(keynames[i]));

	    	// Procedure: Do search and look into some entries if exist
		    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
	    	if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)){
		    	// TEST3: Verify the keyword is shown in details
		    	lookDetails(rowFirst,keynames[i],labelDetail,keynames[i],true);
		    }
	    	
		    // Procedure: Open search popup
	    	baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
	    	
	    	// TEST4: Verify the checked keyword remains
	    	log(TUT + "4: Verify the checked label is still shown in the left area");
	    	doAssertThat(popProc.getText(cmt,Z_TABLECHILD,zclassRow,zclassLabel,tablePos,rowPos,keywordPos,KEYWPANEL),is(keynames[i]));
	    	
	    	// Procedure: Click the keyword again for unchecking 
	    	popProc.clickItemAndWait(cmt, Z_TABLECHILD, zclassCheckImg,tablePos,i,DISABLE);

	    	// TEST5: Verify no keywords appear after unchecking
	    	log(TUT + "5: Verify no labels are shown in the left area");
	    	doAssertThat(popProc.getText(cmt,Z_TABLECHILD,zclassRow,zclassLabel,tablePos,rowPos,keywordPos,KEYWPANEL),is(""));
	    }

	}

	/*
	 * @brief Check multiple Keywords in a select box
	 * @param String keynames instead of keywordName or engGroupName
	 * @param int tablePos instead of generalPos or closedPanePos
	 * @param int rowPos instead of rowKeyLabelPos or rowEngLabelPos
	 */
	void keywordMultiple(String[]keynames,int tablePos,int rowPos){
		// Procedure: Shuffling the number from 0 to 4
	    int[] chkNum = shuffledNum(keynames.length);
	    log("Test groups are: " + keynames[chkNum[0]] + ", " + keynames[chkNum[1]]);

	    // Procedure: Mark two keywords according to the shuffled number, then get texts from the list
    	String expected = "";
    	for(int i = 0; i < 2; i++){
    		popProc.clickItemAndWait(cmt, Z_TABLECHILD, zclassCheckImg,tablePos,chkNum[i],ENABLE);
    	    expected = expected + keynames[chkNum[i]];
    	    if(i != 1) expected = expected + ", ";
    	}
    	
	    // TEST1: verify the marked keywords 
	    log(TUT + "1: Verify the marked labels are shown in the left area");
    	doAssertThat(popProc.getText(cmt,Z_TABLECHILD,zclassRow,zclassLabel,tablePos,rowPos,keywordPos,KEYWPANEL),is(expected));

	    // Procedure: Do search and look into some entries if exist
	    popProc.closePopup(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DONE,CHECK_POPUP_SEARCH);
    	if(!baseProc.isNoEntry(cmt,Z_ENTRY_INFO,NO_ENTRY)){
	    	baseProc.clickEntry(cmt,Z_LISTITEM,Z_LISTCELL,rowFirst,0,"Details");
	    	String[] members = getRowInDetails("Keywords");

	    	// TEST2: Verify keywords are shown in details' area
	    	log(TUT + "2: Verify labels are shown in Details");
	    	for(int i = 0; i < 2; i++) doAssertThat(members[1].contains(keynames[chkNum[i]]),is(equalTo(true)));

	    	// Procedure: Need to refresh for out of focus from the clicked entry above
    		baseProc.doRefresh(cmt,Z_TOOLBAR_BTN,BASE_REFRESH);
	    }
	    	
	    // Procedure: Open search popup
	    baseProc.openPopup(cmt,Z_TOOLBAR_BTN,BASE_SEARCH,CHECK_POPUP_SEARCH);
	    	
	    // TEST3: Verify the marked keyword remains 
	    log(TUT + "3: Verify the marked lables are still shown in the left area");
    	doAssertThat(popProc.getText(cmt,Z_TABLECHILD,zclassRow,zclassLabel,tablePos,rowPos,keywordPos,KEYWPANEL),
    			   is(keynames[chkNum[0]] + ", " + keynames[chkNum[1]]));
	    	
	    // Procedure: Click the keyword again for unchecking 
    	for(int i = 0; i < 2; i++) popProc.clickItemAndWait(cmt, Z_TABLECHILD, zclassCheckImg, tablePos,chkNum[i],DISABLE);
    	
    	// TEST4: Verify keywords are not shown now
    	log(TUT + "4: Verify no labels are shown in the left area");
    	doAssertThat(popProc.getText(cmt,Z_TABLECHILD,zclassRow,zclassLabel,tablePos,rowPos,keywordPos,KEYWPANEL),is(""));
	    
	    // Procedure: Mark all keywords
    	expected  = "";
    	for(int i = 0; i < keynames.length; i++){
    		popProc.clickItemAndWait(cmt, Z_TABLECHILD, zclassCheckImg, tablePos,i,ENABLE);
    	    expected = expected + keynames[i];
    	    if(i != keynames.length-1) expected = expected + ", ";
    	}
   		
    	// TEST5: Verify all keywords are shown
	    log(TUT + "5: Verify all lables are shown in the left area");
    	doAssertThat(popProc.getText(cmt,Z_TABLECHILD,zclassRow,zclassLabel,tablePos,rowPos,keywordPos,KEYWPANEL),is(expected));

	    // Procedure: Load default values 
	    popProc.clickButton(cmt,Z_POPUP,Z_POPUP_BTN,SEARCH_DEFAULT);

	    // TEST6: Verify no keywords appeare
    	log(TUT + "6: Verify no labels are shown due to loading defaults");
    	doAssertThat(popProc.getText(cmt,Z_TABLECHILD,zclassRow,zclassLabel,tablePos,rowPos,keywordPos,KEYWPANEL),is(""));
	}

}