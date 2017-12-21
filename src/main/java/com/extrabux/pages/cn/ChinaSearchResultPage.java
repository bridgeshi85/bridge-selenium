package com.extrabux.pages.cn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class ChinaSearchResultPage extends BasePage {

	private static final Log LOG = LogFactory.getLog(ChinaSearchResultPage.class);

	@FindBy(css = "div.search-headline li:nth-child(1)")
	WebElement storeTab;

	@FindBy(css = "div.search-headline li:nth-child(2)")
	WebElement couponTab;

	@FindBy(css = "div.search-headline li:nth-child(3)")
	WebElement blogTab;

	@FindBy(css = "li.active > span")
	WebElement currentTab;
	
	@FindBy(css = "a.name")
	WebElement firstStoreOrCouponItem;

	@FindBy(css = "h2 > a")
	WebElement firstBlogItem;

	public ChinaSearchResultPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void switchToStoreTab() {
		storeTab.click();
	}
	
	public void switchToCouponTab() {
		couponTab.click();
	}
	
	public void switchToBlogTab() {
		blogTab.click();
	}

	public String getFirstStoreOrCouponItem() {
		String firstItem = firstStoreOrCouponItem.getText();
		LOG.info("Get first item of search result!");
		return firstItem;
	}
	
	public boolean verifyCouponExist(String keyWord) {
		WebDriverUtil.waitForElementPresent(driver, By.className("store"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.xpath("//div[@class='store']//a[contains(@class,'name')][contains(.,'"+keyWord+"')]"));
	}
	
	public boolean verifyProductExist(String keyWord){
		WebDriverUtil.waitForElementPresent(driver, By.className("search-result-lists"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.xpath("//div[@class='product-title']/a[contains(.,'"+keyWord+"')]"));
	}
	
	public String getFirstBlogItem() {
		String firstItem = firstBlogItem.getText();
		LOG.info("Get first item of search result!");
		return firstItem;
	}
	
	public String getCurrentTabName() {
		String currentTabName = currentTab.getText().substring(0, 2);
		LOG.info(currentTabName);
		return currentTabName;
	}
	

}
