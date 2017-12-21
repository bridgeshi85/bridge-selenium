package com.extrabux.pages.cn.bi;

import static org.testng.Assert.assertEquals;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class LinkShareHomePage extends BasePage {	

	private static final String USERNAME_ID = "headerLoginUsername";

	@FindBy(id = USERNAME_ID)
	WebElement userName;
	
	WebElement advertiserSearchField;
	
	WebElement headerSearchButton;
	
	@FindBy(name = "login")
	WebElement loginButton;
	
	public LinkShareHomePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void verifyLoggedInEmail(String username) throws Exception {
		WebDriverUtil.waitForElementPresent(driver, By.id(USERNAME_ID), 10);
		assertEquals(userName.getText(),username,"Logged in username as not expected");
	}
	
	public LinkShareOfferPage getAvailbleOfferRequestLink(String mearchantId) throws Exception {
		goToURL("http://cli.linksynergy.com/cli/publisher/programs/Offers/available_offers.php?mid="
				+mearchantId);
		return new LinkShareOfferPage(driver);
	}
}
