package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT6212TestCase02 {
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
  public void testICT6212TestCase02() throws Exception {
    driver.get(baseUrl + "/webaqua/");
    driver.findElement(By.id("zk_c_57-real")).click();
    driver.findElement(By.id("zk_c_65-real")).click();
    driver.findElement(By.id("zk_c_76-cave")).click();
    driver.findElement(By.cssSelector("i.z-combobox-icon.z-icon-caret-down")).click();
    driver.findElement(By.id("zk_c_278")).click();
    driver.findElement(By.cssSelector("i.z-datebox-icon.z-icon-calendar")).click();
    driver.findElement(By.id("_z_0-ty")).click();
    driver.findElement(By.id("_z_0-y2")).click();
    driver.findElement(By.id("_z_0-m0")).click();
    driver.findElement(By.xpath("//tr[@id='_z_0-w0']/td[7]")).click();
    driver.findElement(By.cssSelector("#zk_c_91-btn > i.z-datebox-icon.z-icon-calendar")).click();
    driver.findElement(By.xpath("//tr[@id='_z_2-w4']/td[2]")).click();
    driver.findElement(By.id("zk_c_60")).click();
    driver.findElement(By.id("zk_c_224")).click();
    driver.findElement(By.id("zk_c_303-cnt")).click();
    driver.findElement(By.id("zk_c_104")).clear();
    driver.findElement(By.id("zk_c_104")).sendKeys("uid://A002/Xb20a71/X3");
    // ERROR: Caught exception [Error: unknown strategy [class] for locator [class=errornumbers]]
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
