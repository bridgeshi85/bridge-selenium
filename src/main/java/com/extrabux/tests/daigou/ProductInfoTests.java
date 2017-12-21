package com.extrabux.tests.daigou;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.extrabux.pages.daigou.CartSummarySection;
import com.extrabux.pages.daigou.ProductInfo;
import com.extrabux.pages.daigou.ProductInfoSection;
import com.extrabux.pages.daigou.Verify;


public class ProductInfoTests extends DaigouBaseTest {
	String productUrl = "http://usc-magento.extrabux.net/banana.html";
	String storeName = "USC Magento";
	String productName = "Banana";
	String price = "0.01";

	@Test(dataProvider = "getWebDriver")
	public void getProductInfo(WebDriver driver) {
		login(driver, config.getString("daigou.productInfoUserEmail"), config.getString("daigou.productInfoUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);
		ProductInfoSection productInfo = new ProductInfoSection(driver);

		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl, false);

		productInfo.waitForProductInfoSpinner();

		Verify verify = productInfo.verifyProductInfo(storeName, productName, price);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void invalidProduct(WebDriver driver) {
		login(driver, config.getString("daigou.productInfoUserEmail"), config.getString("daigou.productInfoUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);
		ProductInfoSection productInfo = new ProductInfoSection(driver);
		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl.replace("banana", "orange"), false);

		productInfo.waitForProductInfoSpinner();

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.productInfo.error.notFound"));

		assertTrue(productInfo.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + productInfo.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void outOfStock(WebDriver driver) {
		login(driver, config.getString("daigou.productInfoUserEmail"), config.getString("daigou.productInfoUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);
		ProductInfoSection productInfo = new ProductInfoSection(driver);
		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, config.getString("daigou.product1.outOfStockUrl"), false);

		productInfo.waitForProductInfoSpinner();

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.productInfo.error.outOfStock"));

		assertTrue(productInfo.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + productInfo.getErrors());
	}

	@Override
	@BeforeMethod
	public void cleanup() {
		clearCartCleanup(config.getString("daigou.productInfoUserEmail"), config.getString("daigou.productInfoUserPassword"));
	}

}
