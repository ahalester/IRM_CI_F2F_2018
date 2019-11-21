package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdvancedResultTableUtils {
	private WebDriver webDriver;
	
	public AdvancedResultTableUtils(WebDriver webDriver){
		this.webDriver = webDriver;
	}
	
	public int getNoOfReturnedResultInUI() {
		WebElement result = webDriver.findElement(By
				.className("info-header"));
		String resultsText = result.getText();
		if (resultsText.matches("[a-zA-Z]+"))
			return 0;
		else {
			int resultNo = Integer.parseInt(resultsText.replaceAll("\\D+", ""));
			return resultNo;
		}

	}

	private int getColumnPosition(String columnName) {
		List<WebElement> headers = webDriver.findElements(By
				.cssSelector("[class='slick-column-name']"));
	
		int col_position = 0;
		for (int i = 0; i <= headers.size(); i++) {
			if ((headers.get(i).getText()).contains(columnName)) {
				col_position = i + 1;
				break;
			}
		}
		return col_position;	
	}
	
	public String getCellValue(String columnName) {
		int colPosition = getColumnPosition(columnName) - 1;
		
		WebDriverWait wait = new WebDriverWait(webDriver, 10);
		wait.until(
		        ExpectedConditions.visibilityOfElementLocated(By
						.cssSelector("[class*='slick-row']:first-child")));
		
		WebElement row = webDriver.findElement(By
				.cssSelector("[class*='slick-row']:first-child"));
		
		WebElement element = row.findElement(By
				.cssSelector("[class='slick-cell l" + colPosition + " r" + colPosition  + "']"));
		
		return element.getText();
	}

	public void doubleClick() {
		WebElement row = webDriver.findElement(By
				.cssSelector("[class*='slick-row']:first-child"));
		
		Actions act = new Actions(webDriver);
		act.doubleClick(row).build().perform();
	}
	
	public String getMonthInt(String monthString) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new SimpleDateFormat("MMM").parse(monthString));
		int monthInt = cal.get(Calendar.MONTH) + 1;
		return Integer.toString(monthInt);
	}

	public void addColumnToTable(String string) {
		// TODO Auto-generated method stub
	}
}
