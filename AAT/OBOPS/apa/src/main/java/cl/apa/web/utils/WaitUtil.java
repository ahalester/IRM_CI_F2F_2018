package cl.apa.web.utils;

import cl.apa.web.pageobjects.GeneralPage;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import cl.apa.web.delegates.WebDriverExtended;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class WaitUtil {

    private static Logger LOG = LoggerFactory.getLogger(WaitUtil.class);

    public static final int DEFAULT_WAIT_FOR_PAGE_LOAD = 60;
    public static final int DEFAULT_WAIT_FOR_ELEMENT = 5;
    public static final int DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR = 180;

    public static void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            LOG.info("Exception thrown while the thread was sleeping for %s milliseconds!", timeout);
        }
    }

    public static WebElement waitUntilVisible(WebDriver driver, WebElement element) {
        WebDriverWait waiting = new WebDriverWait(driver, DEFAULT_WAIT_FOR_ELEMENT);
        waiting.until(ExpectedConditions.visibilityOf(element));
        return element;
    }

    public static WebElement waitUntilVisible(WebDriver driver, WebElement element,
                                              final int wait) {
        WebDriverWait waiting = new WebDriverWait(driver, wait);
        waiting.until(ExpectedConditions.visibilityOf(element));
        return element;
    }

    public static void waitUntilClickable(WebDriver driver, By locator) {
        WebDriverWait waiting = new WebDriverWait(driver, DEFAULT_WAIT_FOR_ELEMENT);
        waiting.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForElement(WebDriver driver, final By by, int timeOutInSeconds) {
        WebElement element = null;
        try {
            nullifyImplicitWait(driver);

            element = new WebDriverWait(driver, timeOutInSeconds)
                    .until(ExpectedConditions.visibilityOfElementLocated(by));

            resetImplicitWait(driver);
        } catch (Exception e) {
            LOG.info("The element was not found in " + timeOutInSeconds + " seconds!");
        }
        return element;
    }

    public static WebElement waitForElement(WebDriver driver, final By by) {
        return waitForElement(driver, by, DEFAULT_WAIT_FOR_ELEMENT);
    }

    public static WebElement waitForElementPresent(WebDriver driver, final By by,
                                                   int timeOutInSeconds) {
        WebElement element = null;
        try {
            nullifyImplicitWait(driver);

            element = new WebDriverWait(driver, timeOutInSeconds)
                    .until(ExpectedConditions.presenceOfElementLocated(by));

            resetImplicitWait(driver);
        } catch (TimeoutException | NoSuchElementException e) {
            LOG.info("The element was not found in " + timeOutInSeconds + " seconds!");
        }
        return element;
    }

    public static WebElement waitForElementPresent(WebDriver driver, final By by,
                                                   WebElement element) {
        // Perform the actions on the new window
        boolean statusInProgress = true;
        long startTime = System.currentTimeMillis();
        long actualTime;
        do {
            WebElement statusElement;
            actualTime = System.currentTimeMillis();
            try {
                statusElement = element;
                if (statusElement.equals(null)) {
                    break;
                } else {
                    sleep(30000);
                }
            } catch (Exception e) {
                statusInProgress = false;
            }
        }
        while (statusInProgress
                && TimeUnit.MILLISECONDS.toSeconds(actualTime - startTime)
                < DEFAULT_WAIT_FOR_ELEMENT);

        Assert.assertTrue(
                String.format("Timeout after waiting '%d' seconds for element " + by + ": "
                        + element, DEFAULT_WAIT_FOR_ELEMENT),
                TimeUnit.MILLISECONDS.toSeconds(actualTime - startTime)
                        < DEFAULT_WAIT_FOR_ELEMENT);
        return element;
    }

    public static WebElement waitForElementPresent(WebDriver driver, final By by) {
        return waitForElementPresent(driver, by, DEFAULT_WAIT_FOR_ELEMENT);
    }

    public static List<WebElement> waitForElementsPresent(WebDriver driver, final By by,
                                                          int timeOutInSeconds) {
        List<WebElement> elements = null;
        try {
            nullifyImplicitWait(driver);

            elements = new WebDriverWait(driver, timeOutInSeconds)
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));

            resetImplicitWait(driver);
        } catch (TimeoutException | NoSuchElementException e) {
            LOG.info("The element was not found in " + timeOutInSeconds + " seconds!");
        }
        return elements;
    }

    public static List<WebElement> waitForElementsPresent(WebDriver driver, final By by) {
        return waitForElementsPresent(driver, by, DEFAULT_WAIT_FOR_ELEMENT);
    }

    public static WebElement waitForElementIsDisplayedNoException(WebDriver driver, final By by,
                                                                  int timeOutInSeconds) {
        WebElement element = null;
        try {
            nullifyImplicitWait(driver);

            element = new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(NoSuchElementException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(by));

            resetImplicitWait(driver);
        } catch (Exception e) {
            LOG.info("The element was not found in " + timeOutInSeconds + " seconds !");
        }
        return element;
    }

    public static WebElement waitForElementIsDisplayedNoException(WebDriver driver, final By by) {
        return waitForElementIsDisplayedNoException(driver, by, DEFAULT_WAIT_FOR_ELEMENT);
    }

    public static WebElement waitForElementIsDisplayed(WebDriver driver, final By by,
                                                       int timeOutInSeconds) {
        WebElement element = null;
        try {
            nullifyImplicitWait(driver);

            element = new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(NoSuchElementException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(by));

            resetImplicitWait(driver);
        } catch (Exception e) {
            Assert.fail(String.format("Timeout when waiting %d seconds for '%s' ", timeOutInSeconds,
                    by));
        }
        return element;
    }

    public static WebElement waitForElementIsDisplayed(WebDriver driver, final By by) {
        return waitForElementIsDisplayed(driver, by, DEFAULT_WAIT_FOR_ELEMENT);
    }

    public static boolean waitForElementIsNotDisplayed(WebDriver driver, final By by,
                                                       int timeOutInSeconds) {
        boolean isNotDisplayed = false;
        try {
            nullifyImplicitWait(driver);

            isNotDisplayed = new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(NoSuchElementException.class)
                    .until(ExpectedConditions.invisibilityOfElementLocated(by));

            resetImplicitWait(driver);
        } catch (Exception e) {
            Assert.fail("Timeout waiting for element to appear longer than "
                    + timeOutInSeconds + " seconds!");
        }
        return isNotDisplayed;
    }

    public static boolean waitForElementIsNotDisplayedNoException(WebDriver driver, final By by,
                                                                  int timeOutInSeconds) {
        boolean isNotDisplayed = false;
        try {
            nullifyImplicitWait(driver);

            isNotDisplayed = new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(NoSuchElementException.class)
                    .until(ExpectedConditions.invisibilityOfElementLocated(by));

            resetImplicitWait(driver);
        } catch (Exception e) {
            LOG.info("Exception occurred while waiting for the element to disappear!");
        }
        return isNotDisplayed;
    }

    public static boolean waitForLocatedElementIsNotDisplayed(WebDriver driver, WebElement element,
                                                              int timeOutInSeconds) {
        boolean isNotDisplayed = false;
        if (!driver.getCurrentUrl().contains("eso.org")) {
            try {
                nullifyImplicitWait(driver);

                isNotDisplayed = new WebDriverWait(driver, timeOutInSeconds)
                        .ignoring(NoSuchElementException.class)
                        .until(invisibilityOfElement(element));

                resetImplicitWait(driver);
            } catch (Exception e) {
                Assert.fail(String.format("Timeout waiting for more than %d seconds for the element "
                                + "identified by class '%s' to disappear!", timeOutInSeconds,
                        element.getAttribute("class")));
            }
        } else {
            sleep(7000);
        }
        return isNotDisplayed;
    }

    /**
     * An expectation for checking that an element is either invisible or not present on the DOM.
     *
     * @param element the expected element
     * @return true if the element is not displayed or the element doesn't exist or stale element
     */
    private static ExpectedCondition<Boolean> invisibilityOfElement(
            final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return !(element.isDisplayed());
                } catch (org.openqa.selenium.NoSuchElementException | StaleElementReferenceException e) {
                    // Returns true because the element is not present in DOM. The
                    // try block checks if the element is present but is invisible.
                    return true;
                }
            }

            @Override
            public String toString() {
                return "element to no longer be visible: " + element.getClass().toString();
            }
        };
    }

    public static boolean waitForTextPresent(WebDriver driver, final By by, final String text,
                                             int timeOutInSeconds) {
        boolean isPresent = false;
        try {
            nullifyImplicitWait(driver);

            new WebDriverWait(driver, timeOutInSeconds) {
            }.until((ExpectedCondition<Boolean>) driverObject -> isTextPresent(driverObject, by, text));
            isPresent = isTextPresent(driver, by, text);

            resetImplicitWait(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isPresent;
    }

    public static boolean waitForTextPresentByWebElement(WebDriver driver, final WebElement element,
                                                         final String text, int timeOutInSeconds) {
        boolean isPresent = false;
        try {
            nullifyImplicitWait(driver);

            new WebDriverWait(driver, timeOutInSeconds) {
            }.until((ExpectedCondition<Boolean>) driverObject -> isTextPresent(driverObject, element, text));
            isPresent = isTextPresent(driver, element, text);

            resetImplicitWait(driver);
        } catch (Exception e) {
            Assert.fail(String.format("Timeout when waiting %d seconds for '%s' ", timeOutInSeconds,
                    element.getClass()));
        }
        return isPresent;
    }

    public static boolean waitForTextPresent(WebDriver driver, final WebElement element,
                                             final String text) {
        return waitForTextPresentByWebElement(driver, element, text, DEFAULT_WAIT_FOR_ELEMENT);
    }

    public static boolean waitForJavaScriptCondition(WebDriver driver, final String javaScript,
                                                     int timeOutInSeconds) {
        boolean jsCondition = false;
        try {
            nullifyImplicitWait(driver);

            new WebDriverWait(driver, timeOutInSeconds) {
            }.until((ExpectedCondition<Boolean>) driverObject -> (Boolean)
                    (driverObject != null ? ((JavascriptExecutor) driverObject).executeScript(javaScript) : null));
            jsCondition = (Boolean) ((JavascriptExecutor) driver).executeScript(javaScript);

            resetImplicitWait(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsCondition;
    }

    public static void waitForJavaScriptToExecute(WebDriver driver, String javaScript,
                                                  String elementId) {
        ((WebDriverExtended) driver).executeScript(javaScript);
        waitForElementIsDisplayedNoException(driver, By.className(elementId));
    }

    public static boolean waitForJQueryProcessing(WebDriver driver, int timeOutInSeconds) {
        boolean jqCondition = false;
        try {
            nullifyImplicitWait(driver);

            new WebDriverWait(driver, timeOutInSeconds) {
            }.until((ExpectedCondition<Boolean>) driverObject -> (Boolean)
                    (driverObject != null ? ((JavascriptExecutor) driverObject)
                            .executeScript("return jQuery.active == 0") : null));
            jqCondition = (Boolean) ((JavascriptExecutor) driver)
                    .executeScript("return jQuery.active == 0");

            resetImplicitWait(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jqCondition;
    }

    public static void nullifyImplicitWait(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(0, SECONDS);
    }

    public static void resetImplicitWait(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_FOR_ELEMENT, SECONDS);
    }

    private static boolean isTextPresent(WebDriver driver, By by, String text) {
        try {
            return driver.findElement(by).getText().contains(text);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private static boolean isTextPresent(WebDriver driver, WebElement element, String text) {
        try {
            return element.getText().contains(text);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean isAlertPresent(WebDriver driver) {
        try {
            driver.switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean waitForDivBlockerEnabled(WebDriverExtended driver, WebElement we) {
        boolean enabled = false;
        long initialTime = System.currentTimeMillis();
        long currentTime;
        do {
            if (GeneralPage.divBlockerList(driver).size() > 1) {
                enabled = true;
                break;
            }
            currentTime = System.currentTimeMillis();
        }
        while (TimeUnit.MILLISECONDS.toSeconds(currentTime - initialTime)
                < DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
        return enabled;
    }


    public static void waitForLoadingProgressNotDisplayed(WebDriverExtended driver) {
        WebElement we;
        try {
            we = GeneralPage.loadingProgress(driver);
            if (we.isDisplayed()) {
                waitForLocatedElementIsNotDisplayed(driver, we,
                        DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR);
            }
        } catch (Exception e) {
            sleep(5000);
        }
    }
}
