package com.extrabux.tests.usc;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.extrabux.pages.HomePageAfterLogin;
import com.extrabux.pages.LoginPage;
import com.extrabux.pages.SignUpPage;
import com.extrabux.pages.usc.BillingPage;
import com.extrabux.pages.usc.CheckoutPage;
import com.extrabux.pages.usc.CreateCreditCardPage;
import com.extrabux.pages.usc.CreditCardListPage;
import com.extrabux.pages.usc.EnterNewShippingAddressPage;
import com.extrabux.pages.usc.ProductPage;
import com.extrabux.pages.usc.PurchasePage;
import com.extrabux.pages.usc.PurchasePreviewPage;
import com.extrabux.tests.BaseTest;
import com.extrabux.util.WebDriverUtil;

public class UscCheckoutTest extends BaseTest {
	private static final Log LOG = LogFactory.getLog(UscCheckoutTest.class);

	String existingUSCUser = "elizabeth+cart@extrabux.com";
	String cartUrl = "https://qa-cart.buynow.com";
	String productUrl = "http://usc-magento.extrabux.net/banana.html";
	String uscUri = "/checkout";
	String uscLocaleUri = "/locale/en";

	String parentHandle;

	//@Test(dataProvider = "getWebDriver")
	public void checkProductCartLink(WebDriver driver) throws Exception {
		String testProductUrl = "http://www.gnc.com/GNC-Hair-Skin-Nails-Formula/product.jsp?productId=3943821";
		
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		HomePageAfterLogin homePage = loginPage.login(existingUSCUser, config.getString("defaultPassword"));

		assertTrue(homePage.verifyLoggedInElementPresent(existingUSCUser), "Logged in email on page not as expected");

		homePage.goToURL(serverName + uscUri);
		List<WebElement> products = driver.findElements(By.className("usc-product"));
		assertTrue(products.size() > 0, "");

		// randomly pick a product
//		Random random = new Random();
//		int randomNum = random.nextInt(products.size());
//		System.err.println("random number: " + randomNum);

		//products.get(randomNum).findElement(By.className("buy")).click();
		// lets just pick the first one for now since i know that one works
		//WebElement randomProduct = products.get(0);
		//productUrl = randomProduct.getAttribute("data-product-url");

		navigateToCheckout(driver, testProductUrl);
		//LOG.info("clicking product : " + testProductUrl);
		
		// is the product info section there?
		assertTrue(driver.findElements(By.id("product-info")).size() > 0, "expected to find product info");
		// is the summary section there?
		assertTrue(driver.findElements(By.id("summary")).size() > 0, "expected to find checkout summary");

		LOG.info("successfully made it to cart");
	}

	@Test(dataProvider = "getWebDriver")
	public void uscCreditCardPurchase(WebDriver driver) throws Exception {
		//HomePageAfterLogin homePage = signUp(driver);

		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		HomePageAfterLogin homePage = loginPage.login(existingUSCUser, config.getString("defaultPassword"));

		assertTrue(homePage.verifyLoggedInElementPresent(existingUSCUser), "Logged in email on page not as expected");

		homePage.goToURL(cartUrl + "/?url=" + productUrl);
		
		CheckoutPage checkout = new CheckoutPage(driver);
		checkout.waitForCheckoutPageToLoad();

		checkout.goToURL(cartUrl + uscLocaleUri);		

		//enterCreditCardBillingInfo(driver, billingPage);
		BillingPage billingPage = new BillingPage(driver);
		try {
			 billingPage = checkout.clickBillingLink();
		} catch (NoSuchElementException e) {
			homePage.goToURL(cartUrl + "/?url=" + productUrl);
			checkout.waitForCheckoutPageToLoad();
			checkout.goToURL(cartUrl + uscLocaleUri);
			billingPage = checkout.clickBillingLink();
		}
		CreditCardListPage creditCards = billingPage.selectCreditCardExpectExistingCards();
		creditCards.selectFirstCreditCard();		

		PurchasePreviewPage previewPage = checkout.clickPurchasePreviewBtn();
		// wait for progress bar to go away
		LOG.info("waiting for purchase preview");
		checkout.waitForPurchasePreviewProgressBarToComplete();

		// click purchase
		PurchasePage success = previewPage.clickPurchase();
		// wait for purchase progress
		LOG.info("waiting for purchase progress to complete");
		checkout.waitForPurchaseProgressBarToComplete();
		// verify on purchase success page
		success.verifyPurchaseSuccessful();
		// close window
		success.closeWindow();

		//WebDriverUtil.switchBackToParentWindow(driver, parentHandle);

		LOG.info("successful purchase");
	}

	//@Test(dataProvider = "getWebDriver")
	public void uscAlipayPrePurchase(WebDriver driver) throws Exception {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		HomePageAfterLogin homePage = loginPage.login(existingUSCUser, config.getString("defaultPassword"));
		
		//HomePageAfterLogin homePage = signUp(driver);
		assertTrue(homePage.verifyLoggedInElementPresent(existingUSCUser), "Logged in email on page not as expected");

		homePage.goToURL(cartUrl + "/?url=" + productUrl);
		
		CheckoutPage checkout = new CheckoutPage(driver);
		checkout.waitForCheckoutPageToLoad();

		//enterShippingAddressInfo(driver, checkout);
		BillingPage billingPage = new BillingPage(driver);
		try {
			billingPage = checkout.clickBillingLink();
		} catch (NoSuchElementException e) {
			homePage.goToURL(cartUrl + "/?url=" + productUrl);
			checkout.waitForCheckoutPageToLoad();
			billingPage = checkout.clickBillingLink();
		}
		billingPage.selectAlipayBilling();

		PurchasePreviewPage preview = checkout.clickPurchasePreviewBtn();
		// wait for progress bar to go away
		LOG.info("waiting for purchase preview");
		preview.waitForPurchasePreviewProgressBarToComplete();

		// click purchase
		preview.clickPurchaseWithAlipay();

		// put some stuff in here

		// wait for purchase progress
		/*LOG.info("waiting for purchase progress to complete");
		checkout.waitForPurchaseProgressBarToComplete();

		PurchasePage purchasePage = new PurchasePage(driver);
		// close window
		purchasePage.closeWindow();

		WebDriverUtil.switchBackToParentWindow(driver, parentHandle);*/

		LOG.info("successful purchase preview");
	}

	private CheckoutPage navigateToCheckout(WebDriver driver, String url) {
		ProductPage productPage = new ProductPage(driver);

		// currently have to close and reopen window because on staging
		// the first product click results in a product not found error.
		productPage.clickProduct(url);
		parentHandle = WebDriverUtil.switchWindows(driver);
		productPage.waitForCheckoutPageToLoad();
		driver.close();
		WebDriverUtil.switchBackToParentWindow(driver, parentHandle);

		CheckoutPage checkout = productPage.clickProduct(url);
		LOG.info("clicking product : " + url);
		parentHandle = WebDriverUtil.switchWindows(driver);
		checkout.waitForCheckoutPageToLoad();
		return checkout;
	}

	@SuppressWarnings("unused")
	private void enterShippingAddressInfo(WebDriver driver,
			CheckoutPage checkout) {
		LOG.info("entering shipping address");
		EnterNewShippingAddressPage shippingPage = checkout.clickShippingAddressLink();
		shippingPage.fillFormAndSubmit(
				config.getString("usc.shipAddr.nickName"),
				config.getString("usc.shipAddr.firstName"),
				config.getString("usc.shipAddr.lastName"), null,
				config.getString("usc.shipAddr.address1"), null,
				config.getString("usc.shipAddr.city"),
				config.getString("usc.shipAddr.state"),
				config.getString("usc.shipAddr.zip"),
				config.getString("usc.shipAddr.phone"),
				config.getBoolean("usc.shipAddr.isDefault"));
		WebDriverUtil.waitForElementVisible(driver, driver.findElement(By.className("alert-success")), 20);
		shippingPage.goBack();

		shippingPage.goToURL("https://" + serverName + uscLocaleUri);
	}

	@SuppressWarnings("unused")
	private void enterCreditCardBillingInfo(WebDriver driver, BillingPage billingPage) {
		LOG.info("entering billing information");
		CreateCreditCardPage creditCardPage = billingPage.selectCreditCardPaymentNoExisting();
		WebDriverUtil.waitForElementVisible(driver, driver.findElement(By.id("create-credit-card")), 30);
		CreditCardListPage cardsPage = creditCardPage.fillFormAndSubmit(
				config.getString("usc.cc.nickName"),
				config.getString("usc.cc.cardNumber"),
				config.getInt("usc.cc.exp.month"),
				config.getInt("usc.cc.exp.year"),
				config.getString("usc.cc.cv2"),
				config.getBoolean("usc.cc.isDefault"),
				config.getString("usc.ccAddr.firstName"),
				config.getString("usc.ccAddr.lastName"), null,
				config.getString("usc.ccAddr.address1"), null,
				config.getString("usc.ccAddr.city"),
				config.getString("usc.ccAddr.state"),
				config.getString("usc.ccAddr.zip"),
				config.getString("usc.ccAddr.phone"));
		WebDriverUtil.waitForElementVisible(driver, driver.findElement(By.className("alert-success")), 40);

		cardsPage.selectFirstCreditCard();
	}

	@SuppressWarnings("unused")
	private HomePageAfterLogin signUp(WebDriver driver) throws Exception {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();

		HomePageAfterLogin homePage = signUpPage.signUp(email,
				config.getString("defaultPassword"),
				config.getString("defaultPassword"));

		assertTrue(homePage.verifyLoggedInElementPresent(email), "Logged in email not as expected.");
		return homePage;
	}
}
