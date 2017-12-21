package com.extrabux.pages.daigou;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.extrabux.util.WebDriverUtil;

public class ProductInfoSection extends BasePage {
	@FindBy(className = "product-info")
	WebElement productInfo;
	@FindBy(className = "product-image")
	WebElement productImage;
	@FindBy(className = "product-title")
	WebElement productTitle;
	@FindBy(className = "product-price")
	WebElement productPrice;
	@FindBy(className = "col-subtotal")
	WebElement subTotalColumn;
	WebElement quantity;
	WebElement prodInfoAddToCartBtn;
	@FindBy (className = "not-found")
	List<WebElement> errorsNotFound;
	@FindBy (className = "text-danger")
	List<WebElement> errorsDifferentStore;
	@FindBy (className = "alert")
	List<WebElement> errors;

	By spinnerLocator = By.id("productInfoSpinner");
	By reviewSpinnerLocator = By.id("reviewsSpinner");
	By cashbackSpinnerLocator = By.id("cashbackSpinner");

	public ProductInfoSection(WebDriver driver) {
		super(driver);
	}

	public void selectQuantity(int quantityToSelect) {
		Select quantitySelect = new Select(quantity);
		quantitySelect.selectByVisibleText(String.valueOf(quantityToSelect));
	}

	public Verify verifyProductInfo(String storeName, String productName, String price) {
		Verify verify = new Verify();
		String error = "";
		String actualStoreName = getStoreNameOnPage();
		String actualProductName = getProductNameOnPage();
		String actualProductPrice = getProductPriceOnPage();
		if (!verifyStoreName(storeName)) {
			error += "store name did not match.  expected " + storeName + " but found " + actualStoreName;
		}
		if (!verifyProductName(productName)) {
			error += "product name did not match.  expected " + productName + " but found " + actualProductName;
		}
		if (!verifyPrice(price)) {
			error += "product price did not match.  expected " + price + " but found " + actualProductPrice;
		}
		verify.setError(error);
		verify.setVerified(verifyStoreName(storeName) && verifyProductName(productName) && verifyPrice(price));
		return verify;
	}

	public String getStoreNameOnPage() {
		return productInfo.findElement(By.className("store")).getText();
	}

	public String getProductNameOnPage() {
		return productTitle.getText();
	}

	public String getProductPriceOnPage() {
		return productPrice.getText().replace("$", "");
	}

	public boolean verifyStoreName(String storeName) {
		return getStoreNameOnPage().contains(storeName);
	}

	public boolean verifyProductName(String productName) {
		return getProductNameOnPage().equals(productName);
	}

	public boolean verifyPrice(String price) {
		return getProductPriceOnPage().equals(price);
	}

	public boolean verifySubtotalPrice(String subTotalPrice) {
		String subTotal = subTotalColumn.findElement(By.className("subtotal")).getText().replace("$", "");
		return subTotal.equals(subTotalPrice);
	}

	public void addToCart() {
		prodInfoAddToCartBtn.click();
	}

	public void waitForProductInfoSpinner() {
		WebDriverUtil.waitForElementPresent(driver, spinnerLocator, 30);
		WebDriverUtil.waitForElementNotVisible(driver, spinnerLocator, 30);
	}

	public void waitForCashbackSpinner() {
		WebDriverUtil.waitForElementNotVisible(driver, cashbackSpinnerLocator, 10);
	}

	public void waitForReviewsSpinner() {
		WebDriverUtil.waitForElementNotVisible(driver, reviewSpinnerLocator, 10);
	}

	public void waitForAddToCartButtonVisible() {
		if(!(driver.findElements(By.id("addToCartBtn")).size() > 0)){
			WebDriverUtil.waitForElementVisible(driver, prodInfoAddToCartBtn, 10);
		}
	}

	public void waitForAddToCartButtonClickable() {
		WebDriverUtil.waitForElementClickable(driver, prodInfoAddToCartBtn, 5);
	}

	public boolean verifyErrorsOnPage(List<String> expectedErrors) {
		// first assert the number of errors found on the page is as expected
		List<String> errorsOnPage = getErrors();
		if (!(errorsOnPage.size() == expectedErrors.size())) {
			return false;
		}
		if (!errorsOnPage.equals(expectedErrors)) {
			return false;
		}
		return true;
	}

	public void selectProductOptions(int ... optionsIndexes) throws InterruptedException {
		WebDriverUtil.waitForElementVisible(driver, productImage, 5);
		waitForNextOptionSelectToLoad(0);
		for (int i = 0; i < optionsIndexes.length; i++) {
			Select selectOption = new Select(driver.findElement(By.id("prodInfoOption" + i)));
			selectOption.selectByValue(Integer.toString(optionsIndexes[i]));
			if ((i + 1) < optionsIndexes.length) {
				waitForNextOptionSelectToLoad(i + 1);
			}
		}
		waitForAddToCartButtonClickable();
	}

	public void waitForNextOptionSelectToLoad(int optionIndex) {
		WebDriverUtil.waitForElementPresent(driver, By.id("prodInfoOption" + optionIndex), 10);
		WebDriverUtil.waitForElementVisible(driver, driver.findElement(By.id("prodInfoOption" + optionIndex)), 10);
	}

	public List<String> getErrors() {
		List<String> errorsOnPage  = new ArrayList<String>();
		for (WebElement error : errors) {
			if (error.isDisplayed()) {
				errorsOnPage.add(error.getText());
			}
		}
		for (WebElement error : errorsNotFound) {
			if (error.isDisplayed()) {
				errorsOnPage.add(error.getText());
			}
		}
		for (WebElement error : errorsDifferentStore) {
			if (error.isDisplayed()) {
				errorsOnPage.add(error.getText());
			}
		}
		return errorsOnPage;
	}

}
