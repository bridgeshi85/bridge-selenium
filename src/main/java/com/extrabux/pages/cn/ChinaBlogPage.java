package com.extrabux.pages.cn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class ChinaBlogPage extends BasePage {
	private static final Log LOG = LogFactory.getLog(ChinaBlogPage.class);
	
	@FindBy(css = "h2")
	WebElement blogTitle;

	public ChinaBlogPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getBlogTitle(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("h2"), 5);
		LOG.info("Blog title on the page is "+ blogTitle.getText());
		return blogTitle.getText();
	}
	
}
