package com.extrabux.pages.daigou;

import java.util.List;

public class CartSummary {
	String totalItems;
	String cartSubtotal;
	String cashBackAmount;
	List<ProductInfo> products;

	public CartSummary(String totalItems, String cartSubtotal,
			String cashBackAmount, List<ProductInfo> products) {
		super();
		this.totalItems = totalItems;
		this.cartSubtotal = cartSubtotal;
		this.cashBackAmount = cashBackAmount;
		this.products = products;
	}

	public String getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(String totalItems) {
		this.totalItems = totalItems;
	}

	public String getCartSubtotal() {
		return cartSubtotal;
	}

	public void setCartSubtotal(String cartSubtotal) {
		this.cartSubtotal = cartSubtotal;
	}

	public String getCashBackAmount() {
		return cashBackAmount;
	}

	public void setCashBackAmount(String cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	public List<ProductInfo> getProducts() {
		return products;
	}

	public void setProducts(List<ProductInfo> products) {
		this.products = products;
	}

}
