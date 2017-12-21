package com.extrabux.pages.daigou;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.extrabux.util.WebDriverUtil;

public class PurchasePreviewPage extends BasePage {
	private static final Log LOG = LogFactory.getLog(PurchasePreviewPage.class);
	final String PROGRESS_BAR_ELEMENT_ID = "progress-bar";
	NewCreditCardSection newCardSection;

	WebElement shippingAddressesBtn;
	@FindBy(id = "alipay")
	WebElement paymentMethodAlipayRadioBtn;
	@FindBy(id = "creditCard")
	WebElement paymentMethodCreditCardRadioBtn;
	WebElement addCreditCardBtn;

	WebElement subtotal;
	WebElement shipping;
	WebElement tax;
	WebElement couponField;
	WebElement couponBtn;
	WebElement couponDiscount;
	WebElement total;
	WebElement cashback;

	List<WebElement> errors;

	WebElement cardAddedSpinner;
	@FindBy(id = "progress-bar")
	WebElement previewProgressBar;

	@FindBy(className = "credit-card-pay-list")
	WebElement savedCardList;

	WebElement manageCreditCardBtn;
	WebElement editCreditCardBtn;

	@FindBy(id = "toast-container")
	WebElement couponAppliedDialog;
	@FindBy(className = "toast-message")
	WebElement couponMessage;



	public PurchasePreviewPage(WebDriver driver) {
		super(driver);
		newCardSection = new NewCreditCardSection(driver);
	}

	public void waitForRedirectSpinner() {
		try{
			WebDriverUtil.waitForElementPresent(driver, By.id("redirectFromAlipaySpinner"), 30);
			WebDriverUtil.waitForElementNotVisible(driver, By.id("redirectFromAlipaySpinner"), 30);
		} catch( Exception e){
			LOG.debug("The redirection was too fast, and the Alipay spinner wasn't present.");
		}
	}

	public void selectPaymentMethod(String paymentMethod) throws Exception {
		if (paymentMethod.equals("alipay")) {
			paymentMethodAlipayRadioBtn.click();;
		} else if (paymentMethod.equals("creditCard")) {
			paymentMethodCreditCardRadioBtn.click();
		} else {
			throw new Exception("payment method must either be creditCard or alipay");
		}
	}

	public boolean verifyAddNewCardBtnPresent() {
		try {
			WebDriverUtil.waitForElementPresentAndClickable(driver, By.id("addCreditCardBtn"), 10);
			return true;
		} catch (NoSuchElementException nsee) {
			return false;
		}
	}

	public boolean verifyManageCreditCardsBtnPresent() {
		try {
			WebDriverUtil.waitForElementPresentAndClickable(driver, By.id("manageCreditCardBtn"), 10);
			return true;
		} catch (NoSuchElementException nsee) {
			return false;
		}
	}

	public boolean verifyShippingAddressPresent() {
		try {
			WebDriverUtil.waitForElementPresentAndClickable(driver, By.id("shippingAddressesBtn"), 10);
			return true;
		} catch (NoSuchElementException nsee) {
			return false;
		}
	}

	public void addNewCreditCard(String name, String number, String expirationMonth, Integer expirationYear, String cv2, Boolean isDefault, SavedAddress newAddress) {
		clickAddNewCreditCardBtn();
		newCardSection.fillCreditCardInfoAndSubmit(number, expirationMonth, expirationYear, cv2, isDefault, newAddress);
	}

	public void clickAddNewCreditCardBtn() {
		addCreditCardBtn.click();
	}

	public void clickEditCreditCardBtn() {
		editCreditCardBtn.click();
	}

	public void clickChangeAddressBtn() {
		shippingAddressesBtn.click();
	}

	public SavedCreditCard getCreditCard(int index) {
		SavedCreditCard savedCard = new SavedCreditCard();

		return savedCard;
	}

	public SavedCreditCard getFirstCreditCard() {
		SavedCreditCard savedCard = new SavedCreditCard();

		WebElement firstCard = getSavedCreditCardElements().get(0);
		savedCard.setType(CreditCardType.valueOf(firstCard.findElement(By.tagName("span")).getAttribute("title")));
		savedCard.setMaskedNumber(firstCard.findElement(By.tagName("em")).getText());

		return savedCard;
	}

	public List<WebElement> getSavedCreditCardElements() {
		return savedCardList.findElements(By.tagName("div"));
	}

	public ManageCreditCardsPage clickManageCreditCardsBtn() {
		WebDriverUtil.waitForElementClickable(driver, manageCreditCardBtn, 10);
		manageCreditCardBtn.click();

		return new ManageCreditCardsPage(driver);
	}

	public void waitForSaveCardSpinner() {
		WebDriverUtil.waitForElementNotVisible(driver, By.id("cardAddedSpinner"), 20);
	}

	public NewCreditCardSection getNewCardSection() {
		return newCardSection;
	}

	public boolean verifyNoCreditCardErrors() {
		return newCardSection.verifyNoErrors();
	}

	public void waitForPurchaseProcessingBar() {
		LOG.debug("Waiting for Purchase Processing progress bar on Purchase Summary page to be present.");
		WebDriverUtil.waitForElementPresent(driver, By.id(PROGRESS_BAR_ELEMENT_ID), 15);
		LOG.debug("Waiting for Purchase Processing progress bar on Purchase Summary page to no longer be visible.");
		WebDriverUtil.waitForElementNotVisible(driver, By.id(PROGRESS_BAR_ELEMENT_ID), 120);
	}

	public void waitForDiscountProcessingBar() {
		LOG.debug("Waiting for Discount Processing progress bar on Purchase Summary page to be present.");
		WebDriverUtil.waitForElementVisible(driver, previewProgressBar, 10);
		LOG.debug("Waiting for Discount Processing progress bar on Purchase Summary page to no longer be visible.");
		WebDriverUtil.waitForElementNotVisible(driver, By.id(PROGRESS_BAR_ELEMENT_ID), 120);
	}

	public void enterCoupon(String code) {
		LOG.debug("Typing coupon code.");
		typeCouponCode(code);
		LOG.debug("Clicking coupon button.");
		waitForCouponBtnClickable();
		clickCouponBtn();
		LOG.debug("Waiting for Discount Processing progress bar.");
		waitForDiscountProcessingBar();
	}

	public void typeCouponCode(String code) {
		couponField.sendKeys(code);
	}

	public void clickCouponBtn() {
		couponBtn.click();
	}

	public void waitForCouponDialog() {
		LOG.debug("Waiting for coupon application dialog on Purchase Summary page to be present.");
		WebDriverUtil.waitForElementPresent(driver, By.id("toast-container"), 15);
		LOG.debug("Waiting for coupon application to be visible.");
		WebDriverUtil.waitForElementVisible(driver, couponAppliedDialog, 15);
	}

	public void waitForCouponBtnClickable() {
		LOG.debug("Waiting for coupon button to be clickable.");
		WebDriverUtil.waitForElementClickable(driver, couponBtn, 15);
	}

	public boolean verifyCouponValidity() {
		String validCoupon = "Valid coupon text";
		// String invalidCoupon = "该优惠码没有折扣";

		LOG.debug("Checking if coupon was valid.");
		if(couponMessage.getText().equals(validCoupon)){
			LOG.debug("Coupon was valid.");
			return true;
		}
		LOG.debug("Coupon was invalid.");
		return false;
	}

}
