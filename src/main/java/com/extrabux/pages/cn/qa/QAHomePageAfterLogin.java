package com.extrabux.pages.cn.qa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.extrabux.pages.BasePage;

public class QAHomePageAfterLogin extends BasePage{
	private static final Log LOG = LogFactory.getLog(QAHomePageAfterLogin.class);
	private static final String USER_IMG_CSS = "a.aw-user-nav-dropdown > img";

	@FindBy(css = USER_IMG_CSS)
	WebElement userImg;
	
	public QAHomePageAfterLogin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public boolean verifyUserName(String userName) throws Exception{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(USER_IMG_CSS)));
			String actual = userImg.getAttribute("alt");
			LOG.info("actual username on page: " + actual + ", expected: " + userName);
			if (!actual.equals(userName)) {
				return false;
			}
		}
		catch (NoSuchElementException e) {
			throw new Exception("user img element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		catch (TimeoutException te) {
			throw new Exception("timed out waiting for member to display. element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		return true;
	}

}
