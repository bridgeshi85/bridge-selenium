package com.extrabux.pages.cn.mvp;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.mvp.alipay.AlipayPage;
import com.extrabux.util.WebDriverUtil;

public class CheckOutPage extends MvpDaigouBasePage{
	
	@FindBy(css="table.cart-table > tbody > tr:not(:first-child)")
	List<WebElement> productList;
	
	@FindBy(id="accept-term")
	WebElement termCheckBox;
	
	@FindBy(css="a.btn.checkout-btn")
	WebElement checkOutBtn;
	
	@FindBy(css="span.total.price-highlight")
	WebElement totalPrice;
	
	@FindBy(css="div.f_r.tr div.price-highlight")
	WebElement totalCashBack;
	
	@FindBy(css="a.continue-shopping.link")
	WebElement modifyAddressLink;
	
	private static final Log LOG = LogFactory.getLog(CheckOutPage.class); 
	
	public CheckOutPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void acceptTerm(){
		LOG.info("click accpet term check box.....");
		termCheckBox.click();
	}
	
	public AlipayPage submitPurchase(){
		//acceptTerm();
		WebDriverUtil.waitForElementPresentAndClickable(driver, By.cssSelector("a.btn.checkout-btn"), 15);
		LOG.info("submit purchase....");
		checkOutBtn.click();
		return new AlipayPage(driver);
	}
	
	/*public AlipayPage submitPurchaseWithHotDeal(){
		//acceptTerm();
		WebDriverUtil.waitForElementPresentAndClickable(driver, By.cssSelector("a.btn.checkout-btn"), 15);
		LOG.info("submit purchase....");
		checkOutBtn.click();
		return new AlipayPage(driver);
	}*/
	
	public double getTotalPrice(){
		LOG.info("total price:"+totalPrice.getText());
		return getPrice(totalPrice.getText());
	}
	
	public double getTotalCashBack(){
		LOG.info("total cash back:"+totalCashBack.getText());
		return getPrice(totalCashBack.getText());
	}
	
	public ShippingAddressPage goToAddressPage(){
		LOG.info("enter shipping address page....");
		modifyAddressLink.click();
		return new ShippingAddressPage(driver);
	}
}
