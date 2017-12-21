package com.extrabux.pages.cn.bi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;

public class AWLoginPage extends BasePage {	

	private static final Log LOG = LogFactory.getLog(AWLoginPage.class);

	WebElement email;
	
	WebElement password;
	
	@FindBy(id = "login")
	WebElement loginBtn;
	
	public AWLoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void Login(String username,String pw) throws Exception {
		email.sendKeys(username);
		password.sendKeys(pw);
		loginBtn.click();
		LOG.info("Logged in affiliate window successful.....");
	}
	
	public String getAWLoginUrl(){
		return "https://darwin.affiliatewindow.com/login";
	}
}
