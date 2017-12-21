package com.extrabux.tests.cn;

import org.testng.annotations.Test;

import com.extrabux.pages.StoreTransferPage;
import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
import com.extrabux.pages.cn.ChinaMerchantPage;
import com.extrabux.pages.cn.ChinaStoresListPage;

import static org.testng.Assert.assertEquals;
//import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;


public class ChinaStoreListTests extends ChinaBaseTest {

	private static final Log LOG = LogFactory.getLog(ChinaStoreListTests.class);	

	@Test(dataProvider = "getWebDriver")
	public void filterByCategory(WebDriver driver) throws Exception {
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		ChinaStoresListPage storeListPage = homePage.clickStoreNav();
		
		storeListPage.filterByLink(config.getString("storeList.category"));
		assertTrue(storeListPage.verifyStorePresent(config.getString("storeList.category.expectStoreName")));
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterByRegion(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		ChinaStoresListPage storeListPage = homePage.clickStoreNav();

		storeListPage.filterByLink(config.getString("storeList.region"));
		assertTrue(storeListPage.verifyStorePresent(config.getString("storeList.region.expectStoreName")));
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterByInitial(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		ChinaStoresListPage storeListPage = homePage.clickStoreNav();

		storeListPage.filterByLink(config.getString("storeList.initial"));
		assertTrue(storeListPage.verifyStorePresent(config.getString("storeList.initial.expectStoreName")));
	}
	
	@Test(dataProvider = "getWebDriver")
	public void sortByCashBack(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		ChinaStoresListPage storeListPage = homePage.clickStoreNav();

		storeListPage.sort("返利");
		assertTrue(storeListPage.verifyCashBackSortResult(),"sort by cash back result not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void sortByPurchase(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		ChinaStoresListPage storeListPage = homePage.clickStoreNav();

		storeListPage.sort("下单成功");
		assertTrue(storeListPage.verfiySuccessPurchaseSortResult(),"sort by success purchase result not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void storeTransferFromStoreList(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"), driver);
		
		ChinaStoresListPage storesListpage = homePage.clickStoreNav();
		
		StoreTransferPage transferPage = storesListpage.clickStartShopping();
		
		LOG.debug("page url after clicking start shopping" + driver.getCurrentUrl());

		transferToMerchant(transferPage,driver,config.getString("store.url"));

		assertTrue(storesListpage.verifyAddPurchaseLinkPresent());
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterByFav(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);
		ChinaStoresListPage storesPage = homePage.clickStoreNav();
		
		storesPage.filterWithCheckBox("我的收藏商家");
		assertTrue(storesPage.verifyStorePresent(config.getString("storeList.fav.storeName")));
		
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterByDirectToCN(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		ChinaStoresListPage storeListPage = homePage.clickStoreNav();

		storeListPage.filterWithCheckBox("直邮中国");
		assertTrue(storeListPage.verifyShippingToChina());
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterByChinaCard(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		ChinaStoresListPage storeListPage = homePage.clickStoreNav();

		storeListPage.filterWithCheckBox("国卡");
		assertTrue(storeListPage.verifyChinaCard());
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterByAlipay(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		ChinaStoresListPage storeListPage = homePage.clickStoreNav();
		
		storeListPage.filterWithCheckBox("支付宝");
		assertTrue(storeListPage.verifyAlipay());
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterByAll(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		ChinaStoresListPage storeListPage = homePage.clickStoreNav();

		storeListPage.filterWithCheckBox("国卡");
		storeListPage.filterWithCheckBox("支付宝");
		storeListPage.filterWithCheckBox("直邮中国");
		
		assertTrue(storeListPage.verifyAll());
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterAndSort(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		ChinaStoresListPage storeListPage = homePage.clickStoreNav();

		storeListPage.filterByLink(config.getString("storeList.category"));
		storeListPage.sort("下单成功");
		
		assertTrue(storeListPage.verifyStorePresent(config.getString("storeList.category.expectStoreName")));
		assertTrue(storeListPage.verfiySuccessPurchaseSortResult(),"sort by success purchase result not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void pageDownThenPageUp(WebDriver driver) throws Exception{
		ChinaStoresListPage storeListPage = new ChinaStoresListPage(driver);
		storeListPage.goToURL(storeListPage.getUrl(serverName));
		
		storeListPage.selectPage("2");
		assertEquals(storeListPage.getCurrentPage(),"2","page down failed");
		
		//click page up
		storeListPage.selectPage("1");
		assertEquals(storeListPage.getCurrentPage(),"1","page up failed");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void sortByEndDateStorePage(WebDriver driver)  throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"), driver);

		ChinaMerchantPage storePage = homePage.searchStore(config.getString("store.searchString"));

		storePage.sortByEndDate();
		
		assertTrue(storePage.verifySortByEndDate(),"sort result not as expected");
	}
}
