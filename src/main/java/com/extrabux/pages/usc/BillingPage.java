package com.extrabux.pages.usc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.extrabux.pages.BasePage;

public class BillingPage extends BasePage {
	@FindBy(id = "billing_type-alipay")
	WebElement alipayRadioBtn;
	@FindBy(id = "billing_type-credit_card")
	WebElement creditCardRadioBtn;

	public BillingPage(WebDriver driver) {
		super(driver);
	}
	
	public void selectAlipayBilling() {
		alipayRadioBtn.click();
		// TODO alipay takes you to external site.  figure out what this should do.
	}
	
	public CreditCardListPage selectCreditCardExpectExistingCards() {
		creditCardRadioBtn.click();
		return new CreditCardListPage(driver);
	}
	
	public CreateCreditCardPage selectCreditCardPaymentNoExisting() {
		creditCardRadioBtn.click();
		return new CreateCreditCardPage(driver);
	}
	
}
