package cl.apa.web.utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import cl.apa.web.delegates.WebDriverExtended;

public class AuthUtil {

    public static void skipNativeAuthDialog(WebDriverExtended driver, String user, String pass) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.authenticateUsing(new UserAndPassword(user, pass));
    }
}
