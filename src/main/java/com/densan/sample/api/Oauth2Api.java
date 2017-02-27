package com.densan.sample.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.densan.sample.common.GoogleOauth2;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path("oauth2")
@Api(value = "/oauth2", description="Google oauth2認証a")
public class Oauth2Api {
	final Logger logger = Logger.getLogger(this.getClass().getName());

	@GET
	@Path("auth")
	@ApiOperation(value = "初回認証")
	public Response auth() throws URISyntaxException, GeneralSecurityException, IOException {
		
		GoogleOauth2 goauth2 = new GoogleOauth2();
		
		URI location = new URI(goauth2.authorize());
		return Response.temporaryRedirect(location).build();
	}

	@GET
	@Path("callback")
	@ApiOperation(value = "コールバック")
	public Response callback(@QueryParam("code") String code) 
			throws URISyntaxException, GeneralSecurityException, IOException {
		
		GoogleOauth2 goauth2 = new GoogleOauth2();
		
		String output = code + ":" + goauth2.oauth2CallBack(code);
		
		return Response.status(200).entity(output).build();
		
		
	}

}