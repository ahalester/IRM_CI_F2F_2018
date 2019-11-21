package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT5932TestCase01 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://edna.sco.alma.cl:8080/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT5932TestCase01() throws Exception {
    driver.get(baseUrl + "/protrack/");
    driver.findElement(By.id("zk_c_12-cnt")).click();
    driver.findElement(By.id("zk_c_85")).clear();
    driver.findElement(By.id("zk_c_85")).sendKeys("uid://A001/X147/X126");
    driver.findElement(By.id("zk_c_157-cm")).click();
    driver.findElement(By.id("zk_c_98")).click();
    try {
      assertEquals("QA2 status", driver.findElement(By.xpath("//*[@id='zk_c_33']/div/div/div/div/div[3]/div/div/div/table/tbody/tr/td/table/tbody/tr[1]/td/div/div[2]/div/div[3]/table/tbody[1]/tr[3]/td[1]/div/span")).getText());
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
