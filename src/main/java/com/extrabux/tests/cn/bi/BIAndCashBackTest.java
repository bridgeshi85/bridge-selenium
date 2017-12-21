package com.extrabux.tests.cn.bi;

import static org.testng.Assert.assertEquals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.util.GoogleSpreedSheetUtil;
import com.extrabux.pages.cn.bi.AWLoginPage;
import com.extrabux.pages.cn.bi.AWMerchantPage;
import com.extrabux.pages.cn.bi.CJAdvertisersPage;
import com.extrabux.pages.cn.bi.CJHomePageAfterLogin;
import com.extrabux.pages.cn.bi.CJHomePageBeforeLogin;
import com.extrabux.pages.cn.bi.LinkShareHomePage;
import com.extrabux.pages.cn.bi.LinkShareLoginPage;
import com.extrabux.pages.cn.bi.LinkShareOfferPage;
import com.extrabux.pages.cn.bi.PepperJamAdPage;
import com.extrabux.pages.cn.bi.PepperJamHomePage;
import com.extrabux.pages.cn.bi.PepperJamLoginPage;
import com.extrabux.tests.cn.ChinaBaseTest;

public class BIAndCashBackTest extends ChinaBaseTest{

	private static final Log LOG = LogFactory.getLog(BIAndCashBackTest.class);
	
	@Test(dataProvider = "getWebDriver")
	public void testCJCommission(WebDriver driver) throws Exception{
		String [][]CommissionDates = GoogleSpreedSheetUtil.getExtrabuxCommission(config.getString("bi.cj.sq"));
		
		CJHomePageBeforeLogin homePage = new CJHomePageBeforeLogin(driver);
		homePage.goToURL(homePage.getAffiliateUrl());
		
		CJHomePageAfterLogin homePageAfter = homePage.loging(config.getString("bi.cj.username"), 
				config.getString("bi.cj.password"));
		LOG.info("Log in cj.com Successful。。...");
		
		CJAdvertisersPage adPage = homePageAfter.goToAdvertisersPage();
		
		for(int i = 0;i < CommissionDates.length;i++){
			adPage.typeMearchtId(CommissionDates[i][0]);
			adPage.submitSearch();
			adPage.clearFilter();
			assertEquals(getPercent(adPage.getCommissions()),getPercent(CommissionDates[i][1]),"Merchant id :"+CommissionDates[i][0] + 
					",Extrabux commission as not expected");
		}
	}
	
	@Test(dataProvider = "getWebDriver")
	public void testLinkShareCommission(WebDriver driver) throws Exception{
		String [][]CommissionDates = GoogleSpreedSheetUtil.getExtrabuxCommission(config.getString("bi.linkshare.sq"));
		
		LinkShareLoginPage loginPage = new LinkShareLoginPage(driver);
		loginPage.goToURL(loginPage.getLinkShareLoginUrl());
		
		LinkShareHomePage homePage = loginPage.login(config.getString("bi.defaultUsername")
				, config.getString("bi.defaultPassword"));

		for(int i = 0;i < CommissionDates.length;i++){
			LinkShareOfferPage offerPage = homePage.getAvailbleOfferRequestLink(CommissionDates[i][0]);
			assertEquals(getPercent(offerPage.getCommission()),getPercent(CommissionDates[i][1]),"Merchant id :"+CommissionDates[i][0] + 
					",Extrabux commission as not expected");
		}
	}

	@Test(dataProvider = "getWebDriver")
	public void testPepperJamCommission(WebDriver driver) throws Exception{
		String [][]CommissionDates = GoogleSpreedSheetUtil.getExtrabuxCommission(config.getString("bi.pepperjam.sq"));

		PepperJamLoginPage loginPage = new PepperJamLoginPage(driver);
		loginPage.goToURL(loginPage.getPepperJamLoginUrl());
		
		PepperJamHomePage homePage = loginPage.login(config.getString("bi.pepperjam.username")
				, config.getString("bi.pepperjam.password"));
		
		PepperJamAdPage adPage = homePage.goToAdvertisersPage();
		adPage.closeFilterStatus();
		adPage.openFilterid();
		
		for(int i = 0;i < CommissionDates.length;i++){
			adPage.filterById(CommissionDates[i][0]);
			assertEquals(getPercent(adPage.getCommission()),getPercent(CommissionDates[i][1]),"Merchant id :"+CommissionDates[i][0] + 
					",Extrabux commission as not expected");
		}
		
	}
	
	@Test(dataProvider = "getWebDriver")
	public void testAWCommission (WebDriver driver) throws Exception{
		String [][]CommissionDates = GoogleSpreedSheetUtil.getExtrabuxCommission(config.getString("bi.aw.sq"));
		
		AWLoginPage loginPage = new AWLoginPage(driver);
		loginPage.goToURL(loginPage.getAWLoginUrl());
		
		loginPage.Login(config.getString("bi.aw.username")
				, config.getString("bi.aw.password"));
		
		AWMerchantPage merchantPage = new AWMerchantPage(driver);
		for(int i = 0;i < CommissionDates.length;i++){
			merchantPage.goToURL(merchantPage.getCommissionUrl(CommissionDates[i][0].toString()));
			assertEquals(getPercent(merchantPage.getCommissionValue()),getPercent(CommissionDates[i][1]),"Merchant id :"+CommissionDates[i][0] + 
					",Extrabux commission as not expected");
		}
	}
}
