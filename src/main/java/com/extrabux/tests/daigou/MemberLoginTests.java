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
import com.extrabux.pages.daigou.MemberLoginSection;
import com.extrabux.pages.daigou.ProductInfo;
import com.extrabux.pages.daigou.ProductUrlInputSection;
import com.extrabux.pages.daigou.PurchasePreviewCartSection;
import com.extrabux.pages.daigou.PurchasePreviewItemListSection;
import com.extrabux.pages.daigou.PurchasePreviewPage;
import com.extrabux.pages.daigou.SavedAddress;
import com.extrabux.pages.daigou.Verify;

public class MemberLoginTests extends DaigouBaseTest {
	//private static final Log LOG = LogFactory.getLog(MemberLoginTests.class);
	String productUrl;
	String storeName;
	String productName;
	String price;

	@Test(dataProvider = "getWebDriver")
	public void emptyLoginFields(WebDriver driver) {
		productUrl = config.getString("daigou.product.drugstore.url");
		storeName = config.getString("daigou.product.drugstore.storeName");
		productName = config.getString("daigou.product.drugstore.productName");
		price = config.getString("daigou.product.drugstore.price");

		login(driver, config.getString("daigou.memberLoginUserEmail"), config.getString("daigou.memberLoginUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);
		cartSummary.waitForCartSpinnerNotPresent();

		ProductUrlInputSection productUrlInput = new ProductUrlInputSection(driver);
		productUrlInput.selectStoreFromDropDown(storeName);

		MemberLoginSection memberLogin = new MemberLoginSection(driver);

		memberLogin.login("", "");

		List<String> expectedErrors2 = Arrays.asList(config.getStringArray("daigou.drugstore.error.emptyForm"));
		List<String> expectedErrors = new ArrayList<String>();
		for (String error : expectedErrors2) {
			expectedErrors.add(error);
		}

		assertTrue(memberLogin.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + memberLogin.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void missingEmail(WebDriver driver) {
		productUrl = config.getString("daigou.product.drugstore.url");
		storeName = config.getString("daigou.product.drugstore.storeName");
		productName = config.getString("daigou.product.drugstore.productName");
		price = config.getString("daigou.product.drugstore.price");

		login(driver, config.getString("daigou.memberLoginUserEmail"), config.getString("daigou.memberLoginUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);
		cartSummary.waitForCartSpinnerNotPresent();

		ProductUrlInputSection productUrlInput = new ProductUrlInputSection(driver);
		productUrlInput.selectStoreFromDropDown(storeName);

		MemberLoginSection memberLogin = new MemberLoginSection(driver);

		memberLogin.login("", config.getString("daigou.drugstore.password"));

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.drugstore.error.missingUsername"));

		assertTrue(memberLogin.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + memberLogin.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void missingPassword(WebDriver driver) {
		productUrl = config.getString("daigou.product.drugstore.url");
		storeName = config.getString("daigou.product.drugstore.storeName");
		productName = config.getString("daigou.product.drugstore.productName");
		price = config.getString("daigou.product.drugstore.price");

		login(driver, config.getString("daigou.memberLoginUserEmail"), config.getString("daigou.memberLoginUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);
		cartSummary.waitForCartSpinnerNotPresent();

		ProductUrlInputSection productUrlInput = new ProductUrlInputSection(driver);
		productUrlInput.selectStoreFromDropDown(storeName);

		MemberLoginSection memberLogin = new MemberLoginSection(driver);

		memberLogin.login(config.getString("daigou.drugstore.username"), "");

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.drugstore.error.missingPassword"));

		assertTrue(memberLogin.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + memberLogin.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void validLogin(WebDriver driver) throws InterruptedException {
		productUrl = config.getString("daigou.product.drugstore.url");
		storeName = config.getString("daigou.product.drugstore.storeName");
		productName = config.getString("daigou.product.drugstore.productName");
		price = config.getString("daigou.product.drugstore.price");

		login(driver, config.getString("daigou.memberLoginUserEmail"), config.getString("daigou.memberLoginUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);
		cartSummary.waitForCartSpinnerNotPresent();

		ProductUrlInputSection productUrlInput = new ProductUrlInputSection(driver);
		productUrlInput.selectStoreFromDropDown(storeName);

		MemberLoginSection memberLogin = new MemberLoginSection(driver);

		memberLogin.login(config.getString("daigou.drugstore.username"), config.getString("daigou.drugstore.password"));

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

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

		PurchasePreviewPage previewPage = new PurchasePreviewPage(driver);
		PurchasePreviewCartSection previewCart = new PurchasePreviewCartSection(driver);
		PurchasePreviewItemListSection previewCartItems = new PurchasePreviewItemListSection(driver);

		previewPage.verifyShippingAddressPresent();

		// verify cart
		Verify verifyCart = previewCart.verifyPreviewCartInfo(cartSubtotal, cartCashback);
		Verify verifyItems = previewCartItems.verifyProducts(expectedProducts);
		assertTrue(verifyCart.isVerified(), verifyCart.getError());
		assertTrue(verifyItems.isVerified(), verifyItems.getError());

		// clear the product from the cart on the Drugstore side
		driver.get(getSugouUrl());
		cartSummary.waitForCartSpinnerNotPresent();
		cartSummary.clickDeleteProduct(0);
		cartSummary.waitForPurchasePreviewBtnNotClickable();
	}

	@Override
	@BeforeMethod
	protected void cleanup() {
		clearDrugstoreCleanup(config.getString("daigou.memberLoginUserEmail"), config.getString("daigou.memberLoginUserPassword"));
	}

}
