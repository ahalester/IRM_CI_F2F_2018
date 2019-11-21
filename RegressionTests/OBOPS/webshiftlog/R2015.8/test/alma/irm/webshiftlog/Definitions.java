package alma.irm.webshiftlog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.hamcrest.Matcher;
import static org.junit.Assert.assertThat;	

/*
 * Common defined parameters for this package
 * Note that this class is imported by Suite/Procedure/Tools classes and names are used directly.
 */
public class Definitions {
	// Date format
	static final String FORMAT_DATE = "yyyy-MM-dd'T'HH:mm:ss";	// Format of Date
	static final String FORMAT_SEARCH = "yyyy-MM-dd hh:mm:ss";	// Format of Interval Begin/End on Search popup, 15-11-12 updated
//	static final String FORMAT_SEARCH = "yyyy/MM/dd hh:mm:ss a";// Format of Interval Begin/End on Search popup
	static final String FORMAT_REPORT = "yyyy/MM/dd HH:mm:ss";	// Format of Interval Begin/End on Report popup
	
	// Log messages
	static final String TU = "### TestCase ### ";				// Put at starting of a test case
	static final String TUT = "# Test";							// Put at assertion
	static final String NOTEST = "Cannot test due to miss to get a text";	// In case getText() failed, put this text and skip the test
	static final String STATUSPAGE = "Status Page";				// Written in getText()
	static final String DETAILAREA = "Details Area";			// Written in getText()
	static final String INTERVALMENU = "Interval";				// Written in getText()
	static final String WARNDIALOG = "Warning dialog";			// Written in getText()
	static final String SHIFTMENU = "Shift Activity";			// Written in getText()
	static final String OBSCYCLEMENU = "Observing Cycle";		// Written in getText()
	static final String KEYWPANEL = "Keyword panel";			// Written in getText() 
	static final String REPPOPUP = "Report popup";				// Written in getText()
	
	// Titles and defined messages
	static final String WEBSLT = "WebSLT";
	static final String WEBTITLE = "ALMA WebShiftlog Tool";		// SLT title
	static final String LOGOUT_DONE = "Logout successful";		// Logout success
	static final String LOGOUT_DIALOG = "Logout confirmation";	// Dialog box title at logging out
	static final String NO_ENTRY = "No entries found, try another search criteria";	// No entries
	static final String SAVECURRENT_DIALOG = "Criteria saved";	// Dialog box title at clicking [Save current]
	static final String DETAIL_HEADER = "Details for entry with ID=";	// Header message in Details	
	
	// Texts in order to confirm their popups are opened
	static final String CHECK_POPUP_SEARCH = "Load Defaults";	// To confirm Search popup
	static final String CHECK_POPUP_REP = "Report details";		// To confirm Report popu

	// ZK Locators on Base
	static final String Z_TOOLBAR_BTN = "z-toolbarbutton";		// Tool bar in WebSLT view
	static final String Z_DIALOG_BTN = "z-messagebox-button";	// Dialog box for logging out, 15-11-11 updated
	static final String Z_ENTRY_INFO = "entries-summary";		// Searched summary in WebSLT view
	static final String Z_POPUP = "z-window-highlighted";		// Popup window by search/Do report
	static final String Z_POPUP_BTN = "z-button";				// Buttons in Popup window, 15-11-11 updated

	// ZK Locators of Entry list on Base 
	static final String Z_PANELCHILD = "z-panelchildren";		// View under the menu includes like Logout
	static final String Z_LISTHEAD = "z-listheader-sort";		// Header of the list, 15-11-11 updated
	static final String Z_LISTITEM = "z-listitem";				// one row of the entry list
	static final String Z_LISTCELL = "z-listcell";				// one column of the entry list

	// ZK Locators of Details on Base
	static final String Z_DETAILS = "z-center";					// Area of details under Entry List
	static final String Z_HEADER = "z-panel-header";			// Header in details area
	static final String Z_ROW = "z-row";						// Rows in details area
	
	// ZK locators of Interval/Types on the search popup
	static final String Z_TABLECHILD = "z-tablechildren";		// Table elements: 0th to 11th (12 panes)
	static final String Z_SELECT = "z-select";					// Interval selection: only one (Use 0th position only)
	static final String Z_TYPETEXT = "*//input[@type='text']";	// For text fields
	static final String Z_TYPECHKBOX = "*//input[@type='checkbox']";// For checking in "Types"

	// Button positions
	// [WebSLT base window]
	static final int BASE_SEARCH = 0;							// 1st button is "Search"
	static final int BASE_REFRESH = 1;							// 2nd button is "Refresh"
	static final int BASE_REPORT = 2;							// 3rd button is "Do report"
	static final int BASE_HELP = 3;								// 4th button is "About Search..."
	static final int BASE_ALMAPORTAL = 4;						// 5th button is "Alma Portal"
	static final int BASE_LOGOUT = 5;							// 6th button is "Logout"
	// [Dialog box for logging out]
	static final int LOGOUT_YES = 0;							// 1st button is "YES" in Dialog box
	static final int LOGOUT_NO = 1;								// 2nd button is "NO" in Dialog box
	// [Search criteria popup window]
	static final int SEARCH_DEFAULT = 0;						// 1st is "Load Defaults" in Search popup
	static final int SEARCH_SAVE = 1;							// 2nd is "Save current" in Search popup			
	static final int SEARCH_CANCEL = 2;							// 3rd is "Cancel" in Search popup
	static final int SEARCH_DONE = 3;							// 4th is "Search" in Search popup
	// [Report details popup window]
	static final int REPORT_CANCEL = 0;							// 1st is "Cancel" in Report popup
	static final int REPORT_DONE = 1;							// 2nd is "Generate Report" in Report popup

	// Column positions in Entry list
	static final String[] CLMS = new String[]{					// All columns on Entries
		"Type"  ,"Timestamp","Location","Project code","SchedBlock","ExecBlock",
		"Status","QA0"      ,"Subject" ,"Author"      ,"Attachment","Calibration flag"};
	static final int CLM_TYPE = 0;								// Column: Type
	static final int CLM_TS = CLM_TYPE + 1;						// Column: Timestamp
	static final int CLM_LOC = CLM_TS + 1;						// Column: Location
	static final int CLM_PCODE = CLM_LOC + 1;					// Column: Project code
	static final int CLM_SB = CLM_PCODE + 1;					// Column: SchedBlock 
	static final int CLM_EB = CLM_SB + 1;						// Column: ExecBlock
	static final int CLM_STATUS = CLM_EB + 1;					// Column: Status
	static final int CLM_QA0 = CLM_STATUS + 1;					// Column: QA0
	static final int CLM_SUBJECT = CLM_QA0 + 1;					// Column: Subject
	static final int CLM_AUTHOR = CLM_SUBJECT + 1;				// Column: Author
	static final int CLM_ATTACH = CLM_AUTHOR + 1;				// Column: attachment
	static final int CLM_CALIB = CLM_ATTACH + 1;				// Column: calibration
	
	// Position of classes of "tablechildren" in the search popup
	static final int INTERVAL = 1;								// Interval selection box
	static final int MAX_ENTRIES = 3;							// Max entries value
	static final int BEGIN = 5;									// Begin timestamp
	static final int END = 8;									// End timestamp
	static final int GENERAL = 10;								// General pane
	static final int CLOSED_PANE = 11;							// Pane of Execution Information and Engineering
	static final int EXECINF = 0;								// Execution Information in tablechildren=11
	static final int ENGINEER = 1;								// Engineering in tablechildren=11

	// Types name on search criteria
	static final String[] TYPE_NAME = new String[]{
			"ANTEN","AOG","ARRAY",  "CMDLN","DOWN","ENGIN",
			"ENTRY","MMEX","SBEX",  "SHIFT","WEATH"};
	static final int TYPES_ARRAY = 2;							// ARRAY position in types
	static final int TYPES_SHIFT = 9;							// SHIFT position in types
	static final int TYPES_WEATH = 10;							// WEATH position in types
	
	// Link text of invert types in Search pane
	static final String TYPE_INVERT = "Invert Selection";		// Name of the link text in Type on Search 

	// Flags
	static final int ENABLE = 1;								// Enablement
	static final int DISABLE = 0;								// Disablement
	static final int SELECT = 1;								// Selected
	static final int UNSELECT = 0;								// Unselected
	
	/*
	 * USER DEFINED PARAMETERS for the meaningful tests
	 * These are set for the test in ACA4 STE originally.
	 * Please edit them according to your environment.
	 */
	//---  from HERE ---
	//[URLs]
	static final String BASEURL = "http://support:10080/webshiftlog/"; 	// Identical to the one in BaseWindowProcedure
	static final String STATUSURL = BASEURL + "status.zul";
	static final String ENFORCED_LOGOUT_URL = "https://support.aca4.ste:10443/cas/logout";
	//[Accounts]
	static final String USERNAME = "";
	static final String PASSWORD = "";
	static final String USERNAME_WO_AUTH = "";
	static final String PASSWORD_WO_AUTH = "";

	// (2015-09-01) It's strange but the navigate back doesn't work with "Tokyo Amesh"
	static final String SAMPLEURL = "http://www.bbc.com/weather/";
	static final String SAMPLEURL_TITLE = "BBC Weather";

	// [Search criteria]
	static final String OTHER_BEGIN = "2014-02-01 00:00:00";	// Begin in Interval "Other", 15-11-12 updated
	static final String OTHER_END = "2014-08-31 23:59:00";		// End in Interval "Other", 15-11-12 updated
	static final String[] PARAM_GENERAL = new String[]{			// Texts in General, put ONE word only!!!
										"almaproc",				// "Author"
										"test",					// "Subject"
										"ACA4", 				// "Location"
										"deleted"				// "Comment"
										};
	static final String[] PARAM_EXECINF = new String[]{
										"9999.4",				// "Project Code"
										"aca-fx",				// "Project Name"
										"almaproc",				// "Project PI"
										"11",					// "Project Version"
										"b0537",				// "SchedBlock name"
										"X11",					// "SchedBlock UID"
										"X1",					// "ExecBlock UID"
										"Array001",				// "Array name"
										"CM01",					// "Antenna name"
										"PhotonicReference2",	// "Photonic Reference"
										"CL",					// "Executive(s)"
										};
	// [Report details]
	static final String OTHER_BEGINREP = "2014/08/01 00:00:00";	// Begin in Interval "Other" in Report popup
	static final String OTHER_ENDREP = "2014/08/31 23:59:59";	// End in Interval "Other" in Report popup
	static final String PRJ_CODE = "9999.4.00001.CSV";			// Project Code for Project report
	static final String MY_SITE = "OTHER";						// Site name of yours from AOS,OSF or OTHER
	static final String MY_LOCATION = "TST-ACA4";				// Location name of yours
	//--- to HERE ---

	/*----------------------------------------------------------------------------
	 * 	 Definition for logging a text into a log file
	 *----------------------------------------------------------------------------*/
	static FileWriter fw = null;
	static BufferedWriter bw = null;
	static String LOGNAME = "";				// This is set by setUp() in each Suite class 

	static String now(){
		Calendar c = Calendar.getInstance();					// Get a calendar by the default locale and timezone
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);	// Set the defined format
		return sdf.format(c.getTime());							// Get current date with the define format
	}

	static void log(String text){
		String line = now();									// Put current time
		line = line.concat(" ").concat(text).concat("\n");		// And then put log messages with a return code
		
		try{
			fw = new FileWriter(LOGNAME,true);					// Open file descriptor with "append" mode 
			bw = new BufferedWriter(fw);						// Open buffering handler
			bw.write(line);										// Write the log into the log file
			bw.flush();											// Flush
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				bw.close();
			}catch(IOException e2){
				e2.printStackTrace();
			}
		}
	}

	static void saveLog(Exception e){
		log("EXCEPTION occurred! " + e);
		StackTraceElement[] ste = e.getStackTrace();
		for(int i = 0; i < ste.length; i++ ){
			if(ste[i].getClassName().contains("webshiftlog"))
				log(" " + ste[i].getClassName() + "#" + ste[i].getMethodName() + " line(" + String.valueOf(ste[i].getLineNumber()) + ")");
			if(ste[i].getClassName().contains("junit")) break;
		}
	}
	
	/*----------------------------------------------------------------------------
	 * 	 Definition for Assertion
	 *----------------------------------------------------------------------------*/
	static int totalAssert;										// Total count of Assertion for each test case
	static int ngAssert;										// NG count of Assertion for each test case

	// Assertion with <String>
	static void doAssertThat(String actual,Matcher<String> expected){
		try{
			totalAssert++;										// Increment the total count of tests
			assertThat(actual,expected);
		}catch(AssertionError e){
			ngAssert++;											// Increment the failure count

			// Print the failure information
			StackTraceElement[] ste = new Throwable().getStackTrace();
			log("# TEST FAILED");
			for(int i = 0; i < 3; i++)
				log(ste[i].getClassName() + "#" + ste[i].getMethodName() + " line(" + String.valueOf(ste[i].getLineNumber()) + ")");
			log(e.getMessage());
		}
	}

	// Assertion with <Boolean>
	static void doAssertThat(Boolean actual,Matcher<Boolean> expected){
		try{
			totalAssert++;										// Increment the total count of tests
			assertThat(actual,expected);
		}catch(AssertionError e){
			ngAssert++;											// Increment the failure count

			// Print the failure information
			StackTraceElement[] ste = new Throwable().getStackTrace();
			log("# Test failed!!");
			for(int i = 0; i < 3; i++)
				log(ste[i].getClassName() + "#" + ste[i].getMethodName() + " line(" + String.valueOf(ste[i].getLineNumber()) + ")");
			log(e.getMessage());
		}
	}
	
	// Assertion with <Integer>
	static void doAssertThat(int actual,Matcher<Integer> expected){
		try{
			totalAssert++;										// Increment the total count of tests
			assertThat(actual,expected);
		}catch(AssertionError e){
			ngAssert++;											// Increment the failure count

			// Print the failure information
			StackTraceElement[] ste = new Throwable().getStackTrace();
			log("# Test failed!!");
			for(int i = 0; i < 3; i++)
				log(ste[i].getClassName() + "#" + ste[i].getMethodName() + " line(" + String.valueOf(ste[i].getLineNumber()) + ")");
			log(e.getMessage());
		}
	}
	
	// Assertion with <Integer[]>
	static void doAssertThat(int[] actual,Matcher<int[]> expected){
		try{
			totalAssert++;										// Increment the total count of tests
			assertThat(actual,expected);
		}catch(AssertionError e){
			ngAssert++;											// Increment the failure count

			// Print the failure information
			StackTraceElement[] ste = new Throwable().getStackTrace();
			log("# Test failed!!");
			for(int i = 0; i < 3; i++)
				log(ste[i].getClassName() + "#" + ste[i].getMethodName() + " line(" + String.valueOf(ste[i].getLineNumber()) + ")");
			log(e.getMessage());
		}
	}
	

}
