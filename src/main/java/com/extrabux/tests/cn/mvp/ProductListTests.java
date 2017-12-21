package com.extrabux.tests.cn.mvp;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.cn.mvp.ProductsListPage;
import com.extrabux.pages.cn.mvp.ShopCartPage;

public class ProductListTests extends MvpDaigouBaseTest{

	@Test(dataProvider = "getWebDriver")
	public void filterByTopCategories(WebDriver driver) throws Exception{
		ProductsListPage listPage = new ProductsListPage(driver);
		listPage.goToURL(listPage.getUrl(serverName));
		
		listPage.filterByAllTopCategories();
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterBySubCategory(WebDriver driver) throws Exception{
		
		ProductsListPage listPage = new ProductsListPage(driver);
		listPage.goToURL(listPage.getUrl(serverName));
		
		listPage.filterByTopCategory(config.getString("mvp.product.dr.category"));
		listPage.filterByLink(config.getString("mvp.product.dr.sub.category"));
		listPage.selectLastPage();
		assertTrue(listPage.verifyProductPresent(config.getString("mvp.product.dr.name")));
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterByBrand(WebDriver driver) throws Exception{
		
		ProductsListPage listPage = new ProductsListPage(driver);
		listPage.goToURL(listPage.getUrl(serverName));
		
		listPage.filterByTopCategory(config.getString("mvp.product.dr.category"));
		listPage.filterByBrand(config.getString("mvp.product.dr.brand"));
		
		assertTrue(listPage.verifyProductPresent(config.getString("mvp.product.dr.name")));
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterByStore(WebDriver driver) throws Exception{
		
		ProductsListPage listPage = new ProductsListPage(driver);
		listPage.goToURL(listPage.getUrl(serverName));
		
		listPage.filterByTopCategory(config.getString("mvp.product.dr.category"));
		listPage.filterByLink(config.getString("mvp.store.dr.name"));
		listPage.selectLastPage();
		assertTrue(listPage.verifyProductPresent(config.getString("mvp.product.dr.name")));
	}
	
	@Test(dataProvider = "getWebDriver")
	public void filterByAllCondition(WebDriver driver) throws Exception{
		
		ProductsListPage listPage = new ProductsListPage(driver);
		listPage.goToURL(listPage.getUrl(serverName));
		
		listPage.filterByTopCategory(config.getString("mvp.product.dr.category"));
		
		//filter by all conditions
		listPage.filterByLink(config.getString("mvp.product.dr.sub.category"));
		listPage.filterByLink(config.getString("mvp.product.dr.brand"));
		listPage.filterByLink(config.getString("mvp.store.dr.name"));
		
		assertTrue(listPage.verifyProductPresent(config.getString("mvp.product.dr.name")));
	}
	
	@Test(dataProvider = "getWebDriver")
	public void addToCart(WebDriver driver) throws Exception{
		
		ProductsListPage listPage = new ProductsListPage(driver);
		listPage.goToURL(listPage.getUrl(serverName));
		
		listPage.selectLastPage();
		ShopCartPage cartPage = listPage.addToCart();
		
		assertEquals(cartPage.getProductAmount(),1,"add to cart from list page failed");
	}
	

	
	@Test(dataProvider = "getWebDriver")
	public void sortByPrice(WebDriver driver) throws Exception{
		
		ProductsListPage listPage = new ProductsListPage(driver);
		listPage.goToURL(listPage.getUrl(serverName));
		
		listPage.sortByPrice();
		assertTrue(listPage.verfiySortByPriceResult("asc"));
		
		listPage.sortByPrice();
		assertTrue(listPage.verfiySortByPriceResult("desc"));
	}
	
	@Test(dataProvider = "getWebDriver")
	public void pageDownThenPageUp(WebDriver driver) throws Exception{
		ProductsListPage listPage = new ProductsListPage(driver);
		listPage.goToURL(listPage.getUrl(serverName));
		
		//click page down
		listPage.selectPage("2");
		assertEquals(listPage.getCurrentPage(),"2","page down failed");
		
		//click page up
		listPage.selectPage("1");
		assertEquals(listPage.getCurrentPage(),"1","page up failed");
	}
	
	
}
