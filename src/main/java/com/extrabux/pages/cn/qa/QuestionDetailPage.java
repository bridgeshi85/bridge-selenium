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

public class QuestionDetailPage extends BasePage{
	
	static final String URI = "/question";
	
	private static final Log LOG = LogFactory.getLog(QuestionDetailPage.class);
	
	private static final String QUERY_TITLE_CSS = "div.mod-head > h1";
	private static final String REPLY_COUNT_CSS = "h2.hidden-xs";
	
	@FindBy(css = "div.mod-head > h1")
	WebElement questionTitle;
	
	@FindBy(name = "answer_content")
	WebElement replyContent;
	
	@FindBy(css = "a.btn.btn-normal.btn-success.pull-right.btn-reply")
	WebElement submitButton;
	
	@FindBy(css = REPLY_COUNT_CSS)
	WebElement replyCount;
	
	public QuestionDetailPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public boolean verifyTitle(String expect) throws Exception{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(QUERY_TITLE_CSS)));
			String actual = questionTitle.getText();
			LOG.info("actual title on page: " + actual + ", expected: " + expect);
			if (!actual.equals(expect)) {
				return false;
			}
		}
		catch (NoSuchElementException e) {
			throw new Exception("title element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		catch (TimeoutException te) {
			throw new Exception("timed out waiting for member to display. element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		return true;
	}
	
	public String getUrl(String serverName,int questionId) {
		return "http://" + serverName + URI + "/" + questionId;
	}
	
	public void replyQuery(String content){
		replyContent.sendKeys(content);
		submitButton.click();
		driver.navigate().refresh();
	}
	
	public int getReplyCount() throws Exception{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		int count = 0;
		try
		{
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(REPLY_COUNT_CSS)));
			String countString = replyCount.getText().substring(0,replyCount.getText().indexOf("个"));
			count = Integer.valueOf(countString.trim()).intValue();
		}catch (NoSuchElementException e) {
			throw new Exception("reply count element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		catch (TimeoutException te) {
			throw new Exception("timed out waiting for member to display. element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		return count;
	}
}
