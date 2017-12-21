package com.extrabux.pages.cn.help;

import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.apache.commons.logging.LogFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class ChinaHelpCenterHomePage extends BasePage{
	private static final Log LOG = LogFactory.getLog(ChinaHelpCenterHomePage.class);
	private static final String URI = "/help";
	
	@FindBy(css = "#help-search-input")
	WebElement searchInput;
	
	@FindBy(css="div.help-center-search-wrapper form > button.search-button")
	WebElement searchButton;
	
	public ChinaHelpCenterHomePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public String getUrl(String serverName) {
		return "https://" + serverName + URI;
	}
	
	public ChinaFaqPage searchFaq(String faq){
		typeSearchFaq(faq);
		WebDriverUtil.waitForElementClickable(driver, searchButton, 10);
		//searchButton.click();
		searchButton.sendKeys(Keys.ENTER);
		LOG.info("submit search...");
		return new ChinaFaqPage(driver);
	}
	
	public void typeSearchFaq(String faq){
		WebDriverUtil.waitForElementPresent(driver, By.className("help-center-search-wrapper"), 10);
		LOG.info("type "+faq+" into search input");
		searchInput.sendKeys(faq);
	}
}
