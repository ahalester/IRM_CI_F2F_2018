package de.obopstest.web.delegates;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class WebDriverExtended extends WebDriverDelegate {

    private String baseUrl;

    public WebDriverExtended(WebDriver webDriver, String baseUrl) {
        super(webDriver);
        this.baseUrl = baseUrl;
    }

    public WebDriverExtended(WebDriver webDriver) {
        super(webDriver);
    }

    public boolean isElementPresent(By by) {
        try {
            findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

//    public void gotoUrl(String url) {
//        gotoUrl(url, false);
//    }

//    public void gotoUrl(String uri, boolean absolute) {
//        String relativeUrl = absolute ? "" : baseUrl;
//        get(relativeUrl + uri);
//    }

    public void gotoUrl(String url) {
        gotoUrl(url, true);
    }

    public void gotoUrl(String uri, boolean absolute) {
        String relativeUrl = absolute ? uri : baseUrl;
        get(relativeUrl + uri);
    }
}
