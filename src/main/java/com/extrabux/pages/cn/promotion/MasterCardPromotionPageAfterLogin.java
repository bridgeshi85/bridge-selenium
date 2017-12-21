package com.extrabux.pages.cn.promotion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
import com.extrabux.util.WebDriverUtil;

public class MasterCardPromotionPageAfterLogin extends ChinaHomePageAfterLogin{

	@FindBy(className="cash")
	WebElement cash;
	
	@FindBy(css="a.mastercard-button")
	WebElement bindCardButton;
	
	@FindBy(css="div.member.header-layer > a > b")
	WebElement emailLoggedIn;
	
	private static final Log LOG = LogFactory.getLog(MasterCardPromotionPageAfterLogin.class);
	
	public MasterCardPromotionPageAfterLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);	
	}

	public String getPromotionStatus() throws Exception{
		String acctualStatus = bindCardButton.getText();
		LOG.info("actual bind status on page: " + acctualStatus);
		return acctualStatus;
	}
	
	public boolean verifyLoggedInElementPresent(String email) throws Exception {

		String actual = getLoggedInEmail(By.cssSelector("div.member > a > b"),emailLoggedIn);
		LOG.info("actual email on page: " + actual + ", expected: " + email);
		if (!actual.equals(email)) {
			return false;
		}
		
		return true;
	}
	
	public String getLifetimeEarnings() {
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.member"), 10);
		hoverOnEmail(emailLoggedIn);
		return cash.getText();
	}
}
