package com.extrabux.pages.daigou;

import java.util.Comparator;

public class ProductComparator implements Comparator<ProductInfo> {

	public int compare(ProductInfo prod1, ProductInfo prod2) {
		return prod1.getProductName().compareTo(prod2.getProductName());
	}

}