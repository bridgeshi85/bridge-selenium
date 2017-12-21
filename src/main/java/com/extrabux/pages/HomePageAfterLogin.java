package com.extrabux.pages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.account.AccountSettingsPage;
import com.extrabux.pages.account.InviteFriendsPage;
import com.extrabux.pages.account.MyEarningsPage;
import com.extrabux.pages.account.MyFavoriteStoresPage;
import com.extrabux.util.WebDriverUtil;


public class HomePageAfterLogin extends HomePageBeforeLogin {
	private static final Log LOG = LogFactory.getLog(HomePageAfterLogin.class);
	private static final String EMAIL_CLASS_NAME = "email";

	WebElement userCornerEarnings;
	@FindBy(className = EMAIL_CLASS_NAME)
	WebElement emailLoggedIn;

	public HomePageAfterLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public String getLifetimeEarnings() {
		return userCornerEarnings.findElement(By.tagName("span")).getText();
	}

	public boolean verifyVipUser(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.member"), 5);
		return WebDriverUtil.verifyElementExist(driver, By.className("icon-vip-bonus"));
	}
	
	public boolean verifyLoggedInElementPresent(String email) throws Exception {
		try {
			WebDriverUtil.waitForElementPresent(driver, By.className(EMAIL_CLASS_NAME), 10);
			LOG.debug("size of email element on page: " + driver.findElements(By.className(EMAIL_CLASS_NAME)).size());
			String actual = emailLoggedIn.getText().replaceAll("(\\s.*)", "");
			LOG.info("actual email on page: " + actual + ", expected: " + email);
			if (!actual.equals(email)) {
				return false;
			}
		}
		catch (NoSuchElementException e) {
			e.printStackTrace();
			throw new Exception("member element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		catch (TimeoutException te) {
			te.printStackTrace();
			System.err.println(driver.getPageSource());
			throw new Exception("timed out waiting for member to display. element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		return true;
	}

	public MyEarningsPage hoverAndClickMyEarnings() {
		hoverAndSelectFromAccountMenu("My Earnings");
		return new MyEarningsPage(driver);
	}

	public AccountSettingsPage hoverAndClickMyAccountSettings() {
		hoverAndSelectFromAccountMenu("Account Settings");
		return new AccountSettingsPage(driver);
	}

	public MyFavoriteStoresPage hoverAndClickMyFavoriteStores() {
		hoverAndSelectFromAccountMenu("My Favorite Stores");
		return new MyFavoriteStoresPage(driver);
	}

	public InviteFriendsPage hoverAndClickInviteFriends() {
		hoverAndSelectFromAccountMenu("Invite Friends");
		return new InviteFriendsPage(driver);
	}

	public HomePageBeforeLogin hoverAndClickLogout() {
		hoverAndSelectFromAccountMenu("Log Out");
		return new HomePageBeforeLogin(driver);
	}

	public void hoverAndSelectFromAccountMenu(String linkText) {
		Actions actions = new Actions(driver);
		actions.moveToElement(emailLoggedIn).perform();

		WebElement subLink = driver.findElement(By.linkText(linkText));
		actions.moveToElement(subLink);
		actions.click();
		actions.perform();
	}
}
