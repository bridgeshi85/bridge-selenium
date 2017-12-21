package com.extrabux.pages.cn.promotion;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.ChinaHomePageBeforeLogin;

public class SpdbPromotionPageBeforeLogin extends ChinaHomePageBeforeLogin{
	
	@FindBy(css="input.startSavingButton.orangeButton.shadowedButton.gradient")
	WebElement submitButton;
	
	static final String URI = "/spdb";
	
	public SpdbPromotionPageBeforeLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);	
	}
	
	public SpdbPromotionPageAfterLogin signUp(String username, String pw, String signUpCode) {
		fillForm(username, pw, signUpCode);
		return submit();
	}

	public SpdbPromotionPageAfterLogin submit(){
		//submitButton.click();
		//click sometime dosen't work,try use enter key
		submitButton.sendKeys(Keys.ENTER);
		return new SpdbPromotionPageAfterLogin(driver);
	}
	
	public String getUrl(String serverName) {
		return "http://" + serverName + URI;
	}
}
