package util;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TearDownUtils {
	DatabaseUtils dbUtil = new DatabaseUtils();
	private WebDriver webDriver;
	
	public TearDownUtils(WebDriver webDriver) {
		this.webDriver = webDriver;
	} 
	
	public void removeFromTableElement(String elementName) {
		WebElement table = webDriver.findElement(By.className("result"));
		String xPathHeader = "//*[@class='result']/thead/tr[1]/th";
		List<WebElement> rowCollectionHeader = table.findElements(By
				.xpath(xPathHeader));

		String xPathRow = "//*[@class='result']/tbody/tr/td";
		List<WebElement> rowCollection = table.findElements(By.xpath(xPathRow));

		String sColumnValue = "Name";
		for (int i = 1; i <= rowCollectionHeader.size(); i++) {
			String sValue = null;
			sValue = webDriver.findElement(By.xpath(".//*[@class='result']/thead/tr[1]/th[" + i + "]")).getText();

			if (sValue.equalsIgnoreCase(sColumnValue)) {
				for (int j = 1; j <= rowCollection.size(); j++) {
					String sRowValue = webDriver.findElement(By.xpath(".//*[@class='result']/tbody/tr[" + j + "]/td[" + i + "]")).getText();
					
					if (sRowValue.equalsIgnoreCase(elementName)){
						WebElement deleteBut= webDriver.findElement(By.xpath(".//*[@class='result']/tbody/tr[" + j + "]/td[1]/form/input[@type='submit']"));
					    deleteBut.click();
					    break;
					}
				}
			}
		}
	}
	
	public void removeSourceTypeId() throws Exception{
		WebElement sourceTypeName = webDriver
				.findElement(By.xpath("//input[@type='hidden'][@value='"
						+ dbUtil.getSourceTypeId()
						+ "'][@name='removeTypeId']/../input[@value='Remove']"));
		sourceTypeName.click();	
	}
	
	public void removeSourceName() throws Exception{
		WebElement sourceName = webDriver
				.findElement(By.xpath("//input[@type='hidden'][@value='"
						+ dbUtil.getSourceNameId()
						+ "'][@name='removeNameId']/../input[@value='Remove']"));
		sourceName.click();
		
	}

	public void removeSourceTypeForAutomationId() throws Exception {
		WebElement sourceTypeName = webDriver
				.findElement(By.xpath("//input[@type='hidden'][@value='"
						+ dbUtil.getSourceTypeForAutomationId()
						+ "'][@name='removeTypeId']/../input[@value='Remove']"));
		sourceTypeName.click();	
		
	}
}
