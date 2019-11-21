package de.aqua.web.utils;

import cucumber.api.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;

import de.aqua.web.delegates.WebDriverExtended;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class CaptureScreenshotUtil {

    public static Logger LOG = LoggerFactory.getLogger(CaptureScreenshotUtil.class);

    public static void captureScreenshot(WebDriverExtended driver, String childClassName,
                                         Scenario scenario) {
        try {
            // Make sure that the directory is there
//            new File("target/surefire-reports/").mkdirs();
            new File("target/surefire-reports/");
            String fileName = "target/surefire-reports/screenshot-" + childClassName + "_" +
                    System.currentTimeMillis() + ".png";
            FileOutputStream out = new FileOutputStream(fileName);
            final byte[] screenshot = driver.getScreenshot();
            out.write(screenshot);
            scenario.embed(screenshot, fileName); //stick it in the report
            out.close();
        } catch (Exception e) {
            LOG.info("Could not take the screenshot");
        }

//        try {
//            // Make sure that the directory is there
//            new File("target/surefire-reports/").mkdirs();
//            FileOutputStream out = new FileOutputStream(
//                    "target/surefire-reports/screenshot-" + childClassName + "_" + System.currentTimeMillis() + ".png");
//            out.write(driver.getScreenshot());
//            out.close();
//        } catch (Exception e) {
//            LOG.info("Could not take the screenshot");
//        }
    }
}

