package com.extrabux.pages.clo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.extrabux.pages.daigou.NewCreditCardSection;
import com.extrabux.pages.daigou.SavedAddress;
import com.extrabux.util.WebDriverUtil;

public class CloCreditCardSection extends NewCreditCardSection {
	private static final Log LOG = LogFactory.getLog(CloCreditCardSection.class);
	final String FOR_ATTRIBUTE = "for";


	@FindBy(id = "linkablesTOS")
	WebElement linkablesTerms;
	@FindBy(id = "ccConfirm-txt")
	WebElement ccConfirmMsg;
	@FindBy (className = "confirm-head")
	WebElement ccConfirmHeader;
	@FindBy(id = "current-offers")
	WebElement currentOffersBtn;
	// TODO add correct element identifier for confirm button if it returns
	WebElement ccConfirmBtn;
	@FindBy(id = "progress-text")
	WebElement progressText;
	@FindBy (className = "has-validation-error")
	public List<WebElement> hasErrorElements;
	@FindBy (xpath = "//*[contains(@class, 'cc-cancel')]")
	WebElement closeBtn;


	public CloCreditCardSection(WebDriver driver) {
		super(driver);
	}

	public void fillBillingAddressInfoAndSubmit(SavedAddress newAddress) {
		newAddressSection.fillForm(newAddress);
		clickSaveCreditCard();
	}

	public void updateBillingAddressInfoAndSubmit(SavedAddress newAddress) {
		newAddressSection.fillForm(newAddress);
		clickUpdateCreditCard();
	}

	@Override
	public void fillCreditCardInfoAndSubmit(String cardNumber, String expirationMonth, Integer expirationYear,
			String cv2, Boolean isDefault, SavedAddress newAddress) {
		fillCreditCardInfo(cardNumber, expirationMonth, expirationYear, cv2, isDefault);
		if (newAddress != null) {
			newAddressSection.fillForm(newAddress);
		}
		clickAcceptTosCheckboxes();
		clickSaveCreditCard();
	}

	public void fillCreditCardInfoAndSubmit(String cardNumber, String expirationMonth, Integer expirationYear,
			String cv2, Boolean isDefault, SavedAddress newAddress, boolean linkablesTos) {
		fillCreditCardInfo(cardNumber, expirationMonth, expirationYear, cv2, isDefault);
		if (newAddress != null) {
			newAddressSection.fillForm(newAddress);
		}

		if (linkablesTos){
			acceptLinkablesTos();
		}
		clickSaveCreditCard();
	}

	public void updateCreditCard(int index, String expirationMonth, Integer expirationYear, String cv2, SavedAddress newAddress) {
		fillCreditCardInfoAndSubmit(null, expirationMonth, expirationYear, cv2, null, newAddress);
	}

	private void clickAcceptTosCheckboxes() {		
		acceptLinkablesTos();
	}

	private void acceptLinkablesTos() {
		if (!linkablesTerms.isSelected()){				
			linkablesTerms.click();
		}
	}

	public boolean verifyCreditCardAdded() {
		if (!(driver.findElements(By.xpath("//div[contains(text(),'enabled')]")).size() > 0)) {
			LOG.info("Card Added message not shown.");
			return false;
		}
		LOG.info("Card added.");
		return true;
	}

	public boolean verifyCreditCardAddedAndLinked() {
		if (!(driver.findElements(By.xpath("//div[contains(text(),'enabled')]")).size() > 0)) {
			LOG.info("Card Added and Linked message not shown.");
			//LOG.info(ccConfirmMsg.getText());
			return false;
		}
		LOG.info("Card added and linked.");
		return true;
	}

	public boolean verifyCreditCardAddedNotLinked() {
		if (!(driver.findElements(By.xpath("//div[contains(text(),'cannot be enabled')]")).size() > 0)) {
			LOG.info("Card Added and not Linked message not shown.");
			//LOG.info(ccConfirmMsg.getText());
			return false;
		}
		LOG.info("Card added but not linked.");
		return true;
	}

	public boolean verifyCreditCardAddedIneligible() {
		if (!(driver.findElements(By.xpath("//div[contains(text(),'card is not eligible')]")).size() > 0)) {
			LOG.info("Card Added and not Linked (ineligible) message not shown.");
			//LOG.info(ccConfirmMsg.getText());
			return false;
		}
		LOG.info("Card added but not eligible for linking.");
		return true;
	}

	/*public void waitForCcConfirmBtn() {
		LOG.debug("Waiting for the card added confirmation button to be present");
		WebDriverUtil.waitForElementPresent(driver, By.id("ccConfirm-close"), 20);
		LOG.debug("Waiting for the card added confirmation button to be visible");
		WebDriverUtil.waitForElementClickable(driver, ccConfirmBtn, 20);
	}

	public void clickCcConfirmBtn() {
		LOG.info("Clicking the CC Confirm button.");
		ccConfirmBtn.click();
		LOG.info("Waiting for the CC Confirm to no longer be visible.");
		WebDriverUtil.waitForElementNotVisible(driver, By.id("ccConfirm-close"), 20);
	}*/

	public void waitForCurrentOffersBtn() {
		LOG.debug("Waiting for the current offers button to be present");
		WebDriverUtil.waitForElementPresent(driver, By.id("current-offers"), 20);
		LOG.debug("Waiting for the current offers button to be visible");
		WebDriverUtil.waitForElementClickable(driver, currentOffersBtn, 20);
	}

	public void clickCurrentOffersBtn() {
		LOG.info("Clicking the Current Offers button.");
		currentOffersBtn.click();
		LOG.info("Waiting for the Close button to no longer be visible.");
		WebDriverUtil.waitForElementNotVisible(driver, By.id("current-offers"), 20);
	}

	public void clickCloseBtn() {
		LOG.info("clicking close X button...");
		closeBtn.click();
	}

	public void waitForCreatingCardMessage() {
		LOG.debug("Waiting for the creating cart message to appear");
		WebDriverUtil.waitForElementPresent(driver, By.className("progress-text"), 10);
		LOG.debug("Waiting for the creating card message to disappear");
		WebDriverUtil.waitForElementNotVisible(driver, By.className("progress-text"), 30);
	}

	public void waitForCreditCardConfirmation() {
		LOG.debug("Waiting for the card added confirmation message to be present");
		WebDriverUtil.waitForElementPresent(driver, By.className("f-grn"), 20);
		LOG.debug("Waiting for the card added confirmation message to be visible");
		WebDriverUtil.waitForElementVisible(driver, ccConfirmHeader, 20);
	}

	public void waitForCreditCardUpdatedConfirmation() {
		LOG.debug("Waiting for the card updated confirmation message to be visible");
		WebDriverUtil.waitForElementVisible(driver, ccConfirmHeader, 20);
	}

	public boolean verifyHighlightedFields(List<String> expectedFieldIds) {
		if (expectedFieldIds.equals(Arrays.asList(new String[]{""}))) {
			expectedFieldIds = null;
		}
		return getHighlightedFieldIds().equals(expectedFieldIds);
	}

	public List<String> getHighlightedFieldIds() {
		List<String> actualIdsHighlighted = new ArrayList<String>();

		for (WebElement error : hasErrorElements) {
			String highlightedField = error.findElement(By.tagName("input")).getAttribute("id");
			if (StringUtils.isNotBlank(highlightedField)) {
				actualIdsHighlighted.add(highlightedField);
			}
		}
		return actualIdsHighlighted;
	}
}
