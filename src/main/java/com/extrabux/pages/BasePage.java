package com.extrabux.pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.extrabux.util.WebDriverUtil;

public class BasePage {
	private static final Log LOG = LogFactory.getLog(BasePage.class); 

	protected WebDriver driver;
	private static final String GREEN_BUBBLE_CLASSNAME = "bubble";

	@FindBy(css=".v2Bar > span.close")
	WebElement v2BarCloseButton;
	
	@FindBy(className = GREEN_BUBBLE_CLASSNAME)
	WebElement greenBubble;
	
	@FindBy(css = ".error")
	List<WebElement> errors;

	@FindBy(css = ".message.error")
	WebElement formError;
	
	public BasePage(final WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void goToURL(final String url) {
		LOG.info("getting URL: " + url);
		driver.get(url);
	}

	public Map<String, String> getErrors() {
		Map<String, String> errorMap = new HashMap<String, String>();
		if (!errors.isEmpty()) {
			for (WebElement error : errors) {
				if (error.getAttribute("for") != null) {
					String type = error.getAttribute("for");
					errorMap.put(type, error.getText());
				} else if (driver.findElements(By.cssSelector(".message.error")).size() != 0) {
					errorMap.put("form", formError.getText());
				}
			}
		} else if (driver.findElements(By.id("message-container")).size() != 0) {
			errorMap.put("form", formError.getText());
		}
		return errorMap;
	}

	public void closeV2Bar(){
		WebDriverUtil.waitForElementPresent(driver, By.className("v2Bar"), 10);
		v2BarCloseButton.click();
		LOG.info("close v2 bar");
		WebDriverUtil.waitForElementNotVisible(driver, By.className("v2Bar"), 10);
	}
	
	public boolean verifyErrorsOnPage(Map<String, String> expectedErrors) {
		// first assert the number of errors found on the page is as expected
		Map<String, String> errorsOnPage = getErrors();
		if (!(errorsOnPage.size() == expectedErrors.size())) {
			return false;
		}
		if (!errorsOnPage.equals(expectedErrors)) {
			return false;
		}
		return true;
	}
	
	public boolean verifyAddPurchaseLinkPresent()throws Exception{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try
		{
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className(GREEN_BUBBLE_CLASSNAME)));
			if(!greenBubble.isDisplayed())
			{
				String state = String.valueOf(	                   
				((JavascriptExecutor) driver)
                        .executeScript("return document.readyState"));
				LOG.info("page state is "+state);
				return false;
			}
		}catch (NoSuchElementException e) {
			throw new Exception("green bubble was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}catch(TimeoutException e)
		{
			throw new Exception("timed out waiting for add green bubble to display. element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		return true;

	}

	public static Date getExpiryDate(String str) throws ParseException{
		 
	    Pattern p=Pattern.compile("(0\\d{1}|1[0-2])/(0\\d{1}|[12]\\d{1}|3[01])/(\\d{4})");
	    Matcher m=p.matcher(str);
	    String findString = "";
	    if(m.find()){
	    	findString = m.group(0);
	    }
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); 
		Date date = sdf.parse(findString);
		return date;
	}
	
	public boolean verifyMvpProductPresent(){
		WebDriverUtil.waitForElementPresent(driver, By.className("product-lists"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.className("item-card-view"));
	}
}
