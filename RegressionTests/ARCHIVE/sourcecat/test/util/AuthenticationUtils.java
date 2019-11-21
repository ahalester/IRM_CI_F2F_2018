package util;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

public class AuthenticationUtils {
	private WebDriver webDriver;
	LoadPropFile properties = new LoadPropFile();
	
	public AuthenticationUtils(WebDriver webDriver){
		this.webDriver = webDriver;
	}
	
	public void loginPowerUser() {
		
		Dimension d = new Dimension(1300,1150);
		webDriver.manage().window().setSize(d);		
		webDriver.findElement(By.linkText("Log in")).click();

		String powerUser = properties.getUsername();
		String powerUserPass = properties.getPassword();
		webDriver.findElement(By.name("j_username")).sendKeys(powerUser);
		webDriver.findElement(By.name("j_password")).sendKeys(powerUserPass);

		webDriver.findElement(By.name("submit")).click();
	}
}
