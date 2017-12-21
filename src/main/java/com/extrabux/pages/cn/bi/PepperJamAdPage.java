package com.extrabux.pages.cn.bi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;
import com.extrabux.util.WebDriverUtil;

public class PepperJamAdPage extends BasePage {	

	private static final Log LOG = LogFactory.getLog(PepperJamAdPage.class);
	private static final String COMMISSIONS_CLASS_NAME = "commissions";
	private static final String ID_INPUT_NAME = "ids[]";
	private static final String FILTER_STATUS_XPATH = "//div[@id='filter-status']/div/div";
	
	@FindBy(name=ID_INPUT_NAME)
	WebElement idInput;

	@FindBy(id="btnLoadData")
	WebElement filterButton;
	
	@FindBy(className=COMMISSIONS_CLASS_NAME)
	WebElement commissions;
	
	@FindBy(xpath=FILTER_STATUS_XPATH)
	WebElement filterStatus;
	
	@FindBy(xpath="//div[@id='filter-prid']/div/div")
	WebElement filterPrId;
	
	public PepperJamAdPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void closeFilterStatus(){
		WebDriverUtil.waitForElementPresent(driver, By.xpath(FILTER_STATUS_XPATH), 10);
		if(filterStatus.getAttribute("aria-pressed").equals("true"))
			filterStatus.click();
	}
	
	public void openFilterid(){
		if(filterPrId.getAttribute("aria-pressed").equals("false"))
			filterPrId.click();
	}
	
	public void filterById(String id) throws Exception{
		WebDriverUtil.waitForElementPresent(driver, By.name(ID_INPUT_NAME), 3);
		idInput.clear();
		idInput.sendKeys(id);
		filterButton.click();
		LOG.info("Search by mearchantId : " +id);
		Thread.sleep(3000);
	}

	public String getCommission() throws InterruptedException{
		WebDriverUtil.waitForElementPresent(driver, By.className(COMMISSIONS_CLASS_NAME), 10);
		String commission = commissions.getText();
		LOG.info("Commission value on the page :" +commission);
		return commission;
	}
	
}
