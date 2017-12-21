package com.extrabux.pages.cn;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class ChinaNewLoginPage extends ChinaLoginPage {	
	static final String LOGIN_URI = "/users/login?sl=1";

	public ChinaNewLoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getUrl(String serverName) {
		return "http://" + serverName + LOGIN_URI;
	}

}
