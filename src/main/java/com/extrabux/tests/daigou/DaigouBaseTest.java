package com.extrabux.tests.daigou;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;

import com.extrabux.pages.BasePage;
import com.extrabux.pages.LoginPage;
import com.extrabux.pages.daigou.CartSummarySection;
import com.extrabux.pages.daigou.ManageShippingAddressesPage;
import com.extrabux.pages.daigou.MemberLoginSection;
import com.extrabux.pages.daigou.ProductInfo;
import com.extrabux.pages.daigou.ProductInfoSection;
import com.extrabux.pages.daigou.ProductUrlInputSection;
import com.extrabux.pages.daigou.SavedAddress;
import com.extrabux.tests.BaseTest;
import com.extrabux.util.WebDriverUtil;

public class DaigouBaseTest extends BaseTest {
	private static final Log LOG = LogFactory.getLog(DaigouBaseTest.class);
	final String LOGIN_URI = "/users/login/return/%2Foauth2%2Fauthorize";
	final String SHOPPER_URI = "/shopper";
	final String CREDIT_CARDS_URI = "/credit-cards/manage";
	final String SHIPPING_ADDRESSES_URI = "/shipping-addresses";
	final String MOCK_ALIPAY_URI = "/payment/mock-notify";
	final String CLEANUP_URI = "/test/utils";
	final String API_LOG_URI = "/test/api-logs";
	final String ERROR_LOG_URI = "/test/error-logs";
	final String CHECKOUT_SUMMARY_URI = "/checkout/summary";
	final String PURCHASE_PREVIEW_URI = "/checkout/purchase/preview";
	final String PURCHASE_CONFIRM_URI = "/checkout/confirmation";
	final String CART_RESTORE_URI = "/cart/restore/%s/ffs/%s";

	String sugouServer = "qa-sugou.extrabux.com";

	@BeforeClass
	public void getDaigouServerName(ITestContext testNGContext) {
		LOG.info("Setting Sugou server name...");
		if (testNGContext.getCurrentXmlTest().getAllParameters().containsKey("daigouServer")
				|| System.getProperties().containsKey("daigouServer")) {
			sugouServer = System.getProperty("daigouServer");
			if (sugouServer == null) {
				sugouServer = testNGContext.getCurrentXmlTest().getParameter("daigouServer");
			}
		} else {
			sugouServer = "qa-sugou.extrabux.com";
		}
		LOG.info("Sugou server: " + sugouServer);
	}

	public String getLoginUrl() {
		// This is different from all other Sugou URLs due to the subdomain
		return "https://staging-current.extrabux.com" + LOGIN_URI;
	}

	public String getSugouUrl() {
		LOG.debug("Getting Sugou URL: https://" + sugouServer);
		return "https://" + sugouServer;
	}

	public String getShopperUrl() {
		LOG.debug("Getting Shopper URL: https://" + sugouServer + SHOPPER_URI);
		return "https://" + sugouServer + SHOPPER_URI;
	}

	public String getSugouMockUrl() {
		LOG.debug("Getting Alipay Mock Payment URL: https://" + sugouServer + MOCK_ALIPAY_URI);
		return "https://" + sugouServer + MOCK_ALIPAY_URI;
	}

	public static String getSugouCleanupUrl() {
		DaigouBaseTest baseTest = new DaigouBaseTest();
		LOG.debug("Getting Cleanup URL: https://" + baseTest.sugouServer + baseTest.CLEANUP_URI);
		return "https://" + baseTest.sugouServer + baseTest.CLEANUP_URI;
	}

	public String getShippingAddressesUrl() {
		LOG.debug("Getting Shipping Addresses URL: https://" + sugouServer + CLEANUP_URI);
		return "https://" + sugouServer + SHIPPING_ADDRESSES_URI;
	}

	public String getCheckoutSummaryUrl() {
		LOG.debug("Getting Checkout Summary URL: https://" + sugouServer + CHECKOUT_SUMMARY_URI);
		return "https://" + sugouServer + CHECKOUT_SUMMARY_URI;
	}

	public String getPurchasePreviewUrl() {
		LOG.debug("Getting Purchase Preview URL: https://" + sugouServer + PURCHASE_PREVIEW_URI);
		return "https://" + sugouServer + PURCHASE_PREVIEW_URI;
	}

	public String getPurchaseConfirmationUrl() {
		LOG.debug("Getting Purchase Confirmation URL: https://" + sugouServer + PURCHASE_CONFIRM_URI);
		return "https://" + sugouServer + PURCHASE_CONFIRM_URI;
	}

	public static String getApiLogUrl() {
		DaigouBaseTest baseTest = new DaigouBaseTest();
		LOG.debug("Getting API Log URL: https://" + baseTest.sugouServer + baseTest.API_LOG_URI);
		return "https://" + baseTest.sugouServer + baseTest.API_LOG_URI;
	}

	public static String getErrorLogUrl() {
		DaigouBaseTest baseTest = new DaigouBaseTest();
		LOG.debug("Getting Error Log URL: https://" + baseTest.sugouServer + baseTest.ERROR_LOG_URI);
		return "https://" + baseTest.sugouServer + baseTest.ERROR_LOG_URI;
	}

	public String getCreditCardsUrl() {
		LOG.debug("Getting Credit Cards URL: https://" + sugouServer + CREDIT_CARDS_URI);
		return "https://" + sugouServer + CREDIT_CARDS_URI;
	}

	public void login(WebDriver driver, String user, String pass) {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToURL(getSugouUrl());
		loginPage.login(user, pass);
	}

	protected void enterUrl(WebDriver driver, String url, boolean hasOptions) {
		ProductUrlInputSection productUrlInput = new ProductUrlInputSection(driver);
		ProductInfoSection productInfo = new ProductInfoSection(driver);

		LOG.debug("Entering product url: " + url);
		productUrlInput.typeProductUrl(url);
		LOG.debug("Clicking submit button.");
		productUrlInput.clickSubmit();

		LOG.debug("Waiting for product info spinner...");
		productInfo.waitForProductInfoSpinner();
		LOG.debug("Waiting for cashback spinner...");
		productInfo.waitForCashbackSpinner();
		LOG.debug("Waiting for reviews spinner...");
		productInfo.waitForReviewsSpinner();
	}

	protected void selectStoreAndEnterUrl(WebDriver driver, String store, String url, boolean hasOptions) {
		ProductUrlInputSection productUrlInput = new ProductUrlInputSection(driver);
		ProductInfoSection productInfo = new ProductInfoSection(driver);

		LOG.debug("Selecting store " + store + " from dropdown list.");
		productUrlInput.selectStoreFromDropDown(store);
		LOG.debug("Entering product url: " + url);
		productUrlInput.typeProductUrl(url);
		LOG.debug("Clicking submit button.");
		productUrlInput.clickSubmit();

		LOG.debug("Waiting for product info spinner...");
		productInfo.waitForProductInfoSpinner();
		LOG.debug("Waiting for cashback spinner...");
		productInfo.waitForCashbackSpinner();
		LOG.debug("Waiting for reviews spinner...");
		productInfo.waitForReviewsSpinner();
	}

	protected void waitForPurchasePreviewPage(WebDriver driver) {
		CartSummarySection cartSummary = new CartSummarySection(driver);
		LOG.debug("Clicking the Checkout Summary Button");
		cartSummary.clickCheckoutSummaryBtn();
		LOG.debug("Waiting for progress bar");
		cartSummary.waitForProgressBar();
		LOG.debug("Waiting for Purchase Button to exist in the DOM");
		cartSummary.waitForPurchaseBtn();
		LOG.debug("Waiting for Purchase Button to be clickable");
		cartSummary.waitForPurchaseBtnClickable();
	}

	protected void getToShippingAddressesPage(WebDriver driver) {
		if(!driver.getCurrentUrl().equals("https://dev-sugou.extrabux.com/shipping-addresses")){
			BasePage page = new BasePage(driver);
			page.goToURL(getShippingAddressesUrl());
			ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);
			shippingAddressPage.waitForNicknameField();
		}
	}

	protected void restoreCart(WebDriver driver, String cartId, String ffsId) {
		// Unfortunate sleep needed to wait for the failed purchase to process
		try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BasePage page = new BasePage(driver);
		page.goToURL(String.format(getCartRestorationUrl(driver), cartId, ffsId));
		waitForPurchasePreviewPage(driver);
	}

	protected void clearCart(WebDriver driver) {
		CartSummarySection cartSummary = new CartSummarySection(driver);
		cartSummary.waitForCartSpinnerNotPresent();
		LOG.info("checking if clear cart button is visible...");
		if (cartSummary.isClearCartBtnVisible()) {
			LOG.debug("Clicking Clear Cart button");
			cartSummary.clickClearCart();
			LOG.debug("Waiting for Purchase Preview button to not be clickable");
			cartSummary.waitForPurchasePreviewBtnNotClickable();
		} else {
			LOG.info("clear cart button was not visible.");
		}
	}

	protected void addOneSearchResultItemToCart(WebDriver driver) throws InterruptedException {
		CartSummarySection cartSummary = new CartSummarySection(driver);
		ProductInfoSection productInfo = new ProductInfoSection(driver);

		int itemsInCart = Integer.parseInt(cartSummary.getNumberOfItemsInCart());
		LOG.debug("Setting product quantity to 1.");
		productInfo.selectQuantity(1);
		LOG.debug("Waiting for Add to Cart Button to be visible.");
		productInfo.waitForAddToCartButtonVisible();
		LOG.debug("Clicking Add to Cart Button.");
		productInfo.addToCart();

		LOG.debug("Waiting for Purchase Preview Button to be clickable.");
		cartSummary.waitForPurchasePreviewBtnClickable();
		LOG.debug("Waiting for cart spinner to not be present.");
		cartSummary.waitForCartSpinnerNotPresent();

		LOG.debug("Waiting for items to appear in cart.");
		WebDriverUtil.waitForElementPresent(driver, By.xpath(String.format("//span[@id='total-items' and text()='(%d)']", itemsInCart+1)), 5);
	}

	public void enterShippingAddressInfo(WebDriver driver,
			ManageShippingAddressesPage shippingAddressPage) {
		LOG.debug("Waiting for nickname field to be present.");
		shippingAddressPage.waitForNicknameField();
		LOG.info("entering shipping address");
		shippingAddressPage.fillFormAndSubmit(new SavedAddress(config));
	}

	public void enterShippingAddressInfo(WebDriver driver,
			ManageShippingAddressesPage shippingAddressPage, SavedAddress address) {
		shippingAddressPage.waitForNicknameField();
		LOG.info("entering shipping address");
		shippingAddressPage.fillFormAndSubmit(address);
	}

	protected void cleanup() {
		FirefoxDriver driver = new FirefoxDriver();
		loginToSugou(driver);
		cleanup(driver);
	}

	protected void cleanup(String userName, String password) {
		FirefoxDriver driver = new FirefoxDriver();
		loginToSugou(driver, userName, password);
		cleanup(driver);
	}

	protected void cleanup(WebDriver driver) {
		clearAllCreditCardsCleanup(driver);

		clearAllSavedAddressesCleanup(driver);

		clearCartCleanup(driver);

		driver.quit();
	}

	protected void loginToSugou(WebDriver driver) {
		BasePage page = new BasePage(driver);
		page.goToURL(getSugouUrl());
		LoginPage loginPage = new LoginPage(driver);
		LOG.debug("Log into Sugou");
		loginPage.login(config.getString("daigou.userEmail"), config.getString("daigou.userPassword"));
	}

	protected void loginToSugou(WebDriver driver, String username, String password) {
		BasePage page = new BasePage(driver);
		page.goToURL(getSugouUrl());
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);
	}

	protected boolean clearAllCreditCardsCleanup(WebDriver driver) {
		BasePage page = new BasePage(driver);
		page.goToURL(getCreditCardsUrl());
		LOG.debug("Deleting credit cards");
		try {
			WebDriverUtil.waitForElementPresentAndVisible(driver, By.xpath("//*[contains(@id, 'cardDeleteBtn')]"), 20);
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error("There were no cards on the page");
		}

		for(int i=0; i < driver.findElements(By.xpath("//*[contains(@id, 'cardDeleteBtn')]")).size(); i++) {
			WebDriverUtil.waitForElementPresentAndVisible(driver, By.xpath("//*[contains(@id, 'cardDeleteBtn')]"), 20);
			LOG.debug("Clicking Delete for the top credit card");
			driver.findElement(By.id("cardDeleteBtn0")).click();
			LOG.debug("Wait for delete spinner");
			WebDriverUtil.waitForElementNotVisible(driver, By.id("editCardsPageSpinner"), 25);
		}
		LOG.debug("Finished deleting credit cards");
		return true;
	}

	protected void clearAllCreditCardsCleanup() {
		FirefoxDriver driver = new FirefoxDriver();
		loginToSugou(driver);
		clearAllCreditCardsCleanup(driver);

		driver.quit();
	}

	protected void clearAllCreditCardsCleanup(String userName, String password) {
		FirefoxDriver driver = new FirefoxDriver();
		loginToSugou(driver, userName, password);
		clearAllCreditCardsCleanup(driver);

		driver.quit();
	}

	protected void clearAllSavedAddressesCleanup(WebDriver driver) {
		BasePage page = new BasePage(driver);
		page.goToURL(getShippingAddressesUrl());
		LOG.debug("Deleting shipping addresses");
		try {
			do {
				WebDriverUtil.waitForElementPresent(driver, By.id("addressDelete0"), 10);
				LOG.debug("Clicking Delete for the top address");
				driver.findElement(By.id("addressDelete0")).click();
			} while(driver.findElements(By.id("addressDelete0")).size() > 0);
		} catch (Exception e) {
			// e.printStackTrace();
			// do nothing
		}
		LOG.debug("Finished deleting shipping addresses");
	}

	protected void clearAllSavedAddressesCleanup() {
		WebDriver driver = new FirefoxDriver();
		loginToSugou(driver);
		clearCartCleanup(driver);
		clearAllSavedAddressesCleanup(driver);

		driver.quit();
	}

	protected void clearAllSavedAddressesCleanup(String userName, String password) {
		FirefoxDriver driver = new FirefoxDriver();
		loginToSugou(driver, userName, password);
		clearCartCleanup(driver);
		clearAllSavedAddressesCleanup(driver);

		driver.quit();
	}

	protected void clearCartCleanup(WebDriver driver) {
		BasePage page = new BasePage(driver);
		page.goToURL(getSugouCleanupUrl());
		driver.findElement(By.xpath("//*[@type='submit' and contains(@value, 'Reset')]")).click();
	}

	protected void clearCartCleanup() {
		WebDriver driver = new FirefoxDriver();
		loginToSugou(driver);
		clearCartCleanup(driver);

		driver.quit();
	}

	protected void clearCartCleanup(String userName, String password) {
		FirefoxDriver driver = new FirefoxDriver();
		loginToSugou(driver, userName, password);
		clearCartCleanup(driver);

		driver.quit();
	}

	protected void ensureProductInCart(String userName, String password) throws Exception {
		FirefoxDriver driver = new FirefoxDriver();
		loginToSugou(driver, userName, password);
		clearCart(driver);
		ensureProductInCart(driver);

		driver.quit();
	}

	protected void ensureProductInCart(WebDriver driver) throws InterruptedException {
		CartSummarySection cartSummary = new CartSummarySection(driver);

		cartSummary.waitForCartSpinnerNotPresent();

		if(!cartSummary.verifyNumberOfItemsInCart("1")){
			String productUrl = "http://usc-magento.extrabux.net/banana.html";
			boolean productUrlHasOptions = false;
			String storeName = "USC Magento";
			String productName = "Banana";
			String price = "$0.01";
			List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
			ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
			expectedProducts.add(product1);

			cartSummary.waitForCartSpinnerNotPresent();

			enterUrl(driver, productUrl, productUrlHasOptions);

			addOneSearchResultItemToCart(driver);
		}
	}

	protected void clearDrugstoreCleanup() {
		WebDriver driver = new FirefoxDriver();
		loginToSugou(driver);
		clearDrugstoreCleanup(driver);
	}

	protected void clearDrugstoreCleanup(String userName, String password) {
		FirefoxDriver driver = new FirefoxDriver();
		loginToSugou(driver, userName, password);
		clearDrugstoreCleanup(driver);
	}

	protected void clearDrugstoreCleanup(WebDriver driver) {
		ProductUrlInputSection productUrlInput = new ProductUrlInputSection(driver);
		LOG.debug("Select Drugstore from the dropdown");
		productUrlInput.selectStoreFromDropDown("Drugstore.com");

		MemberLoginSection memberLogin = new MemberLoginSection(driver);
		LOG.debug("Logout from Drugstore");
		memberLogin.logout();

		LOG.debug("Run clearAllSavedAddressCleanup");
		clearAllSavedAddressesCleanup(driver);

		LOG.debug("Run clearCartCleanup");
		clearCartCleanup(driver);

		LOG.debug("Close the browser");
		driver.quit();
	}

	public boolean verifyOnLoginPage(WebDriver driver) {
		LOG.debug("Waiting for page to load");
		WebDriverUtil.waitForElementPresent(driver, By.tagName("html"), 10);
		LOG.debug("Page loaded");

		LOG.debug("Returning if the URLs match or not");
		return driver.getCurrentUrl().equals(getLoginUrl());
	}

	public boolean verifyOnSugouPage(WebDriver driver) {
		LOG.debug("Waiting for page to load");
		WebDriverUtil.waitForElementPresent(driver, By.tagName("html"), 10);
		LOG.debug("Page loaded");

		LOG.debug("Returning if the URLs match or not");
		return driver.getCurrentUrl().equals(getSugouUrl());
	}

	public boolean verifyOnShopperPage(WebDriver driver) {
		LOG.debug("Waiting for page to load");
		WebDriverUtil.waitForElementPresent(driver, By.tagName("html"), 10);
		LOG.debug("Page loaded");

		LOG.debug("Returning if the URLs match or not");
		return driver.getCurrentUrl().equals(getShopperUrl());
	}

	public boolean verifyOnShippingAddressesPage(WebDriver driver) {
		LOG.debug("Waiting for page to load");
		WebDriverUtil.waitForElementPresent(driver, By.tagName("html"), 10);
		LOG.debug("Page loaded");

		LOG.debug("Returning if the URLs match or not");
		return driver.getCurrentUrl().equals(getShippingAddressesUrl());
	}

	public boolean verifyOnCheckoutSummaryPage(WebDriver driver) {
		LOG.debug("Waiting for page to load");
		WebDriverUtil.waitForElementPresent(driver, By.tagName("html"), 10);
		LOG.debug("Page loaded");

		LOG.debug("Returning if the URLs match or not");
		return driver.getCurrentUrl().equals(getCheckoutSummaryUrl());
	}

	public boolean verifyOnPurchasePreviewPage(WebDriver driver) {
		LOG.debug("Waiting for page to load");
		WebDriverUtil.waitForElementPresent(driver, By.tagName("html"), 10);
		LOG.debug("Page loaded");

		LOG.debug("Returning if the URLs match or not");
		return driver.getCurrentUrl().equals(getPurchasePreviewUrl());
	}

	public boolean verifyOnPurchaseConfirmationPage(WebDriver driver) {
		LOG.debug("Waiting for page to load");
		WebDriverUtil.waitForElementPresent(driver, By.tagName("html"), 10);
		LOG.debug("Page loaded");

		LOG.debug("Returning if the URLs match or not");
		return driver.getCurrentUrl().equals(getPurchaseConfirmationUrl());
	}

	public boolean verifyOnCreditCardsPage(WebDriver driver) {
		LOG.debug("Waiting for page to load");
		WebDriverUtil.waitForElementPresent(driver, By.tagName("html"), 10);
		LOG.debug("Page loaded");

		LOG.debug("Returning if the URLs match or not");
		return driver.getCurrentUrl().equals(getCreditCardsUrl());
	}

	public static String getApiLog(WebDriver driver) {
		BasePage page = new BasePage(driver);
		page.goToURL(getApiLogUrl());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return driver.getPageSource();
	}

	public static String getErrorLog(WebDriver driver) {
		BasePage page = new BasePage(driver);
		page.goToURL(getErrorLogUrl());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return driver.getPageSource();
	}

	public static String getCartId(WebDriver driver) {
		String currentUrl = driver.getCurrentUrl();
		BasePage page = new BasePage(driver);
		page.goToURL(getSugouCleanupUrl());
		String cartId = driver.findElement(By.xpath("//div[contains(text(), 'Cart id: ')]")).getText().replace("Cart id: ", "");
		page.goToURL(currentUrl);
		return cartId;
	}

	public String getCartRestorationUrl(WebDriver driver) {
		LOG.debug("Getting Cleanup URL: https://" + sugouServer + CART_RESTORE_URI);
		return "https://" + sugouServer + CART_RESTORE_URI;
	}

}
