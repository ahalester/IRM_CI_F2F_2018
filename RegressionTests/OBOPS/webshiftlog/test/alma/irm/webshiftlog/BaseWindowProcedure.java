/*
 * NAME: BaseWindowProcedure.java
 * AUTHOR: Kyoko Nakamura
 * DATE: 2015-09-14
 * 
 * PURPOSE: 
 *  A set of common methods relate to the base pane.
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
 * 2017-07-13	knaka	Added new function waitUntilNewClassShown into sortList()
 * 2018-04-27	knaka	Changed arguments of sleep() to defined variable
 * ----------------------------------------------------------------
 */
package alma.irm.webshiftlog;

import java.nio.file.Path;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static alma.irm.webshiftlog.Definitions.*;


class BaseWindowProcedure{
	String thisClass = "BaseWindowProcedure";

	//--- System parameters ---
	private final String usernameId = "username";
	private final String passwordId = "password";
	private final String submitId = "submit";
	private final String loadMsg = "Searching...";
	
	/*
	 *  Constructer is not defined
	 */
	
	/*
	 * Open url and create an instance
	 */
	static BaseWindowProcedure open(WebDriver driver,final String url){
		driver.get(url);
		return new BaseWindowProcedure();
	}
	
	/*
	 * @brief Open url without getting an instance
	 * @param WebDriver driver
	 * @param int n: 0 as WebSLT, 1 as "anotherUrl"
	 */
	void openUrl(WebDriver driver,final String url){
		driver.get(url);
	}
	
	/*
	 * @brief Login with a valid user
	 * @param CommonTools cmt
	 * @param String title
	 */
	void login(CommonTools cmt,String title,final String user,final String pw){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		// Input user name and password
		cmt.inputClear(By.id(usernameId));
		cmt.inputText(By.id(usernameId),user);
	    cmt.inputText(By.id(passwordId),pw);

		// Click [LOGIN]
	    log(myInfo + "Click [login]");
	   	cmt.click(By.name(submitId));
	   	try{
	   		cmt.waitForTitleIsLoaded(title,10);					// Wait for loading the page
	   		// (2015-09-10) Added the following statement because getEntryMsg failed sometimes
	   		cmt.waitForTextIsUnloaded(loadMsg,5);				// Wait until "Searching..." disappears
	   	}catch(Exception e){
	   		saveLog(e);
	   	}
	  }
	
	/*
	 * @brief Login with an invalid user, who don't have any needed roles
	 * @param CommonTools cmt
	 * @param String title
	 */
	void loginWithNoAuth(CommonTools cmt,String title,final String user,final String pw){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		cmt.inputClear(By.id(usernameId));
		cmt.inputText(By.id(usernameId),user);
		cmt.inputText(By.id(passwordId),pw);

	    log(myInfo + "Click [login]");
	   	cmt.click(By.name(submitId));
	   	try{
	   		cmt.waitForTitleIsLoaded(title,10);					// Wait for loading the page
	   	}catch(Exception e){
	   		saveLog(e);
	   	}

	}

	/*
	 * @brief Logout in normal
	 * @param CommonTools cmt
	 * @param String cname
	 * @param int n
	 * @param String dialog
	 * @param int m
	 * @param String title
	 * @param String success
	 */
	void logout(CommonTools cmt,String cname,int n,String dialog,int m,String title,String success){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		
		// Check if the text "Log out" can see in toolbar
		try{
			cmt.waitForTextIsLoaded("Log out", 5);
		}catch(Exception e){
			saveLog(e);
		}
			
		// Click the Logout button and wait for showing the dialog box
		log(myInfo + "Click logout");
		cmt.click(By.className(cname), n);	
		try{
			cmt.waitForTextIsLoaded(title,10);	
		}catch(Exception e){
			saveLog(e);
		}
			
		// Do logging out by clicking the 1st button and wait for loading the logout page
		log(myInfo + "Click Yes");
		cmt.click(By.className(dialog), m);	
		try{
			cmt.waitForTextIsLoaded(success,10);
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Cancel logout
	 * @param CommonTools cmt
	 * @param String cname
	 * @param int n
	 * @param String dialog
	 * @param int m
	 * @param String title
	 */
	void logoutCancel(CommonTools cmt,String cname,int n,String dialog,int m,String title){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		// Check if the text "Log out" can see in toolbar
		try{
			cmt.waitForTextIsLoaded("Log out", 5);
		}catch(Exception e){
			saveLog(e);
		}

		// Click "Log out"
		log(myInfo + "Click [Log out]");
		cmt.click(By.className(cname), n);					// Click the 6th button
		try{
			cmt.waitForTextIsLoaded(title,10);	    			// Wait for a dialog box
		}catch(Exception e){
			saveLog(e);
		}

		// Do NOT logging out 
		log(myInfo + "Click [No]");
		cmt.click(By.className(dialog), m);					// Click the 2nd btn
		try{
			cmt.waitForTextIsUnloaded(title,10);	    		// Wait for unloading the dialog box
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Logout enforcedly when the browser shows "Access Denied" with an invalid user
	 * @param CommonTools cmt
	 * @param String success
	 */
	void logoutEnforced(CommonTools cmt,String success,final String url){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Navigate to the CAS logout");
		cmt.navegateNext(url);
		try{
			cmt.waitForTextIsLoaded(success,10);				// Wait for loading the page
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Save a surrent screen shot
	 * @param CommonTools cmt
	 * @param Path path
	 * @param filename
	 */
	void saveCurrentScreen(CommonTools cmt,Path path,String filename){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Save a current screenshot");
		try{
			cmt.getScreenshot(path,filename);
			log(myInfo +  "Got screenshot: [" + filename + "]");
		}catch(Exception e){
			saveLog(e);
			log(myInfo +  "Failed to get a screenshot of: [" + filename + "]");
		}
	}
	
	/*
	 * @brief Get a window handle name
	 * @param CommonTools cmt
	 */
	String getNewWindowHandle(CommonTools cmt,String currentHandle){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		String newHandle = "";
		log(myInfo + "Get a new window handle");
		log(myInfo + "Current handle name: [" + currentHandle + "]");
		newHandle = cmt.getWinId(currentHandle);
		log(myInfo + "New handle name: [" + newHandle + "]");
		
		return newHandle;
	}
	
	/*
	 * @brief Go to a new window
	 * @param CommonTools cmt
	 * @param String winHandle
	 * @param Strin expectedTitle which is expected on a new window
	 */
	void goToAnotherWindow(CommonTools cmt,String winHandle,String expectedTitle){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Go to a new window by a window handle");
		cmt.switchToWindow(winHandle);
		try{
			cmt.waitForTitleIsLoaded(expectedTitle, 10);
	   	}catch(Exception e){
	   		saveLog(e);
	   	}

	}
	
	/*
	 * @brief Go to new window 
	 * @param CommonTools cmt
	 * @param String winId
	 * @param String text
	 */
	void goToAnotherWindowByText(CommonTools cmt,String winId,String text){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Switch to another window");
		// Change focus to the new window
		cmt.switchToWindow(winId);
		
		// Wait until the expected text is loaded
		try{
			cmt.waitForTextIsLoaded(text, 5);
		}catch(Exception e){
			saveLog(e);
		}
	}	

	/*
	 * @brief Go to another web page
	 * @param CommonTools cmt
	 * @param WebDriver driver
	 */
	void goToAnotherWeb(CommonTools cmt,WebDriver driver,final String url,final String title){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Navigate to: [" + url + "]");
		openUrl(driver,url);
		
		// Wait for the title is loaded
		try{
			cmt.waitForTextIsLoaded(title, 10);
		}catch(Exception e){
			saveLog(e);
		}

		return;
	}
	
	/*
	 * @brief Open Search popup
	 * @param CommonTools cmt
	 * @param String cname
	 * @param int n
	 * @param String text
	 */
	void openPopup(CommonTools cmt,String cname,int n,String text){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		// Check if the text "Log out" can see in toolbar
		try{
			cmt.waitForTextIsLoaded("Log out", 5);
		}catch(Exception e){
			saveLog(e);
		}
		
		// Click Search button
		String request = n == 0? "Search" : "DoReport";		// 0th button is "Search", 2th is "DoReport"
		log(myInfo + "Click [" + request + "] button in toolbar");
		cmt.click(By.className(cname), n);	// Click a button

		try{
			// Wait for loading the popup window, which is checked that the text appeared
			cmt.waitForTextIsLoaded(text,10);	
		}catch(Exception e){
			saveLog(e);
		}

	}

	/*
	 * @brief Go to the status page by changing the url
	 * @param CommonTools cmt
	 * @param String title
	 */
	void goToStatusPage(CommonTools cmt,String title,final String url){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Navigate to the status url");
		cmt.navegateNext(url);
		try{
			cmt.waitForTextIsLoaded(title,10);					// Wait for loading the page
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Back to the previous page after executing "goToStatusPage().
	 * @param CommonTools cmt
	 * @param String title
	 */
	void backToPreviousPage(CommonTools cmt,String title){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Navigate back to the WebSLT");
		cmt.navigateBack();
		try{
			cmt.waitForTextIsLoaded(title,10);				// Wait for loading the page
		}catch(Exception e){
			saveLog(e);
		}
		log(myInfo + "...Just sleep for " + HALFSEC + "./1000.s...");
		cmt.sleep(HALFSEC);			// Need this sleep for finishing the test successfully!!!
	}
	
	/*
	 * @brief Go to the Alma Portal page
	 * @param CommonTools cmt
	 * @param String cname
	 * @param int n
	 * @param String title
	 */
	void goToAlmaPage(CommonTools cmt,String cname,int n,String title){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Click [Alma Portal]");
		cmt.click(By.className(cname), n);					// Click the 6th button
		try{
			cmt.waitForTextIsLoaded(title,10);	    		// Wait for showing 
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Show the search help page in another window
	 * @param CommonTools cmt
	 * @param String cname
	 * @param int n
	 */
	void showHelpPage(CommonTools cmt,String cname,int n){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Click [About Search]");
		cmt.click(By.className(cname), n);

		try{
			cmt.waitUntilNewWindowShown(5);					// Wait for showing
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Show the search help page in another window
	 * @param CommonTools cmt
	 * @param String cname
	 * @param int n
	 */
	void showWebAqua(CommonTools cmt,String linkname){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Click [ln AQUA]");
		cmt.click(By.linkText(linkname));

		try{
			cmt.waitUntilNewWindowShown(10);				// Wait for showing
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Do Refresh
	 * @param CommonTools cmt
	 * @param String cname
	 * @param int n
	 */
	void doRefresh(CommonTools cmt, String cname, int n){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Click [Refresh]");
		cmt.click(By.className(cname),n);
		try{
			// Wait until "Searching..." disappears
			cmt.waitForTextIsUnloaded(loadMsg,5);
			log(myInfo + "\"Searching\" has finished just now after refreshing!");
		}catch(Exception e){
			saveLog(e);
		}
	}
	
	/*
	 * @brief Change the list order by clicking the header. The first click changes in ascending order,
	 * 		  and the second click does in descending order.
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int n
	 */
	void sortList(CommonTools cmt,String cnameP,String cnameC,String cnameCaret,int n){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		// Before clicking, check the element is displayed or not
		// Note that waitForToLoad() with a header name like "Timestamp" is not available here,
		// because the header is already loaded, thus, the wait is in vain.       
		while(!cmt.checkDisplay(By.className(cnameP),By.className(cnameC),0,n)) ;
		
		log(myInfo + "Click header in the list for sorting");
		cmt.click(By.className(cnameP),By.className(cnameC),0,n);
			
		// 17-07-13 Added next function, which waits "sorted" arrow appears in the header  
		try{
			// Wait until "arrow" appears
			cmt.waitUntilNewClassShown(By.className(cnameCaret),5);
			log(myInfo + "Sorting was finished.");
		}catch(Exception e){
			saveLog(e);
		}
		
		// TODO(03-30) Need to "waitFor...", but how? So, put sleep instead.
		// 17-06-05 Changed from 300 to 350 because #010 often failed.
		// 17-07-13 Even if the above wait is added.this sleep is still needed (I'm not sure why).
		// 17-08-02 Changed from 350 to 400 because #010 often failed.
		// 18-04-27 Changed to 500 finally
		log(myInfo + "...Just sleep for " + HALFSEC + "350/1000s...");
		cmt.sleep(HALFSEC);			
	}

	/*
	 * @brief Get a timestamp of the first column
	 * @param CommonTools cmt 
	 * @param String tsText as timestamp text from a first column
	 * @param String typeText as type text from a first column
	 * @param int n as 0 or 1, 0 means getting the start timestamp, and 1 for the end timestamp
	 */
	String getBeginOrEndTS(CommonTools cmt,String tsText,String typeText, int n){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Get the begin/end timestamp on the entry list");
		String ts = "";
		ts = cmt.getTs(tsText,typeText,n);
		log(myInfo + "Got: [" + ts + "]");
		
		return ts;
	}
	
	/*
	 * @brief Click an entry in order to show its details
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 * @param String text
	 */
	void clickEntry(CommonTools cmt,String cnameP, String cnameC, int nP, int nC, String text){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Click an entry list to show its detail information");
		cmt.click(By.className(cnameP),By.className(cnameC),nP,nC);
		// Wait until "Searching..." disappears
		try{
			cmt.waitForTextIsUnloaded(loadMsg,5);
			log(myInfo + "\"Searching\" has finished just now after entry clicking!");

			cmt.waitForTextIsLoaded(text,5);				// Wait for loading the information which includes str
			log(myInfo + "The expected text [" + text + "] is shown now");
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Click an entry and wait until the new entry is shown, 
	 *        this is needed to check the detail information one by one
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 * @param String expected
	 * @param String unexpected
	 */
	void clickEntryCheckingId(CommonTools cmt,String cnameP,String cnameC,int nP,int nC,String expected,String unexpected){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Click an entry list and wait till it's shown actually");
		cmt.click(By.className(cnameP),By.className(cnameC),nP,nC);
		try{
			// Wait until the unexpected text disappears
			if(unexpected != null){
				cmt.waitForTextIsUnloaded(unexpected,5);
				log(myInfo + "Unexpected text [" + unexpected + "] disappeared");
			}
			
			// Wait until the expected text appears
			cmt.waitForTextIsLoaded(expected,5);				// Wait for loading the information which includes str
			log(myInfo + "The expected text [" + expected + "] is shown now");
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Click an entry in order to show its details
	 * @param CommonTools cmt
	 * @param String cname
	 * @param int n1
	 * @param int n2
	 */
	void clickTwoEntries(CommonTools cmt,String cname,int n1, int n2){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Click two entries in the list");
		try{
			cmt.clickOnTwoItems(By.className(cname),n1,n2);			// Actually click on two entries 
		}catch(Exception e){
			saveLog(e);
		}
		log(myInfo + "Two entries are focused");
	}
	
	/*
	 * @brief Back from the switched window to the original window
	 * @param CommonTools cmt
	 * @param String winId
	 * @param String text
	 */
	void backFromNewWindow(CommonTools cmt,String winId,String text){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Switch back to the original window");
		// Change focus to the new window
		cmt.closeWindow();				// CLose the new window
		cmt.switchToWindow(winId);				// Switch back to the original window
		
		// Wait until the expected text is loaded
		try{
			cmt.waitForTextIsLoaded(text, 5);	
		}catch(Exception e){
			saveLog(e);
		}
	}
	
	/*
	 * @Brief Check the text is shown or not. If it's shown, returns "true".
	 * @param CommonTools cmt
	 * @param String text
	 */
	Boolean checkText(CommonTools cmt, String text){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Check if the input text [" + text + "] is shown");
		Boolean result = cmt.isTextPresent(text);
		String judge = (result == true)? "SHOWN" : "NOT SHOWN";
		log(myInfo + "Above text is " + judge);
		return result;
	}
	
	/*
	 * @brief Get Entry summary message
	 * @param CommonTools cmt
	 * @param String cname
	 */
	String getEntryMsg(CommonTools cmt,String cname){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Get Entry summary message");
		String message = "";
		message = cmt.getText(By.className(cname));
		
		log(myInfo + "Got: [" + message + "]");
		return message;
	}

	/*
	 * @brief Check if there is any entries or not: true means no entry, and false means found some entries 
	 * @param CommonTools cmt
	 * @param String cname
	 * @param String text as no entry message
	 */
	Boolean isNoEntry(CommonTools cmt,String cname,String text){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		Boolean judge = false;
		String message = getEntryMsg(cmt,cname);
		
		log(myInfo + "Is there any entries?: ");
		judge = cmt.chkMsg(message, text);		// "true" means no entry is gotten
		String str = (judge == true)? "No" : "Yes";
		log(str);
		
		return judge;
	}
	
	/*
	 * @brief Get total number of Entry summary
	 * @param CommonTools cmt
	 * @param String cname
	 */
	int getTotalNumberOfEntry(CommonTools cmt,String cname){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		int total = 0;
		String message = getEntryMsg(cmt,cname);	// Get message again in order to get numbers of entries

		log(myInfo + "How many entries?: ");
		total = cmt.getEntryTotal(message);
		log(String.valueOf(total));
		
		return total;
	}
	
	/*
	 * @brief Get total number of files
	 * @param CommonTools cmt
	 * @param String directoryName
	 */
	int getTotalNumberOfFiles(CommonTools cmt,String directoryName){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		int total = 0;
		log(myInfo + "How many files?: ");
		total = cmt.getTotalFile(directoryName);
		log(String.valueOf(total));
		
		return total;
		
	}
	
	/*
	 * @brief Get a text from a column area in the entry list
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 * @param String columnName
	 */
	String getColumn(CommonTools cmt,String cnameP, String cnameC,int nP,int nC,String columnName){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		String text = "";
		log(myInfo + "Get a string from [" + columnName + "(" + nP + "th)]");
		text = cmt.getText(By.className(cnameP), By.className(cnameC),nP,nC);
		log(myInfo + "Got: [" + text + "]");
		return text;
	}
	
	/*
	 * @brief Get information in Details' pane
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC, which points row(z-row),special label(z-label) or all area(z-panelchildren)
	 *        In case of all area or the first row, nP = nC = 0.
	 * @param int nP
	 * @param int nC, which points the position of the individual information in case of cnameC != "z_panelchildren"
	 */
	String getDetails(CommonTools cmt,String cnameP, String cnameC,int nP,int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		String text = "";
		log(myInfo + "Get information from Details");
		text = cmt.getText(By.className(cnameP), By.className(cnameC),nP,nC);
		if(cnameC.equals("z-panelchildren")) log(myInfo + "Got everything");
		else 		           				 log(myInfo + "Got: [" + text + "]");
		return text;
	}

	/*
	 * @brief Get the number of rows 
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 */
	int getNumbersOfRow(CommonTools cmt,String cnameP, String cnameC,int nP){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Get the number of web elements: ");
		int totalNumber;
		totalNumber = cmt.getWebElementsNumber(By.className(cnameP), By.className(cnameC),nP);
		log(String.valueOf(totalNumber));

		return totalNumber;	 
	}
	
	/*
	 * @brief Get one row in Details 
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 */
	String[] getDetailsByRow(CommonTools cmt,String cnameP, String cnameC,int nP,int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		String[] strs = new String[] {"",""};
		String buf = "";

		log(myInfo + "Get information with each row in Details");
		buf = cmt.getText(By.className(cnameP), By.className(cnameC),nP,nC);
		if(buf.contains("\n")) strs = buf.split("\n");	// Split with label and information 
		else strs[0] = buf;								// In case of no elements, just save the message
		
		log(myInfo + "label: [" + strs[0] + "], info: [" + strs[1] + "]");
		return strs;
	}
	
	/*
	 * @brief Get <img src="...">
	 * @param CommonTools cmt
	 * @param String xname (Note that "table/tbody/tr[2]/td/img" to "/table/tbody/tr[7]/td/img" 
	 */
	String getImgText(CommonTools cmt,String xname){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		String text = "";
		log(myInfo + "Get src img");
		text = cmt.getImgSrc(By.xpath(xname));
		log(myInfo + "Got: [" + text + "]");
		return text;
	}

	/*
	 * @brief Show the popup menu
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 */
	void showPopupMenu(CommonTools cmt,String cnameP, String cnameC,int nP, int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Show the popup menu of reports");
		cmt.rightClick(By.className(cnameP),By.className(cnameC),nP,nC);
		try{
			cmt.waitForTextIsLoaded("Produce PDF report", 10);
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Show the second popup menu by dragging on the first popup
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 * @param String text for waiting to load
	 */
	void showSecondPopupMenu(CommonTools cmt,String cnameP, String cnameC,int nP, int nC, String text){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Show the second popup menu of reports");
		// 2015-11-13 ZK ver.6.5.2 needed to hover for showing the second popup. It,however,didn't work on ZK ver.7.
		//  ZK7 can open it by clicking.
		cmt.click(By.className(cnameP),By.className(cnameC),nP,nC);
//		cmt.dragAndDrop(By.className(cnameP),By.className(cnameC),nP,nC);
		try{
			cmt.waitForTextIsLoaded(text, 10);
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Check the menu item is selectable or not
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 * @param String atrName as an attribute name want to get
	 */
	Boolean isMenuSelectable(CommonTools cmt,String cnameP, String cnameC,int nP, int nC, String atrName){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Check the menu item is selectable or not");
		Boolean judge = false;
		String atrText = cmt.getAttributeByName(By.className(cnameP),By.className(cnameC),nP,nC,atrName);
		// 2015-11-12 The string "-disd" has been changed to "-disabled" from ZK ver.7
		if(!atrText.contains("-disabled")) judge = true;
		log(myInfo + "the menu item is " + ((judge == true)? "SELECTABLE":"DISABLE"));
		
		return judge;
	}

	/*
	 * @brief Click "a href" for creating a report
	 */
	void createReport(CommonTools cmt,String cnameP, String cssname,int nP,int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Select a report on the entry list");
	    cmt.hrefClick(By.className(cnameP),By.cssSelector(cssname),nP,nC);	

	    // Wait for the new window is appeared, because the report is shown in the new window
	    try{
	    	cmt.waitUntilNewWindowShown(10);
		}catch(Exception e){
			saveLog(e);
		}
	}

}
