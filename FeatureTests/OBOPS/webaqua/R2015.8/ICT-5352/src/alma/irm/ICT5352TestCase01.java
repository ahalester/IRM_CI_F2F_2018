package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT5352TestCase01 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://2015-08.asa-test.alma.cl/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT5352TestCase01() throws Exception {
    driver.get(baseUrl + "/webaqua/");
    driver.findElement(By.id("zk_c_38-cnt")).click();
    driver.findElement(By.id("zk_c_86-btn")).click();
    driver.findElement(By.id("zk_c_330")).click();
    driver.findElement(By.cssSelector("i.z-datebox-icon.z-icon-calendar")).click();
    driver.findElement(By.id("_z_0-tm")).click();
    driver.findElement(By.cssSelector("#_z_0-left > i.z-icon-caret-left")).click();
    driver.findElement(By.id("_z_0-m0")).click();
    driver.findElement(By.xpath("//tr[@id='_z_0-w0']/td[5]")).click();
    driver.findElement(By.id("zk_c_102")).clear();
    driver.findElement(By.id("zk_c_102")).sendKeys("2013.1.00725.S");
    driver.findElement(By.id("zk_c_246-cave")).click();
    driver.findElement(By.id("zk_c_206")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [selectFrame | fbMainContainer | ]]
    driver.findElement(By.id("fbCloseButton")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [selectWindow | null | ]]
    driver.findElement(By.id("zk_c_365")).click();
    driver.findElement(By.id("zk_c_470-cnt")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [selectFrame | fbMainContainer | ]]
    driver.findElement(By.id("fbInspectButton")).click();
    assertEquals("8.87min (parallactic range: 0.05863 deg)", driver.findElement(By.id("zk_c_2257")).getText());
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
