package com.extrabux.pages.usc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductPage extends BaseUscPage {
	// not the greatest but is my only option
	static final String PRODUCT_LINK_URL_XPATH = "//a[@data-product-url='%s']";
	
 	@FindBy(className = "usc-product")
	WebElement uscProduct;

	public ProductPage(WebDriver driver) {
		super(driver);
	}
	
	public CheckoutPage clickProduct(String productUrl) {
		WebElement productLink = uscProduct.findElement(By.xpath(String.format(PRODUCT_LINK_URL_XPATH, productUrl)));
		productLink.click();
		
		return new CheckoutPage(driver);
	}

}
