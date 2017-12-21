package com.extrabux.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PostPurchasePage extends BasePage{

	@FindBy(css="div.col.right")
	List<WebElement> purchaseInfos;
	
	public PostPurchasePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);	
	}

	public String getCashBack(){
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.col.right")));
		
		return purchaseInfos.get(4).getText();
	}
}
