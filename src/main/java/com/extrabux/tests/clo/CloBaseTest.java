package com.extrabux.tests.clo;

import static org.testng.Assert.assertTrue;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.BeforeClass;

import com.extrabux.pages.BasePage;
import com.extrabux.pages.clo.CloCreditCardSection;
import com.extrabux.pages.clo.EbatesCreditCardPage;
import com.extrabux.pages.clo.EbatesLogInSection;
import com.extrabux.pages.clo.EbatesSignUpSection;
import com.extrabux.pages.daigou.CreditCardType;
import com.extrabux.pages.daigou.SavedAddress;
import com.extrabux.pages.daigou.SavedCreditCard;
import com.extrabux.tests.BaseTest;

public class CloBaseTest extends BaseTest {
	private static final Log LOG = LogFactory.getLog(CloBaseTest.class);
	final String NEW_CARD_URI = "my-wallet.htm";
	final String EDIT_CARD_URI = "/?uid=100&ccid=55049";
	final String SIGN_UP_LOGIN_URI = "auth/logon.do";

	String cloServer;

	@BeforeClass
	public void getCloServerName() {
		LOG.info("Setting CLO server name...");

		cloServer = "https://qa-www.ebates.com/";

		LOG.info("CLO server: " + cloServer);
	}

	protected void cleanup() {
		cleanup(new FirefoxDriver());
	}

	protected void cleanup(WebDriver driver) {
		LOG.info("Closing the driver; ");
		driver.quit();
	}

	protected EbatesCreditCardPage navigateToNewCardsPage(WebDriver driver) {
		BasePage page = new BasePage(driver);
		page.goToURL(getNewCardUrl());

		return new EbatesCreditCardPage(driver);
	}

	protected EbatesCreditCardPage navigateToEditCardsPage(WebDriver driver) {
		BasePage page = new BasePage(driver);
		page.goToURL(getEditCardUrl());

		return new EbatesCreditCardPage(driver);
	}

	public String getNewCardUrl() {
		LOG.debug("Getting CLO New Card URL: " + cloServer + NEW_CARD_URI);
		return cloServer + NEW_CARD_URI;
	}

	public String getEditCardUrl() {
		LOG.debug("Getting CLO Edit Card URL: https://" + cloServer + EDIT_CARD_URI);
		return cloServer + EDIT_CARD_URI;
	}

	public String getLoginSignUpUrl() {
		LOG.debug("Getting Login/SignUp URL: https://" + cloServer + SIGN_UP_LOGIN_URI);
		return cloServer + SIGN_UP_LOGIN_URI;
	}

	protected SavedAddress getNewBillingAddressFromConfig() {
		return new SavedAddress(config.getString("clo.ccAddr.firstName"), config.getString("clo.ccAddr.lastName"),
				config.getString("clo.ccAddr.address1"), null, config.getString("clo.ccAddr.city"), config.getString("clo.ccAddr.state"),
				config.getString("clo.ccAddr.zip"), config.getString("clo.ccAddr.country"), config.getString("clo.ccAddr.phone"));
	}

	protected SavedAddress getNewBillingAddress2FromConfig() {
		return new SavedAddress(config.getString("clo.ccAddr2.firstName"), config.getString("clo.ccAddr2.lastName"),
				config.getString("clo.ccAddr2.address1"), null, config.getString("clo.ccAddr2.city"), config.getString("clo.ccAddr2.state"),
				config.getString("clo.ccAddr2.zip"), config.getString("clo.ccAddr2.country"), config.getString("clo.ccAddr2.phone"));
	}

	public void signUp(WebDriver driver) throws Exception{
		EbatesSignUpSection signUpPage = new EbatesSignUpSection(driver);

		String email = getRandomEmail();

		signUpPage.signUp(email, config.getString("defaultPassword"));
	}

	public void logIn(WebDriver driver, String user, String pass) throws Exception{
		EbatesLogInSection logInPage = new EbatesLogInSection(driver);

		logInPage.logIn(user, pass);
	}

	public void openNewCardPage(WebDriver driver) throws Exception{
		driver.get(cloServer);
		EbatesCreditCardPage creditCardPage = navigateToNewCardsPage(driver);
		signUp(driver);
		deleteExistingCards(driver, creditCardPage);
		openNewCardDialog(driver, creditCardPage);
	}

	public void openNewCardPage(WebDriver driver, String user, String pass) throws Exception{
		driver.get(cloServer);
		EbatesCreditCardPage creditCardPage = navigateToNewCardsPage(driver);
		logIn(driver, user, pass);
		deleteExistingCards(driver, creditCardPage);
		openNewCardDialog(driver, creditCardPage);
	}

	protected void updateFirstBillingAddress(WebDriver driver, EbatesCreditCardPage creditCardPage, SavedAddress newAddress) {
		creditCardPage.clickEditCreditCardBtn(0);
		creditCardPage.waitForCreditCardFormLoader();
		LOG.info("Credit Card Form loader shown successfully");
		switchToIframe(driver);
		LOG.info("Credit Card Form shown successfully");
		creditCardPage.updateFirstBillingAddress(newAddress);
	}

	protected void makeSecondCardDefault(WebDriver driver, EbatesCreditCardPage creditCardPage, List<SavedCreditCard> cards) {
		creditCardPage.clickCardMakeDefaultBtn(1);
		creditCardPage.waitForCardDefaultSwitch();
		LOG.info("Clicked second card Set as Default Btn");
		LOG.info("Switching isDefault values for cards");
		for(SavedCreditCard card : cards ){
			LOG.info("Card " + cards.indexOf(card));
			card.switchDefault();
		}
		LOG.info("Reversing expected card order");
		Collections.reverse(cards);
		LOG.info("Waiting for card list to refresh");
		System.err.println(cards.get(0).getNickname());
	}

	protected void openNewCardDialog(WebDriver driver, EbatesCreditCardPage creditCardPage) {
		creditCardPage.waitForAddCreditCardBtn();
		LOG.info("Add Credit Card Button shown successfully");
		creditCardPage.clickAddCreditCardBtn();
		LOG.info("Clicking Add Credit Card Button");
		creditCardPage.waitForCreditCardFormLoader();
		LOG.info("Credit Card Form loader shown successfully");
		switchToIframe(driver);
		creditCardPage.waitForCreditCardForm();
		LOG.info("Credit Card Form shown successfully");
	}

	protected void deleteExistingCards(WebDriver driver, EbatesCreditCardPage creditCardPage) {
		LOG.info("Make sure there are no cards on the page, except the EXISTING cards");
		creditCardPage.deleteAllCards();
	}

	public void switchToIframe(WebDriver driver){
		LOG.info("Switch to the parent frame");
		driver.switchTo().defaultContent();
		LOG.info("Switch to the iFrame");
		driver.switchTo().frame("cc_iframe");
	}

	public void confirmAndCloseCardDialog(WebDriver driver){
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		newCardSection.waitForCreatingCardMessage();
		switchToIframe(driver);
		newCardSection.waitForCreditCardConfirmation();
		// newCardSection.waitForCcConfirmBtn();
		newCardSection.waitForCurrentOffersBtn();

		assertTrue(newCardSection.verifyCreditCardAdded(), "Credit card not added");

		// newCardSection.clickCcConfirmBtn();
		newCardSection.waitForCurrentOffersBtn();

		BasePage page = new BasePage(driver);
		page.goToURL(getNewCardUrl());
	}

	public void confirmAndCloseCardUpdatedDialog(WebDriver driver){
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		newCardSection.waitForCreatingCardMessage();
		switchToIframe(driver);
		newCardSection.waitForCreditCardUpdatedConfirmation();

		BasePage page = new BasePage(driver);
		page.goToURL(getNewCardUrl());
	}

	public void confirmAndCloseCardDialog(WebDriver driver, CreditCardType type){
		CloCreditCardSection newCardSection = new CloCreditCardSection(driver);
		//newCardSection.waitForCreatingCardMessage();
		switchToIframe(driver);
		newCardSection.waitForCreditCardConfirmation();

		switch(type){
		case AMEX:
			assertTrue(newCardSection.verifyCreditCardAddedAndLinked(), "Credit card not added");
			break;
		case MAST:
			assertTrue(newCardSection.verifyCreditCardAddedIneligible(), "Credit card not added");
			break;
		case DISC:
			assertTrue(newCardSection.verifyCreditCardAddedIneligible(), "Credit card not added");
			break;
		default:
			assertTrue(newCardSection.verifyCreditCardAddedAndLinked(), "Credit card not added");
			break;
		}
		driver.switchTo().defaultContent();
		newCardSection.clickCloseBtn();
	}
	
	@Override
	public Object[][] browserInfo(ITestContext testNGContext, ITestNGMethod method) throws MalformedURLException {
		runOnSauceLabs = false;
		if (testNGContext.getCurrentXmlTest().getAllParameters().containsKey("runOnSauceLabs")
				|| System.getProperties().containsKey("runOnSauceLabs")) {
			// take the value of system property before suite xml
			runOnSauceLabs = Boolean.valueOf(System.getProperty("runOnSauceLabs"));
			// if nothing is defined then get it from
			if (runOnSauceLabs == null) {
				runOnSauceLabs = Boolean.valueOf(testNGContext.getCurrentXmlTest().getParameter("runOnSauceLabs"));
			}
		}

		if (runOnSauceLabs) {
			return getBrowsers(method);
		} else {
			WebDriver driver = null;
			if (browser.equals("firefox") || browser.isEmpty()) {
				driver = new FirefoxDriver(profile);
			}
			if (browser.equals("chrome")) {
				driver = new ChromeDriver(options);
			}
			Object[][] webDriver = { { driver } };
			return webDriver;
		}
	}
}
