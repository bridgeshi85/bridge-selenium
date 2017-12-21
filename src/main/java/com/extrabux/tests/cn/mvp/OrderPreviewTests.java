package com.extrabux.tests.cn.mvp;

import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

//import java.text.DecimalFormat;
//import java.util.Locale;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.cn.mvp.CheckOutPage;
import com.extrabux.pages.cn.mvp.MvpDaigouHomePageAfterLogin;
import com.extrabux.pages.cn.mvp.ShopCartPage;
import com.extrabux.pages.cn.mvp.StoreInfo;
import com.extrabux.pages.cn.mvp.ProductInfo;
import com.extrabux.pages.cn.mvp.ShippingAddressInfo;
import com.extrabux.pages.cn.mvp.ShippingAddressPage;

public class OrderPreviewTests extends MvpDaigouBaseTest{
	
	private static String userName;
	
	@Test(dataProvider = "getWebDriver",groups="production-test")
	public void addToCartThenGoToPreview(WebDriver driver) throws Exception{
				
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),
				config.getDouble("mvp.product.USDPrice"));

		StoreInfo store = new StoreInfo(config.getString("mvp.store.name"),config.getDouble("mvp.store.cashBackRate"),config.getDouble("mvp.store.freeShipping"),product);
		
		product.setStore(store);
		
		MvpDaigouHomePageAfterLogin homePage = loginToMvpDaigou(config.getString("cn.existingUserEmail"),
				config.getString("cn.existingUserPassword"),driver);

		ShopCartPage cartPage = addToCart(homePage, product, driver);
		
		double transferFee = getTransferFee(product.getWeight());
		double usShippingFee = getUSShippingFee(product.getUSTotalPrice(),store.getFreeShipping());
		//double cashBack = store.getCashBack();
		
		CheckOutPage checkOutPage = cartPage.checkOut();
		
		assertTrue(checkOutPage.verifyProductInfo(product, 1),"product info in check out page not as expected");
		
		ShippingAddressInfo address = new ShippingAddressInfo(config.getString("mvp.shipping.username"), config.getString("mvp.shipping.email"), config.getString("mvp.shipping.country"),
				config.getString("mvp.shipping.province"), config.getString("mvp.shipping.city"),
				config.getString("mvp.shipping.district"), config.getString("mvp.shipping.address"), config.getString("mvp.shipping.zipCode"),
				config.getString("mvp.shipping.mobilePhoneNumber"), config.getString("mvp.shipping.telePhoneNumber"));

		assertTrue(checkOutPage.verifyAddressInfo(address),"address info in check out page not as expected");
		
		//assert total price
		assertEquals(checkOutPage.getTotalPrice(),product.getTotalPrice()+getTotalShippingFee(usShippingFee,transferFee),"total price as not expected");

		//assert cash back
		//assertEquals(checkOutPage.getTotalCashBack(),cashBack,"cash back as not expected");
	}
	
	@Test(dataProvider = "getWebDriver",dependsOnMethods="addAddressWithInvalidEmailAndPhoneNumber")
	public void addAddress(WebDriver driver) throws Exception{
				
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),
				config.getDouble("mvp.product.USDPrice"));
		
		MvpDaigouHomePageAfterLogin homePage = loginToMvpDaigou(userName,
				config.getString("cn.existingUserPassword"),driver);
		
		//add to cart then checkout
		ShopCartPage cartPage = addToCart(homePage, product, driver);
		
		ShippingAddressPage addressPage = cartPage.checkOutWithoutAddress();
		
		ShippingAddressInfo address = new ShippingAddressInfo(config.getString("mvp.shipping.username"), config.getString("mvp.shipping.email"), config.getString("mvp.shipping.country"),
				config.getString("mvp.shipping.province"), config.getString("mvp.shipping.city"),
				config.getString("mvp.shipping.district"), config.getString("mvp.shipping.address"), config.getString("mvp.shipping.zipCode"),
				config.getString("mvp.shipping.mobilePhoneNumber"), config.getString("mvp.shipping.telePhoneNumber"));

		//add shipping address
		CheckOutPage checkOutPage = addressPage.addShippingAddress(address);
		
		assertTrue(checkOutPage.verifyAddressInfo(address),"address info in check out page not as expected");
	}

	@Test(dataProvider = "getWebDriver",dependsOnMethods="addAddress")
	public void modifyAddress(WebDriver driver) throws Exception{
		
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),
				config.getDouble("mvp.product.USDPrice"));

		MvpDaigouHomePageAfterLogin homePage = loginToMvpDaigou(userName,
				config.getString("cn.existingUserPassword"),driver);
		
		//add to cart then checkout
		ShopCartPage cartPage = addToCart(homePage, product, driver);

		CheckOutPage checkOutPage = cartPage.checkOut();

		ShippingAddressPage addressPage = checkOutPage.goToAddressPage();
		
		ShippingAddressInfo address = new ShippingAddressInfo(config.getString("mvp.shipping.newUsername"), config.getString("mvp.shipping.newEmail"), config.getString("mvp.shipping.newCountry"),
				config.getString("mvp.shipping.newProvince"), config.getString("mvp.shipping.newCity"),
				config.getString("mvp.shipping.newDistrict"), config.getString("mvp.shipping.newAddress"), config.getString("mvp.shipping.newZipCode"),
				config.getString("mvp.shipping.newMobilePhoneNumber"), config.getString("mvp.shipping.newTelePhoneNumber"));
 
		//add shipping address
		checkOutPage = addressPage.addShippingAddress(address);
		
		assertTrue(checkOutPage.verifyAddressInfo(address),"address info in check out page not as expected");
	}
	
	@Test(dataProvider = "getWebDriver",dependsOnMethods="modifyAddress")
	public void deleteAddress(WebDriver driver) throws Exception{
		
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),
				config.getDouble("mvp.product.USDPrice"));

		MvpDaigouHomePageAfterLogin homePage = loginToMvpDaigou(userName,
				config.getString("cn.existingUserPassword"),driver);
		
		//add to cart then checkout
		ShopCartPage cartPage = addToCart(homePage, product, driver);

		CheckOutPage checkOutPage = cartPage.checkOut();

		ShippingAddressPage addressPage = checkOutPage.goToAddressPage();
		
		addressPage.deleteShippingAddress();
		
		assertEquals(addressPage.getConsignee(),"","shipping address delete failed");
	}
	
	@Test(dataProvider = "getWebDriver")
	public void addAddressWithEmptyForm(WebDriver driver) throws Exception{
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),
				config.getDouble("mvp.product.USDPrice"));

		userName = getRandomEmail();
		
		MvpDaigouHomePageAfterLogin homePage = signUpFromMvpDaigou(userName,
				config.getString("cn.existingUserPassword"),config.getString("cn.existingUserPassword"),driver);

		//add to cart then checkout
		ShopCartPage cartPage = addToCart(homePage, product, driver);
		
		ShippingAddressPage addressPage = cartPage.checkOutWithoutAddress();
		
		//add shipping address
		addressPage.addShippingAddress();
	
		//String alertText = WebDriverUtil.accpectAlert(driver, true);
		
		List<String> expectedErrors = Arrays.asList(config.getStringArray("shipping.error.emptyForm"));

		assertTrue(addressPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + addressPage.getErrors());
	}
	
	@Test(dataProvider = "getWebDriver",dependsOnMethods="addAddressWithEmptyForm")
	public void addAddressWithInvalidEmailAndPhoneNumber(WebDriver driver) throws Exception{
		ProductInfo product = new ProductInfo(config.getString("mvp.product.name"),config.getInt("mvp.product.quantity"),
				config.getDouble("mvp.product.price"),config.getInt("mvp.product.id"),config.getInt("mvp.product.weight"),
				config.getDouble("mvp.product.USDPrice"));
		
		MvpDaigouHomePageAfterLogin homePage = loginToMvpDaigou(userName,
				config.getString("cn.existingUserPassword"),driver);
		
		//add to cart then checkout
		ShopCartPage cartPage = addToCart(homePage, product, driver);
		
		ShippingAddressPage addressPage = cartPage.checkOutWithoutAddress();
		
		ShippingAddressInfo address = new ShippingAddressInfo(config.getString("mvp.shipping.username"), config.getString("mvp.shipping.invalid.email"), config.getString("mvp.shipping.country"),
				config.getString("mvp.shipping.province"), config.getString("mvp.shipping.city"),
				config.getString("mvp.shipping.district"), config.getString("mvp.shipping.address"), config.getString("mvp.shipping.zipCode"),
				config.getString("mvp.shipping.invalid.mobilePhoneNumber"), config.getString("mvp.shipping.telePhoneNumber"));

		//add shipping address
		addressPage.addShippingAddress(address);
	
		//String alertText = WebDriverUtil.accpectAlert(driver, true);
		
		List<String> expectedErrors = Arrays.asList(config.getStringArray("shipping.error.invalid"));

		assertTrue(addressPage.verifyErrorsOnPage(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + addressPage.getErrors());
	}
}
