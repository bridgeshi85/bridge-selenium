package com.extrabux.tests.daigou;

import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.extrabux.pages.LoginPage;
import com.extrabux.pages.daigou.CartSummarySection;
import com.extrabux.pages.daigou.CreditCardType;
import com.extrabux.pages.daigou.ManageCreditCardsPage;
import com.extrabux.pages.daigou.PurchasePreviewPage;
import com.extrabux.pages.daigou.SavedAddress;
import com.extrabux.pages.daigou.SavedCreditCard;
import com.extrabux.pages.daigou.Verify;


public class CreditCardTests extends DaigouBaseTest {
	private static final Log LOG = LogFactory.getLog(CreditCardTests.class);

	@Test(dataProvider = "getWebDriver")
	public void enterNewCreditCardFromPurchasePreviewUseShipping(WebDriver driver) throws Exception {
		PurchasePreviewPage previewPage = goToPurchasePreviewPage(driver);

		previewPage.addNewCreditCard(config.getString("usc.cc.cardHolderName"), config.getString("usc.cc.cardNumber"), config.getString("usc.cc.exp.month"),
				config.getInt("usc.cc.exp.year"), config.getString("usc.cc.cv2"), config.getBoolean("usc.cc.isDefault"), null);
		// check that there weren't any errors
		assertTrue(previewPage.verifyNoCreditCardErrors(), "error encountered when adding creditCard.");
		// check that card was added
		previewPage.waitForSaveCardSpinner();

		SavedCreditCard firstCard = previewPage.getFirstCreditCard();
		assertTrue(firstCard.getType() == CreditCardType.VISA, "Expected credit card type to be VISA but found : " + firstCard.getType());
		assertTrue(firstCard.getMaskedNumber().equals("***4242"), "Expected credit card masked # to be ***4242 but found : " + firstCard.getMaskedNumber());
	}

	@Test(dataProvider = "getWebDriver")
	public void enterNewCreditCardFromManageCardsPageUseShipping(WebDriver driver) throws Exception {
		ManageCreditCardsPage manageCardsPage = navigateToManageCardsPage(driver);

		manageCardsPage.addNewCreditCard(config.getString("usc.cc2.cardHolderName"), config.getString("usc.cc2.cardNumber"), config.getString("usc.cc2.exp.month"),
				config.getInt("usc.cc2.exp.year"), config.getString("usc.cc2.cv2"), config.getBoolean("usc.cc2.isDefault"), null);
		// check that there weren't any errors
		assertTrue(manageCardsPage.verifyNoErrors(), "error encountered when adding creditCard.");
		manageCardsPage.waitForEditCardsSpinner();
		manageCardsPage.waitCardAddedSpinner();
		manageCardsPage.waitForDefaultCreditCard();
		// verify first saved card on page is what we just added
		Verify verified = manageCardsPage.verifyFirstCreditCardInList(new SavedCreditCard(config.getString("usc.cc2.cardHolderName"), CreditCardType.VISA,
				"***1111", config.getString("usc.cc2.exp.month") + "." + config.getInt("usc.cc2.exp.year"), config.getBoolean("usc.cc2.isDefault")));
		assertTrue(verified.isVerified(), verified.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void enterNewCreditCardFromPurchasePreviewEnterBilling(WebDriver driver) throws Exception {
		PurchasePreviewPage previewPage = goToPurchasePreviewPage(driver);

		previewPage.addNewCreditCard(config.getString("usc.cc.cardHolderName"), config.getString("usc.cc.cardNumber"), config.getString("usc.cc.exp.month"),
				config.getInt("usc.cc.exp.year"), config.getString("usc.cc.cv2"), config.getBoolean("usc.cc.isDefault"), getNewBillingAddressFromConfig());

		// check that there weren't any errors
		assertTrue(previewPage.verifyNoCreditCardErrors(), "error encountered when adding creditCard.");
		// check that card was added
		previewPage.waitForSaveCardSpinner();

		SavedCreditCard firstCard = previewPage.getFirstCreditCard();
		assertTrue(firstCard.getType() == CreditCardType.VISA, "Expected credit card type to be VISA but found : " + firstCard.getType());
		assertTrue(firstCard.getMaskedNumber().equals("***4242"), "Expected credit card masked # to be ***4242 but found : " + firstCard.getMaskedNumber());

		previewPage.clickEditCreditCardBtn();
		Verify verifiedBilling = previewPage.getNewCardSection().getNewAddressSection().verifyEditedBillingAddress(getNewBillingAddressFromConfig());
		assertTrue(verifiedBilling.isVerified(), verifiedBilling.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void enterNewCreditCardFromManageCardsPageEnterBilling(WebDriver driver) throws Exception {
		ManageCreditCardsPage manageCardsPage = navigateToManageCardsPage(driver);

		manageCardsPage.addNewCreditCard(config.getString("usc.cc2.cardHolderName"), config.getString("usc.cc2.cardNumber"), config.getString("usc.cc2.exp.month"),
				config.getInt("usc.cc2.exp.year"), config.getString("usc.cc2.cv2"), config.getBoolean("usc.cc2.isDefault"), getNewBillingAddressFromConfig());
		// check that there weren't any errors
		assertTrue(manageCardsPage.verifyNoErrors(), "error encountered when adding creditCard.");
		manageCardsPage.waitForEditCardsSpinner();
		manageCardsPage.waitCardAddedSpinner();
		manageCardsPage.waitForDefaultCreditCard();

		// verify first saved card on page is what we just added
		Verify verified = manageCardsPage.verifyFirstCreditCardInList(new SavedCreditCard(config.getString("usc.cc2.cardHolderName"), CreditCardType.VISA,
				"***1111", config.getString("usc.cc2.exp.month") + "." + config.getInt("usc.cc2.exp.year"), config.getBoolean("usc.cc2.isDefault")));
		assertTrue(verified.isVerified(), verified.getError());

		// verify the billing address
		manageCardsPage.clickEditFirstCreditCard();
		Verify verifiedBilling = manageCardsPage.getNewCardSection().getNewAddressSection().verifyEditedBillingAddress(getNewBillingAddressFromConfig());
		assertTrue(verifiedBilling.isVerified(), verifiedBilling.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveAllFieldsBlank(WebDriver driver) throws Exception {
		ManageCreditCardsPage manageCardsPage = navigateToManageCardsPage(driver);

		manageCardsPage.addNewCreditCard("", "", null, null, "", null, null);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.creditCard.error.allFields"));

		assertTrue(manageCardsPage.getNewCardSection().verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + manageCardsPage.getNewCardSection().getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveCardNumberBlank(WebDriver driver) throws Exception {
		ManageCreditCardsPage manageCardsPage = navigateToManageCardsPage(driver);

		manageCardsPage.addNewCreditCard(config.getString("usc.cc2.cardHolderName"), "", config.getString("usc.cc2.exp.month"),
				config.getInt("usc.cc2.exp.year"), config.getString("usc.cc2.cv2"), true, null);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.creditCard.error.cardNumberEmpty"));

		assertTrue(manageCardsPage.getNewCardSection().verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + manageCardsPage.getNewCardSection().getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void cardNumberInvalid(WebDriver driver) throws Exception {
		ManageCreditCardsPage manageCardsPage = navigateToManageCardsPage(driver);

		manageCardsPage.addNewCreditCard(config.getString("usc.cc2.cardHolderName"), "4111", config.getString("usc.cc2.exp.month"),
				config.getInt("usc.cc2.exp.year"), config.getString("usc.cc2.cv2"), true, null);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.creditCard.error.cardNumberInvalid"));

		assertTrue(manageCardsPage.getNewCardSection().verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + manageCardsPage.getNewCardSection().getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void expirationMonthBlank(WebDriver driver) throws Exception {
		ManageCreditCardsPage manageCardsPage = navigateToManageCardsPage(driver);

		manageCardsPage.addNewCreditCard(config.getString("usc.cc2.cardHolderName"), config.getString("usc.cc2.cardNumber"), null,
				config.getInt("usc.cc2.exp.year"), config.getString("usc.cc2.cv2"), true, null);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.creditCard.error.expirationDate"));

		assertTrue(manageCardsPage.getNewCardSection().verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + manageCardsPage.getNewCardSection().getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void expirationYearBlank(WebDriver driver) throws Exception {
		ManageCreditCardsPage manageCardsPage = navigateToManageCardsPage(driver);

		manageCardsPage.addNewCreditCard(config.getString("usc.cc2.cardHolderName"), config.getString("usc.cc2.cardNumber"), config.getString("usc.cc2.exp.month"),
				null, config.getString("usc.cc2.cv2"), true, null);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.creditCard.error.expirationDate"));

		assertTrue(manageCardsPage.getNewCardSection().verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + manageCardsPage.getNewCardSection().getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void expirationBlank(WebDriver driver) throws Exception {
		ManageCreditCardsPage manageCardsPage = navigateToManageCardsPage(driver);

		manageCardsPage.addNewCreditCard(config.getString("usc.cc2.cardHolderName"), config.getString("usc.cc2.cardNumber"), null,
				null, config.getString("usc.cc2.cv2"), true, null);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.creditCard.error.expirationDate"));

		assertTrue(manageCardsPage.getNewCardSection().verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + manageCardsPage.getNewCardSection().getErrorsOnPage());
	}

	//@Test(dataProvider = "getWebDriver")
	// can't enter a date in the past right now since its Jan 2015
	public void expiredCard(WebDriver driver) throws Exception {
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveCvvBlank(WebDriver driver) throws Exception {
		ManageCreditCardsPage manageCardsPage = navigateToManageCardsPage(driver);

		manageCardsPage.addNewCreditCard(config.getString("usc.cc2.cardHolderName"), config.getString("usc.cc2.cardNumber"), config.getString("usc.cc2.exp.month"),
				config.getInt("usc.cc2.exp.year"), "", true, null);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.creditCard.error.cvvEmpty"));

		assertTrue(manageCardsPage.getNewCardSection().verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + manageCardsPage.getNewCardSection().getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void cvvInvalid(WebDriver driver) throws Exception {
		ManageCreditCardsPage manageCardsPage = navigateToManageCardsPage(driver);

		manageCardsPage.addNewCreditCard(config.getString("usc.cc2.cardHolderName"), config.getString("usc.cc2.cardNumber"), config.getString("usc.cc2.exp.month"),
				config.getInt("usc.cc2.exp.year"), "0000", true, null);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.creditCard.error.cvvInvalid"));

		assertTrue(manageCardsPage.getNewCardSection().verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + manageCardsPage.getNewCardSection().getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void changeExpirationDateMonth(WebDriver driver) {
		ManageCreditCardsPage manageCardsPage = addCreditCard(driver);

		manageCardsPage.updateFirstCreditCard("5", null, config.getString("usc.cc2.cv2"), null);
		manageCardsPage.waitForEditCardsSpinner();
		manageCardsPage.waitCardAddedSpinner();
		manageCardsPage.waitForDefaultCreditCard();
		Verify updateVerified = manageCardsPage.verifyFirstCreditCardInList(new SavedCreditCard(config.getString("usc.cc2.cardHolderName"), CreditCardType.VISA,
				"***1111", 5 + "." + config.getInt("usc.cc2.exp.year"), config.getBoolean("usc.cc2.isDefault")));
		assertTrue(manageCardsPage.verifyNoErrors(), "error encountered when adding creditCard.");
		assertTrue(updateVerified.isVerified(), updateVerified.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void changeExpirationDateYear(WebDriver driver) {
		ManageCreditCardsPage manageCardsPage = addCreditCard(driver);

		manageCardsPage.updateFirstCreditCard(null, 2018, config.getString("usc.cc2.cv2"), null);
		manageCardsPage.waitForEditCardsSpinner();
		manageCardsPage.waitCardAddedSpinner();
		manageCardsPage.waitForDefaultCreditCard();
		Verify updateVerified = manageCardsPage.verifyFirstCreditCardInList(new SavedCreditCard(config.getString("usc.cc2.cardHolderName"), CreditCardType.VISA,
				"***1111", config.getString("usc.cc2.exp.month") + "." + 2018, config.getBoolean("usc.cc2.isDefault")));
		assertTrue(manageCardsPage.verifyNoErrors(), "error encountered when adding creditCard.");
		assertTrue(updateVerified.isVerified(), updateVerified.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void changeExpirationDate(WebDriver driver) {
		ManageCreditCardsPage manageCardsPage = addCreditCard(driver);

		manageCardsPage.updateFirstCreditCard("5", 2018, config.getString("usc.cc2.cv2"), null);
		manageCardsPage.waitForEditCardsSpinner();
		manageCardsPage.waitCardAddedSpinner();
		manageCardsPage.waitForDefaultCreditCard();
		Verify updateVerified = manageCardsPage.verifyFirstCreditCardInList(new SavedCreditCard(config.getString("usc.cc2.cardHolderName"), CreditCardType.VISA,
				"***1111", 5 + "." + 2018, config.getBoolean("usc.cc2.isDefault")));
		assertTrue(manageCardsPage.verifyNoErrors(), "error encountered when adding creditCard.");
		assertTrue(updateVerified.isVerified(), updateVerified.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void changeCvv(WebDriver driver) {
		ManageCreditCardsPage manageCardsPage = addCreditCard(driver);

		manageCardsPage.updateFirstCreditCard(null, null, "156", null);
		manageCardsPage.waitForEditCardsSpinner();
		manageCardsPage.waitCardAddedSpinner();
		manageCardsPage.waitForDefaultCreditCard();
		Verify updateVerified = manageCardsPage.verifyFirstCreditCardInList(new SavedCreditCard(config.getString("usc.cc2.cardHolderName"), CreditCardType.VISA,
				"***1111", config.getString("usc.cc2.exp.month") + "." + config.getInt("usc.cc2.exp.year"), config.getBoolean("usc.cc2.isDefault")));
		assertTrue(manageCardsPage.verifyNoErrors(), "error encountered when adding creditCard.");
		assertTrue(updateVerified.isVerified(), updateVerified.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void changeExpirationLeaveCvvBlank(WebDriver driver) {
		ManageCreditCardsPage manageCardsPage = addCreditCard(driver);

		manageCardsPage.updateFirstCreditCard("5", 2018, null, null);
		List<String> expectedErrors = Arrays.asList(config.getStringArray("daigou.creditCard.error.cvvEmpty"));

		assertTrue(manageCardsPage.getNewCardSection().verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + manageCardsPage.getNewCardSection().getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void editBillingAddress(WebDriver driver) {
		ManageCreditCardsPage manageCardsPage = addCreditCard(driver);

		manageCardsPage.updateFirstCreditCard(null, null, null, getNewBillingAddress2FromConfig());
		manageCardsPage.waitForEditCardsSpinner();
		manageCardsPage.waitCardAddedSpinner();
		manageCardsPage.waitForDefaultCreditCard();

		// verify the billing address was updated
		manageCardsPage.clickEditFirstCreditCard();
		Verify verifiedBilling = manageCardsPage.getNewCardSection().getNewAddressSection().verifyEditedBillingAddress(getNewBillingAddress2FromConfig());
		assertTrue(verifiedBilling.isVerified(), verifiedBilling.getError());
	}

	private PurchasePreviewPage goToPurchasePreviewPage(WebDriver driver)
			throws Exception {
		LOG.debug("going to sugou page...");
		driver.get(getSugouUrl());

		LoginPage loginPage = new LoginPage(driver);
		LOG.debug("Logging in to sugou.");
		loginPage.login(config.getString("daigou.creditCardUserEmail"), config.getString("daigou.creditCardUserPassword"));

		CartSummarySection cartSummary = new CartSummarySection(driver);
		LOG.debug("Clicking Purchase Preview Button.");
		cartSummary.clickPurchasePreviewBtn();

		LOG.debug("Waiting for Purchase Preview page to load...");
		waitForPurchasePreviewPage(driver);

		PurchasePreviewPage previewPage = new PurchasePreviewPage(driver);
		LOG.debug("Selecting credit card payment method on purchase preview page.");
		previewPage.selectPaymentMethod("creditCard");
		LOG.info("Verifying that Add New Credit Card Button is present and clickable.");
		assertTrue(previewPage.verifyAddNewCardBtnPresent(), "expected add new credit card button to be found but was not present on page");
		LOG.info("Verifying that Manage Credit Cards Button is present and clickable.");
		assertTrue(previewPage.verifyManageCreditCardsBtnPresent(), "expected manange credit cards button to be found but was not present on the page");
		return previewPage;
	}

	private ManageCreditCardsPage navigateToManageCardsPage(WebDriver driver) {
		loginToSugou(driver, config.getString("daigou.creditCardUserEmail"), config.getString("daigou.creditCardUserPassword"));

		driver.get(getCreditCardsUrl());

		ManageCreditCardsPage manageCardsPage = new ManageCreditCardsPage(driver);
		manageCardsPage.waitForAddNewCreditCardBtn();

		return manageCardsPage;
	}

	private ManageCreditCardsPage addCreditCard(WebDriver driver) {
		ManageCreditCardsPage manageCardsPage = navigateToManageCardsPage(driver);

		manageCardsPage.addNewCreditCard(config.getString("usc.cc2.cardHolderName"), config.getString("usc.cc2.cardNumber"), config.getString("usc.cc2.exp.month"),
				config.getInt("usc.cc2.exp.year"), config.getString("usc.cc2.cv2"), config.getBoolean("usc.cc2.isDefault"), null);
		// check that there weren't any errors
		assertTrue(manageCardsPage.verifyNoErrors(), "error encountered when adding creditCard.");
		manageCardsPage.waitForEditCardsSpinner();
		manageCardsPage.waitCardAddedSpinner();
		manageCardsPage.waitForDefaultCreditCard();
		// verify first saved card on page is what we just added
		Verify verified = manageCardsPage.verifyFirstCreditCardInList(new SavedCreditCard(config.getString("usc.cc2.cardHolderName"), CreditCardType.VISA,
				"***1111", config.getString("usc.cc2.exp.month") + "." + config.getInt("usc.cc2.exp.year"), config.getBoolean("usc.cc2.isDefault")));
		assertTrue(verified.isVerified(), verified.getError());
		return manageCardsPage;
	}

	private SavedAddress getNewBillingAddressFromConfig() {
		return new SavedAddress(config.getString("usc.billingAddr.firstName"), config.getString("usc.billingAddr.lastName"),
				config.getString("usc.billingAddr.address1"), null, config.getString("usc.billingAddr.city"), config.getString("usc.billingAddr.state"),
				config.getString("usc.billingAddr.zip"), config.getString("usc.billingAddr.country"), config.getString("usc.billingAddr.phone"));
	}

	private SavedAddress getNewBillingAddress2FromConfig() {
		return new SavedAddress(config.getString("usc.billingAddr2.firstName"), config.getString("usc.billingAddr2.lastName"),
				config.getString("usc.billingAddr2.address1"), null, config.getString("usc.billingAddr2.city"), config.getString("usc.billingAddr2.state"),
				config.getString("usc.billingAddr2.zip"), config.getString("usc.billingAddr2.country"), config.getString("usc.billingAddr2.phone"));
	}

	@Test(dataProvider = "getWebDriver")
	public void enterNewCreditCardVisa(WebDriver driver) throws Exception {
		PurchasePreviewPage previewPage = goToPurchasePreviewPage(driver);

		previewPage.addNewCreditCard(config.getString("usc.cc.cardHolderName"), config.getString("usc.cc.cardNumberVisa"), config.getString("usc.cc.exp.month"),
				config.getInt("usc.cc.exp.year"), config.getString("usc.cc.cv2"), config.getBoolean("usc.cc.isDefault"), null);
		// check that there weren't any errors
		assertTrue(previewPage.verifyNoCreditCardErrors(), "error encountered when adding creditCard.");
		// check that card was added
		previewPage.waitForSaveCardSpinner();

		SavedCreditCard firstCard = previewPage.getFirstCreditCard();
		assertTrue(firstCard.getType() == CreditCardType.VISA, "Expected credit card type to be VISA but found : " + firstCard.getType());
		assertTrue(firstCard.getMaskedNumber().equals("***4242"), "Expected credit card masked # to be ***4242 but found : " + firstCard.getMaskedNumber());
	}

	@Test(dataProvider = "getWebDriver")
	public void enterNewCreditCardMastercard(WebDriver driver) throws Exception {
		PurchasePreviewPage previewPage = goToPurchasePreviewPage(driver);

		previewPage.addNewCreditCard(config.getString("usc.cc.cardHolderName"), config.getString("usc.cc.cardNumberMc"), config.getString("usc.cc.exp.month"),
				config.getInt("usc.cc.exp.year"), config.getString("usc.cc.cv2"), config.getBoolean("usc.cc.isDefault"), null);
		// check that there weren't any errors
		assertTrue(previewPage.verifyNoCreditCardErrors(), "error encountered when adding creditCard.");
		// check that card was added
		previewPage.waitForSaveCardSpinner();

		SavedCreditCard firstCard = previewPage.getFirstCreditCard();
		assertTrue(firstCard.getType() == CreditCardType.MASTER, "Expected credit card type to be MASTERCARD but found : " + firstCard.getType());
		assertTrue(firstCard.getMaskedNumber().equals("***4444"), "Expected credit card masked # to be ***4444 but found : " + firstCard.getMaskedNumber());
	}

	@Test(dataProvider = "getWebDriver")
	public void enterNewCreditCardDiscover(WebDriver driver) throws Exception {
		PurchasePreviewPage previewPage = goToPurchasePreviewPage(driver);

		previewPage.addNewCreditCard(config.getString("usc.cc.cardHolderName"), config.getString("usc.cc.cardNumberDiscover"), config.getString("usc.cc.exp.month"),
				config.getInt("usc.cc.exp.year"), config.getString("usc.cc.cv2"), config.getBoolean("usc.cc.isDefault"), null);
		// check that there weren't any errors
		assertTrue(previewPage.verifyNoCreditCardErrors(), "error encountered when adding creditCard.");
		// check that card was added
		previewPage.waitForSaveCardSpinner();

		SavedCreditCard firstCard = previewPage.getFirstCreditCard();
		assertTrue(firstCard.getType() == CreditCardType.DISCOVER, "Expected credit card type to be DISCOVER but found : " + firstCard.getType());
		assertTrue(firstCard.getMaskedNumber().equals("***1117"), "Expected credit card masked # to be ***1117 but found : " + firstCard.getMaskedNumber());
	}

	@Test(dataProvider = "getWebDriver")
	public void enterNewCreditCardAmEx(WebDriver driver) throws Exception {
		PurchasePreviewPage previewPage = goToPurchasePreviewPage(driver);

		previewPage.addNewCreditCard(config.getString("usc.cc.cardHolderName"), config.getString("usc.cc.cardNumberAmEx"), config.getString("usc.cc.exp.month"),
				config.getInt("usc.cc.exp.year"), config.getString("usc.cc.cv2AmEx"), config.getBoolean("usc.cc.isDefault"), null);
		// check that there weren't any errors
		assertTrue(previewPage.verifyNoCreditCardErrors(), "error encountered when adding creditCard.");
		// check that card was added
		previewPage.waitForSaveCardSpinner();

		SavedCreditCard firstCard = previewPage.getFirstCreditCard();
		assertTrue(firstCard.getType() == CreditCardType.AMEX, "Expected credit card type to be AMEX but found : " + firstCard.getType());
		assertTrue(firstCard.getMaskedNumber().equals("***0005"), "Expected credit card masked # to be ***0005 but found : " + firstCard.getMaskedNumber());
	}

	@Override
	protected void waitForPurchasePreviewPage(WebDriver driver) {
		CartSummarySection cartSummary = new CartSummarySection(driver);

		cartSummary.waitForProgressBar();
		cartSummary.waitForPurchaseBtn();
	}

	@Override
	@BeforeMethod
	public void cleanup() {
		clearAllCreditCardsCleanup(config.getString("daigou.creditCardUserEmail"), config.getString("daigou.creditCardUserPassword"));
	}

	@BeforeClass
	public void ensureProductInCart() throws Exception{
		ensureProductInCart(config.getString("daigou.creditCardUserEmail"), config.getString("daigou.creditCardUserPassword"));
	}

}
