package com.extrabux.tests;

import static org.testng.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.HomePageAfterLogin;
import com.extrabux.pages.MerchantPage;
import com.extrabux.pages.StoreTransferPage;

public class TrackingTicketTests extends BaseTest {
	private static final Log LOG = LogFactory.getLog(TrackingTicketTests.class);

	@Test(dataProvider = "getWebDriver",groups="production-test")
	public void testStoreTransferClick(WebDriver driver) throws Exception {		
		try {
		HomePageAfterLogin homePage = login(config.getString("existingUserEmail"), config.getString("existingUserPassword"), driver);

		MerchantPage storePage = homePage.search(config.getString("store.searchString"));
		String currentUrl = driver.getCurrentUrl();
		LOG.debug("currentUrl : " + currentUrl);
		LOG.debug(config.getString("store.uri"));
		LOG.info("verifying current url contains merchant store after store search.");
		assertTrue(currentUrl.contains(config.getString("store.uri")), "expected to be on store detail page after signup and store search but are on " + driver.getTitle() + " (" + driver.getCurrentUrl() + ").  cannot click to transfer to store.  something may have gone wrong with store search.");
		StoreTransferPage transferPage = storePage.clickStartShopping();
		LOG.debug("page url after clicking start shopping" + driver.getCurrentUrl());
		
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
		LOG.info("verifying we landed on merchant site after transfer");
		assertTrue(transferPage.verifyOnMerchantSite(config.getString("store.url")), "expected to be on merchant site but are on " + driver.getTitle() + " : " + driver.getCurrentUrl());
		LOG.debug("closing current browser window.");
		driver.close();
		LOG.debug("switching back to parent window.");
		switchBackToParentWindow(driver, parentWindow);

		// remove db check for store click
		/*List<String> expectedTicketStoreNames = Arrays.asList(config.getStringArray("ticket.storeNames"));
		List<String> actualTicketStoreNames = getDBUtil().getMemberTicketStoreNamesByEmail(email);
		// if no stores are returned try again in case there is some delay
		if (actualTicketStoreNames.isEmpty()) {
			Thread.sleep(5000);
			actualTicketStoreNames = getDBUtil().getMemberTicketStoreNamesByEmail(email);
		}
		assertEquals(actualTicketStoreNames.size(), expectedTicketStoreNames.size(), "number of tickets not as expected.");
		assertEquals(actualTicketStoreNames, expectedTicketStoreNames, "ticket store names not as expected");*/
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("currentPage title: " + driver.getTitle() + "\n currentPage url: " + driver.getCurrentUrl() + "\n"+ e.getMessage());
		}
	}

	//move to BaseTest class
	/*protected String switchWindows(WebDriver driver) {
		String parentHandle = driver.getWindowHandle();
		Set<String> allHandles = driver.getWindowHandles();
		// so for now i am assuming there are two windows
		for(String handle : allHandles) {
			if(!handle.equals(parentHandle)) {
				driver.switchTo().window(handle);
			}
		}
		return parentHandle;
	}

	protected void switchBackToParentWindow(WebDriver driver, String parentHandle) {
		driver.switchTo().window(parentHandle);
	}*/
}
