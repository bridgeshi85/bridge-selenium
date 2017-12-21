package com.extrabux.tests.daigou;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GetStoresList {
	protected static final Log LOG = LogFactory.getLog(GetStoresList.class);
	private final static String EBATES_API_KEY_HEADER_NAME = "EBates-APIKey";
	private final static String EBATES_SIGNATURE_HEADER_NAME = "EBates-Signature";
	private static final String STORES_URI = "/stores";

	static String uscServer = "https://qa-secure-checkout.ecbsn.com/usc";
	static String usc_api_extrabux_key = "extrabux:c2a0aed1e94f634d98fa5c12e87975058975f1f2";
	static String usc_api_extrabux_secret = "61c2ebda69c2cbb9d27fdc60d830d1d4e354b24c3e8d723cbe681d7bd328e40d";

	public static List<String> getEnabledStoreList() throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		URIBuilder builder = new URIBuilder(uscServer + STORES_URI);
		URI uri = builder.build();
		HttpGet getRequest = new HttpGet(uri);
		LOG.debug("Request URL GET " + uri);

		String partialStringToHash = getRequest.getMethod() + " " + getRequest.getURI().getPath() + " ";
		getRequest.setHeaders(getRequestHeaders(partialStringToHash));

		HttpResponse response = client.execute(getRequest);

		String responseString = getResponseString(response);

		return getEnabledStoresFromResponse(responseString);
	}

	private static List<String> getEnabledStoresFromResponse(String responseString) throws Exception {
		List<String> enabledStores = new ArrayList<String>();

		ObjectMapper mapper = new ObjectMapper();
		//ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

		// This is just to log formatted json for readability
/*		try {
			LOG.debug("RESPONSE: " + writer.writeValueAsString(mapper.readValue(responseString, Object.class)));
		} catch (JsonParseException jpe) {
			LOG.debug("Raw response content: " + responseString);
			throw new Exception("Problem parsing response!\nResponse String: " + responseString);
		}*/

		List<Store> storesList = mapper.readValue(responseString, new TypeReference<List<Store>>() {
		});

		for (Store store : storesList) {
			if (store.isEnabled()) {
				enabledStores.add(store.getName());
			}
		}
		Collections.sort(enabledStores);
		return enabledStores;
	}

	/**
	 * Sets the headers needed for USC API Access
	 * @param requestEntity
	 * @return the list of needed headers
	 * @throws IOException
	 */
	private static Header[] getRequestHeaders(String stringToHash) throws IOException {
		List<Header> headers = new ArrayList<Header>();

		Header apiKey = new BasicHeader(EBATES_API_KEY_HEADER_NAME, usc_api_extrabux_key);
		headers.add(apiKey);
		Header signature = new BasicHeader(EBATES_SIGNATURE_HEADER_NAME, calculateSignature(stringToHash));
		headers.add(signature);
		// is this needed right now?  rory did not have this in the sample code that he sent me.
		/*Header version = new BasicHeader(EBATES_VERSION_HEADER_NAME, "");
		headers.add(version);*/

		return headers.toArray(new Header[headers.size()]);
	}

	private static String calculateSignature(String stringWithoutKeyToHash) throws IOException {
		String sha256hex = DigestUtils.sha256Hex(usc_api_extrabux_secret + " " + stringWithoutKeyToHash);

		return sha256hex;
	}

	private static String getResponseString(HttpResponse response) throws IOException {
		InputStream in = response.getEntity().getContent();
		String responseString = IOUtils.toString(in, "UTF-8");

		return responseString;
	}

	@Test
	public void testStoreList() throws Exception {
		List<String> stores = getEnabledStoreList();

		System.err.println(stores);
	}

}
