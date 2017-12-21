package com.extrabux.pages.clo;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.pages.daigou.CreditCardType;
import com.extrabux.pages.daigou.SavedAddress;
import com.extrabux.pages.daigou.SavedCreditCard;
import com.extrabux.pages.daigou.Verify;
import com.extrabux.util.WebDriverUtil;

public class EbatesCreditCardPage extends BasePage {
	private static final Log LOG = LogFactory.getLog(EbatesCreditCardPage.class);

	@FindBy(name = "cardNumber")
	WebElement cardNumber;
	@FindBy(id = "addCreditCard")
	public WebElement addCreditCardBtn;
	@FindBy(className = "acct-loading")
	public WebElement cardFormLoader;
	@FindBy(className = "cc-nickname")
	public WebElement cardNickname;
	@FindBy(className = "card-row")
	List<WebElement> cardsOnPage;
	public WebElement iAgree;
	@FindBy(id = "enable-offer")
	public WebElement enableOffer;
	@FindBy(className = "cc-clo")
	List<WebElement> enabledStatus;
	@FindBy(className = "sortWaitBox")
	List<WebElement> sortWaitBox;

	public EbatesCreditCardPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public void clickAddCreditCardBtn() {
		addCreditCardBtn.click();
	}

	public void waitForCreditCardFormLoader() {
		LOG.debug("Waiting for the card form loader to be present");
		WebDriverUtil.waitForElementPresent(driver, By.className("acct-loading"), 15);
		LOG.debug("Waiting for the card form loader to be present");
		WebDriverUtil.waitForElementNotVisible(driver, By.className("acct-loading"), 30);
	}

	public void waitForCreditCardForm() {
		LOG.debug("Waiting for the save button to be present");
		WebDriverUtil.waitForElementPresent(driver, By.id("saveBtn"), 15);
	}

	public void waitForAddCreditCardBtn() {
		LOG.debug("Waiting for the email field to be present");
		WebDriverUtil.waitForElementPresent(driver, By.id("addCreditCard"), 30);
		LOG.debug("Waiting for the email field to be visible");
		WebDriverUtil.waitForElementVisible(driver, driver.findElement(By.id("addCreditCard")), 30);
	}

	public void waitForCardDefaultSwitch() {
		String nick = cardsOnPage.get(1).findElement(By.className("cc-nickname")).getText() + " (default)";
		LOG.info("Waiting for first card nickname to change to " + cardsOnPage.get(1).findElement(By.className("cc-nickname")).getText() + " (default)");
		WebDriverUtil.waitForElementPresent(driver, By.xpath("//*[contains(@class, 'cc-nickname') and contains(text(),'" + nick + "')]"), 30);
		LOG.info("Card list refreshed");
	}

	public void waitLinkCardLoader(){
		LOG.debug("Waiting for the card form loader to be present");
		WebDriverUtil.waitForElementPresent(driver, By.className("sortWaitBox"), 30);
		LOG.debug("Waiting for the card form loader to be no longer visible");
		WebDriverUtil.waitForElementNotVisible(driver, By.className("sortWaitBox"), 30);
	}

	public Verify verifyNoCards() {
		LOG.debug("Verifying no cards on page.");

		if (cardsOnPage.size() > 0) {
			return new Verify("There is a card on the page when there should be none", false);
		}
		return new Verify("", true);
	}

	public Verify verifyCards(List<SavedCreditCard> cards) {
		LOG.debug("Verifying cards.");
		Verify verified;

		for (SavedCreditCard card : cards) {
			verified = verifyCard(card, cards.indexOf(card));
			if (!verified.isVerified()) {
				return verified;
			}
		}
		return new Verify("", true);
	}

	public Verify verifyCard(SavedCreditCard savedCard) {
		LOG.debug("Verifying single card.");
		String error = "";
		int index = cardsOnPage.size() - 1;

		boolean cardTypeVerified = verifyCardType(savedCard.getType(), index);
		boolean nicknameVerified = verifyNickname(savedCard.getNickname(), index);

		boolean verified = nicknameVerified;
		if (!verified) {
			if (!cardTypeVerified) {
				error += "cardType not as expected, ";
			}
			if (!nicknameVerified) {
				error += "Nickname not as expected. Expected: " + savedCard.getNickname() + ", but found: " + cardsOnPage.get(index).findElement(By.className("cc-nickname")).getText();
			}
		}

		return new Verify(error, verified);
	}

	public Verify verifyCard(SavedCreditCard savedCard, int index) {
		LOG.debug("Verifying card.");
		String error = "";

		boolean cardTypeVerified = verifyCardType(savedCard.getType(), index);
		boolean nicknameVerified = verifyNickname(savedCard.getNickname(), index);

		boolean verified =  cardTypeVerified && nicknameVerified;
		if (!verified) {
			if (!cardTypeVerified) {
				error += "cardType not as expected, ";
			}
			if (!nicknameVerified) {
				error += "Nickname not as expected. Expected: " + savedCard.getNickname() + ", but found: " + cardsOnPage.get(index).findElement(By.className("cc-nickname")).getText();
			}
		}

		return new Verify(error, verified);
	}

	public Verify verifyCardEnabled(SavedCreditCard savedCard) throws InterruptedException {
		Verify verify = verifyCard(savedCard);
		assertTrue(verify.isVerified(), verify.getError());

		LOG.debug("Verifying default card is enabled.");
		String error = "";
		boolean verified =  true;
		int index = cardsOnPage.size() - 1;

		if(savedCard.getType() == CreditCardType.DISC || savedCard.getType() == CreditCardType.MAST){
			if(!enabledStatus.get(index).getText().contains("Not eligible for In-Store Offers")){
				LOG.debug("Card is not eligible for linking, but somehow is.");
				error = "Card is not eligible for linking, but somehow is.";
				verified = false;
			} else {
				LOG.debug("Default card is not eligible for linking.");
			}
		} else {
			if(!enabledStatus.get(index).getText().contains("Enabled for In-Store Offers")){
				LOG.debug("Card is not enabled.");
				error = "Card is not enabled.";
				verified = false;
			} else {
				LOG.debug("Default card is enabled.");
			}
		}

		return new Verify(error, verified);
	}

	public Verify verifyCardEnabled(SavedCreditCard savedCard, int index) {
		Verify verify = verifyCard(savedCard, index);
		assertTrue(verify.isVerified(), verify.getError());

		LOG.debug("Verifying card " + index + " is enabled.");
		String error = "";
		boolean verified =  true;

		if(!cardsOnPage.get(index).findElement(By.className("enabled-cc-offer")).getText().contains("Enabled for card-linked offers")){
			LOG.debug("Card " + index + " is not enabled.");
			error = "Card " + index + " is not enabled.";
			verified = false;
		} else {
			LOG.debug("Card " + index + " is enabled.");
		}

		return new Verify(error, verified);
	}

	public boolean verifyCardType(CreditCardType cardType, int index) {
		boolean verified = true;
		String[] cardNickname = cardsOnPage.get(index).findElement(By.className("cc-nickname")).getText().split(" ");
		cardNickname[0] = cardNickname[0].replaceAll("\\s+","");

		switch (cardType) {
			case VISA:
				LOG.debug("The credit card is a Visa.");
				if(cardsOnPage.get(index).findElements(By.className("plate-visa")).size() <= 0){
					LOG.debug("The plate doesn't match.");
					verified = false;
				}
				break;
			case MAST:
				LOG.debug("The credit card is a Mastercard.");
				if(cardsOnPage.get(index).findElements(By.className("plate-mcard")).size() <= 0){
					LOG.debug("The plate doesn't match.");
					verified = false;
				}
				break;
			case DISC:
				LOG.debug("The credit card is a Discover.");
				if(cardsOnPage.get(index).findElements(By.className("plate-disc")).size() <= 0){
					LOG.debug("The plate doesn't match.");
					verified = false;
				}
				break;
			case AMEX:
				LOG.debug("The credit card is an AmEx.");
				if(cardsOnPage.get(index).findElements(By.className("plate-amex")).size() <= 0){
					LOG.debug("The plate doesn't match.");
					verified = false;
				}
				break;
			default:
				LOG.debug("The credit card was not a valid type.");
		}

		return verified;
	}

	public boolean verifyMaskedNumber(String maskedNumber, int index) {
		String[] lastFourOnPage = cardsOnPage.get(index).findElement(By.className("cc-nickname")).getText().split(" ");

		return lastFourOnPage[1].contains(maskedNumber);
	}

	public boolean verifyNickname(String nickname, int index) {
		String nickOnPage = cardsOnPage.get(index).findElement(By.className("cc-nickname")).getText();

		return nickOnPage.contains(nickname);
	}

	public void ensureCardNotInList(SavedCreditCard savedCard) {
		String nick = cardsOnPage.get(0).findElement(By.className("cc-nickname")).getText();
		if(cardsOnPage.get(0).findElement(By.className("cc-nickname")).getText().replaceAll("\\s+","").contains(savedCard.getNickname())){
			LOG.debug("Clicking the delete card button for the first card: " + nick);
			cardsOnPage.get(0).findElement(By.className("deleteCreditCard")).click();
			LOG.debug("Clicking the delete confirmation button for card: " + nick);
			driver.findElement(By.className("delete-cc")).click();

			LOG.debug("Waiting for card " + nick + " to be removed");
			WebDriverUtil.waitForTextNotVisible(driver, By.className("cc-nickname"), nick, 30);
		} else {
			LOG.debug("Card: " + nick + " doesn't exist in card list");
		}
	}

	public void deleteCreditCard() {
		int index = cardsOnPage.size() - 1;
		String nick = cardsOnPage.get(index).findElement(By.className("cc-nickname")).getText();
		LOG.debug("Card " + nick + "found on page");
		if (!nick.contains("EXISTING")) {
			LOG.debug("Clicking the delete card button for the first card: "
					+ nick);
			cardsOnPage.get(index)
					.findElement(By.className("deleteCreditCard")).click();
			LOG.debug("Clicking the delete confirmation button for card: "
					+ nick);
			driver.findElement(By.className("delete-cc")).click();

			LOG.debug("Waiting for card " + nick + " to be removed");
			WebDriverUtil.waitForElementNotVisible(driver,
					By.className("sortWaitBox"), 30);
		}
	}

	public void deleteCreditCard(int index) {
		String nick = cardsOnPage.get(index).findElement(By.className("cc-nickname")).getText();

		if (!nick.contains("EXISTING")) {
			LOG.debug("Clicking the delete card button for card: " + nick);
			cardsOnPage.get(index)
					.findElement(By.className("deleteCreditCard")).click();
			LOG.debug("Clicking the delete confirmation button for card: "
					+ nick);
			driver.findElement(By.className("delete-cc")).click();
			LOG.debug("Waiting for card " + nick + " to be removed");
			WebDriverUtil.waitForElementNotVisible(driver,
					By.className("sortWaitBox"), 30);
		}
	}

	public Boolean deleteCreditCardValidate(int index) {
		if (cardsOnPage.size()>0) {
			String nick = cardsOnPage.get(index)
					.findElement(By.className("cc-nickname")).getText();
			if (!nick.contains("EXISTING")) {
				LOG.debug("Clicking the delete card button for card: " + nick);
				cardsOnPage.get(index)
						.findElement(By.className("deleteCreditCard")).click();
				LOG.debug("Clicking the delete confirmation button for card: "
						+ nick);
				driver.findElement(By.className("delete-cc")).click();
				LOG.debug("Waiting for card " + nick + " to be removed");
				WebDriverUtil.waitForElementNotVisible(driver,
						By.className("sortWaitBox"), 30);
				WebDriverUtil.waitForTextNotVisible(driver,
						By.className("cc-nickname"), nick, 30);
				return true;
			}
		}
		return false;
	}

	public void deleteAllCards() {
		for (WebElement card : cardsOnPage) {
			if (deleteCreditCardValidate(cardsOnPage.indexOf(card))) {
				deleteAllCards();
			}
		}
	}

	public void enableCreditCard() {
		enableCreditCard(0);
	}

	public void enableCreditCard(int index) {
		String nick = cardsOnPage.get(0).findElement(By.className("enableCLO")).getText();

		LOG.debug("Clicking the Enable for card-linked offers button for card " + nick);
		cardsOnPage.get(0).findElement(By.className("enableCLO")).click();
		LOG.debug("Checking the Enable for card-linked offers checkbox for card " + nick);
		checkEnableTosCheckbox();
		LOG.debug("Clicking the Enable for card-linked offers confirmation button for card " + nick);
		enableOffer.click();
		waitLinkCardLoader();

	}

	public void enableAllCards() {
		for (WebElement card : cardsOnPage) {
			enableCreditCard(cardsOnPage.indexOf(card));
		}
	}

	public void checkEnableTosCheckbox() {
		if (!iAgree.isSelected()){
			iAgree.click();
		}
	}

	public void uncheckEnableTosCheckbox() {
		if (iAgree.isSelected()){
			iAgree.click();
		}
	}

	public void clickEditCreditCardBtn(int index) {
		cardsOnPage.get(index).findElement(By.className("updateBillAdd")).click();
	}

	public void clickCardMakeDefaultBtn(int index) {
		cardsOnPage.get(index).findElement(By.className("setAsDefault")).click();
	}

	public void updateFirstBillingAddress(SavedAddress newAddress) {
		CloCreditCardSection updatedCard = new CloCreditCardSection(driver);
		updatedCard.updateBillingAddressInfoAndSubmit(newAddress);
	}

	public Verify verifyBillingAddress(SavedAddress newAddress) {
		String[] billingAddress = driver.findElement(By.xpath("//*[contains(@class, 'ccUserAddress')]")).getText().split("\\r?\\n");

		String name = newAddress.getFullName();
		String address = newAddress.getAddress1();
		String cityStateZip = newAddress.getCityStateZip();
		boolean nameVerified = true;
		boolean addressVerified = true;
		boolean cityStateZipVerified = true;

		String error = "";

		if (!name.equals(billingAddress[0])) {

			nameVerified = false;
			error += "Name did not match.  expected " + name + " but found " + billingAddress[0] + ", ";
		}
		if (!address.equals(billingAddress[1])) {
			nameVerified = false;
			error += "Address did not match.  expected " + address + " but found " + billingAddress[1] + ", ";
		}
		if (!cityStateZip.equals(billingAddress[2])) {
			nameVerified = false;
			error += "City, State Zip did not match.  expected " + cityStateZip + " but found " + billingAddress[2] + ", ";
		}
		if (!error.isEmpty()) {
			error = "failure on address: " + error;
		}
		return new Verify(error, nameVerified && addressVerified && cityStateZipVerified);
	}

}
