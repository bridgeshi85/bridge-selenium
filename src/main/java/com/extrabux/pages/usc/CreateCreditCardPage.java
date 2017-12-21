package com.extrabux.pages.usc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.extrabux.pages.BasePage;

public class CreateCreditCardPage extends BasePage {
	@FindBy(className = "back")
	WebElement back;
	
	// Credit Card Info
	// card nickname
	WebElement nickname;
	WebElement number;
	@FindBy(name = "month")
	WebElement expirationMonth;
	@FindBy(name = "year")
	WebElement expirationYear;
	WebElement cv2;
	@FindBy(id = "is_default")
	WebElement defaultCheckbox;
	
	// Billing Address
	WebElement first_name;
	WebElement last_name;
	WebElement company;
	WebElement address1;
	WebElement address2;
	WebElement city;
	WebElement state_code;
	WebElement zip;
	WebElement phone;
	
	@FindBy(className = "btn-sm")
	WebElement submitBtn;

	public CreateCreditCardPage(WebDriver driver) {
		super(driver);
	}
	
	/**
	 * 
	 * @param nickName
	 * @param cardNumber
	 * @param expirationMonth
	 * @param expirationYear
	 * @param cv2
	 * @param isDefault
	 * @param firstName
	 * @param lastName
	 * @param company
	 * @param address1
	 * @param address2
	 * @param city
	 * @param state
	 * @param zip
	 * @param phone
	 * @return
	 */
	public CreditCardListPage fillFormAndSubmit(String nickName, String cardNumber, int expirationMonth, int expirationYear, 
			String cv2, boolean isDefault, String firstName, String lastName, String company, String address1,
			String address2, String city, String state, String zip, String phone) {
		fillCreditCardInfo(nickName, cardNumber, expirationMonth, expirationYear, cv2, isDefault);
		fillBillingAddressInfo(firstName, lastName, company, address1, address2, city, state, zip, phone);
			
		clickSubmit();
		
		return new CreditCardListPage(driver);
	}
	
	/**
	 * 
	 * @param nickName
	 * @param cardNumber
	 * @param expirationMonth
	 * @param expirationYear
	 * @param cv2
	 * @param isDefault
	 */
	public void fillCreditCardInfo(String nickName, String cardNumber, int expirationMonth, int expirationYear, 
			String cv2, boolean isDefault) {
		typeNickname(nickName);
		typeCardNumber(cardNumber);
		selectExpirationMonth(expirationMonth);
		selectExpirationYear(expirationYear);
		typeCv2(cv2);
		
		if (isDefault) {
			clickDefaultCheckBox();
		}	
	}
	
	/**
	 * 
	 * @param firstName
	 * @param lastName
	 * @param company
	 * @param address1
	 * @param address2
	 * @param city
	 * @param state
	 * @param zip
	 * @param phone
	 */
	public void fillBillingAddressInfo(String firstName, String lastName, String company, String address1,
			String address2, String city, String state, String zip, String phone) {
		typeFirstName(firstName);
		typeLastName(lastName);
		typeCompany(company);
		typeAddress1(address1);
		typeAddress2(address2);
		typeCity(city);
		selectState(state);
		typeZip(zip);
		typePhone(phone);
	}
	
	private void typeNickname(String nickName) {
		nickname.sendKeys(nickName);
	}
	
	private void typeCardNumber(String cardNumber) {
		number.sendKeys(cardNumber);
	}
	
	private void selectExpirationMonth(int month) {
		Select selectMonth = new Select(expirationMonth);
		selectMonth.selectByValue(Integer.toString(month));
	}
	
	private void selectExpirationYear(int year) {
		Select selectYear = new Select(expirationYear);
		selectYear.selectByValue(Integer.toString(year));
	}
	
	private void typeCv2(String cv2Number) {
		cv2.sendKeys(cv2Number);
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

	/**
	 * Select state from the dropdown
	 * @param stateCode The two letter state code.
	 */
	private void selectState(String stateCode) {
		Select select = new Select(state_code);
		select.selectByValue(stateCode);
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
