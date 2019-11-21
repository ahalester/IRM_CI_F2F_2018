package alma.irm.webshiftlog;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;		// For using getScreenshotAs()
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;	// For using getScreenshotAs()
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static alma.irm.webshiftlog.Definitions.*;

public class CommonTools implements Tools {
	private final WebDriver driver;
	private final int retryMax = 3;
	
	// Constructer
	CommonTools(WebDriver driver){
		this.driver = driver;
	}

	/*------------------------------------------------------------------------
	 * Implemented Methods
	 * Declaration: Tools.java
	 * -----------------------------------------------------------------------*/

	//-----  D A T E S  A N D  T I M E S -----
	
	/*
	 * @brief Get current date with a defined format
	 * @param String format
	 * @param int second, which is the amount of seconds from the current date 
	 */
	public String getDate(String format,int second){
		Calendar c = Calendar.getInstance();					// Get a calendar by the default locale and timezone
		if(second != 0) c.add(Calendar.SECOND,second);			// Get date instead of current if need
		SimpleDateFormat sdf = new SimpleDateFormat(format);	// Set the defined format
		return sdf.format(c.getTime());							// Get current date with the define format
	}

	/*
	 * @brief Get date from a unix time (which is shown by String)
	 * @param String format
	 * @param String unixTime
	 */
	public String getDateFromUnixTime(String format,String unixTime){
		Calendar c = Calendar.getInstance();
    	c.setTimeInMillis(Long.parseLong(unixTime));			// Change "String" to "Long" and then set it into Calendar
		SimpleDateFormat sdf = new SimpleDateFormat(format);	// Set a format of date
		return sdf.format(c.getTime());
	}
	
	/*
	 * @brief get a TimeStamp. In case "id" is "0", get the first one. In case of "1", get the second.
	 * 		  Because id = 0(click the header at the first) means list is in ascending order, and
	 * 		  the next click on the header, the entry list order is changed in the descending one.  
	 * @param String TS
	 * @param String type
	 * @param int k: only accept 0 or 1 (Need "Begin TS" or "End TS")
	 */
	public String getTs(String inTs, String type, int k){
		String outTs = "";
		if(inTs == null || type == null){		
			// Do nothing, just return "outTs == null"
		}	
		else if(type.equals("AOG") || type.equals("ANTEN") || type.equals("WEATH")){
			outTs = inTs;
		}else{
			String[] tmpStr = inTs.split(" ", 0);	// Format:"fromTS(=outTs[0]) - toTS(=outTs[2])"
			// (TODO) Confirm: 2015-11-18 Get the "To" timestamp at any time. 
			// If it works, delete the comment below and erase the 3rd input argument of this method
			outTs = tmpStr[2];						// Note that tmpStr[0]=From TS,[1]="-",[2]=To TS						
//			outTs = tmpStr[0 + 2*k];				
		}

		return outTs;
	}

	/*
	 * @brief Change timestamp format 
	 * 		   from: "yyyy/MM/dd hh:mm:ss AM/PM" (hh = 1-12) OR "yyyy/MM/dd HH:mm:ss" (without AM/PM)
	 * 	       to  : "yyyy-MM-dd'T'HH:mm:ss"	 (HH = 0-23)
	 * @param String ts as a formatted timestamp, which will be changed to the new format
	 * @param String preFormat, which is the current format of the input timestamp
	 * @param String postFormat, which is the format want to change 
	 */
	public String formatTS(String ts,String preFormat,String postFormat) throws ParseException{
		String newTs = null;

		// Change String to Date type
		// Note that AM/PM uses 1-12 hours, so format by "hh" not "HH"
		SimpleDateFormat sdf1 = new SimpleDateFormat(preFormat);
		Date date = sdf1.parse(ts);

		// Change Date to new formatted String
		SimpleDateFormat sdf2 = new SimpleDateFormat(postFormat);
		newTs = sdf2.format(date);
		
		return newTs;
	}
		
	//----- N U M B E R -----
	
	/*
	 * @brief Get the entry number from the entry summary message
	 * @param String str like "Found 100 entries in 1.5 seconds" and need to get the number "100" 
	 */
	public int getEntryTotal(String str){
		int n = 0;											// Initialization
		if(str.length() == 0) return 0;						// If no string, return by 0
		
		int indexS= str.indexOf(" ");						// Read position of the first " " is shown
		int indexE = str.indexOf(" ", indexS+1);			// Read the next " " is shown
		n = Integer.parseInt(str.substring(indexS+1,indexE));	// Get number after "Found " and before " entries"

		return n;
	}
	
	//----- F I L E S -----

	/*
	 * @brief Get the total file number at a directory
	 * @param String dirname
	 */
	public int getTotalFile(String dirname){
		File dir = new File(dirname);
		File[] fileList = dir.listFiles();
		return fileList.length;
	}
		
	/*
	 * @brief Create a screenshot. In case of exception, put log at the upper function.
	 * 		  Note that default is put on /tmp as screenshot<num>.png and is deleted automatically 
	 *        at finishing
	 * @param Path captureDirectory
	 * @param String filename
	 */
	public void getScreenshot(Path captureDirectory, String filename) throws Exception{
		if(driver instanceof TakesScreenshot){
     		TakesScreenshot screen = (TakesScreenshot)driver;
     		Path capture = captureDirectory.resolve(filename);
     		try{
     			Files.write(capture, screen.getScreenshotAs(OutputType.BYTES));
    		}catch(Exception e){
    			throw e;
    		}
     	}
    }
    
	// ----- N A V I G A T I O N -----
	
	/*
	 * @brief Go to the new page
	 * @param String url
	 */
	public void navegateNext(String url){
		driver.navigate().to(url);
	}
	
	/*
	 * @brief Back to the previous page
	 * @param none
	 */
	public void navigateBack(){
		driver.navigate().back();
	}

	// ----- W A I T I N G -----
	
	/*
	 * @brief Wait for loading the page until a text is appeared
	 * @param final String text
	 * @param int timeOutInSeconds
	 */
	public void waitForTextIsLoaded(final String text, int timeOutInSeconds) throws Exception{
		try{
			(new WebDriverWait(driver, timeOutInSeconds)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver){	
					return isTextPresent(text);
				}
			});
		}catch(Exception e){
			throw e;
		}
	}

	/*
	 * @brief Wait for loading an expected title
	 * @param final String text
	 * @param int timeOutInSeconds
	 */
	public void waitForTitleIsLoaded(final String title, int timeOutInSeconds) throws Exception{
		try{
			(new WebDriverWait(driver, timeOutInSeconds)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver){	
					return driver.getTitle().equals(title);
				}
			});
		}catch(Exception e){
			throw e;
		}
	}

	/*
	 * @brief Wait for UNLOADING the page
	 * @param final String text
	 * @param int timeOutInSeconds
	 */
	public void waitForTextIsUnloaded(final String text, int timeOutInSeconds) throws Exception{
		try{
			(new WebDriverWait(driver, timeOutInSeconds)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver){	
					return !isTextPresent(text);
				}
			});
		}catch(Exception e){
			throw e;
		}
	}

	/*
	 * @brief Wait for a new window is appeared
	 * @param int timeOutInSeconds
	 */
	public void waitUntilNewWindowShown(int timeOutInSeconds) throws Exception{
		try{
			(new WebDriverWait(driver, timeOutInSeconds)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver){
					// Wait until the number of window handles are two, which are original and new one
					return driver.getWindowHandles().size() == 2;
				}
			});
		}catch(Exception e){
			throw e;
		}
	}

	//----- W I N D O W -----
	
	/*
	 * @brief Get a window handle id
	 * @param String orgWinId
	 */
	public String getWinId(String orgWinId){
		String newWinId = null;
		
		for(String id: driver.getWindowHandles()){
			if(!id.equals(orgWinId)){
				newWinId = id;
			}
		}
		
		return newWinId;
	}
	
	/*
	 * @brief Switch to another window
	 * @param String winId, which is the window handle of the switched window
	 */
	public void switchToWindow(String winId){
		driver.switchTo().window(winId);
	}
	
	/*
	 * @brief Close the driver of the switched temporary window
	 */
	public void closeWindow(){
		driver.close();
	}
	
	/* 
	 * @brief Check text is shown
	 *        Note that Selenium2 (webDriver) does not have the checking like this, thus,
	 *        "isTextPresent()" was gotten from http://stackoverflow.com/qustions/7706776
	 * @param String text
	 */
	public boolean isTextPresent(String text){
	    try{
	        boolean b = driver.getPageSource().contains(text);
	        return b;
	    }
	    catch(Exception e){	// Just return false and no stacktrace print
	        return false;
	    }
	}
	
	//----- S L E E P -----
	
	/*
	 * @brief Do sleep in milliseconds
	 * @param long milliseconds
	 * @param String place	
	 */
	public void sleep(long milliseconds){
	    try{
	    	Thread.sleep(milliseconds);
	    }catch(InterruptedException e){
	    	throw new RuntimeException(e);
	    }

	}
	
	/*--------------------------------------------------------------------------
	 * Methods with parameters of web elements
	 *--------------------------------------------------------------------------*/

	/*------------------------------------------------------------------------
	 * Local Methods
	 * TODO(2015-06-10) These methods must be implemented in interface later
	 * -----------------------------------------------------------------------*/

	/*
	 * @brief Check message string is gotten or not
	 * @param String chkMg, which is a target string to check
	 * @param String noMsg, which is a defined message to ignore
	 */
	boolean getMsg(String chkMsg, String noMsg){
		return chkMsg(chkMsg, noMsg);
	}
	/*
	 * @brief Check message string is gotten or not
	 * @param String chkMg, which is a target string to check
	 * @param String noMsg, which is a defined message to ignore
	 */
	boolean chkMsg(String chkMsg, String noMsg){
//		return (chkMsg == null || chkMsg.equals(noMsg) || chkMsg == "");
		// Change the last check, because it doesn't work correctly
		return (chkMsg == null || chkMsg.equals(noMsg) || chkMsg.length() == 0);
	}
	
	/*
	 * @brief Wait until a locator is selected
	 * @param final By locatorP
	 * @param final By locatorC
	 * @param int nP
	 * @param int nC
	 * @param int timeOutInSeconds
	 */
	void waitUntilSelected(final By locatorP,final By locatorC,final int nP,final int nC,int timeOutInSeconds) throws Exception{
		try{
			(new WebDriverWait(driver, timeOutInSeconds)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver){
					WebElement el = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC);
					return el.isSelected();
				}
			});
		}catch(Exception e){
			throw e;
		}
	}

	/*
	 * @brief Wait until a locator is enabled
	 * @param final By locatorP
	 * @param final By locatorC
	 * @param int nP
	 * @param int nC
	 * @param int timeOutInSeconds
	 */
	void waitUntilEnabled(final By locatorP,final By locatorC,final int nP,final int nC,int timeOutInSeconds) throws Exception{
		try{
			(new WebDriverWait(driver, timeOutInSeconds)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver){
					WebElement el = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC);
					return el.isEnabled();
				}
			});
		}catch(Exception e){
			throw e;
		}
	}

	/*
	 * @brief Wait until a locator is disabled
	 * @param final By locatorP
	 * @param final By locatorC
	 * @param int nP
	 * @param int nC
	 * @param int timeOutInSeconds
	 */
	void waitUntilDisabled(final By locatorP,final By locatorC,final int nP,final int nC,int timeOutInSeconds) throws Exception{
		try{
			(new WebDriverWait(driver, timeOutInSeconds)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver){
					WebElement el = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC);
					return !el.isEnabled();
				}
			});
		}catch(Exception e){
			throw e;
		}
	}

	/*
	 * @brief Check the web element is selected
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 * @param int nC
	 * @param String atrName as an attribute name
	 */
	String getAttributeByName(By locatorP, By locatorC, int nP, int nC, String atrName){
		String result = "";
		for(int i = 0;i < retryMax;i++){	// In case of exception, try again
			try{
				result = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC).getAttribute(atrName);
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		return result;
	}
	
	/*
	 * @brief Move to the web element and check the tooltip
	 */
	String showTooltip(By locatorP,By locatorC,int nP,int nC){
		WebElement el = null;
		for(int i = 0;i < retryMax;i++){	// In case of exception, try again
			try{
				// Create action and perform it
				el = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC);
				Actions builder = new Actions(driver);					// Create actions for interaction
				builder.moveToElement(el).build().perform();			// Build action and then perform the action
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}

		// Wait for showing a tooltip
		sleep(1000);							// 1 second seems good

		if(el.isDisplayed()) return el.getAttribute("title");
		else 				 return null;

	}

	/*
	 * @brief right-click
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 * @param int nC
	 */
	void rightClick(By locatorP,By locatorC,int nP,int nC){
		for(int i = 0;i < retryMax;i++){	// In case of exception, try again
			try{
				// Create action and perform it
				WebElement el = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC);
				Actions builder = new Actions(driver);					// Create actions for interaction
				builder.contextClick(el).build().perform();				// Build action and then perform the action
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}

	/*
	 * @brief Execute <a href...> on a menu popup. Instead of selecting a menu, do servlet forcibly.
	 *        (2015-07-22) Note that Actions with "moveToElement(el).clickAndHold().release()" for selecting
	 *        a menu item doesn't work. So, just access "a href" of that menu directly.
	 * @param By locatorP
	 * @param By locatorCss for cssSelector
	 * @param int nP
	 */
	void hrefClick(By locatorP,By locatorCss,int nP,int nC){
		for(int i = 0;i < retryMax;i++){
			try{
				driver.findElements(locatorP).get(nP).findElements(locatorCss).get(nC).click();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}

	/*
	 * @brief
	 */
	void dragAndDrop(By locatorP,By locatorC,int nP,int nC){
		for(int i = 0;i < retryMax;i++){
			try{
				// Create action and perform it
				WebElement el = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC);
				Actions builder = new Actions(driver);					// Create actions for interaction
				builder.dragAndDropBy(el,50,0).perform();				// Build action and then perform the action
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}
	
	/*
	 * @bried Get the total number of web elements 
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 */
	int getWebElementsNumber(By locatorP, By locatorC, int nP){
		int num = 0;

		for(int i = 0;i < retryMax;i++){
			try{
				num = driver.findElements(locatorP).get(nP).findElements(locatorC).size();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		
		return num;
	}
	
	/*
	 * @brief Clear a text field with a locator
	 * @param By locator
	 */
	void inputClear(By locator){				
		for(int i = 0;i < retryMax;i++){
			try{
				driver.findElement(locator).clear();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}
	
	/*
	 * @brief Clear a text field with locators
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 * @param int nC
	 */
	void inputClear(By locatorP, By locatorC, int nP, int nC){			
		for(int i = 0;i < retryMax;i++){
			try{
				WebElement el = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC);
				el.clear();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}

	/*
	 * @brief Input a text into a text field with a locator
	 * @param By locator
	 * @param String text
	 */
	void inputText(By locator, String text){	
			driver.findElement(locator).sendKeys(text);
	}

	/*
	 * @brief Input a text into a text field with locators and specified positions
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 * @param int nC
	 * @param String text
	 */
	void inputText(By locatorP, By locatorC, int nP, int nC, String text){	
		for(int i = 0;i < retryMax;i++){
			try{
				WebElement el = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC);
				el.sendKeys(text);
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}
	
	/*
	 * @brief get a text with a locator
	 * @param By locator
	 */
	String getText(By locator){
		String text = null;
		// Note that getText() often causes exception in spite of locator's existence
		//  To avoid that situation, do retry for getting a text
		for(int i = 0;i < retryMax;i++){
			try{
				// Note that "Implicit wait" doesn't work if findElement().getText() runs.
				//  It only works at findElement() without any methods
				WebElement el = driver.findElement(locator);	
				text = el.getText();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		return text;
	}
	
	/*
	 * @brief get a text with locators and specified positions
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 * @param int nC
	 */
	String getText(By locatorP,By locatorC,int nP, int nC){
		String text = null;
		for(int i = 0;i < retryMax;i++){
			try{
				WebElement el = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC);
				text = el.getText();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		return text;
	}
	
	/*
	 * @brief get a text with three locators and specified positions
	 * @param By locatorP
	 * @param By locatorC
	 * @param By locatorGC
	 * @param int nP
	 * @param int nC
	 * @param int nGC
	 */
	String getText(By locatorP,By locatorC,By locatorGC, int nP, int nC, int nGC){
		String text = null;
		for(int i = 0;i < retryMax;i++){
			try{
				WebElement el = driver.findElements(locatorP).get(nP)
							          .findElements(locatorC).get(nC)
							          .findElements(locatorGC).get(nGC);
				text = el.getText();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		return text;
	}
	
	/*
	 * @brief Get "value" from text field with locators and a specified position
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 * @param int nC
	 */
	String getTextValue(By locatorP,By locatorC, int nP, int nC){
		String text = null;
		for(int i = 0;i < retryMax;i++){
			try{
				WebElement el = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC);
				text = el.getAttribute("value");
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		return text;
	}

	/*
	 * @brief Get <img src> string\
	 * @param By locator
	 */
	String getImgSrc(By locator){
		String text = null;
		for(int i = 0;i < retryMax;i++){
			try{
				WebElement el = driver.findElement(locator);
				text = el.getAttribute("src");
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		return text;
	}
	
	/*
	 * @brief Select an item from a menu with a locator
	 * @param By locator
	 * @param String label
	 */
	void select(By locator, String label){
		for(int i = 0;i < retryMax;i++){
			try{
				Select element = new Select(driver.findElement(locator));
				element.selectByVisibleText(label);
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}

	/*
	 * @brief Select an item from a menu with locators
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 * @param String label
	 */
	void select(By locatorP, By locatorSelect, int nP, String label){
		for(int i = 0;i < retryMax;i++){
			try{
				WebElement parent = driver.findElements(locatorP).get(nP);
				Select element = new Select(parent.findElement(locatorSelect));
				element.selectByVisibleText(label);
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}

	/*
	 * @brief Select an item from a menu with locators
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 * @param int nC
	 * @param String label
	 */
	void select(By locatorP, By locatorSelect, int nP, int nC, String label){
		for(int i = 0;i < retryMax;i++){
			try{
				WebElement parent = driver.findElements(locatorP).get(nP);
				Select element = new Select(parent.findElements(locatorSelect).get(nC));
				element.selectByVisibleText(label);
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}

	/*
	 * @brief Get the selected option
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 */
	String selectedText(By locatorP, By locatorSelect, int nP){
		String text = null;
		for(int i = 0;i < retryMax;i++){
			try{
				WebElement parent = driver.findElements(locatorP).get(nP);
				Select element = new Select(parent.findElement(locatorSelect));
				WebElement selectedOption = element.getFirstSelectedOption();
				text = selectedOption.getText();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		return text;
	}

	/*
	 * @brief Get the selected option
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 * @param int nC
	 */
	String selectedText(By locatorP, By locatorSelect, int nP, int nC){
		String text = null;
		for(int i = 0;i < retryMax;i++){
			try{
				WebElement parent = driver.findElements(locatorP).get(nP);
				Select element = new Select(parent.findElements(locatorSelect).get(nC));
				WebElement selectedOption = element.getFirstSelectedOption();
				text = selectedOption.getText();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		return text;
	}

	/*
	 * @brief Click a button with a locator
	 * @param By locator
	 */
	void click(By locator){
		for(int i = 0; i < retryMax;i++){
			try{
				// Check the locator is exist or not
				WebDriverWait wait = new WebDriverWait(driver,5);
				wait.until(ExpectedConditions.presenceOfElementLocated(locator));

				WebElement el = driver.findElement(locator);
				el.click();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}
	
	/*
	 * @brief Click a button with a locator and a specified position
	 * @param By locator
	 * @param int n
	 */
	void click(By locator, int n){
		for(int i = 0; i < retryMax;i++){
			try{
				// Check the locator is exist or not
				// Note that check only the first one not "n"th one (if the first one exist, "n"th as well)
				WebDriverWait wait = new WebDriverWait(driver,5);
				wait.until(ExpectedConditions.presenceOfElementLocated(locator));

				WebElement el = driver.findElements(locator).get(n);
				el.click();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}
	
	/*
	 * @brief Click a button with locators and positions
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 * @param int nC
	 */
	void click(By locatorP, By locatorC, int nP, int nC){
		for(int i = 0; i < retryMax;i++){
			try{
				// Check the locator is exist or not
				// Note that check only the parent locator
				WebDriverWait wait = new WebDriverWait(driver,5);
				wait.until(ExpectedConditions.presenceOfElementLocated(locatorP));

				WebElement el = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC);
				el.click();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}

	/*
	 * @brief Focus the element by a key
	 * @param By locator
	 * @param int n
	 */
	void focus(By locator, int n){
		for(int i = 0; i < retryMax;i++){
			try{
				WebElement el = driver.findElements(locator).get(n);
				el.sendKeys(Keys.CONTROL);
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}

	/*
	 * @brief Check the web element is displayed
	 * @param By locator
	 * @param int n
	 */
	boolean checkDisplay(By locator, int n){
		boolean result = false;
		for(int i = 0; i < retryMax;i++){
			try{
				WebElement el = driver.findElements(locator).get(n);
				result = el.isDisplayed();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		return result;
	}

	/*
	 * @brief Check the web element is displayed
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 * @param int nC
	 */
	boolean checkDisplay(By locatorP, By locatorC, int nP, int nC){
		boolean result = false;
		for(int i = 0; i < retryMax;i++){
			try{
				WebElement el = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC);
				result = el.isDisplayed();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		return result;
	}

	/*
	 * @brief Check the web element is enabled
	 * @param By locator
	 * @param int n
	 */
	boolean checkEnable(By locator, int n){
		boolean result = false;
		for(int i = 0; i < retryMax;i++){
			try{
				WebElement el = driver.findElements(locator).get(n);
				result = el.isEnabled();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		return result;
	}

	/*
	 * @brief Check the web element is enabled
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nC
	 */
	boolean checkEnable(By locatorP, By locatorC, int nC){
		boolean result = false;
		for(int i = 0; i < retryMax;i++){
			try{
				result = driver.findElements(new ByChained(locatorP,locatorC)).get(nC).isEnabled();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		return result;
	}

	/*
	 * @brief Check the web element is selected
	 * @param By locatorP
	 * @param By locatorC
	 * @param int nP
	 * @param int nC
	 */
	boolean checkSelect(By locatorP, By locatorC, int nP, int nC){
		boolean result = false;
		for(int i = 0; i < retryMax;i++){
			try{
				WebElement el = driver.findElements(locatorP).get(nP).findElements(locatorC).get(nC);
				result = el.isSelected();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
		return result;
	}

	/*
	 * @brief Click on the two entries
	 * 		  This seems to be execure by Robot() in AWT but not work properly according to the following codes:
	 * 			new Actions(driver).click(driver.findElements(locatorP).get(nP)).build().perform();
	 * 			new Robot().keyPress(KeyEvent.VK_CONTROL);
	 * 			new Actions(driver).click(driver.findElements(locatorP).get(nP+1)).build().perform();
	 * 			new Robot().keyRelease(KeyEvent.VK_CONTROL);
	 *		  Note that StaleElementReferenceException often happens
	 * @param By locator
	 * @param int nA
	 * @param int nB
	 */
	void clickOnTwoItems(By locator,int nA,int nB){
		for(int i = 0;i < retryMax;i++){	// In case of exception, try again
			try{
				Actions builder = new Actions(driver);					// Create actions for interaction
				builder.click(driver.findElements(locator).get(nA));
				builder.keyDown(Keys.CONTROL);
				builder.click(driver.findElements(locator).get(nB));
				builder.build().perform();
				break;
			}catch(Exception e){
				saveLog(e);
			}
		}
	}

	/*
	 * (2015-07-17) THIS IS A TEMPORARY FOR TESTING MOVING BAR ON SUITE1
	 */
	void testForMovingBar(){
		// Check the default area of the Entry list
		WebElement tmpEl = driver.findElement(By.cssSelector(".z-listbox-body"));
		System.out.println("Entry list area's style: [" + tmpEl.getAttribute("style") + "]");
		System.out.println("Entry list: " + driver.findElement(By.cssSelector(".z-listbox-body")).getLocation());
		
		// Check the horizontal bar's dimension and position
		WebElement el = driver.findElement(By.className("z-north-splt"));	// Get a Web element for doing an action
		String heightPx = el.getCssValue("top");
		String widthPx = el.getCssValue("width");
		System.out.println("style: [" + el.getAttribute("style") + "]");
		System.out.println("In style,  width=" + widthPx + ",height=" + heightPx );
		System.out.println(" in Integer: (" + widthPx.substring(0,widthPx.length()-2)
											+ "," + heightPx.substring(0,heightPx.length()-2) + ")");
		
		Dimension dimensions = el.getSize();
		System.out.println("Width=" + dimensions.width + ", Height=" + dimensions.height);
		Point point = el.getLocation();							// At top-left position
		System.out.println("X pos=" + point.x + ", Y pos=" + point.y);
		
		// Move the horizontal bar down
		Actions builder = new Actions(driver);					// Create actions for interaction
		builder.moveToElement(el)
		       .clickAndHold()								// Add an action: Mouse button down
		       .moveByOffset(0,200)								// Add an action: Mouse drag
		       .release()										// Add an action: Mouse button release
			   .perform();										// Perform the action

		// Check the size of the Entry list area 
		System.out.print("After::");
		System.out.println("Entry list: " + driver.findElement(By.cssSelector(".z-listbox-body")).getLocation());
	}
	
	
}
