package de.aqua.web.delegates;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;

import java.util.List;
import java.util.Set;

import static de.aqua.web.utils.WaitUtil.*;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class WebDriverDelegate implements WebDriver {

    private WebDriver webDriver;

    public WebDriverDelegate(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void close() {
        webDriver.close();
    }

    public WebElement findElement(By by) {
        return webDriver.findElement(by);
    }

    public WebElement findElementAndWait(By by, int timeOutInSeconds) {
        return waitForElementPresent(webDriver, by, timeOutInSeconds);
    }

    public WebElement findElementAndWait(By by) {
        return waitForElementPresent(webDriver, by);
    }

    public List<WebElement> findElements(By by) {
        return webDriver.findElements(by);
    }

    public List<WebElement> findElementsAndWait(By by, int timeOutInSeconds) {
        return waitForElementsPresent(webDriver, by, timeOutInSeconds);
    }

    public List<WebElement> findElementsAndWait(By by) {
        return waitForElementsPresent(webDriver, by);
    }

//	public Timeouts pageLoadTimeout() {
//		resetImplicitWait(webDriver);
//		return webDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
//	}

    public void get(String arg0) {
        webDriver.get(arg0);
    }

    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    public String getPageSource() {
        return webDriver.getPageSource();
    }

    public String getTitle() {
        return webDriver.getTitle();
    }

    public String getWindowHandle() {
        return webDriver.getWindowHandle();
    }

    public Set<String> getWindowHandles() {
        return webDriver.getWindowHandles();
    }

    public Options manage() {
        return webDriver.manage();
    }

    public Navigation navigate() {
        return webDriver.navigate();
    }

    public void quit() {
        webDriver.quit();
    }

    public TargetLocator switchTo() {
        return webDriver.switchTo();
    }

    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) webDriver).executeScript(script, args);
    }

    public byte[] getScreenshot() {
        WebDriver augmentedDriver = new Augmenter().augment(webDriver);
        return ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.BYTES);
    }

    public Actions actions() {
        return new Actions(webDriver);
    }

    public void mouseover(WebElement we) {
        Actions builder = new Actions(webDriver);
        builder.moveToElement(we).perform();
    }
}