package com.extrabux.pages.cn.bi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.BasePage;

public class AWMerchantPage extends BasePage {	

	private static final Log LOG = LogFactory.getLog(AWMerchantPage.class);

	WebElement email;
	
	WebElement password;
	
	@FindBy(css = "td.commissionLevel.current")
	WebElement commission;
	
	public AWMerchantPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getCommissionValue() throws Exception {
		LOG.info("Current commission on the page is :"+ commission.getText());
		return commission.getText();
	}
	
	public String getCommissionUrl(String merchantId){
		return "https://darwin.affiliatewindow.com/awin/affiliate/157924/merchant-profile/"+
				merchantId+"/xhr-commission-group-search";
	}
}
