package com.extrabux.pages.cn.mvp;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.util.WebDriverUtil;

public class ProductsListPage extends MvpDaigouBasePage{
	
	@FindBy(css="span.text-more")
	WebElement moreIcon;
	
	@FindBy(css="span.text-hide")
	WebElement hideIcon;
	
	@FindBy(css="div.cashBack-row > a")
	List<WebElement> cashBackRows;
	
	@FindBy(css="div.category-headline > ul > li")
	List<WebElement> topCategories;
	
	// add to cart button
	@FindBy(css="a.shop-btn[href*=addToCart]")
	WebElement addToCartButton;
	
	//item
	@FindBy(xpath="//div[@class='item-card-view'][./div/a='加入购物车']")
	WebElement item;
	
	// add to cart button
	@FindBy(xpath="//div[@class='item-card-view'][.//a='加入购物车']//a[@class='favorite-btn']")
	WebElement addToFavButton;
	
	//@FindBy(css="div.product-info > span.product-price")
	@FindBy(xpath="//div[@class='item-card-view'][.//a='加入购物车']//span[@class='product-price']")
	List<WebElement> priceRows;
	
	@FindBy(css="ul#sortBy >li > a[href*='price']")
	WebElement sortByPriceLink;
	
	@FindBy(css="span.page_now")
	WebElement currentPage;
	
	@FindBy(css="#pager a:last-child")
	WebElement lastPage;
	
	//static final String STORES_URI = "/stores";
	private static final String PRODUCT_IMG_CSS = "div.category-section > div.box div.image-box";
	private static final String URI = "/catalog.php";
	
	private static final Log LOG = LogFactory.getLog(ProductsListPage.class); 
	
	public ProductsListPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getUrl(String serverName) {
		return "https://" + serverName+URI;
	}
	
	public ShopCartPage addToCart(){
		WebDriverUtil.hoverOnElement(item, driver);
		
		WebDriverUtil.waitForElementPresentAndClickable(driver, By.cssSelector("a.shop-btn[href*=addToCart]"), 10);
		
		LOG.info("add goods to cart.....");
		addToCartButton.click();
		
		return clickToShopCartBtnInDailog();
	}
	
	public void addToFav(){
		WebDriverUtil.hoverOnElement(item, driver);
		
		WebDriverUtil.waitForElementPresentAndClickable(driver, By.xpath("//div[@class='item-card-view'][.//a='加入购物车']//a[@class='favorite-btn']"), 10);
		
		LOG.info("add item to fav list.....");
				
		addToFavButton.click();
		
		//WebDriverUtil.waitForElementPresent(driver, By.id("toast-inner-container"), 5);
	}
	
	public void sortByPrice(){
		LOG.info("sort by price");
		sortByPriceLink.click();
	}
	
	public boolean verfiySortByPriceResult(String order){
		WebDriverUtil.waitForElementPresent(driver, By.className("item-card-view"), 10);
		double prevRow=0;
		double currentRow=0;
		for(WebElement row : priceRows){
			currentRow = getPrice(row.getText());
			if(prevRow == 0){
				prevRow = currentRow;
			}else
			{
				if(prevRow <= currentRow && order.equals("asc")){
					prevRow = currentRow;
				}
				else if(prevRow >= currentRow && order.equals("desc")){
					prevRow = currentRow;
				}else{
					LOG.info("prev row: "+prevRow+" current row: "+currentRow);
					return false;
				}
			}
		}
		return true;
	}
	
	public void filterByTopCategory(String categoryName){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.category-headline"), 10);
		LOG.info("filter by "+categoryName);
		WebElement topCategory = driver.findElement(By.xpath("//div[@class='category-headline']//li[contains(., '"+categoryName+"')]"));
		topCategory.findElement(By.tagName("a")).click();
		WebDriverUtil.waitForElementPresent(driver, By.xpath("//div[@class='category-headline']//li[contains(., '"+categoryName+"')][@class='active']"), 10);
	}
	
	public void filterByLink(String linkText){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.category-headline"), 10);
		LOG.info("filter by "+linkText);
		driver.findElement(By.linkText(linkText)).click();
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("span.filter-select-category"), 10);
	}
	
	public void filterByBrand(String linkText){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.category-headline"), 10);
		LOG.info("filter by "+linkText);
		
		//get more brand
		if(WebDriverUtil.verifyElementExist(driver, By.cssSelector("span.text-more"))){
			moreIcon.click();
			WebDriverUtil.waitForElementVisible(driver, hideIcon, 10);
		}

		driver.findElement(By.linkText(linkText)).click();
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("span.filter-select-category"), 10);
	}
	
	public void filterByAllTopCategories(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.category-headline"), 10);
		for(int i =0;i<topCategories.size();i++){
			WebElement topCategory = topCategories.get(i);
			LOG.info("filter by: "+topCategory.getText());
						
			String categoryName = topCategory.getText();
			
			//don't check sports category for now
			if(categoryName.contains("运动户外")){
				break;
			}
			
			topCategory.findElement(By.tagName("a")).click();
					
			WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.category-headline"), 10);

			//find again after click category
			topCategories = driver.findElements(By.cssSelector("div.category-headline > ul > li"));

			assertEquals(topCategories.get(i).getAttribute("class"),"active","category switch may failed");

			assertTrue(WebDriverUtil.verifyElementExist(driver, By.cssSelector(PRODUCT_IMG_CSS)),categoryName + " store list didn't get");
		}
	}
	
	public boolean verifyProductPresent(String productName){
		WebDriverUtil.waitForElementPresent(driver, By.className("product-title"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.xpath("//div[@class='product-title'][contains(., '"+productName+"')]"));
	}
	
	public void selectPage(String pageNum){
		WebDriverUtil.waitForElementPresent(driver, By.className("pagebar"), 10);
		assertNotEquals(currentPage.getText(),pageNum,"current page equals select page");
		driver.findElement(By.cssSelector("div#pager a[href*='page="+pageNum+"']")).click();
		LOG.info("select page to "+pageNum);
	}
	
	public String getCurrentPage(){
		LOG.info("current page is: "+currentPage.getText());
		return currentPage.getText();
	}
	
	public void selectLastPage(){
		WebDriverUtil.waitForElementPresent(driver, By.className("pagebar"), 10);
		if(WebDriverUtil.verifyElementExist(driver, By.cssSelector("#pager a")) && (Integer.parseInt(lastPage.getText()) > Integer.parseInt(getCurrentPage()))){
			lastPage.click();
			LOG.info("select last page");
		}else{
			LOG.info("already in last page");
		}
	}
}
