package com.extrabux.pages.daigou;

public class Verify {
	String error;
	boolean verified;

	public Verify() {};

	public Verify(String error, boolean verified) {
		super();
		this.error = error;
		this.verified = verified;
	}

	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}

}
