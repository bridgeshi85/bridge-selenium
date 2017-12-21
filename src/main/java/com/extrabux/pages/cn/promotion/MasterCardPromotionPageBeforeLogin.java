package com.extrabux.pages.cn.promotion;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.ChinaHomePageBeforeLogin;

public class MasterCardPromotionPageBeforeLogin extends ChinaHomePageBeforeLogin{
	static final String URI = "/mastercard";
	
	@FindBy(css="input.mastercard-button")
	WebElement submitButton;
	
	public MasterCardPromotionPageBeforeLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);		
	}
	
	public String getUrl(String serverName) {
		return "http://" + serverName + URI;
	}

	public MasterCardPromotionPageAfterLogin submit(){
		//submitButton.click();
		//click sometime dosen't work,try use enter key
		submitButton.sendKeys(Keys.ENTER);
		return new MasterCardPromotionPageAfterLogin(driver);
	}

	public MasterCardPromotionPageAfterLogin signUp(String username, String pw, String signUpCode) {
		fillForm(username, pw, signUpCode);
		return submit();
	}
}
