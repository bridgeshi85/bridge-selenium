package com.extrabux.tests.cn;

import static org.testng.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.StoreTransferPage;
import com.extrabux.pages.cn.ChinaFavPage;
import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
import com.extrabux.pages.cn.ChinaMerchantPage;
import com.extrabux.pages.cn.ChinaSignUpPage;
import com.extrabux.pages.cn.ChinaStoresListPage;

public class ChinaFavTests extends ChinaBaseTest {

	private static final Log LOG = LogFactory.getLog(ChinaDealsTests.class);
	private static String memberEmail;
	
	@Test(dataProvider = "getWebDriver")
	public void addStoreFav(WebDriver driver) throws Exception {	
		memberEmail = getRandomEmail();		
		ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
		
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		ChinaHomePageAfterLogin homePage = signUpPage.signUp(memberEmail, config.getString("defaultPassword"),
				config.getString("defaultPassword"),config.getString("cn.signup.captcha"));
		
		ChinaMerchantPage storePage = homePage.searchStore(config.getString("store.searchString"));
		storePage.addOrRemoveFavourite();
		
		ChinaFavPage favPage = homePage.enterFavPage();
		assertTrue(favPage.checkStorePresence(config.getString("store.searchString")),"add to fav list failed");
	}
	
	@Test(dataProvider = "getWebDriver",dependsOnMethods="addStoreFav")	
	public void removeStoreFromFavouriteList(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = login(memberEmail, config.getString("defaultPassword"), driver);
		
		ChinaMerchantPage storePage = homePage.searchStore(config.getString("store.searchString"));

		storePage.addOrRemoveFavourite();
		
		ChinaFavPage favPage = homePage.enterFavPage();
		
		assertTrue(!favPage.checkStorePresence(config.getString("store.searchString")),"remove from fav list failed");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void sortByPurchase(WebDriver driver) throws Exception {
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"), driver);
		
		ChinaFavPage favPage = homePage.enterFavPage();

		favPage.sort("下单成功");
		assertTrue(favPage.verfiySuccessPurchaseSortResult(),"sort by success purchase result not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void sortByCashBack(WebDriver driver) throws Exception {
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"), driver);
		
		ChinaFavPage favPage = homePage.enterFavPage();

		favPage.sort("返利");
		assertTrue(favPage.verifyCashBackSortResult(),"sort by success purchase result not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void transferFromFavourite(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"), driver);
		
		ChinaFavPage favPage = homePage.enterFavPage();
		
		StoreTransferPage transferPage = favPage.clickStartShopping();
		
		LOG.debug("page url after clicking start shopping" + driver.getCurrentUrl());

		transferToMerchant(transferPage,driver,config.getString("store.url"));

		assertTrue(favPage.verifyAddPurchaseLinkPresent());
	}
	
	
	@Test(dataProvider = "getWebDriver")
	public void addStoreToFavFromStoresList(WebDriver driver) throws Exception{
		String memberEmail = getRandomEmail();		
		ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
		
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		ChinaHomePageAfterLogin homePage = signUpPage.signUp(memberEmail, config.getString("defaultPassword"),
				config.getString("defaultPassword"),config.getString("cn.signup.captcha"));
		
		ChinaStoresListPage storeListPage = homePage.clickStoreNav();
		
		storeListPage.addStoreToFav(config.getString("storeList.fav.storeName"));
		
		ChinaFavPage favPage = homePage.enterFavPage();
		assertTrue(favPage.checkStorePresence(config.getString("storeList.fav.storeName")),"add to fav list failed");
	}
}
