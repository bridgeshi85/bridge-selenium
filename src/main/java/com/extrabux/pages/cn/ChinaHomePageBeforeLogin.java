package com.extrabux.pages.cn;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.HomePageAfterLogin;
import com.extrabux.pages.cn.mvp.MvpDaigouHomePageAfterLogin;
import com.extrabux.util.WebDriverUtil;

public class ChinaHomePageBeforeLogin extends HomePageAfterLogin {

	private static final Log LOG = LogFactory.getLog(ChinaHomePageBeforeLogin.class);
	private static final String COUPON_IMG_CSS = "div[id^='deals'].tab-pane.active div.store div.logo";
	//private static final String STORE_IMG_CSS = "div.item.active > a.store-cell img";
	
	@FindBy(css="div.tab-pane.active form input[id^=email]")
	WebElement email;
	
	@FindBy(css="div.tab-pane.active form input[id^=password]")
	WebElement password;
	
	@FindBy(css="form#box input[name=referee_email]")
	WebElement signupCode;
	
	@FindBy(css="div.item.active a.login-layer")
    	WebElement signupLink;
	
	// login link
    	@FindBy(css="a.but-login")
    	WebElement login;
	// register button
	WebElement register;

	@FindBy(css = "li.dropdown-item.selected")
	WebElement defaultType;
	@FindBy(css = "div.dropdown.search-type-dropdown li:nth-child(1)")
	WebElement searchType1;
	@FindBy(css = "div.dropdown.search-type-dropdown li:nth-child(2)")
	WebElement searchType2;
	@FindBy(css = "div.dropdown.search-type-dropdown li:nth-child(3)")
	WebElement searchType3;

	@FindBy(css="ul.dropdown-menu")
	WebElement dropDownMenu;
	
	@FindBy(css="div.types > ul")
	WebElement searchTypes;
	
	@FindBy(css="span.button")
	WebElement fancyClose;
	
	@FindBy(id="fancybox-overlay")
	WebElement fancyOverlay;
	
	@FindBy(linkText="海淘商城")
	WebElement haitaoHomePageLink;
	
	@FindBy(css="#new-deals-page div.headline-tab >ul >li:not(:last-child)")
	List<WebElement> couponTabList;
	
	@FindBy(css=".home-tab-content > div:not(:last-child)")
	List<WebElement> tabList;
	
	@FindBy(css="div.tab-pane.active input[type=submit]")
	WebElement submitBtn;
	
	@FindBy(css="ul.signup-tabs li a[href='#register-tab-pane']")
	WebElement signUpTab;
	
	@FindBy(css="ol.carousel-indicators li")
	WebElement firstCarouselIndicator;
	
	public ChinaHomePageBeforeLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public ChinaLoginPage clickLogin() {
		WebDriverUtil.waitForElementPresent(driver, By.linkText("登录"), 5);
		LOG.info("Click login link....");
		login.click();
		return new ChinaLoginPage(driver);
	}

	public void clickSignUpLink(){
        firstCarouselIndicator.click();
        WebDriverUtil.waitForElementPresentAndVisible(driver, By.cssSelector("div.carousel-inner > div.item:nth-child(1).active a.login-layer"), 15);
        signupLink.click();
	}
	
	public ChinaHomePageAfterLogin login(String userName,String passWord) {
		//WebDriverUtil.waitForElementPresent(driver, By.linkText("登录"), 5);
		LOG.info("Click login link....");
		clickSignUpLink();
		login.click();
		fillForm(userName,passWord,"");
		return submit();
	}
	
	public ChinaHomePageAfterLogin signUp(String userName,String passWord) {
		LOG.info("Click sign up link....");
		clickSignUpLink();
		fillForm(userName,passWord,"");
		return submit();
	}
	
	public ChinaHomePageAfterLogin submit() {
		submitBtn.sendKeys(Keys.ENTER);
		LOG.info("submit sign in/up.....");
		return new ChinaHomePageAfterLogin(driver);
	}
	
	public void checkStoreListPresent(){
		LOG.info("Verify store list is present or not");
		for(WebElement tab : tabList){

			assertTrue(WebDriverUtil.verifyElementExistBasedOnElement(driver, tab, By.className("store-cell")));
			
			//assertTrue(WebDriverUtil.verifyElementExist(driver, By.cssSelector(STORE_IMG_CSS)),tab.getText() + " store list didn't get");
		}
	}

	public ChinaSearchResultPage search(String keyword, String searchType) {

		WebDriverUtil.waitForElementPresent(driver,By.cssSelector("div.types"), 5);
		
		WebElement expectSearchType = searchTypes.findElement(By.xpath("./li[contains(.,'"+searchType+"')]"));

		if(!expectSearchType.getAttribute("class").contains("cur")){
			LOG.info("switch search menu to "+searchType);
			//WebDriverUtil.hoverOnElement(defaultType, driver);
			expectSearchType.click();
			WebDriverUtil.waitForElementAttributeToChange(driver, expectSearchType, 10, "class", "cur");
		}
		
		// searchType should be Store, Coupon, Blog
		/*if (searchType == "Store") {
			WebDriverUtil.waitForElementPresent(driver,
					By.cssSelector("div.dropdown.search-type-dropdown li:nth-child(1)"), 5);
			//searchType1.click();
		} else if (searchType == "Coupon") {
			WebDriverUtil.waitForElementPresent(driver,
					By.cssSelector("div.dropdown.search-type-dropdown li:nth-child(2)"), 5);
			WebDriverUtil.hoverOnElement(defaultType, driver);
			dropDownMenu.findElement(By.xpath("./li[contains(.,'优惠')]")).click();

		} else {
			WebDriverUtil.waitForElementPresent(driver,
					By.cssSelector("div.dropdown.search-type-dropdown li:nth-child(3)"), 5);
			WebDriverUtil.hoverOnElement(defaultType, driver);

			dropDownMenu.findElement(By.xpath("./li[contains(.,'博客')]")).click();
		}*/

		WebDriverUtil.waitForElementPresent(driver, By.id("search-input"), 5);
		LOG.info("Typing " + keyword + " into search input...");
		searchInput.sendKeys(keyword);
		searchSubmit.click();
		return new ChinaSearchResultPage(driver);
	}

	public ChinaMerchantPage search(String keyword) {

		// searchType is Store

		//WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.dropdown.search-type-dropdown li:nth-child(1)"), 5);
		//searchType1.click();

		WebDriverUtil.waitForElementPresent(driver, By.id("search-input"), 5);
		LOG.info("Typing " + keyword + " into search input...");
		searchInput.sendKeys(keyword);
		searchSubmit.click();
		return new ChinaMerchantPage(driver);
	}

	public void fillForm(String username, String pw, String signUpCode) {
		LOG.info("attempting to sign up/in with username: " + username + ", password: " + pw + " and signUpCode: "
				+ signUpCode);
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector(".tab-content"), 15);
		typeUsername(username);
		typePassword(pw);
		if(signUpCode != "")
			typeSignUpCode(signUpCode);
	}

	public void fillSignInForm(String username, String pw) {
		LOG.info("attempting to sign up with username: " + username + ", password: " + pw);
		typeUsername(username);
		typePassword(pw);
	}
	
	private void typeSignUpCode(String signUpCode) {
		signupCode.clear();
		signupCode.sendKeys(signUpCode);
	}
	
	public void typeUsername(String username) {
		email.clear();
		email.sendKeys(username);
	}

	public void typePassword(String pswd) {
		password.clear();
		password.sendKeys(pswd);
	}
	
	public void closeFancyBox(){
		WebDriverUtil.waitForElementPresent(driver, By.id("fancybox-content"), 30);
		fancyClose.click();
		//Actions builder =new Actions(driver);   
		//builder.moveToElement(fancyOverlay,100,100).click().build().perform();
		
		LOG.info("close fancy box.......");
		WebDriverUtil.waitForElementNotVisible(driver, By.id("fancybox-content"), 10);
	}

	
	public MvpDaigouHomePageAfterLogin enterHaitao(){
		WebDriverUtil.waitForElementPresent(driver, By.linkText("海淘商城"), 10);
		haitaoHomePageLink.click();
		LOG.info("Enter extrabux homepage");
		WebDriverUtil.switchWindows(driver);
		return new MvpDaigouHomePageAfterLogin(driver);
	}
	
	public void checkCouponListPresent(){
		LOG.info("Verify coupon list is present or not");
		for(WebElement tab : couponTabList){

			LOG.info("click "+tab.getText());
			
			tab.findElement(By.tagName("a")).click();
			
			//wait for element to be selected
			WebDriverUtil.waitForElementAttributeToChange(driver, tab, 10, "class", "active");
			
			WebDriverUtil.waitForElementNotVisible(driver, By.cssSelector("div.tab-pane.active div.loader"), 15);
						
			assertTrue(WebDriverUtil.verifyElementExist(driver, By.cssSelector(COUPON_IMG_CSS)),tab.getText() + " store list didn't get");
		}
	}

}
