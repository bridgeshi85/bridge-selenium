package com.extrabux.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.HomePageAfterLogin;
import com.extrabux.pages.HomePageBeforeLogin;
import com.extrabux.pages.LoginPage;
import com.extrabux.pages.SignUpPage;
import com.extrabux.pages.account.AccountPage;
import com.extrabux.pages.account.InviteFriendsPage;
import com.extrabux.pages.account.InviteFriendsPage.Friend;


public class SignUpTests extends BaseTest {
	private static final Log LOG = LogFactory.getLog(SignUpTests.class);

	String existingUser;
	String friend;

	//@Test(dataProvider = "getWebDriver")
	public void signUpEmptyForm(WebDriver driver) {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		signUpPage.signUp("", "", "");

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("email", config.getString("signup.error.emailRequired"));
		expectedErrors.put("password", config.getString("signup.error.passwordRequired"));
		expectedErrors.put("password_confirm", config.getString("signup.error.passwordConfirmRequired"));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + signUpPage.getErrors());
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void signUpEmptyEmail(WebDriver driver) {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String defaultPassword = config.getString("defaultPassword");

		signUpPage.signUp("", defaultPassword, defaultPassword);

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("email", config.getString("signup.error.emailRequired"));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + signUpPage.getErrors());
	}

	//@Test(dataProvider = "getWebDriver")
	public void signUpInvalidEmail(WebDriver driver) {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String invalidEmail = config.getString("login.invalidEmail");
		String defaultPassword = config.getString("defaultPassword");

		signUpPage.signUp(invalidEmail, defaultPassword, defaultPassword);

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("email", config.getString("signup.error.invalidEmail"));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + signUpPage.getErrors());
	}

	//@Test(dataProvider = "getWebDriver")
	public void signUpNoPasswords(WebDriver driver) {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		signUpPage.signUp(email, "", "");

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("password", config.getString("signup.error.passwordRequired"));
		expectedErrors.put("password_confirm", config.getString("signup.error.passwordConfirmRequired"));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + signUpPage.getErrors());
	}

	//@Test(dataProvider = "getWebDriver")
	public void signUpPasswordTooShort(WebDriver driver) {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		signUpPage.signUp(email, config.getString("passwordTooShort"), config.getString("passwordTooShort"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("password", config.getString("signup.error.passwordTooShort"));
		expectedErrors.put("password_confirm", config.getString("signup.error.passwordTooShort"));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + signUpPage.getErrors());
	}

	//@Test(dataProvider = "getWebDriver")
	public void signUpPasswordsDontMatch(WebDriver driver) {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		signUpPage.signUp(email, config.getString("defaultPassword"), config.getString("defaultPassword") + "x");

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("password_confirm", config.getString("signup.error.passwordsDontMatch"));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + signUpPage.getErrors());
	}

	//@Test(dataProvider = "getWebDriver")
	public void signUpNoPassword(WebDriver driver) {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		signUpPage.signUp(email, "", config.getString("defaultPassword"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("password", config.getString("signup.error.passwordRequired"));
		expectedErrors.put("password_confirm", config.getString("signup.error.passwordsDontMatch"));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + signUpPage.getErrors());
	}

	//@Test(dataProvider = "getWebDriver")
	public void signUpNoConfirmPassword(WebDriver driver) {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		signUpPage.signUp(email, config.getString("defaultPassword"), "");

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("password_confirm", config.getString("signup.error.passwordConfirmRequired"));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + signUpPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver",groups="production-test")
	public void signUpSuccessNoReferral(WebDriver driver) throws Exception {
		try {
			SignUpPage signUpPage = new SignUpPage(driver);
			signUpPage.goToURL(signUpPage.getUrl(serverName));

			String email = getRandomEmail();
			existingUser = email;

			HomePageAfterLogin homePage = signUpPage.signUp(email, config.getString("defaultPassword"),
					config.getString("defaultPassword"));
			LOG.info("verifying signup was successful - verifying username shows on home page.");
			assertTrue(homePage.verifyLoggedInElementPresent(email), "Logged in email not as expected.");

			// verify the sign up bonus
			String expected = DecimalFormat.getCurrencyInstance(Locale.US).format(config.getDouble("signup.bonusAmount"));
			LOG.info("verifying signup bonus amount.");
			assertEquals(homePage.getLifetimeEarnings(), expected, "Earnings after signup not as expected");
		} catch (Exception e) {
			throw new Exception("currentPage title: " + driver.getTitle() + "\n currentPage url: " + driver.getCurrentUrl() + "\n"+ e.getMessage());
		}
	}

	//@Test(dataProvider = "getWebDriver", dependsOnMethods = "signUpSuccessUsingReferralLink")
	public void signUpSuccessWithReferralEmail(WebDriver driver) throws Exception {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		HomePageAfterLogin homePage = signUpPage.signUpWithRefferal(email, config.getString("defaultPassword"),
				config.getString("defaultPassword"), existingUser);

		assertTrue(homePage.verifyLoggedInElementPresent(email), "Logged in email not as expected.");
		// logout of new account and log back in as the referrer
		HomePageBeforeLogin loggedOutHome = homePage.hoverAndClickLogout();
		assertTrue(loggedOutHome.verifyNotLoggedIn(), "expected new user to be logged out");

		LoginPage loginPage = loggedOutHome.clickLogin();
		homePage = loginPage.login(existingUser, config.getString("defaultPassword"));

		AccountPage accountPage = homePage.hoverAndClickMyEarnings();
		accountPage.clickShowHideDetails();
		InviteFriendsPage invitesPage = accountPage.clickSeeFriendsLink();
		List<Friend> friends = invitesPage.getFriendsList();

		List<Friend> expectedFriends = new ArrayList<Friend>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		expectedFriends.add(new InviteFriendsPage(driver).new Friend(friend, dateFormat.format(new Date())));
		expectedFriends.add(new InviteFriendsPage(driver).new Friend(email, dateFormat.format(new Date())));

		assertTrue(invitesPage.verifyFriendList(friends, expectedFriends), "friends list not as expected.");

		// verify the sign up bonus + friend bonus
		String expected = DecimalFormat.getCurrencyInstance().format(config.getDouble("signup.bonusAmount") + config.getDouble("signup.friendBonusAmount") * 2);
		assertEquals(homePage.getLifetimeEarnings(), expected, "Earnings after signup not as expected");
	}

	//@Test(dataProvider = "getWebDriver")
	// the bad referral will just be ignored
	public void signUpSuccessWithBadReferral(WebDriver driver) throws Exception {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		HomePageAfterLogin homePage = signUpPage.signUpWithRefferal(email, config.getString("defaultPassword"),
				config.getString("defaultPassword"), getRandomEmail());

		assertTrue(homePage.verifyLoggedInElementPresent(email), "Logged in email not as expected.");
	}

	//@Test(dataProvider = "getWebDriver", dependsOnMethods = "signUpSuccessNoReferral")
	public void signUpSuccessUsingReferralLink(WebDriver driver) throws Exception {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));
		HomePageAfterLogin homePage = loginPage.login(existingUser, config.getString("defaultPassword"));

		// get referral link
		InviteFriendsPage invitesPage = homePage.hoverAndClickInviteFriends();
		String referralLink = invitesPage.getReferralLink();

		HomePageBeforeLogin loggedOutHome = homePage.hoverAndClickLogout();
		assertTrue(loggedOutHome.verifyNotLoggedIn(), "expected new user to be logged out");

		loggedOutHome.goToURL(referralLink);

		String email = getRandomEmail();
		friend = email;

		SignUpPage signUpPage = new SignUpPage(driver);
		HomePageAfterLogin homePage2 = signUpPage.signUp(email, config.getString("defaultPassword"),
				config.getString("defaultPassword"));

		assertTrue(homePage2.verifyLoggedInElementPresent(email), "Logged in email not as expected.");
		// logout of new account and log back in as the referrer
		HomePageBeforeLogin loggedOutHome2 = homePage.hoverAndClickLogout();
		assertTrue(loggedOutHome2.verifyNotLoggedIn(), "expected new user to be logged out");

		LoginPage loginPage2 = loggedOutHome.clickLogin();
		homePage = loginPage2.login(existingUser, config.getString("defaultPassword"));

		AccountPage accountPage = homePage.hoverAndClickMyEarnings();
		accountPage.clickShowHideDetails();
		InviteFriendsPage invitesPage2 = accountPage.clickSeeFriendsLink();
		List<Friend> friends = invitesPage2.getFriendsList();

		List<Friend> expectedFriends = new ArrayList<Friend>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		expectedFriends.add(new InviteFriendsPage(driver).new Friend(email, dateFormat.format(new Date())));

		assertTrue(invitesPage.verifyFriendList(friends, expectedFriends), "friends list not as expected.");

		// verify the sign up bonus + friend bonus
		String expected = DecimalFormat.getCurrencyInstance().format(config.getDouble("signup.bonusAmount") + config.getDouble("signup.friendBonusAmount"));
		assertEquals(homePage.getLifetimeEarnings(), expected, "Earnings after signup not as expected");
	}

	//@Test(dataProvider = "getWebDriver")
	public void signUpSuccessWithBonusCode(WebDriver driver) throws Exception {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		HomePageAfterLogin homePage = signUpPage.signUpWithRefferal(email, config.getString("defaultPassword"),
				config.getString("defaultPassword"), config.getString("signup.bonusCode"));

		assertTrue(homePage.verifyLoggedInElementPresent(email), "Logged in email not as expected.");
		// verify the sign up bonus
		String expected = DecimalFormat.getCurrencyInstance().format(config.getDouble("signup.bonusCodeAmount"));
		assertEquals(homePage.getLifetimeEarnings(), expected, "Earnings after signup not as expected");
	}

	//@Test(dataProvider = "getWebDriver", dependsOnMethods = "signUpSuccessNoReferral")
	public void signUpUserAlreadyExists(WebDriver driver) {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String existingPassword = config.getString("existingUserPassword");

		signUpPage.signUp(existingUser, existingPassword, existingPassword);

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("email", String.format(config.getString("signup.error.userExists"), existingUser));

		assertTrue(signUpPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + signUpPage.getErrors());
	}

}
