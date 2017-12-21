package com.extrabux.pages;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class MerchantPage extends BasePage{
	private static final Log LOG = LogFactory.getLog(MerchantPage.class);

	//private static final String ADD_PURCHASE_PROMPT_ID = "promptToAddPurchase";

	@FindBy(className = "description")
	WebElement storeDescription;

	@FindBy(className = "addPurchaseLink")
	WebElement addPurchaseLink;
	
	// all the coupons on the page
	@FindBy(className = "dealRow")
	List<WebElement> coupons;

	// start shopping button (the big orange one)
	// this is a bad way to identify this button but good enough for now
	@FindBy(className = "orangeButton")
	WebElement startShoppingBtn;

	@FindBy(css="div.top>p")
	WebElement cashBack;
	
	public MerchantPage(final WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public StoreTransferPage clickStartShopping() {
		clickTransferButton();
		return new StoreTransferPage(driver);
	}

	public void clickTransferButton(){
		LOG.debug("clicking start shopping page.");
		startShoppingBtn.click();
	}
	
	/*public boolean verifyAddPurchaseLinkPresent()throws Exception{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try
		{
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(ADD_PURCHASE_PROMPT_ID)));
			if(!addPurchaseLink.isDisplayed())
				return false;
		}catch (NoSuchElementException e) {
			throw new Exception("add purchase link was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}catch(TimeoutException e)
		{
			throw new Exception("timed out waiting for add purchase link to display. element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		return true;
	}*/
	
	public String getCashBack(){
		return cashBack.getText();
	}
	
	public AddPurchaseWindow addPurchaseLinkClick()
	{
		addPurchaseLink.click();
		return new AddPurchaseWindow(driver);
	}
}
