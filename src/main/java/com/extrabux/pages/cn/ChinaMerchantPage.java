package com.extrabux.pages.cn;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.apache.commons.logging.LogFactory;

import com.extrabux.pages.MerchantPage;
import com.extrabux.pages.StoreTransferPage;
import com.extrabux.util.WebDriverUtil;

public class ChinaMerchantPage extends MerchantPage{
	private static final Log LOG = LogFactory.getLog(ChinaMerchantPage.class);
	
	@FindBy(linkText="到期时间")
	WebElement endDateLink;
	
	@FindBy(className = "transfer-v2")
	WebElement popUpWindow;
	
	@FindBy(className = "tab-login")
	WebElement panelToggle;
	
	@FindBy(id="email")
	List<WebElement> email;
	
	@FindBy(name="password")
	List<WebElement> password;
	
	@FindBy(css="#login-form .submit")
	WebElement loginButton;
	
	@FindBy(css="#register-form input.submit")
	WebElement signUpButton;
	
	@FindBy(css="span.code-button")
	WebElement couponTransferButton;

	@FindBy(css="div.top>p>del")
	WebElement oldCashBack;

	@FindBy(css="a.favoriteToggle.favorites")
	WebElement addToFav;
	
	@FindBy(css="h1.headline")
	WebElement storeName;
	
	@FindBy(xpath="//div[@id='sortByContent']//span[contains(@class,'expiration')][contains(.,'距离')][contains(.,'/')]")
	List<WebElement> expirationDates;
	
	@FindBy(css="div.login-global > div.on[class*=\"box\"] #captcha")
	WebElement captcha;
	
	public ChinaMerchantPage(final WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void clickStartShoppingWithoutLogin() {
		clickTransferButton();
		WebDriverUtil.waitForElementVisible(driver, popUpWindow, 5);
	}
	
	public StoreTransferPage loginFromPopup(String username,String passWord,String captcha){
		WebDriverUtil.waitForElementPresent(driver, By.className("tab-login"), 15);
		panelToggle.click();
		typeEmailAndPassWord(username,passWord);
		typeCaptcha(captcha);
		LOG.info("Submit login。。。。");
		loginButton.click();
		return new StoreTransferPage(driver);
	}
	
	public void sortByEndDate(){
		WebDriverUtil.waitForElementPresent(driver, By.id("tab-deals-gift"), 10);
		LOG.info("filter by endate");
		endDateLink.click();
	}
	
	public StoreTransferPage signUpFromPopup(String username,String passWord,String captcha){
		typeEmailAndPassWord(username,passWord);
		typeCaptcha(captcha);
		LOG.info("Submit signup。。。。");
		signUpButton.click();
		return new StoreTransferPage(driver);
	}
	
	public void typeEmailAndPassWord(String username,String passWord){
		WebDriverUtil.waitForElementPresent(driver, By.id("email"), 5);
		typeInDisplayInput(email,username,"email");
		typeInDisplayInput(password,passWord,"password");
	}
	
	public StoreTransferPage clickCouponTransferButton(){
		LOG.debug("clicking start shopping page.");
		couponTransferButton.click();
		return new StoreTransferPage(driver);
	}
	
	public void typeInDisplayInput(List<WebElement> elements,String inputString,String inputName)
	{
	    for (WebElement element : elements)
	    {
	        if (element.isDisplayed()) // correct method: isDisplayed()
	        {
	    		LOG.info("Typing "+inputString+" into "+inputName);
	    		element.sendKeys(inputString);
	        }
	    }
	}
	
	public String getUrl(String serverName,String merchantUri) {
		return "https://" + serverName + "/stores/"+merchantUri;
	}
	
	public void addOrRemoveFavourite(){
		addToFav.click();
		LOG.info("add or remove store.....");
	}
	
	public String getOldCashBack(){
		return oldCashBack.getText();
	}
	
	public String getStoreName(){
		String storeName = "";
		if(WebDriverUtil.verifyElementExist(driver, By.cssSelector("h1.headline"))){
			storeName = this.storeName.getText();
		}
		LOG.info("store name is:"+storeName);
		return storeName;
	}
	
	public boolean verifySortByEndDate() throws ParseException{
		 WebDriverUtil.waitForElementPresent(driver, By.cssSelector("li.active > #tab-deals-gift"), 10);
		 Date currentRowDate = new Date();
		 Date prevRowDate = null;
		 for(WebElement row : expirationDates){
			 	//LOG.info(row.getText());
				currentRowDate = getExpiryDate(row.getText());
				//LOG.debug("current: " + currentRowDate);
				if(prevRowDate == null){
					prevRowDate = currentRowDate;
				}else
				{
					if(prevRowDate.compareTo(currentRowDate) <= 0){
						prevRowDate = currentRowDate;
					}else{
						LOG.info("prev row: "+prevRowDate+" current row: "+currentRowDate);
						return false;
					}
				}
			}
		 return true; 
	 }
	
	public void typeCaptcha(String captcha) {
		WebDriverUtil.waitForElementPresentAndVisible(driver, By.cssSelector("div.login-global > div.on[class*=\"box\"] #captcha"), 15);
		this.captcha.sendKeys(captcha);
	}
}
