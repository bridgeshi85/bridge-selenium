package com.extrabux.tests.cn.mvp;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.DecimalFormat;
import java.util.Locale;

//import java.text.DecimalFormat;
//import java.util.Locale;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.cn.ChinaHomePageAfterLogin;
import com.extrabux.pages.cn.ChinaHomePageBeforeLogin;
import com.extrabux.pages.cn.ChinaLoginPage;
import com.extrabux.pages.cn.account.ChinaMyEarningsPage;
import com.extrabux.pages.cn.mvp.MvpDaigouHomePageAfterLogin;
import com.extrabux.pages.cn.mvp.MvpDaigouHomePageBeforeLogin;

public class SignUpAndSignInTests extends MvpDaigouBaseTest{
	
	@Test(dataProvider = "getWebDriver",groups="production-test")
	public void loginFromMvp(WebDriver driver) throws Exception{
		MvpDaigouHomePageAfterLogin homePage = loginToMvpDaigou(config.getString("cn.existingUserEmail"),config.getString("cn.existingUserPassword"),driver);
		 
		//enter extrabux
		ChinaHomePageAfterLogin extrabuHomePage =homePage.enterExtrabux();
		
		//verify extrabux logged in
		assertTrue(extrabuHomePage.verifyLoggedInElementPresent(config.getString("cn.existingUserEmail")), "Logged in email not found on page");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void loginFromExtrabux(WebDriver driver) throws Exception{
		MvpDaigouHomePageBeforeLogin homePageBeforeLogin = new MvpDaigouHomePageBeforeLogin(driver);
		homePageBeforeLogin.goToURL("https://" + serverName);
		
		ChinaHomePageBeforeLogin extrabuxHomePage = homePageBeforeLogin.enterExtrabuxHomePageBeforeLogin();
		
		ChinaLoginPage loginPage = extrabuxHomePage.clickLogin();
		
		ChinaHomePageAfterLogin homePageAfterLogin = loginPage.login(config.getString("cn.existingUserEmail"),config.getString("cn.existingUserPassword"));
		
		MvpDaigouHomePageAfterLogin haitaoHomePage = homePageAfterLogin.enterHaitao();
		
		assertTrue(haitaoHomePage.verifyLoggedInElementPresent(config.getString("cn.existingUserEmail")), "Logged in email not found on page");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void logOut(WebDriver driver) throws Exception{

		MvpDaigouHomePageAfterLogin homePage = loginToMvpDaigou(config.getString("cn.existingUserEmail"),config.getString("cn.existingUserPassword"),driver);

		MvpDaigouHomePageBeforeLogin homePageBeforeLogin = homePage.logOut();
		
		assertTrue(homePageBeforeLogin.verifyNotLoggedIn());
	}
	
	@Test(dataProvider = "getWebDriver")
	public void logOutFromExtrabux(WebDriver driver) throws Exception{

		MvpDaigouHomePageAfterLogin homePage = loginToMvpDaigou(config.getString("cn.existingUserEmail"),config.getString("cn.existingUserPassword"),driver);
		 
		//enter extrabux
		ChinaHomePageAfterLogin extrabuHomePage = homePage.enterExtrabux();
		
		//extrabuHomePage.closeFancyBox();
		
		ChinaHomePageBeforeLogin extrabuxHomePageBeforeLogin = extrabuHomePage.hoverAndClickLogout();
		
		MvpDaigouHomePageBeforeLogin haitaoHomePage = extrabuxHomePageBeforeLogin.enterHaitao();
		
		assertTrue(haitaoHomePage.verifyNotLoggedIn());
	}
	
	@Test(dataProvider = "getWebDriver",groups="production-test")
	public void signUpFromMvp(WebDriver driver) throws Exception{
		MvpDaigouHomePageAfterLogin homePage = signUpFromMvpDaigou(getRandomEmail(),
				config.getString("cn.existingUserPassword"),config.getString("cn.existingUserPassword"),driver);
		
		ChinaMyEarningsPage myEarningsPage = homePage.goToMyAccount();
		
		// verify the sign up bonus
		String expected = DecimalFormat.getCurrencyInstance(Locale.US).format(config.getDouble("signup.bonusAmount"));
		assertEquals(myEarningsPage.getTotalCashBack(), expected, "Earnings after signup not as expected");
	}
	
}
