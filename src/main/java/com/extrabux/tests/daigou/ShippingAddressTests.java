package com.extrabux.tests.daigou;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.extrabux.pages.daigou.CartSummarySection;
import com.extrabux.pages.daigou.ManageShippingAddressesPage;
import com.extrabux.pages.daigou.ProductInfo;
import com.extrabux.pages.daigou.PurchasePreviewPage;
import com.extrabux.pages.daigou.SavedAddress;
import com.extrabux.pages.daigou.Verify;


public class ShippingAddressTests extends DaigouBaseTest {
	//private static final Log LOG = LogFactory.getLog(ShippingAddressTests.class);

	String daigouUrl = "https://dev-sugou.extrabux.com";
	String daigouMockUrl = "https://dev-sugou.extrabux.com/payment/mock-notify";
	String daigouShippingAddressesUrl = "https://dev-sugou.extrabux.com/shipping-addresses";
	String userName = "elizabeth@extrabux.com";
	String password = "javasucks";
	String productUrl = "http://usc-magento.extrabux.net/banana.html";
	String storeName = "USC Magento";
	String productName = "Banana";
	String price = "$0.01";
	String productUrl2 = "http://usc-magento.extrabux.net/angora-rabbit.html";
	String storeName2 = "USC Magento";
	String productName2 = "Angora Rabbit";
	String price2 = "$1.99";
	String productUrl3 = "http://usc-magento2.extrabux.net/fractal-wrongness-poster.html";
	String storeName3 = "USC Magento 2";
	String productName3 = "Fractal Wrongness Poster";
	String price3 = "$7.99";

	@Test(dataProvider = "getWebDriver")
	public void enterAllFields(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		SavedAddress newAddress = new SavedAddress(
				config.getString("usc.shipAddr.nickName"),
				config.getString("usc.shipAddr.firstName"),
				config.getString("usc.shipAddr.lastName"), null,
				config.getString("usc.shipAddr.address1"), "Apt. #5",
				config.getString("usc.shipAddr.city"),
				config.getString("usc.shipAddr.state"),
				config.getString("usc.shipAddr.zip"),
				config.getString("usc.shipAddr.country"),
				config.getString("usc.shipAddr.phone"),
				config.getBoolean("usc.shipAddr.isDefault"));
		expectedAddresses.add(newAddress);

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, newAddress);

		assertTrue(driver.getCurrentUrl().equals("https://qa-sugou.extrabux.com/shipping-addresses"),
				"errors mismatch.  expected: https://qa-sugou.extrabux.com/shipping-addresses. actual: " + driver.getCurrentUrl());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveAllFieldsBlank(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		SavedAddress newAddress = new SavedAddress("","","", null,"", null,"", config.getString("usc.shipAddr.state"),"","","",false);
		expectedAddresses.add(newAddress);

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		getToShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.shippingPage.error.allFields"));

		assertTrue(shippingAddressPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + shippingAddressPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveNonRequiredFieldsBlank(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		getToShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		assertTrue(driver.getCurrentUrl().equals("https://qa-sugou.extrabux.com/shipping-addresses"),
				"errors mismatch.  expected: https://qa-sugou.extrabux.com/shipping-addresses. actual: " + driver.getCurrentUrl());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveShippingAddressNicknameBlank(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		SavedAddress newAddress = new SavedAddress(
				"",
				config.getString("usc.shipAddr.firstName"),
				config.getString("usc.shipAddr.lastName"), null,
				config.getString("usc.shipAddr.address1"), "Apt. #5",
				config.getString("usc.shipAddr.city"),
				config.getString("usc.shipAddr.state"),
				config.getString("usc.shipAddr.zip"),
				config.getString("usc.shipAddr.country"),
				config.getString("usc.shipAddr.phone"),
				config.getBoolean("usc.shipAddr.isDefault"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		getToShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.shippingPage.error.nickname"));

		assertTrue(shippingAddressPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + shippingAddressPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveFirstNameBlank(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		SavedAddress newAddress = new SavedAddress(
				config.getString("usc.shipAddr.nickName"),"",
				config.getString("usc.shipAddr.lastName"), null,
				config.getString("usc.shipAddr.address1"), "Apt. #5",
				config.getString("usc.shipAddr.city"),
				config.getString("usc.shipAddr.state"),
				config.getString("usc.shipAddr.zip"),
				config.getString("usc.shipAddr.country"),
				config.getString("usc.shipAddr.phone"),
				config.getBoolean("usc.shipAddr.isDefault"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		getToShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.shippingPage.error.firstName"));

		assertTrue(shippingAddressPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + shippingAddressPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveLastNameBlank(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		SavedAddress newAddress = new SavedAddress(
				config.getString("usc.shipAddr.nickName"),
				config.getString("usc.shipAddr.firstName"),"", null,
				config.getString("usc.shipAddr.address1"), "Apt. #5",
				config.getString("usc.shipAddr.city"),
				config.getString("usc.shipAddr.state"),
				config.getString("usc.shipAddr.zip"),
				config.getString("usc.shipAddr.country"),
				config.getString("usc.shipAddr.phone"),
				config.getBoolean("usc.shipAddr.isDefault"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		getToShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.shippingPage.error.lastName"));

		assertTrue(shippingAddressPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + shippingAddressPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveAddress1Blank(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		SavedAddress newAddress = new SavedAddress(
				config.getString("usc.shipAddr.nickName"),
				config.getString("usc.shipAddr.firstName"),
				config.getString("usc.shipAddr.lastName"), null,"", null,
				config.getString("usc.shipAddr.city"),
				config.getString("usc.shipAddr.state"),
				config.getString("usc.shipAddr.zip"),
				config.getString("usc.shipAddr.country"),
				config.getString("usc.shipAddr.phone"),
				config.getBoolean("usc.shipAddr.isDefault"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		getToShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.shippingPage.error.address1"));

		assertTrue(shippingAddressPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + shippingAddressPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveCityBlank(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		SavedAddress newAddress = new SavedAddress(
				config.getString("usc.shipAddr.nickName"),
				config.getString("usc.shipAddr.firstName"),
				config.getString("usc.shipAddr.lastName"), null,
				config.getString("usc.shipAddr.address1"), null,"",
				config.getString("usc.shipAddr.state"),
				config.getString("usc.shipAddr.zip"),
				config.getString("usc.shipAddr.country"),
				config.getString("usc.shipAddr.phone"),
				config.getBoolean("usc.shipAddr.isDefault"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		getToShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.shippingPage.error.city"));

		assertTrue(shippingAddressPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + shippingAddressPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveZipBlank(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		SavedAddress newAddress = new SavedAddress(
				config.getString("usc.shipAddr.nickName"),
				config.getString("usc.shipAddr.firstName"),
				config.getString("usc.shipAddr.lastName"), null,
				config.getString("usc.shipAddr.address1"), "Apt. #5",
				config.getString("usc.shipAddr.city"),
				config.getString("usc.shipAddr.state"),"",
				config.getString("usc.shipAddr.country"),
				config.getString("usc.shipAddr.phone"),
				config.getBoolean("usc.shipAddr.isDefault"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		getToShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.shippingPage.error.zip"));

		assertTrue(shippingAddressPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + shippingAddressPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveCountryBlank(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		SavedAddress newAddress = new SavedAddress(
				config.getString("usc.shipAddr.nickName"),
				config.getString("usc.shipAddr.firstName"),
				config.getString("usc.shipAddr.lastName"), null,
				config.getString("usc.shipAddr.address1"), "Apt. #5",
				config.getString("usc.shipAddr.city"),
				config.getString("usc.shipAddr.state"),
				config.getString("usc.shipAddr.zip"),"",
				config.getString("usc.shipAddr.phone"),
				config.getBoolean("usc.shipAddr.isDefault"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		getToShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.shippingPage.error.country"));

		assertTrue(shippingAddressPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + shippingAddressPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void leavePhoneBlank(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		SavedAddress newAddress = new SavedAddress(
				config.getString("usc.shipAddr.nickName"),
				config.getString("usc.shipAddr.firstName"),
				config.getString("usc.shipAddr.lastName"), null,
				config.getString("usc.shipAddr.address1"), "Apt. #5",
				config.getString("usc.shipAddr.city"),
				config.getString("usc.shipAddr.state"),
				config.getString("usc.shipAddr.zip"),
				config.getString("usc.shipAddr.country"),"",
				config.getBoolean("usc.shipAddr.isDefault"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		getToShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.shippingPage.error.phone"));

		assertTrue(shippingAddressPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + shippingAddressPage.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifySavedAddress(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		expectedAddresses.add(new SavedAddress(config));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		getToShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		PurchasePreviewPage previewPage = new PurchasePreviewPage(driver);
		previewPage.verifyShippingAddressPresent();
		previewPage.clickChangeAddressBtn();

		shippingAddressPage = new ManageShippingAddressesPage(driver);
		shippingAddressPage.waitForSavedAddressNickname(0);

		shippingAddressPage = new ManageShippingAddressesPage(driver);

		Verify verify = shippingAddressPage.verifyAddresses(expectedAddresses);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void editSavedAddress(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();

		SavedAddress newAddress = new SavedAddress("Meling's Address", "Matthew", "Meling", "", "123 Fake St.",
				"", "North Park", "OR", "90210", "CAN", "6198131234", true);

		expectedAddresses.add(newAddress);

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		PurchasePreviewPage previewPage = new PurchasePreviewPage(driver);
		previewPage.verifyShippingAddressPresent();
		previewPage.clickChangeAddressBtn();

		shippingAddressPage = new ManageShippingAddressesPage(driver);
		shippingAddressPage.waitForSavedAddressNickname(0);

		shippingAddressPage = new ManageShippingAddressesPage(driver);
		shippingAddressPage.clickEditAddressBtn(0);

		enterShippingAddressInfo(driver, shippingAddressPage, newAddress);

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		previewPage = new PurchasePreviewPage(driver);
		previewPage.verifyShippingAddressPresent();
		previewPage.clickChangeAddressBtn();

		shippingAddressPage = new ManageShippingAddressesPage(driver);
		shippingAddressPage.waitForSavedAddressNickname(0);

		shippingAddressPage = new ManageShippingAddressesPage(driver);
		shippingAddressPage.clickEditAddressBtn(0);

		Verify verify1 = shippingAddressPage.verifyAddresses(expectedAddresses);
		Verify verify2 = shippingAddressPage.verifyEditedAddress(newAddress);
		assertTrue(verify1.isVerified(), verify1.getError());
		assertTrue(verify2.isVerified(), verify2.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void multipleSavedAddresses(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		SavedAddress newAddress1 = new SavedAddress(config);
		SavedAddress newAddress2 = new SavedAddress(config.getString("usc.shipAddr.nickName") + "2",
				config.getString("usc.shipAddr.firstName"),
				config.getString("usc.shipAddr.lastName") + "2", null,
				config.getString("usc.shipAddr.address1") + "2", "Apt. #5",
				config.getString("usc.shipAddr.city"),
				config.getString("usc.shipAddr.state"),
				config.getString("usc.shipAddr.zip"),
				config.getString("usc.shipAddr.country"),
				config.getString("usc.shipAddr.phone"),
				config.getBoolean("usc.shipAddr.isDefault"));
		expectedAddresses.add(newAddress1);
		expectedAddresses.add(newAddress2);

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, newAddress1);

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		PurchasePreviewPage previewPage = new PurchasePreviewPage(driver);
		previewPage.verifyShippingAddressPresent();
		previewPage.clickChangeAddressBtn();

		shippingAddressPage = new ManageShippingAddressesPage(driver);
		shippingAddressPage.waitForSavedAddressNickname(0);

		enterShippingAddressInfo(driver, shippingAddressPage, newAddress2);

		shippingAddressPage.waitForAddressSaveSuccessful();

		shippingAddressPage.waitForSavedAddressNickname(1);

		shippingAddressPage = new ManageShippingAddressesPage(driver);

		Verify verify = shippingAddressPage.verifyAddresses(expectedAddresses);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Override
	@BeforeMethod
	public void cleanup() {
		clearAllSavedAddressesCleanup(config.getString("daigou.shippingAddressUserEmail"), config.getString("daigou.shippingAddressUserPassword"));
	}

}