package util;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.AssertJUnit;

import constants.GeneralComponentIDs;
import constants.PositionComponentIDs;

public class QueryFormUtils {
	private WebDriver webDriver;

	public QueryFormUtils(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public void waitForAutocomplete(String id) {
		String valueIn = null;
		while (valueIn == null || valueIn == "") {
			valueIn = webDriver.findElement(By.id(id)).getAttribute("value");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void insertValue(String id, String i) {
		WebElement element = webDriver.findElement(By.id(id));
		element.sendKeys(i);
	}

	public void pressSearchBut() {
		WebElement searchBut = webDriver.findElement(By
				.id(GeneralComponentIDs.searchButtonId));
		searchBut.click();
	}

	private WebElement getElement(WebDriver driver, String id) {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		WebElement element = webDriver.findElement(By.id(id));
		return element;
	}

	public String extractRAValue() {
		WebElement ra = getElement(webDriver, PositionComponentIDs.raId);
		return ra.getAttribute("value");
	}

	public String extractDecValue() {
		WebElement dec = getElement(webDriver, PositionComponentIDs.decId);
		return dec.getAttribute("value");
	}

	public String extractRadiusValue() {
		WebElement radius = getElement(webDriver,
				PositionComponentIDs.searchRadiusId);
		return radius.getAttribute("value");
	}

	public void checkRadiusFieldIsMandatory() {
		WebElement radius = getElement(webDriver,
				PositionComponentIDs.searchRadiusId);
		String radiusAttribute = radius.getAttribute("class");
		AssertJUnit.assertTrue("Radius field is mandatory!",
				radiusAttribute != null);
	}

	public void selectOptionInMultiSelectDropDown(String id, String optionText) {
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("document.getElementById('" + id
				+ "').style.display='block';");

		Select dropdown = new Select(webDriver.findElement(By.id(id)));
		dropdown.selectByVisibleText(optionText);
	}

	public void checkNoResultsMessage() {
		WebDriverWait wait = new WebDriverWait(webDriver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//span[@class='grid-message'][1]")));

		WebElement msg = webDriver.findElement(By
				.xpath("//span[@class='grid-message'][2]"));
		Assert.assertTrue(
				msg.getText()
						.contains(
								"No results were found. Please relax your search criteria and search again"),
				"Results were retrieved when they shouldn't");
	}

	public void clearValue(String fieldId) {
		WebElement element = webDriver.findElement(By.id(fieldId));
		element.clear();
	}

}
