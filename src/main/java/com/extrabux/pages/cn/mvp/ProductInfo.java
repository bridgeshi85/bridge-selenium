package com.extrabux.pages.cn.mvp;

import java.math.BigDecimal;

public class ProductInfo {

	String productName;
	int quantity;
	Double productPrice;
	int productId;
	int weight;
	Double USPrice;
	StoreInfo store;
	boolean hotDeal = false;
	String option;
	
	public ProductInfo(String productName,int quantity, double productPrice,int productId) {
		this.productName = productName;
		this.quantity = quantity;
		this.productPrice = productPrice;
		this.productId = productId;
	}
	
	public ProductInfo(String productName,int quantity, double productPrice,int productId,String option) {
		this.productName = productName;
		this.quantity = quantity;
		this.productPrice = productPrice;
		this.productId = productId;
		this.option = option;
	}
	
	public ProductInfo(String productName,double USPrice,int productId) {
		this.productName = productName;
		this.USPrice = USPrice;
		this.productId = productId;
	}
	
	public ProductInfo(String productName,int weight,int productId) {
		this.productName = productName;
		this.weight = weight;
		this.productId = productId;
	}
	
	public ProductInfo(String productName,int quantity, double productPrice,int productId,int weight,double USPrice) {
		this.productName = productName;
		this.quantity = quantity;
		this.productPrice = productPrice;
		this.productId = productId;
		this.weight = weight;
		this.USPrice = USPrice;	
	}
	
	public ProductInfo(String productName,int quantity, double productPrice,int productId,int weight,double USPrice,String option) {
		this.productName = productName;
		this.quantity = quantity;
		this.productPrice = productPrice;
		this.productId = productId;
		this.weight = weight;
		this.USPrice = USPrice;	
		this.option = option;
	}
	
	public String getProductName(){
		return productName;
	}
	
	public String getOption(){
		return option;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public double getPrice(){
		return productPrice;
	}
	
	public int getProductId(){
		return productId;
	}
	
	public int getWeight(){
		return quantity*weight;
	}
	
	public double getUSPrice(){
		return USPrice;
	}
	
	public void setFreeShippingQuantity(double freeShippingThreshold){
		this.quantity = (int) Math.ceil(freeShippingThreshold/this.USPrice);
	}
	
	public void setQuantityByPounds(int pounds){
		this.quantity = (((pounds-1)*500) / weight) +1;
	}
	
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
	
	public boolean getIsHotDeal(){
		return hotDeal;
	}
	
	public void setIsHotDeal(boolean isHotDeal){
		this.hotDeal = isHotDeal;
	}
	
	public double getTotalPrice(){
		
    	BigDecimal priceDouble = new BigDecimal(productPrice);
    	BigDecimal quantityDouble = new BigDecimal(quantity);
    	
    	return priceDouble.multiply(quantityDouble).doubleValue();
	}
	
	public double getUSTotalPrice(){
		
    	BigDecimal priceDouble = new BigDecimal(USPrice);
    	BigDecimal quantityDouble = new BigDecimal(quantity);
    	return priceDouble.multiply(quantityDouble).doubleValue();
	}
	
	public void setStore(StoreInfo store){
		this.store = store;
	}
	
	public void setPrice(double productPrice){
		this.productPrice = productPrice;
	}
	
	public double getProductCashBack(){
    	BigDecimal priceDouble = new BigDecimal(getUSTotalPrice());
    	BigDecimal cashBackDouble = new BigDecimal(store.cashBackRate);
    	    	
    	double cashBack = priceDouble.multiply(cashBackDouble).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
		return cashBack;
	}
}
