package com.extrabux.pages.cn.promotion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.ChinaLoginPage;
import com.extrabux.util.WebDriverUtil;


public class ChinaCMBCPage extends ChinaLoginPage{
	
	@FindBy(css="div.tab-headline a[href*='page-card']")
	WebElement cardTab;
	
	@FindBy(id="login-form")
	WebElement cmbcForm;
	
	WebElement email;
	WebElement password;
	
	static final String CMBC_URI = "/cmbc";
	private static final Log LOG = LogFactory.getLog(ChinaCMBCPage.class); 
	
	public ChinaCMBCPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getUrl(String serverName) {
		return "https://" + serverName + CMBC_URI;
	}

	public ChinaCMBCPageAfterLogin login(String username, String password) {
		cardTab.click();
		WebDriverUtil.waitForElementVisible(driver, cmbcForm, 15);
		LOG.info("click card tab...");
		loginNoSubmit(username, password);
		email.submit();
		return new ChinaCMBCPageAfterLogin(driver);
	}
}
