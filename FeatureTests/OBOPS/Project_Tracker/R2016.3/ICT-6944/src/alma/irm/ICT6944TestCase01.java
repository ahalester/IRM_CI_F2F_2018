package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT6944TestCase01 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://2016-03.asa-test.alma.cl/protrack/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT6944TestCase01() throws Exception {
    driver.get(baseUrl + "/protrack/");
    driver.findElement(By.xpath("(//input[@name='file'])[3]")).clear();
    driver.findElement(By.xpath("(//input[@name='file'])[3]")).sendKeys("/home/patricio/workspace_trunk/IRM/FeatureTests/OBOPS/Project_Tracker/R2016.3/ICT-6944/xlst/allStateChanges.csv");
    driver.findElement(By.id("zk_c_79")).clear();
    driver.findElement(By.id("zk_c_79")).sendKeys("uid://A002/X5c0871/X1");
    driver.findElement(By.id("zk_c_121")).click();
    assertEquals("Test User (testuser)", driver.findElement(By.xpath("//*[@id='zk_c_331']")).getText());
    assertEquals("David Rodriguez (drodrigu)", driver.findElement(By.xpath("//*[@id='zk_c_335']")).getText());
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