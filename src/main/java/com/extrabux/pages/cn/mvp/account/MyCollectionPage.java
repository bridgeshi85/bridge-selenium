package com.extrabux.pages.cn.mvp.account;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.extrabux.pages.cn.mvp.MvpDaigouBasePage;
import com.extrabux.pages.cn.mvp.ShopCartPage;
import com.extrabux.util.WebDriverUtil;

public class MyCollectionPage extends MvpDaigouBasePage{
	
	@FindBy(css="table.user-collection-lists > tbody > tr")
	List<WebElement> productList;
	
	@FindBy(linkText="删除")
	WebElement removeLink;
	
	@FindBy(css="div.bootbox.modal.bootbox-confirm.in")
	WebElement removeConfirmDialog;
	
	@FindBy(css="div.bootbox button.btn.btn-primary")
	WebElement removeConfirmBtn;
	
	private static final Log LOG = LogFactory.getLog(MyCollectionPage.class); 
	
	static final String URI = "/user.php?act=collection_list";
	
	public MyCollectionPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getUrl(String serverName){
		return "https://" + serverName + URI;
	}
	
	public int getFavCount(){
		WebDriverUtil.waitForElementPresent(driver, By.cssSelector("div.userCenterBox.boxCenterList"), 10);
		LOG.info("my fav product sum: "+productList.size());
		return productList.size();
	}
	
	public ShopCartPage addToCart(int productId){
		
		By addToCartBtnSelector = By.cssSelector("a.extrabux-orange.add-cart[href*='"+productId+"']");
		
		WebDriverUtil.waitForElementPresentAndClickable(driver, addToCartBtnSelector, 10);
		
		//click btn
		LOG.info("add "+productId+" to cart");
		
		WebElement addToCartButton = driver.findElement(addToCartBtnSelector);
		addToCartButton.click();
		
		return clickToShopCartBtnInDailog();
	}
	
	public boolean verifyProductInFav(String productName){
		WebDriverUtil.waitForElementPresent(driver, By.className("user-collection-lists"), 10);
		return WebDriverUtil.verifyElementExist(driver, By.xpath("//td[@class='user-goods-desc']/a[text()='"+productName+"']"));
	}
	
	public void deleteFav(){
		LOG.info("remove product from my fav list...");
		removeLink.click();
		WebDriverUtil.waitForElementVisible(driver, removeConfirmDialog, 5);
		
		removeConfirmBtn.click();
		
		driver.navigate().refresh();
	}
	
	public double getProductPrice(int productId){
		WebElement price = driver.findElement(By.xpath("//tr[./td[@class='user-control']/a[contains(@href,'"+
				productId+"')]]/td[@class='user-goods-price']/span"));
		LOG.info("product price is :"+price.getText());
		return getPrice(price.getText());
	}
}
