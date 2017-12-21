package com.extrabux.pages.account;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.extrabux.pages.cn.account.ChinaAccountSettingPage;
import com.extrabux.util.WebDriverUtil;

public class AccountSettingsPage extends AccountPage {

	private static final Log LOG = LogFactory.getLog(AccountSettingsPage.class);
	
	@FindBy(css="div.locales > div[class*=selected]")
	WebElement selectedLocale; 
	
	@FindBy(css="div#accountLocales i.selector")
	WebElement localesIcon;
	
	public AccountSettingsPage(WebDriver driver) {
		super(driver);
	}

	public String getSelectedLocale(){
		WebDriverUtil.waitForElementPresent(driver, By.className("locales"), 10);
		String selectedLocaleName = selectedLocale.getAttribute("class").substring(0,5);
		LOG.info("Current selected locale is "+	selectedLocaleName);
		return selectedLocaleName;
	}
	
	public void selectLocale(String locale){
		WebDriverUtil.waitForElementPresent(driver, By.id("accountLocales"), 10);
		WebElement localeOption = driver.findElement(By.cssSelector("div#accountLocales div."+locale+".locale-option > a"));
		WebDriverUtil.hoverOnElement(localesIcon, driver);
		LOG.info("Set locale to "+ locale);
		WebDriverUtil.waitForElementVisible(driver, localeOption, 5);
		localeOption.click();
	}
	
	public ChinaAccountSettingPage setLocalToZhCn(){
		selectLocale("zh_CN");
		return new ChinaAccountSettingPage(driver);
	}
	
}
