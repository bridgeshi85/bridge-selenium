package com.extrabux.pages.usc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BaseUscPage {
	@FindBy(css = "*[data-sid='options']") 
	WebElement optionsLink;
	@FindBy(css = "*[data-sid='quantity']")
	WebElement quantityLink;
	@FindBy(css = "*[data-sid='shipping-addresses']")
	WebElement shippingAddressLink;
	@FindBy(css = "*[data-sid='shipping-methods']")
	WebElement shippingMethodLink;
	@FindBy(css = "*[data-sid='billing']")
	WebElement billingLink;
	@FindBy(id = "purchase-preview")
	WebElement purchasePreview;
	WebElement purchase;

	public CheckoutPage(WebDriver driver) {
		super(driver);
	}
	
	public OptionsPage clickOptionsLink() {
		optionsLink.click();
		return new OptionsPage(driver);
	}

	public QuantityPage clickQuantityLink() {
		quantityLink.click();		
		return new QuantityPage(driver);
	}
	
	public EnterNewShippingAddressPage clickShippingAddressLink() {
		shippingAddressLink.click();
		return new EnterNewShippingAddressPage(driver);
	}
	
	public ShippingMethodPage clickShippingMethodLink() {
		shippingMethodLink.click();
		return new ShippingMethodPage(driver);
	}
	
	public BillingPage clickBillingLink() {
		billingLink.click();
		return new BillingPage(driver);
	}
	
	public PurchasePreviewPage clickPurchasePreviewBtn() {
		purchasePreview.click();
		return new PurchasePreviewPage(driver);
	}

}
