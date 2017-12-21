package com.extrabux.tests.cn;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.cn.help.ChinaContactPage;
import com.extrabux.pages.cn.help.ChinaContactSuccessPage;
import com.extrabux.pages.cn.help.ChinaFaqPage;
import com.extrabux.pages.cn.help.ChinaHelpCenterHomePage;


public class ChinaHelpCenterTests extends ChinaBaseTest {
	
	@Test(dataProvider = "getWebDriver")
	public void searchFaq(WebDriver driver) throws Exception{
		ChinaHelpCenterHomePage homePage = new ChinaHelpCenterHomePage(driver);
		homePage.goToURL(homePage.getUrl(serverName));
		
		ChinaFaqPage faqPage = homePage.searchFaq("返利");
		
		assertTrue(faqPage.verifyQuestionPresent("返利"),"faq search failed");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void sendEmailToCustomerServer(WebDriver driver) throws Exception{
		login(config.getString("cn.existingUserEmail"), config.getString("cn.existingUserPassword"), driver);

		ChinaContactPage contactPage = new ChinaContactPage(driver);
		contactPage.goToURL(contactPage.getUrl(serverName));
		
		ChinaContactSuccessPage successPage = contactPage.sendEmail(config.getString("cn.help.name"), config.getString("cn.existingUserEmail"),
				config.getString("cn.help.message"));
		
		assertEquals(successPage.getPageTitle(),config.getString("cn.help.thanks"),"email send failed");
	}
}
