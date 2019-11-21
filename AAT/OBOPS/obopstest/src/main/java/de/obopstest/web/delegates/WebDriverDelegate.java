package de.obopstest.web.delegates;

import de.obopstest.web.utils.WaitUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;

import java.util.List;
import java.util.Set;

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
        return WaitUtil.waitForElementPresent(webDriver, by, timeOutInSeconds);
    }

    public WebElement findElementAndWait(By by) {
        return WaitUtil.waitForElementPresent(webDriver, by);
    }

    public List<WebElement> findElements(By by) {
        return webDriver.findElements(by);
    }

    public List<WebElement> findElementsAndWait(By by, int timeOutInSeconds) {
        return WaitUtil.waitForElementsPresent(webDriver, by, timeOutInSeconds);
    }

    public List<WebElement> findElementsAndWait(By by) {
        return WaitUtil.waitForElementsPresent(webDriver, by);
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
        ( ( JavascriptExecutor ) webDriver )
                .executeScript( "window.onbeforeunload = function(e){};" );
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

    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) webDriver).executeAsyncScript(script, args);
    }
}