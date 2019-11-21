package cl.apa.web.utils;

import cl.apa.web.delegates.WebDriverExtended;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by bdumitru on 7/24/2017.
 */
@SuppressWarnings("unused")
public class RuntimeUtil {

    private static Logger LOG = LoggerFactory.getLogger(RuntimeUtil.class);

    public static int itemIndex = 0;
    public static String buffer = "";
    public static String userSearchName = "";
    public static Boolean emptyFlag = false;
    public static Boolean awaitingDecision = false;
    public static List<String> runtimeList = new ArrayList<>();
    public static Map<String, String> bufferMap = new HashMap<>();

    public static void analyzeLog(WebDriverExtended driver) {
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry : logEntries.getAll()) {
            LOG.info(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
        }
    }

    static void highlightElement(WebDriverExtended driver, WebElement element,
                                 final int duration) {
        String originalStyle = element.getAttribute("style");

        driver.executeScript(
                "arguments[0].setAttribute(arguments[1], arguments[2])",
                element,
                "style",
                "border: 2px solid red; border-style: solid;");

        if (duration > 0) {
            WaitUtil.sleep(duration * 100);
            driver.executeScript(
                    "arguments[0].setAttribute(arguments[1], arguments[2])",
                    element,
                    "style",
                    originalStyle);
        }
    }
}
