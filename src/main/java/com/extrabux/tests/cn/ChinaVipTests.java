package com.extrabux.tests.cn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

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

public class ChinaVipTests extends ChinaBaseTest {
	private static final Log LOG = LogFactory.getLog(ChinaVipTests.class);
	
	private static String oldCashBack;
	
	@Test(dataProvider = "getWebDriver")
	public void testAddPurchaseWithVipUser(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.adminUserEmail"), config.getString("cn.adminUserPassword"), driver);

		ChinaMerchantPage storePage = homePage.searchStore(config.getString("vip.store.searchString"));
		
		String currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains(config.getString("vip.store.uri")), "expected to be on store detail page after signup and store search but are on " + driver.getTitle() + " (" + driver.getCurrentUrl() + ").  cannot click to transfer to store.  something may have gone wrong with store search.");
		StoreTransferPage transferPage = storePage.clickStartShopping();
		LOG.debug("page url after clicking start shopping" + driver.getCurrentUrl());
		
		transferToMerchant(transferPage,driver,config.getString("vip.store.url"));
		
		assertTrue(storePage.verifyAddPurchaseLinkPresent(),"green bubble not display");
		
		//get expect cash back
		NumberFormat format = DecimalFormat.getCurrencyInstance(Locale.US);
		format.setMaximumFractionDigits(2);
		String expectCashBack = format.format(getPercent(storePage.getCashBack())*config.getDouble("purchase.subTotal"));
		oldCashBack = format.format(getPercent(storePage.getOldCashBack())*config.getDouble("purchase.subTotal"));
		String expectVipBonus = getVipBonus(getPercent(storePage.getCashBack()),getPercent(storePage.getOldCashBack()),config.getDouble("purchase.subTotal"));
							
		ChinaMyEarningsPage earningsPage = homePage.clickMyEarnings();
		ChinaTrackingHistoryPage historyPage = earningsPage.clickHistory();
		
		assertTrue(historyPage.verifyPurchaseLinkPresent());
		
		ChinaAddPurchaseWindow addPurchaseWindow = historyPage.addPruchase();
			
		LOG.info("Start add purcharse....");
		
		addPurchaseWindow.addPurchase(config.getString("purchase.subTotal"),getRandomOrderId(),config.getString("purchase.orderConfirmation"));
		
		LOG.info("Purchase added");
		
		assertEquals(historyPage.getMessage(),config.getString("message.addpurchase"),"Earnings after add purchase not as expected");

		//verify cash back
		//assertEquals(postPurchasePage.getCashBack(),expectCashBack,"Earnings after add purchase not as expected");
		
		//earningsPage = homePage.clickMyEarnings();
				
		//assertTrue(earningsPage.getVipBonus().contains(expectVipBonus),"vip bonus not as epected");
		
		//assertEquals(earningsPage.getVipBonus(),expectVipBonus,"vip bonus not as epected");
	}
	
	@Test(dataProvider = "getWebDriver",dependsOnMethods="testAddPurchaseWithVipUser")
	public void addVipStorePurchaseWithNormalUser(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"), driver);

		ChinaMerchantPage storePage = homePage.searchStore(config.getString("vip.store.searchString"));
		
		String currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains(config.getString("vip.store.uri")), "expected to be on store detail page after signup and store search but are on " + driver.getTitle() + " (" + driver.getCurrentUrl() + ").  cannot click to transfer to store.  something may have gone wrong with store search.");
		StoreTransferPage transferPage = storePage.clickStartShopping();
		LOG.debug("page url after clicking start shopping" + driver.getCurrentUrl());
		
		transferToMerchant(transferPage,driver,config.getString("vip.store.url"));
		
		assertTrue(storePage.verifyAddPurchaseLinkPresent(),"green bubble not display");

		NumberFormat format = DecimalFormat.getCurrencyInstance(Locale.US);
		format.setMaximumFractionDigits(2);
		String expectCashBack = format.format(getPercent(storePage.getCashBack())*config.getDouble("purchase.subTotal"));
		//LOG.info(oldCashBack);
		assertEquals(oldCashBack,expectCashBack,"vip user and normal user didn't see the same normal cash back");
		
		ChinaMyEarningsPage earningsPage = homePage.clickMyEarnings();
		ChinaTrackingHistoryPage historyPage = earningsPage.clickHistory();
		
		assertTrue(historyPage.verifyPurchaseLinkPresent());
		
		ChinaAddPurchaseWindow addPurchaseWindow = historyPage.addPruchase();
			
		LOG.info("Start add purcharse....");
		
		addPurchaseWindow.addPurchase(config.getString("purchase.subTotal"),getRandomOrderId(),config.getString("purchase.orderConfirmation"));
		
		LOG.info("Purchase added");
		
		//verify cash back
		//assertEquals(postPurchasePage.getCashBack(),expectCashBack,"Earnings after add purchase not as expected");
		assertEquals(historyPage.getMessage(),config.getString("message.addpurchase"),"Earnings after add purchase not as expected");
		
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void visitVipStoreWithOutVipUser(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"), driver);

		ChinaMerchantPage storePage = homePage.searchStore(config.getString("vipOnly.store.searchString"));

		assertTrue(!storePage.getStoreName().contains(config.getString("vipOnly.store.searchString")),"store name not as expected");
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void visitVipStoreWithVipUser(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.adminUserEmail"), config.getString("cn.adminUserPassword"), driver);

		ChinaMerchantPage storePage = homePage.searchStore(config.getString("vipOnly.store.searchString"));
		
		assertTrue(storePage.getStoreName().contains(config.getString("vipOnly.store.searchString")),"store name not as expected");
	}
	
	public static String getVipBonus(Double vipCashBackPercentage,Double oldCashBackPercentage,Double purchaseTotal){
		NumberFormat format = DecimalFormat.getCurrencyInstance(Locale.US);
		format.setMaximumFractionDigits(2);
		Double vipBonusDouble = (vipCashBackPercentage - oldCashBackPercentage)*purchaseTotal;
		assertTrue(vipBonusDouble>0,"vip cach back is not higher than normal cash back");
		String vipBonus = format.format(vipBonusDouble);
		return vipBonus; 
	}
}
