package com.extrabux.pages.cn.promotion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.ChinaHomePageBeforeLogin;
import com.extrabux.util.WebDriverUtil;

public class VisaPromotionPageBeforeLogin extends ChinaHomePageBeforeLogin{
	static final String URI = "/visa";
	
	private static final Log LOG = LogFactory.getLog(VisaPromotionPageBeforeLogin.class);
	
	WebElement email;
	
	WebElement password;
	
	WebElement signupCode;
	
	@FindBy(css="input.startSavingButton.orangeButton.shadowedButton.gradient")
	WebElement submitButton;
	
	public VisaPromotionPageBeforeLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);		
	}
	
	public String getUrl(String serverName) {
		return "http://" + serverName + URI;
	}
	
	public VisaPromotionPageAfterLogin signUp(String username, String pw, String signUpCode) {
		fillForm(username, pw, signUpCode);
		return submit();
	}

	public VisaPromotionPageAfterLogin submit(){
		//submitButton.click();
		//click sometime dosen't work,try use enter key
		submitButton.sendKeys(Keys.ENTER);
		return new VisaPromotionPageAfterLogin(driver);
	}

	public void fillForm(String username, String pw, String signUpCode) {
		LOG.info("attempting to sign up/in with username: " + username + ", password: " + pw + " and signUpCode: "
				+ signUpCode);
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("#box"), 15);
		typeUsername(username);
		typePassword(pw);
		if(signUpCode != "")
			typeSignUpCode(signUpCode);
	}
	
	public void typeUsername(String username) {
		email.clear();
		email.sendKeys(username);
	}

	public void typePassword(String pswd) {
		password.clear();
		password.sendKeys(pswd);
	}
	
	protected void typeSignUpCode(String signUpCode) {
		signupCode.clear();
		signupCode.sendKeys(signUpCode);
	}
}