package util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class AdvancedSelectorUtils {
	private WebDriver webDriver;
	DatabaseUtils dbUtil = new DatabaseUtils();
	
	public AdvancedSelectorUtils(WebDriver webDriver){
		this.webDriver = webDriver;
	}

	private WebElement getElementByLinkText(String text) {
		WebElement element = webDriver.findElement(By.linkText(text));
		return element;
	}

	public void clickOnLinkText(String text) {
		getElementByLinkText(text).click();
	}

	public void selectCatalogCheckBox() throws Exception {
		int idCatalog = dbUtil.getCatalogueId();
		WebElement catalogCheckBox = webDriver
				.findElement(By
						.cssSelector("input[type='checkbox'][name='catalogues'][value= '"
								+ idCatalog + "']"));
		catalogCheckBox.click();
	}

	public void selectSourceTypeCheckBox() throws Exception {
		int idSourceType = dbUtil.getSourceTypeId();
		WebElement sourceTypeCheckBox = webDriver.findElement(By
				.cssSelector("input[type='checkbox'][name='types'][value= '"
						+ idSourceType + "']"));
		sourceTypeCheckBox.click();
	}

	public void selectBandCheckBox() throws Exception {
		int idBand = dbUtil.getBandId();
		WebElement bandCheckBox = webDriver
				.findElement(By
						.cssSelector("input[type='checkbox'][name='selectedRanges'][value= '"
								+ idBand + "']"));
		bandCheckBox.click();
	}

	public void selectTextFromDropdown(String sourcetypeid, String text) {
		WebElement dropdown = webDriver.findElement(By.id(sourcetypeid));
		Select mySelect = new Select(dropdown);
		mySelect.selectByVisibleText(text);
	}

	public void clickOnButtonName(String buttonName) {
		WebElement createBut = webDriver.findElement(By
				.xpath("//input[@value='" + buttonName + "']"));
		createBut.click();
	}

	public void clickOnButtonAddName(String buttonName) {
		WebElement createBut = webDriver.findElement(By
				.xpath("//input[@value='Add'][@name='" + buttonName + "']"));
		createBut.click();
	}

	public String extractInfoMessage() {
		String infoMessage = webDriver.findElement(
				By.id("message-box-content")).getText();
		return infoMessage;
	}

	public int extractNewSourceId() {
		String h1 = webDriver.findElement(By.tagName("h1")).getText();
		int sourceId = Integer.parseInt(h1.replaceAll("\\D+", ""));
		return sourceId;
	}

	public void clickOnSaveButton() {	
		WebElement saveButton = webDriver.findElement(By
				.xpath("//button[contains(., 'Save')]"));
		saveButton.click();
	}
}
