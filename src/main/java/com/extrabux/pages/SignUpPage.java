package com.extrabux.pages;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.util.WebDriverUtil;

public class SignUpPage extends BasePage {
	private static final Log LOG = LogFactory.getLog(SignUpPage.class);

	static final String SIGNUP_URI = "/users/register";

	WebElement email;
	WebElement password;
	WebElement password_confirm;
	WebElement submitButton;
	WebElement friendCheckbox;
	WebElement friendEmail;

	String emailDomain = "somewhere.com";
	String pswd = "password";

	Map<String, Object> testContext;

	public SignUpPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);		
	}

	public SignUpPage(WebDriver driver, Map<String, Object> testContext) {
		super(driver);
		this.testContext = testContext;
		PageFactory.initElements(driver, this);	
	}

	public void fillForm(String username, String pw, String confirmPw) {
		LOG.info("attempting to sign up with username: " + username + ", password: " + pw + " and confirm password: "
				+ confirmPw);
		typeUsername(username);
		typePassword(pw);
		typeConfirmPassword(confirmPw);
	}

	public HomePageAfterLogin signUp(String username, String pw, String confirmPw) {
		fillForm(username, pw, confirmPw);
		return submit();
	}

	public HomePageAfterLogin signUpWithRefferal(String username, String pw, String confirmPw, String referral) {
		fillForm(username, pw, confirmPw);
		checkFriendInvited();
		LOG.info("entering referral code/email: " + referral);
		friendEmail.sendKeys(referral);
		return submit();
	}

	public HomePageAfterLogin submit() {
		LOG.debug("clicking submit signup form.");
		WebDriverUtil.waitForElementPresent(driver, By.id("submitButton"), 10);
		submitButton.click();
		//submitButton.click();
		return new HomePageAfterLogin(driver);
	}

	public void typeUsername(String username) {
		LOG.debug("typing username: " + username + " into signup form.");
		email.sendKeys(username);
	}

	public void typePassword(String pswd) {
		LOG.debug("typing password: " + pswd + " into signup form.");
		password.sendKeys(pswd);
	}

	public void typeConfirmPassword(String confirmPswd) {
		password_confirm.sendKeys(confirmPswd);
	}

	public void checkFriendInvited() {
		friendCheckbox.click();
	}

	public String getPswd() {
		return pswd;
	}

	// TODO make this better
	public String getUrl(String serverName) {
		return "https://" + serverName + SIGNUP_URI;
	}

}
