package com.extrabux.tests.clo;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.clo.CloCreditCardSection;
import com.extrabux.pages.clo.EbatesCreditCardPage;
import com.extrabux.pages.daigou.CreditCardType;
import com.extrabux.pages.daigou.SavedAddress;
import com.extrabux.pages.daigou.SavedCreditCard;
import com.extrabux.pages.daigou.Verify;

public class CloCreditCardTests extends CloBaseTest {
	//private static final Log LOG = LogFactory.getLog(NewCreditCardTests.class);

	@Test(dataProvider = "getWebDriver")
	public void leaveAllFieldsBlank(WebDriver driver) throws Exception {
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit("", null, null, "" , null, null, false);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.allFields"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}
     
	@Test(dataProvider = "getWebDriver")
	public void leaveCardNumberBlank(WebDriver driver) throws Exception {
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit("", config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), true, getNewBillingAddressFromConfig(), false);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.cardNumberEmpty"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void cardNumberInvalid(WebDriver driver) throws Exception {
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit("4111", config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), true, getNewBillingAddressFromConfig());

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.cardNumberInvalid"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void expirationMonthBlank(WebDriver driver) throws Exception {
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), null,
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), true, getNewBillingAddressFromConfig());

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.expirationDate"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void expirationYearBlank(WebDriver driver) throws Exception {
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				null, config.getString("clo.cc.cv2"), true, getNewBillingAddressFromConfig());

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.expirationDate"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void expirationBlank(WebDriver driver) throws Exception {
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), null, null, config.getString("clo.cc.cv2"), true, getNewBillingAddressFromConfig());

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.expirationDate"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveCvvBlank(WebDriver driver) throws Exception {
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), "", true, getNewBillingAddressFromConfig());

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.cvvEmpty"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void cvvInvalid(WebDriver driver) throws Exception {
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), "0000", true, getNewBillingAddressFromConfig());

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.cvvInvalid"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveNonRequiredFieldsBlank(WebDriver driver) throws Exception {
		SavedAddress newAddress = new SavedAddress("", config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));;
				CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

				openNewCardPage(driver);

				newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
						config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

				List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.firstName"));

				assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
						+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveFirstNameBlank(WebDriver driver) throws Exception {
		SavedAddress newAddress = new SavedAddress("", config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));;
				CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

				openNewCardPage(driver);

				newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
						config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

				List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.firstName"));

				assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
						+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveLastNameBlank(WebDriver driver) throws Exception {
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), "",
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.lastName"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveAddress1Blank(WebDriver driver) throws Exception {
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				"", null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.address1"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveCityBlank(WebDriver driver) throws Exception {
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, "", config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.city"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void leaveZipBlank(WebDriver driver) throws Exception {
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				"", config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.zipEmpty"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void invalidZipLength(WebDriver driver) throws Exception {
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				"2", config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.zipInvalid"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void invalidZipNonNumbers(WebDriver driver) throws Exception {
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				"zip69", config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.zipInvalid"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void leavePNumBlank(WebDriver driver) throws Exception {
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), "");
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.phoneEmpty"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void invalidPNumLength(WebDriver driver) throws Exception {
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), "2");
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.phoneInvalid"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void invalidPNumNonNumbers(WebDriver driver) throws Exception {
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), "asdfghjkl;");
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		List<String> expectedErrors = Arrays.asList(config.getStringArray("clo.error.phoneInvalid"));

		assertTrue(newCardPage.verifyErrors(expectedErrors), "errors mismatch.  expected: " + expectedErrors
				+ " actual: " + newCardPage.getErrorsOnPage());
	}

	@Test(dataProvider = "getWebDriver")
	public void onlyLinkablesTosChecked(WebDriver driver) throws Exception {
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardPage = new CloCreditCardSection(driver);

		openNewCardPage(driver);

		newCardPage.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress, false);

		List<String> expectedHighlightedFields = Arrays.asList(config.getStringArray("clo.error.linkablesTos"));

		assertTrue(newCardPage.verifyHighlightedFields(expectedHighlightedFields), "errors fields mismatch.  expected: " + expectedHighlightedFields
				+ " actual: " + newCardPage.getHighlightedFieldIds());
	}

	@Test(dataProvider = "getWebDriver")
	public void addVisa(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumber");
		String cardNumberLastFour = cardNumber.split(" ")[3];
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.VISA, cardNumberLastFour);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(cardNumber, config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		confirmAndCloseCardDialog(driver, CreditCardType.VISA);

		Verify verify = cardPage.verifyCard(newCard);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void addMastercard(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumberMc");
		String cardNumberLastFour = cardNumber.split(" ")[3];
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.MAST, cardNumberLastFour);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumberMc"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress, false);

		confirmAndCloseCardDialog(driver, CreditCardType.MAST);

		Verify verify = cardPage.verifyCard(newCard);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void addDiscover(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumberDiscover");
		String cardNumberLastFour = cardNumber.split(" ")[3];
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.DISC, cardNumberLastFour);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumberDiscover"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress, false);

		confirmAndCloseCardDialog(driver, CreditCardType.DISC);

		Verify verify = cardPage.verifyCard(newCard);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void addAmEx(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumberAmEx");
		String cardNumberLastFour = cardNumber.substring(cardNumber.length() - 3);
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.AMEX, cardNumberLastFour);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumberAmEx"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2AmEx"), config.getBoolean("clo.cc.isDefault"), newAddress);

		confirmAndCloseCardDialog(driver, CreditCardType.AMEX);

		Thread.sleep(2000);

		Verify verify = cardPage.verifyCard(newCard);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void addTwoUniqueCards(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumber");
		String cardNumber1LastFour = cardNumber.split(" ")[3];
		cardNumber = config.getString("clo.cc.cardNumberMc");
		String cardNumber2LastFour = cardNumber.split(" ")[3];
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		SavedAddress newAddress2 = new SavedAddress(config.getString("clo.ccAddr2.firstName"), config.getString("clo.ccAddr2.lastName"),
				config.getString("clo.ccAddr2.address1"), null, config.getString("clo.ccAddr2.city"), config.getString("clo.ccAddr2.state"),
				config.getString("clo.ccAddr2.zip"), config.getString("clo.ccAddr2.country"), config.getString("clo.ccAddr2.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		List<SavedCreditCard> newCards = new ArrayList<SavedCreditCard>();
		newCards.add(new SavedCreditCard(CreditCardType.VISA, cardNumber1LastFour));
		newCards.add(new SavedCreditCard(CreditCardType.MAST, cardNumber2LastFour));
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		confirmAndCloseCardDialog(driver, CreditCardType.VISA);

		openNewCardDialog(driver, cardPage);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumberMc"), config.getString("clo.cc2.exp.month"),
				config.getInt("clo.cc2.exp.year"), config.getString("clo.cc2.cv2"), config.getBoolean("clo.cc2.isDefault"), newAddress2, false);

		confirmAndCloseCardDialog(driver, CreditCardType.MAST);

		Verify verify = cardPage.verifyCards(newCards);
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void updateBillingAddressVisa(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumber");
		String cardNumberLastFour = cardNumber.split(" ")[3];
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		SavedAddress newAddress2 = new SavedAddress(config.getString("clo.ccAddr2.firstName"), config.getString("clo.ccAddr2.lastName"),
				config.getString("clo.ccAddr2.address1"), null, config.getString("clo.ccAddr2.city"), config.getString("clo.ccAddr2.state"),
				config.getString("clo.ccAddr2.zip"), config.getString("clo.ccAddr2.country"), config.getString("clo.ccAddr2.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.VISA, cardNumberLastFour);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		confirmAndCloseCardDialog(driver, CreditCardType.VISA);

		updateFirstBillingAddress(driver, cardPage, newAddress2);

		confirmAndCloseCardUpdatedDialog(driver);

		Verify verifyCard = cardPage.verifyCard(newCard);
		Verify verifyAddr = cardPage.verifyBillingAddress(newAddress2);
		assertTrue(verifyCard.isVerified(), verifyCard.getError());
		assertTrue(verifyAddr.isVerified(), verifyAddr.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void updateBillingAddressMastercard(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumberMc");
		String cardNumberLastFour = cardNumber.split(" ")[3];
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		SavedAddress newAddress2 = new SavedAddress(config.getString("clo.ccAddr2.firstName"), config.getString("clo.ccAddr2.lastName"),
				config.getString("clo.ccAddr2.address1"), null, config.getString("clo.ccAddr2.city"), config.getString("clo.ccAddr2.state"),
				config.getString("clo.ccAddr2.zip"), config.getString("clo.ccAddr2.country"), config.getString("clo.ccAddr2.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.MAST, cardNumberLastFour);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumberMc"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress, false);

		confirmAndCloseCardDialog(driver, CreditCardType.MAST);

		updateFirstBillingAddress(driver, cardPage, newAddress2);

		confirmAndCloseCardUpdatedDialog(driver);

		Verify verifyCard = cardPage.verifyCard(newCard);
		Verify verifyAddr = cardPage.verifyBillingAddress(newAddress2);
		assertTrue(verifyCard.isVerified(), verifyCard.getError());
		assertTrue(verifyAddr.isVerified(), verifyAddr.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void updateBillingAddressDiscover(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumberDiscover");
		String cardNumberLastFour = cardNumber.split(" ")[3];
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		SavedAddress newAddress2 = new SavedAddress(config.getString("clo.ccAddr2.firstName"), config.getString("clo.ccAddr2.lastName"),
				config.getString("clo.ccAddr2.address1"), null, config.getString("clo.ccAddr2.city"), config.getString("clo.ccAddr2.state"),
				config.getString("clo.ccAddr2.zip"), config.getString("clo.ccAddr2.country"), config.getString("clo.ccAddr2.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.DISC, cardNumberLastFour);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumberDiscover"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress, false);

		confirmAndCloseCardDialog(driver, CreditCardType.DISC);

		updateFirstBillingAddress(driver, cardPage, newAddress2);

		confirmAndCloseCardUpdatedDialog(driver);

		Verify verifyCard = cardPage.verifyCard(newCard);
		Verify verifyAddr = cardPage.verifyBillingAddress(newAddress2);
		assertTrue(verifyCard.isVerified(), verifyCard.getError());
		assertTrue(verifyAddr.isVerified(), verifyAddr.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void updateBillingAddressAmEx(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumberAmEx");
		String cardNumberLastFour = cardNumber.substring(cardNumber.length() - 3);
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		SavedAddress newAddress2 = new SavedAddress(config.getString("clo.ccAddr2.firstName"), config.getString("clo.ccAddr2.lastName"),
				config.getString("clo.ccAddr2.address1"), null, config.getString("clo.ccAddr2.city"), config.getString("clo.ccAddr2.state"),
				config.getString("clo.ccAddr2.zip"), config.getString("clo.ccAddr2.country"), config.getString("clo.ccAddr2.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.AMEX, cardNumberLastFour);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumberAmEx"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2AmEx"), config.getBoolean("clo.cc.isDefault"), newAddress);

		confirmAndCloseCardDialog(driver, CreditCardType.AMEX);

		updateFirstBillingAddress(driver, cardPage, newAddress2);

		confirmAndCloseCardUpdatedDialog(driver);

		Verify verifyCard = cardPage.verifyCard(newCard);
		Verify verifyAddr = cardPage.verifyBillingAddress(newAddress2);
		assertTrue(verifyCard.isVerified(), verifyCard.getError());
		assertTrue(verifyAddr.isVerified(), verifyAddr.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void deleteVisa(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumber");
		String cardNumberLastFour = cardNumber.split(" ")[3];
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.VISA, cardNumberLastFour);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		confirmAndCloseCardDialog(driver, CreditCardType.VISA);

		Verify verify = cardPage.verifyCard(newCard);
		assertTrue(verify.isVerified(), verify.getError());

		cardPage.deleteCreditCard();

		verify = cardPage.verifyNoCards();
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void deleteMastercard(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumberMc");
		String cardNumberLastFour = cardNumber.split(" ")[3];
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.MAST, cardNumberLastFour);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumberMc"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress, false);

		confirmAndCloseCardDialog(driver, CreditCardType.MAST);

		Verify verify = cardPage.verifyCard(newCard);
		assertTrue(verify.isVerified(), verify.getError());

		cardPage.deleteCreditCard();

		verify = cardPage.verifyNoCards();
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void deleteDiscover(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumberDiscover");
		String cardNumberLastFour = cardNumber.split(" ")[3];
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.DISC, cardNumberLastFour);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumberDiscover"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress, false);

		confirmAndCloseCardDialog(driver, CreditCardType.DISC);

		Verify verify = cardPage.verifyCard(newCard);
		assertTrue(verify.isVerified(), verify.getError());

		cardPage.deleteCreditCard();

		verify = cardPage.verifyNoCards();
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void deleteAmEx(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumberAmEx");
		String cardNumberLastFour = cardNumber.substring(cardNumber.length() - 3);
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.AMEX, cardNumberLastFour);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumberAmEx"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2AmEx"), config.getBoolean("clo.cc.isDefault"), newAddress);

		confirmAndCloseCardDialog(driver, CreditCardType.AMEX);

		Verify verify = cardPage.verifyCard(newCard);
		assertTrue(verify.isVerified(), verify.getError());

		cardPage.deleteCreditCard();

		verify = cardPage.verifyNoCards();
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void deleteMultipleCards(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.cardNumber");
		String cardNumber1LastFour = cardNumber.split(" ")[3];
		cardNumber = config.getString("clo.cc.cardNumberMc");
		String cardNumber2LastFour = cardNumber.split(" ")[3];
		cardNumber = config.getString("clo.cc.cardNumberDiscover");
		String cardNumber3LastFour = cardNumber.split(" ")[3];
		cardNumber = config.getString("clo.cc.cardNumberAmEx");
		String cardNumber4LastFour = cardNumber.substring(cardNumber.length() - 3);
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		SavedAddress newAddress2 = new SavedAddress(config.getString("clo.ccAddr2.firstName"), config.getString("clo.ccAddr2.lastName"),
				config.getString("clo.ccAddr2.address1"), null, config.getString("clo.ccAddr2.city"), config.getString("clo.ccAddr2.state"),
				config.getString("clo.ccAddr2.zip"), config.getString("clo.ccAddr2.country"), config.getString("clo.ccAddr2.phone"));
		SavedAddress newAddress3 = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		SavedAddress newAddress4 = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		List<SavedCreditCard> newCards = new ArrayList<SavedCreditCard>();
		newCards.add(new SavedCreditCard(CreditCardType.VISA, cardNumber1LastFour));
		newCards.add(new SavedCreditCard(CreditCardType.MAST, cardNumber2LastFour));
		newCards.add(new SavedCreditCard(CreditCardType.DISC, cardNumber3LastFour));
		newCards.add(new SavedCreditCard(CreditCardType.AMEX, cardNumber4LastFour));
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumber"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		confirmAndCloseCardDialog(driver, CreditCardType.VISA);

		openNewCardDialog(driver, cardPage);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumberMc"), config.getString("clo.cc2.exp.month"),
				config.getInt("clo.cc2.exp.year"), config.getString("clo.cc2.cv2"), config.getBoolean("clo.cc2.isDefault"), newAddress2, false);

		confirmAndCloseCardDialog(driver, CreditCardType.MAST);

		openNewCardDialog(driver, cardPage);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumberDiscover"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc2.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc2.isDefault"), newAddress3, false);

		confirmAndCloseCardDialog(driver, CreditCardType.DISC);

		openNewCardDialog(driver, cardPage);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.cardNumberAmEx"), config.getString("clo.cc2.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2AmEx"), config.getBoolean("clo.cc2.isDefault"), newAddress4);

		confirmAndCloseCardDialog(driver, CreditCardType.AMEX);

		Verify verify = cardPage.verifyCards(newCards);
		assertTrue(verify.isVerified(), verify.getError());

		cardPage.deleteAllCards();

		verify = cardPage.verifyNoCards();
		assertTrue(verify.isVerified(), verify.getError());
	}

	@Test(dataProvider = "getWebDriver")
	public void linkVisa(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.validVisa");
		String cardNumberLastFour = cardNumber.split(" ")[3];
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);
		try {
			SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
					config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
					config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
			CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
			SavedCreditCard newCard = new SavedCreditCard(CreditCardType.VISA, cardNumberLastFour, true);


			openNewCardPage(driver);

			newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.validVisa"), config.getString("clo.cc.exp.month"),
					config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

			confirmAndCloseCardDialog(driver, CreditCardType.VISA);

			Verify verify = cardPage.verifyCardEnabled(newCard);
			assertTrue(verify.isVerified(), verify.getError());
		} finally {
			cardPage.goToURL(getNewCardUrl());

			cardPage.deleteAllCards();
		}
	}

	@Test(dataProvider = "getWebDriver")
	public void cantLinkMastercard(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.validMc");
		String cardNumberLastFour = cardNumber.split(" ")[3];
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.MAST, cardNumberLastFour, true);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.validMc"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		confirmAndCloseCardDialog(driver, CreditCardType.MAST);

		Verify verify = cardPage.verifyCardEnabled(newCard);
		assertTrue(verify.isVerified(), verify.getError());

		cardPage.deleteAllCards();
	}

	@Test(dataProvider = "getWebDriver")
	public void cantLinkDiscover(WebDriver driver) throws Exception {
		String cardNumber = config.getString("clo.cc.validDiscover");
		String cardNumberLastFour = cardNumber.split(" ")[3];
		SavedAddress newAddress = new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		SavedCreditCard newCard = new SavedCreditCard(CreditCardType.DISC, cardNumberLastFour, true);
		EbatesCreditCardPage cardPage = new EbatesCreditCardPage(driver);

		openNewCardPage(driver);

		newCardSection.fillCreditCardInfoAndSubmit(config.getString("clo.cc.validDiscover"), config.getString("clo.cc.exp.month"),
				config.getInt("clo.cc.exp.year"), config.getString("clo.cc.cv2"), config.getBoolean("clo.cc.isDefault"), newAddress);

		confirmAndCloseCardDialog(driver, CreditCardType.DISC);

		Verify verify = cardPage.verifyCardEnabled(newCard);
		assertTrue(verify.isVerified(), verify.getError());

		cardPage.deleteAllCards();
	}
}
