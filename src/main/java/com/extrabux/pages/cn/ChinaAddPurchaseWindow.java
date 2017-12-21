package com.extrabux.pages.cn;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.AddPurchaseWindow;
import com.extrabux.pages.PostPurchasePage;
import com.extrabux.util.WebDriverUtil;

public class ChinaAddPurchaseWindow extends AddPurchaseWindow{

	private static final String ADD_PURCHASE_MODAL_ID = "addPurchaseModal";
	
	public ChinaAddPurchaseWindow(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public PostPurchasePage addPurchase(String subTotal,String orderId,String orderConfirmation)
	{
		//wait for window
		WebDriverUtil.waitForElementPresent(driver, By.id(ADD_PURCHASE_MODAL_ID), 5);

		typePurchaseInfo(subTotal,orderId,orderConfirmation);
		
		return submit();
	}	
	
}
