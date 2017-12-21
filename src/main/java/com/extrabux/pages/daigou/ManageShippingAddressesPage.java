package com.extrabux.pages.daigou;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.extrabux.util.WebDriverUtil;

public class ManageShippingAddressesPage extends BasePage {
	private static final Log LOG = LogFactory.getLog(ManageShippingAddressesPage.class);

	@FindBy(className = "radio")
	List<WebElement> savedAddresses;
	@FindBy(name = "nickname")
	WebElement nickname;
	WebElement firstName;
	WebElement lastName;
	WebElement company;
	WebElement address1;
	WebElement address2;
	WebElement city;
	WebElement state;
	WebElement zip;
	WebElement country;
	WebElement phone;
	@FindBy(name = "isDefault")
	WebElement defaultCheckbox;
	@FindBy(className = "btn-red")
	WebElement submitBtn;
	@FindBy (className = "help-block")
	List<WebElement> errors;
	@FindBy(className = "toast-success")
	WebElement saveSuccessful;
	WebElement checkoutSummaryBtn;

	By spinnerLocator = By.id("productInfoSpinner");
	By reviewSpinnerLocator = By.id("reviewsSpinner");
	By cashbackSpinnerLocator = By.id("cashbackSpinner");

	public ManageShippingAddressesPage(WebDriver driver) {
		super(driver);
	}

	/**
	 *
	 * @param nickName
	 * @param firstName
	 * @param lastName
	 * @param company
	 * @param address1
	 * @param address2
	 * @param city
	 * @param state two letter state code
	 * @param zip
	 * @param country
	 * @param phone
	 * @param isDefault
	 */

	public void fillFormAndSubmit(SavedAddress address) {
		typeNickName(address.getNickname());
		typeFirstName(address.getFirstName());
		typeLastName(address.getLastName());
		if (address.getCompany() != null && !address.getCompany().isEmpty()) {
			typeCompany(address.getCompany());
		}
		typeAddress1(address.getAddress1());
		if (address.getAddress2() != null && !address.getAddress2().isEmpty()) {
			typeAddress2(address.getAddress2());
		}
		typeCity(address.getCity());
		selectState(address.getState());
		typeZip(address.getZip());
		typeCountry(address.getCountry());
		typePhone(address.getPhone());
		if (address.getIsDefault()) {
			clickDefaultCheckBox();
		}
		LOG.debug("Submitting shipping address form.");
		clickSubmit();
	}

	private void typeNickName(String addressNickname) {
		this.nickname.clear();
		this.nickname.sendKeys(addressNickname);
	}

	private void typeFirstName(String first) {
		firstName.clear();
		firstName.sendKeys(first);
	}

	private void typeLastName(String last) {
		lastName.clear();
		lastName.sendKeys(last);
	}

	private void typeCompany(String company) {
		this.company.clear();
		this.company.sendKeys(company);
	}

	private void typeAddress1(String address1) {
		this.address1.clear();
		this.address1.sendKeys(address1);
	}

	private void typeAddress2(String address2) {
		this.address2.clear();
		this.address2.sendKeys(address2);
	}

	private void typeCity(String city) {
		this.city.clear();
		this.city.sendKeys(city);
	}

	private void selectState(String stateCode) {
		Select selectState = new Select(state);
		selectState.selectByValue(stateCode);
	}

	private void typeZip(String zip) {
		this.zip.clear();
		this.zip.sendKeys(zip);
	}

	private void typeCountry(String cntry) {
		country.clear();
		country.sendKeys(cntry);
	}

	private void typePhone(String phone) {
		this.phone.clear();
		this.phone.sendKeys(phone);
	}

	private void clickDefaultCheckBox() {
		if( !defaultCheckbox.isSelected()) {
			defaultCheckbox.click();
		}
	}

	private void clickSubmit() {
		submitBtn.click();
	}

	public boolean verifyErrorsOnPage(List<String> expectedErrors) {
		// first assert the number of errors found on the page is as expected
		List<String> errorsOnPage = getErrors();
		if (!(errorsOnPage.size() == expectedErrors.size())) {
			return false;
		}
		if (!errorsOnPage.equals(expectedErrors)) {
			return false;
		}
		return true;
	}

	public List<String> getErrors() {
		List<String> errorsOnPage  = new ArrayList<String>();
		for (WebElement error : errors) {
			if (error.isDisplayed()) {
				errorsOnPage.add(error.getText());
			}
		}
		return errorsOnPage;
	}

	public void waitForNicknameField() {
		WebDriverUtil.waitForElementPresent(driver, By.id("nick-name"), 30);
	}

	public void waitForSavedAddressNickname(int index) {
		WebDriverUtil.waitForElementPresent(driver, By.id("savedAddressNickname" + index), 15);
		WebDriverUtil.waitForElementVisible(driver, savedAddresses.get(index).findElement(By.id("savedAddressNickname" + index)), 15);
	}

	public void clickEditAddressBtn(int index) {
		waitForSavedAddressNickname(index);
		savedAddresses.get(index).findElement(By.id("addressEdit" + index)).click();
	}

	public String getSavedAddress(int index) {
		// verify the saved address is good
		String nicknameOnPage = savedAddresses.get(index).findElement(By.id("savedAddressNickname" + index)).getText().replace("备注名: ", "");
		String nameOnPage = savedAddresses.get(index).findElement(By.id("savedAddressName" + index)).getText().replace("收货人: ", "");
		String addressOnPage = savedAddresses.get(index).findElement(By.id("savedAddressAddress" + index)).getText().replace("收货地址: ", "");
		String phoneOnPage = savedAddresses.get(index).findElement(By.id("savedAddressPhone" + index)).getText().replace("电话: ", "");

		return nicknameOnPage.trim() + ", " + nameOnPage.trim() + ", " + addressOnPage.trim() + ", " + phoneOnPage.trim();
	}

	public boolean verifySavedAddressNickname(int itemIndex, String nickname) {
		return getSavedAddressNickname(itemIndex).equals(nickname);
	}

	private String getSavedAddressNickname(int index) {
		return savedAddresses.get(index).findElement(By.id("savedAddressNickname" + index)).getText().replace("备注名: ", "").trim();
	}

	public boolean verifySavedAddressName(int itemIndex, String name) {
		return getSavedAddressName(itemIndex).equals(name);
	}

	private String getSavedAddressName(int index) {
		return savedAddresses.get(index).findElement(By.id("savedAddressName" + index)).getText().replace("收货人: ", "").trim();
	}

	public boolean verifySavedAddressAddress(int itemIndex, String address) {
		return getSavedAddressAddress(itemIndex).equals(address);
	}

	private String getSavedAddressAddress(int index) {
		return savedAddresses.get(index).findElement(By.id("savedAddressAddress" + index)).getText().replace("收货地址: ", "").trim();
	}

	public boolean verifySavedAddressPhone(int itemIndex, String phone) {
		return getSavedAddressPhone(itemIndex).equals(phone);
	}

	private String getSavedAddressPhone(int index) {
		return savedAddresses.get(index).findElement(By.id("savedAddressPhone" + index)).getText().replace("电话: ", "").trim();
	}

	public Verify verifyAddresses(List<SavedAddress> addresses) {
		Verify verified;
		for (SavedAddress address : addresses) {
			verified = verifyAddress(address, addresses.indexOf(address));
			if (!verified.isVerified()) {
				return verified;
			}
		}
		return new Verify("", true);
	}

	public Verify verifyAddress(SavedAddress address, int index) {
		boolean nicknameVerified = true;
		boolean nameVerified = true;
		boolean addressVerified = true;
		boolean phoneVerified = true;
		String error = "";

		if (!verifySavedAddressNickname(index, address.getNickname())) {
			nicknameVerified = false;
			error += "Nickname did not match.  expected " + address.getNickname() + " but found " + getSavedAddressNickname(index) + ", ";
		}
		if (!verifySavedAddressName(index, address.getName())) {
			nameVerified = false;
			error += "Name did not match.  expected " + address.getName() + " but found " + getSavedAddressName(index) + ", ";
		}
		if (!verifySavedAddressAddress(index, address.getAddress1())) {
			addressVerified = false;
			error += "Address did not match.  expected " + address.getAddress1() + " but found " + getSavedAddressAddress(index) + ", ";
		}
		if (!verifySavedAddressPhone(index, address.getPhone())) {
			phoneVerified = false;
			error += "Phone number did not match.  expected " + address.getPhone() + " but found " + getSavedAddressPhone(index);
		}
		if (!error.isEmpty()) {
			error = "failure on address: " + index + " " + error;
		}
		return new Verify(error, nicknameVerified && nameVerified && addressVerified && phoneVerified);
	}

	public Verify verifyEditedAddress(SavedAddress address) {
		boolean nicknameVerified = true;
		boolean firstNameVerified = true;
		boolean lastNameVerified = true;
		boolean address1Verified = true;
		boolean address2Verified = true;
		boolean cityVerified = true;
		boolean stateVerified = true;
		boolean zipVerified = true;
		boolean countryVerified = true;
		boolean phoneVerified = true;
		boolean defaultVerified = true;
		String error = "";

		if (!address.getNickname().equals(this.nickname.getAttribute("value"))) {
			nicknameVerified = false;
			error += "Nickname did not match.  expected " + address.getNickname() + " but found " + this.nickname.getAttribute("value") + ", ";
		}
		if (!address.getFirstName().equals(this.firstName.getAttribute("value"))) {
			firstNameVerified = false;
			error += "First name did not match.  expected " + address.getFirstName() + " but found " + this.firstName.getAttribute("value") + ", ";
		}
		if (!address.getLastName().equals(this.lastName.getAttribute("value"))) {
			lastNameVerified = false;
			error += "Last name did not match.  expected " + address.getLastName() + " but found " + this.lastName.getAttribute("value") + ", ";
		}
		if (!address.getAddress1().equals(this.address1.getAttribute("value"))) {
			address1Verified = false;
			error += "Address 1 did not match.  expected " + address.getAddress1() + " but found " + this.address1.getAttribute("value") + ", ";
		}
		if (address.getAddress2() != null) {
			if (!address.getAddress2().equals(this.address2.getAttribute("value"))) {
				address2Verified = false;
				error += "Address 2 did not match.  expected " + address.getAddress2() + " but found " + this.address2.getAttribute("value") + ", ";
			}
		}
		if (!address.getCity().equals(this.city.getAttribute("value"))) {
			cityVerified = false;
			error += "City did not match.  expected " + address.getCity() + " but found " + this.city.getAttribute("value") + ", ";
		}
		if (!address.getState().equals(this.state.getAttribute("value"))) {
			stateVerified = false;
			error += "State did not match.  expected " + address.getState() + " but found " + this.state.getAttribute("value") + ", ";
		}
		if (!address.getZip().equals(this.zip.getAttribute("value"))) {
			zipVerified = false;
			error += "Zip code did not match.  expected " + address.getZip() + " but found " + this.zip.getAttribute("value") + ", ";
		}
		if (!address.getCountry().equals(this.country.getAttribute("value"))) {
			countryVerified = false;
			error += "Country did not match.  expected " + address.getCountry() + " but found " + this.country.getAttribute("value") + ", ";
		}
		if (!address.getPhone().equals(this.phone.getAttribute("value"))) {
			phoneVerified = false;
			error += "Phone number did not match.  expected " + address.getPhone() + " but found " + this.phone.getAttribute("value") + ", ";
		}
		if (!(defaultCheckbox.isSelected() && address.getIsDefault())){
			defaultVerified = false;
			error += "Is Default did not match.  expected " + address.getIsDefault() + " but found " + defaultCheckbox.isSelected() + ", ";
		}
		if (!error.isEmpty()) {
			error = "failure on address: " + error;
		}
		return new Verify(error, nicknameVerified && firstNameVerified && lastNameVerified && address1Verified && address2Verified &&
				cityVerified && stateVerified && zipVerified && countryVerified && phoneVerified && defaultVerified);
	}

	public void waitForAddressSaveSuccessful() {
		LOG.debug("Waiting for address save message to be visible in address management section.");
		WebDriverUtil.waitForElementVisible(driver, saveSuccessful, 15);
		LOG.debug("Waiting for address save message to be disappear in address management section.");
		WebDriverUtil.waitForElementNotVisible(driver, By.className("toast-successful"), 30);
	}

}
