package alma.aat.archive.web.utils;

import alma.aat.archive.web.delegates.WebDriverExtended;

import java.util.concurrent.TimeUnit;

/**
 * Created by bdumitru on 8/8/2017.
 */
@SuppressWarnings("unused")
public class BrowserUtil {

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
            if (driver.getTitle().equals(pageTitle)) {
                break;
            }
        }
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
            WaitUtil.sleep(2000);
            currTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        }
        while (currTime - startTime <= timeout);
    }

}
