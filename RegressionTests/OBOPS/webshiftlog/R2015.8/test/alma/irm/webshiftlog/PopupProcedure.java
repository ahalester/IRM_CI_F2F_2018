package alma.irm.webshiftlog;

import java.text.ParseException;
import org.openqa.selenium.By;

import static alma.irm.webshiftlog.Definitions.*;

class PopupProcedure {
	String thisClass = "PopupProcedure";
	String loadMsg = "Searching...";
	
	// Constructer
	// There is not a defined constructer
	
	/*
	 * @brief Close Search popup for Search or Cancel
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int n, which is a button position
	 * @param String text
	 */
	void closePopup(CommonTools cmt,String cnameP, String cnameC,int n,String text){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		// Check the button position means "Search" or "Cancel"
		String buttonLabel = n == 3? "Search" : "Cancel";
		log(myInfo + "Click [" + buttonLabel + "] button in popup window");
	    cmt.click(By.className(cnameP), By.className(cnameC),0,n);

	    try{
		    // Wait for unloading the popup
	    	cmt.waitForTextIsUnloaded(text,10);
		}catch(Exception e){
			saveLog(e);
		}

		// Wait until "Searching..." disappears 
    	if(n == 3){		// Check only at clicking [Search], not [Cancel]
    		try{
	    		cmt.waitForTextIsUnloaded(loadMsg,5);
	    		log(myInfo + "\"Searching\" has finished just now!");
    		}catch(Exception e){
    			saveLog(e);
    		}
    	}
	}

	/*
	 * @brief Close a warning popup
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param String text
	 */
	void closeWarnPopup(CommonTools cmt,String cnameP, String cnameC, String text){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Click close button in a warning popup");
	    cmt.click(By.className(cnameP), By.className(cnameC),0,0);
	    try{
	    	cmt.waitForTextIsUnloaded(text,10);
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Click [Load Defaults]/[Save current] in search popup
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int n
	 */
	void clickButton(CommonTools cmt,String cnameP, String cnameC,int n){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Click a button in search popup window");
		cmt.click(By.className(cnameP), By.className(cnameC),0,n);

	    // TODO(03-30) Need to check "waitFor...", but how ??
		log(myInfo + "...Just sleep for 300./1000.s...");
	    cmt.sleep(300);								
	}

	/*
	 * @brief Get current date and time 
	 * @param CommonTools cmt
	 * @param String dateFormat
	 */
	String getCurrentDate(CommonTools cmt,String dateFormat){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Get current date");
		String dateStr = "";
		dateStr = cmt.getDate(dateFormat,0);
		log(myInfo + "[" + dateStr + "]");
		return dateStr;
	}
	
	/*
	 * @brief Get current date and time 
	 * @param CommonTools cmt
	 * @param String dateFormat
	 * @param int seconds
	 */
	String getNearDate(CommonTools cmt,String dateFormat,int seconds){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Get near date from the current");
		String dateStr = "";
		dateStr = cmt.getDate(dateFormat,seconds);
		log(myInfo + "[" + dateStr + "]");
		return dateStr;
	}
	
	/*
	 * @param Get date from the UNIX time
	 * @param CommonTools cmt
	 * @param String dateFormat
	 * @param String unixTime
	 */
	String getUnixTimeDate(CommonTools cmt,String dateFormat,String unixTime){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Get date from a UNIX time");
		String dateStr = "";
		dateStr = cmt.getDateFromUnixTime(dateFormat,unixTime);
		log(myInfo + "[" + dateStr + "]");
		return dateStr;
	}
	
	/*
	 * @brief put date
	 * @param ommonTools cmt
	 * @param String cname
	 * @param String xname
	 * @param int nP
	 * @param String date
	 */
	void putDate(CommonTools cmt,String cname,String xname,int nP,String date){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Input Timestamp in Interval");
		cmt.inputClear(By.className(cname), By.xpath(xname),nP,0);
		cmt.inputText(By.className(cname), By.xpath(xname),nP,0,date);
	    log(myInfo + "Put: " + cmt.getTextValue(By.className(cname),By.xpath(xname),nP,0));
	}

	/*
	 * @brief put date on DoReport popup
	 * @param ommonTools cmt
	 * @param String cname
	 * @param String xname
	 * @param int nP
	 * @param int nC
	 * @param String date
	 */
	void putDateOnReport(CommonTools cmt,String cname,String xname,int nP,int nC,String date){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Input Timestamp in Interval with two locators");
		cmt.inputClear(By.className(cname), By.xpath(xname), nP, nC);
		cmt.inputText(By.className(cname), By.xpath(xname), nP, nC ,date);
	    log(myInfo + "Put: " + cmt.getTextValue(By.className(cname),By.xpath(xname),nP,nC));
	}

	/*
	 * @brief Get a formatted timestamp
	 * @param CommonTools cmt
	 * @param String inTs
	 * @param String preFormat
	 * @param String postFormat
	 */
	String getFormatTS(CommonTools cmt,String inTs,String preFormat,String postFormat){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Change format of the timestamp From [" + inTs + "]");
		String outTs = "";
		try{
			outTs = cmt.formatTS(inTs,preFormat,postFormat);
			}catch(ParseException e){
				StackTraceElement[] ste = new Throwable().getStackTrace();
				log("# Test failed!!");
				for(int i = 0; i < 3; i++)
					log(ste[i].getClassName() + "#" + ste[i].getMethodName() + " line(" + String.valueOf(ste[i].getLineNumber()) + ")");
				log(e.getMessage());
		}
		log(myInfo + "To [" + outTs + "]");
		
		return outTs;
	}
	
	/*
	 * @brief get date with a format, which include AM or PM
	 * @param CommonTools cmt
	 * @param String cname
	 * @param String xname
	 * @param int nP
	 */
	String getDateWithFormat(CommonTools cmt,String cname,String xname,int nP,String preFormat,String postFormat){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		String formattedDate = "";
		String dateText = cmt.getTextValue(By.className(cname),By.xpath(xname),nP,0);
		log(myInfo + "Got the shown timestamp: [" + dateText + "]");
		
		formattedDate = getFormatTS(cmt,dateText,preFormat,postFormat);
		return formattedDate;
	}
	
	/*
	 * @brief Get date with a format, which does not include AM or PM. 
	 * 		  Note that this kind of format is used at [Report details] popup
	 * @param CommonTools cmt
	 * @param String cname
	 * @param String xname
	 * @param int nP
	 * @param int nC
	 */
	String getDateWithFormat(CommonTools cmt,String cname,String xname,int nP, int nC,String preFormat,String postFormat){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		String formattedDate = "";
		String dateText = cmt.getTextValue(By.className(cname),By.xpath(xname), nP, nC);
		log(myInfo + "Got the shown timestamp: [" + dateText + "]");

		formattedDate = getFormatTS(cmt,dateText,preFormat,postFormat);
		return formattedDate;
	}

	/*
	 * @brief Clear the text
	 * @param ommonTools cmt
	 * @param String cnameP
	 * @param String xname
	 * @param int nP
	 * @param int nC
	 */
	void clearText(CommonTools cmt,String cnameP,String xname,int nP,int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Clear the text of a text field");
		cmt.inputClear(By.className(cnameP), By.xpath(xname), nP, nC);
	}
	
	/*
	 * @brief put a text
	 * @param ommonTools cmt
	 * @param String cnameP
	 * @param String xname
	 * @param int nP
	 * @param int nC
	 * @param String text
	 */
	void putText(CommonTools cmt,String cnameP,String xname,int nP,int nC,String text){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		clearText(cmt,cnameP,xname,nP,nC);

		log(myInfo + "Input a text to a text field");
    	cmt.inputText(By.className(cnameP), By.xpath(xname), nP, nC, text);
	}

	/*
	 * @brief put a text and wait for a while
	 * @param ommonTools cmt
	 * @param String cnameP
	 * @param String xname
	 * @param int nP
	 * @param int nC
	 * @param String text
	 */
	void putTextWithWait(CommonTools cmt,String cnameP,String xname,int nP,int nC,String text){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		clearText(cmt,cnameP,xname,nP,nC);

		log(myInfo + "Input a text to a text field");
    	cmt.inputText(By.className(cnameP), By.xpath(xname), nP, nC, text);
    	
	    // TODO(2016-02-02) Need to check "waitFor...", but how ??
		log(myInfo + "...Just sleep for 300./1000.s...");
	    cmt.sleep(300);								
	}

	/*
	 * @brief Get inputted text
	 * @param String cnameP
	 * @param String xname
	 * @param int nP
	 * @param int nC
	 */
	String getInputText(CommonTools cmt,String cnameP,String xname,int nP,int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		String text = "";

		log(myInfo + "Get input text");
		text = cmt.getTextValue(By.className(cnameP),By.xpath(xname),nP,nC);
		log(myInfo + "Got: [" + text + "]");
		
		return text;
	}
	
	/*
	 * @brief Get a text
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 * @param String where, this is the place the text is located
	 */
	String getText(CommonTools cmt,String cnameP,String cnameC,int nP,int nC,String where){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Get a text from [" + where + "]");
		String text = "";
		text = cmt.getText(By.className(cnameP),By.className(cnameC),nP,nC);			
	    log(myInfo + "Got: [" + text + "]");
		return text;
	}

	/*
	 * @brief Get a text
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param String cnameGC
	 * @param int nP
	 * @param int nC
	 * @param int nGC
	 * @param String where, this is the place the text is located
	 */
	String getText(CommonTools cmt,String cnameP,String cnameC,String cnameGC,int nP,int nC,int nGC,String where){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Get a text from [" + where + "]");
		String text = "";
		text = cmt.getText(By.className(cnameP),By.className(cnameC),By.className(cnameGC),nP,nC,nGC);			
	    log(myInfo + "Got: [" + text + "]");
		return text;
	}

	/*
	 * @brief select menu in Interval
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int n
	 * @param String menu
	 */
	void selectOption(CommonTools cmt,String cnameP, String cnameC, int n, String menu){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Select an option from a menu");
		cmt.select(By.className(cnameP),By.className(cnameC),n,menu);

		// 2015-11-16 Wait mechanism is needed from ZK7
		try{
			// Wait for loading contents of the pane
			cmt.waitForTextIsLoaded(menu,10);	
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief select menu in Interval
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 * @param String menu
	 */
	void selectOption(CommonTools cmt,String cnameP, String cnameC, int nP, int nC, String menu){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Select an option from a menu");
		cmt.select(By.className(cnameP),By.className(cnameC),nP,nC,menu);

		// 2015-11-16 Wait mechanism is needed from ZK7
		try{
			// Wait for loading contents of the pane
			cmt.waitForTextIsLoaded(menu,10);	
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Click an item with "className", which is used by checking a keyword, 
	 * 		  selecting an option from menu, and so on
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 */
	void clickItem(CommonTools cmt,String cnameP, String cnameC, int nP, int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Click an item");
		cmt.click(By.className(cnameP), By.className(cnameC), nP,nC);
	}

	/*
	 * @brief Click item and wait the check mark is appeared
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 * @param int flag, which is ENABLE(=1) or DISABLE(=0)
	 */
	void clickItemAndWait(CommonTools cmt,String cnameP, String cnameC, int nP, int nC, int flag){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		String label = (flag == 1)? "ENABLE" : "DISABLE";
		log(myInfo + "Click an item and wait [" + label + "]");
		cmt.click(By.className(cnameP), By.className(cnameC), nP,nC);

		try{
			if(flag == 1)	// wait until the check mark is appeared
				cmt.waitUntilEnabled(By.className(cnameP), By.className(cnameC), nP, nC, 5);
			else{			// wait until the check mark is disappeared
				// TODO(2015-11-25) waitUntilDisabled() causes TimeoutException so use sleep temporary (10ms is good)
				// 2015-12-03 10ms seems to be a little short in keywordOneByOne, change to 15ms.
				log(myInfo + "...Just sleep for 15./1000.s...");
				cmt.sleep(15);
			}	
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Click a checkbox and wait until the check mark is appeared
	 * @param CommonTools cmt
	 * @param String cname
	 * @param String xname
	 * @param int nP
	 * @param int nC
	 * @param int flag, which is SELECT(=1) or UNSELECT(=0)
	 */
	void clickCheckbox(CommonTools cmt,String cname, String xname, int nP, int nC,int flag){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		String label = (flag == 1)? "SELECT" : "UNSELECT";
		log(myInfo + "Click a check box and wait [" + label + "]");
		cmt.click(By.className(cname), By.xpath(xname), nP,nC);

		try{
			if(flag == 1)	// wait until the check mark is appeared
				cmt.waitUntilSelected(By.className(cname), By.xpath(xname), nP, nC, 5);
			else{			// wait until the check mark is disappeared
				// TODO(2015-11-25) waitUntilDisabled() causes TimeoutException so use sleep temporary (10ms is good)
				log(myInfo + "...Just sleep for 15./1000.s...");
				cmt.sleep(15);
			}	
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Click an arrow image
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 */
	void clickImage(CommonTools cmt,String cnameP, String cnameC, int nP, int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Click an image");
		cmt.click(By.className(cnameP), By.className(cnameC), nP,nC);
	}

	/*
	 * @brief Click a link text
	 * @param CommonTools cmt
	 * @param String lname
	 */
	void clickLink(CommonTools cmt, String lname){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Click a link text");
		cmt.click(By.linkText(lname));
	}

	/*
	 * @brief Wait until the check box is marked
	 * @param CommonTools cmt
	 * @param String cname
	 * @param String xname
	 * @param int nP
	 * @param int nC
	 */
	void waitAfterClickLink(CommonTools cmt,String cname, String xname,int nP, int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Wait just a while until the check box is checked");

		try{
			// wait until the check mark is appeared
			cmt.waitUntilSelected(By.className(cname), By.xpath(xname), nP, nC, 5);
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Click a button
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nC
	 * @param String label
	 */
	void generateReport(CommonTools cmt,String cnameP,String cnameC,int nP,int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Click [Generate report]");
	    cmt.click(By.className(cnameP),By.className(cnameC),nP,nC);	
	    try{
		    // Wait for the new window is appeared, because the report is shown in the new window
	    	cmt.waitUntilNewWindowShown(10);	// The PDF report takes time!
		}catch(Exception e){
			saveLog(e);
		}

	}
	
	/*
	 * @brief Expand the closed pane
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 * @param String text
	 */
	void expandPane(CommonTools cmt,String cnameP,String cnameC,int nP,int nC,String text){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		// Click a closed pane bar
		log(myInfo + "Click a pane bar in Search popup");
		cmt.click(By.className(cnameP), By.className(cnameC), nP, nC);

		try{
			// Wait for loading contents of the pane
			cmt.waitForTextIsLoaded(text,10);	
		}catch(Exception e){
			saveLog(e);
		}
	}
	
	/*
	 * @brief Expand the closed pane
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 * @param String title
	 * @param String nameC2
	 * @param int nC2
	 */
	void contractPane(CommonTools cmt,String cnameP, String cnameC,int nP, int nC,String label, 
				   String cnameC2, int nC2){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		// Click a closed pane bar
		log(myInfo + "Click a pane bar in Search popup");
		cmt.click(By.className(cnameP), By.className(cnameC), nP, nC);
			
		// Wait if a web element on the pane are really active (displayed) or not
		// Note that waitForElementToUnload() doesn't work correctly, so do this checking only.
		while(cmt.checkDisplay(By.className(cnameP),By.className(cnameC2),nP, nC2)) ;
	}

	/*
	 * @brief Wait until the input text is shown
	 * @param CommonTools cmt
	 * @param text which is expected
	 */
	void waitAppearance(CommonTools cmt,String text){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Wait just a while until the text is appeared");
		try{
			cmt.waitForTextIsLoaded(text,5);	
		}catch(Exception e){
			saveLog(e);
		}
	}

	/*
	 * @brief Wait until the locator is enable
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 */
	void waitEnable(CommonTools cmt,String cnameP, String cnameC,int nP, int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Wait just a while until the element becomes enable");
		try{
			cmt.waitUntilEnabled(By.className(cnameP), By.className(cnameC), nP, nC,5);	
		}catch(Exception e){
			log(myInfo + "Failed to wait!");
			saveLog(e);
		}
	}

	/*
	 * @brief Show the tooltip
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 */
	String getTooltip(CommonTools cmt,String cnameP, String cnameC,int nP, int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Show the tooltip and get its text");
		log(myInfo + "...Just sleep for 1000./1000.s...");
		String tooltipStr = cmt.showTooltip(By.className(cnameP),By.className(cnameC),nP,nC);
		log(myInfo + tooltipStr);
		return tooltipStr;
 	}
	
	/*
	 * @brief Get the locator status about enablement
	 * @param CommonTools cmt
	 * @param String cname
	 * @param int n
	 */
	Boolean getEnablement(CommonTools cmt,String cname, int n){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Get the state about enablement");
		Boolean result = false;
		result = cmt.checkEnable(By.className(cname), n);
		String judge = (result == true)? "ENABLE" : "DISABLE";
		log(myInfo + "[" + judge + "]");
		return result;
	}

	/*
	 * @brief Get the locator status about enablement
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int n
	 */
	Boolean getEnablement(CommonTools cmt,String cnameP, String cnameC, int n){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Get the state about enablement");
		Boolean result = false;
		result = cmt.checkEnable(By.className(cnameP), By.className(cnameC), n);
		String judge = (result == true)? "ENABLE" : "DISABLE";
		log(myInfo + "[" + judge + "]");
		return result;
	}

	/*
	 * @brief Get the locator status about selection
	 * @param CommonTools cmt
	 * @param String cname
	 * @param String xname
	 * @param int nP
	 * @param int nC
	 */
	Boolean stateInSelection(CommonTools cmt,String cname, String xname, int nP, int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		Boolean result = false;
		log(myInfo + "Get the state about selection");
		result = cmt.checkSelect(By.className(cname), By.xpath(xname), nP, nC);
		String judge = (result == true)? "SELECTED" : "NOT SELECTED";
		log(myInfo + "[" + judge + "]");
		return result;
	}
	
	/*
	 * @brief Get the selected option name about selection
	 * @param CommonTools cmt
	 * @param String cnameP
	 * @param String cnameC
	 * @param int nP
	 * @param int nC
	 */
	String textInSelection(CommonTools cmt,String cnameP,String cnameC, int nP, int nC){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		String text = "";
		log(myInfo + "Get the selected text in selection");
		text = cmt.selectedText(By.className(cnameP),By.className(cnameC),nP,nC);
		log(myInfo + "Got: [" + text + "]");
		return text;
	}
	
	/*
	 * @brief Logout in normal
	 * @param CommonTools cmt
	 * @param String dialog
	 * @param int m
	 * @param String title
	 * @param String success
	 */
	void confirmDialog(CommonTools cmt,String title,String dialog,int m){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		// Wait for the dialog box is shown
		log(myInfo + "Wait for the dialog box is shown");
		try{
			cmt.waitForTextIsLoaded(title,10);	
		}catch(Exception e){
			saveLog(e);
		}
		// CLick yes to close the dialog box
		log(myInfo + "Click Yes on the dialog box");
		cmt.click(By.className(dialog), m);	
		try{
			cmt.waitForTextIsUnloaded(title,10);
		}catch(Exception e){
			saveLog(e);
		}
	}
	
	/*
	 * @brief Focusing
	 * @param CommonTools cmt
	 * @param String cname
	 * @param int n
	 */
	void focusing(CommonTools cmt,String cname,int n){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Focus for the determination");
		cmt.focus(By.className(cname),n);
	}

	/*
	 * @brief Waiting before doing something
	 * @param CommonTools cmt
	 * @param long milliseconds
	 */
	void waiting(CommonTools cmt, long milliseconds){
		String myInfo = thisClass.concat("#").concat(new Throwable().getStackTrace()[0].getMethodName()).concat(": ");
		log(myInfo + "Waiting before going ahead");
		log(myInfo + "...Just sleep for " + milliseconds + "/1000.s...");
	    cmt.sleep(milliseconds);								
	}
	
}
