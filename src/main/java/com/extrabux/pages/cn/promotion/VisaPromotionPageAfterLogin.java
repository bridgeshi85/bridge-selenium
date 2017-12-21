package com.extrabux.pages.cn.promotion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.ChinaHomePageAfterLogin;

public class VisaPromotionPageAfterLogin extends ChinaHomePageAfterLogin{

	
	@FindBy(css="a.startSavingButton.orangeButton.shadowedButton.gradient")
	WebElement bindCardButton;
	
	
	private static final Log LOG = LogFactory.getLog(VisaPromotionPageAfterLogin.class);
	
	public VisaPromotionPageAfterLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);	
	}

	public String getPromotionStatus() throws Exception{
		String acctualStatus = bindCardButton.getText();
		LOG.info("actual bind status on page: " + acctualStatus);
		return acctualStatus;
	}
	
}
