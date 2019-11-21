package alma.irm;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ICT7282TestCase02 {
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
  public void testICT7282TestCase02() throws Exception {
    driver.get("https://2016-04.asa-test.alma.cl/protrack/");
    driver.findElement(By.id("zk_c_93-inp")).click();
    driver.findElement(By.cssSelector("#zk_c_93-sel > div.z-chosenbox-option")).click();
    driver.findElement(By.id("zk_c_125")).click();
    assertEquals("A", driver.findElement(By.xpath("//*[@id='zk_c_562-cave']")).getText());
    assertEquals("1", driver.findElement(By.xpath("//*[@id='zk_c_563-cave']")).getText());
    driver.findElement(By.id("zk_c_553-cave")).click();
    driver.findElement(By.cssSelector("span.z-treecell-text")).click();
    assertEquals("Priority Grade", driver.findElement(By.xpath("//*[@id='zk_c_2363']")).getText());
    assertEquals("A", driver.findElement(By.xpath("//*[@id='zk_c_2364']")).getText());
    assertEquals("Rank", driver.findElement(By.xpath("//*[@id='zk_c_2431']")).getText());
    assertEquals("1", driver.findElement(By.xpath("//*[@id='zk_c_2432']")).getText());
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
