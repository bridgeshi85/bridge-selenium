package com.extrabux.tests;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.extrabux.pages.HomePageAfterLogin;
import com.extrabux.pages.LoginPage;
import com.extrabux.pages.SignUpPage;
import com.extrabux.webdriver.Capability;
import com.extrabux.webdriver.SeleniumConfig;
import com.saucelabs.saucerest.SauceREST;
import com.thoughtworks.xstream.XStream;

public class JenkinsTest {
	
	private static final Log LOG = LogFactory.getLog(BaseTest.class);

	protected Configuration config;

	protected String serverName;
	String sauceUsername;
	String sauceAccessKey;
	Boolean allBrowsers;
	
	@Test(dataProvider = "getWebDriver")
	public void loginSuccess(WebDriver driver) throws Exception {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToURL(loginPage.getUrl(serverName));

		HomePageAfterLogin homePage = loginPage.login(config.getString("existingUserEmail"), config.getString("existingUserPassword"));

		assertTrue(homePage.verifyLoggedInElementPresent("emillan26@gmail.com"), "Logged in email not found on page");
	}

	@BeforeClass
	public void getServerName(ITestContext testNGContext) {
		if (testNGContext.getCurrentXmlTest().getAllParameters().containsKey("serverName")
				|| System.getProperties().containsKey("serverName")) {
			serverName = testNGContext.getCurrentXmlTest().getParameter("serverName");
			if (serverName == null) serverName = System.getProperty("serverName");
		} else {
			serverName = "staging-current.extrabux.com";
		}
	}

	// TODO move this into a listener
	@AfterMethod
	public void cleanupDriver(ITestResult result) {
		// find the method param and close it if its still open
		Object[] methodParams= result.getParameters();
		WebDriver driver = getDriverParam(methodParams);

		if (driver != null) {
			// right now allBrowsers is kind like runOnSauceLabs
			// TODO consider doing this differently.
			if (allBrowsers) {
				SauceREST client = new SauceREST(sauceUsername, sauceAccessKey);
				String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
				if (result.isSuccess()) {
					client.jobPassed(sessionId);
				} else {
					client.jobFailed(sessionId);
				}
			}
			//driver.close();
			driver.quit();
		}
	}

	@DataProvider(name = "getWebDriver")
	public Object[][] getDriver(ITestContext testNGContext, ITestNGMethod method) throws Exception {
		return browserInfo(testNGContext, method);
	}

	public Object[][] browserInfo(ITestContext testNGContext, ITestNGMethod method) throws MalformedURLException {
		allBrowsers = false;
		if (testNGContext.getCurrentXmlTest().getAllParameters().containsKey("allBrowsers")
				|| System.getProperties().containsKey("all browsers")) {
			allBrowsers = Boolean.valueOf(testNGContext.getCurrentXmlTest().getParameter("allBrowsers"));
			if (allBrowsers == null) allBrowsers = Boolean.valueOf(System.getProperty("allBrowsers"));
		} 

		if (allBrowsers) {
			return getBrowsers(method);
		} else {
			WebDriver driver = new FirefoxDriver();
			//WebDriver driver = new ChromeDriver();
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

	private WebDriver getDriverParam(Object[] methodParams) {
		WebDriver driver = null;
		// find the context map
		for (int i = 0; i < methodParams.length; i++) {
			if(methodParams[i] instanceof WebDriver) {
				driver = (WebDriver) methodParams[i];
			}
		}
		return driver;
	}

	/**
	 * Returns a random email address.
	 * 
	 * @return random email value.
	 */
	public String getRandomEmail() {
		Random random = new Random();
		Long randomNumber = random.nextLong();

		if (randomNumber < 0L) {
			randomNumber = randomNumber * -1;
		}

		return "test_user_" + randomNumber + "@nowhere.com";
	}

	@BeforeSuite
	public void loadPropertiesConfig() throws ConfigurationException {
		DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
		builder.setEncoding("UTF-8");
		builder.setFile(new File("extrabux-sel-test.xml"));
		config = builder.getConfiguration(true);
	}
	
	@BeforeMethod
	public void logTestStart(Method method) {
		LOG.info("********** Running test: " + method.getName() + " **********");
	}
	
	@AfterMethod
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

}
