package com.extrabux.tests.daigou;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.extrabux.pages.daigou.CartSummarySection;
import com.extrabux.pages.daigou.ManageShippingAddressesPage;
import com.extrabux.pages.daigou.ProductComparator;
import com.extrabux.pages.daigou.ProductInfo;
import com.extrabux.pages.daigou.PurchasePreviewCartSection;
import com.extrabux.pages.daigou.PurchasePreviewItemListSection;
import com.extrabux.pages.daigou.PurchasePreviewPage;
import com.extrabux.pages.daigou.SavedAddress;
import com.extrabux.pages.daigou.Verify;


public class PurchasePreviewTests extends DaigouBaseTest {
	String productUrl = "http://usc-magento.extrabux.net/banana.html";
	boolean productUrlHasOptions = false;
	String storeName = "USC Magento";
	String productName = "Banana";
	String price = "$0.01";
	String productUrl2 = "http://usc-magento.extrabux.net/classic-white-rabbit.html";
	boolean productUrl2HasOptions = false;
	String storeName2 = "USC Magento";
	String productName2 = "Classic White Rabbit";
	String price2 = "$15.99";
	String productUrl3 = "http://usc-magento2.extrabux.net/fractal-wrongness-poster.html";

	@Test(dataProvider = "getWebDriver")
	public void verifyOneItem(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.purchasePreviewUserEmail"), config.getString("daigou.purchasePreviewUserPassword"));

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		expectedAddresses.add(new SavedAddress(config));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		enterUrl(driver, productUrl, false);

		addOneSearchResultItemToCart(driver);

		String cartSubtotal = cartSummary.getCartSubtotal().replace("$", "");
		String cartCashback = cartSummary.getCartCashback().replace("$", "");

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		PurchasePreviewPage previewPage = new PurchasePreviewPage(driver);
		PurchasePreviewCartSection previewCart = new PurchasePreviewCartSection(driver);
		PurchasePreviewItemListSection previewCartItems = new PurchasePreviewItemListSection(driver);

		previewPage.verifyShippingAddressPresent();

		// verify cart
		Verify verifyCart = previewCart.verifyPreviewCartInfo(cartSubtotal, cartCashback);
		Verify verifyItems = previewCartItems.verifyProducts(expectedProducts);
		assertTrue(verifyCart.isVerified(), verifyCart.getError());
		assertTrue(verifyItems.isVerified(), verifyItems.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifyMultipleItems(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.purchasePreviewUserEmail"), config.getString("daigou.purchasePreviewUserPassword"));

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		expectedAddresses.add(new SavedAddress(config));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		ProductInfo product2 = new ProductInfo(storeName2, productName2, "", "1", price2);
		expectedProducts.add(product1);
		expectedProducts.add(product2);

		cartSummary.waitForCartSpinnerNotPresent();

		enterUrl(driver, productUrl2, productUrl2HasOptions);

		addOneSearchResultItemToCart(driver);

		enterUrl(driver, productUrl, productUrlHasOptions);

		addOneSearchResultItemToCart(driver);

		Thread.sleep(2000);

		String cartSubtotal = cartSummary.getCartSubtotal().replace("$", "");
		String cartCashback = cartSummary.getCartCashback().replace("$", "");

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		PurchasePreviewPage previewPage = new PurchasePreviewPage(driver);
		PurchasePreviewCartSection previewCart = new PurchasePreviewCartSection(driver);
		PurchasePreviewItemListSection previewCartItems = new PurchasePreviewItemListSection(driver);

		previewPage.verifyShippingAddressPresent();

		Collections.sort(expectedProducts, new ProductComparator());

		Verify verifyCart = previewCart.verifyPreviewCartInfo(cartSubtotal, cartCashback);
		Verify verifyItems = previewCartItems.verifyProducts(expectedProducts);
		assertTrue(verifyCart.isVerified(), verifyCart.getError());
		assertTrue(verifyItems.isVerified(), verifyItems.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void validCouponCode(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.purchasePreviewUserEmail"), config.getString("daigou.purchasePreviewUserPassword"));

		String couponCode = config.getString("daigou.coupon.code");
		String couponDiscountAmount = config.getString("daigou.coupon.discount");

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		expectedAddresses.add(new SavedAddress(
				config.getString("usc.shipAddr.nickName"),
				config.getString("usc.shipAddr.firstName"),
				config.getString("usc.shipAddr.lastName"), null,
				config.getString("usc.shipAddr.address1"), "Apt. #5",
				"Boulder", "CO", "80304",
				config.getString("usc.shipAddr.country"),
				config.getString("usc.shipAddr.phone"),
				config.getBoolean("usc.shipAddr.isDefault")));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName2, productName2, "", "1", price2);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		enterUrl(driver, productUrl2, false);

		addOneSearchResultItemToCart(driver);

		String cartSubtotal = cartSummary.getCartSubtotal().replace("$", "");
		String cartCashback = String.valueOf(Float.parseFloat(cartSummary.getCartCashback().replace("$", ""))/2);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, expectedAddresses.get(0));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		PurchasePreviewPage previewPage = new PurchasePreviewPage(driver);
		PurchasePreviewCartSection previewCart = new PurchasePreviewCartSection(driver);
		PurchasePreviewItemListSection previewCartItems = new PurchasePreviewItemListSection(driver);

		previewPage.enterCoupon(couponCode);

		previewPage.verifyShippingAddressPresent();

		// verify cart
		Verify verifyCart = previewCart.verifyPreviewCartInfo(cartSubtotal, cartCashback, couponDiscountAmount);
		Verify verifyItems = previewCartItems.verifyProducts(expectedProducts);
		assertTrue(verifyCart.isVerified(), verifyCart.getError());
		assertTrue(verifyItems.isVerified(), verifyItems.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void invalidCouponCode(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.purchasePreviewUserEmail"), config.getString("daigou.purchasePreviewUserPassword"));

		String couponCode = "boogers";

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		expectedAddresses.add(new SavedAddress(
				config.getString("usc.shipAddr.nickName"),
				config.getString("usc.shipAddr.firstName"),
				config.getString("usc.shipAddr.lastName"), null,
				config.getString("usc.shipAddr.address1"), "Apt. #5",
				"Boulder", "CO", "80304",
				config.getString("usc.shipAddr.country"),
				config.getString("usc.shipAddr.phone"),
				config.getBoolean("usc.shipAddr.isDefault")));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName2, productName2, "", "1", price2);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		enterUrl(driver, productUrl2, false);

		addOneSearchResultItemToCart(driver);

		String cartSubtotal = cartSummary.getCartSubtotal().replace("$", "");
		String cartCashback = cartSummary.getCartCashback().replace("$", "");

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, expectedAddresses.get(0));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		PurchasePreviewPage previewPage = new PurchasePreviewPage(driver);
		PurchasePreviewCartSection previewCart = new PurchasePreviewCartSection(driver);

		previewPage.enterCoupon(couponCode);
		previewPage.waitForCouponDialog();

		Verify verifyCart = previewCart.verifyPreviewCartInfo(cartSubtotal, cartCashback);
		assertTrue(!previewPage.verifyCouponValidity());
		assertTrue(verifyCart.isVerified(), verifyCart.getError());
	}

	@Override
	@BeforeMethod
	public void cleanup() {
		super.cleanup(config.getString("daigou.purchasePreviewUserEmail"), config.getString("daigou.purchasePreviewUserPassword"));
	}

}
