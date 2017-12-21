package com.extrabux.pages.daigou;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class NewAddressSection extends BasePage {
	private static final Log LOG = LogFactory.getLog(NewAddressSection.class);
	@FindBy(id = "nick-name")
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

	public NewAddressSection(WebDriver driver) {
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

	public void fillForm(SavedAddress address) {
		try {
			Thread.sleep(2000);
		} catch (Exception exception) {
			LOG.debug("Couldn't sleep before filling in card info");
		}
		if (address.getNickname() != null) {
			typeNickName(address.getNickname());
		}
		if (address.getFirstName() != null) {
			typeFirstName(address.getFirstName());
		}
		if (address.getLastName() != null) {
			typeLastName(address.getLastName());
		}
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
	}

	private void typeNickName(String addressNickname) {
		nickname.clear();
		nickname.sendKeys(addressNickname);
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
		if(driver.findElements(By.id("country")).size() > 0){
			country.clear();
			country.sendKeys(cntry);
		}
	}

	private void typePhone(String phone) {
		this.phone.clear();
		this.phone.sendKeys(phone);
	}

	private void clickDefaultCheckBox() {
		defaultCheckbox.click();
	}

	public Verify verifyEditedBillingAddress(SavedAddress address) {
		boolean firstNameVerified = true;
		boolean lastNameVerified = true;
		boolean address1Verified = true;
		boolean address2Verified = true;
		boolean cityVerified = true;
		boolean stateVerified = true;
		boolean zipVerified = true;
		boolean countryVerified = true;
		boolean phoneVerified = true;
		String error = "";

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
		if (!error.isEmpty()) {
			error = "failure on address: " + error;
		}
		return new Verify(error, firstNameVerified && lastNameVerified && address1Verified && address2Verified &&
				cityVerified && stateVerified && zipVerified && countryVerified && phoneVerified);
	}

}
