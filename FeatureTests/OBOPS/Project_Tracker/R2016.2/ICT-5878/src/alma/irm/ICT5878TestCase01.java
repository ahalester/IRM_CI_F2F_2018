package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT5878TestCase01 {
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
  public void testICT5878TestCase01() throws Exception {
    driver.get(baseUrl + "/protrack/");
    driver.findElement(By.id("zk_c_88")).clear();
    driver.findElement(By.id("zk_c_88")).sendKeys("GRB_b_03_TE");
    driver.findElement(By.id("zk_c_114")).click();
    driver.findElement(By.cssSelector("#zk_c_325-a > span.z-menuitem-text")).click();
    driver.findElement(By.id("zk_c_450")).clear();
    driver.findElement(By.id("zk_c_450")).sendKeys("ICT-5878");
    driver.findElement(By.id("zk_c_452")).click();
    assertEquals("WaitingForP2G", driver.findElement(By.id("zk_c_1493-cave")).getText());
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
