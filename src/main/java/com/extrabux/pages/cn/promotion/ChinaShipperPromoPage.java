package com.extrabux.pages.cn.promotion;

import org.openqa.selenium.By;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.util.WebDriverUtil;


public class ChinaShipperPromoPage extends ChineseNewYearPage{
	
	@FindBy(css="#fancybox-content div.modal-luckybag")
	WebElement couponBox;
	
	static final String SHIPPER_PROMO_URI = "/campaign/forwarder";
	private static final Log LOG = LogFactory.getLog(ChinaShipperPromoPage.class); 
	
	public ChinaShipperPromoPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getUrl(String serverName) {
		return "https://" + serverName + SHIPPER_PROMO_URI;
	}

	public String shipperLuckyDraw() {
		WebDriverUtil.waitForElementPresentAndClickable(driver, By.className("spinner"), 30);
		spinnerButton.click();
		LOG.info("start luck draw....");
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("#fancybox-content div.modal-luckybag"), 60);
		WebDriverUtil.waitForElementClickable(driver, fancyBoxCloseButton, 30);
		if(WebDriverUtil.verifyElementExist(driver, By.cssSelector("#fancybox-content div.modal-luckybag[id$='0']"))){
			return "谢谢参与";
		}else{
			return couponBox.getText();
		}
	}
	
	public void closeCouponBox(){
		fancyBoxCloseButton.click();
		WebDriverUtil.waitForElementNotVisible(driver, By.id("fancybox-overlay"), 10);
	}
}
