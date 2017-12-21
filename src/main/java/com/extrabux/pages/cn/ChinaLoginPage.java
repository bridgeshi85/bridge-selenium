package com.extrabux.pages.cn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.LoginPage;
import com.extrabux.pages.cn.migrate.ChinaMigratePage;
import com.extrabux.pages.cn.mvp.MvpDaigouHomePageAfterLogin;
import com.extrabux.pages.cn.qa.QAHomePageAfterLogin;
import com.extrabux.util.WebDriverUtil;


public class ChinaLoginPage extends LoginPage {	
	static final String LOGIN_URI = "/users/login";
	private static final Log LOG = LogFactory.getLog(ChinaLoginPage.class);

	// Common elements
	WebElement email;
	WebElement password;
	WebElement phone;
	// ExtraBux elements	
	@FindBy(id="submitButton")
	WebElement submitButton;
	// Facebook elements
	WebElement facebookConnectLink;
	WebElement pass;
	WebElement persistBox;
	WebElement login;
	@FindBy(id = "u_0_1")
	WebElement facebookSubmit;

	
	@FindBy(className="link-login-phone")
	WebElement phoneLoginLink;
	
	@FindBy(css="input.button.btn-send-phone-code")
	WebElement sendCodeBtn;
	
	@FindBy(css="#login-phone-form #password")
	WebElement codeInput;
	
	@FindBy(css="#login-phone-form .submit")
	WebElement phoneSubmitBtn;
	
	public ChinaLoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public ChinaHomePageAfterLogin login(String username, String password) {
		loginNoSubmit(username, password);
		return submit();
	}
	
	
	
	public ChinaMigratePage loginWithEbates(String username, String password) {
		loginNoSubmit(username, password);
		email.submit();
		return new ChinaMigratePage(driver);
	}
	
	public ChinaHomePageAfterLogin submit() {
		//submitButton.click();
		email.submit();
		return new ChinaHomePageAfterLogin(driver);
	}
	
	public ChinaHomePageAfterLogin phoneLoginSubmit() {
		//submitButton.click();
		phoneSubmitBtn.click();
		return new ChinaHomePageAfterLogin(driver);
	}

	public MvpDaigouHomePageAfterLogin loginFromMvp(String username, String password) {
		loginNoSubmit(username, password);
		return submitFromMvp();
	}
	
	public MvpDaigouHomePageAfterLogin submitFromMvp() {
		submitButton.click();
		return new MvpDaigouHomePageAfterLogin(driver);
	}
	
	public QAHomePageAfterLogin submitFromQA() {
		submitButton.click();
		return new QAHomePageAfterLogin(driver);
	}
	
	public QAHomePageAfterLogin loginFromQA(String username, String password) {
		loginNoSubmit(username, password);
		return submitFromQA();
	}
	
	public void loginNoSubmit(String username, String password) {
		typeUsername(username);
		clickPassword();
		typePassword(password);
	}

	public ChinaHomePageAfterLogin phoneQiuckLogin(String phoneNumber,String code){
		phoneLoginLink.click();
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("input.button.btn-send-phone-code"), 15);
		typePhoneNumber(phoneNumber);
		sendCodeBtn.click();
		typeCode(code);
		return phoneLoginSubmit();
	}
	
	public void clickPassword(){
		password.click();
	}
	
	public void typePhoneNumber(String phoneNumber) {
		LOG.debug("typing phone: " + phoneNumber + " into login form.");
		phone.sendKeys(phoneNumber);
	}
	
	public void typeCode(String code) {
		LOG.debug("typing code: " + code + " into login form.");
		codeInput.sendKeys(code);
	}
}
