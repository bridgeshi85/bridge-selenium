package com.extrabux.pages.cn;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class ChinaNewSignUpPage extends ChinaSignUpPage {

	static final String SIGNUP_URI = "/users/register?sl=1";

	public ChinaNewSignUpPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getUrl(String serverName) {
		return "http://" + serverName + SIGNUP_URI;
	}

}
