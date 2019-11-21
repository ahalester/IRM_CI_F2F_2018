package de.obopstest.web.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class WebdriverUtil {

    private static Logger LOG = LoggerFactory.getLogger(WebdriverUtil.class);

    /**
     *
     * @param webDriver
     * @return browser console logs
     */
    public static String getBrowserLog(WebDriver webDriver) {
        try {
            LogEntries logEntries = webDriver.manage().logs().get(LogType.BROWSER);
            if (logEntries != null) {
                StringBuilder logBuilder = new StringBuilder();
                for (LogEntry logEntry : logEntries)
                    logBuilder.append(new Date(logEntry.getTimestamp())).append(": ").append(logEntry.getMessage()).append("\r\n");

                return logBuilder.toString();
            }
            return null;
        } catch (Exception e) {
            LOG.info("Could not generate browser console logs");
            LOG.info(e.getMessage());
            return null;
        }

    }

}
