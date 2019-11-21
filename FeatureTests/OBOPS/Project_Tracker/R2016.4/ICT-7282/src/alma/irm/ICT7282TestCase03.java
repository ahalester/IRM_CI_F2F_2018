package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT7282TestCase03 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://2016-04.asa-test.alma.cl/protrack";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testICT7282TestCase03() throws Exception {
    driver.get("https://2016-04.asa-test.alma.cl/protrack/");
    driver.findElement(By.id("zk_c_410-inp")).click();
    driver.findElement(By.cssSelector("#zk_c_410-sel > div.z-chosenbox-option")).click();
    driver.findElement(By.id("zk_c_460")).click();
    driver.findElement(By.id("zk_c_2911-cave")).click();
    driver.findElement(By.cssSelector("span.z-treecell-text")).click();
    assertEquals("Priority Grade", driver.findElement(By.xpath("//*[@id='zk_c_1100']")).getText());
    assertEquals("A", driver.findElement(By.xpath("//*[@id='zk_c_1101']")).getText());
    assertEquals("Rank", driver.findElement(By.xpath("//*[@id='zk_c_1102']")).getText());
    assertEquals("1", driver.findElement(By.xpath("//*[@id='zk_c_1103']")).getText());
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
