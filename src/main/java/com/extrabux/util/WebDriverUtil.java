package com.extrabux.util;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverUtil {

	private static final Log LOG = LogFactory.getLog(WebDriverUtil.class);

	public static void scrollPage(WebDriver driver,String x,String y){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy("+x+","+y+")");
		LOG.info("scroll down...");
	}
	
	public static void waitForElementVisible(WebDriver driver, WebElement element, long timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public static void waitForElementPresent(WebDriver driver, By elementLocator, long timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(elementLocator));
	}

	public static void waitForElements(WebDriver driver, List<WebElement> elements, long timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.visibilityOfAllElements(elements));
	}

	public static void waitForTextPresentInElement(WebDriver driver, WebElement element, String text, long timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	public static void waitForElementNotVisible(WebDriver driver, By elementLocator, long timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(elementLocator));
	}

	public static void waitForTextNotVisible(WebDriver driver, By elementLocator, String text, long timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.invisibilityOfElementWithText(elementLocator, text));
	}

	public static void waitForElementsNotVisible(WebDriver driver, List<By> elementLocators, long timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		for (By by : elementLocators) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
		}
	}

	public static void waitForElementClickable(WebDriver driver, WebElement element, long timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public static void waitForElementPresentAndClickable(WebDriver driver, By elementLocator, long timeOutInSeconds) {
		waitForElementPresent(driver, elementLocator, timeOutInSeconds);
		waitForElementClickable(driver, driver.findElement(elementLocator), timeOutInSeconds);
	}

	public static void waitForElementPresentAndVisible(WebDriver driver, By elementLocator, long timeOutInSeconds) {
		waitForElementPresent(driver, elementLocator, timeOutInSeconds);
		waitForElementVisible(driver, driver.findElement(elementLocator), timeOutInSeconds);
	}

	public static void waitForElementAttributeToChange(WebDriver driver, final WebElement element, long timeOutInSeconds,final String attributeName,final String expect) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(new ExpectedCondition<Boolean>()
		{
					public Boolean apply(WebDriver driver) {
						String attribute = element.getAttribute(attributeName);
						if(attribute.equals(expect)) {
							return true;
						} else {
							return false;
						}
					}
		}
		);
	}
	
	public static void waitForElementToActive(WebDriver driver, final WebElement element, long timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(new ExpectedCondition<Boolean>()
		{
					public Boolean apply(WebDriver driver) {
						LOG.info("active:"+element.equals(driver.switchTo().activeElement()));
						return element.equals(driver.switchTo().activeElement());
					}
		}
		);
	}

	public static boolean verifyElementExist(WebDriver driver, By elementLocator){

		if (driver.findElements(elementLocator).size() > 0) {
			LOG.info("element: " + elementLocator.toString()+" found");
			return true;
		}else
		{
			LOG.info("element: " + elementLocator.toString() +" was not found on current page (" + driver.getCurrentUrl());
			return false;
		}
	}

	public static boolean verifyElementExistBasedOnElement(WebDriver driver,WebElement baseElement, By elementLocator){
		if (baseElement.findElements(elementLocator).size() > 0) {
			LOG.info("element: " + elementLocator.toString()+" found");
			return true;
		}else
		{
			LOG.info("element: " + elementLocator.toString() +" was not found on current page:" + driver.getCurrentUrl());
			return false;
		}
	}
	
	public static void waitForPageToLoadComplete(WebDriver driver,long timeOutInSeconds){
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(new ExpectedCondition<Boolean>()
		{
					public Boolean apply(WebDriver driver) {
						String state = String.valueOf(
						((JavascriptExecutor) driver)
	                            .executeScript("return document.readyState"));
						LOG.info("page state is "+state);
						if(state.equals("complete")) {
							return true;
						} else {
							return false;
						}
					}
		}
		);
	}

	public static void hoverOnElement(WebElement element,WebDriver driver){
		Actions actions = new Actions(driver);
		actions.moveToElement(element).perform();
	}

	/**
	 * Switch to the first window that is not the parent
	 * @param driver
	 * @return parent handle to be used to switch back to parent
	 */
	public static String switchWindows(WebDriver driver) {
		String parentHandle = driver.getWindowHandle();
		Set<String> allHandles = driver.getWindowHandles();
		// so for now i am assuming there are two windows
		for(String handle : allHandles) {
			if(!handle.equals(parentHandle)) {
				driver.switchTo().window(handle);
			}
		}
		return parentHandle;
	}


	public static String accpectAlert(WebDriver driver,boolean acceptNextAlert){
		Alert alert = driver.switchTo().alert();
		String alertText = alert.getText();
		if (acceptNextAlert) {
			alert.accept();
			LOG.info("accept "+alertText+" alert");
		} else {
			alert.dismiss();
			LOG.info("dismiss "+alertText+" alert");
		}
		return alertText;
	}
	
	public static void switchBackToParentWindow(WebDriver driver, String parentHandle) {
		driver.switchTo().window(parentHandle);
	}
}
