package com.extrabux.pages.cn.account;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.extrabux.pages.account.AccountSettingsPage;
import com.extrabux.util.WebDriverUtil;

public class ChinaAccountSettingPage extends AccountSettingsPage {

	private static final Log LOG = LogFactory.getLog(ChinaAccountSettingPage.class);
	private static final String CHECK_RADIO_ID = "check";
	private static final String CREDITCARD_RADIO_ID = "shenpayTagCreditCard";
	private static final String MASTERCARD_RADIO_ID = "mastercard";
	private static final String GIFTCARD_RADIO_ID = "giftCard";
	private static final String PAYPAL_RADIO_ID = "paypal";
	private static final String SHENPAY_CSS = "label[for='shenpay']";
	private static final String CHARITY_RADIO_ID = "charity";
	private static final String MESSAGE_NOTICE_CSS = "div.message.notice";
	private static final String SUBMIT_BUTTON_CSS = "input.orangeButton.shadowedButton.gradient";
	private static final String PHONE_NUMBER_CSS ="div#bind-phone-panel strong";
	private static final String CURRENT_PAYMENT_METHODS_CSS= "div.slideBox.open";
	
	WebElement firstName;
	WebElement lastName;
	WebElement street;
	WebElement city;
	WebElement zip;
	WebElement paypalEmail;
	@FindBy(css="div.open #bankAccount")
	WebElement bankAccount;
	@FindBy(css="div.open select[name='bankName']")
	WebElement bankName;
	@FindBy(css="div.open #bankUserName")
	WebElement bankUserName;
	@FindBy(css="div.open #nid")
	WebElement nid;

	@FindBy(css="div.open input.orangeButton")
	WebElement submitPaymentButton;
	
	@FindBy(id = "ccNumber")
	WebElement creditCardNumber;

	@FindBy(id = "ccExpirationMonth")
	WebElement creditCardExpirationMonth;

	@FindBy(id = "ccExpirationYear")
	WebElement creditCardExpirationYear;

	@FindBy(className = "giftCardMerchant")
	WebElement giftCardMerchant;
	
	@FindBy(id = "requestPaymentButton")
	WebElement requestPaymentButton;
	
	@FindBy(css = "a.orangeButton.confirm")
	WebElement requestPaymentConfirmButton;
	
	@FindBy(id = CHECK_RADIO_ID)
	WebElement check;
	
	@FindBy(id = CREDITCARD_RADIO_ID)
	WebElement creditCard;
	
	@FindBy(id = GIFTCARD_RADIO_ID)
	WebElement giftCard;
	
	@FindBy(id = PAYPAL_RADIO_ID)
	WebElement paypal;

	@FindBy(xpath = "//div[./label[@for='shenpay']]/input")
	WebElement shenpay;

	@FindBy(id = CHARITY_RADIO_ID)
	WebElement charity;

	@FindBy(id = MASTERCARD_RADIO_ID)
	WebElement masterCard;
	
	@FindBy(css = MESSAGE_NOTICE_CSS)
	WebElement setPaymentMethodMsg;
	
	@FindBy(css = SUBMIT_BUTTON_CSS)
	List<WebElement> submitButtons;

	@FindBy(css=CURRENT_PAYMENT_METHODS_CSS)
	WebElement currentPaymentMethod;
	
	@FindBy(name = "phone")
	WebElement phone;
	
	@FindBy(xpath="//input[@value='发送']")
	WebElement sendButton;
	
	@FindBy(css="input.phone-submit.orangeButton.shadowedButton.gradient")
	WebElement sendButtonBind;
	
	@FindBy(name = "code")
	WebElement code;
	
	@FindBy(name = "validate_code")
	WebElement validateCode;
	
	@FindBy(xpath="//input[@value='确认']")
	WebElement submitButton;
	
	@FindBy(css="form#changePhoneForm input.orangeButton")
	WebElement submitButtonBind;
	
	@FindBy(css=PHONE_NUMBER_CSS)
	WebElement bindedPhoneNumber;
	
	//change password element
	@FindBy(name="password_current")
	WebElement passwordCurrent;
	
	@FindBy(name="password_new")
	WebElement passwordNew;
	
	@FindBy(name="password_confirm")
	WebElement passwordConfirm;
	
	@FindBy(css="form#changePasswordForm>table>tbody>tr+tr>td.submit>input")
	WebElement submitChangePasswordButton;
	
	@FindBy(className="deleteCard")
	WebElement deleteCard;
	
	@FindBy(id="fancybox-close")
	WebElement closeVisaRemindButton;
	
	public ChinaAccountSettingPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void setCheckPayment(String firstname, String lastname, String address, String cityName, String zipCode) throws Exception {
		
		WebDriverUtil.waitForElementPresent(driver, By.id(CHECK_RADIO_ID), 10);
		LOG.info("Find check radio!");
			
		check.click();
		WebDriverUtil.waitForElementClickable(driver, driver.findElement(By.cssSelector("form#checkPaymentForm input.orangeButton")), 10);
		firstName.sendKeys(firstname);
		lastName.sendKeys(lastname);
		street.sendKeys(address);
		city.sendKeys(cityName);
		zip.sendKeys(zipCode);
		submit();
		LOG.info("Set payment method to check with :" + firstname + " "+ lastname);
	}
	
	public void setCreditCardPayment(String cardNumber, String bank, String userName) throws Exception {
		
		WebDriverUtil.waitForElementPresent(driver, By.id(CREDITCARD_RADIO_ID), 10);
		LOG.info("Find credit card radio!");
		
		creditCard.click();
		WebDriverUtil.waitForElementClickable(driver, driver.findElement(By.cssSelector("form#shenpayPaymentFormTagCreditCard input.orangeButton")), 10);

		/*creditCardNumber.sendKeys(cardNumber);
		new Select(creditCardExpirationMonth)
				.selectByVisibleText(expirationMonth);
		new Select(creditCardExpirationYear)
				.selectByVisibleText(ExpirationYear);*/
		Select bankSelect  = new Select(bankName);
		bankSelect.selectByValue(bank);
		bankAccount.clear();
		bankAccount.sendKeys(cardNumber);
		bankUserName.clear();
		bankUserName.sendKeys(userName);
		submit();
		LOG.info("Set payment method with card number :" + cardNumber);
	}

	public void closeVisaRemind(){
		creditCard.click();
		WebDriverUtil.waitForElementPresentAndVisible(driver, By.id("fancybox-close"), 10);
		closeVisaRemindButton.click();
		LOG.info("close visa remind");
		WebDriverUtil.waitForElementNotVisible(driver, By.id("fancybox-overlay"), 10);
	}
	
	public void setGiftCardPayment() throws Exception {
		
		WebDriverUtil.waitForElementPresent(driver, By.id(GIFTCARD_RADIO_ID), 10);
		LOG.info("Find gift card radio!");
		
		giftCard.click();
		giftCardMerchant.click();
		submit();
		LOG.info("Set payment method to gift card ");
	}

	public void setPaypalPayment(String email) throws Exception {
		
		WebDriverUtil.waitForElementPresent(driver, By.id(PAYPAL_RADIO_ID), 10);

		LOG.info("Find paypal radio!");
		
		paypal.click();
		WebDriverUtil.waitForElementClickable(driver, driver.findElement(By.cssSelector("form#paypalPaymentForm input.orangeButton")), 10);

		paypalEmail.clear();
		paypalEmail.sendKeys(email);
		submit();
		LOG.info("Set payment method to paypal with email:" + email);
	}

	public void setShenpayPayment(String bankaccount, String bankname, String bankusername,String nidNumber) {

		WebDriverUtil.waitForElementPresent(driver, By.cssSelector(SHENPAY_CSS), 10);
		LOG.info("Find shenpay radio!");

		shenpay.click();
		WebDriverUtil.waitForElementClickable(driver, driver.findElement(By.cssSelector("form#shenpayPaymentForm input.orangeButton")), 10);

		Select bankSelect  = new Select(bankName);
		bankSelect.selectByValue(bankname);
		bankAccount.clear();
		bankAccount.sendKeys(bankaccount);
		bankUserName.clear();
		bankUserName.sendKeys(bankusername);
		nid.clear();
		nid.sendKeys(nidNumber);
		
		submit();
		LOG.info("Set payment method to Shenpay");
	}
	
	public void setCharityPayment() {
		WebDriverUtil.waitForElementPresent(driver, By.id(CHARITY_RADIO_ID), 10);
		LOG.info("Find charity radio!");

		charity.click();
		WebDriverUtil.waitForElementClickable(driver, driver.findElement(By.cssSelector("form#charityPaymentForm input.orangeButton")), 10);

		submit();
		LOG.info("Set payment method to charity");
	}

	public void setMasterCardPayment(String cardNumber, String expirationMonth, String ExpirationYear){

		WebDriverUtil.waitForElementPresent(driver, By.id(MASTERCARD_RADIO_ID), 10);
		LOG.info("Find master card radio!");
		
		masterCard.click();
		WebDriverUtil.waitForElementClickable(driver, driver.findElement(By.cssSelector("form#creditCardPaymentForm input.orangeButton")), 10);

		creditCardNumber.sendKeys(cardNumber);
		new Select(creditCardExpirationMonth)
				.selectByVisibleText(expirationMonth);
		new Select(creditCardExpirationYear)
				.selectByVisibleText(ExpirationYear);
		submit();
		LOG.info("Set payment method with card number :" + cardNumber);
		
	}
	
	public void submit() {
		submitPaymentButton.click();
		LOG.info("payment form submit......");
		/*for (WebElement submitButton : submitButtons) {
			if (submitButton.isDisplayed()) {
				if(submitButton.findElement(By.xpath("../../../../..")).getAttribute("name").contains(paymentName))
				{
					LOG.debug("form name is: "+submitButton.findElement(By.xpath("../../../../..")).getAttribute("name"));
					submitButton.click();
				}
				else
				{
					LOG.debug("form name don't match,is: "+submitButton.findElement(By.xpath("../../../../..")).getAttribute("name"));
					break;
				}
				return;
			}
		}*/
	}

	public boolean verifySetPayemntMethodSucessMsgPresent(String setPaymentMethodSucessMsg) throws Exception {
		try {
			WebDriverUtil.waitForElementPresent(driver, By.cssSelector(MESSAGE_NOTICE_CSS), 10);
			String actual = setPaymentMethodMsg.getText();
			LOG.info("Actual message on page after set shenpay: " + actual + ", expected: " + setPaymentMethodSucessMsg);
			if (!actual.equals(setPaymentMethodSucessMsg)) {
				return false;
			}
		} catch (NoSuchElementException e) {
			throw new Exception(
					"The message notice was not found on current page ("
							+ driver.getCurrentUrl() + "). ");
		} catch (TimeoutException te) {
			throw new Exception(
					"Timed out waiting for member to display. element was not found on current page ("
							+ driver.getCurrentUrl() + "). ");
		}
		return true;
	}
	
	public String getCurrentPaymentMethod(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.payment-methods"), 10);
		String currentPayment="";
		if(WebDriverUtil.verifyElementExist(driver,By.cssSelector(CURRENT_PAYMENT_METHODS_CSS))){
			currentPayment = currentPaymentMethod.findElement(By.tagName("label")).getAttribute("for");
		}else{
			currentPayment="none";
		}
		LOG.info("Current payment methods is :"+currentPayment);
		return currentPayment;
	}
	
	public String getLastFourPhoneNumber(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector(PHONE_NUMBER_CSS), 10);
		LOG.info("binded phone number is: "+bindedPhoneNumber.getText());
		String lastFourNumber = bindedPhoneNumber.getText().substring(7);
		return lastFourNumber;
	}
	
	public void bindMobile(String phoneNumber,String smsCode){
		LOG.debug("typing phone number: " + phoneNumber + " into phone input.");
		phone.sendKeys(phoneNumber);
		LOG.debug("Click send button......");
		sendButtonBind.click();
		LOG.debug("typing validate code: " + smsCode + " into validate input.");
		validateCode.sendKeys(smsCode);
		WebDriverUtil.waitForElementClickable(driver, submitButtonBind, 10);
		LOG.debug("Click submit button......");
		submitButtonBind.click();
	}
	
	public void verifyPhone(String firstSevenPhoneNumber,String smsCode){
		WebDriverUtil.waitForElementPresent(driver, By.id(CHARITY_RADIO_ID), 10);
		charity.click();
		WebDriverUtil.waitForElementPresent(driver, By.id("payment-method-security-modal"), 10);
		LOG.debug("typing first seven phone number: " + firstSevenPhoneNumber + " into phone input.");
		phone.sendKeys(firstSevenPhoneNumber);
		LOG.debug("Click send button......");
		sendButton.click();
		LOG.debug("typing validate code: " + smsCode + " into validate input.");
		code.sendKeys(smsCode);
		WebDriverUtil.waitForElementClickable(driver, submitButton, 10);
		LOG.debug("Click submit button......");
		submitButton.click();
	}
	
	public void removeCreditCard(){
		WebDriverUtil.waitForElementPresent(driver, By.className("deleteCard"), 10);
		deleteCard.click();
		WebDriverUtil.accpectAlert(driver,true);
		WebDriverUtil.waitForPageToLoadComplete(driver, 15);
		WebDriverUtil.waitForElementNotVisible(driver, By.className("deleteCard"), 15);
	}
	
	public void changePassWord(String currentPassword,String newPassWord,String confirmPassWord){
		WebDriverUtil.waitForElementPresent(driver, By.name("password_current"), 10);
		LOG.debug("typing current password: " + currentPassword);
		passwordCurrent.sendKeys(currentPassword);
		LOG.debug("typing new password: " + newPassWord);
		passwordNew.sendKeys(newPassWord);
		LOG.debug("typing confirm password: " + confirmPassWord);
		passwordConfirm.sendKeys(confirmPassWord);
		LOG.debug("Click submit button......");
		WebDriverUtil.scrollPage(driver, "0", "500");
		submitChangePasswordButton.click();
		
		//wait for page reload
		WebDriverUtil.waitForElementPresent(driver, By.name("password_current"), 10);
	}
	
	public AccountSettingsPage setLocalToEnCn(){
		selectLocale("en_CN");
		return new AccountSettingsPage(driver);
	}
	
	public ChinaPaymentHistoryPage requestPayment(){
		//click request button
		WebDriverUtil.waitForElementClickable(driver, requestPaymentButton, 10);
		
		LOG.info("request payment........");
		requestPaymentButton.click();
		
		//click confirm button
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("a.orangeButton.confirm"), 10);
		requestPaymentConfirmButton.click();
		
		return new ChinaPaymentHistoryPage(driver);
	}
}