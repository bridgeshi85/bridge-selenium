package com.extrabux.tests.api;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import com.extrabux.tests.BaseTest;

public class ApiBaseTest extends BaseTest{
	
	public Response sendAndReciveResponse(OAuthRequest request,String key,String secert){
        OAuthService oAuthService = new ServiceBuilder().provider(
        		(Class<? extends Api>) GoogleApi.class).apiKey(key)
            .apiSecret(secert).build();
        
        //token
        final Token DEFAULT_ACCESS_TOKEN = new Token("", "");

        oAuthService.signRequest(DEFAULT_ACCESS_TOKEN, request);

        //发送请求
        Response response = request.send();
        
		return response;
	}
}
