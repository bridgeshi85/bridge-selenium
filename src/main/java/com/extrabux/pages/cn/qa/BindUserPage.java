package com.extrabux.pages.cn.qa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;

public class BindUserPage extends BasePage{

	private static final Log LOG = LogFactory.getLog(BindUserPage.class);

	@FindBy(css = "button.btn.btn-large.btn-blue.btn-block")
	WebElement confirmButton;
	
	public BindUserPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public QAHomePageAfterLogin submit(){
		confirmButton.click();
		LOG.info("user bind success");
		return new QAHomePageAfterLogin(driver);
	}

}
