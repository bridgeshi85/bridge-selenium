package com.extrabux.pages.cn.account;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.account.MyEarningsPage;
import com.extrabux.util.WebDriverUtil;

public class ChinaMyEarningsPage extends MyEarningsPage {
	static final String MY_EARNINGS_URI = "/users/profile";
	private static final Log LOG = LogFactory.getLog(ChinaMyEarningsPage.class);
	// tabs
	@FindBy(linkText = "我添加的订单")
	WebElement addedByMeTab;
	
	// tabs
	@FindBy(linkText = "点击记录")
	WebElement trackingHistoryLink;
	
	@FindBy(css = "table.cashbackOverview tr > td:nth-child(2)")
	WebElement cashBack;
	
	@FindBy(xpath="//li[span[@class='icon-vip-bonus']]")
	WebElement vipBonusAmount;
	
	@FindBy(xpath="//div[@class='bonus'][i[@class='icon-visa-plus']]//span[@class='bonus-amount']")
	WebElement visaBonusAmount;
	
	@FindBy(css="div.user-new-year-red-pocket a.successed")
	WebElement redPocket;
	
	public ChinaMyEarningsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public ChinaInquiriesPage clickAddedByMeTab() {
		WebDriverUtil.waitForElementPresent(driver, By.linkText("我添加的订单"), 10);
		addedByMeTab.click();
		return new ChinaInquiriesPage(driver);
	}
	
	public ChinaTrackingHistoryPage clickHistory() {
		WebDriverUtil.waitForElementPresent(driver, By.linkText("点击记录"), 10);
		trackingHistoryLink.click();
		LOG.info("Click tracking history......");
		return new ChinaTrackingHistoryPage(driver);
	}
	
	public String getTotalCashBack(){
		WebDriverUtil.waitForElementPresent(driver, By.id("userTotals"), 10);
		return cashBack.getText();
	}
	
	public String getVipBonus(){
		WebDriverUtil.waitForElementPresent(driver, By.id("profile"), 10);
		return vipBonusAmount.getText();
	}
	
	public String getVisaBonus(){
		WebDriverUtil.waitForElementPresent(driver, By.id("profile"), 10);
		return visaBonusAmount.getText();
	}
	
	// TODO make this better
	public String getUrl(String serverName) {
		return "http://" + serverName + MY_EARNINGS_URI;
	}

	public String getRedPocket(){
		WebDriverUtil.waitForElementPresent(driver, By.className("user-new-year-red-pocket"), 10);
		LOG.info("red pocket is:"+ redPocket.findElement(By.tagName("strong")).getText());
		return redPocket.findElement(By.tagName("strong")).getText();
	}
	
	public boolean puzzlePresent(){
		WebDriverUtil.waitForElementPresent(driver, By.className("main-col-content"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.cssSelector("div.all-puzzles >.puzzle.lighted"));
	}
}
