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
    baseUrl = "https://2016-03.asa-test.alma.cl/webaqua";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT6356TestCase01() throws Exception {
    driver.get(baseUrl + "/webaqua/");
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_35-cnt']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_36-cnt']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_37-cnt']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_40']")));
    driver.findElement(By.id("zk_c_449")).click();
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_42']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_43']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_44']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_45']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_46']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_47']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_55']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_63']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_68']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_76-cave']")));
    assertTrue(isElementPresent(By.xpath("//*[@id='zk_c_61']")));
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
