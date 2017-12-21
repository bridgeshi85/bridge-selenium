package com.extrabux.pages.cn.mvp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.account.ChinaMyEarningsPage;
import com.extrabux.util.WebDriverUtil;

public class MvpDaigouHomePageAfterLogin extends MvpDaigouHomePageBeforeLogin{

	private static final Log LOG = LogFactory.getLog(MvpDaigouHomePageAfterLogin.class);
	private static final String EMAIL_CLASS_NAME = "f4_b";

	@FindBy(className = EMAIL_CLASS_NAME)
	WebElement emailLoggedIn;
	
	@FindBy(linkText = "我的账户")
	WebElement myAccount;
	
	@FindBy(linkText = "退出")
	WebElement logOut;
	
	@FindBy(linkText = "更多商品 »")
	WebElement more;
	
	public MvpDaigouHomePageAfterLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public boolean verifyLoggedInElementPresent(String email) throws Exception {

		String actual = getLoggedInEmail(By.className(EMAIL_CLASS_NAME),emailLoggedIn);
		LOG.info("actual email on page: " + actual + ", expected: " + email);
		if (!actual.equals(email)) {
			return false;
		}
		return true;
	}
	
	public String getLoggedInEmail(By elementLocator,WebElement email) throws Exception{
		String actual = "";
		try{
			WebDriverUtil.waitForElementPresent(driver, elementLocator, 10);
			actual = email.getText().replaceAll("(\\s.*)", "");
		}catch (NoSuchElementException e) {
			throw new Exception("member element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		catch (TimeoutException te) {
			throw new Exception("timed out waiting for member to display. element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		return actual;
	}
	
	public MvpDaigouHomePageBeforeLogin logOut(){
		WebDriverUtil.waitForElementPresent(driver, By.linkText("我的账户"), 5);
		LOG.info("Click log out link....");
		logOut.click();
		return new MvpDaigouHomePageBeforeLogin(driver);
	}
	
	public ChinaMyEarningsPage goToMyAccount(){
		WebDriverUtil.waitForElementPresent(driver, By.linkText("我的账户"), 5);
		LOG.info("Click my account link....");
		myAccount.click();
		return new ChinaMyEarningsPage(driver);
	}
	
	public ProductsListPage goToListPage(){
		WebDriverUtil.waitForElementPresent(driver, By.linkText("我的账户"), 5);
		LOG.info("Click more link....");
		more.click();
		return new ProductsListPage(driver);
	}
}
