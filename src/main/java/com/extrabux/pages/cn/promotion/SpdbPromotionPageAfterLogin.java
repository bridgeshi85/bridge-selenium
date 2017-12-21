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

public class SpdbPromotionPageAfterLogin extends ChinaHomePageAfterLogin{

	private static final String EMAIL_CSS_NAME = "div.member.header-layer>a>b";
	
	@FindBy(css="a.startSavingButton.orangeButton.shadowedButton.gradient")
	WebElement bindCardButton;
	
	@FindBy(css=EMAIL_CSS_NAME)
	WebElement emailLoggedIn;
		
	@FindBy(className="cash")
	WebElement cash;
	
	private static final Log LOG = LogFactory.getLog(SpdbPromotionPageAfterLogin.class);
	
	public SpdbPromotionPageAfterLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);	
	}

	public String getPromotionStatus() throws Exception{
		String acctualStatus = bindCardButton.getText();
		LOG.info("actual bind status on page: " + acctualStatus);
		return acctualStatus;
	}
	
	public boolean verifyLoggedInElementPresent(String email) throws Exception {

		String actual = getLoggedInEmail(By.cssSelector(EMAIL_CSS_NAME),emailLoggedIn);
		LOG.info("actual email on page: " + actual + ", expected: " + email);
		if (!actual.equals(email)) {
			return false;
		}
		
		return true;
	}
	
	public String getLifetimeEarnings() {
		WebDriverUtil.waitForElementVisible(driver, emailLoggedIn, 10);
		hoverOnEmail(emailLoggedIn);
		return cash.getText();
	}
}
