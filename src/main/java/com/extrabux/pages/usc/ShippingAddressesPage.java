package com.extrabux.pages.usc;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.extrabux.pages.BasePage;

public class ShippingAddressesPage extends BasePage {
	@FindBy(name = "address_id")
	List<WebElement> addresses;
	WebElement addNewAddressBtn;
	
	public ShippingAddressesPage(WebDriver driver) {
		super(driver);
	}
}
