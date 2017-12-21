package com.extrabux.pages.cn.help;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class ChinaContactSuccessPage extends BasePage{
	
	@FindBy(css = "h1.pageTitle")
	WebElement pageTitle;
		
	public ChinaContactSuccessPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public String getPageTitle(){
		WebDriverUtil.waitForElementPresent(driver, By.id("contactUsThankYouPage"), 15);
		return pageTitle.getText();
	}
}
