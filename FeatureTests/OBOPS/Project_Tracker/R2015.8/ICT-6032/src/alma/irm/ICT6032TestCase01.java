package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT6032TestCase01 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://2015-08.asa-test.alma.cl/protrack/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT6032TestCase01() throws Exception {
    driver.get("https://2015-08.asa-test.alma.cl/protrack/");
    driver.findElement(By.xpath("//*[@id='zk_c_8']")).clear();
    driver.findElement(By.xpath("//*[@id='zk_c_8']")).sendKeys("2015.1.00502.S");
    driver.findElement(By.xpath("//*[@id='zk_c_8']")).sendKeys(Keys.ENTER);
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if ("1 Projects found".equals(driver.findElement(By.xpath("//*[@id='zk_c_23']")).getText())) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    assertEquals("2015.1.00502.S", driver.findElement(By.xpath("//*[@id='zk_c_32']/table/tbody/tr/td/table/tbody/tr[1]/td/div/div[3]/table/tbody[1]/tr/td[1]/div")).getText());
    driver.findElement(By.xpath("//*[@id='zk_c_975']/div[2]/div[1]/div[1]/div[1]/div[3]/table/tbody/tr[7]/td[1]/div")).click();
    driver.findElement(By.xpath("//*[@id='zk_c_689']/div/div/div/table/tbody/tr/td/table/tbody/tr[5]/td/table/tbody/tr/td/table/tbody/tr[3]/td/button")).click();
    try {
      assertTrue(isElementPresent(By.id("zk_c_201")));
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
