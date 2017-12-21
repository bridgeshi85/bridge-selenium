package com.extrabux.pages.daigou;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.extrabux.util.WebDriverUtil;

public class ManageCreditCardsPage extends BasePage {
	NewCreditCardSection newCreditCardSection;
	WebElement addCardBtn;
	WebElement savedCreditCards;

	By addCartBtnLocator = By.id("addCardBtn");
	By cardAddedSpinner = By.id("cardAddedSpinner");
	By editCardsSpinnerLocator = By.id("editCardsPageSpinner");

	public ManageCreditCardsPage(WebDriver driver) {
		super(driver);
		newCreditCardSection = new NewCreditCardSection(driver);
	}

	public void clickAddNewCreditCardBtn() {
		addCardBtn.click();
	}

	public void addNewCreditCard(String name, String number, String expirationMonth, Integer expirationYear, String cv2, Boolean isDefault, SavedAddress newAddress) {
		clickAddNewCreditCardBtn();
		newCreditCardSection.fillCreditCardInfoAndSubmit(number, expirationMonth, expirationYear, cv2, isDefault, newAddress);
	}

	public void waitForEditCardsSpinner() {
		WebDriverUtil.waitForElementNotVisible(driver, editCardsSpinnerLocator, 30);
	}

	public void waitForAddNewCreditCardBtn() {
		WebDriverUtil.waitForElementPresentAndClickable(driver, addCartBtnLocator, 10);
	}

	public void waitForDefaultCreditCard() {
		WebDriverUtil.waitForElementPresent(driver, By.className("default-pay"), 10);
	}

	public void waitCardAddedSpinner() {
		WebDriverUtil.waitForElementNotVisible(driver, cardAddedSpinner, 30);
	}

	public boolean verifyCreditCard() {
		return true;
	}

	public boolean verifyCardType(CreditCardType cardType, int index) {
		CreditCardType typeOnPage = CreditCardType.valueOf(savedCreditCards.findElement(By.id("cardType" + index)).getAttribute("title"));

		return typeOnPage.equals(cardType);
	}

	public boolean verifyMaskedNumber(String maskedNumber, int index) {
		String lastFourOnPage = savedCreditCards.findElement(By.id("cardNumber" + index)).getText();

		return lastFourOnPage.equals(maskedNumber);
	}

	public boolean verifyExpiration(String expiration, int index) {
		String expirationOnPage = savedCreditCards.findElement(By.id("cardExpiration" + index)).getText();

		return expirationOnPage.equals(expiration);
	}

	public boolean verifyCardDefault(boolean isDefault, int index) {
		return isDefaultCard(index) == isDefault;
	}

	public boolean isDefaultCard(int index) {
		return driver.findElements(By.id("cardIsDefault" + index)).size() == 1;
	}

	public Verify verifyFirstCreditCardInList(SavedCreditCard savedCard) {
		int index = 0;
		String error = "";

		boolean cardTypeVerified = verifyCardType(savedCard.getType(), index);
		boolean maskedNumberVerified = verifyMaskedNumber(savedCard.getMaskedNumber(), index);
		boolean expirationVerified = verifyExpiration(savedCard.getExpiration(), index);
		boolean defaultVerified = verifyCardDefault(savedCard.isDefault, index);

		boolean verified =  cardTypeVerified && maskedNumberVerified && expirationVerified && defaultVerified;
		if (!verified) {
			if (!cardTypeVerified) {
				error += "cardType not as expected, ";
			}
			if (!maskedNumberVerified) {
				error += "cardMaskedNumber not as expected, ";
			}
			if (!expirationVerified) {
				error += "cardExpiration not as expected, ";
			}
			if (!defaultVerified) {
				error += "defaultCard not as expected.";
			}
		}

		return new Verify(error, verified);

	}

	public void clickEditFirstCreditCard() {
		clickEditCreditCard(0);
	}

	public void updateFirstCreditCard(String expirationMonth, Integer expirationYear, String cv2, SavedAddress newAddress) {
		clickEditFirstCreditCard();
		updateCreditCard(0, expirationMonth, expirationYear, cv2, newAddress);
	}

	public void updateCreditCard(int index, String expirationMonth, Integer expirationYear, String cv2, SavedAddress newAddress) {
		clickEditCreditCard(index);
		newCreditCardSection.fillCreditCardInfoAndSubmit(null, expirationMonth, expirationYear, cv2, null, newAddress);
	}

	public void clickEditCreditCard(int index) {
		driver.findElement(By.id("cardEditBtn" + index)).click();
	}

	public void deleteFirstCreditCard() {
		deleteCreditCard(0);
	}

	public void deleteCreditCard(int index) {
		driver.findElement(By.id("cardDeleteBtn" + index)).click();
		waitForEditCardsSpinner();
	}

	public boolean verifyNoErrors() {
		return newCreditCardSection.verifyNoErrors();
	}

	public NewCreditCardSection getNewCardSection() {
		return newCreditCardSection;
	}

}
