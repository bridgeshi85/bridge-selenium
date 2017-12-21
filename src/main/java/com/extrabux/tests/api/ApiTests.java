package com.extrabux.tests.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import com.alibaba.fastjson.JSON;


public class ApiTests extends ApiBaseTest{
	private static final Log LOG = LogFactory.getLog(ApiTests.class);

	@Test(groups = "production-test")
	public void testCreateClick(){
		
		String requestUrl = "http://"+getExServerName(serverName)+config.getString("api.rest_click");
				
		//String requestUrl = "http://"+serverName+"/rest/click";
		
		LOG.info("request api url:"+requestUrl);
		
        OAuthRequest request = new OAuthRequest(Verb.POST, requestUrl);
        
        request.addBodyParameter("user_id", config.getString("api.click.user_id"));
        request.addBodyParameter("merchant_id", config.getString("api.click.merchant_id"));
        
        Response response = sendAndReciveResponse(request,config.getString("api.momoso.key"),config.getString("api.momoso.secret"));
        
        LOG.info(response.getCode()+" "+response.getMessage());
        
        assertEquals(response.getCode(),201,"api response code is not 201");
        
        LOG.info("response body:\n"+JSON.toJSONString(JSON.parse(response.getBody()), true));
        
	}
}
