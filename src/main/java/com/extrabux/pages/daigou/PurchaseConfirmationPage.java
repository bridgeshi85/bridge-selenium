package com.extrabux.pages.daigou;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PurchaseConfirmationPage extends BasePage {
	private static final Log LOG = LogFactory.getLog(PurchaseConfirmationPage.class);
	final String ORDER_SUCESS_ELEMENT_CLASSNAME = "order-success";

	WebElement id;
	WebElement total;
	WebElement cashback;
	WebElement name;
	WebElement address;
	WebElement phone;
	WebElement shippingMethod;
	WebElement paymentMethod;
	WebElement store;
	WebElement cashbackRate;
	@FindBy(className = "item-list-container")
	WebElement itemListContainer;
	@FindBy(className = "item")
	List<WebElement> cartItems;

	public PurchaseConfirmationPage(WebDriver driver) {
		super(driver);

	}

	public boolean verifyOnConfirmationPage() {
		return driver.findElements(By.className(ORDER_SUCESS_ELEMENT_CLASSNAME)).size() > 0;
	}

	public String getId() {
		return id.getText();
	}

	private String getTotal() {
		Matcher matcher = Pattern.compile("(?<=$)[^(]*").matcher(total.getText().replaceAll("\\s",""));
		matcher.find();
		return matcher.group();
	}

	private String getShipping() {
		Matcher matcher = Pattern.compile("(?<=含运费$)[^,]*").matcher(total.getText().replaceAll("\\s",""));
		matcher.find();
		return matcher.group();
	}

	private String getTax() {
		Matcher matcher = Pattern.compile("(?<=含消费税$)[^)]*").matcher(total.getText().replaceAll("\\s",""));
		matcher.find();
		return matcher.group();
	}

	private String getCashback() {
		return cashback.getText().replace("$", "");
	}

	private String getName() {
		return name.getText().replace("收货人: ", "");
	}

	private String getAddress() {
		return address.getText().replace("地址: ", "");
	}

	private String getPhone() {
		return phone.getText().replace("电话: ", "");
	}

	private String getShippingMethod() {
		return shippingMethod.getText();
	}

	private String getPaymentMethod() {
		System.err.println(paymentMethod.getAttribute("name"));
		return paymentMethod.getAttribute("name");
	}

	public boolean verifyId(String sentId) {
		String pageId = getId();
		return pageId.equals(sentId);
	}

	public boolean verifyTotal(String sentTotal) {
		return total.getText().contains(sentTotal);
	}

	public boolean verifyShipping(String sentShipping) {
		return total.getText().contains(sentShipping);
	}

	public boolean verifyTax(String sentTax) {
		return total.getText().contains(sentTax);
	}

	public boolean verifyCashback(String sentCashback) {
		String pageCashback = getCashback();
		return pageCashback.equals(sentCashback);
	}

	public boolean verifyName(String sentName) {
		String pageName = getName();
		return pageName.equals(sentName);
	}

	public boolean verifyAddress(String sentAddress) {
		String pageAddress = getAddress();
		return pageAddress.equals(sentAddress);
	}

	public boolean verifyPhone(String sentPhone) {
		String pagePhone = getPhone();
		return pagePhone.equals(sentPhone);
	}

	public boolean verifyShippingMethod(String sentShippingMethod) {
		String pageShippingMethod = getShippingMethod();
		return pageShippingMethod.equals(sentShippingMethod);
	}

	public boolean verifyPaymentMethod(String sentPaymentMethod) {
		String pagePaymentMethod = getPaymentMethod();
		return pagePaymentMethod.equals(sentPaymentMethod);
	}

	public Verify verifyTotals(String total, String shipping, String tax, String cashback) {
		boolean totalVerified = true;
		boolean shippingVerified = true;
		boolean taxVerified = true;
		boolean cashbackVerified = true;
		String error = "";

		if (!verifyTotal(total)) {
			totalVerified = false;
			error += "Total did not match: Expected " + total + " but found " + getTotal() + ", ";
		}
		if (!shipping.equals("0.00")) {
			if (!verifyShipping(shipping)) {
				shippingVerified = false;
				error += "Shipping did not match: Expected " + shipping + " but found " + getShipping() + ", ";
			}
		}
		if (!tax.equals("0.00")) {
			if (!verifyTax(tax)) {
				taxVerified = false;
				error += "Shipping did not match: Expected " + tax + " but found " + getTax() + ", ";
			}
		}
		if (!cashback.equals("0.00")) {
			if (!verifyCashback(cashback)) {
				cashbackVerified = false;
				error += "Cashback did not match: Expected " + cashback + " but found " + getCashback() + ", ";
			}
		}
		return new Verify(error, totalVerified && shippingVerified && taxVerified && cashbackVerified);
	}

	public Verify verifyBillingInfo(SavedAddress address, String shippingMethod, String paymentMethod) {
		boolean nameVerified = true;
		boolean addressVerified = true;
		boolean phoneVerified = true;
		boolean shippingVerified = true;
		boolean paymentMethodVerified = true;
		String error = "";

		if (!verifyName(address.getFullName())) {
			nameVerified = false;
			error += "Shipping name did not match.  expected " + address.getFullName() + " but found " + getName() + ", ";
		}
		if (!verifyAddress(address.getFullAddress())) {
			addressVerified = false;
			error += "Shipping address did not match.  expected " + address.getFullAddress() + " but found " + getAddress() + ", ";
		}
		if (!verifyPhone(address.getPhone())) {
			phoneVerified = false;
			error += "Shipping phone did not match.  expected " + address.getPhone() + " but found " + getPhone() + ", ";
		}
		if (!verifyPaymentMethod(paymentMethod)) {
			paymentMethodVerified = false;
			error += "Payment method did not match.  expected " + paymentMethod + " but found " + getPaymentMethod() + ", ";
		}
		return new Verify(error, nameVerified && addressVerified && phoneVerified && shippingVerified && paymentMethodVerified);
	}

//	TODO: Pull this chunk into a base page so it can be shared with Purchase Tests

	public Verify verifyProducts(List<ProductInfo> products) {
		Verify verified;
		Collections.sort(products, new ProductComparator());
		List<ProductInfo> productList = getProductList(products.size());

		for (ProductInfo product : products) {
			verified = verifyProduct(product, productList.get(products.indexOf(product)), products.indexOf(product));
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
		return cartItems.get(productIndex).findElement(By.id("price" + productIndex)).getText().replace("价格: $", "");
	}

	private String getItemActualQuantity(int productIndex) {
		return cartItems.get(productIndex).findElement(By.id("quantity" + productIndex)).getText().replace("数量: ", "");
	}

	private String getItemActualProductName(int productIndex) {
		return cartItems.get(productIndex).findElement(By.id("prodTitle" + productIndex)).getText();
	}

	private String getItemActualStoreName(int productIndex) {
		String actualStoreName = driver.findElement(By.id("store")).getText();
		Pattern p = Pattern.compile("商家: (.*) 享返利 (.*)%");
		Matcher m = p.matcher(actualStoreName);
		while(m.find()) {
			actualStoreName = m.group(1);
		}
		return actualStoreName.replace("商家: ", "");
	}

	public boolean verifyProductStoreName(int itemIndex, String productStoreName) {
		return getItemActualStoreName(itemIndex).equals(productStoreName);
	}

	public boolean verifyProductName(int itemIndex, String productName) {
		return getItemActualProductName(itemIndex).equals(productName);
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

	private List<ProductInfo> getProductList(int itemCount) {
		List<ProductInfo> productList = new ArrayList<ProductInfo>();
		for( int i = 0; i < itemCount; i++){
			try {
				productList.add(new ProductInfo(getItemActualStoreName(i), getItemActualProductName(i), "", getItemActualQuantity(i), getItemActualPrice(i)));
			} catch (Exception e) {
				LOG.info("Unexpected end of product list.");
			}
		}

		Collections.sort(productList, new ProductComparator());

		return productList;
	}

}