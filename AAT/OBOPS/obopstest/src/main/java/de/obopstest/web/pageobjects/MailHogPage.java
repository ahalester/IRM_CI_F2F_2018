package de.obopstest.web.pageobjects;

import de.obopstest.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by bdumitru on 9/5/2017.
 */
public class MailHogPage {

    public static WebElement email(WebDriverExtended driver, String subject) {
        WebElement element = null;
        List<WebElement> inbox = GeneralPage.visibleElements(driver, "msglist-message");
        for (WebElement msg : inbox) {
            if (msg.findElement(By.className("subject")).getAttribute("innerHTML")
                    .contains(subject)) {
                element = msg.findElement(By.className("subject"));
                break;
            }
        }
        return element;
    }

    public static WebElement emailBody(WebDriverExtended driver) {
        return driver.findElement(By.id("preview-plain"));
    }

    public static Boolean deliveryTimeExists(WebDriverExtended driver, String secondsAgo,
                                             String oneMinuteAgo) {
        List<WebElement> times = GeneralPage.visibleElements(driver, "text-right");
        Boolean exists = false;
        for (WebElement time : times) {
            if (time.getAttribute("innerHTML").contains(secondsAgo)
                    || time.getAttribute("innerHTML").contains(oneMinuteAgo)) {
                exists = true;
                break;
            }
        }
        return exists;
    }
}
