package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT6619TestCase01 {
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
  public void testICT6619TestCase01() throws Exception {
    driver.get(baseUrl + "/snoopi");
    driver.findElement(By.linkText("Home")).click();
    driver.findElement(By.cssSelector("div.comment")).click();
    driver.findElement(By.linkText("Home")).click();
    driver.findElement(By.xpath("//div[@id='page-wrapper']/div[2]/home-page/div/div[2]/info-widget/div/div/a/div/div/div[2]/div[2]")).click();
    driver.findElement(By.linkText("Home")).click();
    driver.findElement(By.xpath("//div[@id='page-wrapper']/div[2]/home-page/div/div[3]/info-widget/div/div/a/div/div/div[2]/div[2]")).click();
    driver.findElement(By.linkText("Home")).click();
    driver.findElement(By.xpath("//div[@id='page-wrapper']/div[2]/home-page/div/div[4]/info-widget/div/div/a/div/div/div[2]/div[2]")).click();
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
