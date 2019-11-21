package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT7043TestCase01 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://2016-04.asa-test.alma.cl/webaqua";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT7043TestCase01() throws Exception {
    driver.get(baseUrl + "/webaqua/");
    driver.findElement(By.id("zk_c_105")).click();
    driver.findElement(By.xpath("//*[@id='zk_c_109']")).clear();
    driver.findElement(By.xpath("//*[@id='zk_c_109']")).sendKeys("uid://A002/xb2b000/X84e4");
    driver.findElement(By.id("zk_c_219")).click();
    driver.findElement(By.id("zk_c_397")).click();
    driver.findElement(By.id("zk_c_314-cnt")).click();
    assertEquals("Angular Resolution [arcsec]", driver.findElement(By.xpath("//*[@id='zk_c_368']")).getText());
    assertEquals("0.500", driver.findElement(By.xpath("//*[@id='zk_c_369']")).getText());
    assertEquals("Expected Angular Resolution [arcsec]", driver.findElement(By.xpath("//*[@id='zk_c_371']")).getText());
    assertEquals("0.630", driver.findElement(By.xpath("//*[@id='zk_c_372']")).getText());
    assertEquals("Maximum Recoverable Scale [arcsec]", driver.findElement(By.xpath("//*[@id='zk_c_374']")).getText());
    assertEquals("5.053", driver.findElement(By.xpath("//*[@id='zk_c_375']")).getText());
    assertEquals("Expected Maximum Recoverable Scale [arcsec]", driver.findElement(By.xpath("//*[@id='zk_c_377']")).getText());
    assertEquals("5.900", driver.findElement(By.xpath("//*[@id='zk_c_378']")).getText());
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
