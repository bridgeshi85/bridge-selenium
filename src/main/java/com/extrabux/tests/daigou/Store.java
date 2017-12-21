package com.extrabux.tests.daigou;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Store {
	int id;
	String name;
	@JsonProperty
	public boolean canPurchase;
	@JsonProperty
	public boolean canProductInfo;
	@JsonProperty
	public boolean canProductReviews;
	@JsonProperty
	public boolean isCouponSupported;
	@JsonProperty
	public boolean isNonGuestCheckoutSupported;
	@JsonProperty
	public boolean isGuestCheckoutSupported;
	@JsonProperty
	public boolean isInStorePickupSupported;
	List<String> domains;
	boolean enabled;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getDomains() {
		return domains;
	}
	
	public void setDomains(List<String> domains) {
		this.domains = domains;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}	
}
