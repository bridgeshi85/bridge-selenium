package com.extrabux.pages.cn;

import static org.testng.Assert.assertNotEquals;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.pages.StoreTransferPage;
import com.extrabux.util.WebDriverUtil;

public class ChinaStoresListPage extends BasePage{
	
	@FindBy(css="span.pagination-page-current")
	WebElement currentPage;
	
	@FindBy(className="store")
	WebElement store;
	
	@FindBy(xpath="//div[@class='store'][.//a[contains(.,'Walgreens')]]//a[@class='v2ButtonGreen transferLink']")
	WebElement storeTransferLink;
	
	@FindBy(xpath="//div[@class='cashBack-row']/a[contains(.,'%')]")
	List<WebElement> cashBackRows;
	
	@FindBy(css="div.store-3col > span")
	List<WebElement> successPurchaseRows;
	
	@FindBy(css="#paymentTypes > span.more > span.text-more")
	WebElement moreArrowDownIcon;
	
	static final String STORES_URI = "/stores";
	private static final Log LOG = LogFactory.getLog(ChinaStoresListPage.class); 
	
	public ChinaStoresListPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getUrl(String serverName) {
		return "https://" + serverName + STORES_URI;
	}
	
	public void filterWithCheckBox(String text){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.filter-categories"), 30);
		WebDriverUtil.hoverOnElement(moreArrowDownIcon, driver);
		LOG.info("filter by "+text);
		driver.findElement(By.xpath("//tr[td[contains(., '"+text+"')]]/td/input")).click();
		WebDriverUtil.waitForPageToLoadComplete(driver, 15);
	}
	
	public void filterByLink(String linkText){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.filter-categories"), 30);
		LOG.info("filter by "+linkText);
		driver.findElement(By.linkText(linkText)).sendKeys(Keys.ENTER);
	}
	
	public void sort(String linkText){
		WebDriverUtil.waitForElementPresent(driver, By.id("sort"), 10);
		LOG.info("sort by "+linkText);
		driver.findElement(By.linkText(linkText)).click();
	}
	
	public void addStoreToFav(String storeName){
		WebDriverUtil.waitForElementPresent(driver, By.id("storeLists"), 10);
		LOG.info("add "+storeName+" to fav list");
		driver.findElement(By.cssSelector(".favoriteStar.tooltip[data-storeName=\""+storeName+"\"]")).click();
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector(".slidingMessage.shadow.good"), 15);
	}
	
	public boolean verifyStorePresent(String storeName){
		WebDriverUtil.waitForElementPresent(driver, By.className("store"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.xpath("//div[@class='store-headline']/a[contains(., \""+storeName+"\")]"));
	}
	
	public StoreTransferPage clickStartShopping() {
		clickTransferButton();
		return new StoreTransferPage(driver);
	}
	
	public void clickTransferButton(){
		LOG.debug("clicking start shopping button.");
		storeTransferLink.click();
	}

	public void selectPage(String pageNum){
		WebDriverUtil.waitForElementPresent(driver, By.className("pagination"), 10);
		assertNotEquals(currentPage.getText(),pageNum,"current page equals select page");
		driver.findElement(By.cssSelector("div.pagination-controls a[href*='page="+pageNum+"']")).click();
		WebDriverUtil.waitForPageToLoadComplete(driver, 10);
		LOG.info("select page to "+pageNum);
	}
	
	public String getCurrentPage(){
		LOG.info("current page is: "+currentPage.getText());
		return currentPage.getText();
	}
	
	public boolean verifyCashBackSortResult(){
		return verfiySortResult(cashBackRows);
	}
	
	public boolean verfiySuccessPurchaseSortResult(){
		return verfiySortResult(successPurchaseRows);
	}
	
	public boolean verifyShippingToChina(){
		WebDriverUtil.waitForElementPresent(driver, By.className("store"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.cssSelector("div.icon.eighteen-flat.shipping"));
	}
	
	public boolean verifyChinaCard(){
		WebDriverUtil.waitForElementPresent(driver, By.className("store"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.cssSelector("div.icon.eighteen-flat.payment-icon-chinese-card"));
	}
	
	public boolean verifyAlipay(){
		WebDriverUtil.waitForElementPresent(driver, By.className("store"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.cssSelector("div.icon.eighteen-flat.payment-icon-alipay"));
	}
	
	public boolean verifyAll(){
		WebDriverUtil.waitForElementPresent(driver, By.className("store"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.cssSelector("div.icon.eighteen-flat.payment-icon-alipay")) && 
			   WebDriverUtil.verifyElementExist(driver, By.cssSelector("div.icon.eighteen-flat.payment-icon-chinese-card")) &&
			   WebDriverUtil.verifyElementExist(driver, By.cssSelector("div.icon.eighteen-flat.shipping"));
	}
	
	public boolean verfiySortResult(List<WebElement> resultRows){
		WebDriverUtil.waitForElementPresent(driver, By.className("store"), 10);
		int prevRow=0;
		int currentRow=0;
		for(WebElement row : resultRows){
			currentRow = getIntFromString(row.getText());
			if(prevRow == 0){
				prevRow = currentRow;
			}else
			{
				if(prevRow >= currentRow){
					prevRow = currentRow;
				}else{
					LOG.info("prev row: "+prevRow+" current row: "+currentRow);
					return false;
				}
			}
		}
		return true;
	}
	
	public static int getIntFromString(String str){
		 
	    Pattern p=Pattern.compile("\\d+");
	    Matcher m=p.matcher(str);
	    String findString = "";
	    if(m.find()){
	    	findString = m.group(0);
	    }
		return Integer.parseInt(findString);
	}
}
