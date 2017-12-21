package com.extrabux.pages.daigou;

import org.apache.commons.configuration.Configuration;

public class CreditCard {
	String name;
	String number;
	String expMonth;
	String expYear;
	String cvv;
	Boolean isDefault;

	public CreditCard(Configuration config) {
		this.name = config.getString("usc.cc.carHolderName");
		this.number = config.getString("usc.cc.cardNumber");
		this.expMonth = config.getString("usc.cc.exp.month");
		this.expYear = config.getString("usc.cc.exp.year");
		this.cvv = config.getString("usc.cc.cv2");
		this.isDefault = config.getBoolean("usc.cc.isDefault");
	}

	public CreditCard(String name, String number, String expMonth, String expYear, String cvv, boolean isDefault) {
		super();
		this.name = name;
		this.number = number;
		this.expMonth = expMonth;
		this.expYear = expYear;
		this.cvv = cvv;
		this.isDefault = isDefault;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}

	public String getExpYear() {
		return expYear;
	}

	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
}