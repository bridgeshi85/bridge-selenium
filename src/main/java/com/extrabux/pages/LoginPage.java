package com.extrabux.pages;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.util.WebDriverUtil;


public class LoginPage extends BasePage {
	private static final Log LOG = LogFactory.getLog(LoginPage.class);
	static final String LOGIN_URI = "/users/login";

	// Common elements
	WebElement email;
	WebElement password;
	// ExtraBux elements
	WebElement submitButton;
	// Facebook elements
	WebElement facebookConnectLink;
	WebElement pass;
	WebElement persistBox;
	WebElement login;
	@FindBy(id = "u_0_1")
	WebElement facebookSubmit;

	public LoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public HomePageAfterLogin login(String username, String password) {
		LOG.debug("Entering username and password");
		loginNoSubmit(username, password);
		LOG.debug("Submit login");
		return submit();
	}

	public void loginNoSubmit(String username, String password) {
		typeUsername(username);
		typePassword(password);
	}

	public HomePageAfterLogin loginFacebook(String username, String password, boolean keepLoggedIn) {
		typeUsername(username);
		typePasswordFacebook(password);
		if (keepLoggedIn) {
			persistBox.click();
		}
		return submitFacebook();
	}

	private void typePasswordFacebook(String password) {
		pass.sendKeys(password);
	}

	public HomePageAfterLogin loginWithFacebook(String usernameOrNumber, String password, boolean keepLoggedIn) {
		facebookConnectLink.click();
		Set<String> handles = driver.getWindowHandles();
		List<Object> handleList = Arrays.asList(handles.toArray());
		// I know there is a better way to do this but I haven't done this in ages.
		driver.switchTo().window((String) handleList.get(handleList.size() - 1));
		loginFacebook(usernameOrNumber, password, keepLoggedIn);
		driver.switchTo().window((String) handleList.get(0));
		// TODO this should be handled by an implicit wait
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new HomePageAfterLogin(driver);
	}

	public void typeUsername(String username) {
		LOG.debug("typing username: " + username + " into login form.");
		email.clear();
		email.sendKeys(username);
	}

	public void typePassword(String pswd) {
		LOG.debug("typing password: " + pswd + " into login form.");
		password.sendKeys(pswd);
	}

	public HomePageAfterLogin submit() {
		LOG.debug("clicking login submit button.");
		WebDriverUtil.waitForElementPresent(driver, By.id("submitButton"), 10);
		submitButton.click();
		return new HomePageAfterLogin(driver);
	}

	public HomePageAfterLogin submitFacebook() {
		facebookSubmit.click();
		return new HomePageAfterLogin(driver);
	}

	// TODO make this better
	public String getUrl(String serverName) {
		return "https://" + serverName + LOGIN_URI;
	}

}
