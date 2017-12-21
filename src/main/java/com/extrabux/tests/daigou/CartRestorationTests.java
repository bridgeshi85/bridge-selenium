package com.extrabux.tests.daigou;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.extrabux.pages.daigou.PurchasePreviewItemListSection;
import com.extrabux.pages.daigou.PurchasePreviewPage;
import com.extrabux.pages.daigou.SavedAddress;
import com.extrabux.pages.daigou.SavedCreditCard;
import com.extrabux.pages.daigou.Verify;


public class CartRestorationTests extends DaigouBaseTest {
	private static final Log LOG = LogFactory.getLog(CartRestorationTests.class);

	String productUrl = "http://www.homedepot.com/p/Estwing-12-oz-Deadhead-Rubber-Mallet-DH-12/100145263";
	String storeName = "The Home Depot";
	String productName = "12 oz. Deadhead Rubber Mallet";
	String price = "$11.68";
	String productUrl2 = "http://usc-magento.extrabux.net/angora-rabbit.html";
	String storeName2 = "USC Magento";
	String productName2 = "Angora Rabbit";
	String price2 = "$1.99";

	@Test(dataProvider = "getWebDriver")
	public void restoreOneItem(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.purchaseUserEmail"), config.getString("daigou.purchaseUserPassword"));

		List<SavedAddress> expectedAddresses = new ArrayList<SavedAddress>();
		expectedAddresses.add(new SavedAddress(config));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		expectedProducts.add(new ProductInfo(storeName, productName, "", "1", price));

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		addOneSearchResultItemToCart(driver);

		String cartSubtotal = cartSummary.getCartSubtotal().replace("$", "");
		String cartCashback = cartSummary.getCartCashback().replace("$", "");

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		String cartId = getCartId(driver);

		cartSummary.waitForProgressBar();

		PurchasePreviewPage previewPage = new PurchasePreviewPage(driver);
		PurchasePreviewCartSection previewCart = new PurchasePreviewCartSection(driver);
		PurchasePreviewItemListSection previewCartItems = new PurchasePreviewItemListSection(driver);

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
		String ffsId = purchaseConfirmation.getId();

		restoreCart(driver, cartId, ffsId);

		Collections.sort(expectedProducts, new ProductComparator());

		Verify verifyCart = previewCart.verifyPreviewCartInfo(cartSubtotal, cartCashback);
		Verify verifyItems = previewCartItems.verifyProducts(expectedProducts);
		assertTrue(verifyCart.isVerified(), verifyCart.getError());
		assertTrue(verifyItems.isVerified(), verifyItems.getError());
	}
//
//	@Test(dataProvider = "getWebDriver")
//	public void restoreValidPurchase(WebDriver driver) throws Exception {
//	}
//
//	@Test(dataProvider = "getWebDriver")
//	public void restoreNonMatchingIds(WebDriver driver) throws Exception {
//	}
//
//	@Test(dataProvider = "getWebDriver")
//	public void restoreNoIds(WebDriver driver) throws Exception {
//	}

	@Override
	@BeforeMethod
	public void cleanup() {
		LOG.info("Cleanup.");
		super.cleanup(config.getString("daigou.purchaseUserEmail"), config.getString("daigou.purchaseUserPassword"));
		LOG.info("/Cleanup.");
	}
}
