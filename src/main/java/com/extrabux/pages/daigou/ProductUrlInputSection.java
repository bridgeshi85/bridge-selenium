package com.extrabux.pages.daigou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class ProductUrlInputSection extends BasePage {
	final String LOADER_CLASS_NAME = "loader-box";
	By spinnerLocator = By.className(LOADER_CLASS_NAME);
	final String FOR_ATTRIBUTE = "for";

	WebElement searchForm;
	public WebElement store;
	public WebElement productUrl;
	@FindBy (className = "has-validation-error")
	public List<WebElement> hasErrorElements;
	@FindBy (className = "text-danger")
	List<WebElement> errors;

	public ProductUrlInputSection(WebDriver driver) {
		super(driver);
	}

	public void selectStoreFromDropDown(String storeName) {
		Select storeSelect = new Select(store);
		storeSelect.selectByVisibleText(storeName);
	}

	public void typeProductUrl(String productUrlString) {
		productUrl.clear();
		productUrl.sendKeys(productUrlString);
	}

	public void clickSubmit() {
		searchForm.submit();
	}

	public boolean verifyErrorsOnPage(List<String> expectedErrors) {
		// first assert the number of errors found on the page is as expected
		List<String> errorsOnPage = getErrors();
		if (!(errorsOnPage.size() == expectedErrors.size())) {
			System.err.print("sizes incorrect");
			return false;
		}
		if (!errorsOnPage.equals(expectedErrors)) {
			System.err.print("Errors different");
			return false;
		}
		return true;
	}

	public List<String> getErrors() {
		List<String> errorsOnPage  = new ArrayList<String>();
		for (WebElement error : errors) {
			if (error.isDisplayed()) {
				errorsOnPage.add(error.getText().replaceAll("\\s+",""));
			}
		}
		return errorsOnPage;
	}

	public boolean verifyHighlightedFields(List<String> expectedFieldIds) {
		if (expectedFieldIds.equals(Arrays.asList(new String[]{""}))) {
			expectedFieldIds = null;
		}
		return getHighlightedFieldIds().equals(expectedFieldIds);
	}

	public List<String> getHighlightedFieldIds() {
		List<String> actualIdsHighlighted = new ArrayList<String>();

		for (WebElement error : hasErrorElements) {
			String highlightedField = error.findElement(By.xpath("label")).getAttribute(FOR_ATTRIBUTE);
			if (StringUtils.isNotBlank(highlightedField)) {
				actualIdsHighlighted.add(highlightedField);
			}
		}
		return actualIdsHighlighted;
	}

	public boolean verifySelectedStore(String storeName) {
		return storeName.equals(new Select(store).getFirstSelectedOption().getText());
	}

	public String getSelectedStore() {
		return new Select(store).getFirstSelectedOption().getText();
	}

	public List<String> getActiveStoresList() {
		List<String> enabledStores = new ArrayList<String>();
		List<WebElement> storeList = new Select(store).getOptions();

		for(WebElement store:storeList) {
		      enabledStores.add(store.getText());
		}

		enabledStores.remove(0); // remove the "select store" text

		Collections.sort(enabledStores);

		return enabledStores;
	}

}
