package com.extrabux.pages.cn.promotion;

import org.openqa.selenium.By;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.pages.cn.account.ChinaMyEarningsPage;
import com.extrabux.util.WebDriverUtil;


public class China17thAnniversaryPage extends BasePage{
	
	@FindBy(css=".icon-2016-17th-anniversary-go-to-profile")
	WebElement goToProfile;
	
	@FindBy(css=".start-game")
	WebElement startButton;
	
	static final String SEVENTEEN_PROMO_URI = "/campaigns/2016-17th-anniversary";
	private static final Log LOG = LogFactory.getLog(China17thAnniversaryPage.class); 
	
	public China17thAnniversaryPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getUrl(String serverName) {
		return "https://" + serverName + SEVENTEEN_PROMO_URI;
	}

	public ChinaMyEarningsPage luckyDraw() {
		WebDriverUtil.waitForElementPresentAndClickable(driver, By.className("start-game"), 30);
		LOG.info("start luck draw....");
		startButton.click();
		WebDriverUtil.waitForElementPresent(driver, By.id("fancybox-content"), 60);
		goToProfile.click();
		LOG.info("go to profile page");
		return new ChinaMyEarningsPage(driver);
	}
	

}
