package com.extrabux.pages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.extrabux.util.WebDriverUtil;

public class StoreTransferPage extends BasePage {
	private static final Log LOG = LogFactory.getLog(StoreTransferPage.class);

	// transfer box
	@FindBy(id = "transfer-success")
	WebElement transferBox;
	// skip link
	WebElement directLink;
	WebElement redirectTimer;
	// where user's name is displayed
	WebElement personalThanks;
	// not you link
	@FindBy(className = "notYou")
	WebElement notYou;

	public StoreTransferPage(WebDriver driver) {
		super(driver);
	}

	public boolean verifyOnTransferPage() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("transfer-success")));
		return transferBox.isDisplayed();
	}
	
	public boolean stopInterstitialLoadVerifyOnTransferPageThenTransfer() {
		boolean transferSuccess;
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("window.stop();");
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("transfer-success")));
		transferSuccess = transferBox.isDisplayed();
		clickSkip();
		
		return transferSuccess;
	}

	public boolean verifyOnMerchantSite(String storeUrlFragment) {
		return driver.getCurrentUrl().contains(storeUrlFragment);
	}

	public void waitForTransfer() {
		// TODO make this configurable
		WebDriverUtil.waitForElementVisible(driver, redirectTimer, 10);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		LOG.info("waiting to be transferred to merchant site...");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("transfer-success")));
		LOG.info("done waiting");                
	}
	
	public String getDirectLink(){
		LOG.info("direct link is : "+directLink.getAttribute("href"));
		return directLink.getAttribute("href");
	}
	
	public void clickSkip() {
		directLink.click();
	}

}
