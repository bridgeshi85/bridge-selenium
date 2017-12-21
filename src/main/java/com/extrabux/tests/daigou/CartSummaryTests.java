package com.extrabux.tests.daigou;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.extrabux.pages.daigou.CartSummarySection;
import com.extrabux.pages.daigou.ManageShippingAddressesPage;
import com.extrabux.pages.daigou.ProductInfo;
import com.extrabux.pages.daigou.ProductInfoSection;
import com.extrabux.pages.daigou.SavedAddress;
import com.extrabux.pages.daigou.Verify;


public class CartSummaryTests extends DaigouBaseTest {
	String productUrl = "http://usc-magento.extrabux.net/banana.html";
	boolean productUrlHasOptions = false;
	String storeName = "USC Magento";
	String productName = "Banana";
	String price = "$0.01";
	String pricex3 = "$0.03";
	String productUrl2 = "http://usc-magento.extrabux.net/angora-rabbit.html";
	boolean productUrl2HasOptions = false;
	String storeName2 = "USC Magento";
	String productName2 = "Angora Rabbit";
	String price2 = "$1.99";
	String productUrl3 = "http://usc-magento2.extrabux.net/fractal-wrongness-poster.html";
	boolean productUrl3HasOptions = true;
	String storeName3 = "USC Magento 2";
	String productName3 = "Fractal Wrongness Poster";
	String price3 = "$6.99";

	@Test(dataProvider = "getWebDriver")
	public void onlyProductsFromOneStore(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);
		ProductInfoSection productInfo = new ProductInfoSection(driver);
		cartSummary.waitForCartSpinnerNotPresent();


		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);

		addOneSearchResultItemToCart(driver);

		selectStoreAndEnterUrl(driver, config.getString("daigou.product2.storeName"), config.getString("daigou.product2.url"), true);

		productInfo.waitForProductInfoSpinner();

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.productInfo.error.onlyOneStore"));

		assertTrue(productInfo.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + productInfo.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifySingleItemInCart(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);

		addOneSearchResultItemToCart(driver);

		// verify products.  for now we are only using one product

		assertTrue(cartSummary.verifyNumberOfItemsInCart("1"), "number of items in cart not as expected.  EXPECTED: 1, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal(price), "cart subtotal not as expected.  EXPECTED: " + price + ", ACTUAL: " + cartSummary.getCartSubtotal());
		// verify cash back amount

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifyMultipleItemsInCart(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		ProductInfo product2 = new ProductInfo(storeName2, productName2, "", "1", price2);
		expectedProducts.add(product1);
		expectedProducts.add(product2);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);

		addOneSearchResultItemToCart(driver);

		selectStoreAndEnterUrl(driver, storeName2, productUrl2, productUrl2HasOptions);

		addOneSearchResultItemToCart(driver);

		Thread.sleep(2000);

		assertTrue(cartSummary.verifyNumberOfItemsInCart("2"), "number of items in cart not as expected.  EXPECTED: 2, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal("$2.00"), "cart subtotal not as expected.  EXPECTED: $2.00, ACTUAL: " + cartSummary.getCartSubtotal());

		// This is to make up for the indexes of the products in the cart being reversed
		Collections.reverse(expectedProducts);

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifyClearedCart(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);

		addOneSearchResultItemToCart(driver);

		clearCart(driver);

		// verify products.  for now we are only using one product

		assertTrue(cartSummary.verifyNumberOfItemsInCart("0"), "number of items in cart not as expected.  EXPECTED: 0, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal("$0.00"), "cart subtotal not as expected.  EXPECTED: $0.00, ACTUAL: " + cartSummary.getCartSubtotal());
		// verify cash back amount

		Verify verify = cartSummary.verifyProducts(new ArrayList<ProductInfo>());
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void clearCartThenAddItemAgain(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);
		addOneSearchResultItemToCart(driver);

		clearCart(driver);

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);
		addOneSearchResultItemToCart(driver);

		assertTrue(cartSummary.verifyNumberOfItemsInCart("1"), "number of items in cart not as expected.  EXPECTED: 1, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal(price), "cart subtotal not as expected.  EXPECTED: " + price + ", ACTUAL: " + cartSummary.getCartSubtotal());

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void clearCartThenAddItemFromAnotherStore(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product2 = new ProductInfo(storeName3, productName3, "", "1", price3);
		expectedProducts.add(product2);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);

		addOneSearchResultItemToCart(driver);

		clearCart(driver);

		selectStoreAndEnterUrl(driver, storeName3, productUrl3, productUrl3HasOptions);

		ProductInfoSection productInfo = new ProductInfoSection(driver);

		productInfo.selectProductOptions(1);

		addOneSearchResultItemToCart(driver);

		assertTrue(cartSummary.verifyNumberOfItemsInCart("1"), "number of items in cart not as expected.  EXPECTED: 1, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal(price3), "cart subtotal not as expected.  EXPECTED: " + price3 + ", ACTUAL: " + cartSummary.getCartSubtotal());

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void removeItemThenAddItAgain(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);
		addOneSearchResultItemToCart(driver);

		cartSummary.waitForPurchasePreviewBtnClickable();

		cartSummary.clickDeleteProduct(0);

		cartSummary.waitForPurchasePreviewBtnNotClickable();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);
		addOneSearchResultItemToCart(driver);

		assertTrue(cartSummary.verifyNumberOfItemsInCart("1"), "number of items in cart not as expected.  EXPECTED: 1, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal(price), "cart subtotal not as expected.  EXPECTED: " + price + ", ACTUAL: " + cartSummary.getCartSubtotal());

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());

	}


	@Test(dataProvider = "getWebDriver")
	public void removeItemThenAddAnotherItemFromSameStore(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName2, productName2, "", "1", price2);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);
		addOneSearchResultItemToCart(driver);

		cartSummary.waitForPurchasePreviewBtnClickable();

		cartSummary.clickDeleteProduct(0);

		cartSummary.waitForPurchasePreviewBtnNotClickable();

		selectStoreAndEnterUrl(driver, storeName2, productUrl2, productUrl2HasOptions);
		addOneSearchResultItemToCart(driver);

		assertTrue(cartSummary.verifyNumberOfItemsInCart("1"), "number of items in cart not as expected.  EXPECTED: 1, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal(price2), "cart subtotal not as expected.  EXPECTED: " + price2 + ", ACTUAL: " + cartSummary.getCartSubtotal());

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void addTwoItemsThenRemoveOne(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName2, productName2, "", "1", price2);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);
		addOneSearchResultItemToCart(driver);

		selectStoreAndEnterUrl(driver, storeName2, productUrl2, productUrl2HasOptions);
		addOneSearchResultItemToCart(driver);

		Thread.sleep(2000);

		cartSummary.clickDeleteProduct(1);

		assertTrue(cartSummary.verifyNumberOfItemsInCart("1"), "number of items in cart not as expected.  EXPECTED: 1, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal(price2), "cart subtotal not as expected.  EXPECTED: " + price2 + ", ACTUAL: " + cartSummary.getCartSubtotal());

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void addSameItemToCartMultipleTimes(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "3", pricex3);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);
		addOneSearchResultItemToCart(driver);
		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);
		addOneSearchResultItemToCart(driver);
		cartSummary.increaseProductQuantity(0);

		assertTrue(cartSummary.verifyNumberOfItemsInCart("3"), "number of items in cart not as expected.  EXPECTED: 3, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal("$0.03"), "cart subtotal not as expected.  EXPECTED: " + pricex3 + ", ACTUAL: " + cartSummary.getCartSubtotal());

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());
	}


	@Test(dataProvider = "getWebDriver")
	public void removeItemThenAddAnItemFromAnotherStore(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product2 = new ProductInfo(storeName3, productName3, "", "1", price3);
		expectedProducts.add(product2);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);

		addOneSearchResultItemToCart(driver);

		cartSummary.waitForPurchasePreviewBtnClickable();

		cartSummary.clickDeleteProduct(0);

		cartSummary.waitForPurchasePreviewBtnNotClickable();

		selectStoreAndEnterUrl(driver, storeName3, productUrl3, productUrl3HasOptions);

		ProductInfoSection productInfo = new ProductInfoSection(driver);

		productInfo.selectProductOptions(1);

		addOneSearchResultItemToCart(driver);

		assertTrue(cartSummary.verifyNumberOfItemsInCart("1"), "number of items in cart not as expected.  EXPECTED: 1, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal(price3), "cart subtotal not as expected.  EXPECTED: " + price3 + ", ACTUAL: " + cartSummary.getCartSubtotal());

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void updateProductOptions(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, "http://usc-magento.extrabux.net/t-shirt-with-options.html", true);

		ProductInfoSection productInfo = new ProductInfoSection(driver);
		productInfo.selectProductOptions(1, 1);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickEditProductOptions(0);
	}

	@Test(dataProvider = "getWebDriver")
	public void goToPurchasePreviewVerifySingleItemInCart(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		driver.get("https://" + sugouServer);
		cartSummary.waitForCartSpinnerNotPresent();

		assertTrue(cartSummary.verifyNumberOfItemsInCart("1"), "number of items in cart not as expected.  EXPECTED: 1, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal(price), "cart subtotal not as expected.  EXPECTED: " + price + ", ACTUAL: " + cartSummary.getCartSubtotal());

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void goToPurchasePreviewVerifyMultipleItemsInCart(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		ProductInfo product2 = new ProductInfo(storeName2, productName2, "", "1", price2);
		expectedProducts.add(product1);
		expectedProducts.add(product2);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		driver.get("https://" + sugouServer);
		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName2, productUrl2, productUrl2HasOptions);

		addOneSearchResultItemToCart(driver);

		Thread.sleep(2000);

		// verify products.  for now we are only using one product

		assertTrue(cartSummary.verifyNumberOfItemsInCart("2"), "number of items in cart not as expected.  EXPECTED: 2, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal("$2.00"), "cart subtotal not as expected.  EXPECTED: $2.00, ACTUAL: " + cartSummary.getCartSubtotal());
		// verify cash back amount

		// This is to make up for the indexes of the prodycts in the cart being reversed
		Collections.reverse(expectedProducts);

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void addItemGoToPurchasePreviewAddSecondItem(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		ProductInfo product2 = new ProductInfo(storeName2, productName2, "", "1", price2);
		expectedProducts.add(product1);
		expectedProducts.add(product2);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		driver.get("https://" + sugouServer);
		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName2, productUrl2, productUrl2HasOptions);

		addOneSearchResultItemToCart(driver);

		// This is to make up for the indexes of the products in the cart being reversed
		Collections.reverse(expectedProducts);

		assertTrue(cartSummary.verifyNumberOfItemsInCart("2"), "number of items in cart not as expected.  EXPECTED: 2, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal("$2.00"), "cart subtotal not as expected.  EXPECTED: $2.00, ACTUAL: " + cartSummary.getCartSubtotal());

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void GoToPurchasePreviewClearCartThenAddItemAgain(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);
		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		driver.get("https://" + sugouServer);
		cartSummary.waitForCartSpinnerNotPresent();

		clearCart(driver);

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);
		addOneSearchResultItemToCart(driver);

		// verify products.  for now we are only using one product

		assertTrue(cartSummary.verifyNumberOfItemsInCart("1"), "number of items in cart not as expected.  EXPECTED: 1, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal(price), "cart subtotal not as expected.  EXPECTED: " + price + ", ACTUAL: " + cartSummary.getCartSubtotal());
		// verify cash back amount

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void goToPurchasePreviewClearCartThenAddItemFromAnotherStore(WebDriver driver) throws InterruptedException {
		login(driver, config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product2 = new ProductInfo(storeName3, productName3, "", "1", price3);
		expectedProducts.add(product2);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, productUrlHasOptions);

		addOneSearchResultItemToCart(driver);

		cartSummary.clickPurchasePreviewBtn();

		getToShippingAddressesPage(driver);

		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);

		enterShippingAddressInfo(driver, shippingAddressPage, new SavedAddress(config));

		shippingAddressPage.waitForAddressSaveSuccessful();

		waitForPurchasePreviewPage(driver);

		driver.get("https://" + sugouServer);
		cartSummary.waitForCartSpinnerNotPresent();

		clearCart(driver);

		selectStoreAndEnterUrl(driver, storeName3, productUrl3, productUrl3HasOptions);

		ProductInfoSection productInfo = new ProductInfoSection(driver);

		productInfo.selectProductOptions(1);

		addOneSearchResultItemToCart(driver);

		// verify products.  for now we are only using one product

		assertTrue(cartSummary.verifyNumberOfItemsInCart("1"), "number of items in cart not as expected.  EXPECTED: 1, ACTUAL: " + cartSummary.getNumberOfItemsInCart());
		assertTrue(cartSummary.verifyCartSubtotal(price3), "cart subtotal not as expected.  EXPECTED: " + price3 + ", ACTUAL: " + cartSummary.getCartSubtotal());
		// verify cash back amount

		Verify verify = cartSummary.verifyProducts(expectedProducts);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Override
	@BeforeMethod
	public void cleanup() {
		clearAllSavedAddressesCleanup(config.getString("daigou.cartSummaryUserEmail"), config.getString("daigou.cartSummaryUserPassword"));
	}

}
