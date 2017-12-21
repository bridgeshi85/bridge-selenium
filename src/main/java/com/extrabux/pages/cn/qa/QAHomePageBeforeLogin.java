package com.extrabux.pages.cn.qa;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.pages.cn.ChinaLoginPage;
import com.extrabux.pages.cn.ChinaSignUpPage;

public class QAHomePageBeforeLogin extends BasePage{

	@FindBy(css = "a.login.btn.btn-normal.btn-primary")
	WebElement loginButton;
	
	@FindBy(css = "a.register.btn.btn-normal.btn-success")
	WebElement signUpButton;
	
	@FindBy(css = "input.form-control.search-query")
	WebElement searchInput;
	
	@FindBy(css = "i.icon.icon-search")
	WebElement searchIcon;
	
	public QAHomePageBeforeLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public ChinaLoginPage clickLoginButton(){
		loginButton.click();
		return new ChinaLoginPage(driver);
	}

	public ChinaSignUpPage clickSignUpButton(){
		signUpButton.click();
		return new ChinaSignUpPage(driver);
	}
	
	public SearchResultPage searchQuestion(String keyWord){
		searchInput.sendKeys(keyWord);
		searchIcon.click();
		return new SearchResultPage(driver);
	}
}
