package com.extrabux.tests.cn.mvp;

import static org.testng.Assert.assertTrue;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.cn.mvp.CheckOutPage;
import com.extrabux.pages.cn.mvp.MvpDaigouHomePageAfterLogin;
import com.extrabux.pages.cn.mvp.ShopCartPage;
import com.extrabux.pages.cn.mvp.StoreInfo;
import com.extrabux.pages.cn.mvp.alipay.AlipayPage;
import com.extrabux.pages.cn.mvp.ProductInfo;
import com.extrabux.pages.cn.mvp.ProductPage;

public class PurchaseTests extends MvpDaigouBaseTest{
	
	@Test(dataProvider = "getWebDriver")
	public void addPurchaseWithShippingAddress(WebDriver driver) throws Exception{
		
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),config.getDouble("mvp.product.USDPrice"));

		StoreInfo store = new StoreInfo(config.getString("mvp.store.name"),config.getDouble("mvp.store.cashBackRate"),config.getDouble("mvp.store.freeShipping"),product);
			
		product.setStore(store);
		
		MvpDaigouHomePageAfterLogin homePage = loginToMvpDaigou(config.getString("cn.existingUserEmail"),
				config.getString("cn.existingUserPassword"),driver);
			
		ShopCartPage cartPage = addToCart(homePage, product, driver);
		
		//verify product info
		assertTrue(cartPage.verifyProductInfo(product, 1),"product info in cart not as expected");
		
		double transferFee = getTransferFee(product.getWeight());
		double usShippingFee = getUSShippingFee(product.getUSTotalPrice(),store.getFreeShipping());
		
		//verify transfer fee
		assertEquals(cartPage.getTransferFee(store.getName()),transferFee,"transfer fee as not expected");
		
		//verify us shipping fee
		assertEquals(cartPage.getUSShippingFee(store.getName()),usShippingFee,"us shipping fee as not expected");
		
		CheckOutPage checkOutPage = cartPage.checkOut();
		
		assertTrue(checkOutPage.verifyProductInfo(product, 1),"product info in check out page not as expected");
		
		double orderTotalPrice = product.getTotalPrice()+getTotalShippingFee(transferFee,usShippingFee);
		//double cashBackTotalPrice = store.getCashBack();
		
		//submit
		AlipayPage alipayPage = checkOutPage.submitPurchase();
	
		//verify total price
		assertEquals(alipayPage.getPayAmount(),orderTotalPrice,"order total not as expected");
	}
	
	
	@Test(dataProvider = "getWebDriver")
	public void addOrderWithTwoStores(WebDriver driver) throws Exception{
		
		ProductInfo product1 = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),config.getDouble("mvp.product.USDPrice"));
		
		ProductInfo product2 = new ProductInfo(config.getString("mvp.product.dr.name"),config.getInt("mvp.product.dr.quantity"),
				config.getDouble("mvp.product.dr.price"),config.getInt("mvp.product.dr.id"),config.getInt("mvp.product.dr.weight"),config.getDouble("mvp.product.dr.USDPrice"));
	
		MvpDaigouHomePageAfterLogin homePage = loginToMvpDaigou(config.getString("cn.existingUserEmail"),
				config.getString("cn.existingUserPassword"),driver);
		
		ShopCartPage cartPage = new ShopCartPage(driver);

		//add products to list
		List<ProductInfo> productList = new ArrayList<ProductInfo>();
		productList.add(product1);
		productList.add(product2);
		
		StoreInfo vitacost = new StoreInfo(config.getString("mvp.store.name"),config.getDouble("mvp.store.cashBackRate"),config.getDouble("mvp.store.freeShipping"),product1);
		StoreInfo drugstore = new StoreInfo(config.getString("mvp.store.dr.name"),config.getDouble("mvp.store.dr.cashBackRate"),config.getDouble("mvp.store.dr.freeShipping"),product2);

		//add stores to list
		List<StoreInfo> storeList = new ArrayList<StoreInfo>();
		storeList.add(vitacost);
		storeList.add(drugstore);
		
		//double totalCashBack = 0;
		double totalOrderPrice = 0;
		double totalShippingFee = 0;
		double totalProductPrice = 0;

		//add to cart then get cashback and order total
		for(int i=0;i<productList.size();i++){
			
			productList.get(i).setStore(storeList.get(i));
			
			cartPage = addToCart(homePage, productList.get(i), driver);
			
			double transferFee = getTransferFee(productList.get(i).getWeight());
			double usShippingFee = getUSShippingFee(productList.get(i).getUSTotalPrice(),storeList.get(i).getFreeShipping());

			//totalCashBack += productList.get(i).getProductCashBack();
			totalProductPrice += productList.get(i).getPrice();
			totalShippingFee += transferFee+usShippingFee;
		}
		
		totalOrderPrice = totalProductPrice + getTotalShippingFee(totalShippingFee);
		
		CheckOutPage checkOutPage = cartPage.checkOut();

		//submit
		AlipayPage alipayPage = checkOutPage.submitPurchase();
		
		//verify total price
		assertEquals(alipayPage.getPayAmount(),totalOrderPrice,"order total not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void addOrderWithMultipleItems(WebDriver driver) throws Exception{
		
		ProductInfo product1 = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),config.getDouble("mvp.product.USDPrice"));
		
		ProductInfo product2 = new ProductInfo(config.getString("mvp.product1.name"),config.getInt("mvp.product1.quantity"),
				config.getDouble("mvp.product1.price"),config.getInt("mvp.product1.id"),config.getInt("mvp.product1.weight"),config.getDouble("mvp.product1.USDPrice"));
		
		MvpDaigouHomePageAfterLogin homePage = loginToMvpDaigou(config.getString("cn.existingUserEmail"),
				config.getString("cn.existingUserPassword"),driver);
		
		ShopCartPage cartPage = new ShopCartPage(driver);

		//add products to list
		List<ProductInfo> productList = new ArrayList<ProductInfo>();
		productList.add(product1);
		productList.add(product2);
		
		StoreInfo vitacost = new StoreInfo(config.getString("mvp.store.name"),config.getDouble("mvp.store.cashBackRate"),config.getDouble("mvp.store.freeShipping"),productList);

		//double totalCashBack = 0;
		double totalOrderPrice = 0;
		
		//add to cart then get cashback and order total
		for(int i=0;i<productList.size();i++){
			
			productList.get(i).setStore(vitacost);
			
			cartPage = addToCart(homePage, productList.get(i), driver);
			
		}
		
		//totalCashBack = vitacost.getCashBack();
		
		//totalOrderPrice = vitacost.getTotalProductPrice()+getTransferFee(productList)+
		//		getUSShippingFee(productList,vitacost.getFreeShipping());
		
		totalOrderPrice = vitacost.getTotalProductPrice()+getTotalShippingFee(getTransferFee(productList),
				getUSShippingFee(productList,vitacost.getFreeShipping()));

		
		CheckOutPage checkOutPage = cartPage.checkOut();

		//submit
		AlipayPage alipayPage = checkOutPage.submitPurchase();
		
		//verify total price
		assertEquals(alipayPage.getPayAmount(),totalOrderPrice,"order total not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void addOrderWithOptionProduct(WebDriver driver) throws Exception{
		
		ProductInfo product = new ProductInfo(config.getString("product.option.name"),config.getInt("product.otion.quantity"),
				config.getDouble("product.option.price"),config.getInt("product.option.id"),
				config.getInt("product.option.weight"),config.getDouble("product.option.USDPrice"),config.getString("product.option.option")
				);
			
		StoreInfo store = new StoreInfo(config.getString("mvp.store.nordstrom.name"),config.getDouble("mvp.store.cashBackRate"),config.getDouble("mvp.store.nordstrom.freeShipping"),product);
		
		product.setStore(store);
		
		MvpDaigouHomePageAfterLogin homePage = loginToMvpDaigou(config.getString("cn.existingUserEmail"),
				config.getString("cn.existingUserPassword"),driver);
		
		ProductPage productPage = new ProductPage(driver);
		homePage.goToURL(productPage.getProductUrl(serverName, product.getProductId()));
		
		double transferFee = getTransferFee(product.getWeight());
		double usShippingFee = getUSShippingFee(product.getUSTotalPrice(),store.getFreeShipping());
		
		//verify goods title
		assertEquals(productPage.getProductName(),product.getProductName(),"goods name not as expected");
		
		productPage.selectOption(product.getOption());
		
		product.setPrice(productPage.getProductPrice());
		
		ShopCartPage cartPage = productPage.addToCart(product.getQuantity());
		
		CheckOutPage checkOutPage = cartPage.checkOut();
		
		assertTrue(checkOutPage.verifyProductInfo(product, 1),"product info in check out page not as expected");
		
		double orderTotalPrice = product.getTotalPrice()+getTotalShippingFee(transferFee,usShippingFee);
		
		//submit
		AlipayPage alipayPage = checkOutPage.submitPurchase();
		
		//verify total price
		assertEquals(alipayPage.getPayAmount(),orderTotalPrice,"order total not as expected");
		
	}
	
	//@Test(dataProvider = "getWebDriver")
	public void addOrderWithHotDeal(WebDriver driver) throws Exception{
		
		ProductInfo product = new ProductInfo(config.getString("mvp.product.hotDeal.name"),config.getInt("mvp.product.hotDeal.quantity"),
				config.getDouble("mvp.product.hotDeal.price"),config.getInt("mvp.product.hotDeal.id"));

		product.setIsHotDeal(true);
		
		MvpDaigouHomePageAfterLogin homePage = loginToMvpDaigou(config.getString("cn.existingUserEmail"),
				config.getString("cn.existingUserPassword"),driver);
			
		ProductPage productPage = new ProductPage(driver);
		homePage.goToURL(productPage.getProductUrl(serverName, product.getProductId()));
		
		//add to cart
		CheckOutPage checkOutPage = productPage.addToCart();
		
		//assertTrue(checkOutPage.verifyProductInfo(product, 1),"product info in check out page not as expected");

		AlipayPage alipay = checkOutPage.submitPurchase();
		
		//verify order price
		assertEquals(alipay.getPayAmount(),product.getTotalPrice(),"order total not as expected");
		
	}

	public String setCashBackToString(double cashBack){
		
	    NumberFormat DollarFormat  = new DecimalFormat("$######0.00");   
	    //if cash is integer
    	if(cashBack % 1.0 == 0){
    		DollarFormat = new DecimalFormat("$######0");   
    	}
    	// if cash back only one decimal
    	else if(cashBack *10 % 1.0 == 0){
    		DollarFormat = new DecimalFormat("$######0.0");   
    	}
    	
    	return DollarFormat.format(cashBack);
	}
}
