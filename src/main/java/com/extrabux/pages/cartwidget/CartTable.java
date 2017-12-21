package com.extrabux.pages.cartwidget;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CartTable extends BasePage {
	@FindBy(className = "subtotal-price")
	WebElement cartSubtotal;

	public CartTable(WebDriver driver) {
		super(driver);
	}
	
	public String getCartSubtotal() {
		return cartSubtotal.getText().replace("Total: $", "");
	}

}
