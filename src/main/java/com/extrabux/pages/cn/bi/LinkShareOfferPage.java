package com.extrabux.pages.cn.bi;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.util.WebDriverUtil;


public class LinkShareOfferPage extends LinkShareHomePage {	

	private static final Log LOG = LogFactory.getLog(LinkShareOfferPage.class);
	private static final String COMMISSION_XPATH = ".//td[4]/small[2]";
	private static final String OFFER_ROW_XPATH = "//table[@class='table table-striped']/tbody/tr";

	WebElement normaloffers;
	
	@FindBy(xpath=OFFER_ROW_XPATH)
	List<WebElement> availableOfferRowList;
	
	public LinkShareOfferPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getCommission() throws InterruptedException{
		WebDriverUtil.waitForElementPresent(driver, By.xpath(OFFER_ROW_XPATH), 10);
		String commission = "" ;
		for(WebElement offerRow : availableOfferRowList){
			if(offerRow.findElement(By.id("normaloffers")).isSelected()){
				commission = offerRow.findElement(By.xpath(COMMISSION_XPATH)).getText();
			}
		}
		LOG.info("Commission date on the page :" +commission);
		return commission;
	}
	

	
}
