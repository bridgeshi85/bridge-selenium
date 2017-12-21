package com.extrabux.tests;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.configuration.CombinedConfiguration;
//import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import com.extrabux.pages.HomePageAfterLogin;
import com.extrabux.pages.LoginPage;
import com.extrabux.pages.SignUpPage;
import com.extrabux.util.DBUtil;
import com.extrabux.webdriver.Capability;
import com.extrabux.webdriver.SeleniumConfig;
import com.thoughtworks.xstream.XStream;

public class BaseTest {
	private static final Log LOG = LogFactory.getLog(BaseTest.class);

	protected CombinedConfiguration config;

	protected String userAgent;
	protected String serverName;
	String sauceUsername;
	String sauceAccessKey;
	protected Boolean runOnSauceLabs;
	protected String browser;
	String language="";
	protected FirefoxProfile profile;
	protected ChromeOptions options;
	protected static String propertiesFileName;


	@BeforeClass(alwaysRun =true)
	public void getServerName(ITestContext testNGContext) {
		if (testNGContext.getCurrentXmlTest().getAllParameters().containsKey("serverName")
				|| System.getProperties().containsKey("serverName")) {
			serverName = testNGContext.getCurrentXmlTest().getParameter("serverName");
			if (serverName == null) {
				serverName = System.getProperty("serverName");
			}
		} else {
			serverName = "staging-current.extrabux.com";
		}
	}

	@BeforeClass(alwaysRun =true,dependsOnMethods="getBrowserType")
	public void setIntialLanguage(ITestContext testNGContext) {
		profile = new FirefoxProfile();
		if (testNGContext.getCurrentXmlTest().getAllParameters().containsKey("useLanguage")) {
			//get language from xml,will use browser language if hasn't been set
			language= testNGContext.getCurrentXmlTest().getParameter("useLanguage");
			if (browser.equals("firefox")) {
				profile.setPreference("intl.accept_languages", language);
			} else if (browser.equals("chrome")){
				options = new ChromeOptions();
				options.addArguments("--lang=" + language);
			}
		}
	}

	@BeforeClass(alwaysRun = true)
	public void getPropertiesFileName(ITestContext testNGContext) {
		propertiesFileName = testNGContext.getCurrentXmlTest().getParameter("propertiesFileName");
		if (propertiesFileName == null) {
			propertiesFileName = "test.properties";
		}
	}

	@BeforeClass(alwaysRun =true)
	public void getBrowserType(ITestContext testNGContext) {
		if (testNGContext.getCurrentXmlTest().getAllParameters().containsKey("browser")
				|| System.getProperties().containsKey("browser")) {
			browser = testNGContext.getCurrentXmlTest().getParameter("browser");
			if (browser == null) {
				browser = System.getProperty("browser");
			}
		} else {
			browser = "firefox"; 
		}
	}

	@DataProvider(name = "getWebDriver")
	public Object[][] getDriver(ITestContext testNGContext, ITestNGMethod method) throws Exception {
		return browserInfo(testNGContext, method);
	}

	public Object[][] browserInfo(ITestContext testNGContext, ITestNGMethod method) throws MalformedURLException {
		runOnSauceLabs = false;
		if (testNGContext.getCurrentXmlTest().getAllParameters().containsKey("runOnSauceLabs")
				|| System.getProperties().containsKey("runOnSauceLabs")) {
			// take the value of system property before suite xml
			runOnSauceLabs = Boolean.valueOf(System.getProperty("runOnSauceLabs"));
			// if nothing is defined then get it from
			if (runOnSauceLabs == null) {
				runOnSauceLabs = Boolean.valueOf(testNGContext.getCurrentXmlTest().getParameter("runOnSauceLabs"));
			}
		}

		if (runOnSauceLabs) {
			return getBrowsers(method);
		} else {
			WebDriver driver = null;
			if (browser.equals("firefox") || browser.isEmpty()) {
				//set user agent to help we fix the unstable tests
				userAgent = config.getString("defaultUserAgent")
				+ " Selenium_Test " + method.getMethodName() + "_" + getRandomNumber();
				profile.setPreference("general.useragent.override", userAgent);
				driver = new FirefoxDriver(profile);
				
			}
			if (browser.equals("chrome")) {
				userAgent = config.getString("chromeUserAgent") + " Selenium_Test " + method.getMethodName() + " " + getRandomNumber();
				options.addArguments("--user-agent=" + userAgent);
				driver = new ChromeDriver(options);
			}
			Object[][] webDriver = { { driver } };
			return webDriver;
		}
	}

	public Object[][] getBrowsers(ITestNGMethod method) throws MalformedURLException {
		XStream xstream = new XStream();
		xstream.processAnnotations(SeleniumConfig.class);

		SeleniumConfig driverConfig = (SeleniumConfig) xstream.fromXML(new File("src/test/resources/browser-config.xml"));
		List<Capability> capabilities = driverConfig.getCapabilities();
		method.getMethodName();
		Object[][] toReturn = new Object [capabilities.size()][1];

		for (Capability capability: capabilities) {
			sauceUsername = driverConfig.getSauceUsername();
			sauceAccessKey = driverConfig.getSauceAccessKey();
			DesiredCapabilities caps = capability.getDesiredCapablities();
			caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			caps.setCapability("name", method.getMethodName() + " - " + serverName);
			// move these to a config file
			caps.setCapability("idle-timeout", 30);
			caps.setCapability("command-timeout", 60);
			caps.setCapability("max-duration", 300);
			caps.setCapability("video-upload-on-pass", false);
			WebDriver driver = new RemoteWebDriver(new URL("http://" + sauceUsername + ":" + sauceAccessKey + "@ondemand.saucelabs.com:80/wd/hub"), caps);
			toReturn[capabilities.indexOf(capability)][0] = driver;
		}

		return toReturn;
	}

	/**
	 * Returns a random email address.
	 *
	 * @return random email value.
	 */
	public String getRandomEmail() {
		/*Random random = new Random();
		Long randomNumber = random.nextLong();

		if (randomNumber < 0L) {
			randomNumber = randomNumber * -1;
		}*/

		return "test_user_" + getRandomNumber() + "@nowhere.com";
	}

	public String getRandomOrderId() {
		/*Random random = new Random();
		Long randomNumber = random.nextLong();

		if (randomNumber < 0L) {
			randomNumber = randomNumber * -1;
		}*/

		return "test_order_" + getRandomNumber();
	}

	public Long getRandomNumber(){
		Random random = new Random();
		Long randomNumber = random.nextLong();

		if (randomNumber < 0L) {
			randomNumber = randomNumber * -1;
		}

		return randomNumber;
	}

	@BeforeClass(alwaysRun = true, dependsOnMethods = "getPropertiesFileName")
	public void loadPropertiesConfig() throws ConfigurationException {
		DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
		builder.setEncoding("UTF-8");
		builder.setFile(new File("extrabux-sel-test.xml"));
		config = builder.getConfiguration(true);

		// load properties file
		PropertiesConfiguration propertiesConfig = new PropertiesConfiguration();
		propertiesConfig.setEncoding("UTF-8");

		propertiesConfig.load(propertiesFileName);

		config.addConfiguration(propertiesConfig);
	}

	@BeforeMethod(alwaysRun=true)
	public void logTestStart(Method method) {
		LOG.info("********** Running test: " + method.getName() + " **********");
		if (userAgent != null) {
			LOG.debug("User Agent: "+ userAgent);
		}
	}

	@AfterMethod(alwaysRun=true)
	public void logTestFinish(Method method) {
		LOG.info("********** Completed test: " + method.getName() + " **********");
	}

	public HomePageAfterLogin login(String username, String passwd, WebDriver driver) throws Exception {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		HomePageAfterLogin homePage = loginPage.login(username, passwd);
		assertTrue(homePage.verifyLoggedInElementPresent(username), "Logged in email not found on page");
		return homePage;
	}

	protected String createRandomAccount(WebDriver driver) throws Exception {
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToURL(signUpPage.getUrl(serverName));

		String email = getRandomEmail();
		String defaultPassword = config.getString("defaultPassword");

		// TODO this throws an exception if something goes wrong and user
		// doesn't land on home page.
		// is this really what we want? might want to add this as an assert here
		// instead so we can
		// add to failure message and know where things went wrong.
		HomePageAfterLogin homePage = signUpPage.signUp(email, defaultPassword, defaultPassword);

		assertTrue(homePage.verifyLoggedInElementPresent(email), "Logged in email not found on page");

		return email;
	}

	protected DBUtil getDBUtil() {
		return new DBUtil(config);
	}

	protected String switchWindows(WebDriver driver) {
		LOG.debug("switcing browser windows.");
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

	protected void switchBackToParentWindow(WebDriver driver, String parentHandle) {
		driver.switchTo().window(parentHandle);
	}
	
	public static String getExServerName(String serverName){
		String exServerName = serverName.replace("ebates.cn", "extrabux.com");
		return exServerName;
	}
}