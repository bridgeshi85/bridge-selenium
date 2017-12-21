package com.extrabux;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Platform;
import org.testng.annotations.Test;

import com.extrabux.webdriver.Capability;
import com.extrabux.webdriver.SeleniumConfig;
import com.thoughtworks.xstream.XStream;

public class DesiredCapabilitiesConversionTest {

	@Test
	public void toXML() {
		XStream xstream = new XStream();
		xstream.processAnnotations(SeleniumConfig.class);
		
		SeleniumConfig browserConfig = new SeleniumConfig();
		
		List<Capability> capabilities = new ArrayList<Capability>();
		Capability capability = new Capability(Platform.MAC, "firefox", "22");
		capabilities.add(capability);
		
		browserConfig.setCapabilities(capabilities);
		
		String xml = xstream.toXML(browserConfig);
		System.err.println(xml);
	}
}