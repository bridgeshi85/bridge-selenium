package com.extrabux.pages.cn.migrate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class ChinaMigratePage extends BasePage{
	
	@FindBy(css="a.to-upgrade")
	WebElement upgradeButton;

	@FindBy(css=".success-title")
	WebElement successTitle;
	
	@FindBy(css="a.to-my-account.to-merge-account")
	WebElement toMergerButton;
	
	private static final Log LOG = LogFactory.getLog(ChinaMigratePage.class); 
	
	public ChinaMigratePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void upgradeToExtrabuxUser(){
		upgradeButton.click();
		WebDriverUtil.waitForElementPresent(driver, By.className("success-title"), 15);
	}
	
	public String getMessage(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector(".success-title"), 15);
		LOG.info(successTitle.getText());
		return successTitle.getText();
	}
	
	public ChinaMergePage goToMergePage(){
		toMergerButton.click();
		return new ChinaMergePage(driver);
	}
}
