package com.extrabux.pages.cn.account;

import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.apache.commons.logging.Log;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class ChinaInquiriesPage extends BasePage{

	private static final Log LOG = LogFactory.getLog(ChinaInquiriesPage.class); 
	
	@FindBy(css="input.submit.gradient.orangeButton.shadowedButton")
	WebElement removePurchaseSubmitButton;
	
	@FindBy(css = "td.status")
	List<WebElement> purchaseStatusList;
	
	@FindBy(xpath = "//td/div[@class='removePurchase']")
	List<WebElement> removePurchaseIcons;
	
	public ChinaInquiriesPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public int getUnConfirmedPurchaseTotal() throws Exception{
		try{
			WebDriverUtil.waitForElementPresent(driver, By.xpath("//td/div[@class='removePurchase']"), 10);
		}catch(TimeoutException te){
			LOG.info("timed out waiting for remove icon to display. possible no unconfirmed purchase");
		}
		return removePurchaseIcons.size();
	}
	
	public void removePurchase(){
		hoverAndClickRemoveIcon();
		removePurchaseSubmitButton.click();
		WebDriverUtil.waitForElementPresentAndVisible(driver, By.cssSelector(".slidingMessage.shadow.good"), 10);
		driver.navigate().refresh();
	}
	
	private void hoverAndClickRemoveIcon()
	{
		Actions actions = new Actions(driver);
		actions.moveToElement(purchaseStatusList.get(0)).perform();
		removePurchaseIcons.get(0).click();
	}
}
