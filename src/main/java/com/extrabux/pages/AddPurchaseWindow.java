package com.extrabux.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.extrabux.util.WebDriverUtil;

public class AddPurchaseWindow extends BasePage{

	private static final String ADD_PURCHASE_MODAL_ID = "addPurchaseModal";
	private static final String PURCHASE_DATE_ID = "clickId";
	
	@FindBy(name = "purchase_subtotal")
	WebElement purchaseSubTotal;
	
	@FindBy(name = "order_id")
	WebElement purchaseOrderId;
	
	@FindBy(id = "order_confirmation")
	WebElement purchaseOrderConfirmation;
	
	@FindBy(className = "purchaseDatePrepop")
	WebElement purchaseDateRadio;
	
	@FindBy(css="input.submit.gradient.orangeButton")
	WebElement submitButton;
	
	@FindBy(id = PURCHASE_DATE_ID)
	WebElement purchaseDateSelect;
	
	public AddPurchaseWindow(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public PostPurchasePage addPurchase(String subTotal,String orderId,String orderConfirmation)
	{
		//wait for window
		WebDriverUtil.waitForElementPresent(driver, By.id(ADD_PURCHASE_MODAL_ID), 5);
		selectDate();
		typePurchaseInfo(subTotal,orderId,orderConfirmation);
		
		return submit();
	}
	
	public void typePurchaseInfo(String subTotal,String orderId,String orderConfirmation)
	{
		purchaseSubTotal.sendKeys(subTotal);
		purchaseOrderId.sendKeys(orderId);
		purchaseOrderConfirmation.click();
		purchaseOrderConfirmation.sendKeys(orderConfirmation);
	}
	
	public void selectDate(){
		WebDriverUtil.waitForElementPresent(driver, By.id(PURCHASE_DATE_ID), 3);
		
		new Select(purchaseDateSelect).selectByIndex(1);
	}
	
	public PostPurchasePage submit()
	{
		submitButton.click();
		return new PostPurchasePage(driver);
	}
}
