package com.extrabux.pages.cn.account;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.account.AccountPage;
import com.extrabux.pages.cn.ChinaAddPurchaseWindow;
import com.extrabux.util.WebDriverUtil;

public class ChinaTrackingHistoryPage extends AccountPage{

	private static final Log LOG = LogFactory.getLog(ChinaTrackingHistoryPage.class);
	
	@FindBy(className="confirm-purchase")
	WebElement confirmPurchase;
	
	@FindBy(css="button.btn.confirm")
	WebElement confirmButton;
	
	@FindBy(className = "icon-caret-o")
	WebElement hasPurchaseIcon;
	
	@FindBy(className = "addPurchaseLink")
	WebElement addPurcahseLink;
	
	public ChinaTrackingHistoryPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public ChinaAddPurchaseWindow addPruchase(){
		WebDriverUtil.scrollPage(driver, "0", "250");
		confirmPurchase.click();
		confirmButton.click();
		WebDriverUtil.waitForElementPresent(driver, By.className("addPurchaseLink"), 5);
		addPurcahseLink.click();
		LOG.info("open add purchase window successfully........");
		return new ChinaAddPurchaseWindow(driver);
	}
	
	public boolean verifyPurchaseLinkPresent()throws Exception{
		try
		{
			WebDriverUtil.waitForElementPresent(driver, By.className("confirm-purchase"), 5);
			if(!confirmPurchase.isDisplayed())
				return false;
		}catch (NoSuchElementException e) {
			throw new Exception("add purchase link was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}catch(TimeoutException e)
		{
			throw new Exception("timed out waiting for add purchase link to display. element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		return true;
	}
	
}
