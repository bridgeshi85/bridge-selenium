package com.extrabux.tests.cn;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;

import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
import com.extrabux.pages.cn.account.ChinaAccountSettingPage;
import com.extrabux.pages.cn.account.ChinaPaymentHistoryPage;
import com.extrabux.util.DBUtil;

public class ChinaCashOutTests extends ChinaBaseTest {

	// private static final Log LOG =
	// LogFactory.getLog(ChinaCashOutTests.class);

	private DBUtil db;

	@BeforeClass
	public void updateDataAndResetCache() throws Exception{
		 db = getDBUtil();
		 List<String> cashOutUserList = Arrays.asList(config.getStringArray("cn.cashOut.userList"));
		 
		 for(String userName : cashOutUserList){
			 int id = db.getMemberIdByEmail(userName);
			 updatePurchaseAndCommission(id);
		 }
		// reset cache
		resetLocalCache(config.getString("cn.adminUserEmail"), config.getString("cn.adminUserPassword"));
	}

	public void updatePurchaseAndCommission(int memberId) throws Exception {
		db.updatePurchaseToAvailabelByUserId(memberId, 1400);
		db.updateCommissionToAvailabelByUserId(memberId);
		db.deleteAllPaymentByUserId(memberId);
	}

	@Test(dataProvider = "getUserInfoAndWebDriver",dependsOnGroups="setPayment")
	public void cashOut(WebDriver driver, String userName, String passWord, String expectRequestMethod,
			Double expectAmount) throws Exception {

		//int id = db.getMemberIdByEmail(userName);

		// update purchase to available
		//updatePurchaseAndCommission(id);

		// reset cache
		//resetLocalCache(config.getString("cn.adminUserEmail"), config.getString("cn.adminUserPassword"));

		ChinaHomePageAfterLogin homePage = login(userName, passWord, driver);

		ChinaAccountSettingPage settingPage = homePage.hoverAndClickAccountSetting();

		settingPage.verifyPhone(config.getString("account.phoneNumber"), config.getString("account.sms"));

		ChinaPaymentHistoryPage paymentHistoryPage = settingPage.requestPayment();

		String expected = setDoubleToString(expectAmount);
		
		List<String> expectPaymentInfos = new ArrayList<String>();

		expectPaymentInfos.add("提现中");
		expectPaymentInfos.add(expectRequestMethod);
		expectPaymentInfos.add(expected);

		// assert paymentInfo
		assertEquals(paymentHistoryPage.getPaymentInfoList(), expectPaymentInfos, "payment info not as expected");
	}

	@Test(dataProvider = "getWebDriver",groups="setPayment")
	public void testSetPayMethod(WebDriver driver) throws Exception {
		ChinaAccountSettingPage settingPage = loginAndVerifyPhone(config.getString("cn.cashOut.paypalUserEmail"),
				config.getString("cn.existingUserPassword"), config.getString("account.phoneNumber"), driver);

		settingPage.setPaypalPayment(config.getString("account.paypal.email"));

		assertTrue(settingPage.verifySetPayemntMethodSucessMsgPresent("您的付款方式已经成功更新好了。"));
		//assertEquals(settingPage.getCurrentPaymentMethod(), "paypal", "Current payment method as not expected");
	}

	@Test(dataProvider = "getWebDriver",groups="setPayment")
	public void testSetShenpayMethod(WebDriver driver) throws Exception {
		ChinaAccountSettingPage settingPage = loginAndVerifyPhone(config.getString("cn.cashOut.shenpayUserEmail"),
				config.getString("cn.existingUserPassword"), config.getString("account.phoneNumber"), driver);

		settingPage.setShenpayPayment(config.getString("account.CardNumber"),
				config.getString("account.shenpay.bankName"), config.getString("account.shenpay.bankUserName"),
				config.getString("account.shenpay.nid"));

		assertTrue(settingPage.verifySetPayemntMethodSucessMsgPresent("您的付款方式已经成功更新好了。"));
		//assertEquals(settingPage.getCurrentPaymentMethod(), "shenpay", "Current payment method as not expected");

	}

	@Test(dataProvider = "getWebDriver",groups="setPayment")
	public void testSetCreditCardPayMethod(WebDriver driver) throws Exception{
		//db.deletePaymentMethodsByEmail(config.getString("cn.cashOut.cardUserEmail"));
		
		ChinaAccountSettingPage settingPage = loginAndVerifyPhone(config.getString("cn.cashOut.cardUserEmail"),config.getString("cn.existingUserPassword"),config.getString("account.phoneNumber"),driver);
		
		settingPage.setCreditCardPayment(config.getString("account.CardNumber"),
				config.getString("account.shenpay.bankName"),
				config.getString("account.shenpay.bankUserName"));
		
		assertTrue(settingPage.verifySetPayemntMethodSucessMsgPresent("您的付款方式已经成功更新好了。"));
		//assertEquals(settingPage.getCurrentPaymentMethod(),"shenpayTagCreditCard","Current payment method as not expected");
		
	}
	
	//@Test(dataProvider = "getWebDriver",dependsOnMethods="cashOut")
	public void removeCreditCard(WebDriver driver) throws Exception {
		
		ChinaAccountSettingPage settingPage = loginAndVerifyPhone(config.getString("cn.cashOut.cardUserEmail"),config.getString("cn.existingUserPassword"),config.getString("account.phoneNumber"),driver);
		
		//verify current payment is credit card
		assertEquals(settingPage.getCurrentPaymentMethod(),"creditCard","Current payment method as not expected");

		settingPage.removeCreditCard();
		
		assertEquals(settingPage.getCurrentPaymentMethod(),"none","Current payment method as not expected");

	}
	
	@Test(dataProvider = "getWebDriver",dependsOnMethods="cashOut")
	public void removeCashOut(WebDriver driver) throws Exception {		
		login(config.getString("cn.cashOut.cardUserEmail"), config.getString("cn.existingUserPassword"), driver);
				
		ChinaPaymentHistoryPage paymentHistoryPage = new ChinaPaymentHistoryPage(driver);
		
		//go to payment history
		paymentHistoryPage.goToURL(paymentHistoryPage.getUrl(serverName));
		
		int pendingPayment = paymentHistoryPage.getPendingPaymentTotal();
		
		//verify user have pending payment
		assertTrue(pendingPayment>0,"no pending payment");
		
		//cancel
		paymentHistoryPage.cancelPurchase();
		
		assertEquals(paymentHistoryPage.getPendingPaymentTotal(),pendingPayment-1,"payment cancel failed");
	}
	
	@DataProvider(name = "getUserInfoAndWebDriver")
	public Object[][] getUserInfoAndWebDriver(ITestContext testNGContext, ITestNGMethod method) throws Exception {
		return new Object[][] {{ (WebDriver) getDriver(testNGContext, method)[0][0],
				config.getString("cn.cashOut.cardUserEmail"), config.getString("cn.existingUserPassword"), "银行卡",
				config.getDouble("cn.cashOut.cardPaymentAmount") }, 
			{ (WebDriver) getDriver(testNGContext, method)[0][0],
					config.getString("cn.cashOut.shenpayUserEmail"), config.getString("cn.existingUserPassword"), "银行卡",
					config.getDouble("cn.cashOut.cardPaymentAmount") },
			{ (WebDriver) getDriver(testNGContext, method)[0][0],
						config.getString("cn.cashOut.paypalUserEmail"), config.getString("cn.existingUserPassword"), "Paypal",
						config.getDouble("cn.cashOut.cardPaymentAmount") },
			};
	}

	public static String setDoubleToString(double amount){
	    NumberFormat DollarFormat  = new DecimalFormat("$######0.00");   
    	if(amount % 1.0 == 0){
    		DollarFormat = new DecimalFormat("$######0");   
    	}
    	else if(amount *10 % 1.0 == 0){
    		DollarFormat = new DecimalFormat("$######0.0");   
    	}
    	
    	return DollarFormat.format(amount);
	}
}
