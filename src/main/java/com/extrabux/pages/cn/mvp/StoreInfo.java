package com.extrabux.pages.cn.mvp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StoreInfo {
	
	String name;
	double cashBackRate;
	double vipCashBackRate;
	double freeShippingThreshold;
	double shippingCost = 5.00;
	//ProductInfo product;
	List<ProductInfo> productList;
	
	public StoreInfo(String name,double cashBackRate,double freeShippingThreshold,ProductInfo product) {
		this.name = name;
		this.cashBackRate = cashBackRate;
		this.freeShippingThreshold = freeShippingThreshold;
		//this.product = product;
		this.productList = new ArrayList<ProductInfo>();
		productList.add(product);
	}
	
	public StoreInfo(String name,double cashBackRate,double freeShippingThreshold,List<ProductInfo> productList) {
		this.name = name;
		this.cashBackRate = cashBackRate;
		this.freeShippingThreshold = freeShippingThreshold;
		this.productList = productList;
	}
	
	public String getName() {
		return name;
	}
	
	public double getFreeShipping(){
		return freeShippingThreshold;
	}
	
	public double getCashBack(){
		double totalUSPrice = 0.00;
		for(ProductInfo product:productList){
			totalUSPrice += product.getUSTotalPrice();
		}
    	BigDecimal priceDouble = new BigDecimal(totalUSPrice);
    	BigDecimal cashBackDouble = new BigDecimal(cashBackRate);
    	
    	double cashBack = priceDouble.multiply(cashBackDouble).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
		return cashBack;
	}
	
	public double getTotalProductPrice(){
		double totalPrice = 0.00;
		for(ProductInfo product:productList){
			totalPrice += product.getTotalPrice();
		}
    	
		return totalPrice;
	}
	
}
