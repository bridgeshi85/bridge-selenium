package com.extrabux.pages.cn.bi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class CJHomePageAfterLogin extends BasePage {	

	private static final Log LOG = LogFactory.getLog(CJHomePageAfterLogin.class);
	private static final String AD_LINK_TEXT = "Advertisers";
	
	@FindBy(linkText = AD_LINK_TEXT)
	WebElement Advertisers;
	
	public CJHomePageAfterLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public CJAdvertisersPage goToAdvertisersPage(){
		WebDriverUtil.waitForElementPresent(driver, By.linkText(AD_LINK_TEXT), 1000);
		Advertisers.click();
		LOG.info("Enter Advertisers Page......");
		return new CJAdvertisersPage(driver);
	}
}
