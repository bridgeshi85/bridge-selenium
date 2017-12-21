package com.extrabux.pages.cartwidget;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductInfoDialog extends BasePage {
	@FindBy(className = "merchant-info")
	WebElement merchantInfo;
	@FindBy(className = "product-title")
	WebElement productTitle;
	@FindBy(className = "product-price")
	WebElement productPrice;
	@FindBy(className = "option-item")
	List<WebElement> productOptions;
	@FindBy(className = "minus")
	WebElement minus;
	@FindBy(className = "plus")
	WebElement plus;
	@FindBy(className = "addToCartBtn")
	WebElement addToCartBtn;

	public ProductInfoDialog(WebDriver driver) {
		super(driver);
	}
	
	public String getMerchantName() {
		return merchantInfo.getText().replace("Merchant:", "");
	}
	
	public String getProductTitle() {
		return productTitle.getText();
	}
	
	public String getProductPrice() {
		return productPrice.getText().replace("$", "");
	}
	
	public void clickAddToCart() {
		addToCartBtn.click();
	}
	
	public List<String> getProductOptions() {
		// TODO figure out how to do this
		return new ArrayList<String>();
	}
	
	public void clickMinus() {
		minus.click();
	}
	
	public void clickPlus() {
		plus.click();
	}
}
