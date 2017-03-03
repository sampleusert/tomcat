package com.densan.sample;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.densan.sample.bean.SchAddReq;
import com.densan.sample.bean.SchAddRes;
import com.densan.sample.bean.ScheduleList;
import com.densan.sample.common.GoogleOauth2;
import com.densan.sample.constant.Result;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path("samplee")
@Api(value = "/samplee", description="test")
public class TestService {
	final Logger logger = Logger.getLogger(this.getClass().getName());

     @GET
     @Path("{param}")
     public Response getMsg(@PathParam("param") String msg) {
          String output = "Jersey say4 : " + msg;
          logger.info("logtest" + output);
          return Response.status(200).entity(output).build();
     }
     
 	@POST
 	@Path("schAdd")
 	@ApiOperation(value = "スケジュール登録")
 	@Consumes(MediaType.APPLICATION_JSON)
 	@Produces(MediaType.APPLICATION_JSON)
 	public SchAddRes schAdd(SchAddReq saq)  {
 		
 		SchAddRes sas = new SchAddRes();
 		
		// リクエスト情報
		logger.info(saq.userId);
		logger.info(saq.subject);
		logger.info(saq.startDate);
		logger.info(saq.endDate);
		logger.info(saq.location);
		
		sas.userId = saq.userId;
		
		// 必須チェック
		if (saq.userId == null || saq.subject == null || saq.startDate == null || saq.endDate == null) {
			sas.result = Result.NG;
			sas.errMessage = "parameter error";
			
			return sas;
		}
		
		try {
			// DBからリフレッシュトークンの取得 未実装
			String refreshToken = "";
			
	 		GoogleOauth2 goauth2 = new GoogleOauth2();
	 		Credential credential = goauth2.getCredetionl(refreshToken);
	 		Calendar service = goauth2.getCalendarService(credential);
	 		
	 		Event event = new Event();
	 		event.setSummary(saq.subject);
	 		
	 		// 終日指定
	 		if (saq.startDate.length() == 10) {
	 			DateTime startDateTime = new DateTime(saq.startDate);
	 			EventDateTime start = new EventDateTime()
	 					.setDate(startDateTime)
	 					.setTimeZone("Asia/Tokyo");
	 			event.setStart(start);
	 			
	 			DateTime endDateTime = new DateTime(saq.endDate);
	 			EventDateTime end = new EventDateTime()
	 					.setDate(endDateTime)
	 					.setTimeZone("Asia/Tokyo");
	 			event.setEnd(end);
	 		
	 		// 時間指定
	 		} else {
	 			DateTime startDateTime = new DateTime(replace(saq.startDate));
	 			EventDateTime start = new EventDateTime()
	 					.setDateTime(startDateTime)
	 					.setTimeZone("Asia/Tokyo");
	 			event.setStart(start);
	 			
	 			DateTime endDateTime = new DateTime(replace(saq.endDate));
	 			EventDateTime end = new EventDateTime()
	 					.setDateTime(endDateTime)
	 					.setTimeZone("Asia/Tokyo");
	 			event.setEnd(end);
	 			
	 		}
	 		
	 		if (saq.location != null) {
	 			event.setLocation(saq.location);
	 		}

	 		// カレンダー登録
	 		event = service.events().insert("primary", event).execute();
	 		
			sas.result = Result.OK;
			
		} catch(Exception e) {
			e.printStackTrace();
			sas.result = Result.NG;
			sas.errMessage = "system error";

		}
		
		return sas;
     }
 	
	private static String replace(String date){
		date = date.replace(" ", "T");
		date = date + ".000+09:00";
		return date;
	}



}