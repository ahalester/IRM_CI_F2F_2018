package de.obopstest.web.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Static methods for different wait scenarios.
 */
public final class Wait {
    private static final Logger LOG = getLogger(Wait.class);

    private static final int DELAY = 20;

    public static void waitForElementByCss(WebDriver driver, String elementCss) {
        waitForElement(driver, By.cssSelector(elementCss));
    }

    public static void waitForElement(WebDriver driver, By by) {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    List<WebElement> el = driver.findElements(by);
                    return !el.isEmpty();
                } catch (Exception e) {
                    return false;
                }
            }
        };
        waitFor(DELAY * 1000, callable);
    }

    public static void waitForElement(WebDriver driver, By by, int waitTime) {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    List<WebElement> el = driver.findElements(by);
                    return !el.isEmpty();
                } catch (Exception e) {
                    return false;
                }
            }
        };
        waitFor(waitTime * 1000, callable);
    }


    public static void waitForElementIsNotPresent(WebDriver driver, By by) {
        try {
            new WebDriverWait(driver, DELAY).until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (Exception exception) {
            //silent catch for exception. log is not informative cause will attach class name
        }
        Callable<Boolean> wait = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    return !driver.findElement(by).isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            }
        };
        waitFor(DELAY * 1000, wait);
    }

    public static void waitForElementIsNotPresentCustom(WebDriver driver, By by, int waitTime) {
        try {
            driver.manage().timeouts().implicitlyWait(waitTime, SECONDS);
            new WebDriverWait(driver, waitTime).until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (Exception exception) {
            //silent catch for exception. log is not informative cause will attach class name
        }

        Callable<Boolean> wait = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    driver.manage().timeouts().implicitlyWait(waitTime, SECONDS);
                    return !driver.findElement(by).isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        };
        WaitUtil.resetImplicitWait(driver);
        waitFor(DELAY * 1000, wait);
    }

    public static void waitForElement(WebDriver driver, final WebElement element) {
        try {
            new WebDriverWait(driver, DELAY).until(ExpectedConditions.visibilityOf(element));
        } catch (Exception exception) {
            //silent catch for exception. log is not informative cause will attach class name
        }
        Callable<Boolean> wait = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    return element.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            }
        };
        waitFor(DELAY * 1000, wait);
    }

    public static void sleep(long mils) {
        try {
            Thread.sleep(mils);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
        }
    }

    public static void waitForElementClickable(WebDriver driver, final WebElement element) {
        try {
            new WebDriverWait(driver, DELAY).until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception exception) {
            LOG.error(exception.getMessage());
            LOG.error("Element not found!" + " " + element.toString());
            //Assert.fail("Element not found!" + " " + element.toString());
        }
        Callable<Boolean> wait = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    return element.isEnabled();
                } catch (Exception e) {
                    return false;
                }
            }
        };
        waitFor(DELAY * 1000, wait);
    }

    public static boolean waitFor(Callable<Boolean> function) {
        return waitFor(DELAY*1000, function);
    }

    public static boolean waitFor(long timeoutMillis, Callable<Boolean> function) {
        long start = System.currentTimeMillis();
        long endTime = start + timeoutMillis;
        try {
            while (!Thread.interrupted() && System.currentTimeMillis() < endTime) {
                try {
                    if (function.call()) {
                        return true;
                    }
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
                Thread.sleep(300);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } catch (Error e) {
            LOG.error(e.getMessage());
        }
        return false;
    }

}