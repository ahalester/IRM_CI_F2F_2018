package de.aqua.web.utils;

import de.aqua.web.delegates.WebDriverExtended;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static de.aqua.web.utils.WaitUtil.sleep;

/**
 * Created by bdumitru on 8/8/2017.
 */
@SuppressWarnings("unused")
public class BrowserUtil {

    private static String parentWindowHandler = null;

    public static void switchWindow(WebDriverExtended driver) {

        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
        //  driver.manage().window().maximize();
    }

    public static void switchWindowByTitle(WebDriverExtended driver, String pageTitle) {
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
            //  driver.manage().window().maximize();
            if (!driver.getTitle().equals(pageTitle)) {
                parentWindowHandler = driver.getWindowHandle();
            } else
                break;
        }
    }

    public static void switchToParentWindow(WebDriverExtended driver) {
        driver.switchTo().window(parentWindowHandler);
    }

    public static void switchWindowByTitleAndWait(WebDriverExtended driver, String pageTitle,
                                                  long timeout) {

        Boolean loaded = false;
        long startTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        long currTime;
        do {
            for (String winHandle : driver.getWindowHandles()) {
                driver.switchTo().window(winHandle);
                if (driver.getTitle().equals(pageTitle)) {
                    loaded = true;
                    break;
                }
            }
            if (loaded) {
                //  driver.manage().window().maximize();
                break;
            }
            sleep(2000);
            currTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        }
        while (currTime - startTime <= timeout);
    }

    public static void resize(WebDriverExtended driver, WebElement elementToResize, int xOffset,
                              int yOffset) {
        try {
            if (elementToResize.isDisplayed()) {
                Actions action = new Actions(driver);
                action.clickAndHold(elementToResize).moveByOffset(xOffset, yOffset).release().build().perform();
            } else {
                System.out.println("Element was not displayed to drag");
            }
        } catch (StaleElementReferenceException e) {
            System.out.println("Element with " + elementToResize + "is not attached to the page document " + e.getStackTrace());
        } catch (NoSuchElementException e) {
            System.out.println("Element " + elementToResize + " was not found in DOM " + e.getStackTrace());
        } catch (Exception e) {
            System.out.println("Unable to resize" + elementToResize + " - " + e.getStackTrace());
        }
    }

}
