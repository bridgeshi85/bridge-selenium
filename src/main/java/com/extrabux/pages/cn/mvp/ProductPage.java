package com.extrabux.pages.cn.mvp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.util.WebDriverUtil;

public class ProductPage extends MvpDaigouBasePage{

	static final String GOODS_ID_URI = "/goods-";
	
	// add to cart button
	@FindBy(css="*.add-to-cart-btn")
	WebElement addToCartButton;
	
	@FindBy(css="form#ECS_FORMBUY h1")
	WebElement productName;
	
	@FindBy(css="input#number")
	WebElement quantityControl;
	
	@FindBy(css="div.plus-control")
	WebElement plusControl;
	
	@FindBy(css="div.modal-dialog div.bootbox-body")
	WebElement dialogTextBox;
	
	@FindBy(id="quantity-control-msg")
	WebElement quantiyControlMessage;
	
	@FindBy(id="ECS_SHOPPRICE")
	WebElement price;
	
	private static final Log LOG = LogFactory.getLog(ProductPage.class); 
	
	public ProductPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public String getProductUrl(String serverName,int productId){
		return "https://" + serverName + GOODS_ID_URI+productId+".html";
	}
	
	public ShopCartPage addToCart(int quantity){
		WebDriverUtil.waitForElementPresentAndClickable(driver, By.cssSelector("*.add-to-cart-btn.btn"), 10);
		if(quantity>1)
			typeQuantity(quantity);
		
		LOG.info("add goods to cart.....");
		addToCartButton.click();
		
		return clickToShopCartBtnInDailog();
	}
	
	public CheckOutPage addToCart(){
		WebDriverUtil.waitForElementPresentAndClickable(driver, By.cssSelector("*.add-to-cart-btn.btn"), 10);
		
		LOG.info("add goods to cart.....");
		addToCartButton.click();
		
		return new CheckOutPage(driver);
	}
	
	public void addToCartWithOutOfStock(int quantity){
		WebDriverUtil.waitForElementPresentAndClickable(driver, By.cssSelector("*.add-to-cart-btn.btn"), 10);
		if(quantity>1)
			typeQuantity(quantity);
		
		LOG.info("add goods to cart.....");
		addToCartButton.click();
	}
	
	public String getProductName(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("form#ECS_FORMBUY"), 30);
		return productName.getText();
	}
	
	public void typeQuantity(int quantity){
		//LOG.debug("typing product quantity: " + String.valueOf(quantity) + " into nubmer input.");
		for(int i = 1;i<quantity;i++){
			//LOG.debug("click plus button");
			plusControl.click();
		}
		
		LOG.info("set quantity to "+quantity);
		/*quantityControl.click();
		WebDriverUtil.waitForElementToActive(driver, quantityControl, 10);
		
		//double fail-safe
		quantityControl.sendKeys(Keys.BACK_SPACE);
		quantityControl.clear();
		
		quantityControl.sendKeys(String.valueOf(quantity));*/
	}
	
	public void selectOption(String optionName){
		WebElement option = driver.findElement(By.xpath("//ul[@class='inline']//a[contains(., '"+optionName+"')]"));
		option.click();
		LOG.info("select "+optionName+" option");
		WebDriverUtil.waitForElementAttributeToChange(driver, option.findElement(By.xpath("..")), 15, "class", "selected");
	}
	
	public String getDailogText(){
		WebDriverUtil.waitForElementPresent(driver, By.className("bootbox-body"), 20);
		return dialogTextBox.getText();
	}
	
	public String getQuantityControlMsg(){
		WebDriverUtil.waitForElementPresent(driver, By.id("quantity-control-msg"), 10);
		LOG.info("out of stock:"+quantiyControlMessage.getText());
		return quantiyControlMessage.getText();
	}
	
	public double getProductPrice(){
		LOG.info("product price is :"+price.getText());
		return getPrice(price.getText());
	}
}
