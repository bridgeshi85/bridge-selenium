package com.extrabux.pages.cn;

import java.util.List;

import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.apache.commons.logging.LogFactory;

import com.extrabux.util.WebDriverUtil;

public class ChinaFavPage extends ChinaStoresListPage{
	private static final Log LOG = LogFactory.getLog(ChinaFavPage.class);
	
	@FindBy(css="a.favoriteToggle.favorites")
	WebElement addToFav;
	
	public ChinaFavPage(final WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public boolean checkStorePresence(String storeName){
		WebDriverUtil.waitForElementPresent(driver, By.id("storeLists"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.xpath("//div[@class='store-headline']/a[contains(., \""+storeName+"\")]"));
	}
}
