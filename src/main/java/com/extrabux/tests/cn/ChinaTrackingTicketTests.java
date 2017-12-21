package com.extrabux.tests.cn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;


import com.extrabux.pages.PostPurchasePage;
import com.extrabux.pages.StoreTransferPage;
import com.extrabux.pages.cn.ChinaAddPurchaseWindow;
import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
import com.extrabux.pages.cn.ChinaMerchantPage;
import com.extrabux.pages.cn.account.ChinaMyEarningsPage;
import com.extrabux.pages.cn.account.ChinaTrackingHistoryPage;


public class ChinaTrackingTicketTests extends ChinaBaseTest {
	private static final Log LOG = LogFactory.getLog(ChinaTrackingTicketTests.class);
	private static String expectCashBack;
	private static String publisherId;
	
	@Test(dataProvider = "getWebDriver",groups="production-test")
	public void testStoreTransferClick(WebDriver driver) throws Exception {		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"), driver);

		ChinaMerchantPage storePage = homePage.searchStore(config.getString("store.searchString"));
		
		String currentUrl = driver.getCurrentUrl();
		LOG.debug("currentUrl : " + currentUrl);
		LOG.debug(config.getString("store.uri"));
		assertTrue(currentUrl.contains(config.getString("store.uri")), "expected to be on store detail page after signup and store search but are on " + driver.getTitle() + " (" + driver.getCurrentUrl() + ").  cannot click to transfer to store.  something may have gone wrong with store search.");
		StoreTransferPage transferPage = storePage.clickStartShopping();
		LOG.debug("page url after clicking start shopping" + driver.getCurrentUrl());
		
		publisherId = transferToMerchant(transferPage,driver,config.getString("store.url"));
		
		assertTrue(storePage.verifyAddPurchaseLinkPresent(),"green bubble not display");
		
		//get expect cash back
		NumberFormat format = DecimalFormat.getCurrencyInstance();
		format.setMaximumFractionDigits(2);
		expectCashBack = format.format(getPercent(storePage.getCashBack())*config.getDouble("purchase.subTotal"));
	}
	
	@Test(dataProvider = "getWebDriver",dependsOnMethods="testStoreTransferClick")
	public void testAddPurchase(WebDriver driver) throws Exception{
			ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"), driver);

			//close fancy box
			//homePage.closeFancyBox();
			
			ChinaMyEarningsPage earningsPage = homePage.clickMyEarnings();
			ChinaTrackingHistoryPage historyPage = earningsPage.clickHistory();
			
			assertTrue(historyPage.verifyPurchaseLinkPresent());
			
			ChinaAddPurchaseWindow addPurchaseWindow = historyPage.addPruchase();
				
			LOG.info("Start add purcharse....");
			
			addPurchaseWindow.addPurchase(config.getString("purchase.subTotal"),getRandomOrderId(),config.getString("purchase.orderConfirmation"));
						
			LOG.info("Purchase added");
			
			//verify cash back
			assertEquals(historyPage.getMessage(),config.getString("message.addpurchase"),"Earnings after add purchase not as expected");
	}
	
	@Test(dataProvider = "getWebDriver",dependsOnMethods="testStoreTransferClick",groups="production-test")
	public void transferFromCoupon(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"), driver);

		ChinaMerchantPage storePage = homePage.searchStore(config.getString("store.searchString"));
		
		assertTrue(driver.getCurrentUrl().contains(config.getString("store.uri")), "expected to be on store detail page after signup and store search but are on " + driver.getTitle() + " (" + driver.getCurrentUrl() + ").  cannot click to transfer to store.  something may have gone wrong with store search.");
		StoreTransferPage transferPage = storePage.clickCouponTransferButton();
		
		String publisherIdFromCoupon = transferToMerchant(transferPage,driver,config.getString("store.url"));
		
		assertEquals(publisherIdFromCoupon,publisherId,"publish id from store and coupon not as equal");
	}
	

}
