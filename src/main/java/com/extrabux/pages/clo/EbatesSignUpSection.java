package com.extrabux.pages.clo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class EbatesSignUpSection extends BasePage {
	private static final Log LOG = LogFactory.getLog(EbatesSignUpSection.class);

	String serverName = "qa-www.ebates.com";
	@FindBy(id = "email_address")
	public WebElement email;
	public WebElement password;
	@FindBy(xpath = "//*[contains(@src, 'button_signup')]")
	public WebElement submitBtn;
	public String emailDomain;
	public String pswd;

	public EbatesSignUpSection(WebDriver driver) {
		super(driver);
	}

	public void waitForJoinForm() {
		LOG.debug("Waiting for the join form to be present");
		WebDriverUtil.waitForElementPresent(driver, By.id("email_address"), 30);
		LOG.debug("Waiting for the join form to be visible");
		WebDriverUtil.waitForElementVisible(driver, email, 15);
	}

	public EbatesHomePage signUp(String username, String pw) {
		fillForm(username, pw);
		return submit();
	}

	public void fillForm(String username, String pw) {
		LOG.info("attempting to sign up with username: " + username + ", password: " + pw);
		typeUsername(username);
		typePassword(pw);
	}

	public EbatesHomePage submit() {
		LOG.debug("clicking submit signup form.");
		submitBtn.click();
		return new EbatesHomePage(driver);
	}

	public void typeUsername(String username) {
		LOG.debug("typing username: " + username + " into signup form.");
		email.clear();
		email.sendKeys(username);
	}

	public void typePassword(String pswd) {
		LOG.debug("typing password: " + pswd + " into signup form.");
		password.clear();
		password.sendKeys(pswd);
	}

	public void waitForEmailField() {
		LOG.debug("Waiting for the email field to be present");
		WebDriverUtil.waitForElementPresent(driver, By.id("email"), 15);
		LOG.debug("Waiting for the email field to be visible");
		WebDriverUtil.waitForElementVisible(driver, email, 90);
	}

	public void waitForPasswordField() {
		LOG.debug("Waiting for the password field to be present");
		WebDriverUtil.waitForElementPresent(driver, By.id("password"), 15);
		LOG.debug("Waiting for the password field to be visible");
		WebDriverUtil.waitForElementVisible(driver, password, 90);
	}
}