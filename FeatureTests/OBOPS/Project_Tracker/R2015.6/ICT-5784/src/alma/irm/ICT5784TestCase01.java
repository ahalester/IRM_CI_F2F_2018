package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT5784TestCase01 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://2015-06.asa-test.alma.cl/protrack/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT5784TestCase01() throws Exception {
    driver.get(baseUrl + "/protrack/");
    driver.findElement(By.id("zk_c_71-inp")).click();
    driver.findElement(By.id("zk_c_71-inp")).clear();
    driver.findElement(By.id("zk_c_71-inp")).sendKeys("2015.1");
    driver.findElement(By.xpath("//div[@id='zk_c_71-sel']/div[11]")).click();
    driver.findElement(By.cssSelector("#zk_c_124-btn > i.z-combobox-icon.z-icon-caret-down")).click();
    driver.findElement(By.cssSelector("span.z-comboitem-text")).click();
    driver.findElement(By.id("zk_c_100")).click();
    try {
      assertEquals("2015.1.00191.S", driver.findElement(By.xpath("//*[@id='zk_c_30']/div/div/div/div/table/tbody/tr/td/table/tbody/tr[1]/td/div/div[3]/table/tbody[1]/tr/td[1]/div")).getText());
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
