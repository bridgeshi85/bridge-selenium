package com.extrabux.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;


public class GoogleSpreedSheetUtil {

	  //public static final String GOOGLE_ACCOUNT_USERNAME = "test_emails@extrabux.com"; 
	  //public static final String GOOGLE_ACCOUNT_PASSWORD = "popsicles28";
	  public static final String SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/spreadsheets/1UA5av9c9-S22wetQflQMq0f95NuS035luIy84FqlpGE"; 

	  private static String pattern = "\\d+(\\.\\d+)?%";
	  
	  public static GoogleCredential createCredentialForServiceAccount(
		      HttpTransport transport,
		      JsonFactory jsonFactory,
		      String serviceAccountId,
		      Collection<String> serviceAccountScopes,
		      File p12File) throws GeneralSecurityException, IOException {
		    return new GoogleCredential.Builder().setTransport(transport)
		        .setJsonFactory(jsonFactory)
		        .setServiceAccountId(serviceAccountId)
		        .setServiceAccountScopes(serviceAccountScopes)
		        .setServiceAccountPrivateKeyFromP12File(p12File)
		        .build();
		  }
	  
	  
	  public static ListFeed getListFeed(String sq) throws Exception{
		    SpreadsheetService service = new SpreadsheetService("Get Google Spreadsheet Date");
		    
		    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		    String emailAddress = "714149318201-pv6r5t4f26jpl4be7j5ujv8abplsrs6h@developer.gserviceaccount.com";
		    
		    List<String> list = new ArrayList<String>();
		    list.add("http://spreadsheets.google.com/feeds/");
		    
		    GoogleCredential credential = createCredentialForServiceAccount(httpTransport,jsonFactory,emailAddress,
		    		list,new File("src/test/resources/google-privatekey-bi-report.p12"));
		    
		    // Build service account credential.
		    service.setOAuth2Credentials(credential);
		    
		    // Login and prompt the user to pick a sheet to use.
		    //service.setUserCredentials(GOOGLE_ACCOUNT_USERNAME, GOOGLE_ACCOUNT_PASSWORD);
		    
		    // Load sheet
		    URL metafeedUrl = new URL(SPREADSHEET_URL);
		    SpreadsheetEntry spreadsheet = service.getEntry(metafeedUrl, SpreadsheetEntry.class);
		    
		    WorksheetFeed worksheetFeed = service.getFeed(
		            spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		    WorksheetEntry worksheet = worksheetFeed.getEntries().get(0);
		    
		    //search sheet
		    sq = URLEncoder.encode(sq,"UTF-8");
		    URL listFeedUrl = new URI(worksheet.getListFeedUrl().toString() + "?sq="+sq).toURL();
		    ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		    
		    return listFeed;
	  }
	  
	  public static String[][] getExtrabuxCommission(String sq) throws Exception{

		    ListFeed listFeed = getListFeed(sq);
		    
		    String [][]biDates = new String[2][2];
		    
		    int i = 0;
		     // Iterate through each row, printing its cell values.
		    for (ListEntry row : listFeed.getEntries()) {
		    	
		    	if(!matchRegex(row.getCustomElements().getValue("extrabuxcommission"),pattern))
		    		continue;
		    	
		    	if(i>=2)
		    		 break;
		    
		       biDates[i][0] = row.getCustomElements().getValue("networkmerchantid");
		       biDates[i][1] = row.getCustomElements().getValue("extrabuxcommission");
		       i++;
		     }
		     
			return biDates;
	  }
	  
	
	  private static boolean matchRegex(String str,String pattern){
		    Pattern p=Pattern.compile(pattern);
		    Matcher m=p.matcher(str);
		    return m.matches();
	  }
		
}