package com.extrabux.pages.usc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.extrabux.pages.BasePage;

public class CreditCardListPage extends BasePage {
	// not the greatest but is my only option
	static final String CARD_RADIO_BUTTON_XPATH = "//input[@name='credit_card_id']/../label[b='%s']/preceding-sibling::*";
	
	@FindBy(linkText = "Add New Credit Card")
	WebElement addNewCreditCardLink;
	@FindBy(name = "credit_card_id")
	WebElement creditCardItem;

	public CreditCardListPage(WebDriver driver) {
		super(driver);
	}
	
	public void selectFirstCreditCard() {
		creditCardItem.click();
	}
	
	public void selectCreditCardByNickName(String nickName) {
		driver.findElement(By.xpath(String.format(CARD_RADIO_BUTTON_XPATH, CARD_RADIO_BUTTON_XPATH))).click();;
	}

}
