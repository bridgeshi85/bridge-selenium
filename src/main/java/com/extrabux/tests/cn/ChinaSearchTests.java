package com.extrabux.tests.cn;

import static org.testng.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
import com.extrabux.pages.cn.ChinaHomePageBeforeLogin;
import com.extrabux.pages.cn.ChinaSearchResultPage;

public class ChinaSearchTests extends ChinaBaseTest{
	
	private static final Log LOG = LogFactory.getLog(ChinaSearchTests.class);
	
	@Test(dataProvider = "getSearchStoreInfo")
	public void searchStore(WebDriver driver, String keyword, String storeUri) throws Exception {
		//Type keyword[full store name, store keyword, store Chinese name], search_type = store -> Individual store page
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		homePage.search(keyword);
		String currentUrl = driver.getCurrentUrl();
		LOG.debug("currentUrl : " + currentUrl);
		LOG.debug(storeUri);

		assertTrue(currentUrl.contains(storeUri), "Expected to be on individual store page, but is on " + driver.getTitle() + " (" + driver.getCurrentUrl() + ").  Something may have gone wrong with search full store name.");
	}
	
	@Test(dataProvider = "getSearchCouponInfo")
	public void searchCoupon(WebDriver driver, String keyword) throws Exception {
		
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);

		LOG.debug("Open env...");
	    ChinaSearchResultPage searchResultPage = homePage.search(keyword, "优惠");
	    LOG.debug("After search...");
	    
		//String currentUrl = driver.getCurrentUrl();		
		String tabName = searchResultPage.getCurrentTabName();
		//String firstItem = searchResultPage.getFirstStoreOrCouponItem();
		//LOG.debug("Current Url is " + currentUrl + "; Current tab is " + tabName + "; First item of search result is " + firstItem);
		
		assertTrue(tabName.equals("优惠"), "Expected current tab is 优惠, but it's not!");
		assertTrue(searchResultPage.verifyCouponExist(keyword), "Expected first item of search result contains " + keyword +", but it's not!");
	}
	
	//@Test(dataProvider = "getSearchHaitaoProductInfo")
	public void searchHaitaoProduct(WebDriver driver, String keyword) throws Exception{
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),driver);
		
		ChinaSearchResultPage searchResultPage = homePage.search(keyword, "商品");
		
		String tabName = searchResultPage.getCurrentTabName();
		
		assertTrue(tabName.equals("商品"), "Expected current tab is 商品, but it's not!");
		assertTrue(searchResultPage.verifyProductExist(keyword), "Expected first item of search result contains " + keyword +", but it's not!");

	}
	
	/*
	
	@Test
	public void searchPartStoreName(String keyword) {
		//Type keyword[part store name], search_type = store -> China search result page
	}
	
	@Test
	public void searchFullBlogTitle(String keyword) {
		//Type keyword[Blog title], search_type = blog -> Individual blog page
	}
	

    @Test
	public void searchStoreAndGoSuggestionItem(String keyword, int i){
	//Type keyword [store name], search_type = store -> Select the [i] suggestion item -> Individual store page
	
	}
	
	@Test
	public void searchCouponAndGoSuggestionItem(String keyword, int i){
	//Type keyword [coupon name], search_type = coupon -> Select the [i] suggestion item -> Individual coupon page
	}
	
	@Test
	public void searchBlogAndGoSuggestionItem(String keyword, int i){
	//Type keyword [blog title], search_type = blog -> Select the [i] suggestion item -> Individual blog page
	 
	}
	 * */

	@DataProvider(name = "getSearchStoreInfo")
	public Object[][] getSearchStore(ITestContext testNGContext, ITestNGMethod method) throws Exception {
		return new Object[][]{
				{(WebDriver)getDriver(testNGContext, method)[0][0],config.getString("store.searchString"),config.getString("store.uri")},
				{(WebDriver)getDriver(testNGContext, method)[0][0],config.getString("store.searchKeyWord"),config.getString("store.uri")}
		};
	}
	
	@DataProvider(name = "getSearchCouponInfo")
	public Object[][] getSearchCoupon(ITestContext testNGContext, ITestNGMethod method) throws Exception {
		return new Object[][]{
				{(WebDriver)getDriver(testNGContext, method)[0][0],config.getString("coupon.searchFullCouponName")},
				{(WebDriver)getDriver(testNGContext, method)[0][0],config.getString("coupon.searchPartCoupon1")},
				{(WebDriver)getDriver(testNGContext, method)[0][0],config.getString("coupon.searchPartCoupon2")},
				{(WebDriver)getDriver(testNGContext, method)[0][0],config.getString("coupon.searchPartCoupon3")}
		};
	}
	
	@DataProvider(name = "getSearchHaitaoProductInfo")
	public Object[][] getSearchProduct(ITestContext testNGContext, ITestNGMethod method) throws Exception {
		return new Object[][]{
				{(WebDriver)getDriver(testNGContext, method)[0][0],config.getString("haitao.productName")},
		};
	}
}
