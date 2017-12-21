package com.extrabux.pages.cn.bi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class CJAdvertisersPage extends BasePage {	

	private static final Log LOG = LogFactory.getLog(CJAdvertisersPage.class);
	private static final String AD_INPUT_ID = "token-input-";
	private static final String COMMISSION_VALUE_CSS = "div.commission-value.sale-terms";
	private static final String SEARCH_BUTTON_ID = "searchButton";
	private static final String CLEAR_FILTER_ID = "clear-filters";
	
	@FindBy(id = AD_INPUT_ID)
	WebElement AdvertisersInput;
	
	@FindBy(id = SEARCH_BUTTON_ID)
	WebElement searchButton;
	
	@FindBy(className = "token-input-delete-token")
	WebElement deleteToken;
	
	@FindBy(css = COMMISSION_VALUE_CSS)
	WebElement commissionVaule;
	
	@FindBy(id = CLEAR_FILTER_ID)
	WebElement clearFilter;
	
	public CJAdvertisersPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void typeMearchtId(String id) throws InterruptedException{
		WebDriverUtil.waitForElementPresent(driver, By.id(AD_INPUT_ID), 2000);
		
		AdvertisersInput.sendKeys(id);
		Thread.sleep(1000);
		
		AdvertisersInput.sendKeys(Keys.ENTER);
		LOG.info("Input merchant id :" + id);
	}
	
	public void submitSearch() throws InterruptedException{
		WebDriverUtil.waitForElementPresent(driver, By.id(SEARCH_BUTTON_ID), 5);
		searchButton.click();
		LOG.info("Submit search........");
	}
	
	public void clearFilter(){
		WebDriverUtil.waitForElementPresent(driver, By.id(CLEAR_FILTER_ID), 5);
		clearFilter.click();
		LOG.info("Clear Filter........");
	}
	
	public String getCommissions(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector(COMMISSION_VALUE_CSS), 5);
		LOG.info("Commission on page : "+commissionVaule.getText());
		return commissionVaule.getText();
	}
	
}
