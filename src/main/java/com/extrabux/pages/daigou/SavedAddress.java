package com.extrabux.pages.daigou;

import org.apache.commons.configuration.Configuration;

public class SavedAddress {
	String nickname;
	String firstName;
	String lastName;
	String company;
	String address1;
	String address2;
	String city;
	String state;
	String zip;
	String country;
	String phone;
	boolean isDefault;

	public String getNickname() {
		return nickname;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public String getFullAddress() {
		return address1 + " , " + city + " , " + state + " " + zip + " , " + country;
	}

	public String getCityStateZip() {
		return city + ", " + state + " " + zip;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	public void setFirstName(String name) {
		this.firstName = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public SavedAddress(Configuration config) {
		this.nickname = config.getString("usc.shipAddr.nickName");
		this.firstName = config.getString("usc.shipAddr.firstName");
		this.lastName = config.getString("usc.shipAddr.lastName");
		this.company = null;
		this.address1 = config.getString("usc.shipAddr.address1");
		this.address2 = null;
		this.city = config.getString("usc.shipAddr.city");
		this.state = config.getString("usc.shipAddr.state");
		this.zip = config.getString("usc.shipAddr.zip");
		this.country = config.getString("usc.shipAddr.country");
		this.phone = config.getString("usc.shipAddr.phone");
		this.isDefault = true;
	}

	public SavedAddress(String firstName, String lastName, String address1,
			String address2, String city, String state, String zip, String country, String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
		this.phone = phone;
	}

	public SavedAddress(String nickName, String firstName, String lastName, String company, String address1,
			String address2, String city, String state, String zip, String country, String phone, boolean isDefault) {
		this.nickname = nickName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.company = company;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
		this.phone = phone;
		this.isDefault = isDefault;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getFirstName() {
		return firstName;
	}

}
