package com.extrabux.pages.account;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyEarningsPage extends AccountPage {
	static final String MY_EARNINGS_URI = "/users/profile";

	// tabs
	@FindBy(linkText = "All")
	WebElement allTab;
	@FindBy(linkText = "Pending")
	WebElement pendingTab;
	@FindBy(linkText = "Available")
	WebElement availableTab;
	@FindBy(linkText = "Processing")
	WebElement processingTab;
	@FindBy(linkText = "Paid")
	WebElement paidTab;
	@FindBy(linkText = "Added by Me")
	WebElement addedByMeTab;

	public MyEarningsPage(WebDriver driver) {
		super(driver);
	}

	// TODO make this better
	public String getUrl(String serverName) {
		return "http://" + serverName + MY_EARNINGS_URI;
	}

}
