package com.extrabux.tests.cn;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import static org.testng.Assert.assertEquals;


//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;

import com.extrabux.pages.cn.ChinaLoginPage;
import com.extrabux.pages.cn.migrate.ChinaMergePage;
import com.extrabux.pages.cn.migrate.ChinaMigratePage;
import com.extrabux.util.DBUtil;

public class ChinaUserMergeTests extends ChinaBaseTest {

	private DBUtil db;

	@BeforeClass
	public void deleteAllConnects() throws Exception{
		 db = getDBUtil();
		 
		 db.deleteUserConnectsByEmail(config.getString("cn.ebates.existingUserEmail"));
		 db.deleteUserByEmail(config.getString("cn.ebates.existingUserEmail"));
		 db.deleteUserConnectsByEmail(config.getString("cn.ebates.merge.existingUserEmail"));
		// reset cache
	}
	
	@Test(dataProvider = "getWebDriver")
	public void testUpgradeToExtrabux(WebDriver driver) throws Exception {
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));
		
		ChinaMigratePage migratePage = loginPage.loginWithEbates(config.getString("cn.ebates.existingUserEmail"), config.getString("cn.ebates.existingUserPassword"));
		
		migratePage.upgradeToExtrabuxUser();
		
		assertEquals(migratePage.getMessage(),"账号升级成功！","user upgrade failed");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void testMergeWithExtrabux(WebDriver driver) throws Exception {
		ChinaLoginPage loginPage = new ChinaLoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));
		
		ChinaMigratePage migratePage = loginPage.loginWithEbates(config.getString("cn.ebates.merge.existingUserEmail"), config.getString("cn.ebates.existingUserPassword"));

		ChinaMergePage mergePage = migratePage.goToMergePage();
		
		mergePage.mergeEbWithExUser(config.getString("cn.ebates.merge.existingUserEmail"), config.getString("cn.existingUserPassword"), config.getString("cn.ebates.existingUserPassword"));
		
		mergePage.verifyMobile(config.getString("cn.extrabuxUser.mobileNumber"));
		
		assertEquals(mergePage.getMessage(),"账号绑定成功！","user upgrade failed");

	}
	
}
