package com.densan.sample.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.densan.sample.common.GoogleOauth2;
import com.densan.sample.common.Mysql;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path("oauth2")
@Api(value = "/oauth2", description="Google oauth2認証")
public class Oauth2Api {
	final Logger logger = Logger.getLogger(this.getClass().getName());

	@GET
	@Path("auth")
	@ApiOperation(value = "初回認証")
	public Response auth() throws URISyntaxException, GeneralSecurityException, IOException, InterruptedException {
	    NewCookie cookie = new NewCookie("userId", "333");
	    
	    //return Response.ok("OK").cookie(cookie).build();
	    
		GoogleOauth2 goauth2 = new GoogleOauth2();
		
		URI location = new URI(goauth2.authorize());
		return Response.temporaryRedirect(location).cookie(cookie).build();
		
	}

	@GET
	@Path("callback")
	@ApiOperation(value = "コールバック")
	public Response callback(@QueryParam("code") String code, @CookieParam("userId") Cookie cookie ) 
			throws URISyntaxException, GeneralSecurityException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, InterruptedException {
		
		GoogleOauth2 goauth2 = new GoogleOauth2();
		Mysql m = new Mysql();
		Connection con = m.connection();
		
		Statement stm = con.createStatement();
		String sql = "insert into Auth values('" + cookie.getValue() + "','" + goauth2.oauth2CallBack(code) + "')";
		stm.executeUpdate(sql);
		
		m.closeDb(con);
		
		//String output = code + ":" + goauth2.oauth2CallBack(code) + ":" + cookie.getValue();
		NewCookie userCookie = new NewCookie(cookie,null,-1,false);
		
		return Response.status(200).cookie(userCookie).build();
		
		
	}

}