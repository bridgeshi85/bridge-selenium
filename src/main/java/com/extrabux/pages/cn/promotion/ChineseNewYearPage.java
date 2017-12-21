package com.extrabux.pages.cn.promotion;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
import com.extrabux.util.WebDriverUtil;

public class ChineseNewYearPage extends ChinaHomePageAfterLogin{
	
	@FindBy(css="div#modal-qianghongbao > a")
	WebElement fancyBoxButton;
	
	@FindBy(css="a#fancybox-close")
	WebElement fancyBoxCloseButton;
	
	@FindBy(css="div.store em.countdown-time")
	List<WebElement> countDownTime;
	
	@FindBy(css="div.roulette > a.spinner")
	WebElement spinnerButton;
	
	@FindBy(css="div.member.header-layer > a > b")
	WebElement emailLoggedIn;
	
	static final String NEW_YEAR_URI = "/campaigns/2016-season-of-red";
	private static final Log LOG = LogFactory.getLog(ChineseNewYearPage.class); 
	
	public ChineseNewYearPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getUrl(String serverName) {
		return "https://" + serverName + NEW_YEAR_URI;
	}
	
	public void closeFancyBox() {
		WebDriverUtil.waitForElementPresent(driver, By.id("modal-qianghongbao"), 10);
		fancyBoxButton.click();
		LOG.info("close fancy box....");
		WebDriverUtil.waitForElementNotVisible(driver, By.id("fancybox-overlay"), 10);
	}
	
	public void luckyDraw() {
		WebDriverUtil.waitForElementPresentAndClickable(driver, By.className("spinner"), 10);
		spinnerButton.click();
		LOG.info("start luck draw....");
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div#fancybox-content div.modal-luckybag"), 60);
		WebDriverUtil.waitForElementClickable(driver, fancyBoxCloseButton, 30);
		fancyBoxCloseButton.click();
		WebDriverUtil.waitForElementNotVisible(driver, By.id("fancybox-overlay"), 10);
	}
	
	public void hoverAndSelectFromAccountMenu(WebElement subLink) {
		Actions actions = new Actions(driver);
		actions.moveToElement(emailLoggedIn).perform();

		actions.moveToElement(subLink);
		actions.click();
		actions.perform();
		
		WebDriverUtil.waitForPageToLoadComplete(driver, 10);
	}
}
