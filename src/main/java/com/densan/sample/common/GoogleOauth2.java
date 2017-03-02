package com.densan.sample.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;

import com.densan.sample.constant.Constants;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import java.util.List;
import java.util.Properties;


public class GoogleOauth2 {
    /** Application name. */
    private final String APPLICATION_NAME =
        "Google Calendar API Java Quickstart";

    private JsonFactory JSON_FACTORY;
    private HttpTransport HTTP_TRANSPORT;

    private final List<String> SCOPES =
        Arrays.asList(CalendarScopes.CALENDAR_READONLY);
        
    private String REDIRECT_URL;
    
    private String CLIENT_ID;
    private String CLIENT_SECRET;
    
    public GoogleOauth2() throws GeneralSecurityException, IOException {
    	HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    	JSON_FACTORY = JacksonFactory.getDefaultInstance();
    	
    	Properties properties = new Constants().getConstants();
    	
    	REDIRECT_URL = properties.getProperty("REDIRECT_URL");
    	CLIENT_ID = properties.getProperty("CLIENT_ID");
    	CLIENT_SECRET = properties.getProperty("CLIENT_SECRET");
    	
    }
    
    public GoogleAuthorizationCodeFlow getFlow() throws IOException {
    	/*InputStream in =
    			GoogleOauth2.class.getResourceAsStream("./client_secret.json");
    	GoogleClientSecrets clientSecrets =
    			GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
    	
    	GoogleAuthorizationCodeFlow flow =
    			new GoogleAuthorizationCodeFlow.Builder(
    					HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
    			.setAccessType("offline").setApprovalPrompt("force")
    			.build();*/
    	GoogleAuthorizationCodeFlow flow =
    			new GoogleAuthorizationCodeFlow.Builder(
    					HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPES)
    			.setAccessType("offline").setApprovalPrompt("force")
    			.build();
    	
    	return flow;
    }
    
    /**
     * 初回認証
     * @return
     * @throws IOException
     */
    public String authorize() throws IOException {
    	
    	GoogleAuthorizationCodeFlow flow = getFlow();
    	
       return flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URL).build();
    }
    
    /**
     * コールバック
     * @param code
     * @return
     * @throws IOException
     */
    public String oauth2CallBack(String code) throws IOException {
    	
    	GoogleAuthorizationCodeFlow flow = getFlow();
    	
    	GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URL).execute();
    	
    	return response.getRefreshToken();
    }
    
    /**
     * リフレッシュトークン
     * @param refreshToken
     * @return
     * @throws IOException
     */
    public GoogleCredential getCredetionl(String refreshToken) throws IOException {
    	/*InputStream in =
    			GoogleOauth2.class.getResourceAsStream("./client_secret.json");
    	GoogleClientSecrets clientSecrets =
    			GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));*/
    	
    	GoogleCredential credential = 
    			new GoogleCredential.Builder()
    			.setTransport(HTTP_TRANSPORT)
    			.setJsonFactory(JSON_FACTORY)
    			//.setClientSecrets(clientSecrets)
    			.setClientSecrets(CLIENT_ID, CLIENT_SECRET)
    			.build();
    	
    	credential.setRefreshToken(refreshToken);
    	
    	credential.refreshToken();
    	
    	return credential;
    	
    }
    
    /**
     * カレンダー連携
     * @param credential
     * @return
     * @throws IOException
     */
    public Calendar getCalendarService(Credential credential) throws IOException{
		
	    return new com.google.api.services.calendar.Calendar.Builder(
	            HTTP_TRANSPORT, JSON_FACTORY, credential)
	            .setApplicationName(APPLICATION_NAME)
	            .build();
    }

}
