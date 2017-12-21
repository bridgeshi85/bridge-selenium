package com.extrabux.pages.account;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class AccountPage extends BasePage {
	static final String ACCOUNT_URI = "/users";

	// the common navigation bar on account pages
	WebElement titleNav;
	@FindBy(linkText = "Account Summary")
	WebElement accountSummary;
	@FindBy(linkText = "Invite Friends")
	WebElement inviteFriends;
	@FindBy(linkText = "Account Settings")
	WebElement accountSettings;

	@FindBy(css="div.message.notice")
	WebElement message;
	
	// earning (right side)
	@FindBy(className = "cashbackDetails")
	WebElement showHideDetails;

	// current earnings
	// normally i wouldn't use xpath but since this is throw away stuff it
	// doesn't really matter
	@FindBy(xpath = "//*[@id=\"userTotals\"]/div[1]/table")
	WebElement currentEarningsTable;
	// earnings from purchases
	@FindBy(xpath = "//*[@id=\"userTotals\"]/div[2]/table")
	WebElement purchaseEarningsTable;
	// earnings from friends
	@FindBy(xpath = "//*[@id=\"userTotals\"]/div[3]/table")
	WebElement friendEarningsTable;
	// see friends
	@FindBy(linkText = "See Friends »")
	WebElement friendsLink;

	public AccountPage(WebDriver driver) {
		super(driver);
	}

	public MyEarningsPage clickNavAccountSummary() {
		accountSummary.click();
		return new MyEarningsPage(driver);
	}

	public InviteFriendsPage clickNavInviteFriends() {
		inviteFriends.click();
		return new InviteFriendsPage(driver);
	}

	public AccountSettingsPage clickNavAccountSettings() {
		accountSettings.click();
		return new AccountSettingsPage(driver);
	}

	public AccountPage clickShowHideDetails() {
		showHideDetails.click();
		return this;
	}

	public InviteFriendsPage clickSeeFriendsLink() {
		friendsLink.click();
		return new InviteFriendsPage(driver);
	}

	/*
	public String getCashAvailable() {
		return getCashTotal("Available");
	}

	public String getCashPending() {
		return getCashTotal("Pending");
	}

	public String getCashTotal(String totalRequested) {
		List<WebElement> amountRows = cashAmountTable.findElements(By.tagName("td"));
		for (WebElement amount : amountRows) {
			String total = amount.getText();
			if (totalRequested.equalsIgnoreCase("Available")) {
				return total.split(":")[0];
			}
			if (totalRequested.equalsIgnoreCase("Pending")) {
				return total.split(":")[0];
			}

		}
		return null;
	}*/

	public String getMessage(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.message.notice"), 15);
		WebDriverUtil.waitForPageToLoadComplete(driver, 30);
		return message.getText();
	}
	
	// TODO make this better
	public String getUrl(String serverName) {
		return "http://" + serverName + ACCOUNT_URI;
	}
}
