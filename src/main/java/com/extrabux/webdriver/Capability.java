package com.extrabux.webdriver;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Capability {
	private Platform platform;
	private String browser;
	private String browserVersion;

	public Capability(Platform platform, String browser, String browserVersion) {
		this.platform = platform;
		this.browser = browser;
		this.browserVersion = browserVersion;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public DesiredCapabilities getDesiredCapablities() {
		return new DesiredCapabilities(browser, browserVersion, platform);
	}
}