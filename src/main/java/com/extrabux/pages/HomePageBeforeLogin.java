package com.extrabux.pages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.util.WebDriverUtil;

public class HomePageBeforeLogin extends BasePage {

	private static final Log LOG = LogFactory.getLog(HomePageBeforeLogin.class);

	// login link
	WebElement login;
	// register button
	WebElement register;

	// search
	@FindBy(id = "search-input")
	protected WebElement searchInput;
	@FindBy(id = "search-form-image")
	protected WebElement searchSubmit;

	public HomePageBeforeLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public boolean verifyNotLoggedIn() {
		// make sure the logged in element isn't there
		boolean loggedIn = driver.findElements(By.className("memeber")).size() > 0;
		if (loggedIn) {
			return false;
		}
		return true;
	}

	public LoginPage clickLogin() {
		login.click();
		return new LoginPage(driver);
	}

	public SignUpPage clickRegister() {
		register.click();
		return new SignUpPage(driver);
	}

	public MerchantPage search(String keyword) {
		// Ssearch with default type - Store, this function can be used on no-Chinese page
		WebDriverUtil.waitForElementPresent(driver, By.id("search-input"), 10);

		searchInput.sendKeys(keyword);
		searchSubmit.click();

		return new MerchantPage(driver);
	}

	public BasePage search(String keyword, int i) {
		// Search type 1 STORE -> Type keyword -> Select the [i] suggestion item
		// -> Individual store page
		return new BasePage(driver);
	}

}
