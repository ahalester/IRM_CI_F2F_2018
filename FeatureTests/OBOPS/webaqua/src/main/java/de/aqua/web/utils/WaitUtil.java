package de.aqua.web.utils;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import de.aqua.web.delegates.WebDriverExtended;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class WaitUtil {

    private static Logger LOG = LoggerFactory.getLogger(WaitUtil.class);

    public static final int DEFAULT_WAIT_FOR_PAGE_LOAD = 20;
    public static final int DEFAULT_WAIT_FOR_ELEMENT = 10;
    public static final int DEFAULT_WAIT_FOR_ELEMENT_TO_DISAPPEAR = 40;

    public static void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static WebElement waitUntilVisible(WebDriver driver, WebElement element) {
        WebDriverWait waiting = new WebDriverWait(driver, DEFAULT_WAIT_FOR_ELEMENT);
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
            e.printStackTrace();
        }
        return element;
    }

    public static WebElement waitForElement(WebDriver driver, final By by) {
        return waitForElement(driver, by, DEFAULT_WAIT_FOR_ELEMENT);
    }

    public static WebElement waitForElementPresent(WebDriver driver, final By by, int timeOutInSeconds) {
        WebElement element = null;
        try {
            nullifyImplicitWait(driver);

            element = new WebDriverWait(driver, timeOutInSeconds)
                    .until(ExpectedConditions.presenceOfElementLocated(by));

            resetImplicitWait(driver);
        } catch (TimeoutException e) {
            LOG.info("The element was not found in " + timeOutInSeconds + " seconds !");
        } catch (org.openqa.selenium.NoSuchElementException e) {
            LOG.info("The element was not found in " + timeOutInSeconds + " seconds !");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return element;
    }

    public static WebElement waitForElementPresent(WebDriver driver, final By by, WebElement element) {
        // Perform the actions on the new window
        boolean statusInProgress = true;
        long startTime = System.currentTimeMillis();
        long actualTime = 0;
        do {
            WebElement statusElement = null;
            actualTime = System.currentTimeMillis();
            try {
                statusElement = element;
                if (statusElement.equals(null)) {
                    statusInProgress = false;
                    break;
                } else
                    sleep(30000);
            } catch (Exception e) {
                statusInProgress = false;
            }
        } while (statusInProgress
                && TimeUnit.MILLISECONDS.toSeconds(actualTime - startTime) < DEFAULT_WAIT_FOR_ELEMENT);

        Assert.assertTrue(
                String.format("Timeout after waiting '%d' seconds for element " + by + ": " + element,
                        DEFAULT_WAIT_FOR_ELEMENT),
                TimeUnit.MILLISECONDS.toSeconds(actualTime - startTime) < DEFAULT_WAIT_FOR_ELEMENT);
        return element;
    }

    public static WebElement waitForElementPresent(WebDriver driver, final By by) {
        return waitForElementPresent(driver, by, DEFAULT_WAIT_FOR_ELEMENT);
    }

    public static WebElement waitForElementIsDisplayedNoException(WebDriver driver, final By by, int timeOutInSeconds) {
        WebElement element = null;
        try {
            nullifyImplicitWait(driver);

            element = new WebDriverWait(driver, timeOutInSeconds).ignoring(NoSuchElementException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(by));

            resetImplicitWait(driver);
        } catch (Exception e) {
            // Assert.fail(String.format("Timeout when waiting more than '%d'
            // seconds for the element searched '%s'!",
            // timeOutInSeconds, by));
//            LOG.info(String.format("Timeout when waiting more than '%d' seconds for the element searched '%s'!",
//                    timeOutInSeconds, by));
        }
        return element;
    }

    public static WebElement waitForElementIsDisplayedNoException(WebDriver driver, final By by) {
        return waitForElementIsDisplayedNoException(driver, by, DEFAULT_WAIT_FOR_ELEMENT);
    }

    public static WebElement waitForElementIsDisplayed(WebDriver driver, final By by, int timeOutInSeconds) {
        WebElement element = null;
        try {
            nullifyImplicitWait(driver);

            element = new WebDriverWait(driver, timeOutInSeconds).ignoring(NoSuchElementException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(by));

            resetImplicitWait(driver);
        } catch (Exception e) {
            Assert.fail(String.format("Timeout when waiting %d seconds for '%s' ", timeOutInSeconds, by));
        }
        return element;
    }

    public static WebElement waitForElementIsDisplayed(WebDriver driver, final By by) {
        return waitForElementIsDisplayed(driver, by, DEFAULT_WAIT_FOR_ELEMENT);
    }

    public static boolean waitForElementIsNotDisplayed(WebDriver driver, final By by, int timeOutInSeconds) {
        boolean isNotDisplayed = false;
        try {
            nullifyImplicitWait(driver);

            isNotDisplayed = new WebDriverWait(driver, timeOutInSeconds).ignoring(NoSuchElementException.class)
                    .until(ExpectedConditions.invisibilityOfElementLocated(by));

            resetImplicitWait(driver);
        } catch (Exception e) {
            Assert.fail("Timeout");
        }
        return isNotDisplayed;
    }

    public static boolean waitForElementIsNotDisplayedNoException(WebDriver driver, final By by, int timeOutInSeconds) {
        boolean isNotDisplayed = false;
        try {
            nullifyImplicitWait(driver);

            isNotDisplayed = new WebDriverWait(driver, timeOutInSeconds).ignoring(NoSuchElementException.class)
                    .until(ExpectedConditions.invisibilityOfElementLocated(by));

            resetImplicitWait(driver);
        } catch (Exception e) {
//            LOG.info(e);
        }
        return isNotDisplayed;
    }

    public static boolean waitForLocatedElementIsNotDisplayed(WebDriver driver, WebElement element,
                                                              int timeOutInSeconds) {
        boolean isNotDisplayed = false;
        try {
            nullifyImplicitWait(driver);

            isNotDisplayed = new WebDriverWait(driver, timeOutInSeconds).ignoring(NoSuchElementException.class)
                    .until(invisibilityOfElement(element));

            resetImplicitWait(driver);
        } catch (Exception e) {
            Assert.fail(String.format("Timeout waiting for the element identified by class %s " +
                    "to disappear!", element.getClass().toString()));
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
                } catch (org.openqa.selenium.NoSuchElementException e) {
                    // Returns true because the element is not present in DOM. The
                    // try block checks if the element is present but is invisible.
                    return true;
                } catch (StaleElementReferenceException e) {
                    // Returns true because stale element reference implies that element
                    // is no longer visible.
                    return true;
                }
            }

            @Override
            public String toString() {
                return "element to no longer be visible: " + element.getClass().toString();
            }
        };
    }

    public static boolean waitForTextPresent(WebDriver driver, final By by, final String text, int timeOutInSeconds) {
        boolean isPresent = false;
        try {
            nullifyImplicitWait(driver);

            new WebDriverWait(driver, timeOutInSeconds) {
            }.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return isTextPresent(driverObject, by, text);
                }
            });
            isPresent = isTextPresent(driver, by, text);

            resetImplicitWait(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isPresent;
    }

    public static boolean waitForTextPresentByWebElement(WebDriver driver, final WebElement element, final String text,
                                                         int timeOutInSeconds) {
        boolean isPresent = false;
        try {
            nullifyImplicitWait(driver);

            new WebDriverWait(driver, timeOutInSeconds) {
            }.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return isTextPresent(driverObject, element, text);
                }
            });
            isPresent = isTextPresent(driver, element, text);

            resetImplicitWait(driver);
        } catch (Exception e) {
            Assert.fail(
                    String.format("Timeout when waiting %d seconds for '%s' ", timeOutInSeconds, element.getClass()));
        }
        return isPresent;
    }

    public static boolean waitForTextPresent(WebDriver driver, final WebElement element, final String text) {
        return waitForTextPresentByWebElement(driver, element, text, DEFAULT_WAIT_FOR_ELEMENT);
    }

    public static boolean waitForJavaScriptCondition(WebDriver driver, final String javaScript, int timeOutInSeconds) {
        boolean jsCondition = false;
        try {
            nullifyImplicitWait(driver);

            new WebDriverWait(driver, timeOutInSeconds) {
            }.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return (Boolean) ((JavascriptExecutor) driverObject).executeScript(javaScript);
                }
            });
            jsCondition = (Boolean) ((JavascriptExecutor) driver).executeScript(javaScript);

            resetImplicitWait(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsCondition;
    }

    public static void waitForJavaScriptToExecute(WebDriver driver, String javaScript, String elementId) {
        ((WebDriverExtended) driver).executeScript(javaScript);
        waitForElementIsDisplayedNoException(driver, By.className(elementId));
    }

    public static boolean waitForJQueryProcessing(WebDriver driver, int timeOutInSeconds) {
        boolean jQcondition = false;
        try {
            nullifyImplicitWait(driver);

            new WebDriverWait(driver, timeOutInSeconds) {
            }.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return (Boolean) ((JavascriptExecutor) driverObject).executeScript("return jQuery.active == 0");
                }
            });
            jQcondition = (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");

            resetImplicitWait(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jQcondition;
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
}
