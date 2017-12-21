package com.extrabux.pages.clo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class EbatesLogInSection extends BasePage {
	private static final Log LOG = LogFactory.getLog(EbatesLogInSection.class);

	String serverName = "qa-www.ebates.com";
	@FindBy(id = "retUserName")
	public WebElement email;
	@FindBy(id = "retPassword")
	public WebElement password;
	@FindBy(xpath = "//*[contains(@src, 'button_login')]")
	public WebElement submitBtn;

	public EbatesLogInSection(WebDriver driver) {
		super(driver);
	}

	public void waitForJoinForm() {
		LOG.debug("Waiting for the login form to be present");
		WebDriverUtil.waitForElementPresent(driver, By.id("email_address"), 30);
		LOG.debug("Waiting for the login form to be visible");
		WebDriverUtil.waitForElementVisible(driver, email, 15);
	}

	public EbatesHomePage logIn(String user, String pass) {
		fillForm(user, pass);
		return submit();
	}

	public void fillForm(String user, String pass) {
		LOG.info("attempting to log in with username: " + user + ", password: " + pass);
		typeUsername(user);
		typePassword(pass);
	}

	public EbatesHomePage submit() {
		LOG.debug("clicking submit login form.");
		submitBtn.click();
		return new EbatesHomePage(driver);
	}

	public void typeUsername(String user) {
		LOG.debug("typing username: " + user + " into login form.");
		email.clear();
		email.sendKeys(user);
	}

	public void typePassword(String pass) {
		LOG.debug("typing password: " + pass + " into login form.");
		password.clear();
		password.sendKeys(pass);
	}

	public void waitForEmailField() {
		LOG.debug("Waiting for the email field to be present");
		WebDriverUtil.waitForElementPresent(driver, By.id("retUserName"), 15);
		LOG.debug("Waiting for the email field to be visible");
		WebDriverUtil.waitForElementVisible(driver, email, 90);
	}

	public void waitForPasswordField() {
		LOG.debug("Waiting for the password field to be present");
		WebDriverUtil.waitForElementPresent(driver, By.id("retPassword"), 15);
		LOG.debug("Waiting for the password field to be visible");
		WebDriverUtil.waitForElementVisible(driver, password, 90);
	}
}