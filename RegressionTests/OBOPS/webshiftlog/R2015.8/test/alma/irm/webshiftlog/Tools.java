package alma.irm.webshiftlog;

import java.nio.file.Path;
import java.text.ParseException;

public interface Tools {
	
	/*--------------------------------------------------------------------------
	 * General methods
	 *--------------------------------------------------------------------------*/

	/*
	 * @brief Get date
	 * @param String format
	 * @param int second
	 */
	String getDate(String format,int second);
	
	/*
	 * @brief Get date from a unix time (which is shown by String)
	 * @param String format
	 * @param String unixTime
	 */
	String getDateFromUnixTime(String format,String unixTime);

	/*
	 * @brief get the TimeStamp. In case "id" is "0", get the first one. In case of "1", get the second.
	 * 		  Because id = 0(click the header at the first) means list is in ascending order, and
	 * 		  the next click on the header, the entry list order is changed in the descending one.  
	 * @param String TS
	 * @param String type
	 * @param int id: only accept 0 or 1 (Need "Begin TS" or "End TS")
	 */
	String getTs(String inTs, String type, int id);
	
	/*
	 * @brief Change timestamp format 
	 * @param String timeStamp
	 * @param String preFormat, which is the current format of the input timestamp
	 * @param String postFormat, which is the format want to change 
	 */
	String formatTS(String timeStamp,String preFormat,String postFormat) throws ParseException;
	
	/*
	 * @brief Get the total file number in a directory
	 * @param String directoryName
	 */
	int getTotalFile(String directoryName);

	/*
	 * @brief Get the entry number from the entry summary message
	 * @param String string as an entry message
	 */
	int getEntryTotal(String string);

	/*
	 * @brief Create a screenshot
	 * @param Path directoryName for saving
	 * @param String fileName
	 */
	void getScreenshot(Path directoryName, String fileName) throws Exception;
	
	/*
	 * @brief Go to the new page
	 * @param String urlString
	 */
	void navegateNext(String urlString);

	/*
	 * @brief Back to the previous page
	 * @param none
	 */
	void navigateBack();

	/*
	 * @brief Wait for loading the page until a text is appeared
	 * @param final String text as expected
	 * @param int seconds for waiting
	 */
	void waitForTextIsLoaded(final String text, int seconds) throws Exception;
	
	/*
	 * @brief Wait for loading an expected title
	 * @param final String titleName as expected
	 * @param int seconds for waiting
	 */
	void waitForTitleIsLoaded(final String titleName, int seconds) throws Exception;

	/*
	 * @brief Wait for UNLOADING the page
	 * @param final String text
	 * @param int seconds for waiting
	 */
	void waitForTextIsUnloaded(final String text, int seconds) throws Exception;

	/*
	 * @brief Wait for a new window is appeared
	 * @param int timeOutInSeconds
	 */
	void waitUntilNewWindowShown(int timeOutInSeconds) throws Exception;

	/*
	 * @brief Get a window handle id
	 * @param String string
	 */
	String getWinId(String string);
	
	/*
	 * @brief Switch to another window
	 * @param String windowId
	 */
	void switchToWindow(String windowId);
	
	/*
	 * @brief Close the driver of the switched temporary window
	 * @param none
	 */
	void closeWindow();
	
	/* 
	 * @brief Check text is shown
	 * @param String text
	 */
	boolean isTextPresent(String text);

	/*
	 * @brief Do sleep in milliseconds
	 * @param long milliseconds
	 */
	void sleep(long milliseconds);
	
}
