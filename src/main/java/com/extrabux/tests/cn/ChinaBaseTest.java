package com.extrabux.tests.cn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.FindBy;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;

import com.extrabux.pages.HomePageAfterLogin;
import com.extrabux.pages.LoginPage;
import com.extrabux.pages.StoreTransferPage;
import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
import com.extrabux.pages.cn.ChinaHomePageBeforeLogin;
import com.extrabux.pages.cn.ChinaLoginPage;
import com.extrabux.pages.cn.ChinaSignUpPage;
import com.extrabux.pages.cn.account.ChinaAccountSettingPage;
import com.extrabux.pages.cn.qa.QAHomePageAfterLogin;
import com.extrabux.pages.cn.qa.QAHomePageBeforeLogin;
import com.extrabux.tests.BaseTest;
import com.extrabux.util.WebDriverUtil;

public class ChinaBaseTest extends BaseTest{
	
	private static String pattern = "\\d+(\\.\\d+)?%";
	private static final Log LOG = LogFactory.getLog(ChinaBaseTest.class);
	Boolean promotionBox;
	
	public ChinaHomePageAfterLogin login(String username, String passwd, WebDriver driver) throws Exception {
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		ChinaHomePageAfterLogin homePage = loginPage.login(username, passwd);
		assertTrue(homePage.verifyLoggedInElementPresent(username), "Logged in email not found on page");
		return homePage;
	}
	
	public ChinaHomePageBeforeLogin logout(WebDriver driver) throws Exception {
		ChinaHomePageBeforeLogin homePage = new ChinaHomePageBeforeLogin(driver);
		homePage.goToURL("https://" + serverName + "/users/logout");

		WebDriverUtil.waitForElementPresent(driver, By.className("logo"), 30);
		assertTrue(homePage.verifyNotLoggedIn(), "expected new user to be logged out");
		return homePage;
	}
	
	public String transferToMerchant(StoreTransferPage transferPage,WebDriver driver,String storeUrl){
				// need to switch to the transfer window and verify
				String parentWindow = switchWindows(driver);
				LOG.debug("page url after switching windows" + driver.getCurrentUrl());
				//assertTrue(transferPage.stopInterstitialLoadVerifyOnTransferPageThenTransfer(), "not on store transfer page as expected");
				// need to check they were actually forwarded to the merchant page
				String directLink = transferPage.getDirectLink();
				try {
					transferPage.waitForTransfer();
				} catch (TimeoutException te) {
					LOG.debug("in catch block after timing out waiting for store transfer");
					assertTrue(transferPage.verifyOnMerchantSite(storeUrl), "expected to be on merchant site but are on " + driver.getTitle() + " : " + driver.getCurrentUrl());
				}
				assertTrue(transferPage.verifyOnMerchantSite(storeUrl), "expected to be on merchant site but are on " + driver.getTitle() + " : " + driver.getCurrentUrl());
				String publisherId = getPublisherId(directLink);
				driver.close();
				LOG.info("publisher id is : "+publisherId);
				switchBackToParentWindow(driver, parentWindow);
				return publisherId;
	}
	
	public static String getPublisherId(String url){
	    Pattern p=Pattern.compile("(?<=\\?id=)\\w+");
	    Matcher m=p.matcher(url);
	    String findString = "";
	    if(m.find()){
	    	findString = m.group(0);
	    }
		return findString;
	}
	
	public void resetLocalCache(String username, String passwd) throws Exception {
		//System.setProperty("webdriver.chrome.driver",
		//"C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");

		WebDriver driver = new FirefoxDriver();

		//closePromotionBox(driver);

		LoginPage loginPage = new LoginPage(driver);
		driver.get("https://" + getExServerName(serverName) + "/admin/cache/reset-local-cache");
		//loginPage.goToURL(loginPage.getUrl(getExServerName(serverName)));
		
		loginPage.login(username, passwd);
		
		WebDriverUtil.waitForTextPresentInElement(driver, driver.findElement(By.cssSelector("body > pre")), "Success", 10);
		
		LOG.debug("reset cache");

		driver.quit();
	}
	
	public QAHomePageAfterLogin LoginFromQA(String email,String passWord,WebDriver driver) throws Exception{
		
		//open wenda
		QAHomePageBeforeLogin homePage = new QAHomePageBeforeLogin(driver);
		homePage.goToURL("http://"+serverName);
		
		ChinaLoginPage loginPage = homePage.clickLoginButton();
				
		//login
		QAHomePageAfterLogin homePageAfterLogin = loginPage.loginFromQA(email, passWord);
		
		//verfiy
		homePageAfterLogin.verifyUserName(email.substring(0, email.indexOf("@")));
		
		return homePageAfterLogin;
	}
	
	public ChinaHomePageAfterLogin signUp(String username, String passwd,WebDriver driver) throws Exception {
			ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
			signUpPage.goToURL(signUpPage.getUrl(serverName));

			ChinaHomePageAfterLogin homePage = signUpPage.signUp(username, passwd,
					passwd,config.getString("cn.signup.captcha"));
			assertTrue(homePage.verifyLoggedInElementPresent(username), "Logged in email not as expected.");

			// verify the sign up bonus
			String expected = DecimalFormat.getCurrencyInstance(Locale.US).format(config.getDouble("signup.bonusAmount"));
			assertEquals(homePage.getLifetimeEarnings(), expected, "Earnings after signup not as expected");
			return homePage;
	}
	
	 public static Double getPercent(String str){
			
		    Pattern p=Pattern.compile(pattern);
		    Matcher m=p.matcher(str);
		    String findString = "";
		    if(m.find()){
		    	findString = m.group(0);
		    }
		    BigDecimal percentage = new BigDecimal(findString.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
			return percentage.doubleValue();
	  }
	 

	
	public static String getRandomPhoneNumber(){
		Random  random = new  Random();
		String number="";
		while(number.length()<11){
			number="13";
			number = number + random.nextInt(1000000000);
		}
		return number;
	}
	
	public ChinaAccountSettingPage loginAndVerifyPhone(String username,String password,String firstServenNumber,WebDriver driver) throws Exception
	{
		ChinaHomePageAfterLogin homePage = login(username, password, driver);
		
		ChinaAccountSettingPage settingPage = homePage.hoverAndClickAccountSetting();
		
		settingPage.verifyPhone(firstServenNumber,config.getString("account.sms"));
		
		WebDriverUtil.waitForPageToLoadComplete(driver, 30);
		
		return settingPage;
	}
	
	@BeforeMethod(enabled = true,groups="production-test")
	public void handlePromotionBox(ITestContext testNGContext,Object[] driver) {
		
		promotionBox = false;
		if (testNGContext.getCurrentXmlTest().getAllParameters().containsKey("promotionBox")
				|| System.getProperties().containsKey("promotionBox")) {
			
			promotionBox = Boolean.valueOf(testNGContext.getCurrentXmlTest().getParameter("promotionBox"));
		}
		
		//LOG.info(promotionBox);

		if(promotionBox){		
			closePromotionBox((WebDriver)driver[0]);
		}
	}
	
	public void closePromotionBox(WebDriver driver){
		ChinaHomePageBeforeLogin homePage = new ChinaHomePageBeforeLogin(driver);
		
		homePage.goToURL("http://"+serverName);
		homePage.closeFancyBox();
		LOG.info("close promo box in home page");
	}

}
