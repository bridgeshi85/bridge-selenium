package com.extrabux.pages.cn;

import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.apache.commons.logging.LogFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class ChinaCouponPage extends BasePage{
	private static final Log LOG = LogFactory.getLog(ChinaCouponPage.class);
	
	@FindBy(css = "a.dealTitle.transferLink")
	WebElement couponTitle;
		
	public ChinaCouponPage(final WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getCouponName(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("a.dealTitle.transferLink"), 5);
		LOG.info("Coupon name on the page is "+ couponTitle.getText());
		return couponTitle.getText();
	}

}
