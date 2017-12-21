package com.extrabux.pages.cn.mvp.alipay;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.mvp.MvpDaigouBasePage;
import com.extrabux.util.WebDriverUtil;

public class AlipayPage extends MvpDaigouBasePage{

	@FindBy(css="strong.amount-font-22")
	WebElement  amount;
	
	private static final Log LOG = LogFactory.getLog(AlipayPage.class); 
	
	public AlipayPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public double getPayAmount(){
		WebDriverUtil.waitForElementPresent(driver, By.className("alipay-logo"), 100);
		LOG.info("alipay total amout: "+amount.getText());
		return getPrice(amount.getText());
	}
}
