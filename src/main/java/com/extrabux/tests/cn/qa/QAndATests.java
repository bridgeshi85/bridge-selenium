package com.extrabux.tests.cn.qa;

import static org.testng.Assert.assertEquals;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.cn.ChinaLoginPage;
import com.extrabux.pages.cn.ChinaSignUpPage;
import com.extrabux.pages.cn.qa.BindUserPage;
import com.extrabux.pages.cn.qa.PublishQueryPage;
import com.extrabux.pages.cn.qa.QAHomePageAfterLogin;
import com.extrabux.pages.cn.qa.QAHomePageBeforeLogin;
import com.extrabux.pages.cn.qa.QuestionDetailPage;
import com.extrabux.pages.cn.qa.SearchResultPage;
import com.extrabux.tests.cn.ChinaBaseTest;

public class QAndATests extends ChinaBaseTest{
	
	private static final Log LOG = LogFactory.getLog(QAndATests.class);
	
	@Test(dataProvider = "getWebDriver")
	public void testLoginFromQA(WebDriver driver) throws Exception{
		
		//open wenda
		QAHomePageBeforeLogin homePage = new QAHomePageBeforeLogin(driver);
		homePage.goToURL("http://"+serverName);
		
		ChinaLoginPage loginPage = homePage.clickLoginButton();
		
		String email = config.getString("cn.existingUserEmail");
		
		//login
		QAHomePageAfterLogin homePageAfterLogin = loginPage.loginFromQA(email, config.getString("cn.existingUserPassword"));
		
		//verfiy
		homePageAfterLogin.verifyUserName(email.substring(0, email.indexOf("@")));
	}
	
	@Test(dataProvider = "getWebDriver")
	public void testSignUpFromQA(WebDriver driver) throws Exception{
		
		QAHomePageBeforeLogin homePage = new QAHomePageBeforeLogin(driver);
		homePage.goToURL("http://"+serverName);
		
		ChinaSignUpPage signUpPage = homePage.clickSignUpButton();
	
		String email = getRandomEmail();
		
		//signup
		BindUserPage bindPage = signUpPage.signUpFromQA(email, config.getString("defaultPassword"), config.getString("defaultPassword"));
		
		//bind user
		QAHomePageAfterLogin homePageAfterLogin = bindPage.submit();
		
		//verify user name
		homePageAfterLogin.verifyUserName(email.substring(0, email.indexOf("@")));
	}
	
	@Test(dataProvider = "getWebDriver")
	public void testSearchQuestion(WebDriver driver) throws Exception{
		
		QAHomePageBeforeLogin homePage = new QAHomePageBeforeLogin(driver);
		homePage.goToURL("http://"+serverName);
		
		SearchResultPage resultPage = homePage.searchQuestion(config.getString("qa.searchKeyWord"));
		resultPage.verifySearchResult("drugstore");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void testAddQuery(WebDriver driver) throws Exception{
		QAHomePageAfterLogin homePage = LoginFromQA(config.getString("cn.existingUserEmail"),config.getString("cn.existingUserPassword"),driver);
		
		PublishQueryPage publishPage = new PublishQueryPage(driver);
		homePage.goToURL(publishPage.getUrl(serverName));
		
		QuestionDetailPage queryPage = publishPage.addQuestion(config.getString("qa.question.title"), 
				config.getString("qa.question.content"), config.getString("qa.question.tag"));
		queryPage.verifyTitle(config.getString("qa.question.title"));
	}
	
	@Test(dataProvider = "getWebDriver")
	public void testReplyQuery(WebDriver driver) throws Exception{
		//login
		QAHomePageAfterLogin homePage = LoginFromQA(config.getString("cn.existingUserEmail"),
				config.getString("cn.existingUserPassword"),driver);
		
		QuestionDetailPage queryPage = new QuestionDetailPage(driver);
		homePage.goToURL(queryPage.getUrl(serverName, 10));
		
		int replyCount = queryPage.getReplyCount();
		
		queryPage.replyQuery("content");
		
		int replyCountAfter = queryPage.getReplyCount();
		
		LOG.debug("reply count :" +replyCountAfter);
		
		assertEquals(replyCountAfter,replyCount+1,"reply count not as expected");
	}
}
