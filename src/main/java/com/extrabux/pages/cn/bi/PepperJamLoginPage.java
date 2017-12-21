package com.extrabux.pages.cn.bi;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;

public class PepperJamLoginPage extends BasePage {	

	private static final Log LOG = LogFactory.getLog(PepperJamLoginPage.class);
	
	WebElement email;
	
	WebElement passwd;
	
	@FindBy(className = "button")
	WebElement loginButton;
	
	public PepperJamLoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getPepperJamLoginUrl(){
		return "http://www.pepperjamnetwork.com/login.php";
	}
	
	public PepperJamHomePage login(String username,String pw){
		email.sendKeys(username);
		passwd.sendKeys(pw);
		loginButton.click();
		
		LOG.info("Logged in pepperjam successfull。。。。。。");
		return new PepperJamHomePage(driver);
	}
}
