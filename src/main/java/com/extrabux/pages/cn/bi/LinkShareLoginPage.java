package com.extrabux.pages.cn.bi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;

public class LinkShareLoginPage extends BasePage {	

	private static final Log LOG = LogFactory.getLog(LinkShareLoginPage.class);
	
	@FindBy(id = "username")
	WebElement userName;
	
	@FindBy(id = "password")
	WebElement passWord;
	
	@FindBy(name = "login")
	WebElement loginButton;
	
	public LinkShareLoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getLinkShareLoginUrl(){
		return "https://login.linkshare.com/sso/login?service="
				+ "http%3A%2F%2Fcli.linksynergy.com%2Fcli%2Fcommon%2Flogin.php";
	}
	
	public LinkShareHomePage login(String username,String pw){
		userName.sendKeys(username);
		passWord.sendKeys(pw);
		
		loginButton.click();
		
		LOG.info("Logged in linkshare successfull。。。。。。");
		return new LinkShareHomePage(driver);
	}
}
