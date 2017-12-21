package com.extrabux.daigou;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.extrabux.pages.LoginPage;
import com.extrabux.pages.daigou.CartSummarySection;
import com.extrabux.pages.daigou.ProductInfo;
import com.extrabux.pages.daigou.ProductInfoSection;
import com.extrabux.pages.daigou.ProductUrlInputSection;
import com.extrabux.pages.daigou.PurchaseConfirmationPage;
import com.extrabux.pages.daigou.PurchasePreviewPage;
import com.extrabux.pages.daigou.SavedAddress;
import com.extrabux.pages.daigou.ManageShippingAddressesPage;
import com.extrabux.pages.daigou.Verify;
import com.extrabux.tests.BaseTest;
import com.extrabux.util.WebDriverUtil;

public class ProductUrlInputTest extends BaseTest {
	private static final Log LOG = LogFactory.getLog(ProductUrlInputTest.class);

	String daigouUrl = "https://daigou.dev";
	String daigouMockUrl = "https://daigou.dev/payment/mock-notify";
	String daigouShippingAddressesUrl = "https://daigou.dev/shipping-addresses";
	String userName = "elizabeth@extrabux.com";
	String password = "javasucks";

	String storeName = "USC Magento";
	String productName = "Banana";
	String price = "0.01";

	@Test(dataProvider = "getWebDriver")
	public void testProductUrlInput(WebDriver driver) throws Exception {
		String productUrl = "http://usc-magento.extrabux.net/banana.html";

		driver.get(daigouUrl);

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(userName, password);
		
		CartSummarySection cartSummary = new CartSummarySection(driver);
		ProductInfoSection productInfo = new ProductInfoSection(driver);
		
		List<ProductInfo> expectedProducts = new ArrayList<ProductInfo>();
		ProductInfo product1 = new ProductInfo(storeName, productName, "", "1", price);
		expectedProducts.add(product1);

		cartSummary.waitForCartSpinnerNotPresent();
		
		ProductUrlInputSection productUrlInput = new ProductUrlInputSection(driver);
		productUrlInput.selectStoreFromDropDown(storeName);
		productUrlInput.typeProductUrl(productUrl);
		productUrlInput.clickSubmit();	
				
		productInfo.waitForProductInfoSpinner();		
		productInfo.selectQuantity(1);
		productInfo.addToCart();

		Verify verify = productInfo.verifyProductInfo(storeName, productName, price);
		assertTrue(verify.isVerified(), verify.getError());
		
		cartSummary.waitForCartSummarySpinnerNotPresent();		
		cartSummary.waitForPurchasePreviewBtnClickable();
		//CartSummary cart = new CartSummary("1", "0.01", "1.44", expectedProducts);
		//assertTrue(cartSummary.verifyCartSummary(cart), "cart not as expected");
		cartSummary.clickPurchasePreviewBtn();
		
		ManageShippingAddressesPage shippingAddressPage = new ManageShippingAddressesPage(driver);
		enterShippingAddressInfo(driver, shippingAddressPage);
		
		// verify purchase summary stuff
		cartSummary.clickPurchaseBtn();
		cartSummary.waitForProgressBar();
		// will transfer to alipay but we are cheating with the mocky way
		cartSummary.goToURL(daigouMockUrl);
		PurchasePreviewPage purchasePreview = new PurchasePreviewPage(driver);
		purchasePreview.waitForRedirectSpinner();

		PurchaseConfirmationPage purchaseConfirmation = new PurchaseConfirmationPage(driver);
		assertTrue(purchaseConfirmation.verifyOnConfirmationPage(), "not on confirmation page.  current url : " + driver.getCurrentUrl());
		// verify crap on page
	}

	private void enterShippingAddressInfo(WebDriver driver,
			ManageShippingAddressesPage shippingAddressPage) {
		LOG.info("entering shipping address");
		shippingAddressPage.fillFormAndSubmit(new SavedAddress(config));
	}

	@BeforeMethod
	public void cleanup() throws IOException {
		WebDriver driver = new FirefoxDriver();
		CartSummarySection cartSummary = new CartSummarySection(driver);		
			driver.get(daigouUrl);
			LoginPage loginPage = new LoginPage(driver);
			loginPage.login(userName, password);

			driver.get(daigouShippingAddressesUrl);
			cartSummary.waitForCartSummarySpinnerNotPresent();
			if (driver.findElements(By.id("addressLoading")).size() > 0) {
				WebDriverUtil.waitForElementPresent(driver, By.id("addressLoading"), 10);
				System.err.println("waited for addressloading");
			}
			try {
				WebDriverUtil.waitForElementPresent(driver, By.className("delete"), 10);
				System.err.println("deleting address");
				driver.findElement(By.className("delete")).click();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			driver.get(daigouUrl);
			cartSummary.waitForCartSpinnerNotPresent();
			if (Integer.parseInt(cartSummary.getNumberOfItemsInCart()) > 0) {
				cartSummary.clickClearCart();
			}
			driver.close();
		} 

}
