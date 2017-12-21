package com.extrabux.tests.daigou;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.extrabux.pages.daigou.CartSummarySection;
import com.extrabux.pages.daigou.ProductUrlInputSection;


public class ProductUrlInputTests extends DaigouBaseTest {
	String productUrl = "http://usc-magento.extrabux.net/banana.html";
	String storeName = "USC Magento";
	String productName = "Banana";
	String price = "0.01";

	@Test(dataProvider = "getWebDriver")
	public void emptyFields(WebDriver driver) {
		login(driver, config.getString("daigou.productUrlInputUserEmail"), config.getString("daigou.productUrlInputPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);
		cartSummary.waitForCartSpinnerNotPresent();

		ProductUrlInputSection productUrlInput = new ProductUrlInputSection(driver);
		productUrlInput.clickSubmit();

		List<String> expectedErrors2 = Arrays.asList(config.getStringArray("daigou.productInfo.error.emptyForm"));
		List<String> expectedErrors = new ArrayList<String>();
		for (String error : expectedErrors2) {
			if (expectedErrors2.indexOf(error) == 0) {
				expectedErrors.add(error.substring(1));
			} else {
				expectedErrors.add(error);
			}
		}
		List<String> expectedHighlightedFields = Arrays.asList(config.getStringArray("daigou.productInfo.emptyForm.errorFields"));

		assertTrue(productUrlInput.verifyHighlightedFields(expectedHighlightedFields), "errors fields mismatch.  expected: " + expectedHighlightedFields
				+ " actual: " + productUrlInput.getHighlightedFieldIds());
		assertTrue(productUrlInput.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + productUrlInput.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void invalidUrl(WebDriver driver) {
		login(driver, config.getString("daigou.productUrlInputUserEmail"), config.getString("daigou.productUrlInputPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);
		cartSummary.waitForCartSpinnerNotPresent();

		selectStoreAndEnterUrl(driver, storeName, productUrl.replace("http://", ""), false);
		ProductUrlInputSection productUrlInput = new ProductUrlInputSection(driver);
		productUrlInput.selectStoreFromDropDown(storeName);
		productUrlInput.typeProductUrl(productUrl.replace("http://", ""));
		productUrlInput.clickSubmit();

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.productInfo.error.invalidUrl"));
		List<String> expectedHighlightedFields = Arrays.asList(config.getStringArray("daigou.productInfo.invalidUrl.errorFields"));

		assertTrue(productUrlInput.verifyHighlightedFields(expectedHighlightedFields), "errors fields mismatch.  expected: " + expectedHighlightedFields
				+ " actual: " + productUrlInput.getHighlightedFieldIds());
		assertTrue(productUrlInput.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + productUrlInput.getErrors());
	}

	@Test(dataProvider = "getWebDriver")
	public void verifyAutomaticStoreSelection(WebDriver driver) {
		login(driver, config.getString("daigou.productUrlInputUserEmail"), config.getString("daigou.productUrlInputPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);
		cartSummary.waitForCartSpinnerNotPresent();

		ProductUrlInputSection productUrlInput = new ProductUrlInputSection(driver);
		productUrlInput.typeProductUrl(productUrl);
		productUrlInput.clickSubmit();

		assertTrue(productUrlInput.verifySelectedStore(storeName), "Store name mismatch.  expected: " + storeName
				+ " actual: " + productUrlInput.getSelectedStore());
	}

	@Test(dataProvider = "getWebDriver")
	public void disabledStore(WebDriver driver) throws Exception {
		login(driver, config.getString("daigou.productUrlInputUserEmail"), config.getString("daigou.productUrlInputPassword"));

		ProductUrlInputSection productUrlInput = new ProductUrlInputSection(driver);

		List<String> enabledStores = productUrlInput.getActiveStoresList();
		List<String> stores = GetStoresList.getEnabledStoreList();

		assertTrue(enabledStores.equals(stores));
	}

	@Override
	@BeforeMethod
	public void cleanup() {
		clearCartCleanup(config.getString("daigou.productUrlInputUserEmail"), config.getString("daigou.productUrlInputPassword"));
	}

}
