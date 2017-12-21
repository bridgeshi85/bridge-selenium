package com.extrabux.tests.cn;

import static org.testng.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.StoreTransferPage;
import com.extrabux.pages.cn.ChinaDealsPage;
import com.extrabux.pages.cn.ChinaHomePageAfterLogin;

public class ChinaDealsTests extends ChinaBaseTest {

	private static final Log LOG = LogFactory.getLog(ChinaDealsTests.class);

	@Test(dataProvider = "getWebDriver")
	public void sortByEndDate(WebDriver driver) throws Exception {	
		
		ChinaDealsPage dealsPage = new ChinaDealsPage(driver);
		dealsPage.goToURL(dealsPage.getUrl(serverName));
		
		dealsPage.sortByEndDate();
		
		assertTrue(dealsPage.verfiySortResultByCountDownTime(),"sort result not as expected");
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void filterByCouponType(WebDriver driver) throws Exception {
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		ChinaDealsPage dealsPage = homePage.clickDealsNav();
		
		dealsPage.filterCoupon("Free Shipping");
		
		assertTrue(dealsPage.verifyCouponPresentByName("Free shipping"),"filter by type failed");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterByStore(WebDriver driver) throws Exception {
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		ChinaDealsPage dealsPage = homePage.clickDealsNav();
		
		dealsPage.filterCoupon(config.getString("deals.storeName.linkText"));
		
		assertTrue(dealsPage.verifyCouponPresentByStore(config.getString("deals.storeName")),"filter by type failed");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterByFavStore(WebDriver driver) throws Exception {
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);
		
		ChinaDealsPage dealsPage = homePage.clickDealsNav();
		
		dealsPage.filterByFav();
		
		assertTrue(dealsPage.verifyCouponPresentByStore(config.getString("deals.storeName")),"filter by type failed");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterAndSort(WebDriver driver) throws Exception {	
				
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		ChinaDealsPage dealsPage = homePage.clickDealsNav();
		
		dealsPage.filterCoupon(config.getString("deals.storeName.linkText"));
		dealsPage.sortByEndDate();
		
		assertTrue(dealsPage.verifyCouponPresentByStore(config.getString("deals.storeName")),"filter by type failed");
		assertTrue(dealsPage.verifySortByEndDate(),"sort result not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void transferFromDealsPage(WebDriver driver) throws Exception {	
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"), driver);

		ChinaDealsPage dealsPage = homePage.clickDealsNav();
		
		dealsPage.showMoreStores();
		
		dealsPage.filterCoupon(config.getString("deals.storeName.linkText"));
		
		StoreTransferPage transferPage = dealsPage.clickStartShopping();
		
		LOG.debug("page url after clicking start shopping" + driver.getCurrentUrl());

		transferToMerchant(transferPage,driver,config.getString("deals.store.url"));

		assertTrue(dealsPage.verifyAddPurchaseLinkPresent());
	}
	
}
