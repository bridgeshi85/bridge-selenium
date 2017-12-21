package com.extrabux.pages.daigou;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PurchasePreviewItemListSection extends BasePage {
	//private static final Log LOG = LogFactory.getLog(PurchasePreviewItemListSection.class);
	WebElement totalItems;
	@FindBy(className = "item-list-container")
	WebElement itemListContainer;
	@FindBy(className = "item")
	List<WebElement> cartItems;

	By purchaseBtnLocator = By.id("purchaseBtn");

	public PurchasePreviewItemListSection(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

//	TODO: Pull this chunk into a base page so it can be shared with Purchase Tests

	public Verify verifyProducts(List<ProductInfo> products) {
		Verify verified;
		Collections.sort(products, new ProductComparator());

		for (ProductInfo product : products) {
			System.err.println(product);
			System.err.println(products.indexOf(product));
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
		if (!verifyProductPrice(productIndex, product.getProductPrice().replace("$", ""))) {
			priceVerified = false;
			error += "product price did not match.  expected " + product.getProductPrice() + " but found " + getItemActualPrice(productIndex);
		}
		if (!error.isEmpty()) {
			error = "failure on product: " + productIndex + " " + error;
		}
		return new Verify(error, storeVerified && nameVerified && quantityVerified && priceVerified);
	}

	public Verify verifyProduct(ProductInfo product, ProductInfo product2, int productIndex) {
		boolean storeVerified = true;
		boolean nameVerified = true;
		boolean quantityVerified = true;
		boolean priceVerified = true;
		String error = "";

		if (!product.getStoreName().equals(product2.getStoreName())) {
			storeVerified = false;
			error += "store name did not match.  expected " + product.getStoreName() + " but found " + product2.getStoreName() + ", ";
		}
		if (!product.getProductName().equals(product2.getProductName())) {
			nameVerified = false;
			error += "product name did not match.  expected " + product.getProductName() + " but found " + product2.getProductName() + ", ";
		}
		if (!product.getQuantity().equals(product2.getQuantity())) {
			quantityVerified = false;
			error += "product quantity did not match.  expected " + product.getQuantity() + " but found " + product2.getQuantity() + ", ";
		}
		if (!product.getProductPrice().replace("$", "").equals(product2.getProductPrice().replace("$", ""))) {
			priceVerified = false;
			error += "product price did not match.  expected " + product.getProductPrice().replace("$", "") + " but found " + product2.getProductPrice().replace("$", "");
		}
		if (!error.isEmpty()) {
			error = "failure on product: " + productIndex + " " + error;
		}
		return new Verify(error, storeVerified && nameVerified && quantityVerified && priceVerified);
	}

	private String getItemActualPrice(int productIndex) {
		return cartItems.get(productIndex).findElement(By.id("price" + productIndex)).getText().replace("$", "");
	}

	private String getItemActualQuantity(int productIndex) {
		return cartItems.get(productIndex).findElement(By.id("quantity" + productIndex)).getText();
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

	public boolean verifyNumberOfItemsInCart(String itemNumber) {
		String numberOfItems = getNumberOfItemsInCart();
		return numberOfItems.equals(itemNumber);
	}

	public String getNumberOfItemsInCart() {
		String numberOfItems = totalItems.getText().replaceAll("[()]", "");
		return numberOfItems;
	}

	public boolean verifyProductStoreName(int itemIndex, String productStoreName) {
		return getItemActualStoreName(itemIndex).equals(productStoreName);
	}

	public boolean verifyProductName(int itemIndex, String productName) {
		return getItemActualProductName(itemIndex).contains(productName);
	}

	public boolean verifyProductQuantity(int itemIndex, String productQuantity) {
		return getItemActualQuantity(itemIndex).equals(productQuantity);
	}

	public boolean verifyProductOptions(int itemIndex, String productOptions) {
		return cartItems.get(itemIndex).findElement(By.id("options" + itemIndex)).getText().equals(productOptions);
	}

	public boolean verifyProductPrice(int itemIndex, String productPrice) {
		return getItemActualPrice(itemIndex).equals(productPrice);
	}

//	private List<ProductInfo> getProductList(int itemCount) {
//		List<ProductInfo> productList = new ArrayList<ProductInfo>();
//		for( int i = 0; i < itemCount; i++){
//			try {
//				productList.add(new ProductInfo(getItemActualStoreName(i), getItemActualProductName(i), "", getItemActualQuantity(i), getItemActualPrice(i)));
//			} catch (Exception e) {
//				LOG.info("Unexpected end of product list.");
//			}
//		}
//
//		Collections.sort(productList, new ProductComparator());
//
//		return productList;
//	}

}
