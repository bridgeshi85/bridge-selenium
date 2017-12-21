package com.extrabux.pages.daigou;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SavedCreditCard {
	private static final Log LOG = LogFactory.getLog(SavedCreditCard.class);

	String cardHolderName;
	CreditCardType type;
	String maskedNumber;
	String expiration;
	String nickname;
	boolean isDefault;

	public SavedCreditCard(){};

	public SavedCreditCard(String cardHolderName, CreditCardType type,
			String maskedNumber, String expiration, boolean isDefault) {
		super();
		this.cardHolderName = cardHolderName;
		this.type = type;
		this.maskedNumber = maskedNumber;
		this.expiration = expiration;
		this.isDefault = isDefault;
	}

	public SavedCreditCard(CreditCardType type, String maskedNumber) {
		super();
		this.type = type;
		this.maskedNumber = maskedNumber;

		switch (type) {
			case VISA:
				LOG.debug("The credit card is a Visa.");
				this.nickname = "Visa";
				break;
			case MAST:
				LOG.debug("The credit card is a Mastercard.");
				this.nickname = "Master";
				break;
			case DISC:
				LOG.debug("The credit card is a Discover.");
				this.nickname = "Discover";
				break;
			case AMEX:
				LOG.debug("The credit card is an AmEx.");
				this.nickname = "Amex";
				break;
			default:
				LOG.debug("The credit card was not a valid type.");
		}

		this.nickname = maskedNumber;
	}

	public SavedCreditCard(CreditCardType type,
			String maskedNumber, String nickname) {
		super();
		this.type = type;
		this.maskedNumber = maskedNumber;
		this.nickname = nickname;
	}

	public SavedCreditCard(CreditCardType type,
			String maskedNumber, boolean isDefault) {
		super();
		this.type = type;
		this.maskedNumber = maskedNumber;

		switch (type) {
			case VISA:
				LOG.debug("The credit card is a Visa.");
				this.nickname = "Visa";
				break;
			case MAST:
				LOG.debug("The credit card is a Mastercard.");
				this.nickname = "Master";
				break;
			case DISC:
				LOG.debug("The credit card is a Discover.");
				this.nickname = "Discover";
				break;
			case AMEX:
				LOG.debug("The credit card is an AmEx.");
				this.nickname = "Amex";
				break;
			default:
				LOG.debug("The credit card was not a valid type.");
		}

		this.nickname = maskedNumber;
		this.isDefault = isDefault;
	}

	public SavedCreditCard(CreditCardType type,
			String maskedNumber, String nickname, boolean isDefault) {
		super();
		this.type = type;
		this.maskedNumber = maskedNumber;
		this.nickname = nickname;
		this.isDefault = isDefault;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public CreditCardType getType() {
		return type;
	}

	public void setType(CreditCardType type) {
		this.type = type;
	}

	public String getMaskedNumber() {
		return maskedNumber;
	}

	public void setMaskedNumber(String maskedNumber) {
		this.maskedNumber = maskedNumber;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public void switchDefault() {
		isDefault = !isDefault;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
