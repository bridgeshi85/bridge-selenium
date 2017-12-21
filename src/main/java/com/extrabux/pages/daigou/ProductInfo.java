package com.extrabux.pages.daigou;

public class ProductInfo {
	String url;
	String storeName;
	String productName;
	String productCashBackRate;
	String quantity;
	String productPrice;
	boolean hasOptions;

	public ProductInfo(String storeName, String productName,
			String productCashBackRate, String quantity, String productPrice) {
		super();
		this.storeName = storeName;
		this.productName = productName;
		this.productCashBackRate = productCashBackRate;
		this.quantity = quantity;
		this.productPrice = productPrice;
	}

	public ProductInfo(String url, String storeName, String productName,
			String productCashBackRate, String quantity, String productPrice,
			boolean hasOptions) {
		super();
		this.url = url;
		this.storeName = storeName;
		this.productName = productName;
		this.productCashBackRate = productCashBackRate;
		this.quantity = quantity;
		this.productPrice = productPrice;
		this.hasOptions = hasOptions;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCashBackRate() {
		return productCashBackRate;
	}

	public void setProductCashBackRate(String productCashBackRate) {
		this.productCashBackRate = productCashBackRate;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public boolean hasOptions() {
		return hasOptions;
	}

	public void setHasOptions(boolean hasOptions) {
		this.hasOptions = hasOptions;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		ProductInfo product = (ProductInfo) obj;
		return (storeName == product.storeName) && (productName == product.productName) && (productCashBackRate == product.productCashBackRate) && (quantity == product.quantity) && (productPrice == product.productPrice);
	}

	@Override
	public int hashCode() {
		return this.storeName.length() + this.productName.length();
	}
	
	@Override
	public String toString() {
		return "(" + storeName + ", " + productName + ", " + productCashBackRate + ", " + quantity + ", " + productPrice + ")";
	}

}
