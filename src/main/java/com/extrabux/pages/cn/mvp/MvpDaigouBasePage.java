package com.extrabux.pages.cn.mvp;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
import com.extrabux.util.WebDriverUtil;

public class MvpDaigouBasePage {

	@FindBy(css="table.cart-table > tbody > tr:not(:first-child)")
	List<WebElement> productList;
	
	@FindBy(css="div.panel-body")
	WebElement addressInfo;
	
	@FindBy(css="div.modal-footer > a.btn.btn-highlight")
	WebElement toShopCartBtn;
	
	@FindBy(id="add-to-cart-modal")
	WebElement addToCartDailog;
	
	@FindBy(linkText="首页")
	WebElement extrabuxHomePage;
	
	protected WebDriver driver;
	private static final Log LOG = LogFactory.getLog(MvpDaigouBasePage.class); 

	public MvpDaigouBasePage(final WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void goToURL(final String url) {
		LOG.info("getting URL: " + url);
		driver.get(url);
	}
	
	public ChinaHomePageAfterLogin enterExtrabux(){
		extrabuxHomePage.click();
		LOG.info("Enter extrabux homepage");
		return new ChinaHomePageAfterLogin(driver);
	}
	
	public ShopCartPage clickToShopCartBtnInDailog(){
		WebDriverUtil.waitForElementVisible(driver, addToCartDailog, 5);
		assertTrue(WebDriverUtil.verifyElementExist(driver, By.cssSelector("i.media-object.icon-successful")),"add to cart failed");
		
		toShopCartBtn.click();
		
		return new ShopCartPage(driver);
	}
	
	//verify one row product info
	public boolean verifyProductInfo(ProductInfo product,int itemIndex){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("table.cart-table > tbody > tr:not(:first-child)"), 15);
		if(verifyProductName(itemIndex-1,product.getProductName()) &&
			verifyProductQuantity(itemIndex-1,product.getQuantity()) &&
			verifyProductPrice(itemIndex-1,product.getPrice()) &&
			verifySubTotalPrice(itemIndex-1,product.getTotalPrice()) &&
			verifyProductOption(itemIndex-1,product.getOption())
			//verifyProductCashBack(itemIndex-1,product.getProductCashBack())
		){
			LOG.info("product info all as expected");
			return true;
		}else
		{
			return false;
		}
	}
	
	public boolean verifyProductOption(int itemIndex, String optionName){
		if(optionName != null && optionName != ""){
			String actualoptionName = productList.get(itemIndex).findElement(By.className("product-attributes")).getText();
			if(actualoptionName.contains(optionName)){
				LOG.info("option name as expected");
				return true;
			}else
			{
				LOG.info("option name not as expected,actual: "+actualoptionName+" expect: "+optionName);
				return false;
			}
		}else{
			return true;
		}
	}
	
	//verify product info by store name
	public boolean verifyProductInfo(ProductInfo product,int itemIndex,String storeName){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("table.cart-table > tbody > tr:not(:first-child)"), 15);
		
		productList = driver.findElements(By.xpath("//th[contains(.,'"+storeName
		+"')]//ancestor::table[@class='cart-table']//tr[position()>1]"));
		
		if(verifyProductName(itemIndex-1,product.getProductName()) &&
			verifyProductQuantity(itemIndex-1,product.getQuantity()) &&
			verifyProductPrice(itemIndex-1,product.getPrice()) &&
			verifySubTotalPrice(itemIndex-1,product.getTotalPrice())
			//verifyProductCashBack(itemIndex-1,product.getProductCashBack())
		){
			LOG.info("product info all as expected");
			return true;
		}else
		{
			return false;
		}
	}
	
	public boolean verifyProductName(int itemIndex, String productName){
		String actualProductName = productList.get(itemIndex).findElement(By.className("title-link")).getText();
		if(actualProductName.equals(productName)){
			LOG.info("product name as expected");
			return true;
		}else
		{
			LOG.info("product name not as expected,actual: "+actualProductName+" expect: "+productName);
			return false;
		}
	}
	
	public boolean verifyProductQuantity(int itemIndex, int quantity){
		String actualProductQuantity;
		if(WebDriverUtil.verifyElementExist(driver, By.cssSelector("input.quantity"))){
			actualProductQuantity  = productList.get(itemIndex).findElement(By.className("quantity")).getAttribute("value");
		}else{
			actualProductQuantity = productList.get(itemIndex).findElement(By.cssSelector("td:nth-child(3)")).getText();
		}
		int actualProductQuantityInt = Integer.parseInt(actualProductQuantity);
		if(actualProductQuantityInt == quantity){
			LOG.info("product quantity as expected");
			return true;
		}else
		{
			LOG.info("product quantity not as expected,actual: "+actualProductQuantity+" expect: "+quantity);
			return false;
		}
	}
	
	public boolean verifyProductPrice(int itemIndex, double productPrice){
		double actualProductPrice = getPrice(productList.get(itemIndex).findElement(By.className("price")).getText());
		
		if(actualProductPrice == productPrice){
			LOG.info("product price as expected");
			return true;
		}else
		{
			LOG.info("product price not as expected,actual: "+actualProductPrice+" expect: "+productPrice);
			return false;
		}
	}
	
	public boolean verifyProductCashBack(int itemIndex, double productCashBack){
		double actualCashBack = getPrice(productList.get(itemIndex).findElement(By.cssSelector("table.cart-table td:nth-child(5)")).getText());
		
		if(actualCashBack == productCashBack){
			LOG.info("product cash back as expected");
			return true;
		}else
		{
			LOG.info("product cash back not as expected,actual: "+actualCashBack+" expect: "+productCashBack);
			return false;
		}
	}
	
	public double getPrice(String str){
		 
	    Pattern p=Pattern.compile("(\\d+(?:\\.\\d{1,2})?)");
	    Matcher m=p.matcher(str);
	    String findString = "";
	    if(m.find()){
	    	findString = m.group(0);
	    }
	    
		return Double.parseDouble(findString);
	}
	
	public boolean verifySubTotalPrice(int itemIndex, double totalPrice){
		double actualSubtoalPrice = getPrice(productList.get(itemIndex).findElement(By.cssSelector("td:nth-of-type(4) span.subtotal")).getText());
		
		if(actualSubtoalPrice == totalPrice){
			LOG.info("product subtoal price as expected");
			return true;
		}else
		{
			LOG.info("product subtotal price not as expected,actual: "+actualSubtoalPrice+" expect: "+totalPrice);
			return false;
		}
	}
	
	//verify all address info
	public boolean verifyAddressInfo(ShippingAddressInfo addressInfo){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.panel-body"), 15);
		if(verifyUserName(addressInfo.getUserName()) &&
			verifyEmail(addressInfo.getEmail()) &&
			verifyAddress(addressInfo.getDetailedAddress()) &&
			//verifyZipCode(addressInfo.getZipCode()) &&
			//verifyTelePhoneNumber(addressInfo.getTelePhoneNumber()) &&
			verifyMobilePhoneNumber(addressInfo.getMobilePhoneNumber())
		){
			LOG.info("address info all as expected");
			return true;
		}else
		{
			return false;
		}

	}
	
	public boolean verifyUserName(String userName){
		String actualUserName = addressInfo.findElement(By.xpath("//span[contains(., '收货人姓名')]")).getText();
		if(actualUserName.contains(userName)){
			LOG.info("user name as expected");
			return true;
		}else
		{
			LOG.info("user name not as expected,actual: "+actualUserName+" expect: "+userName);
			return false;
		}
	}
	
	public boolean verifyEmail(String email){
		String actualEmail = addressInfo.findElement(By.xpath("//span[contains(., '电子邮件地址')]")).getText();
		if(actualEmail.contains(email)){
			LOG.info("email as expected");
			return true;
		}else
		{
			LOG.info("email not as expected,actual: "+actualEmail+" expect: "+email);
			return false;
		}
	}
	
	public boolean verifyAddress(String address){
		String actualAddress = addressInfo.findElement(By.xpath("//span[contains(., '详细地址')]")).getText();
		if(actualAddress.contains(address)){
			LOG.info("address as expected");
			return true;
		}else
		{
			LOG.info("address not as expected,actual: "+actualAddress+" expect: "+address);
			return false;
		}
	}
	
	public boolean verifyZipCode(String zipCode){
		String actualZipCode = addressInfo.findElement(By.xpath("//span[contains(., '邮政编码')]")).getText();
		if(actualZipCode.contains(zipCode)){
			LOG.info("zipCode as expected");
			return true;
		}else
		{
			LOG.info("zipCode not as expected,actual: "+actualZipCode+" expect: "+zipCode);
			return false;
		}
	}
	
	public boolean verifyTelePhoneNumber(String telePhoneNumber){
		String actualTelePhoneNumber = addressInfo.findElement(By.xpath("//span[contains(., '电话')]")).getText();
		if(actualTelePhoneNumber.contains(telePhoneNumber)){
			LOG.info("telePhoneNumber as expected");
			return true;
		}else
		{
			LOG.info("telePhoneNumber not as expected,actual: "+actualTelePhoneNumber+" expect: "+telePhoneNumber);
			return false;
		}
	}
	
	public boolean verifyMobilePhoneNumber(String mobilePhoneNumber){
		String actualMobilePhoneNumber = addressInfo.findElement(By.xpath("//span[contains(., '手机')]")).getText();
		if(actualMobilePhoneNumber.contains(mobilePhoneNumber)){
			LOG.info("mobilePhoneNumber as expected");
			return true;
		}else
		{
			LOG.info("mobilePhoneNumber not as expected,actual: "+actualMobilePhoneNumber+" expect: "+mobilePhoneNumber);
			return false;
		}
	}
}
