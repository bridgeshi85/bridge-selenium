package com.extrabux.listeners;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.extrabux.tests.daigou.DaigouBaseTest;

public class ScreenshotListener extends TestListenerAdapter {
	private static final Log LOG = LogFactory.getLog(ScreenshotListener.class);

	ITestContext testContext;

	@Override
	public void onTestStart(ITestResult result) {
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		Object[] methodParams= result.getParameters();
		WebDriver driver = getDriver(methodParams);

		if (driver != null) {
			driver.quit();
		}
		LOG.info("---------- TEST PASSED ----------");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		Object[] methodParams= result.getParameters();
		WebDriver driver = getDriver(methodParams);
		String currentTest = result.getMethod().getMethodName();

		try {
			if(testContext.getCurrentXmlTest().getAllParameters().containsKey("consoleErrorLog") &&
					testContext.getCurrentXmlTest().getParameter("consoleErrorLog").equals("true"))
			{
				analyzeLog(driver, result, currentTest);
			}
			if(driver != null){
				captureScreen(driver, result, currentTest);
				LOG.info("current url: " + driver.getCurrentUrl() + ", current page title: " + driver.getTitle());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (driver != null) {
			driver.quit();
		} else {
			System.err.println("driver was null for driver on test failure: " + driver + " test name -> " + currentTest                                                                                                                                              );
		}
		LOG.info("---------- TEST FAILED ----------");
	}

	@Override
	public void onConfigurationFailure(ITestResult result) {
		super.onConfigurationFailure(result);
		if (testContext != null) {
			Object[] methodParams= result.getParameters();
			WebDriver driver = getDriver(methodParams);
			try {
				if (driver != null) {
					captureScreen(driver, result, null);
					driver.quit();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConfigurationSuccess(ITestResult result) {
		super.onConfigurationSuccess(result);
		if (testContext != null) {
			Object[] methodParams= result.getParameters();
			WebDriver driver = getDriver(methodParams);
			try {
				if (driver != null) {
					captureScreen(driver, result, null);
					driver.quit();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


    public void analyzeLog(WebDriver driver,ITestResult result, String currentTestName) throws IOException {

        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        List<LogEntry> entryList = logEntries.filter(Level.ALL);

        // create new file
	String fileName = testContext.getOutputDirectory() + "/"+currentTestName + "-console-error.log";
        File writename = new File(fileName);

        if (!writename.getParentFile().exists()) {
			writename.getParentFile().mkdirs();
		}

        writename.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));

        //Write into file
        for (LogEntry entry : entryList) {
            out.write(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage()+"\r\n"); // \r\n即为换行
        }

        out.flush();
        out.close();
    }


	private WebDriver getDriver(Object[] methodParams) {
		WebDriver driver = null;
		// find the context map
		for (Object methodParam : methodParams) {
			if(methodParam instanceof WebDriver) {
				driver = (WebDriver) methodParam;
			}
		}
		return driver;
	}

	@Override
	public void onStart(ITestContext context) {
		testContext = context;
	}

	private void captureScreen(WebDriver driver, ITestResult result, String currentTestName) throws Exception {
		if (driver != null) {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String fileName = testContext.getOutputDirectory() + "/screenshots/" + currentTestName + ".png";
			File htmlFile = new File(testContext.getOutputDirectory() + "/html/" + currentTestName + ".html");

			FileUtils.copyFile(scrFile, new File(fileName));
			FileUtils.writeStringToFile(htmlFile, driver.getPageSource());
			Reporter.setCurrentTestResult(result);
			Reporter.log("<a href=\"./" + testContext.getSuite().getName() + "/screenshots/" + currentTestName + ".png\">" + currentTestName + " Screenshot</a> | ");
			Reporter.log("<a href=\"./" + testContext.getSuite().getName() + "/html/" + currentTestName + ".html\">" + currentTestName + " HTML Output</a> | ");

			if(result.toString().contains("com.extrabux.tests.daigou")){
				// This is a Sugou test
				File apiLogHtmlFile = new File(testContext.getOutputDirectory() + "/html/" + currentTestName + "ApiLog.html");
				File errorLogHtmlFile = new File(testContext.getOutputDirectory() + "/html/" + currentTestName + "ErrorLog.html");
				FileUtils.writeStringToFile(apiLogHtmlFile, DaigouBaseTest.getApiLog(driver));
				FileUtils.writeStringToFile(errorLogHtmlFile, DaigouBaseTest.getErrorLog(driver));
				Reporter.log("<a href=\"./" + testContext.getSuite().getName() + "/html/" + currentTestName + "ApiLog.html\">" + currentTestName + " API Log</a> | ");
				Reporter.log("<a href=\"./" + testContext.getSuite().getName() + "/html/" + currentTestName + "ErrorLog.html\">" + currentTestName + " Error Log</a>");
			}
		} else {
			throw new Exception("driver was null, cannot capture screen");
		}
	}
}
