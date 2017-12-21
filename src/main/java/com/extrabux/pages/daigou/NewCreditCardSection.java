package com.extrabux.pages.daigou;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.extrabux.util.WebDriverUtil;

public class NewCreditCardSection extends BasePage {
	private static final Log LOG = LogFactory.getLog(NewCreditCardSection.class);
	protected NewAddressSection newAddressSection;

	WebElement cardHolderName;
	@FindBy(name = "cardNumber")
	WebElement cardNumber;
	@FindBy(id = "expMonth")
	WebElement expirationMonth;
	@FindBy(id = "expYear")
	WebElement expirationYear;
	@FindBy(id = "cardCvv")
	WebElement cv2;
	@FindBy(id = "isDefault")
	WebElement defaultCheckbox;

	@FindBy(id = "saveBtn")
	WebElement saveCreditCardBtn;
	@FindBy(xpath = "//*[@id='saveBtn' and text()='Update Information']")
	WebElement updateCreditCardBtn;

	// billing address radio buttons
	WebElement useShippingAddress;
	protected WebElement newBillingAddress;

	// errors
	@FindBy(className = "help-block")
	List<WebElement> errors;

	public NewCreditCardSection(WebDriver driver) {
		super(driver);
		newAddressSection = new NewAddressSection(driver);
	}

	/**
	 *
	 * @param name card holder name
	 * @param cardNumber
	 * @param expirationMonth
	 * @param expirationYear
	 * @param cv2
	 * @param isDefault
	 */
	public void fillCreditCardInfo(String cardNumber, String expirationMonth, Integer expirationYear,
			String cv2, Boolean isDefault) {
		try {
			Thread.sleep(2000);
		} catch (Exception exception) {
			LOG.debug("Couldn't sleep before filling in card info");
		}
		if (cardNumber != null) {
			typeCardNumber(cardNumber);
		}
		if (expirationMonth != null) {
			selectExpirationMonth(expirationMonth);
		}
		if (expirationYear != null) {
			selectExpirationYear(expirationYear);
		}
		typeCv2(cv2);
	}

	// if newAddress != null add it when adding new card otherwise use shipping
	public void fillCreditCardInfoAndSubmit(String cardNumber, String expirationMonth, Integer expirationYear,
			String cv2, Boolean isDefault, SavedAddress newAddress) {
		fillCreditCardInfo(cardNumber, expirationMonth, expirationYear, cv2, isDefault);
		if (newAddress != null) {
			if (newBillingAddress.isDisplayed()) {
				clickNewAddressForBilling();
			}
			newAddressSection.fillForm(newAddress);
		}
		clickSaveCreditCard();
	}

	public void typeCardNumber(String number) {
		WebDriverUtil.waitForElementVisible(driver, cardNumber, 10);
		String[] cardNums = number.split(" ");
		for (String cardNum : cardNums) {
			cardNumber.sendKeys(cardNum);
		}
	}

	private void selectExpirationMonth(String month) {
		Select selectMonth = new Select(expirationMonth);
		selectMonth.selectByValue(month);
	}

	private void selectExpirationYear(int year) {
		Select selectYear = new Select(expirationYear);
		selectYear.selectByVisibleText(Integer.toString(year));
	}

	private void typeCv2(String cv2Number) {
		cv2.clear();
		cv2.sendKeys(cv2Number);
	}

	protected void clickSaveCreditCard() {
		saveCreditCardBtn.click();
	}

	protected void clickUpdateCreditCard() {
		updateCreditCardBtn.click();
	}

	public void clickUseShippingAddressForBilling() {
		useShippingAddress.click();
	}

	public void clickNewAddressForBilling() {
		newBillingAddress.click();
	}

	public NewAddressSection getNewAddressSection() {
		return newAddressSection;
	}

	public boolean verifyErrors(List<String> expectedErrors) {
		// first assert the number of errors found on the page is as expected
		List<String> errorsOnPage = getErrorsOnPage();
		if (!(errorsOnPage.size() == expectedErrors.size())) {
			return false;
		}
		if (!errorsOnPage.equals(expectedErrors)) {
			return false;
		}
		return true;
	}

	public List<String> getErrorsOnPage() {
		List<String> errorsOnPage = new ArrayList<String>();
		for (WebElement error : errors) {
			if (StringUtils.isNotBlank(error.getText())) {
				LOG.debug("Error on page: " + error.getText());
				errorsOnPage.add(error.getText());
			}
		}

		return errorsOnPage;
	}

	public boolean verifyNoErrors() {
		return getErrorsOnPage().isEmpty();
	}

}
