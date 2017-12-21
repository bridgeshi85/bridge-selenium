package com.extrabux.pages.account;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InviteFriendsPage extends AccountPage {
	private static final Log LOG = LogFactory.getLog(InviteFriendsPage.class);

	// tabs
	@FindBy(linkText = "Overview")
	WebElement overview;
	@FindBy(linkText = "My Friends")
	WebElement myFriends;

	@FindBy(id = "history")
	WebElement friendTable;
	
	@FindBy(className = "rLink")
	WebElement referralLinkDiv;

	public InviteFriendsPage(WebDriver driver) {
		super(driver);
	}

	public void clickOverviewTab() {
		overview.click();
	}

	public void clickMyFriendsTab() {
		myFriends.click();
	}
	
	public String getReferralLink() {
		return referralLinkDiv.findElement(By.tagName("input")).getAttribute("value");
	}

	// what about when there is more than one page of bonuses?
	public List<Friend> getFriendsList() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("history")));
		WebElement tableBody = friendTable.findElement(By.tagName("tbody"));
		List<WebElement> rows = tableBody.findElements(By.tagName("tr"));

		return getFriendsFromTable(rows);
	}

	private List<Friend> getFriendsFromTable(List<WebElement> tableRows) {
		List<Friend> friends = new ArrayList<Friend>();

		for (WebElement row : tableRows) {
			List<WebElement> columns = row.findElements(By.tagName("td"));
			// first make sure i have 4 columns
			assertEquals(columns.size(), 2, "number of columns in friend table not as expected");

			String friendEmail = columns.get(0).getText();
			String date = columns.get(1).getText();
			Friend friend = new Friend(friendEmail, date);
			friends.add(friend);
			LOG.info("friend: " + friendEmail + ", date joined: " + date);
		}

		return friends;
	}

	public class Friend {
		String email;
		String dateJoined;

		public Friend(String friendEmail, String dateJoined) {
			this.email = friendEmail;
			this.dateJoined = dateJoined;
		}
		
		public String getEmail() {
			return email;
		}
		public void setEmail(String friendEmail) {
			this.email = friendEmail;
		}
		public String getDateJoined() {
			return dateJoined;
		}
		public void setDateJoined(String dateJoined) {
			this.dateJoined = dateJoined;
		}
		
		public int hashCode() {
	        return new HashCodeBuilder(17, 31).
	            append(email).
	            append(dateJoined).
	            toHashCode();
	    }

	    public boolean equals(Object obj) {
	        if (obj == null)
	            return false;
	        if (obj == this)
	            return true;
	        if (!(obj instanceof Friend))
	            return false;

	        Friend friend = (Friend) obj;
	        return new EqualsBuilder().
	            append(email, friend.email).
	            append(dateJoined, friend.dateJoined).
	            isEquals();
	    }
	    
	    public String toString() {
			return email;
		}

	}

	public boolean verifyFriendList(List<Friend> actualFriends, List<Friend> expectedFriends) {
		for(Friend friend: expectedFriends){
			LOG.info("Expected friend user name: "+friend.getEmail() + " date joined: "+friend.getDateJoined());
		}
		return actualFriends.containsAll(expectedFriends) && expectedFriends.containsAll(actualFriends);
	}

}
