package com.extrabux.pages.cn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.StoreTransferPage;
import com.extrabux.pages.cn.account.ChinaAccountSettingPage;
import com.extrabux.pages.cn.account.ChinaMyEarningsPage;
import com.extrabux.pages.cn.mvp.MvpDaigouHomePageAfterLogin;
import com.extrabux.util.WebDriverUtil;

public class ChinaHomePageAfterLogin extends ChinaHomePageBeforeLogin {
	private static final Log LOG = LogFactory.getLog(ChinaHomePageAfterLogin.class);
	private static final String EMAIL_CLASS_NAME = "emailAddress";
	private static final String VIP_TAB_XPATH = "//div[@class='tabs']//a[contains(., 'vip')]";
	
	@FindBy(css="li.my-profits em")
	WebElement cash;
	
	@FindBy(css="div.side-middle strong")
	WebElement cashSide;
	
	@FindBy(className = EMAIL_CLASS_NAME)
	WebElement emailLoggedIn;

	@FindBy(css="li.my-profits a")
	WebElement myCashLink;
	
	@FindBy(css="div.my-account > a")
	WebElement accountSetting;
	
	@FindBy(xpath="//ul[@class='nav-lists wrapper-large']/li/a[contains(.,'返利商家')]")
	WebElement storeListNav;
	
	@FindBy(xpath="//ul[@class='nav-lists wrapper-large']/li/a[contains(.,'热门优惠')]")
	WebElement dealsNav;
	
	// search
	@FindBy(id = "search-input")
	WebElement searchInput;
	@FindBy(id = "search-form-image")
	WebElement searchSubmit;
	
	@FindBy(id="fancybox-content")
	WebElement fancybox;
	
	@FindBy(className="fancy-close")
	WebElement fancyClose;
	
	@FindBy(xpath="//ul[@class='home-headline-tab']/li[contains(.,'我收藏的商家')]")
	WebElement myFavourStoreTab;
	
	@FindBy(xpath="//li[contains(.,'收藏商家的优惠')]")
	WebElement myFavourStoreCouponTab;
	
	@FindBy(xpath=VIP_TAB_XPATH)
	WebElement vipStoreTab;
	
	@FindBy(css="div.cont.cur div.box span.img > a")
	WebElement storeImgUrl;
	
	@FindBy(css="#deals-favorite-store a.name.transferLink[href*='/store/3']")
	WebElement storeTransferLink;
	
	@FindBy(linkText="海淘商城")
	WebElement haitaoHomePageLink;
	
	@FindBy(css="#deals-favorite-store div.store div.infos >b >a")
	WebElement favourStoreName;
	
	@FindBy(xpath="//div[@class='header-layer-menu']//a[contains(.,'收藏的商家')]")
	WebElement myFavorite;
	
    @FindBy(css="div.tab-pane.active div.loader")
    WebElement loading;
	
	public boolean verifyLoggedInElementPresent(String email) throws Exception {

		String actual = getLoggedInEmail(By.className(EMAIL_CLASS_NAME),emailLoggedIn);
		LOG.info("actual email on page: " + actual + ", expected: " + email);
		if (!actual.equals(email)) {
			return false;
		}
		
		return true;
	}
	
	public String getLoggedInEmail(By elementLocator,WebElement email) throws Exception{
		String actual = "";
		try{
			WebDriverUtil.waitForElementPresent(driver, elementLocator, 10);
			actual = email.getText().replaceAll("(\\s.*)", "");
		}catch (NoSuchElementException e) {
			throw new Exception("member element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		catch (TimeoutException te) {
			throw new Exception("timed out waiting for member to display. element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		return actual;
	}
	
	public ChinaHomePageAfterLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getLifetimeEarnings() {
		//hoverOnEmail(emailLoggedIn);
		return cash.getText();
	}
	
	public String getLifetimeEarningsFromSide() {
		LOG.info("cash back amount: " +cashSide.getText());
		return cashSide.getText();
	}
		
	public void hoverOnEmail(WebElement element){
		Actions actions = new Actions(driver);
		actions.moveToElement(element).perform();
	}	

	public void hoverAndSelectFromAccountMenu(WebElement subLink) {
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(By.cssSelector("div.member > a")));
		actions.perform();

		actions.moveToElement(subLink);
		actions.click();
		actions.perform();
		
		WebDriverUtil.waitForPageToLoadComplete(driver, 10);
	}
	
	public ChinaFavPage enterFavPage(){
		hoverAndSelectFromAccountMenu(myFavorite);
		return new ChinaFavPage(driver);
	}
	
	public ChinaMyEarningsPage clickMyEarnings() {
		//hoverAndSelectFromAccountMenu(myCashLink);
		myCashLink.click();
		LOG.info("click my earing.........");
		return new ChinaMyEarningsPage(driver);
	}
	
	public ChinaAccountSettingPage hoverAndClickAccountSetting() {
		hoverAndSelectFromAccountMenu(accountSetting);
		LOG.info("click my account setting.........");
		return new ChinaAccountSettingPage(driver);
	}
	
	public ChinaMerchantPage searchStore(String storeName) {
		searchInput.sendKeys(storeName);
		LOG.info("type "+storeName+" into search input");
		
		searchSubmit.click();
		LOG.info("submit search...");
		return new ChinaMerchantPage(driver);
	}
	
	public ChinaHomePageBeforeLogin hoverAndClickLogout() {
		hoverAndSelectFromAccountMenu("退出");
		return new ChinaHomePageBeforeLogin(driver);
	}
	
	public void hoverAndSelectFromAccountMenu(String linkText) {
		Actions actions = new Actions(driver);
		actions.moveToElement(emailLoggedIn).perform();

		WebElement subLink = driver.findElement(By.linkText(linkText));
		actions.moveToElement(subLink);
		actions.click();
		actions.perform();
	}
	
	public void closeFancyBox(){
		WebDriverUtil.waitForElementPresent(driver, By.id("fancybox-content"), 10);
		fancyClose.click();
		LOG.info("close fancy box.......");
		WebDriverUtil.waitForElementNotVisible(driver, By.id("fancybox-content"), 10);
	}
	
	public boolean checkVipListPresent(){
		LOG.info("Verify vip store list is present or not");
		return WebDriverUtil.verifyElementExist(driver, By.xpath("//h2[text()='VIP精选商家']"));
	}
	
	public boolean checkGoodsListPresent(){
		LOG.info("Verify goods list is present or not");
		return WebDriverUtil.verifyElementExist(driver, By.cssSelector("div#sub-carousel div.img"));
	}
	
	public String getMyFirstFavouriteStoreName(){
		
		clickFavTab();

		String storeName = favourStoreName.getText();
		
		LOG.info("First favour store name is "+storeName);
		return storeName;
	}
	
	public boolean checkEmptyFavouriteStore(){
		clickFavTab();
		
		return WebDriverUtil.verifyElementExist(driver, By.className("no-favirote-store"));
	}
		
	public boolean verifyFavouriteStorePresent(String storeId){
		
		return WebDriverUtil.verifyElementExist(driver, By.cssSelector("#fs6 .store-cell img[data-original*='"+storeId+"']"));
	}
	
	public boolean verifyFavouriteStoreCouponPresent(String storeId){
		
		clickFavCouponTab();

		return WebDriverUtil.verifyElementExist(driver, By.cssSelector("#deals-favorite-store div.store a[href*='"+storeId+"']"));
	}
	
	public void clickFavTab(){
		
		LOG.info("click "+myFavourStoreTab.getText());

		myFavourStoreTab.findElement(By.tagName("a")).click();
		
		//wait for selected
		WebDriverUtil.waitForElementAttributeToChange(driver, myFavourStoreTab, 10, "class", "active");
		
		//WebDriverUtil.waitForElementNotVisible(driver, By.cssSelector("div.tab-pane.active div.loader"), 15);
		
	}
	
	public void clickFavCouponTab(){
		
		LOG.info("click "+myFavourStoreCouponTab.getText());

		myFavourStoreCouponTab.findElement(By.tagName("a")).click();
		
		//wait for selected
		WebDriverUtil.waitForElementAttributeToChange(driver, myFavourStoreCouponTab, 10, "class", "active");
		
		WebDriverUtil.waitForElementNotVisible(driver, By.cssSelector("div.tab-pane.active div.loader"), 15);
		
		scrollDownToBottom();
	}
	
	public void scrollDownToBottom(){
        WebDriverUtil.scrollPage(driver, "0", "2500");

        WebDriverUtil.waitForElementVisible(driver, loading, 15);

        WebDriverUtil.waitForElementNotVisible(driver, By.cssSelector("div.tab-pane.active div.loader"), 15);
	}
	
	public StoreTransferPage clickStartShopping() {
		clickTransferButton();
		return new StoreTransferPage(driver);
	}
	
	public void clickTransferButton(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("#deals-favorite-store a.coupon-code"), 15);
		LOG.debug("clicking start shopping page.");
		storeTransferLink.click();
	}
	
	public ChinaStoresListPage clickStoreNav(){
		LOG.debug("clicking store list nav.");
		storeListNav.click();
		return new ChinaStoresListPage(driver);
	}
	
	public ChinaDealsPage clickDealsNav(){
		LOG.debug("clicking deal nav.");
		dealsNav.click();
		return new ChinaDealsPage(driver);
	}
	
	public MvpDaigouHomePageAfterLogin enterHaitao(){
		haitaoHomePageLink.click();
		LOG.info("Enter extrabux homepage");
		WebDriverUtil.switchWindows(driver);
		return new MvpDaigouHomePageAfterLogin(driver);
	}
}
