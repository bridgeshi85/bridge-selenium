package com.extrabux.pages.usc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PurchasePreviewPage extends BaseUscPage {
	WebElement purchase;
	@FindBy(id = "purchase-with-alipay")
	WebElement purchaseWithAlipay;

	public PurchasePreviewPage(WebDriver driver) {
		super(driver);
	}
	
	public PurchasePage clickPurchase() {
		purchase.click();
		return new PurchasePage(driver);
	}
	
	public void clickPurchaseWithAlipay() {
		purchaseWithAlipay.click();
	}

}
