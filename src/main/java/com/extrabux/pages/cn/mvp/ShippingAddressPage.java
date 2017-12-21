package com.extrabux.pages.cn.mvp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.extrabux.util.WebDriverUtil;

public class ShippingAddressPage extends MvpDaigouBasePage{

	static final String GOODS_ID_URI = "/goods-";
	
	// add to cart button
	@FindBy(id="consignee_0")
	WebElement consignee;

	WebElement province;
	WebElement city;
	WebElement district;
	
	@FindBy(name="address")
	WebElement addressInput;
	WebElement zipcode;
	WebElement email;
	
	WebElement mobile;
	WebElement tel;
	WebElement Submit;
	@FindBy(name="button")
	WebElement deleteButton;
	
	@FindBy(css = "label.error")
	List<WebElement> errors;
	
	private static final Log LOG = LogFactory.getLog(ShippingAddressPage.class); 
	
	public ShippingAddressPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public CheckOutPage addShippingAddress(ShippingAddressInfo address){
		fillAddressForm(address);
		LOG.info("Address form submit......");
		Submit.click();
		return new CheckOutPage(driver);
	}
	
	public void addShippingAddress(){
		LOG.info("Address form submit with empty......");
		Submit.click();
	}
	
	public void deleteShippingAddress(){
		LOG.info("delete first address......");
		deleteButton.click();
		WebDriverUtil.accpectAlert(driver, true);
	}
	
	public String getConsignee(){
		WebDriverUtil.waitForElementPresent(driver, By.id("consignee_0"), 10);
		LOG.info("consignee is: "+consignee.getText());
		return consignee.getText();
	}
	
	public void fillAddressForm(ShippingAddressInfo address){
		//wait for form present
		WebDriverUtil.waitForElementPresent(driver, By.className("form-validate"), 10);
		
		//begin fill form
		consignee.clear();
		consignee.sendKeys(address.userName);
		LOG.info("typing "+address.userName+" into consignee");
		
		new Select(province).selectByVisibleText(address.province);
		LOG.info("select "+address.province+" for province");
		
		//wait for city option present
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("select#selCities_0 option:not(:first-child)"), 15);
		new Select(city).selectByVisibleText(address.city);
		LOG.info("select "+address.city+" for city");
		
		//wait for city option present
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("select#selDistricts_0 option:not(:first-child)"), 15);
		new Select(district).selectByVisibleText(address.district);
		LOG.info("select "+address.district+" for district");
		
		addressInput.clear();
		addressInput.sendKeys(address.address);
		LOG.info("typing "+address.address+" into address");
		
		zipcode.clear();
		zipcode.sendKeys(address.zipCode);
		LOG.info("typing "+address.zipCode+" into zipcode");
		
		email.clear();
		email.sendKeys(address.email);
		LOG.info("typing "+address.email+" into email");
		
		mobile.clear();
		mobile.sendKeys(address.mobilePhoneNumber);
		LOG.info("typing "+address.mobilePhoneNumber+" into mobile");
		
		tel.clear();
		tel.sendKeys(address.telePhoneNumber);
		LOG.info("typing "+address.telePhoneNumber+" into telPhone");
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
		List<String> errorsOnPage = new ArrayList<String>();
		for (WebElement error : errors) {
			errorsOnPage.add(error.getText());
		}
		return errorsOnPage;
	}
}
