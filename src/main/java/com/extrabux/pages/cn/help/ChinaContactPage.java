package com.extrabux.pages.cn.help;

import org.apache.commons.logging.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.apache.commons.logging.LogFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class ChinaContactPage extends BasePage{
	private static final Log LOG = LogFactory.getLog(ChinaContactPage.class);
	private static final String URI = "/help/contact";
	
	@FindBy(css = "a[href='#emailServer']")
	WebElement emailServerTab;
		
	WebElement name;
	WebElement email;
	WebElement message;
	WebElement subject;
	
	WebElement submit;
	
	public ChinaContactPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public String getUrl(String serverName) {
		return "https://" + serverName + URI;
	}
	
	public ChinaContactSuccessPage sendEmail(String name,String email,String message){
		emailServerTab.click();
		fillContactForm(name,email,message);
		submit.click();
		return new ChinaContactSuccessPage(driver);
		
	}
	
	public void fillContactForm(String name,String email,String message){
		WebDriverUtil.waitForElementVisible(driver, this.name, 10);
		LOG.info("type name:"+name+" email:"+email+" message:"+message+" into contact form");
		this.name.sendKeys(name);
		this.email.clear();
		this.email.sendKeys(email);
		this.message.sendKeys(message);
		
		Select select = new Select(subject);
		select.selectByIndex(1);
	}
}
