package com.extrabux.tests.cn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.DecimalFormat;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.StoreTransferPage;
import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
import com.extrabux.pages.cn.ChinaHomePageBeforeLogin;

public class ChinaHomePageTests extends ChinaBaseTest{
	
	private static final Log LOG = LogFactory.getLog(ChinaHomePageTests.class);	
	
	@Test(dataProvider = "getWebDriver",groups = "production-test")
	public void storeListPresent(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);
		
		//ChinaHomePageBeforeLogin homePage = new ChinaHomePageBeforeLogin(driver);
		//homePage.goToURL("https://" + serverName);
		
		homePage.checkStoreListPresent();
	}
	
	@Test(dataProvider = "getWebDriver")
	public void couponListPresent(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);
		
		homePage.checkCouponListPresent();
	}
	
	@Test(dataProvider = "getWebDriver")
	public void favouriteCouponListPresent(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);
		
		homePage.checkCouponListPresent();
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void goodsListPresentAfterLogin(WebDriver driver) throws Exception{

		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		assertTrue(homePage.checkGoodsListPresent(),"hot product list didn't find");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void favourCouponPresent(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		assertTrue(homePage.verifyFavouriteStoreCouponPresent(config.getString("cn.favourStore.id")),"favour store coupon didn't match");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void emptyFavourStoreList(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = signUp(getRandomEmail(), config.getString("defaultPassword"), driver);
				
		assertTrue(homePage.checkEmptyFavouriteStore(),"favour store list is not empty");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void favouriteStorePresent(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);
		
		assertTrue(homePage.verifyFavouriteStorePresent(config.getString("cn.favourStore.id")),"favour store not as expected");
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void vipStoreList(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.adminUserEmail"), 
				config.getString("cn.adminUserPassword"),driver);
		
		assertTrue(homePage.checkVipListPresent(),"vip store list didn't find");
	}
	
	//@Test(dataProvider = "getWebDriver",groups = "production-test")
	public void verifyMvpDaigouProduct(WebDriver driver) throws Exception{
		
		ChinaHomePageBeforeLogin homePageBeforeLogin = new ChinaHomePageBeforeLogin(driver);
		homePageBeforeLogin.goToURL("https://" + serverName);
		
		assertTrue(homePageBeforeLogin.verifyMvpProductPresent(),"mvp product list didn't find on homepage before login");
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), 
				config.getString("cn.existingUserPassword"),driver);
		
		assertTrue(homePage.verifyMvpProductPresent(),"mvp product list didn't find on homepage after login");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void testStoreTransferFromeHomePage(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		homePage.clickFavCouponTab();
		
		StoreTransferPage transferPage = homePage.clickStartShopping();
		
		LOG.debug("page url after clicking start shopping" + driver.getCurrentUrl());

		transferToMerchant(transferPage,driver,config.getString("store.url"));

		assertTrue(homePage.verifyAddPurchaseLinkPresent());
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void signInFromHomePage(WebDriver driver) throws Exception{
		
		/*Dimension POPULAR_DISPLAY_SIZE = new Dimension(1024, 768);
		driver.manage().window().setSize(POPULAR_DISPLAY_SIZE);
		
		LOG.debug("Set window size to 1024*768");*/
		
		ChinaHomePageBeforeLogin homePage = new ChinaHomePageBeforeLogin(driver);
		homePage.goToURL("https://" + serverName);
		
		ChinaHomePageAfterLogin homePageAfter = homePage.login(config.getString("cn.existingUserEmail"),config.getString("cn.existingUserPassword"));

		assertTrue(homePageAfter.verifyLoggedInElementPresent(config.getString("cn.existingUserEmail")), "Logged in email on page not as expected");
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void signUpFromHomePage(WebDriver driver) throws Exception{
		
		ChinaHomePageBeforeLogin homePage = new ChinaHomePageBeforeLogin(driver);
		homePage.goToURL("https://" + serverName);
		
		String email = getRandomEmail();
		ChinaHomePageAfterLogin homePageAfter = homePage.signUp(email,config.getString("cn.existingUserPassword"));

		assertTrue(homePageAfter.verifyLoggedInElementPresent(email), "Logged in email on page not as expected");
		
		//verify cashback amount
		String expected = DecimalFormat.getCurrencyInstance(Locale.US).format(config.getDouble("signup.bonusAmount"));
		assertEquals(homePageAfter.getLifetimeEarnings(), expected, "Earnings after signup not as expected");
	}
}

