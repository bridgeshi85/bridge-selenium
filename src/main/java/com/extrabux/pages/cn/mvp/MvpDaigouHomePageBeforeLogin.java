package com.extrabux.pages.cn.mvp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.ChinaHomePageBeforeLogin;
import com.extrabux.pages.cn.ChinaLoginPage;
import com.extrabux.pages.cn.ChinaSignUpPage;
import com.extrabux.util.WebDriverUtil;

public class MvpDaigouHomePageBeforeLogin extends ProductsListPage{

	// login link
	@FindBy(linkText="登录")
	WebElement login;
	
	// login link
	@FindBy(linkText="免费注册")
	WebElement signUp;
	
	private static final Log LOG = LogFactory.getLog(MvpDaigouHomePageBeforeLogin.class); 
	
	public MvpDaigouHomePageBeforeLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public ChinaLoginPage clickLogin() {
		WebDriverUtil.waitForElementPresent(driver, By.linkText("登录"), 5);
		LOG.info("Click login link....");
		login.click();
		return new ChinaLoginPage(driver);
	}
	
	public ChinaSignUpPage clickSignUp() {
		WebDriverUtil.waitForElementPresent(driver, By.linkText("免费注册"), 5);
		LOG.info("Click sign up link....");
		signUp.click();
		return new ChinaSignUpPage(driver);
	}
	
	public ChinaHomePageBeforeLogin enterExtrabuxHomePageBeforeLogin(){
		extrabuxHomePage.click();
		LOG.info("Enter extrabux homepage");
		return new ChinaHomePageBeforeLogin(driver);
	}
	
	public boolean verifyNotLoggedIn() {
		// make sure the logged in element isn't there
		WebDriverUtil.waitForElementPresent(driver, By.id("v2Header-bg"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.linkText("登录"));
	}
}
