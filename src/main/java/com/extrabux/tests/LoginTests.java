package com.extrabux.tests;

import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.HomePageAfterLogin;
import com.extrabux.pages.LoginPage;
import com.extrabux.pages.account.AccountPage;

public class LoginTests extends BaseTest {
	private static final Log LOG = LogFactory.getLog(LoginTests.class);

	@Test(dataProvider = "getWebDriver",groups = "production-test")
	public void loginSuccess(WebDriver driver) throws Exception {
		try {
			LoginPage loginPage = new LoginPage(driver);
			loginPage.goToURL(loginPage.getUrl(serverName));

			HomePageAfterLogin homePage = loginPage.login(config.getString("existingUserEmail"), config.getString("existingUserPassword"));

			LOG.info("verifying login was successful - verifying username shows on home page.");
			assertTrue(homePage.verifyLoggedInElementPresent(config.getString("existingUserEmail")), "Logged in email on page not as expected");
		} catch (Exception e) {
			throw new Exception("currentPage title: " + driver.getTitle() + "\n currentPage url: " + driver.getCurrentUrl() + "\n"+ e.getMessage());
		}
	}

	//@Test(dataProvider = "getWebDriver")
	public void loginEmptyForm(WebDriver driver) {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		loginPage.login("", "");

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("email", config.getString("login.error.emailRequired"));
		expectedErrors.put("password", config.getString("login.error.passwordRequired"));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	}

	//@Test(dataProvider = "getWebDriver")
	public void loginNoEmail(WebDriver driver) {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		loginPage.login("", config.getString("defaultPassword"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("email", config.getString("login.error.emailRequired"));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	}

	//@Test(dataProvider = "getWebDriver")
	public void loginBadEmailFormat(WebDriver driver) {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		loginPage.loginNoSubmit(config.getString("login.invalidEmail"), config.getString("defaultPassword"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("email", config.getString("login.error.emailInvaid"));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());

		// and clicking the submit button shouldn't change anything
		loginPage.submit();

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	}

	//@Test(dataProvider = "getWebDriver")
	public void loginNonExistentAccount(WebDriver driver) {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		String email = config.getString("login.nonExistentEmail");
		loginPage.login(email, config.getString("defaultPassword"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("email", String.format(config.getString("login.error.emailDoesntExist"), email));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	}

	//@Test(dataProvider = "getWebDriver")
	public void loginNoPassword(WebDriver driver) {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		loginPage.login(config.getString("existingUserEmail"), "");

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("password", config.getString("login.error.passwordRequired"));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	}

	//@Test(dataProvider = "getWebDriver")
	public void loginIncorrectPassword(WebDriver driver) {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		loginPage.login(config.getString("existingUserEmail"), config.getString("login.badPassword"));

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("form", config.getString("login.error.incorrectCreds"));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	}

	//@Test(dataProvider = "getWebDriver")
	public void attemptAccessSecurePageNotLoggedIn(WebDriver driver) {
		AccountPage accountPage = new AccountPage(driver);
		accountPage.goToURL(accountPage.getUrl(serverName));
		LoginPage loginPage = new LoginPage(driver);

		// TODO find a better way to build the expected errors
		Map<String, String> expectedErrors = new HashMap<String, String>();
		expectedErrors.put("form", config.getString("login.error.security"));

		assertTrue(loginPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + loginPage.getErrors());
	}
}
