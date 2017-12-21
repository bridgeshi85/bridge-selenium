package com.extrabux.tests.cn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

//import com.extrabux.pages.SignUpPage;
import com.extrabux.pages.StoreTransferPage;
//import com.extrabux.pages.account.InviteFriendsPage;
import com.extrabux.pages.account.InviteFriendsPage.Friend;
import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
//import com.extrabux.pages.cn.ChinaHomePageBeforeLogin;
//import com.extrabux.pages.cn.ChinaLoginPage;
import com.extrabux.pages.cn.ChinaMerchantPage;
import com.extrabux.pages.cn.ChinaNewSignUpPage;
import com.extrabux.pages.cn.ChinaSignUpPage;
import com.extrabux.pages.cn.account.ChinaInviteFriendsPage;
import com.extrabux.util.DBUtil;

public class ChinaSignUpTests extends ChinaBaseTest {
	private static final Log LOG = LogFactory.getLog(ChinaSignUpTests.class);

	String existingUser;
	String friend;
	String referralLink;
	
	List<Friend> expectedFriends = new ArrayList<Friend>();
	private DBUtil db;
	
	@BeforeClass
	public void setDBUtil() {
		db = new DBUtil(config);
	}
	
	@Test(dataProvider = "getWebDriver",groups="production-test")
	public void signUpSuccessNoReferral(WebDriver driver) throws Exception {
		try {			
			ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
			signUpPage.goToURL(signUpPage.getUrl(serverName));

			String email = getRandomEmail();
			existingUser = email;

			ChinaHomePageAfterLogin homePage = signUpPage.signUp(email, config.getString("defaultPassword"),
					config.getString("defaultPassword"),config.getString("cn.signup.captcha"));
			
			//close fancy box
			//homePage.closeFancyBox();
			
			assertTrue(homePage.verifyLoggedInElementPresent(email), "Logged in email not as expected.");

			// verify the sign up bonus
			String expected = DecimalFormat.getCurrencyInstance(Locale.US).format(config.getDouble("signup.bonusAmount"));
			assertEquals(homePage.getLifetimeEarnings(), expected, "Earnings after signup not as expected");
			
			//verify the sign up bonus in homepage
			//assertEquals(homePage.getLifetimeEarningsFromSide(), expected, "Earnings after signup not as expected");

			// get referral link
			ChinaInviteFriendsPage invitesPage = new ChinaInviteFriendsPage(driver);
			invitesPage.goToURL(invitesPage.getUrl(serverName));
			referralLink = invitesPage.getReferralLink();

			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("currentPage title: " + driver.getTitle() + "\n currentPage url: " + driver.getCurrentUrl() + "\n"+ e.getMessage());
		}
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void newSignUpSuccessNoReferral(WebDriver driver) throws Exception {
		try {
			ChinaNewSignUpPage signUpPage = new ChinaNewSignUpPage(driver);
			signUpPage.goToURL(signUpPage.getUrl(serverName));

			String email = getRandomEmail();

			ChinaHomePageAfterLogin homePage = signUpPage.signUp(email, config.getString("defaultPassword"),
					config.getString("defaultPassword"),config.getString("cn.signup.captcha"));
			
			//close fancy box
			//homePage.closeFancyBox();
			
			assertTrue(homePage.verifyLoggedInElementPresent(email), "Logged in email not as expected.");

			// verify the sign up bonus
			String expected = DecimalFormat.getCurrencyInstance().format(config.getDouble("signup.bonusAmount"));
			assertEquals(homePage.getLifetimeEarnings(), expected, "Earnings after signup not as expected");
		} catch (Exception e) {
			throw new Exception("currentPage title: " + driver.getTitle() + "\n currentPage url: " + driver.getCurrentUrl() + "\n"+ e.getMessage());
		}
	}
	
	
	
	@Test(dataProvider = "getWebDriver")
	public void signUpEmptyForm(WebDriver driver) {
			ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
			signUpPage.goToURL(signUpPage.getUrl(serverName));

			signUpPage.signUp("", "", "");

			// TODO find a better way to build the expected errors
			Map<String, String> expectedErrors = new HashMap<String, String>();
			expectedErrors.put("email", config.getString("cn.signup.error.emailRequired"));
			expectedErrors.put("password", config.getString("cn.signup.error.passwordRequired"));
			expectedErrors.put("password_confirm", config.getString("cn.signup.error.passwordConfirmRequired"));

			assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
					+ " actual: " + signUpPage.getErrors());
		}
		
		@Test(dataProvider = "getWebDriver")
		public void signUpEmptyEmail(WebDriver driver) {
			ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
			signUpPage.goToURL(signUpPage.getUrl(serverName));

			String defaultPassword = config.getString("defaultPassword");

			signUpPage.signUp("", defaultPassword, defaultPassword,config.getString("cn.signup.captcha"));

			// TODO find a better way to build the expected errors
			Map<String, String> expectedErrors = new HashMap<String, String>();
			expectedErrors.put("email", config.getString("cn.signup.error.emailRequired"));

			assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
					+ " actual: " + signUpPage.getErrors());
		}

	@Test(dataProvider = "getWebDriver")
	public void signUpInvalidEmail(WebDriver driver) {
			ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
			signUpPage.goToURL(signUpPage.getUrl(serverName));

			String invalidEmail = config.getString("login.invalidEmail");
			String defaultPassword = config.getString("defaultPassword");

			signUpPage.signUp(invalidEmail, defaultPassword, defaultPassword,config.getString("cn.signup.captcha"));

			// TODO find a better way to build the expected errors
			Map<String, String> expectedErrors = new HashMap<String, String>();
			expectedErrors.put("email", config.getString("cn.signup.error.invalidEmail"));

			assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
					+ " actual: " + signUpPage.getErrors());
		}

	@Test(dataProvider = "getWebDriver")
	public void signUpPasswordTooShort(WebDriver driver) {
		ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		signUpPage.signUp(email, config.getString("passwordTooShort"), config.getString("passwordTooShort"),config.getString("cn.signup.captcha"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("password", config.getString("cn.signup.error.passwordTooShort"));
		expectedErrors.put("password_confirm", config.getString("cn.signup.error.passwordTooShort"));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
					+ " actual: " + signUpPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void signUpPasswordsDontMatch(WebDriver driver) {
			ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
			signUpPage.goToURL(signUpPage.getUrl(serverName));

			String email = getRandomEmail();

			signUpPage.signUp(email, config.getString("defaultPassword"), config.getString("defaultPassword") + "x",config.getString("cn.signup.captcha"));

			// TODO find a better way to build the expected errors
			Map<String, String> expectedErrors = new HashMap<String, String>();
			expectedErrors.put("password_confirm", config.getString("cn.signup.error.passwordsDontMatch"));

			assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
					+ " actual: " + signUpPage.getErrors());
		}

	@Test(dataProvider = "getWebDriver")
	public void signUpNoPassword(WebDriver driver) {
		ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		signUpPage.signUp(email, "", config.getString("defaultPassword"),config.getString("cn.signup.captcha"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("password", config.getString("cn.signup.error.passwordRequired"));
		expectedErrors.put("password_confirm", config.getString("cn.signup.error.passwordsDontMatch"));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + signUpPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void signUpNoConfirmPassword(WebDriver driver) {
		ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		signUpPage.signUp(email, config.getString("defaultPassword"), "",config.getString("cn.signup.captcha"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("password_confirm", config.getString("cn.signup.error.passwordConfirmRequired"));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + signUpPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver", dependsOnMethods = "signUpSuccessNoReferral")
	public void signUpSuccessWithReferralEmail(WebDriver driver) throws Exception {
		ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();
		
		ChinaHomePageAfterLogin homePage = signUpPage.signUpWithRefferal(email, config.getString("defaultPassword"),
				config.getString("defaultPassword"), existingUser,config.getString("cn.signup.captcha"));

		assertTrue(homePage.verifyLoggedInElementPresent(email), "Logged in email not as expected.");
		assertEquals(db.getParentMemberIdByEmail(email),db.getMemberIdByEmail(existingUser),"referral user parent id not as expected");

		// logout of new account 
		/*ChinaHomePageBeforeLogin loggedOutHome = homePage.hoverAndClickLogout();
		assertTrue(loggedOutHome.verifyNotLoggedIn(), "expected new user to be logged out");

		//reset cache
		resetLocalCache(config.getString("cn.adminUserEmail"),config.getString("cn.adminUserPassword"));
		
		//log back in as the referrer
		ChinaLoginPage loginPage = loggedOutHome.clickLogin();
		homePage = loginPage.login(existingUser, config.getString("defaultPassword"));

		//Go to friendsPage
		ChinaInviteFriendsPage invitesPage = new ChinaInviteFriendsPage(driver);
		invitesPage.goToURL(invitesPage.getUrl(serverName));
		
		//get referral list
		List<Friend> friends = invitesPage.getFriendsList();

		//get expect referral list
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		expectedFriends.add(new InviteFriendsPage(driver).new Friend(email, dateFormat.format(syncToGmt())));

		assertTrue(invitesPage.verifyFriendList(friends, expectedFriends), "friends list not as expected.");

		//verify the sign up bonus + friend bonus
		//String expected = DecimalFormat.getCurrencyInstance().format(config.getDouble("signup.bonusAmount") + config.getDouble("signup.friendBonusAmount")*expectedFriends.size());
		//assertEquals(homePage.getLifetimeEarnings(), expected, "Earnings after signup not as expected");*/
	}
	
	@Test(dataProvider = "getWebDriver", dependsOnMethods = "signUpSuccessNoReferral")
	public void signUpSuccessWithReferralCode(WebDriver driver) throws Exception{
		ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();
		
		//get referral code
		String referralCode = getReferralCode(referralLink);
		
		ChinaHomePageAfterLogin homePage = signUpPage.signUpWithRefferal(email, config.getString("defaultPassword"),
				config.getString("defaultPassword"), referralCode,config.getString("cn.signup.captcha"));

		assertTrue(homePage.verifyLoggedInElementPresent(email), "Logged in email not as expected.");
		assertEquals(db.getParentMemberIdByEmail(email),db.getMemberIdByEmail(existingUser),"referral user parent id not as expected");

	}
	
	@Test(dataProvider = "getWebDriver")
	public void signUpUserAlreadyExists(WebDriver driver) {
		ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = config.getString("cn.existingUserEmail");
		String existingPassword = config.getString("existingUserPassword");

		signUpPage.signUp(email, existingPassword, existingPassword,config.getString("cn.signup.captcha"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("form", String.format(config.getString("cn.signup.error.userExists"), email));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + signUpPage.getErrors());
	}
	
	@Test(dataProvider = "getWebDriver", dependsOnMethods = "signUpSuccessNoReferral")
	public void signUpSuccessUsingReferralLink(WebDriver driver) throws Exception {
		

		String email = getRandomEmail();
		friend = email;

		ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
		signUpPage.goToURL(referralLink);
		ChinaHomePageAfterLogin homePage = signUpPage.signUp(email, config.getString("defaultPassword"),
				config.getString("defaultPassword"),config.getString("cn.signup.captcha"));

		assertTrue(homePage.verifyLoggedInElementPresent(email), "Logged in email not as expected.");
		
		assertEquals(db.getParentMemberIdByEmail(email),db.getMemberIdByEmail(existingUser),"referral user parent id not as expected");
		// logout of new account and log back in as the referrer
		/*ChinaHomePageBeforeLogin loggedOutHome = logout(driver);
		
		//reset cache
		resetLocalCache(config.getString("cn.adminUserEmail"),config.getString("cn.adminUserPassword"));
		
		ChinaLoginPage loginPage = loggedOutHome.clickLogin();
		homePage = loginPage.login(existingUser, config.getString("defaultPassword"));

		//Go to friendsPage
		ChinaInviteFriendsPage invitesPage = new ChinaInviteFriendsPage(driver);
		invitesPage.goToURL(invitesPage.getUrl(serverName));
		
		//get referral list
		List<Friend> friends = invitesPage.getFriendsList();

		//get expect referral list
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		expectedFriends.add(new InviteFriendsPage(driver).new Friend(email, dateFormat.format(syncToGmt())));

		assertTrue(invitesPage.verifyFriendList(friends, expectedFriends), "friends list not as expected.");

		// verify the sign up bonus + friend bonus
		//String expected = DecimalFormat.getCurrencyInstance().format(config.getDouble("signup.bonusAmount") + config.getDouble("signup.friendBonusAmount")*expectedFriends.size());
		
		//assertEquals(homePage.getLifetimeEarnings(), expected, "Earnings after signup not as expected");*/
	}
	
	@Test(dataProvider = "getWebDriver")
	public void signUpSuccessWithBonusCode(WebDriver driver) throws Exception {
		ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		ChinaHomePageAfterLogin homePage = signUpPage.signUpWithRefferal(email, config.getString("defaultPassword"),
				config.getString("defaultPassword"), config.getString("visa.signUpCode"),config.getString("cn.signup.captcha"));

		//close fancy box
		//homePage.closeFancyBox();
		
		assertTrue(homePage.verifyLoggedInElementPresent(email), "Logged in email not as expected.");
		// verify the sign up bonus
		String expected = DecimalFormat.getCurrencyInstance().format(config.getDouble("visa.signup.bonusCodeAmount"));
		assertEquals(homePage.getLifetimeEarnings(), expected, "Earnings after signup not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void signUpPasswordTooLong(WebDriver driver) {
		ChinaSignUpPage signUpPage = new ChinaSignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		signUpPage.signUp(email, config.getString("passwordTooLong"), config.getString("passwordTooLong"),config.getString("cn.signup.captcha"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("password", config.getString("cn.signup.error.passwordTooLong"));
		expectedErrors.put("password_confirm", config.getString("cn.signup.error.passwordTooLong"));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
					+ " actual: " + signUpPage.getErrors());
	}
	
	@Test(dataProvider = "getWebDriver")
	public void signUpFromPopup(WebDriver driver) throws Exception{
		ChinaMerchantPage storePage = new ChinaMerchantPage(driver);
		storePage.goToURL(storePage.getUrl(serverName, config.getString("store.uri")));
		
		String currentUrl = driver.getCurrentUrl();
		LOG.debug("currentUrl : " + currentUrl);
		LOG.debug(config.getString("store.uri"));
		assertTrue(currentUrl.contains(config.getString("store.uri")), "expected to be on store detail page after signup and store search but are on " + driver.getTitle() + " (" + driver.getCurrentUrl() + ").  cannot click to transfer to store.  something may have gone wrong with store search.");
		
		String email = getRandomEmail();

		storePage.clickStartShoppingWithoutLogin();
		StoreTransferPage transferPage = storePage.signUpFromPopup(email, config.getString("cn.existingUserPassword"),config.getString("cn.signup.captcha"));
		
		LOG.debug("page url after clicking start shopping " + driver.getCurrentUrl());
		transferToMerchant(transferPage,driver,config.getString("store.url"));

		// need to switch to the transfer window and verify
		/*String parentWindow = switchWindows(driver);
		LOG.debug("page url after switching windows" + driver.getCurrentUrl());
		//assertTrue(transferPage.stopInterstitialLoadVerifyOnTransferPageThenTransfer(), "not on store transfer page as expected");
		// need to check they were actually forwarded to the merchant page
		try {
			transferPage.waitForTransfer();
		} catch (TimeoutException te) {
			LOG.debug("in catch block after timing out waiting for store transfer");
			assertTrue(transferPage.verifyOnMerchantSite(config.getString("store.url")), "expected to be on merchant site but are on " + driver.getTitle() + " : " + driver.getCurrentUrl());
		}
		assertTrue(transferPage.verifyOnMerchantSite(config.getString("store.url")), "expected to be on merchant site but are on " + driver.getTitle() + " : " + driver.getCurrentUrl());
		driver.close();
		switchBackToParentWindow(driver, parentWindow);*/
		
		//verify bonus
		ChinaHomePageAfterLogin homePageAfter = new ChinaHomePageAfterLogin(driver);
		String expected = DecimalFormat.getCurrencyInstance().format(config.getDouble("signup.bonusAmount"));
		assertEquals(homePageAfter.getLifetimeEarnings(), expected, "Earnings after signup not as expected");
		
		//assertTrue(storePage.verifyAddPurchaseLinkPresent(),"green bubble not display");
	}
	
	public Date syncToGmt(){
		Calendar calendar = Calendar.getInstance();    
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR, -7);
		return calendar.getTime();
	}
	
	public static String getReferralCode(String url){
	    Pattern p=Pattern.compile("(?<=r\\/)\\w+");
	    Matcher m=p.matcher(url);
	    String findString = "";
	    if(m.find()){
	    	findString = m.group(0);
	    }
		return findString;
	}
}
