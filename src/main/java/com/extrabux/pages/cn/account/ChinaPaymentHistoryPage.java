package com.extrabux.pages.cn.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.extrabux.pages.account.AccountPage;
import com.extrabux.util.WebDriverUtil;

public class ChinaPaymentHistoryPage extends AccountPage{

	private static final Log LOG = LogFactory.getLog(ChinaPaymentHistoryPage.class);
	static final String ACCOUNT_URI = "/users/payment-history";
	
	@FindBy(className="paymentAmount")
	WebElement paymentAmount;
	
	@FindBy(className="paymentStatus")
	WebElement paymentStatus;
	
	@FindBy(className="paymentMethod")
	WebElement paymentMethod;
	
	@FindBy(css="div.cancelPayment.tooltip")
	WebElement cancelPayment;
	
	@FindBy(xpath="//td[@class='paymentStatus'][contains(., '提现中')]")
	List<WebElement> pendingPayments;
	
	@FindBy(css="input.submit")
	WebElement cancelSubmit;
	
	@FindBy(css="input.orangeButton.shadowedButton.gradient")
	WebElement cancelSubmitConfirm;
	
	public ChinaPaymentHistoryPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public String getPaymentAmount(){
		WebDriverUtil.waitForElementPresent(driver, By.className("paymentAmount"), 10);
		LOG.info("payment amout: "+paymentAmount.getText());
		return paymentAmount.getText();
	}
	
	public String getPaymentMethod(){
		WebDriverUtil.waitForElementPresent(driver, By.className("paymentMethod"), 10);
		LOG.info("payment method: "+paymentMethod.getText());
		return paymentMethod.getText();
	}
	
	public String getPaymentStatus(){
		waitForHistoryShowUp(driver,By.xpath("//strong[text()='没有提现记录']"),180,5);
		WebDriverUtil.waitForElementPresent(driver, By.className("paymentStatus"), 10);
		LOG.info("payment status: "+paymentStatus.getText());
		return paymentStatus.getText();
	}
	
	public List<String> getPaymentInfoList(){
		List<String> paymentInfos = new ArrayList<String>();
		
		paymentInfos.add(getPaymentStatus());
		paymentInfos.add(getPaymentMethod());
		paymentInfos.add(getPaymentAmount());

		return paymentInfos;
	}
	
	public int getPendingPaymentTotal() {
		WebDriverUtil.waitForElementPresent(driver, By.id("profile"), 10);
		LOG.info("total pending payment request: "+ pendingPayments.size());
		return pendingPayments.size();
	}
	
	public void cancelPurchase(){
		LOG.info("cacel payment request.....");
		cancelPayment.click();
		WebDriverUtil.waitForElementPresent(driver, By.id("fancybox-cancel-payment"), 10);
		cancelSubmit.click();
		WebDriverUtil.waitForElementPresent(driver, By.className("cashbackCopy"), 10);
		cancelSubmitConfirm.click();
	}
	
	public String getUrl(String serverName) {
		return "http://" + serverName + ACCOUNT_URI;
	}
	
	public static void waitForHistoryShowUp(WebDriver driver, final By elementLocator, long timeOutInSeconds,long sleepInMillis) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds,sleepInMillis);
		wait.until(new ExpectedCondition<Boolean>()
		{
					public Boolean apply(WebDriver driver) {
						WebDriverUtil.waitForElementPresent(driver, By.id("profile"), 10);
						if(WebDriverUtil.verifyElementExist(driver, elementLocator)) {
							driver.navigate().refresh();
							LOG.info("refresh");
							return false;
						} else {
							return true;
						}
					}
		}
		);
	}
}
