package com.extrabux.pages.usc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.extrabux.pages.BasePage;

public class EnterNewShippingAddressPage extends BasePage {
	@FindBy(className = "back")
	WebElement back;

	@FindBy(id = "nickname")
	WebElement addressNickName;
	WebElement first_name;
	WebElement last_name;
	WebElement company;
	WebElement address1;
	WebElement address2;
	WebElement city;
	WebElement state_code;
	WebElement zip;
	WebElement phone;
	@FindBy(id = "is_default")
	WebElement defaultCheckbox;
	@FindBy(className = "btn-sm")
	WebElement submitBtn;

	public EnterNewShippingAddressPage(WebDriver driver) {
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
	 * @param state
	 * @param zip
	 * @param phone
	 * @param isDefault
	 */
	public void fillFormAndSubmit(String nickName, String firstName, String lastName, String company, String address1,
			String address2, String city, String state, String zip, String phone, boolean isDefault) {
		typeNickName(nickName);
		typeFirstName(firstName);
		typeLastName(lastName);
		if (company != null && !company.isEmpty()) {
			typeCompany(company);
		}
		typeAddress1(address1);
		if (address2 != null && !address2.isEmpty()) {
			typeAddress2(address2);
		}
		typeCity(city);
		typeState(state);
		typeZip(zip);
		typePhone(phone);
		if (isDefault) {
			clickDefaultCheckBox();
		}		
		clickSubmit();
	}

	private void typeNickName(String addressNickname) {
		this.addressNickName.sendKeys(addressNickname);
	}

	private void typeFirstName(String firstName) {
		first_name.sendKeys(firstName);
	}

	private void typeLastName(String lastName) {
		last_name.sendKeys(lastName);
	}

	private void typeCompany(String company) {
		this.company.sendKeys(company);
	}

	private void typeAddress1(String address1) {
		this.address1.sendKeys(address1);
	}

	private void typeAddress2(String address2) {
		this.address2.sendKeys(address2);
	}

	private void typeCity(String city) {
		this.city.sendKeys(city);
	}

	private void typeState(String stateCode) {
		state_code.sendKeys(stateCode);
	}

	private void typeZip(String zip) {
		this.zip.sendKeys(zip);
	}

	private void typePhone(String phone) {
		this.phone.sendKeys(phone);
	}

	private void clickDefaultCheckBox() {
		defaultCheckbox.click();
	}

	private void clickSubmit() {
		submitBtn.click();
	}

	public void goBack() {
		back.click();
	}

}
