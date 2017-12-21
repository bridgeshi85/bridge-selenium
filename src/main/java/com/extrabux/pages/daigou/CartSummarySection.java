package com.extrabux.pages.daigou;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class CartSummarySection extends BasePage {
	private static final Log LOG = LogFactory.getLog(CartSummarySection.class);

	final String PROGRESS_BAR_ELEMENT_ID = "progress-bar";

	@FindBy(className = "item-list-container")
	WebElement itemListContainer;
	WebElement clearCart;
	@FindBy(id = "total-items")
	WebElement totalItems;
	WebElement cartSubtotal;
	WebElement cartCashback;
	WebElement subTotalCart;
	@FindBy(className = "item")
	List<WebElement> cartItems;
	WebElement checkoutSummaryBtn;
	WebElement purchasePreviewBtn;
	WebElement purchaseBtn;
	@FindBy(id = PROGRESS_BAR_ELEMENT_ID)
	WebElement progressBar;

	By purchaseBtnLocator = By.id("purchaseBtn");

	public CartSummarySection(WebDriver driver) {
		super(driver);
	}
	
	public boolean isClearCartBtnVisible() {
		WebDriverUtil.waitForElementPresent(driver, By.id("clearCart"), 5);
		return clearCart.isDisplayed();
	}

	public void clickClearCart() {
		WebDriverUtil.waitForElementClickable(driver, clearCart, 5);
		try {
			clearCart.click();
			WebDriverUtil.waitForElementNotVisible(driver, By.id("clearCart"), 5);
		}
		catch (TimeoutException e) {
			// Recursively clear the cart until it actually clears the flipping cart.
			clickClearCart();
		}
	}

	public Verify verifyProducts(List<ProductInfo> products) {
		Verify verified;
		for (ProductInfo product : products) {
			verified = verifyProduct(product, products.indexOf(product));
			if (!verified.isVerified()) {
				return verified;
			}
		}
		return new Verify("", true);
	}

	public Verify verifyProduct(ProductInfo product, int productIndex) {
		boolean storeVerified = true;
		boolean nameVerified = true;
		boolean quantityVerified = true;
		boolean priceVerified = true;
		String error = "";

		if (!verifyProductStoreName(productIndex, product.getStoreName())) {
			storeVerified = false;
			error += "store name did not match.  expected " + product.getStoreName() + " but found " + getItemActualStoreName(productIndex) + ", ";
		}
		if (!verifyProductName(productIndex, product.getProductName())) {
			nameVerified = false;
			error += "product name did not match.  expected " + product.getProductName() + " but found " + getItemActualProductName(productIndex) + ", ";
		}
		if (!verifyProductQuantity(productIndex, product.getQuantity())) {
			quantityVerified = false;
			error += "product quantity did not match.  expected " + product.getQuantity() + " but found " + getItemActualQuantity(productIndex) + ", ";
		}
		if (!verifyProductPrice(productIndex, product.getProductPrice())) {
			priceVerified = false;
			error += "product price did not match.  expected " + product.getProductPrice() + " but found " + getItemActualPrice(productIndex);
		}
		if (!error.isEmpty()) {
			error = "failure on product: " + productIndex + " " + error;
		}
		return new Verify(error, storeVerified && nameVerified && quantityVerified && priceVerified);
	}

	private String getItemActualPrice(int productIndex) {
		return cartItems.get(productIndex).findElement(By.id("price" + productIndex)).getText();
	}

	private String getItemActualQuantity(int productIndex) {
		return cartItems.get(productIndex).findElement(By.id("quantity" + productIndex)).getAttribute("value");
	}

	private String getItemActualProductName(int productIndex) {
		return cartItems.get(productIndex).findElement(By.id("prodTitle" + productIndex)).getText();
	}

	private String getItemActualStoreName(int productIndex) {
		String actualStoreName = cartItems.get(productIndex).findElement(By.id("storeName" + productIndex)).getText();
		Pattern p = Pattern.compile("商家: (.*) 享返利");
		Matcher m = p.matcher(actualStoreName);
		while(m.find()) {
			actualStoreName = m.group(1);
		}
		return actualStoreName;
	}

	public boolean verifyCartInfo(CartSummary cartSummary) {
		return verifyNumberOfItemsInCart(cartSummary.getTotalItems()) && verifyCartSubtotal(cartSummary.getCartSubtotal()) /*&& cashBack*/;
	}

	public boolean verifyNumberOfItemsInCart(String itemNumber) {
		String numberOfItems = getNumberOfItemsInCart();
		if( numberOfItems.equals(itemNumber) ){
			LOG.debug("Number of items is equal");
		} else {
			LOG.debug("Number of items is not equal");
		}
		return numberOfItems.equals(itemNumber);
	}

	public String getNumberOfItemsInCart() {
		String numberOfItems = totalItems.getText().replaceAll("[()]", "");
		LOG.debug("Returning numberOfItems: " + numberOfItems);
		return numberOfItems;
	}

	public boolean verifyCartSubtotal(String subTotalPrice) {
		if( getCartSubtotal().equals(subTotalPrice) ){
			LOG.debug("Cart subtotal is equal");
		} else {
			LOG.debug("Cart subtotal is not equal");
		}
		return getCartSubtotal().equals(subTotalPrice);
	}

	public String getCartSubtotal() {
		LOG.debug("Returning cartSubtotal: " + cartSubtotal.getText());
		return cartSubtotal.getText();
	}

	public boolean verifyCartCashback(String cashback) {
		if( getCartSubtotal().equals(cashback) ){
			LOG.debug("Cart cashback is equal");
		} else {
			LOG.debug("Cart cashback is not equal");
		}
		return getCartSubtotal().equals(cashback);
	}

	public String getCartCashback() {
		LOG.debug("Returning cartCashback: " + cartCashback.getText());
		return cartCashback.getText();
	}

	public boolean verifyProductStoreName(int itemIndex, String productStoreName) {
		if( getItemActualStoreName(itemIndex).equals(productStoreName) ){
			LOG.debug("Store name is equal");
		} else {
			LOG.debug("Store name is not equal");
		}
		return getItemActualStoreName(itemIndex).equals(productStoreName);
	}

	public boolean verifyProductName(int itemIndex, String productName) {
		if( getItemActualProductName(itemIndex).equals(productName) ){
			LOG.debug("Product name is equal");
		} else {
			LOG.debug("Product name is not equal");
		}
		return getItemActualProductName(itemIndex).equals(productName);
	}

	public boolean verifyProductQuantity(int itemIndex, String productQuantity) {
		if( getItemActualQuantity(itemIndex).equals(productQuantity) ){
			LOG.debug("Item quantity is equal");
		} else {
			LOG.debug("Item quantity is not equal");
		}
		return getItemActualQuantity(itemIndex).equals(productQuantity);
	}

	public void increaseProductQuantity(int itemIndex)  {
		LOG.debug("Clicking plus" + itemIndex);
		cartItems.get(itemIndex).findElement(By.id("plus" + itemIndex)).click();
	}

	public void decreaseProductQuantity(int itemIndex) {
		LOG.debug("Clicking minus" + itemIndex );
		cartItems.get(itemIndex).findElement(By.id("minus" + itemIndex)).click();
	}

	public boolean verifyProductOptions(int itemIndex, String productOptions) {
		if( cartItems.get(itemIndex).findElement(By.id("options" + itemIndex)).getText().equals(productOptions) ){
			LOG.debug("options" + itemIndex + " is equal");
		} else {
			LOG.debug("options" + itemIndex + " is not equal");
		}
		return cartItems.get(itemIndex).findElement(By.id("options" + itemIndex)).getText().equals(productOptions);
	}

	public boolean verifyProductPrice(int itemIndex, String productPrice) {
		if( getItemActualPrice(itemIndex).equals(productPrice) ){
			LOG.debug("Product price is equal");
		} else {
			LOG.debug("Product price is not equal");
		}
		return getItemActualPrice(itemIndex).equals(productPrice);
	}

	public void clickEditProductOptions(int itemIndex) {
		LOG.debug("Clicking editOptions" + itemIndex);
		cartItems.get(itemIndex).findElement(By.id("editOptions" + itemIndex)).click();
	}

	public void clickUpdateProductOptions(int itemIndex) {
		LOG.debug("Clicking updateOptionsBtn" + itemIndex);
		cartItems.get(itemIndex).findElement(By.id("updateOptionsBtn" + itemIndex)).click();
	}

	public void clickCancelProductOptions(int itemIndex) {
		LOG.debug("Clicking cancelUpdateBtn" + itemIndex);
		cartItems.get(itemIndex).findElement(By.id("cancelUpdateBtn" + itemIndex)).click();
	}

	public void clickDeleteProduct(int itemIndex) throws InterruptedException {
		Thread.sleep(2000);
		waitForDeleteProductClickable(itemIndex);
		LOG.debug("Clicking delete" + itemIndex);
		cartItems.get(itemIndex).findElement(By.id("delete" + itemIndex)).click();
		WebDriverUtil.waitForElementNotVisible(driver, By.xpath("//*[@id='item" + itemIndex + "']//*[contains(@class, 'removing-modal-content')]"), 10);
	}

	public void clickPurchasePreviewBtn() throws InterruptedException {
		waitForPurchasePreviewBtnClickable();
		LOG.debug("Clicking purchasePreviewBtn");
		purchasePreviewBtn.click();
	}

	public void waitForDeleteProductClickable(int itemIndex) {
		LOG.debug("Waiting for delete" + itemIndex + " to be present");
		WebDriverUtil.waitForElementPresent(driver, By.id("delete" + itemIndex), 10);
		LOG.debug("Waiting for delete" + itemIndex + " to be clickable");
		WebDriverUtil.waitForElementClickable(driver, cartItems.get(itemIndex).findElement(By.id("delete" + itemIndex)), 10);
	}

	public void waitForPurchasePreviewBtnClickable() throws InterruptedException {
		LOG.debug("Waiting for purchasePreviewBtn to be clickable");
		WebDriverUtil.waitForElementClickable(driver, purchasePreviewBtn, 10);
		Thread.sleep(2000);
	}

	public void waitForPurchasePreviewBtnNotClickable() {
		LOG.debug("Checking if purchasePreviewBtn exists");
		if(driver.findElements(By.id("purchasePreviewBtn")).size() > 0){
			LOG.debug("Waiting for purchasePreviewBtn to not be visible");
			WebDriverUtil.waitForElementNotVisible(driver, By.id("purchasePreviewBtn"), 10);
		}
	}

	public void waitForCheckoutSummaryBtnVisible() {
		LOG.debug("Waiting for the checkoutSummaryBtn to be visible");
		WebDriverUtil.waitForElementVisible(driver, checkoutSummaryBtn, 10);
	}

	public void waitForCheckoutSummaryBtnClickable() {
		LOG.debug("Waiting for the text \"立即下单\" to be present");
		WebDriverUtil.waitForTextPresentInElement(driver, checkoutSummaryBtn, "立即下单", 15);
		LOG.debug("Waiting for the checkoutSummaryBtn to be clickable");
		WebDriverUtil.waitForElementClickable(driver, checkoutSummaryBtn, 90);
	}

	public PurchaseConfirmationPage clickCheckoutSummaryBtn() {
		waitForCheckoutSummaryBtnClickable();
		LOG.debug("Clicking the checkoutSummaryBtn");
		checkoutSummaryBtn.click();

		return new PurchaseConfirmationPage(driver);
	}

	public void waitForPurchaseBtn() {
		LOG.debug("Waiting for purchase button to be present in cart summary section.");
		WebDriverUtil.waitForElementPresent(driver, purchaseBtnLocator, 15);
		LOG.debug("Now waiting for the purchase button to be visible.");
		WebDriverUtil.waitForElementVisible(driver, purchaseBtn, 15);
	}

	public void waitForPurchaseBtnClickable() {
		LOG.debug("Waiting for purchase button to be clickable.");
		WebDriverUtil.waitForElementClickable(driver, purchaseBtn, 90);
	}

	public void clickPurchaseBtn() {
		waitForPurchaseBtnClickable();
		LOG.debug("Clicking the purchaseBtn");
		purchaseBtn.click();
	}

	public void waitForProgressBar() {
		LOG.debug("Waiting for Progress Bar on Cart Summary Section to be present");
		WebDriverUtil.waitForElementPresent(driver, By.id(PROGRESS_BAR_ELEMENT_ID), 15);
		LOG.debug("Waiting for Progress Bar to no longer be visible");
		WebDriverUtil.waitForElementNotVisible(driver, By.id(PROGRESS_BAR_ELEMENT_ID), 120);
	}

	public void waitForCartSpinnerNotPresent() {
		LOG.debug("Wait for the cartLoadingSpinner to not be visible");
		WebDriverUtil.waitForElementNotVisible(driver, By.id("cartLoadingSpinner"), 30);
	}

	public void waitForCartSummarySpinnerNotPresent() {
		LOG.debug("Wait for loading spinner to not be visible");
		WebDriverUtil.waitForElementNotVisible(driver, By.id("loading"), 30);
	}

}
