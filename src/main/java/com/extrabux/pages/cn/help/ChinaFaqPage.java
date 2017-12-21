package com.extrabux.pages.cn.help;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class ChinaFaqPage extends BasePage{
	
	@FindBy(css = "a.dealTitle.transferLink")
	WebElement couponTitle;
		
	@FindBy
	WebElement question;
	
	public ChinaFaqPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public boolean verifyQuestionPresent(String faq){
		WebDriverUtil.waitForElementPresent(driver, By.id("faqPage"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.xpath("//div[@class='question'][contains(., '"+faq+"')]"));
	}
}
