package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT6945TestCase01 {
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
  public void testICT6945TestCase01() throws Exception {
    assertEquals("P2G Attention Flag", driver.findElement(By.xpath("//*[@id='zk_c_101']")).getText());
    assertEquals("P2G Attention Reasons", driver.findElement(By.xpath("//*[@id='zk_c_104']")).getText());
    assertEquals("P2G UserId", driver.findElement(By.xpath("//*[@id='zk_c_107']")).getText());
    driver.findElement(By.xpath("//*[@id='zk_c_10-cnt']")).click();
    driver.findElement(By.xpath("//*[@id='zk_c_160-cm']")).click();
    assertEquals("P2G Attention", driver.findElement(By.xpath("//*[@id='zk_c_190-cave']")).getText());
    assertEquals("P2G Attention Reasons", driver.findElement(By.xpath("//*[@id='zk_c_191-cave']")).getText());
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
