package com.extrabux.pages.usc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.extrabux.pages.BasePage;

public class OptionsPage extends BasePage {
	// button to go back to the previous option page
	WebElement previous_button;

	public OptionsPage(WebDriver driver) {
		super(driver);
	}
	
	// index starts with zero
	public void selectOptionN(int optionIndex) {
		WebElement option = driver.findElement(By.xpath("//*[@value=\"" + optionIndex + "\"]"));
		option.click();
	}

}
