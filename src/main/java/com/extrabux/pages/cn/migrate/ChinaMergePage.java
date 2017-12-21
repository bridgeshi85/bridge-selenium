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

public class ChinaMergePage extends BasePage{
	
	@FindBy(name="accountEx")
	WebElement exUserName;
	
	@FindBy(name="passportEx")
	WebElement exPassword;
	
	@FindBy(name="accountEb")
	WebElement ebUserName;
	
	@FindBy(name="passportEb")
	WebElement ebPassword;
	
	@FindBy(css="div.step-box:not([style^='display']) a.next-step")
	WebElement submit;
	
	@FindBy(name="mobileEx")
	WebElement mobileNumber;
	
	@FindBy(css=".success-title")
	WebElement successTitle;
	
	private static final Log LOG = LogFactory.getLog(ChinaMergePage.class); 
	
	public ChinaMergePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public void mergeEbWithExUser(String exUsername,String exPassword,String ebPassword){
		LOG.info("merge with extrabux user:"+exUsername);
		WebDriverUtil.waitForElementPresent(driver, By.name("accountEx"), 15);
		fillExtrabuxAccount(exUsername,exPassword);
		fillEbatesAccount(ebPassword);
		submit();
	}
	
	public void fillExtrabuxAccount(String username,String password){
		LOG.debug("typing username: " + username + " and password: "+ password +" into extrabux user form.");
		exUserName.sendKeys(username);
		exPassword.sendKeys(password);
	}
	
	public void fillEbatesAccount(String password){
		LOG.debug("typing password: "+ password +" into ebates user form.");
		ebPassword.sendKeys(password);
	}
	
	public void submit(){
		LOG.info("go to next step....");
		submit.click();
	}
	
	public void verifyMobile(String mobile){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector(".second-step.current"), 10);
		LOG.info("input moblie number:"+mobile);
		mobileNumber.sendKeys(mobile);
		submit();
	}
	
	public String getMessage(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector(".third-step.current"), 10);
		LOG.info(successTitle.getText());
		return successTitle.getText();
	}
}
