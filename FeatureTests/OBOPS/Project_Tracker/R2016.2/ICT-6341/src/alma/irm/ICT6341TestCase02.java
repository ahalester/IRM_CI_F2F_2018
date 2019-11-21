package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT6341TestCase02 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://2016-02.asa-test.alma.cl/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT6341TestCase02() throws Exception {
    driver.get(baseUrl + "/protrack/");
    driver.findElement(By.xpath("//*[@id='zk_c_12-cnt']")).click();
    driver.findElement(By.id("zk_c_61")).clear();
    driver.findElement(By.id("zk_c_61")).sendKeys("rsoto");
    driver.findElement(By.id("zk_c_110")).click();
    driver.findElement(By.id("zk_c_297-cave")).click();
    driver.findElement(By.id("zk_c_860-real")).clear();
    driver.findElement(By.id("zk_c_860-real")).sendKeys("keflavich");
    driver.findElement(By.id("zk_c_278-cnt")).click();
    driver.findElement(By.id("zk_c_871")).click();
    try {
      assertEquals("Adam Ginsburg", driver.findElement(By.xpath("//*[@id='zk_c_282']")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
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
