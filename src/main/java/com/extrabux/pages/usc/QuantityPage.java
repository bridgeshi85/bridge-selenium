package com.extrabux.pages.usc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.extrabux.pages.BasePage;

public class QuantityPage extends BasePage {
	
	public QuantityPage(WebDriver driver) {
		super(driver);
	}
	
	public CheckoutPage selectQuantity(int quantity) {
		WebElement quantityRadio = driver.findElement(By.id("quantity_" + quantity));
		quantityRadio.click();
		
		return new CheckoutPage(driver);		
	}

}
