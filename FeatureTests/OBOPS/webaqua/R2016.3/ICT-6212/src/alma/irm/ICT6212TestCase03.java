package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT6212TestCase03 {
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
  public void testICT6212TestCase03() throws Exception {
    driver.get(baseUrl + "/webaqua/");
    driver.findElement(By.id("zk_c_76-cave")).click();
    driver.findElement(By.id("zk_c_104")).clear();
    driver.findElement(By.id("zk_c_104")).sendKeys("uid://A002/Xb20949/X17d");
    driver.findElement(By.id("zk_c_57-real")).click();
    driver.findElement(By.id("zk_c_65-real")).click();
    driver.findElement(By.id("zk_c_70-real")).click();
    driver.findElement(By.id("zk_c_1505-cave")).click();
    driver.findElement(By.id("zk_c_1498-cnt")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [selectFrame | fbMainContainer | ]]
    driver.findElement(By.id("fbInspectButton")).click();
    driver.findElement(By.cssSelector("menuitem")).click();
    driver.findElement(By.id("zk_c_60")).click();
    assertTrue(isElementPresent(By.id("zk_c_1300-chdextr")));
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
