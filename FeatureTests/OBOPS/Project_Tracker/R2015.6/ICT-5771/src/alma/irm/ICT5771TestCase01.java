package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT5771TestCase01 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://2015-06-docker.asa-test.alma.cl/protrack/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT5771TestCase01() throws Exception {
    driver.get(baseUrl + "/protrack/");
    driver.findElement(By.xpath("//*[@id='zk_c_10-cnt']")).click();
    driver.findElement(By.id("zk_c_96-real")).click();
    driver.findElement(By.id("zk_c_100")).click();
    driver.findElement(By.xpath("//*[@id='zk_c_30']/div[1]/div[1]/div[1]/div[1]/table/tbody/tr/td/table/tbody/tr[1]/td/div/div[3]/table[1]/tbody/tr[1]/td[1]")).click();
    assertEquals("Ph1m Priority Grade", driver.findElement(By.xpath("//*[@id='zk_c_30']/div[2]/div/div/div/div/div[3]/div/div/div/table/tbody/tr/td/table/tbody/tr[1]/td[1]/table/tbody/tr/td/table/tbody/tr[1]/td/div/div[2]/div[1]/div[1]/div[3]/table/tbody/tr[5]/td[1]/div[1]/span")).getText());
    // Warning: assertTextNotPresent may require manual changes
    assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*//[\\s\\S]*\\[@id='zk_c_30'\\]/div\\[2\\]/div/div/div/div/div\\[3\\]/div/div/div/table/tbody/tr/td/table/tbody/tr\\[1\\]/td\\[1\\]/table/tbody/tr/td/table/tbody/tr\\[1\\]/td/div/div\\[2\\]/div\\[1\\]/div\\[1\\]/div\\[3\\]/table/tbody/tr\\[5\\]/td\\[1\\]/div\\[1\\]/span[\\s\\S]*$"));
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
