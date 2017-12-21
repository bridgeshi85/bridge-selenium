package com.extrabux.pages.cn.mvp;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.util.WebDriverUtil;

public class OrderConfirmPage extends MvpDaigouBasePage{
	
	@FindBy(css="table.cart-table > tbody > tr:not(:first-child)")
	List<WebElement> productList;
	
	@FindBy(id="accept-term")
	WebElement termCheckBox;
	
	@FindBy(css="a.btn.checkout-btn")
	WebElement checkOutBtn;
	
	@FindBy(css="div.extrabux-successful h6")
	WebElement orderSuccessText;
	
	@FindBy(css="li.order-total > span")
	WebElement orderTotal;
	
	@FindBy(css="li.order-total > span:nth-child(2) > strong")
	WebElement cashBackTotal;
	
	@FindBy(css="div.order-info > div")
	List<WebElement> orderInfoList;
	
	private static final Log LOG = LogFactory.getLog(OrderConfirmPage.class); 
	
	public OrderConfirmPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public boolean verifyOrderSuccess(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.successful-step.finised3"), 15);
		LOG.info(orderSuccessText.getText());
		for(WebElement orderInfo : orderInfoList){
			LOG.info(orderInfo.getText());
		}
		return WebDriverUtil.verifyElementExist(driver, By.cssSelector("span.icon.icon-successful"));
	}

	public String getOrderTotal(){
		LOG.info("order total of this order: " +orderTotal.getText());
		return orderTotal.getText();
	}
	
	public String getCashBackTotal(){
		LOG.info("cash back of this order: " +cashBackTotal.getText());
		return cashBackTotal.getText();
	}
}
