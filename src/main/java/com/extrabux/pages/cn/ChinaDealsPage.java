package com.extrabux.pages.cn;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.pages.StoreTransferPage;
import com.extrabux.util.WebDriverUtil;

public class ChinaDealsPage extends BasePage{
	
	@FindBy(linkText="即将过期")
	WebElement endDateLink;
	
	@FindBy(xpath="//div[@class='product-coupon']//span[@class='code-button']")
	WebElement startTransferLink;
	
	@FindBy(css="div.store em.countdown-time")
	List<WebElement> countDownTime;
	
	@FindBy(xpath="//div[@id='storeLists']//span[contains(@class,'expiration')][contains(.,'距离')][contains(.,'/')]")
	List<WebElement> expirationDates;
	
	@FindBy(css="label.fav-lab > input")
	WebElement favCheckBox;
	
	@FindBy(css="div.filter-categories span.text-more")
	WebElement moreStoresFilterLink;
	
	static final String DEALS_URI = "/deals";
	private static final Log LOG = LogFactory.getLog(ChinaDealsPage.class); 
	
	public ChinaDealsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getUrl(String serverName) {
		return "https://" + serverName + DEALS_URI;
	}
	
	public void sortByEndDate(){
		WebDriverUtil.waitForElementPresent(driver, By.id("sort"), 10);
		LOG.info("filter by endate");
		endDateLink.click();
	}
	
	public StoreTransferPage clickStartShopping() {
		clickTransferButton();
		return new StoreTransferPage(driver);
	}
	
	public void clickTransferButton(){
		LOG.debug("clicking start shopping button.");
		WebDriverUtil.waitForElementPresent(driver, By.className("code-button"), 15);
		startTransferLink.click();
	}
	
	public void filterCoupon(String linkText){
		WebDriverUtil.waitForElementPresent(driver, By.id("store-filter"), 10);
		LOG.info("filter by "+linkText);
		driver.findElement(By.xpath(".//a[contains(@href,'deals?store')][text()=\""+linkText+"\"]")).click();
	}
	
	public void filterByFav(){
		WebDriverUtil.waitForElementPresent(driver, By.id("store-filter"), 10);
		LOG.info("filter by my favourite");
		favCheckBox.click();
	}
	
	public void showMoreStores(){
		moreStoresFilterLink.click();
		WebDriverUtil.waitForElementPresent(driver, By.xpath("//span[@class='text-more'][contains(.,'收起')]"), 5);
		LOG.info("show more stores");
	}
	
	public boolean verifyCouponPresentByName(String couponName){
		WebDriverUtil.waitForElementPresent(driver, By.className("store"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.xpath("//div[@class='store-headline']/a[contains(., '"+couponName+"')]"));
	}
	
	public boolean verifyCouponPresentByStore(String storeName){
		WebDriverUtil.waitForElementPresent(driver, By.className("store"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.xpath("//div[@id='storeLists']//div[@class='infos']//b[contains(., '"+storeName+"')]"));
	}
	
	public boolean verifySortByEndDate() throws ParseException{
		 WebDriverUtil.waitForElementPresent(driver, By.id("storeLists"), 10);
		 Date currentRowDate = new Date();
		 Date prevRowDate = null;
		 for(WebElement row : expirationDates){
			 	//LOG.info(row.getText());
				currentRowDate = getExpiryDate(row.getText());
				//LOG.debug("current: " + currentRowDate);
				if(prevRowDate == null){
					prevRowDate = currentRowDate;
				}else
				{
					if(prevRowDate.compareTo(currentRowDate) <= 0){
						prevRowDate = currentRowDate;
					}else{
						LOG.info("prev row: "+prevRowDate+" current row: "+currentRowDate);
						return false;
					}
				}
			}
		 return true; 
	 }
	
	public boolean verfiySortResultByCountDownTime(){
		WebDriverUtil.waitForElementPresent(driver, By.className("store"), 10);
		long prevRow=0;
		long currentRow=0;
		for(WebElement row : countDownTime){
			currentRow = Long.parseLong(row.getAttribute("data-end"));
			//LOG.debug("current: " + currentRow);
			if(prevRow == 0){
				prevRow = currentRow;
			}else
			{
				if(prevRow <= currentRow){
					prevRow = currentRow;
				}else{
					LOG.info("prev row: "+prevRow+" current row: "+currentRow);
					return false;
				}
			}
		}
		return true;
	}
	

}
