package com.extrabux.pages.cartwidget;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductPage extends BasePage {
	// not the greatest but is my only option right now
	static final String PRODUCT_LINK_URL_XPATH = "//button[@data-product-url='%s']";
	
	@FindBy(className = "usc-cart-icon")
	WebElement cartIcon;

	public ProductPage(WebDriver driver) {
		super(driver);
	}
	
	public ProductInfoDialog clickProduct(String productUrl) {
		WebElement productLink = driver.findElement(By.xpath(String.format(PRODUCT_LINK_URL_XPATH, productUrl)));
		productLink.click();
		
		return new ProductInfoDialog(driver);
	}
	
	public int getCartItemCount() {
		return Integer.parseInt(cartIcon.getText());
	}

}
