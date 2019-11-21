package util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;

import constants.ResultTableComponentClassName;

//import alma.sourcecat.integrationtests.simplesearch.SimpleSearchByEnergyBand;

public class ResultTableUtils {
	private WebDriver webDriver;

	public ResultTableUtils(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public String getPageTitle(String id) {
		String resultsTableTitle = webDriver.findElement(By.id(id)) //
				.getText();
		return resultsTableTitle;
	}

	public void checkIfResultsAreRetrievedInResultTable(String message) {
		WebElement table = webDriver
				.findElement(
						By.className(ResultTableComponentClassName.resultTableClassName));
		AssertJUnit.assertTrue("Search results by " + message
				+ " are retrieved!", table.getText() != null);
	}

	public int getNumberOfResults() {
		WebDriverWait wait = new WebDriverWait(webDriver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//span[@class='grid-message'][1]")));
		
		WebElement pager = webDriver.findElement(
				By.className("slick-pager-status"));
		String pagerText = pager.getText();
		int result = Integer.parseInt(pagerText.replaceAll("\\D+", ""));

		return result;
	}
}
