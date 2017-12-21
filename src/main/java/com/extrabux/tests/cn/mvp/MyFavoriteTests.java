package com.extrabux.tests.cn.mvp;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import com.extrabux.pages.cn.mvp.account.MyCollectionPage;
import com.extrabux.pages.cn.mvp.MvpDaigouHomePageAfterLogin;
import com.extrabux.pages.cn.mvp.ProductInfo;
import com.extrabux.pages.cn.mvp.ProductsListPage;
import com.extrabux.pages.cn.mvp.ShopCartPage;
import com.extrabux.pages.cn.mvp.StoreInfo;

public class MyFavoriteTests extends MvpDaigouBaseTest{
	
	//private static final Log LOG = LogFactory.getLog(MyFavoriteTests.class); 
	
	private static String userName;
	
	@Test(dataProvider = "getWebDriver")
	public void addToCartFromFavList(WebDriver driver) throws Exception{
		
		loginToMvpDaigou(config.getString("cn.existingUserEmail"),
				config.getString("cn.existingUserPassword"),driver);
		
		MyCollectionPage favPage = new MyCollectionPage(driver);
		
		favPage.goToURL(favPage.getUrl(serverName));
		
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),config.getDouble("mvp.product.USDPrice"));
		
		StoreInfo store = new StoreInfo(config.getString("mvp.store.name"),config.getDouble("mvp.store.cashBackRate"),config.getDouble("mvp.store.freeShipping"),product);
		
		product.setStore(store);
		
		product.setPrice(favPage.getProductPrice(product.getProductId()));
		
		//add to cart
		ShopCartPage cartPage = favPage.addToCart(product.getProductId());
		
		//verify product info
		assertTrue(cartPage.verifyProductInfo(product, 1),"product info in cart not as expected");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void addToFav(WebDriver driver) throws Exception{
		
		userName = getRandomEmail();
		
		MvpDaigouHomePageAfterLogin homePage = signUpFromMvpDaigou(userName,
				config.getString("cn.existingUserPassword"),config.getString("cn.existingUserPassword"),driver);
				
		ProductsListPage listPage = homePage.goToListPage();
				
		listPage.addToFav();
		
		MyCollectionPage collectionPage = new MyCollectionPage(driver);
		collectionPage.goToURL(collectionPage.getUrl(serverName));
		
		assertEquals(collectionPage.getFavCount(),1,"add to fav list probably failed");
	}
	
	@Test(dataProvider = "getWebDriver",dependsOnMethods="addToFav")
	public void removeFav(WebDriver driver) throws Exception{
		
		loginToMvpDaigou(userName,
				config.getString("cn.existingUserPassword"),driver);
		
		MyCollectionPage favPage = new MyCollectionPage(driver);
		
		favPage.goToURL(favPage.getUrl(serverName));
		
		int favAmount = favPage.getFavCount();
		
		favPage.deleteFav();
		
		assertEquals(favPage.getFavCount(),favAmount-1,"remove product from my fav failed");
	}
}
