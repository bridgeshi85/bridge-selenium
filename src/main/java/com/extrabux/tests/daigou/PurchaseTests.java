package com.extrabux.tests.daigou;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.extrabux.pages.daigou.CartSummarySection;
import com.extrabux.pages.daigou.CreditCardType;
import com.extrabux.pages.daigou.ManageShippingAddressesPage;
import com.extrabux.pages.daigou.ProductComparator;
import com.extrabux.pages.daigou.ProductInfo;
import com.extrabux.pages.daigou.PurchaseConfirmationPage;
import com.extrabux.pages.daigou.PurchasePreviewCartSection;
import com.extrabux.pages.daigou.PurchasePreviewPage;
import com.extrabux.pages.daigou.SavedAddress;
import com.extrabux.pages.daigou.SavedCreditCard;
import com.extrabux.pages.daigou.Verify;


public class PurchaseTests extends DaigouBaseTest {
	//private static final Log LOG = LogFactory.getLog(PurchaseTests.class);

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
	String productUrl4 = "http://usc-magento.extrabux.net/classic-white-rabbit.html";
	String storeName4 = "USC Magento";
	String productName4 = "Classic White Rabbit";
	String price4 = "$15.99";

	@Test(dataProvider = "getWebDriver")
	public void verifyOneItemAlipay(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.purchaseUserEmail"), config.getString("daigou.purchaseUserPassword"));

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		expectedAddresses.add(new SavedAddress(config));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		expectedProducts.add(new ProductInfo(storeName, productName, "", "1", price));

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		PurchasePreviewCartSection previewCart = new PurchasePreviewCartSection(driver);

		String cartTotal = previewCart.getTotal();
		String cartShipping = previewCart.getShipping();
		String cartTax = previewCart.getTax();
		String cartCashback = previewCart.getDiscount();

		PurchasePreviewPage purchasePreview = new PurchasePreviewPage(driver);
		purchasePreview.selectPaymentMethod("alipay");
		cartSummary.clickPurchaseBtn();
		// will transfer to alipay but we are cheating with the mocky way
		cartSummary.goToURL(getSugouMockUrl());
		purchasePreview.waitForRedirectSpinner();

		PurchaseConfirmationPage purchaseConfirmation = new PurchaseConfirmationPage(driver);

		Verify purchaseTotals = purchaseConfirmation.verifyTotals(cartTotal, cartShipping, cartTax, cartCashback);
		Verify purchaseBillingInfo = purchaseConfirmation.verifyBillingInfo(new SavedAddress(config), "", "alipay");
		Verify purchasedItems = purchaseConfirmation.verifyProducts(expectedProducts);
		assertTrue(purchaseConfirmation.verifyOnConfirmationPage(), "not on confirmation page.  current url : " + driver.getCurrentUrl());
		assertTrue(purchaseTotals.isVerified(), purchaseTotals.getError());
		assertTrue(purchaseBillingInfo.isVerified(), purchaseBillingInfo.getError());
		assertTrue(purchasedItems.isVerified(), purchasedItems.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifyMultipleItemsAlipay(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.purchaseUserEmail"), config.getString("daigou.purchaseUserPassword"));

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		expectedAddresses.add(new SavedAddress(config));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		expectedProducts.add(new ProductInfo(storeName2, productName2, "", "1", price2));
		expectedProducts.add(new ProductInfo(storeName, productName, "", "1", price));

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		selectStoreAndEnterUrl(driver, storeName2, productUrl2, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		PurchasePreviewCartSection previewCart = new PurchasePreviewCartSection(driver);

		String cartTotal = previewCart.getTotal();
		String cartShipping = previewCart.getShipping();
		String cartTax = previewCart.getTax();
		String cartCashback = previewCart.getDiscount();

		PurchasePreviewPage purchasePreview = new PurchasePreviewPage(driver);
		purchasePreview.selectPaymentMethod("alipay");
		cartSummary.clickPurchaseBtn();
		// will transfer to alipay but we are cheating with the mocky way
		cartSummary.goToURL(getSugouMockUrl());
		purchasePreview.waitForRedirectSpinner();

		PurchaseConfirmationPage purchaseConfirmation = new PurchaseConfirmationPage(driver);

		Collections.sort(expectedProducts, new ProductComparator());

		Verify purchaseTotals = purchaseConfirmation.verifyTotals(cartTotal, cartShipping, cartTax, cartCashback);
		Verify purchaseBillingInfo = purchaseConfirmation.verifyBillingInfo(new SavedAddress(config), "", "alipay");
		Verify purchasedItems = purchaseConfirmation.verifyProducts(expectedProducts);
		assertTrue(purchaseConfirmation.verifyOnConfirmationPage(), "not on confirmation page.  current url : " + driver.getCurrentUrl());
		assertTrue(purchaseTotals.isVerified(), purchaseTotals.getError());
		assertTrue(purchaseBillingInfo.isVerified(), purchaseBillingInfo.getError());
		assertTrue(purchasedItems.isVerified(), purchasedItems.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifyOneItemCreditCard(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.purchaseUserEmail"), config.getString("daigou.purchaseUserPassword"));

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		expectedAddresses.add(new SavedAddress(config));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		expectedProducts.add(new ProductInfo(storeName, productName, "", "1", price));

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		PurchasePreviewCartSection previewCart = new PurchasePreviewCartSection(driver);

		String cartTotal = previewCart.getTotal();
		String cartShipping = previewCart.getShipping();
		String cartTax = previewCart.getTax();
		String cartCashback = previewCart.getDiscount();

		PurchasePreviewPage previewPage = new PurchasePreviewPage(driver);
		previewPage.selectPaymentMethod("creditCard");

		previewPage.addNewCreditCard(config.getString("usc.cc.cardHolderName"), config.getString("usc.cc.cardNumber"), config.getString("usc.cc.exp.month"),
				config.getInt("usc.cc.exp.year"), config.getString("usc.cc.cv2"), config.getBoolean("usc.cc.isDefault"), null);
		// check that there weren't any errors
		assertTrue(previewPage.verifyNoCreditCardErrors(), "error encountered when adding creditCard.");
		// check that card was added
		previewPage.waitForSaveCardSpinner();

		SavedCreditCard firstCard = previewPage.getFirstCreditCard();
		assertTrue(firstCard.getType() == CreditCardType.VISA, "Expected credit card type to be VISA but found : " + firstCard.getType());
		assertTrue(firstCard.getMaskedNumber().equals("***4242"), "Expected credit card masked # to be ***4242 but found : " + firstCard.getMaskedNumber());

		cartSummary.clickPurchaseBtn();

		previewPage.waitForPurchaseProcessingBar();

		PurchaseConfirmationPage purchaseConfirmation = new PurchaseConfirmationPage(driver);

		Verify purchaseTotals = purchaseConfirmation.verifyTotals(cartTotal, cartShipping, cartTax, cartCashback);
		Verify purchaseBillingInfo = purchaseConfirmation.verifyBillingInfo(new SavedAddress(config), "", "creditCard");
		Verify purchasedItems = purchaseConfirmation.verifyProducts(expectedProducts);
		assertTrue(purchaseConfirmation.verifyOnConfirmationPage(), "not on confirmation page.  current url : " + driver.getCurrentUrl());
		assertTrue(purchaseTotals.isVerified(), purchaseTotals.getError());
		assertTrue(purchaseBillingInfo.isVerified(), purchaseBillingInfo.getError());
		assertTrue(purchasedItems.isVerified(), purchasedItems.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifyMultipleItemsCreditCard(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.purchaseUserEmail"), config.getString("daigou.purchaseUserPassword"));

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		expectedAddresses.add(new SavedAddress(config));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		expectedProducts.add(new ProductInfo(storeName2, productName2, "", "1", price2));
		expectedProducts.add(new ProductInfo(storeName, productName, "", "1", price));

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		selectStoreAndEnterUrl(driver, storeName2, productUrl2, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		PurchasePreviewCartSection previewCart = new PurchasePreviewCartSection(driver);

		String cartTotal = previewCart.getTotal();
		String cartShipping = previewCart.getShipping();
		String cartTax = previewCart.getTax();
		String cartCashback = previewCart.getDiscount();

		PurchasePreviewPage previewPage = new PurchasePreviewPage(driver);
		previewPage.selectPaymentMethod("creditCard");


		previewPage.addNewCreditCard(config.getString("usc.cc.cardHolderName"), config.getString("usc.cc.cardNumber"), config.getString("usc.cc.exp.month"),
				config.getInt("usc.cc.exp.year"), config.getString("usc.cc.cv2"), config.getBoolean("usc.cc.isDefault"), null);
		// check that there weren't any errors
		assertTrue(previewPage.verifyNoCreditCardErrors(), "error encountered when adding creditCard.");
		// check that card was added
		previewPage.waitForSaveCardSpinner();

		SavedCreditCard firstCard = previewPage.getFirstCreditCard();
		assertTrue(firstCard.getType() == CreditCardType.VISA, "Expected credit card type to be VISA but found : " + firstCard.getType());
		assertTrue(firstCard.getMaskedNumber().equals("***4242"), "Expected credit card masked # to be ***4242 but found : " + firstCard.getMaskedNumber());

		cartSummary.clickPurchaseBtn();

		previewPage.waitForPurchaseProcessingBar();

		PurchaseConfirmationPage purchaseConfirmation = new PurchaseConfirmationPage(driver);

		Collections.sort(expectedProducts, new ProductComparator());

		Verify purchaseTotals = purchaseConfirmation.verifyTotals(cartTotal, cartShipping, cartTax, cartCashback);
		Verify purchaseBillingInfo = purchaseConfirmation.verifyBillingInfo(new SavedAddress(config), "", "creditCard");
		Verify purchasedItems = purchaseConfirmation.verifyProducts(expectedProducts);
		assertTrue(purchaseConfirmation.verifyOnConfirmationPage(), "not on confirmation page.  current url : " + driver.getCurrentUrl());
		assertTrue(purchaseTotals.isVerified(), purchaseTotals.getError());
		assertTrue(purchaseBillingInfo.isVerified(), purchaseBillingInfo.getError());
		assertTrue(purchasedItems.isVerified(), purchasedItems.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifyOneItemAlipayWithCoupon(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.purchaseUserEmail"), config.getString("daigou.purchaseUserPassword"));

		String couponCode = config.getString("daigou.coupon.code");
		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		expectedAddresses.add(new SavedAddress(config));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		expectedProducts.add(new ProductInfo(storeName4, productName4, "", "1", price4));

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName4, productUrl4, false);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		PurchasePreviewPage previewPage = new PurchasePreviewPage(driver);
		PurchasePreviewCartSection previewCart = new PurchasePreviewCartSection(driver);

		previewPage.enterCoupon(couponCode);

		String cartTotal = previewCart.getTotal();
		String cartShipping = previewCart.getShipping();
		String cartTax = previewCart.getTax();
		String cartCashback = previewCart.getDiscount();

		PurchasePreviewPage purchasePreview = new PurchasePreviewPage(driver);
		purchasePreview.selectPaymentMethod("alipay");
		cartSummary.clickPurchaseBtn();
		// will transfer to alipay but we are cheating with the mocky way
		cartSummary.goToURL(getSugouMockUrl());
		purchasePreview.waitForRedirectSpinner();

		PurchaseConfirmationPage purchaseConfirmation = new PurchaseConfirmationPage(driver);

		Verify purchaseTotals = purchaseConfirmation.verifyTotals(cartTotal, cartShipping, cartTax, cartCashback);
		Verify purchaseBillingInfo = purchaseConfirmation.verifyBillingInfo(new SavedAddress(config), "", "alipay");
		Verify purchasedItems = purchaseConfirmation.verifyProducts(expectedProducts);
		assertTrue(purchaseConfirmation.verifyOnConfirmationPage(), "not on confirmation page.  current url : " + driver.getCurrentUrl());
		assertTrue(purchaseTotals.isVerified(), purchaseTotals.getError());
		assertTrue(purchaseBillingInfo.isVerified(), purchaseBillingInfo.getError());
		assertTrue(purchasedItems.isVerified(), purchasedItems.getError());
	}

	@Override
	@BeforeMethod
	public void cleanup() {
		super.cleanup(config.getString("daigou.purchaseUserEmail"), config.getString("daigou.purchaseUserPassword"));
	}
}
