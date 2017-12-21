package com.extrabux.tests.daigou;

import static org.testng.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.BasePage;

public class DeepLinkingTests extends DaigouBaseTest {
	private static final Log LOG = LogFactory.getLog(DeepLinkingTests.class);

	@Test(dataProvider = "getWebDriver")
	public void verifyNotLoggedIn(WebDriver driver) {
		LOG.debug("Going to sugou page");
		BasePage page = new BasePage(driver);
		page.goToURL(getSugouUrl());

		LOG.debug("Verify page");
		assertTrue(verifyOnLoginPage(driver), "Not on Login page. Current url: " + driver.getCurrentUrl());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifyCreditCardsPage(WebDriver driver) {
		login(driver, config.getString("daigou.deepLinkingUserEmail"), config.getString("daigou.deepLinkingUserPassword"));

		LOG.debug("Going to Credit Cards Page");
		driver.get(getCreditCardsUrl());

		LOG.debug("Verify page");
		assertTrue(verifyOnShippingAddressesPage(driver), "Not on Shipping Addresses page after Credit Card redirect. Current url: " + driver.getCurrentUrl() + "; Should be " + getShippingAddressesUrl());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifyShippingAddressesPage(WebDriver driver) {
		login(driver, config.getString("daigou.deepLinkingUserEmail"), config.getString("daigou.deepLinkingUserPassword"));

		LOG.debug("Going to Shipping Addresses Page");
		driver.get(getShippingAddressesUrl());

		LOG.debug("Verify page");
		assertTrue(verifyOnShippingAddressesPage(driver), "Not on Shipping Addresses page. Current url: " + driver.getCurrentUrl());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifyCheckoutSummaryPage(WebDriver driver) {
		login(driver, config.getString("daigou.deepLinkingUserEmail"), config.getString("daigou.deepLinkingUserPassword"));

		LOG.debug("Going to Checkout Summary Page");
		driver.get(getCheckoutSummaryUrl());

		LOG.debug("Verify page");
		assertTrue(verifyOnShippingAddressesPage(driver), "Not on Shipping Addresses page after Checkout Summary redirect. Current url: " + driver.getCurrentUrl() + "; Should be " + getShippingAddressesUrl());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifyPurchaseConfirmationPage(WebDriver driver)  {
		login(driver, config.getString("daigou.deepLinkingUserEmail"), config.getString("daigou.deepLinkingUserPassword"));

		LOG.debug("Going to Purchase Confirmation Page");
		driver.get(getPurchaseConfirmationUrl());

		LOG.debug("Verify page");
		assertTrue(verifyOnShopperPage(driver), "Not on Shopper page after Purchase Confirmation redirect. Current url: " + driver.getCurrentUrl() + "; Should be " + getShopperUrl());
	}
}
