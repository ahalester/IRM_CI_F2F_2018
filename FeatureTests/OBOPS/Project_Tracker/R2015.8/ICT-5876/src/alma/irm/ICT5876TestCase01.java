package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT5876TestCase01 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://edna.sco.alma.cl:8080/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT5876TestCase01() throws Exception {
    driver.get(baseUrl + "/protrack/");
    driver.findElement(By.id("zk_c_82")).clear();
    driver.findElement(By.id("zk_c_82")).sendKeys("TW_Hya_a_04_TE");
    driver.findElement(By.id("zk_c_108")).click();
    driver.findElement(By.id("zk_c_11-cnt")).click();
    driver.findElement(By.cssSelector("#zk_c_323-a > span.z-menuitem-text")).click();
    driver.findElement(By.id("zk_c_445")).clear();
    driver.findElement(By.id("zk_c_445")).sendKeys("Testing ICT-5876");
    driver.findElement(By.id("zk_c_447")).click();
    // ERROR: Caught exception [Error: locator strategy either id or name must be specified explicitly.]
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
