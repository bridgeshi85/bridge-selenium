package com.extrabux.pages.usc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class PurchasePage extends BasePage {
	static final String PURCHASE_CONFIRMATION_TEXT = "Purchase Successful";
	
	@FindBy(className = "purchase-confirmation-header")
	WebElement successMessage;
	@FindBy(className = "btn")
	WebElement closeWindowBtn;

	public PurchasePage(WebDriver driver) {
		super(driver);
	}

	public void verifyPurchaseSuccessful() {
		WebDriverUtil.waitForTextPresentInElement(driver, successMessage, PURCHASE_CONFIRMATION_TEXT, 30);
	}
	
	public void closeWindow() {
		closeWindowBtn.click();
	}

}
