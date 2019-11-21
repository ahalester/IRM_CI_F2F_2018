package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT6357TestCase01 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://2016-03.asa-test.alma.cl/webaqua";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT6357TestCase01() throws Exception {
    driver.get(baseUrl + "/webaqua/");
    driver.findElement(By.id("zk_c_317")).click();
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_301']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_303']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_311']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_315']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_629']")));
    assertEquals("ExecBlock Status", driver.findElement(By.xpath("//*[@id='zk_c_631']")).getText());
    assertEquals("Antennas:", driver.findElement(By.xpath("//*[@id='zk_c_635']")).getText());
    assertEquals("Array:", driver.findElement(By.xpath("//*[@id='zk_c_638']")).getText());
    assertEquals("Correlator:", driver.findElement(By.xpath("//*[@id='zk_c_641']")).getText());
    assertEquals("Representative frequency", driver.findElement(By.xpath("//*[@id='zk_c_644']")).getText());
    assertEquals("QA0 Status", driver.findElement(By.xpath("//*[@id='zk_c_649']")).getText());
    assertEquals("Execution fraction", driver.findElement(By.xpath("//*[@id='zk_c_654']")).getText());
    assertEquals("Final QA0 Comment", driver.findElement(By.xpath("//*[@id='zk_c_657']")).getText());
    assertEquals("Times on Sources", driver.findElement(By.xpath("//*[@id='zk_c_700-cave']")).getText());
    assertEquals("Comments", driver.findElement(By.xpath("//*[@id='zk_c_670-cave']")).getText());
    assertEquals("Attachments", driver.findElement(By.xpath("//*[@id='zk_c_677-cave']")).getText());
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
