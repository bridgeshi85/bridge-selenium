package com.extrabux.pages.cn.account;

import static org.testng.AssertJUnit.assertTrue;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.account.MyEarningsPage;
import com.extrabux.util.WebDriverUtil;

public class ChinaMyCouponsPage extends MyEarningsPage {
	
	static final String MY_COUPONS_URI = "/users/activities";
	private static final Log LOG = LogFactory.getLog(ChinaMyCouponsPage.class);
	// tabs
	@FindBy(linkText = "我添加的订单")
	WebElement addedByMeTab;
	
	@FindBy(css=".coupon-list tr:not(:first-of-type)")
	List<WebElement> couponsList;
	
	public ChinaMyCouponsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getUrl(String serverName) {
		return "http://" + serverName + MY_COUPONS_URI;
	}
	
	public boolean verifyCouponList(List<String> couponNameList){
		int i = 0;
		for(WebElement coupon:couponsList){
			coupon.findElement(By.cssSelector("a[id^='showMoreLink']")).click();
			WebElement couponDetail = coupon.findElement(By.cssSelector(".moreLessSlider"));
			LOG.info(couponDetail.getText());
			if(!couponDetail.getText().contains(couponNameList.get(couponsList.size()-1-i))){
				LOG.info(couponNameList.get(couponsList.size()-1-i));
				return false;
			}
			i++;
		}
		return true;
	}
}
