package com.extrabux.pages.cn.mvp;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.util.WebDriverUtil;

public class ShopCartPage extends MvpDaigouBasePage{
	
	@FindBy(css="a.btn.checkout-btn")
	WebElement checkOutBtn;
	
	@FindBy(css="table.cart-summary-table tr:nth-child(2) td.col-price")
	WebElement totalShippingFee;
	
	@FindBy(css="table.cart-summary-table tr:nth-child(4) td.col-price")
	WebElement totalCashBack;
	
	@FindBy(css="table.cart-summary-table tr td.col-price.price-highlight")
	WebElement totalOrderPrice;
	
	@FindBy(css="table.cart-summary-table tr td.col-price")
	WebElement totalProductPrice;
	
	@FindBy(css="div.item-card-view > div.product-title")
	List<WebElement> favProductTitle;
	
	@FindBy(css="div.bootbox.modal.bootbox-confirm.in")
	WebElement confirmDialog;
	
	@FindBy(css="button.btn.btn-primary")
	WebElement confirmBtn;
	
	@FindBy(xpath="//em[contains(.,'重量合计')]")
	WebElement totalOrderWeight;
	
	private static final Log LOG = LogFactory.getLog(ShopCartPage.class); 
	
	public ShopCartPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public CheckOutPage checkOut(){
		WebDriverUtil.waitForElementClickable(driver, checkOutBtn, 10);
		LOG.info("enter check out page....");
		checkOutBtn.click();
		return new CheckOutPage(driver);
	}
	
	public ShippingAddressPage checkOutWithoutAddress(){
		WebDriverUtil.waitForElementClickable(driver, checkOutBtn, 10);
		LOG.info("enter shipping address page....");
		checkOutBtn.click();
		return new ShippingAddressPage(driver);
	}
	
	public double getTotalShippingFee(){
		LOG.info("total shipping fee: "+ totalShippingFee.getText());
		return getPrice(totalShippingFee.getText());
	}
	
	public double getTotalCashBack(){
		
		LOG.info("total cash back: "+ totalCashBack.getText());
		return getPrice(totalCashBack.getText());
	}
	
	public double getTotalOrderPrice(){
		LOG.info("total order price: "+ totalOrderPrice.getText());
		return getPrice(totalOrderPrice.getText());
	}
	
	public double getTotalProductPrice(){
		LOG.info("total product price: "+ totalProductPrice.getText());
		return getPrice(totalProductPrice.getText());
	}
	
	public double getUSShippingFee(String storeName){
		WebDriverUtil.waitForElementPresent(driver, By.className("cart-table-info"), 10);
		
		WebElement USShippingFee = driver.findElement(By.xpath("//th[contains(.,'"+storeName+
				"')]//ancestor::table[@class='cart-table']/following-sibling::div[1]//em[contains(.,'美境运费')]"));
		
		LOG.info("us shipping fee: "+ USShippingFee.getText());

		return getPrice(USShippingFee.getText());
	}
	
	public double getTransferFee(String storeName){
		WebDriverUtil.waitForElementPresent(driver, By.className("cart-table-info"), 10);

		WebElement transferFee = driver.findElement(By.xpath("//th[contains(.,'"+storeName+
				"')]//ancestor::table[@class='cart-table']/following-sibling::div[1]//em[contains(.,'国内运费')]"));
		LOG.info("transfer fee: "+ transferFee.getText());
		return getPrice(transferFee.getText());
	}
	
	public void updateQuantiy(int quantiy,int index){
		WebDriverUtil.waitForElementPresent(driver, By.className("cart-table"), 15);
		LOG.info("set quantiy to "+quantiy);
		
		WebElement plusControl = productList.get(index).findElement(By.cssSelector("a.plus"));

		String weight = totalOrderWeight.getText();
		typeQuantity(quantiy,plusControl);
		/*quantiyInput.clear();
		quantiyInput.sendKeys(Integer.toString(quantiy));
		quantiyInput.sendKeys(Keys.ENTER);*/
		
		WebDriverUtil.waitForPageToLoadComplete(driver, 10);
		WebDriverUtil.waitForElementNotVisible(driver, By.xpath("//em[contains(.,'"+weight+"')]"), 30);
	}
	
	public void typeQuantity(int quantity,WebElement plusControl){
		//LOG.debug("typing product quantity: " + String.valueOf(quantity) + " into nubmer input.");
		for(int i = 1;i<quantity;i++){
			LOG.debug("click plus button");
			plusControl.click();
		}
	}
	
	public int getProductAmount(){
		WebDriverUtil.waitForElementPresent(driver, By.id("formCart"), 15);
		return productList.size();
	}
	
	public void moveProductToFav(int index){
		WebDriverUtil.waitForElementPresent(driver, By.className("cart-table"), 15);
		productList.get(index).findElement(By.linkText("移入收藏夹")).click();
		
		WebDriverUtil.waitForElementVisible(driver, confirmDialog, 5);
		
		confirmBtn.click();
		
		WebDriverUtil.waitForPageToLoadComplete(driver, 15);
	}
	
	public String getFavProductName(int index){
		LOG.info("fav product name: "+favProductTitle.get(index).getText());
		return favProductTitle.get(index).getText();
	}
	
	public void removeProduct(int index){
		WebDriverUtil.waitForElementPresent(driver, By.className("cart-table"), 15);
		productList.get(index).findElement(By.linkText("删除")).click();
		
		WebDriverUtil.waitForElementVisible(driver, confirmDialog, 5);
		
		confirmBtn.click();
		
		WebDriverUtil.waitForPageToLoadComplete(driver, 15);
	}
}
