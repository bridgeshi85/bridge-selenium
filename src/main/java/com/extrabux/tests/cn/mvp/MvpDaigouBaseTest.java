package com.extrabux.tests.cn.mvp;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.extrabux.pages.cn.ChinaLoginPage;
import com.extrabux.pages.cn.ChinaSignUpPage;
import com.extrabux.pages.cn.mvp.MvpDaigouBasePage;
import com.extrabux.pages.cn.mvp.MvpDaigouHomePageAfterLogin;
import com.extrabux.pages.cn.mvp.MvpDaigouHomePageBeforeLogin;
import com.extrabux.pages.cn.mvp.ProductInfo;
import com.extrabux.pages.cn.mvp.ProductPage;
import com.extrabux.pages.cn.mvp.ShopCartPage;
import com.extrabux.tests.BaseTest;

public class MvpDaigouBaseTest extends BaseTest{

	static NumberFormat RMBFormat  = new DecimalFormat("￥######0");   
	
	public MvpDaigouHomePageAfterLogin loginToMvpDaigou(String username, String passwd, WebDriver driver) throws Exception {
		
		MvpDaigouHomePageBeforeLogin homePageBeforeLogin = new MvpDaigouHomePageBeforeLogin(driver);
		homePageBeforeLogin.goToURL("https://" + serverName);
		ChinaLoginPage loginPage = homePageBeforeLogin.clickLogin();

		MvpDaigouHomePageAfterLogin homePageAfterLogin = loginPage.loginFromMvp(username, passwd);
		assertTrue(homePageAfterLogin.verifyLoggedInElementPresent(username), "Logged in email not found on page");
		return homePageAfterLogin;
	}
	
	public MvpDaigouHomePageAfterLogin signUpFromMvpDaigou(String username, String passwd,String confirmPw, WebDriver driver) throws Exception {
		
		MvpDaigouHomePageBeforeLogin homePageBeforeLogin = new MvpDaigouHomePageBeforeLogin(driver);
		homePageBeforeLogin.goToURL("https://" + serverName);
		ChinaSignUpPage signUpPage = homePageBeforeLogin.clickSignUp();

		MvpDaigouHomePageAfterLogin homePageAfterLogin = signUpPage.signUpFromMvp(username, passwd,confirmPw);
		assertTrue(homePageAfterLogin.verifyLoggedInElementPresent(username), "Logged in email not found on page");
		
		return homePageAfterLogin;
	}
	
	public ShopCartPage addToCart(MvpDaigouBasePage prevPage,ProductInfo product,WebDriver driver) throws Exception {
		
		ProductPage productPage = new ProductPage(driver);
		prevPage.goToURL(productPage.getProductUrl(serverName, product.getProductId()));
		
		//verify goods title
		assertEquals(productPage.getProductName(),product.getProductName(),"goods name not as expected");
		
		product.setPrice(productPage.getProductPrice());
		
		//add to cart
		ShopCartPage cartPage = productPage.addToCart(product.getQuantity());
		return cartPage;
	}
	
	public double getTransferFee(List<ProductInfo> productList){
		double transferFee = 0.00;
		int totalWeight = 0;
		for(ProductInfo product : productList){
			totalWeight += product.getWeight();
		}
		int pounds = (int) Math.ceil((double)totalWeight/500);
		transferFee = Math.floor(38.4*pounds);
		return transferFee;
	}
	
	public double getTransferFee(int totalWeight){
		int pounds = (int) Math.ceil((double)totalWeight/500);
		return Math.floor(38.4*pounds);
	}
	
	public double getUSShippingFee(List<ProductInfo> productList,double freeThreshold){
		double totalUSPrice = 0.00;
		for(ProductInfo product : productList){
			totalUSPrice = product.getUSTotalPrice()+totalUSPrice;
		}
		return shippingFeeCalculate(totalUSPrice,freeThreshold);
	}
	
	
	public double getUSShippingFee(double totalUSPrice,double freeThreshold){
		return shippingFeeCalculate(totalUSPrice,freeThreshold);
	}
	
	public double shippingFeeCalculate(double totalUSPrice,double freeThreshold){
		double USShippingFee;
		if(freeThreshold == 0){
			USShippingFee = config.getDouble("USShipping.fee");
		}else
		{
			if(totalUSPrice >= freeThreshold)
				USShippingFee = 0;
			else
				USShippingFee = config.getDouble("USShipping.fee");
		}
		return USShippingFee;
	}
	
	public double getTotalShippingFee(double usShippingFee,double transferFee){
		double discount = config.getDouble("shipping.discount");
		double totalFee = Math.floor((usShippingFee+transferFee)*discount);
		return totalFee;
	}
	
	public double getTotalShippingFee(double totalShippingFee){
		double discount = config.getDouble("shipping.discount");
		double totalFee = Math.floor(totalShippingFee*discount);
		return totalFee;
	}
}
