package com.extrabux.tests.cn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import com.extrabux.pages.cn.account.ChinaAccountSettingPage;
import com.extrabux.pages.cn.account.ChinaMyCouponsPage;
import com.extrabux.pages.cn.account.ChinaMyEarningsPage;
import com.extrabux.pages.cn.account.ChinaTrackingHistoryPage;
import com.extrabux.pages.cn.promotion.China17thAnniversaryPage;
import com.extrabux.pages.cn.promotion.ChinaCMBCPage;
import com.extrabux.pages.cn.promotion.ChinaCMBCPageAfterLogin;
import com.extrabux.pages.cn.promotion.ChinaShipperPromoPage;
import com.extrabux.pages.cn.promotion.ChineseNewYearPage;
import com.extrabux.pages.cn.promotion.MasterCardPromotionPageAfterLogin;
import com.extrabux.pages.cn.promotion.MasterCardPromotionPageBeforeLogin;
import com.extrabux.pages.cn.promotion.SpdbPromotionPageAfterLogin;
import com.extrabux.pages.cn.promotion.SpdbPromotionPageBeforeLogin;
import com.extrabux.pages.cn.promotion.VisaPromotionPageAfterLogin;
import com.extrabux.pages.cn.promotion.VisaPromotionPageBeforeLogin;
import com.extrabux.util.WebDriverUtil;

public class ChinaPromotionTests extends ChinaBaseTest {

	private static final Log LOG = LogFactory.getLog(ChinaPromotionTests.class);

	private static String masterCardUser;
	private static String visaUser;
	
	// @Test(dataProvider = "getWebDriver",groups="production-test")
	public void signUpSpdb(WebDriver driver) throws Exception {
		try {
			SpdbPromotionPageBeforeLogin spdbPage = new SpdbPromotionPageBeforeLogin(driver);
			spdbPage.goToURL(spdbPage.getUrl(serverName));

			String email = getRandomEmail();

			SpdbPromotionPageAfterLogin spdbPageAfterLogin = spdbPage.signUp(email, config.getString("defaultPassword"),
					config.getString("spdb.signUpCode"));

			// close fancy box
			// spdbPageAfterLogin.closeFancyBox();

			assertTrue(spdbPageAfterLogin.verifyLoggedInElementPresent(email), "Logged in email not as expected.");

			// verify the sign up bonus
			String expected = DecimalFormat.getCurrencyInstance(Locale.US)
					.format(config.getDouble("signup.spdbBonusAmount"));
			assertEquals(spdbPageAfterLogin.getLifetimeEarnings(), expected, "Earnings after signup not as expected");

			// verify visa bind status
			String expectStatus = config.getString("spdb.unbind");
			assertEquals(spdbPageAfterLogin.getPromotionStatus(), expectStatus,
					"Spdb status after signup not as expected");

		} catch (Exception e) {
			throw new Exception("currentPage title: " + driver.getTitle() + "\n currentPage url: "
					+ driver.getCurrentUrl() + "\n" + e.getMessage());
		}
	}

	//@Test(dataProvider = "getWebDriver", groups = "production-test")
	public void signUpVisa(WebDriver driver) throws Exception {
		VisaPromotionPageBeforeLogin visaPage = new VisaPromotionPageBeforeLogin(driver);
		visaPage.goToURL(visaPage.getUrl(serverName));

		visaUser = getRandomEmail();

		VisaPromotionPageAfterLogin visaPageAfterLogin = visaPage.signUp(visaUser, config.getString("defaultPassword"),
				config.getString("visa.signUpCode"));

		// close fancy box
		// visaPageAfterLogin.closeFancyBox();

		assertTrue(visaPageAfterLogin.verifyLoggedInElementPresent(visaUser), "Logged in email not as expected.");

		// verify the sign up bonus
		String expected = DecimalFormat.getCurrencyInstance(Locale.US)
				.format(config.getDouble("signup.spdbBonusAmount"));
		assertEquals(visaPageAfterLogin.getLifetimeEarnings(), expected, "Earnings after signup not as expected");

		// verify visa bound status
		String expectStatus = config.getString("visa.unbind");
		assertEquals(visaPageAfterLogin.getPromotionStatus(), expectStatus, "Visa status after signup not as expected");
	}
	
	//@Test(dataProvider = "getWebDriver",dependsOnMethods="signUpVisa")
	public void bindVisaCard(WebDriver driver) throws Exception {
		ChinaHomePageAfterLogin homePage = login(visaUser,config.getString("defaultPassword"),driver);
		
		ChinaAccountSettingPage settingPage = homePage.hoverAndClickAccountSetting();
		
		//bind mobile
		String phoneNumber = getRandomPhoneNumber();
		settingPage.bindMobile(phoneNumber, config.getString("account.sms"));
		
		assertEquals(settingPage.getLastFourPhoneNumber(),phoneNumber.substring(7),"last four number as not expected");
		
		//verify mobile
		settingPage.verifyPhone(phoneNumber.substring(0, 7),config.getString("account.sms"));
		
		WebDriverUtil.waitForPageToLoadComplete(driver, 30);
		
		settingPage.closeVisaRemind();
		
		settingPage.setCreditCardPayment(config.getString("account.CardNumber"),
				config.getString("account.shenpay.bankName"),
				config.getString("account.shenpay.bankUserName"));
		
		assertEquals(settingPage.getCurrentPaymentMethod(),"shenpayTagCreditCard","Current payment method as not expected");
	}
	
	//@Test(dataProvider = "getWebDriver",dependsOnMethods="bindVisaCard")
	public void testAddPurchaseWithVisaUser(WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = login(visaUser, config.getString("defaultPassword"), driver);

		ChinaMerchantPage storePage = homePage.searchStore(config.getString("visa.store.searchString"));
		
		StoreTransferPage transferPage = storePage.clickStartShopping();
		
		transferToMerchant(transferPage,driver,config.getString("visa.store.url"));
		
		//get expect cash back
		NumberFormat format = DecimalFormat.getCurrencyInstance(Locale.US);
		format.setMaximumFractionDigits(2);
		String expectCashBack = format.format(getPercent(storePage.getCashBack())*config.getDouble("purchase.subTotal"));
		String expectVisaBonus = format.format(config.getDouble("visa.bonusPercent")*config.getDouble("purchase.subTotal"));
		
		ChinaMyEarningsPage earningsPage = homePage.clickMyEarnings();
		ChinaTrackingHistoryPage historyPage = earningsPage.clickHistory();
		
		assertTrue(historyPage.verifyPurchaseLinkPresent());
		
		ChinaAddPurchaseWindow addPurchaseWindow = historyPage.addPruchase();
			
		LOG.info("Start add purcharse....");
		
		PostPurchasePage postPurchasePage = addPurchaseWindow.addPurchase(config.getString("purchase.subTotal"),getRandomOrderId(),config.getString("purchase.orderConfirmation"));
		
		LOG.info("Purchase added");
		
		//verify cash back
		assertEquals(postPurchasePage.getCashBack(),expectCashBack,"Earnings after add purchase not as expected");
		
		earningsPage = homePage.clickMyEarnings();
		
		assertEquals(earningsPage.getVisaBonus(),expectVisaBonus,"vip bonus not as epected");
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void signUpMasterCard(WebDriver driver) throws Exception {
		MasterCardPromotionPageBeforeLogin masterPage = new MasterCardPromotionPageBeforeLogin(driver);
		masterPage.goToURL(masterPage.getUrl(serverName));

		masterCardUser = getRandomEmail();

		MasterCardPromotionPageAfterLogin masterPageAfterLogin = masterPage.signUp(masterCardUser,
				config.getString("defaultPassword"), config.getString("master.signUpCode"));

		// close fancy box
		// visaPageAfterLogin.closeFancyBox();

		assertTrue(masterPageAfterLogin.verifyLoggedInElementPresent(masterCardUser), "Logged in email not as expected.");

		// verify visa bound status
		String expectStatus = config.getString("visa.unbind");
		assertEquals(masterPageAfterLogin.getPromotionStatus(), expectStatus,
				"Visa status after signup not as expected");
	}
	
	//@Test(dataProvider = "getWebDriver",dependsOnMethods="signUpMasterCard")
	public void setPaymentToMasterCard(WebDriver driver) throws Exception{
		
		ChinaHomePageAfterLogin homePage = login(masterCardUser,config.getString("defaultPassword"),driver);
		
		ChinaAccountSettingPage settingPage = homePage.hoverAndClickAccountSetting();
		
		//bind mobile
		String phoneNumber = getRandomPhoneNumber();
		settingPage.bindMobile(phoneNumber, config.getString("account.sms"));
		
		assertEquals(settingPage.getLastFourPhoneNumber(),phoneNumber.substring(7),"last four number as not expected");
		
		//verify mobile
		settingPage.verifyPhone(phoneNumber.substring(0, 7),config.getString("account.sms"));
		
		WebDriverUtil.waitForPageToLoadComplete(driver, 30);
		
		//set payment to master card
		settingPage.setCreditCardPayment(config.getString("account.masterCardNumber"),
				config.getString("account.shenpay.bankName"),
				config.getString("account.shenpay.bankUserName"));
		
		assertEquals(settingPage.getCurrentPaymentMethod(),"shenpayTagCreditCard","Current payment method as not expected");
		
		// reset cache
		//resetLocalCache(config.getString("cn.adminUserEmail"), config.getString("cn.adminUserPassword"));

		//refresh the page
		//settingPage = homePage.hoverAndClickAccountSetting();
		
		// verify the sign up bonus
		//String expected = DecimalFormat.getCurrencyInstance(Locale.US).format(config.getDouble("master.signup.bonusCodeAmount"));
		//assertEquals(homePage.getLifetimeEarnings(), expected, "Earnings after signup not as expected");

		assertTrue(homePage.verifyVipUser());
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void newYearLuckyDraw(WebDriver driver) throws Exception{
		signUp(getRandomEmail(),config.getString("defaultPassword"),driver);
		
		ChineseNewYearPage newYearPage = new ChineseNewYearPage(driver);
		
		newYearPage.goToURL(newYearPage.getUrl(serverName));
		
		newYearPage.closeFancyBox();
		newYearPage.luckyDraw();
		
		ChinaMyEarningsPage myEarningPage = newYearPage.clickMyEarnings();
		
		List<String> redPocketList = Arrays.asList(config.getStringArray("newYear.redPocketLlist"));

		assertTrue(redPocketList.contains(myEarningPage.getRedPocket()),"red pocket name not as expected");
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void seventeenAnniversary(WebDriver driver) throws Exception{
		signUp(getRandomEmail(),config.getString("defaultPassword"),driver);
		
		China17thAnniversaryPage seventeenPage = new China17thAnniversaryPage(driver);
		
		seventeenPage.goToURL(seventeenPage.getUrl(serverName));
		
		ChinaMyEarningsPage myEarningsPage = seventeenPage.luckyDraw();
		
		assertTrue(myEarningsPage.puzzlePresent(),"puzzle dosen't exist");
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void shipperPromotionDraw(WebDriver driver) throws Exception{
		signUp(getRandomEmail(),config.getString("defaultPassword"),driver);
		
		ChinaShipperPromoPage shipperPromoPage = new ChinaShipperPromoPage(driver);
		
		shipperPromoPage.goToURL(shipperPromoPage.getUrl(serverName));
		
		ArrayList<String> couponNameList = new ArrayList<String>();
		
		for(int i = 0 ;i<3;i++){
			String couponName = shipperPromoPage.shipperLuckyDraw();
			LOG.info(couponName);
			if(couponName != "谢谢参与")
				couponNameList.add(couponName);
			shipperPromoPage.closeCouponBox();
		}
		
		assertTrue(couponNameList.size()>0,"no coupon get");
		
		ChinaMyCouponsPage myCouponsPage = new ChinaMyCouponsPage(driver);
		
		myCouponsPage.goToURL(myCouponsPage.getUrl(serverName));
		
		assertTrue(myCouponsPage.verifyCouponList(couponNameList));
		//List<String> redPocketList = Arrays.asList(config.getStringArray("newYear.redPocketLlist"));

		//assertTrue(redPocketList.contains(myEarningPage.getRedPocket()),"red pocket name not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void cmbcLogin(WebDriver driver) throws Exception{
		ChinaCMBCPage cmbcPage = new ChinaCMBCPage(driver);
		cmbcPage.goToURL(cmbcPage.getUrl(serverName));

		ChinaCMBCPageAfterLogin cmbcPageAfterLogin = cmbcPage.login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"));

		assertTrue(cmbcPageAfterLogin.verifyLoggedInElementPresent(config.getString("cn.existingUserEmail")), "Logged in email on page not as expected");

	}
}
