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

public class SearchResultPage extends BasePage{

	private static final Log LOG = LogFactory.getLog(SearchResultPage.class);
	private static final String SEARCH_RESULT_ID = "search_result";
	
	@FindBy(id = SEARCH_RESULT_ID)
	WebElement searchResult;
	
	@FindBy(className = "aw-text-color-red")
	WebElement highLightText;
	
	public SearchResultPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public boolean verifySearchResult(String keyWord) throws Exception{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try{
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(SEARCH_RESULT_ID)));
			LOG.info("Search result highLightText:"+highLightText.getText());
			if(!searchResult.getText().equals("没有内容") || !highLightText.getText().equals(keyWord)){
				LOG.info("Search result:"+searchResult.getText());
				return false;
			}
		}catch(NoSuchElementException e){
			throw new Exception("search result element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}catch (TimeoutException te) {
			throw new Exception("timed out waiting for member to display. element was not found on current page (" + driver.getCurrentUrl() + ").  possible white page displayed");
		}
		
		return true;
	}

}
