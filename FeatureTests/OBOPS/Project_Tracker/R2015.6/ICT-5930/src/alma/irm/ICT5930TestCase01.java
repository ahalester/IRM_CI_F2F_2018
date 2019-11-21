package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT5930TestCase01 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://natalie.sco.alma.cl:8080/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT5930TestCase01() throws Exception {
    driver.get(baseUrl + "/protrack/");
    driver.findElement(By.xpath("//*[@id='zk_c_10-cnt']")).click();
    assertEquals("Reviewed", driver.findElement(By.xpath("//*[@id='zk_c_106']/div/table/tbody[1]/tr[1]/td/div/span[2]")).getText());
    driver.findElement(By.xpath("//*[@id='zk_c_106']/div/table/tbody[1]/tr[1]/td/div")).click();
    driver.findElement(By.id("zk_c_71-inp")).click();
    driver.findElement(By.xpath("//div[@id='zk_c_71-sel']/div[7]")).click();
    driver.findElement(By.xpath("//*[@id='zk_c_100']")).click();
    assertEquals("Reviewed", driver.findElement(By.xpath("//*[@id='zk_c_30']/div/div/div/div/table/tbody/tr/td/table/tbody/tr[1]/td/div/div[3]/table/tbody[1]/tr[1]/td[6]/div")).getText());
    driver.findElement(By.id("zk_c_423-cave")).click();
    assertEquals("Reviewed", driver.findElement(By.xpath("//*[@id='zk_c_34']/div/div[3]/div/div/div/table/tbody/tr/td/table/tbody/tr[1]/td[1]/table/tbody/tr/td/table/tbody/tr[1]/td/div/div[2]/div[1]/div/div[3]/table/tbody[1]/tr[10]/td[2]/div/div/div[1]/span")).getText());
    assertEquals("Reviewed → ObservingTimedOut", driver.findElement(By.xpath("//*[@id='zk_c_382']/ul/li[1]/a/span")).getText());
    assertEquals("Reviewed → Phase1Submitted", driver.findElement(By.xpath("//*[@id='zk_c_382']/ul/li[1]/a/span")).getText());
    assertEquals("Reviewed → Canceled", driver.findElement(By.xpath("//*[@id='zk_c_382']/ul/li[1]/a/span")).getText());
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
