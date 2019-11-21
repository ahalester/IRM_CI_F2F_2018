package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT6356TestCase01 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://2016-04.asa-test.alma.cl/webaqua/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT6356TestCase01() throws Exception {
    driver.get(baseUrl + "/webaqua/");
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_40']")));
    assertEquals("Project", driver.findElement(By.xpath("//*[@id='zk_c_42-cave']")).getText());
    assertEquals("SB Name", driver.findElement(By.xpath("//*[@id='zk_c_43-cave']")).getText());
    assertEquals("Duration", driver.findElement(By.xpath("//*[@id='zk_c_44-cave']")).getText());
    assertEquals("Start Time", driver.findElement(By.xpath("//*[@id='zk_c_45-cave']")).getText());
    assertEquals("QA0 State", driver.findElement(By.xpath("//*[@id='zk_c_46-cave']")).getText());
    assertEquals("EB UID", driver.findElement(By.xpath("//*[@id='zk_c_47-cave']")).getText());
    assertEquals("EB Search", driver.findElement(By.xpath("//*[@id='zk_c_50-cnt']")).getText());
    driver.findElement(By.xpath("//*[@id='zk_c_51-cnt']")).click();
    driver.findElement(By.xpath("//*[@id='zk_c_99']")).clear();
    driver.findElement(By.xpath("//*[@id='zk_c_99']")).sendKeys("Example");
    driver.findElement(By.xpath("//*[@id='zk_c_106']")).click();
    assertEquals("", driver.findElement(By.xpath("//*[@id='zk_c_99']")).getText());
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
