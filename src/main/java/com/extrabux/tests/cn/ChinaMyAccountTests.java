package com.extrabux.tests.cn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import com.extrabux.pages.account.AccountSettingsPage;
import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
import com.extrabux.pages.cn.ChinaHomePageBeforeLogin;
import com.extrabux.pages.cn.ChinaLoginPage;
import com.extrabux.pages.cn.account.ChinaAccountSettingPage;
import com.extrabux.pages.cn.account.ChinaInquiriesPage;
import com.extrabux.pages.cn.account.ChinaMyEarningsPage;

public class ChinaMyAccountTests extends ChinaBaseTest{
	
	private static final Log LOG = LogFactory.getLog(ChinaMyAccountTests.class);
	private static String memberEmail;
	private static String phoneNumber;
	
	@Test(dataProvider = "getWebDriver")
	public void testRemovePurchase(WebDriver driver) throws Exception{
		//login
		ChinaHomePageAfterLogin homePage = login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"), driver);
			
		//close fancy box
		//homePage.closeFancyBox();
			
		ChinaMyEarningsPage earningsPage = homePage.clickMyEarnings();
			
		ChinaInquiriesPage inquiriesPage = earningsPage.clickAddedByMeTab();
			
		int unConfirmedOrderTotal = inquiriesPage.getUnConfirmedPurchaseTotal();
		LOG.info("unconfirmed order total:"+ unConfirmedOrderTotal);
		assertTrue(unConfirmedOrderTotal > 0,"unconfirmed order not found");
			
		inquiriesPage.removePurchase();
		LOG.info("purchase removed。。。。");

		assertEquals(inquiriesPage.getUnConfirmedPurchaseTotal(),unConfirmedOrderTotal-1,"confirmed order total not as expected");
	}

	@Test(dataProvider = "getWebDriver")
	public void testSetCheckMethod(WebDriver driver) throws Exception{

		ChinaAccountSettingPage settingPage = loginAndVerifyPhone(config.getString("cn.existingUserEmail"),config.getString("cn.existingUserPassword"),config.getString("account.phoneNumber"),driver);
				
		settingPage.setCheckPayment(config.getString("account.check.firstName"), 
				config.getString("account.check.lastName"), 
				config.getString("account.check.addr"), 
				config.getString("account.check.city"),
				config.getString("account.check.zip"));
		
		//assertTrue(settingPage.verifySetPayemntMethodSucessMsgPresent("您的付款方式已经成功更新好了。"));
		assertEquals(settingPage.getCurrentPaymentMethod(),"check","Current payment method as not expected");

	}
	

	
	//@Test(dataProvider = "getWebDriver")
	public void testSetGiftCardMethod(WebDriver driver) throws Exception{
		ChinaAccountSettingPage settingPage = loginAndVerifyPhone(config.getString("cn.existingUserEmail"),config.getString("cn.existingUserPassword"),config.getString("account.phoneNumber"),driver);
		
		settingPage.setGiftCardPayment();
		
		//assertTrue(settingPage.verifySetPayemntMethodSucessMsgPresent("您的付款方式已经成功更新好了。"));
		assertEquals(settingPage.getCurrentPaymentMethod(),"giftCard","Current payment method as not expected");
	}


 
	
	@Test(dataProvider = "getWebDriver")
	public void setShenpayInvalidCard(WebDriver driver) throws Exception {
		ChinaAccountSettingPage settingPage = loginAndVerifyPhone(config.getString("cn.existingUserEmail"),config.getString("cn.existingUserPassword"),config.getString("account.phoneNumber"),driver);

		String currentPaymentMethod = settingPage.getCurrentPaymentMethod();
		
		settingPage.setShenpayPayment(config.getString("account.invalid.CardNumber"), 
				config.getString("account.shenpay.bankName"), 
				config.getString("account.shenpay.bankUserName"),
				config.getString("account.shenpay.nid")
				);

		//assertTrue(settingPage.verifySetPayemntMethodSucessMsgPresent("您的付款方式已经成功更新好了。"));
		assertEquals(settingPage.getCurrentPaymentMethod(),currentPaymentMethod,"Current payment method as not expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void setCreditCardInvalidCard(WebDriver driver) throws Exception {
		ChinaAccountSettingPage settingPage = loginAndVerifyPhone(config.getString("cn.existingUserEmail"),config.getString("cn.existingUserPassword"),config.getString("account.phoneNumber"),driver);

		String currentPaymentMethod = settingPage.getCurrentPaymentMethod();
		
		settingPage.setCreditCardPayment(config.getString("account.invalid.CardNumber"),
				config.getString("account.shenpay.bankName"),
				config.getString("account.shenpay.bankUserName"));
		
		//assertTrue(settingPage.verifySetPayemntMethodSucessMsgPresent("您的付款方式已经成功更新好了。"));
		assertEquals(settingPage.getCurrentPaymentMethod(),currentPaymentMethod,"Current payment method as not expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void testSetCharityMethod(WebDriver driver) throws Exception{
		ChinaAccountSettingPage settingPage = loginAndVerifyPhone(config.getString("cn.existingUserEmail"),config.getString("cn.existingUserPassword"),config.getString("account.phoneNumber"),driver);
		settingPage.setCharityPayment();
		
		//assertTrue(settingPage.verifySetPayemntMethodSucessMsgPresent("您的付款方式已经成功更新好了。"));
		assertEquals(settingPage.getCurrentPaymentMethod(),"charity","Current payment method as not expected");
	}
	
	@Test(dataProvider = "getWebDriver",dependsOnMethods="testBindPhone")
	public void testChangePassWord(WebDriver driver) throws Exception{
		ChinaAccountSettingPage settingPage = loginAndVerifyPhone(memberEmail,config.getString("defaultPassword"),
				phoneNumber.substring(0, 7),driver);
		
		settingPage.changePassWord(config.getString("defaultPassword"), config.getString("cn.newPassword"), config.getString("cn.newPassword"));
		
		//log out
		ChinaHomePageBeforeLogin homePageBeforeLogin = logout(driver);
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		homePageBeforeLogin.goToURL(loginPage.getUrl(serverName));
		
		ChinaHomePageAfterLogin homePageAfterLogin = loginPage.login(memberEmail, config.getString("cn.newPassword"));
		//ChinaHomePageAfterLogin homePageAfterLogin = homePageBeforeLogin.login(memberEmail,config.getString("cn.newPassword"));

		assertTrue(homePageAfterLogin.verifyLoggedInElementPresent(memberEmail), "Logged in email not found on page");

	}
	
	//@Test(dataProvider = "getWebDriver")
	public void testSetLocale(WebDriver driver) throws Exception{				
		ChinaAccountSettingPage accountPage = signUpAndEnterAccountPage(getRandomEmail(),config.getString("defaultPassword"),driver);
		
		//set locale to en_cn
		AccountSettingsPage accountPageEn = accountPage.setLocalToEnCn();
		assertEquals(accountPageEn.getSelectedLocale(),"en_CN","selected locale didn't as expect");
		
		//set locale to zh_cn
		ChinaAccountSettingPage accountPageZh = accountPageEn.setLocalToZhCn();
		assertEquals(accountPageZh.getSelectedLocale(),"zh_CN","selected locale didn't as expect");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void testBindPhone(WebDriver driver) throws Exception{
		memberEmail = getRandomEmail();

		ChinaAccountSettingPage accountPage = signUpAndEnterAccountPage(memberEmail,config.getString("defaultPassword"),driver);
		
		phoneNumber = getRandomPhoneNumber();
		accountPage.bindMobile(phoneNumber, config.getString("account.sms"));
		
		assertEquals(accountPage.getLastFourPhoneNumber(),phoneNumber.substring(7),"last four number as not expected");
	}
	
	public ChinaAccountSettingPage signUpAndEnterAccountPage(String userName,String passWord,WebDriver driver) throws Exception{
		ChinaHomePageAfterLogin homePage = signUp(userName, passWord, driver);
		
		return homePage.hoverAndClickAccountSetting();
	}
	

}
