package com.extrabux.pages.usc;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.extrabux.pages.BasePage;

public class ShippingMethodPage extends BasePage {

	public ShippingMethodPage(WebDriver driver) {
		super(driver);
	}
	
	public void selectShippingOptionN(int optionIndex) {
		WebElement option = driver.findElement(By.xpath("//*[@id=\"product-step\"]/div[2]/form/ul/li[\"" + optionIndex + "\"]/input"));
		option.click();
	}
	
	// get shipping method options
	// should this be strings?
	public List<String> getShippingOptions() {
		List<String> shippingOptions = new ArrayList<String>();
		
		return shippingOptions;
	}
}
