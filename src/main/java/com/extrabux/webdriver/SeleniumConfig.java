package com.extrabux.webdriver;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("browserConfig")
public class SeleniumConfig {
	String sauceUsername;
	String sauceAccessKey;
	@XStreamImplicit(itemFieldName = "capability")
	List<Capability> capabilities;

	public String getSauceUsername() {
		return sauceUsername;
	}

	public void setSauceUsername(String sauceUsername) {
		this.sauceUsername = sauceUsername;
	}

	public String getSauceAccessKey() {
		return sauceAccessKey;
	}

	public void setSauceAccessKey(String sauceAccessKey) {
		this.sauceAccessKey = sauceAccessKey;
	}

	public List<Capability> getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(List<Capability> capabilities) {
		this.capabilities = capabilities;
	}
	
}