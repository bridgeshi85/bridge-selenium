package com.extrabux.pages.cn;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.SignUpPage;
import com.extrabux.pages.cn.mvp.MvpDaigouHomePageAfterLogin;
import com.extrabux.pages.cn.qa.BindUserPage;
import com.extrabux.util.WebDriverUtil;

public class ChinaSignUpPage extends SignUpPage {

	private static final Log LOG = LogFactory.getLog(ChinaSignUpPage.class);

	@FindBy(css="#register-form #email")
	WebElement email;
	
	@FindBy(css="#register-form #password")
	WebElement password;
	
	@FindBy(css="#register-form #password_confirm")
	WebElement password_confirm;
	
	@FindBy(css="#register-form .submit")
	WebElement submitButton;
	
	@FindBy(css=".ckb-friend-code")
	WebElement friendCheckbox;
	
	@FindBy(name="referee_email")
	WebElement friendEmail;
	
	@FindBy(css="#register-form #captcha")
	WebElement captcha;
	
	@FindBy(css = "div.register-box .error")
	List<WebElement> errors;
	
	@FindBy(css = "div.register-box .message.error")
	WebElement formError;
	
	public ChinaSignUpPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);		
	}

	public ChinaHomePageAfterLogin signUp(String username, String pw, String confirmPw,String captcha) {
		fillForm(username, pw, confirmPw);
		typeCaptcha(captcha);
		return submit();
	}

	public ChinaHomePageAfterLogin signUpWithRefferal(String username, String pw, String confirmPw, String referral,String captcha) {
		fillForm(username, pw, confirmPw);
		typeCaptcha(captcha);
		checkFriendInvited();
		LOG.info("entering referral code/email: " + referral);
		friendEmail.sendKeys(referral);
		return submit();
	}
	
	public ChinaHomePageAfterLogin submit() {
		
		submitButton.sendKeys(Keys.ENTER);
		
		//submitButton.click();
		return new ChinaHomePageAfterLogin(driver);
	}
	
	public MvpDaigouHomePageAfterLogin signUpFromMvp(String username, String pw, String confirmPw) {
		fillForm(username, pw, confirmPw);
		return submitFromMvp();
	}
	
	public MvpDaigouHomePageAfterLogin submitFromMvp() {
		//submitButton.click();
		submitButton.sendKeys(Keys.ENTER);
		return new MvpDaigouHomePageAfterLogin(driver);
	}
	
	public BindUserPage signUpFromQA(String username, String pw, String confirmPw) {
		fillForm(username, pw, confirmPw);
		return submitFromQA();
	}
	
	public BindUserPage submitFromQA() {
		
		submitButton.sendKeys(Keys.ENTER);
		
		//submitButton.click();
		return new BindUserPage(driver);
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
	
	public void typeCaptcha(String captcha) {
		WebDriverUtil.waitForElementPresentAndVisible(driver, By.cssSelector("#register-form #captcha"), 15);
		this.captcha.sendKeys(captcha);
	}
	
	public void checkFriendInvited() {
		friendCheckbox.click();
	}
	
	public boolean verifyErrorsOnPage(Map<String, String> expectedErrors) {
		// first assert the number of errors found on the page is as expected
		Map<String, String> errorsOnPage = getErrors();
		if (!(errorsOnPage.size() == expectedErrors.size())) {
			return false;
		}
		if (!errorsOnPage.equals(expectedErrors)) {
			return false;
		}
		return true;
	}
	
	public Map<String, String> getErrors() {
		Map<String, String> errorMap = new HashMap<String, String>();
		if (!errors.isEmpty()) {
			for (WebElement error : errors) {
				if (error.getAttribute("for") != null) {
					String type = error.getAttribute("for");
					errorMap.put(type, error.getText());
				} else if (driver.findElements(By.cssSelector("div.register-box .message.error")).size() != 0) {
					errorMap.put("form", formError.getText());
				}
			}
		} else if (driver.findElements(By.cssSelector("div.register-box .message.error")).size() != 0) {
			errorMap.put("form", formError.getText());
		}
		return errorMap;
	}
}
