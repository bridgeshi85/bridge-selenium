package com.extrabux.tests.cn;

import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.StoreTransferPage;
import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
import com.extrabux.pages.cn.ChinaLoginPage;
import com.extrabux.pages.cn.ChinaMerchantPage;
import com.extrabux.pages.cn.ChinaNewLoginPage;

import org.openqa.selenium.JavascriptExecutor;

public class ChinaLoginTests extends ChinaBaseTest {

	private static final Log LOG = LogFactory.getLog(ChinaLoginTests.class);
	
	@Test(dataProvider = "getWebDriver",groups = "production-test")
	public void loginSuccess(WebDriver driver) throws Exception {
		try {
			ChinaLoginPage loginPage = new ChinaLoginPage(driver);
			loginPage.goToURL(loginPage.getUrl(serverName));

			ChinaHomePageAfterLogin homePage = loginPage.login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"));

			assertTrue(homePage.verifyLoggedInElementPresent(config.getString("cn.existingUserEmail")), "Logged in email on page not as expected");
		} catch (Exception e) {
			throw new Exception("currentPage title: " + driver.getTitle() + "\n currentPage url: " + driver.getCurrentUrl() + "\n"+ e.getMessage());
		}
	}

	//@Test(dataProvider = "getWebDriver")
	public void newLoginSuccess(WebDriver driver) throws Exception
	{
		try {
			ChinaNewLoginPage newLoginPage = new ChinaNewLoginPage(driver);
			newLoginPage.goToURL(newLoginPage.getUrl(serverName));

			String userName = config.getString("cn.existingUserEmail");
			String password = config.getString("cn.existingUserPassword");
			
			ChinaHomePageAfterLogin homePage = newLoginPage.login(userName, password);

			assertTrue(homePage.verifyLoggedInElementPresent(userName), "Logged in email on page not as expected");
		} catch (Exception e) {
			throw new Exception("currentPage title: " + driver.getTitle() + "\n currentPage url: " + driver.getCurrentUrl() + "\n"+ e.getMessage());
		}
	}
	
	@Test(dataProvider = "getWebDriver")
	public void loginEmptyForm(WebDriver driver) {
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		loginPage.login("", "");

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("email", config.getString("cn.login.error.emailRequired"));
		expectedErrors.put("password", config.getString("cn.login.error.passwordRequired"));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	}
	
	@Test(dataProvider = "getWebDriver")
	public void loginNoEmail(WebDriver driver) {
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		loginPage.login("", config.getString("defaultPassword"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("email", config.getString("cn.login.error.emailRequired"));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	}
	
	@Test(dataProvider = "getWebDriver")
	public void loginNonExistentAccount(WebDriver driver) {
			ChinaLoginPage loginPage = new ChinaLoginPage(driver);
			loginPage.goToURL(loginPage.getUrl(serverName));

			String email = config.getString("login.nonExistentEmail");
			loginPage.login(email, config.getString("defaultPassword"));

			// TODO find a better way to build the expected errors
			Map<String, String> expectedErrors = new HashMap<String, String>();
			expectedErrors.put("form", String.format(config.getString("cn.login.error.incorrectCreds"), email));

			assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
					+ " actual: " + loginPage.getErrors());
		}
	
	@Test(dataProvider = "getWebDriver")
	public void loginFromPopup(WebDriver driver){
		ChinaMerchantPage storePage = new ChinaMerchantPage(driver);
		storePage.goToURL(storePage.getUrl(serverName, config.getString("store.uri")));
		
		String currentUrl = driver.getCurrentUrl();
		LOG.debug("currentUrl : " + currentUrl);
		LOG.debug(config.getString("store.uri"));
		assertTrue(currentUrl.contains(config.getString("store.uri")), "expected to be on store detail page after signup and store search but are on " + driver.getTitle() + " (" + driver.getCurrentUrl() + ").  cannot click to transfer to store.  something may have gone wrong with store search.");
		
		storePage.clickStartShoppingWithoutLogin();
		StoreTransferPage transferPage = storePage.loginFromPopup(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"),config.getString("cn.signup.captcha"));
		
		LOG.debug("page url after clicking start shopping " + driver.getCurrentUrl());
		
		// need to switch to the transfer window and verify
		String parentWindow = switchWindows(driver);
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
		switchBackToParentWindow(driver, parentWindow);
	}
	
	@Test(dataProvider = "getWebDriver")
	public void loginNoPassword(WebDriver driver) {
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		loginPage.login(config.getString("cn.existingUserEmail"), "");

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("password", config.getString("cn.login.error.passwordRequired"));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void loginIncorrectPassword(WebDriver driver) {
		
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		loginPage.login(config.getString("existingUserEmail"), config.getString("login.badPassword"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("form", config.getString("cn.login.error.incorrectCreds"));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	}
	
	@Test(dataProvider = "getWebDriver")
	public void loginBadEmailFormat(WebDriver driver) {
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		loginPage.loginNoSubmit(config.getString("login.invalidEmail"), config.getString("defaultPassword"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("email", config.getString("cn.login.invalidEmail"));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());

		// and clicking the submit button shouldn't change anything
		loginPage.submit();

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	}
	
	@Test(dataProvider = "getWebDriver")
	public void loginWithPhoneNumber(WebDriver driver) throws Exception{
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		ChinaHomePageAfterLogin homePage = loginPage.login(config.getString("cn.existingUserPhoneNumber"), config.getString("cn.existingUserPassword"));

		assertTrue(homePage.verifyLoggedInElementPresent(config.getString("cn.existingUserEmail")), "Logged in email on page not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void phoneQiuckLogin(WebDriver driver) throws Exception{
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		ChinaHomePageAfterLogin homePage = loginPage.phoneQiuckLogin(config.getString("cn.existingUserPhoneNumber"),config.getString("account.sms"));

		assertTrue(homePage.verifyLoggedInElementPresent(config.getString("cn.existingUserEmail")), "Logged in email on page not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void loginBadPhoneNumber(WebDriver driver) throws Exception{
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		loginPage.loginNoSubmit(config.getString("cn.login.invalidPhone"), config.getString("defaultPassword"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("email", config.getString("cn.login.invalidEmail"));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());

		// and clicking the submit button shouldn't change anything
		loginPage.submit();

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	
	}
	
	@Test(dataProvider = "getWebDriver")
	public void loginNonExistentPhone(WebDriver driver) throws Exception{
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		String email = config.getString("cn.login.nonExistentPhone");
		loginPage.login(email, config.getString("defaultPassword"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("form", String.format(config.getString("cn.login.error.incorrectCreds"), email));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());

	}
	
	@Test(dataProvider = "getWebDriver")
	public void phoneLoginIncorrectPassword(WebDriver driver) {
		
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		loginPage.login(config.getString("cn.existingUserPhoneNumber"), config.getString("login.badPassword"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("form", config.getString("cn.login.error.incorrectCreds"));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	}
	
	@Test(dataProvider = "getWebDriver")
	public void loginFromPopupWithPhone(WebDriver driver){
		ChinaMerchantPage storePage = new ChinaMerchantPage(driver);
		storePage.goToURL(storePage.getUrl(serverName, config.getString("store.uri")));
		
		String currentUrl = driver.getCurrentUrl();
		LOG.debug("currentUrl : " + currentUrl);
		LOG.debug(config.getString("store.uri"));
		assertTrue(currentUrl.contains(config.getString("store.uri")), "expected to be on store detail page after signup and store search but are on " + driver.getTitle() + " (" + driver.getCurrentUrl() + ").  cannot click to transfer to store.  something may have gone wrong with store search.");
		
		storePage.clickStartShoppingWithoutLogin();
		StoreTransferPage transferPage = storePage.loginFromPopup(config.getString("cn.existingUserPhoneNumber"), config.getString("cn.existingUserPassword"),config.getString("cn.signup.captcha"));
		
		LOG.debug("page url after clicking start shopping " + driver.getCurrentUrl());
		
		// need to switch to the transfer window and verify
		String parentWindow = switchWindows(driver);
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
		switchBackToParentWindow(driver, parentWindow);
	}
}
