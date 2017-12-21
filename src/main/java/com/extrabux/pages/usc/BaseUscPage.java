package com.extrabux.pages.usc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class BaseUscPage extends BasePage {
	static final String LOADING_ELEMENT_ID =  "loading";
	static final String PROGRESS_BAR_ELEMENT_ID = "progress-bar";
	static final String PURCHASE_PROGRESS_BAR_ELEMENT_ID = "progress";

	public BaseUscPage(WebDriver driver) {
		super(driver);
	}

	public void waitForCheckoutPageToLoad() {
		WebDriverUtil.waitForElementNotVisible(driver, By.id(LOADING_ELEMENT_ID), 20);
	}
	
	
	public void waitForPurchasePreviewProgressBarToComplete() {
		WebDriverUtil.waitForElementNotVisible(driver, By.id(PROGRESS_BAR_ELEMENT_ID), 120);
	}
	
	public void waitForPurchaseProgressBarToComplete() {
		WebDriverUtil.waitForElementNotVisible(driver, By.id(PURCHASE_PROGRESS_BAR_ELEMENT_ID), 120);
	}
}
