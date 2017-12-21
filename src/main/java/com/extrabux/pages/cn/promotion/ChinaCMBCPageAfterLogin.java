package com.extrabux.pages.cn.promotion;

import org.openqa.selenium.By;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.ChinaHomePageAfterLogin;

public class ChinaCMBCPageAfterLogin extends ChinaHomePageAfterLogin{
	
	@FindBy(css="div.member")
	WebElement emailLoggedIn;
	
	private static final Log LOG = LogFactory.getLog(ChinaCMBCPageAfterLogin.class); 
	
	public ChinaCMBCPageAfterLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public boolean verifyLoggedInElementPresent(String email) throws Exception {
		String actual = getLoggedInEmail(By.cssSelector("div.member"),emailLoggedIn);
		LOG.info("actual email on page: " + actual + ", expected: " + email);
		if (!actual.equals(email)) {
			return false;
		}
		
		return true;
	}
	
}
