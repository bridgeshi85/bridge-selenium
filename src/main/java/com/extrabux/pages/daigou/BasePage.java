package com.extrabux.pages.daigou;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.tests.BaseTest;

public class BasePage {
	private static final Log LOG = LogFactory.getLog(BaseTest.class);

	protected WebDriver driver;

	public BasePage(final WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void goToURL(final String url) {
		LOG.info("getting URL: " + url);
		driver.get(url);
	}

}
