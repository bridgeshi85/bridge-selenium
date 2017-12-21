package com.extrabux.pages.cn.account;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.extrabux.pages.account.InviteFriendsPage;
import com.extrabux.util.WebDriverUtil;

public class ChinaInviteFriendsPage extends InviteFriendsPage {
	private static final Log LOG = LogFactory.getLog(ChinaInviteFriendsPage.class);

	static final String URI = "/users/referral";
	
	@FindBy(css = "div.friend-list > table > tbody")
	WebElement friendTable;
	
	@FindBy(css = "div.friend-list tr.rows")
	List<WebElement> friendRows;
	
	@FindBy(id="myReferralLinkToShareField")
	WebElement referralLink;
	
	public ChinaInviteFriendsPage(WebDriver driver) {
		super(driver);
	}
	
	public String getUrl(String serverName) {
		return "https://" + serverName + URI;
	}
	
	public List<Friend> getFriendsList() {
		WebDriverUtil.waitForElementPresent(driver, By.className("friend-list"), 10);
		return getFriendsFromTable(friendRows);
	}
	
	public List<Friend> getFriendsFromTable(List<WebElement> tableRows) {
		List<Friend> friends = new ArrayList<Friend>();

		LOG.info("friend list rows:" + tableRows.size());
		for (WebElement row : tableRows) {
			List<WebElement> columns = row.findElements(By.tagName("td"));
			// first make sure i have 4 columns
			assertEquals(columns.size(), 3, "number of columns in friend table not as expected");

			String friendEmail = columns.get(0).getText();
			String date = columns.get(1).getText();
			Friend friend = new Friend(friendEmail, date);
			friends.add(friend);
			LOG.info("friend: " + friendEmail + ", date joined: " + date);
		}

		return friends;
	}
	
	public String getReferralLink() {
		return referralLink.getAttribute("value");
	}
}
