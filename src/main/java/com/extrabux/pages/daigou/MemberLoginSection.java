package com.extrabux.pages.daigou;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.util.WebDriverUtil;

public class MemberLoginSection extends BasePage {
	WebElement storeLoginUsername;
	WebElement storeLoginPassword;
	WebElement storeLoginBtn;
	WebElement storeLogoutBtn;
	WebElement storeLogoutConfirmBtn;
	WebElement storeLogoutCancelBtn;
	WebElement storeLoginLoggedInAs;

	@FindBy (className = "has-validation-error")
	public List<WebElement> hasErrorElements;
	@FindBy (className = "help-block")
	List<WebElement> errors;

	By loginSpinnerLocator = By.id("storeLoginSpinner");
	By loggingInSpinnerLocator = By.id("storeLoggingInSpinner");

	public MemberLoginSection(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void typeUsername(String username) {
		storeLoginUsername.clear();
		storeLoginUsername.sendKeys(username);
	}

	public void typePassword(String password) {
		storeLoginPassword.clear();
		storeLoginPassword.sendKeys(password);
	}

	public void clickLoginBtn() {
		storeLoginBtn.click();
	}

	public void clickLogoutBtn() {
		storeLogoutBtn.click();
	}

	public void clickLogoutConfirmBtn() {
		storeLogoutConfirmBtn.click();
	}

	public void clickLogoutCancelBtn() {
		storeLogoutCancelBtn.click();
	}

	public void waitForLogin() {
		WebDriverUtil.waitForElementPresent(driver, By.id("storeLoginUsername"), 30);
		WebDriverUtil.waitForElementVisible(driver, storeLoginUsername, 30);
	}

	public void waitForLoginSpinner() {
		WebDriverUtil.waitForElementPresent(driver, loginSpinnerLocator, 30);
		WebDriverUtil.waitForElementNotVisible(driver, loginSpinnerLocator, 30);
	}

	public void waitForLoggingInSpinner() {
		WebDriverUtil.waitForElementPresent(driver, loggingInSpinnerLocator, 30);
		WebDriverUtil.waitForElementNotVisible(driver, loggingInSpinnerLocator, 60);
	}

	public void waitForLogoutBtn() {
		WebDriverUtil.waitForElementPresent(driver, By.id("storeLogoutBtn"), 30);
		WebDriverUtil.waitForElementVisible(driver, storeLogoutBtn, 30);
	}

	public void waitForLogoutConfirmBtn() {
		WebDriverUtil.waitForElementPresent(driver, By.id("storeLogoutConfirmBtn"), 30);
		WebDriverUtil.waitForElementVisible(driver, storeLogoutConfirmBtn, 30);
	}

	public boolean isLoggedIn() {
		if (storeLoginLoggedInAs.isDisplayed()) {
			return true;
		}

		return false;
	}

	public void login(String username, String password) {
		waitForLoginSpinner();

		if (!isLoggedIn()) {
			waitForLogin();
			typeUsername(username);
			typePassword(password);
			clickLoginBtn();

			waitForLoggingInSpinner();
		}
	}

	public void logout() {
		waitForLoginSpinner();

		if (isLoggedIn()) {
			waitForLoginSpinner();
			waitForLogoutBtn();
			clickLogoutBtn();
			waitForLogoutConfirmBtn();
			clickLogoutConfirmBtn();
		}
	}

	public boolean verifyErrorsOnPage(List<String> expectedErrors) {
		// first assert the number of errors found on the page is as expected
		List<String> errorsOnPage = getErrors();
		if (!(errorsOnPage.size() == expectedErrors.size())) {
			System.err.print("sizes incorrect");
			return false;
		}
		if (!errorsOnPage.equals(expectedErrors)) {
			System.err.print("Errors different");
			return false;
		}
		return true;
	}

	public List<String> getErrors() {
		List<String> errorsOnPage  = new ArrayList<String>();
		for (WebElement error : errors) {
			if (error.isDisplayed()) {
				errorsOnPage.add(error.getText().replaceAll("\\s+",""));
			}
		}
		return errorsOnPage;
	}
}
