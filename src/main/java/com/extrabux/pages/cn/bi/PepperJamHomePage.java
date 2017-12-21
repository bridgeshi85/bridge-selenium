package com.extrabux.pages.cn.bi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class PepperJamHomePage extends BasePage {	

	private static final Log LOG = LogFactory.getLog(PepperJamHomePage.class);
	private static final String ADVERTISER_XPATH = "//li[@class='first']/a";
	
	@FindBy(xpath = ADVERTISER_XPATH)
	WebElement advertiser;
	
	@FindBy(linkText = "My Advertisers")
	WebElement myAdvertiser;
	
	public PepperJamHomePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public PepperJamAdPage goToAdvertisersPage() throws Exception {
		Actions actions = new Actions(driver);
		WebDriverUtil.waitForElementPresent(driver, By.xpath(ADVERTISER_XPATH), 5);
		actions.moveToElement(advertiser).perform();
		
		myAdvertiser.click();
		LOG.info("Enter My Advertiser Page。。。。。");
		return new PepperJamAdPage(driver);
	}
}
