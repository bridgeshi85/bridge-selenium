package com.extrabux.tests.cn.mvp;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import com.extrabux.pages.cn.mvp.ShopCartPage;
import com.extrabux.pages.cn.mvp.StoreInfo;
import com.extrabux.pages.cn.mvp.MvpDaigouHomePageAfterLogin;
import com.extrabux.pages.cn.mvp.ProductInfo;
import com.extrabux.pages.cn.mvp.ProductPage;

public class ShoppingCartTests extends MvpDaigouBaseTest{
		
	@Test(dataProvider = "getWebDriver",groups="production-test")
	public void addSingleProductToCart(WebDriver driver) throws Exception{
		
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),config.getDouble("mvp.product.USDPrice"));

		StoreInfo store = new StoreInfo(config.getString("mvp.store.name"),config.getDouble("mvp.store.cashBackRate"),config.getDouble("mvp.store.freeShipping"),product);
		
		product.setStore(store);
		
		ShopCartPage cartPage = new ShopCartPage(driver);
				
		cartPage = addToCart(cartPage, product, driver);
		
		//verify product info
		assertTrue(cartPage.verifyProductInfo(product, 1),"product info in cart not as expected");
		
		double transferFee = getTransferFee(product.getWeight());
		double usShippingFee = getUSShippingFee(product.getUSTotalPrice(),store.getFreeShipping());
		
		//verify transfer fee
		assertEquals(cartPage.getTransferFee(store.getName()),transferFee,"transfer fee as not expected");
		
		//verify us shipping fee
		assertEquals(cartPage.getUSShippingFee(store.getName()),usShippingFee,"us shipping fee as not expected");
		
		//verify total shipping fee
		assertEquals(cartPage.getTotalShippingFee(),getTotalShippingFee(usShippingFee,transferFee),"total shipping fee as not expected");

		//verify cash back
		//assertEquals(cartPage.getTotalCashBack(),store.getCashBack(),"total cash back as not expected");
	
	}
	
	@Test(dataProvider = "getWebDriver")
	public void addMulitpleProductToCart(WebDriver driver) throws Exception{
	
		ProductInfo product1 = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),config.getDouble("mvp.product.USDPrice"));
		
		ProductInfo product2 = new ProductInfo(config.getString("mvp.product1.name"),config.getInt("mvp.product1.quantity"),
				config.getDouble("mvp.product1.price"),config.getInt("mvp.product1.id"),config.getInt("mvp.product1.weight"),config.getDouble("mvp.product1.USDPrice"));
		
		ShopCartPage cartPage = new ShopCartPage(driver);
		
		//add products to list
		List<ProductInfo> productList = new ArrayList<ProductInfo>();
		productList.add(product1);
		productList.add(product2);
				
		StoreInfo store = new StoreInfo(config.getString("mvp.store.name"),config.getDouble("mvp.store.cashBackRate"),config.getDouble("mvp.store.freeShipping"),productList);
		
		for(int i=0;i<productList.size();i++){
			
			productList.get(i).setStore(store);
						
			cartPage = addToCart(cartPage, productList.get(i), driver);
			
			//verify product info
			//assertTrue(cartPage.verifyProductInfo(productList.get(i), 1),"product info in cart not as expected");
		}
				
		double transferFee = getTransferFee(productList);
		double usShippingFee = getUSShippingFee(productList,store.getFreeShipping());
		
		//verify transfer fee
		assertEquals(cartPage.getTransferFee(store.getName()),transferFee,"transfer fee as not expected");
		
		//verify us shipping fee
		assertEquals(cartPage.getUSShippingFee(store.getName()),usShippingFee,"us shipping fee as not expected");
		
		//verify total shipping fee
		assertEquals(cartPage.getTotalShippingFee(),getTotalShippingFee(usShippingFee,transferFee),"total shipping fee as not expected");

		//verify cash back
		//assertEquals(cartPage.getTotalCashBack(),store.getCashBack(),"total cash back as not expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void addTwoProductsThenRemoveOne(WebDriver driver) throws Exception{
		
		ProductInfo product1 = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),config.getDouble("mvp.product.USDPrice"));
		
		ProductInfo product2 = new ProductInfo(config.getString("mvp.product1.name"),config.getInt("mvp.product1.quantity"),
				config.getDouble("mvp.product1.price"),config.getInt("mvp.product1.id"),config.getInt("mvp.product1.weight"),config.getDouble("mvp.product1.USDPrice"));
		
		ShopCartPage cartPage = new ShopCartPage(driver);
		
		//add products to list
		List<ProductInfo> productList = new ArrayList<ProductInfo>();
		productList.add(product1);
		productList.add(product2);
		
		//StoreInfo store = new StoreInfo(config.getString("mvp.store.name"),config.getDouble("mvp.store.cashBackRate"),config.getDouble("mvp.store.freeShipping"),productList);
		
		for(int i=0;i<productList.size();i++){
			
			cartPage = addToCart(cartPage, productList.get(i), driver);
		}
		
		//remove first one
		cartPage.removeProduct(0);
		productList.remove(0);
		
		double transferFee = getTransferFee(productList);
		double usShippingFee = getUSShippingFee(productList,config.getDouble("mvp.store.freeShipping"));
		
		//verify transfer fee
		assertEquals(cartPage.getTransferFee(config.getString("mvp.store.name")),transferFee,"transfer fee as not expected");
		
		//verify us shipping fee
		assertEquals(cartPage.getUSShippingFee(config.getString("mvp.store.name")),usShippingFee,"us shipping fee as not expected");

		//verify total shipping fee
		assertEquals(cartPage.getTotalShippingFee(),getTotalShippingFee(usShippingFee,transferFee),"total shipping fee as not expected");

		//verify cash back
		//assertEquals(cartPage.getTotalCashBack(),store.getCashBack(),"total cash back as not expected");
	
	}
	
	@Test(dataProvider = "getWebDriver")
	public void addOneProductThenRemove(WebDriver driver) throws Exception{
		
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),config.getDouble("mvp.product.USDPrice"));
			
		ShopCartPage cartPage = new ShopCartPage(driver);
		
		cartPage = addToCart(cartPage, product, driver);
		
		//remove first one
		cartPage.removeProduct(0);
		
		assertEquals(cartPage.getProductAmount(),0,"remove product might be failed");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void removeProductThenAddAgain(WebDriver driver) throws Exception{
		
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),config.getDouble("mvp.product.USDPrice"));
			
		ShopCartPage cartPage = new ShopCartPage(driver);
		
		cartPage = addToCart(cartPage, product, driver);
		
		StoreInfo store = new StoreInfo(config.getString("mvp.store.name"),config.getDouble("mvp.store.cashBackRate"),config.getDouble("mvp.store.freeShipping"),product);
		
		product.setStore(store);
		
		//remove first one
		cartPage.removeProduct(0);
		
		cartPage = addToCart(cartPage, product, driver);
		
		//verify product info
		assertTrue(cartPage.verifyProductInfo(product, 1),"product info in cart not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void verifyFreeShipping(WebDriver driver) throws Exception{
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getDouble("mvp.product.USDPrice"),config.getInt("mvp.product.id"));
		
		//set quantity to satisfy free shipping
		product.setFreeShippingQuantity(config.getDouble("mvp.store.freeShipping"));
		
		//add to cart
		ShopCartPage cartPage = new ShopCartPage(driver);
		
		cartPage = addToCart(cartPage, product, driver);
		
		//verify us shipping fee
		assertEquals(cartPage.getUSShippingFee(config.getString("mvp.store.name")),0.00,"us shipping fee as not expected");
		
	}
	
	@Test(dataProvider = "getWebDriver")
	public void verifyTransferFeeTwoPouns(WebDriver driver) throws Exception{
		
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.weight"),config.getInt("mvp.product.id"));

		//set quantity to satisfy 2 pounds transfer fee
		product.setQuantityByPounds(2);
		
		//add to cart
		ShopCartPage cartPage = new ShopCartPage(driver);
		
		cartPage = addToCart(cartPage, product, driver);
		
		//verify us shipping fee
		assertEquals(cartPage.getTransferFee(config.getString("mvp.store.name")),getTransferFee(product.getWeight()),"us shipping fee as not expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void addProductsFromTwoStores(WebDriver driver) throws Exception{
		
		ProductInfo product1 = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),config.getDouble("mvp.product.USDPrice"));
		
		ProductInfo product2 = new ProductInfo(config.getString("mvp.product.dr.name"),config.getInt("mvp.product.dr.quantity"),
				config.getDouble("mvp.product.dr.price"),config.getInt("mvp.product.dr.id"),config.getInt("mvp.product.dr.weight"),config.getDouble("mvp.product.dr.USDPrice"));
	
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
		
		double totalShippingFee = 0;
		//double totalCashBack = 0;
		//double totalProductPrice = 0;
		
		for(int i=0;i<productList.size();i++){
			
			productList.get(i).setStore(storeList.get(i));
			
			cartPage = addToCart(cartPage, productList.get(i), driver);
			
			//verify product info
			assertTrue(cartPage.verifyProductInfo(productList.get(i), 1,storeList.get(i).getName()),"product info in cart not as expected");
			
			double transferFee = getTransferFee(productList.get(i).getWeight());
			double usShippingFee = getUSShippingFee(productList.get(i).getUSTotalPrice(),storeList.get(i).getFreeShipping());

			totalShippingFee =  transferFee+usShippingFee+totalShippingFee;
			//totalCashBack += productList.get(i).getProductCashBack();
			//totalProductPrice += productList.get(i).getPrice();

			//verify transfer fee
			assertEquals(cartPage.getTransferFee(storeList.get(i).getName()),transferFee,"transfer fee as not expected");
			
			//verify us shipping fee
			assertEquals(cartPage.getUSShippingFee(storeList.get(i).getName()),usShippingFee,"us shipping fee as not expected");
		}
		
		//verify total shipping fee
		//assertEquals(cartPage.getTotalShippingFee(),totalShippingFee,"total shipping fee as not expected");
		assertEquals(cartPage.getTotalShippingFee(),getTotalShippingFee(totalShippingFee),"total shipping fee as not expected");

		//verify cash back
		//assertEquals(cartPage.getTotalCashBack(),new BigDecimal(totalCashBack).setScale(2,BigDecimal.ROUND_DOWN).doubleValue(),"total cash back as not expected");

		//verify total product price
		//assertEquals(cartPage.getTotalProductPrice(),totalProductPrice,"total price as not expected");

	}
	
	@Test(dataProvider = "getWebDriver")
	public void updateQuantityToTwo(WebDriver driver) throws Exception{
		
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),config.getDouble("mvp.product.USDPrice"));

		StoreInfo store = new StoreInfo(config.getString("mvp.store.name"),config.getDouble("mvp.store.cashBackRate"),config.getDouble("mvp.store.freeShipping"),product);
		
		product.setStore(store);
		
		ShopCartPage cartPage = new ShopCartPage(driver);
				
		cartPage = addToCart(cartPage, product, driver);
		
		product.setQuantity(2);
		
		cartPage.updateQuantiy(product.getQuantity(), 0);
		
		//verify product info
		assertTrue(cartPage.verifyProductInfo(product, 1),"product info in cart not as expected");
		
		double transferFee = getTransferFee(product.getWeight());
		double usShippingFee = getUSShippingFee(product.getUSTotalPrice(),store.getFreeShipping());
		
		//verify transfer fee
		assertEquals(cartPage.getTransferFee(store.getName()),transferFee,"transfer fee as not expected");
		
		//verify us shipping fee
		assertEquals(cartPage.getUSShippingFee(store.getName()),usShippingFee,"us shipping fee as not expected");
		
		//verify total shipping fee
		assertEquals(cartPage.getTotalShippingFee(),getTotalShippingFee(usShippingFee,transferFee),"total shipping fee as not expected");

		//verify cash back
		//assertEquals(cartPage.getTotalCashBack(),store.getCashBack(),"total cash back as not expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void addToCartThenAddToFav(WebDriver driver) throws Exception{
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),config.getDouble("mvp.product.USDPrice"));
		
		MvpDaigouHomePageAfterLogin homePage = signUpFromMvpDaigou(getRandomEmail(),
				config.getString("cn.existingUserPassword"),config.getString("cn.existingUserPassword"),driver);
			
		ShopCartPage cartPage = addToCart(homePage, product, driver);
		
		cartPage.moveProductToFav(0);
		
		assertEquals(cartPage.getFavProductName(0),product.getProductName(),"fav product name not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void addToCartWithQuantityOfTwo(WebDriver driver) throws Exception{
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),2,
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),config.getDouble("mvp.product.USDPrice"));
		
		StoreInfo store = new StoreInfo(config.getString("mvp.store.name"),config.getDouble("mvp.store.cashBackRate"),config.getDouble("mvp.store.freeShipping"),product);
		
		product.setStore(store);
		
		ShopCartPage cartPage = new ShopCartPage(driver);
		
		cartPage = addToCart(cartPage, product, driver);
		
		//verify product info
		assertTrue(cartPage.verifyProductInfo(product, 1),"product info in cart not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void addToCartWithOption(WebDriver driver) throws Exception{
		
		ProductInfo product = new ProductInfo(config.getString("product.option.name"),config.getInt("product.otion.quantity"),
				config.getDouble("product.option.price"),config.getInt("product.option.id"),config.getString("product.option.option"));
				
		ProductPage productPage = new ProductPage(driver);
		productPage.goToURL(productPage.getProductUrl(serverName, product.getProductId()));
		
		//verify goods title
		assertEquals(productPage.getProductName(),product.getProductName(),"goods name not as expected");
		
		productPage.selectOption(product.getOption());
		
		product.setPrice(productPage.getProductPrice());
		
		ShopCartPage cartPage = productPage.addToCart(product.getQuantity());
		
		//verify product info
		assertTrue(cartPage.verifyProductInfo(product, 1),"product info in cart not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void addToCartWithOutOfStockProduct(WebDriver driver) {
		ProductInfo product = new ProductInfo(config.getString("mvp.product.outOfStock.name"),config.getInt("mvp.product.outOfStock.quantity"),
				config.getDouble("mvp.product.outOfStock.price"),config.getInt("mvp.product.outOfStock.id"));

		ProductPage productPage = new ProductPage(driver);
		productPage.goToURL(productPage.getProductUrl(serverName, product.getProductId()));
		
		//verify goods title
		assertEquals(productPage.getProductName(),product.getProductName(),"goods name not as expected");
		
		productPage.typeQuantity(product.getQuantity());
		
		assertEquals(productPage.getQuantityControlMsg(),config.getString("outOfStock.message"),"add out of stock product failed");
	}
}
