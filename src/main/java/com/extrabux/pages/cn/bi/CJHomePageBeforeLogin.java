package com.extrabux.pages.cn.bi;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class CJHomePageBeforeLogin extends BasePage {	

	@FindBy(name = "pw")
	WebElement passWord;
	
	@FindBy(name = "uname")
	WebElement userName;
	
	@FindBy(css = "a.login-button.fcgreen")
	WebElement loginButton;
	
	@FindBy(className = "login")
	WebElement submitButton;
	
	public CJHomePageBeforeLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public String getAffiliateUrl(){
		return "http://www.cj.com";
	}
	
	public CJHomePageAfterLogin loging(String email,String pw){
		loginButton.click();
		
		WebDriverUtil.waitForElementVisible(driver, userName, 3);
		userName.sendKeys(email);
		passWord.sendKeys(pw);
		
		submitButton.click();
		return new CJHomePageAfterLogin(driver);
	}
}
