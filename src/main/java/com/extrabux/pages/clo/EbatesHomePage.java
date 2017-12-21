package com.extrabux.pages.clo;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class EbatesHomePage extends BasePage {
	private static final Log LOG = LogFactory.getLog(EbatesHomePage.class);

	@FindBy(id = "new-join")
	public WebElement join;
	@FindBy(className = "user")
	public WebElement emailOnPage;

	static String afterJoinUrl = "http://www.ebates.com/index.do";

	public String getRandomEmail() {
		Random random = new Random();
		Long randomNumber = random.nextLong();

		if (randomNumber < 0L) {
			randomNumber = randomNumber * -1;
		}

		return "test_user_" + randomNumber + "@nowhere.com";
	}

	public EbatesHomePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public boolean verifyLoggedInElementPresent(String email) throws Exception {
		try {
			WebDriverUtil.waitForElementPresent(driver, By.className("user"), 10);
			email = email.substring(0, 7);
			String actual = emailOnPage.getText().replace("Welcome, ", "").substring(0, 7);
			LOG.info("actual email on page: " + actual + ", expected: " + email);
			if (!actual.equals(email)) {
				return false;
			}
		}
		catch (NoSuchElementException e) {
			e.printStackTrace();
			throw new Exception("member element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		catch (TimeoutException te) {
			System.err.println(driver.getPageSource());
			throw new Exception("timed out waiting for member to display. element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		return true;
	}

	public void clickJoinButton() {
		join.click();
	}
}
