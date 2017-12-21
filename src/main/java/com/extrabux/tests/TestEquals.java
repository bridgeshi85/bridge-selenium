package com.extrabux.tests;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import com.extrabux.pages.account.InviteFriendsPage;
import com.extrabux.pages.account.InviteFriendsPage.Friend;

public class TestEquals {

	@Test
	public void testEquals() {
		Friend friend1 = new InviteFriendsPage(new FirefoxDriver()).new Friend("emillan", "11/25/2013");
		Friend friend2 = new InviteFriendsPage(new FirefoxDriver()).new Friend("emillanx", "11/25/2013");
		System.err.println(friend1.equals(friend2));
		System.err.println(friend1.getEmail() + " " + friend2.getEmail());
	}
}
