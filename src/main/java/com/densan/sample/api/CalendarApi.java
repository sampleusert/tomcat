package com.densan.sample.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.densan.sample.bean.SchAddReq;
import com.densan.sample.bean.SchAddRes;
import com.densan.sample.bean.SchDelReq;
import com.densan.sample.bean.SchDelRes;
import com.densan.sample.bean.SchGetReq;
import com.densan.sample.bean.SchGetRes;
import com.densan.sample.bean.SchGetResInfo;
import com.densan.sample.common.GoogleOauth2;
import com.densan.sample.common.Mysql;
import com.densan.sample.constant.Result;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar.Events;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


@Path("calendar")
@Api(value = "/calendar", description="スケジュールAPI")
public class CalendarApi {
	final Logger logger = Logger.getLogger(this.getClass().getName());
	
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
			//refreshTokenをDBから取ってくる
			String refreshToken=null;
			Mysql m = new Mysql();
			Connection con = m.connection();
			Statement stm = con.createStatement();
			String sql = "SELECT * FROM Auth WHERE userId='" + saq.userId +"'";
			ResultSet rs = stm.executeQuery(sql);
			while(rs.next()){
				refreshToken = rs.getString("refreshToken");
				//logger.info(refreshToken);
			}
			m.closeDb(con);
			
	 		GoogleOauth2 goauth2 = new GoogleOauth2();
	 		Credential credential = goauth2.getCredetionl(refreshToken);
	 		com.google.api.services.calendar.Calendar service = goauth2.getCalendarService(credential);
	 		
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

	
	@POST
	@Path("schGet")
	@ApiOperation(value = "スケジュール参照")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SchGetRes schGet(SchGetReq sgq) throws URISyntaxException, GeneralSecurityException, IOException, InterruptedException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		
		SchGetRes sgs = new SchGetRes();
		
		String userId = sgq.userId;
		String subject = sgq.subject;
		String startDate = sgq.startDate;
		String endDate = null;
		if(sgq.endDate != null){
			endDate = sgq.endDate;
		}		
		String location = sgq.location;
		String allDay = sgq.allDay;
		String condition = sgq.condition;
		int count = 0;
		if(sgq.count != null){
			count = Integer.parseInt(sgq.count);
			//logger.info("count数 : " + count);
		}
		
		//必須チェック
		if(userId == null || startDate == null || allDay == null){
			sgs.result = Result.NG;
			sgs.errMessage = "parameter error";
			
			return sgs;			
		}		
		
		
		//refreshTokenをDBから取ってくる
		String refreshToken=null;
		Mysql m = new Mysql();
		Connection con = m.connection();
		Statement stm = con.createStatement();
		String sql = "SELECT * FROM Auth WHERE userId='" + userId +"'";
		ResultSet rs = stm.executeQuery(sql);
		while(rs.next()){
			refreshToken = rs.getString("refreshToken");
			//logger.info(refreshToken);
		}
		m.closeDb(con);
		
			//googleCalendarにアクセスし予定を参照
			GoogleOauth2 goauth2 = new GoogleOauth2();
			Credential credential = goauth2.getCredetionl(refreshToken);
			com.google.api.services.calendar.Calendar service = goauth2.getCalendarService(credential);
			 	
			/*
				if(subject == null){
					subject = "";
				}
			 */
			
				startDate = replace(startDate);
				DateTime sDate = new DateTime(startDate);				  
				
				com.google.api.services.calendar.model.Events events = null;
				List<Event> resultItems = new ArrayList();
				try{	
					if(endDate != null && sgq.count != null){//endDataあり×countあり
						endDate = replace(endDate);
						DateTime eDate = new DateTime(endDate);						
						events = service.events().list("primary")	
			        			.setMaxResults(count)
			        			.setTimeZone("Asia/Tokyo")
			        			.setTimeMax(eDate)
			        			.setTimeMin(sDate)
			        			.setQ(subject)
			        			.setOrderBy("startTime")
			        			.setSingleEvents(true)
			        			.execute();	
					}else if(endDate != null && sgq.count == null){//endDataあり×countなし
						endDate = replace(endDate);
						DateTime eDate = new DateTime(endDate);						
				        events = service.events().list("primary")	
					        	  //.setMaxResults(count)
					            .setTimeZone("Asia/Tokyo")
					            .setTimeMin(sDate)
					            .setTimeMax(eDate) 
					            .setQ(subject)
					            .setOrderBy("startTime")
					            .setSingleEvents(true)
					            .execute();						
					}else if(endDate == null && sgq.count != null){//endDataなし×countあり
				        events = service.events().list("primary")	
					        	  .setMaxResults(count)
					            .setTimeZone("Asia/Tokyo")
					            .setTimeMin(sDate)
					            //.setTimeMax(eDate) 
					            .setQ(subject)
					            .setOrderBy("startTime")
					            .setSingleEvents(true)
					            .execute();								
					}else if(endDate == null && sgq.count == null){//endDataなし×countなし
				        events = service.events().list("primary")	
					        	  //.setMaxResults(count)
					            .setTimeZone("Asia/Tokyo")
					            .setTimeMin(sDate)
					            //.setTimeMax(eDate) 
					            .setQ(subject)
					            .setOrderBy("startTime")
					            .setSingleEvents(true)
					            .execute();								
					}
				}catch(Exception e){
					e.printStackTrace();
					sgs.result = Result.NG;
					sgs.errMessage = "system error";					
				}
				
				//logger.info("subject指定 : " + events.getItems());
				List<Event> items = events.getItems();
				//結果の絞り込み
				for(Event event : items){
					boolean flg = true;
					
					//予定の場所検索
					if(location != null){
						if(event.getLocation() == null || event.getLocation().indexOf(location) == -1){
							flg = false;
						}
					}
					
					//終日を含めるか否か
					if(!allDay.equals("1")){
						if(allDay.equals("0")){
							if(event.getStart().getDate() != null){
								flg = false;
								//logger.info("終日");
							}							
						}else{
							if(event.getStart().getDate() == null){
								flg = false;
								//logger.info("時間指定");
							}							
						}
					}
					
					//絞り込み条件
					if(condition != null){
						String[] splitCondition = condition.split(" ");
		                DateTime start = event.getStart().getDateTime();
		                if (start == null) {
		                    start = event.getStart().getDate();
		                  }		
		                String startstr = start.toString();
		                //logger.info("ccc " + startstr);
		                if(startstr.indexOf("T") != -1){
		                	  startstr = startstr.substring(0,10);
		                	  //logger.info("bbb " + startstr);
		                }
						//logger.info("aaa" + splitCondition[1]);
						if(splitCondition[1].indexOf("*") == -1){//曜日指定だったら
							String[] week_name = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
							Calendar cal = Calendar.getInstance();
							String tmp[] = startstr.split("-");	
							//logger.info(tmp[0] + " " + tmp[1] + " " +tmp[2]);
							cal.set(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1])-1, Integer.parseInt(tmp[2]));
							
							int week = cal.get(Calendar.DAY_OF_WEEK)- 1;
							//logger.info("abc" + week);
							if(!week_name[week].equals(splitCondition[1])){
								flg = false;
							}
						}else{//日付指定だったら
							//logger.info(startstr);
							startstr = startstr.substring(5,10);
							//logger.info(startstr);
							splitCondition[0] = splitCondition[0].replaceAll("\\*", ".");
							//logger.info(splitCondition[0]);
							Pattern p = Pattern.compile(splitCondition[0]);
							Matcher mm = p.matcher(startstr);
							
							if (!mm.find()){
								  flg = false;
								}
						}						
					}
					
				if(flg == true){
						resultItems.add(event); 
					}				
				}     			
        
        //List<Event> items = events.getItems();
		 String EventId = null;		
        String Subject = null;
        String[] date = null;
        String StartDate = null;
        String StartTime = null;
        String EndDate = null;
        String EndTime = null;       
        String Location = null;
        int Hitcount = resultItems.size();
        //logger.info("resultitems : " + Hitcount);
        
        //SchGetRes sl = new SchGetRes();
        List<SchGetResInfo> list = new ArrayList();
        
		if (resultItems.size() == 0) {
            logger.info("No upcoming events found.");
        } else {
            logger.info("Upcoming events");
            
            for (Event event : resultItems) {
                DateTime start = event.getStart().getDateTime();
                DateTime end = event.getEnd().getDateTime();
                logger.info("end : " + end);
                if (start == null) {
                    start = event.getStart().getDate();
                }
                if (end == null) {
                    end = event.getEnd().getDate();
                }                
                //レスポンスデータに値をセット
                EventId = event.getId();
                Subject = event.getSummary();
                Location = event.getLocation();
   
                if(start.toString().indexOf("T") != -1){
                	date = returnDate(start.toString());
                  StartDate = date[0];
                  StartTime = date[1];                	
                }else{
                	StartDate = start.toString();
                }

                if(end.toString().indexOf("T") != -1){
                	date = returnDate(start.toString());
                  EndDate = date[0];
                  EndTime = date[1];                	
                }else{
                	EndDate = start.toString();
                }
                
                SchGetResInfo sf = new SchGetResInfo();
                sf.eventId = EventId;
       		  sf.location = Location;
       		  sf.subject = Subject;
       		  sf.startDate = StartDate;
       		  sf.startTime = StartTime;
       		  sf.endDate = EndDate;
       		  sf.endTime = EndTime;
       		  list.add(sf);
       		  //sl.errMessage = "test";
       		  //sl.hitCount = String.valueOf(hitcount);
       		  //sl.schList = list;
            }
         }
		
		 //ScheduleList sl = new ScheduleList();
		 //ScheduleInfo sf = new ScheduleInfo();
		 
		 //List<ScheduleInfo> list = new ArrayList();
		 //sf.location = "location";
		 //sf.subject = "subject";
		 //list.add(sf);
		 sgs.userId = userId;
		 //sgs.result = "0"; //後で判定するメソッド作る
		 //sl.errMessage = "test";
		 sgs.hitCount = String.valueOf(Hitcount);
		 sgs.schList = list;
		 sgs.result = Result.OK;
		 //sl.schList = list;
		 return sgs;
        //return Response.status(200).entity(sl).build();
	}         
    
	@POST
	@Path("schDel")
	@ApiOperation(value = "スケジュール削除")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SchDelRes schDel(SchDelReq sdq) {
 		SchDelRes sds = new SchDelRes();
 		
		// リクエスト情報
		logger.info(sdq.userId);
		logger.info(sdq.eventId);
		
		sds.userId = sdq.userId;
		
		// 必須チェック
		if (sdq.userId == null || sdq.eventId == null) {
			sds.result = Result.NG;
			sds.errMessage = "parameter error";
			
			return sds;
		}
		
		try {
			//refreshTokenをDBから取ってくる
			String refreshToken=null;
			Mysql m = new Mysql();
			Connection con = m.connection();
			Statement stm = con.createStatement();
			String sql = "SELECT * FROM Auth WHERE userId='" + sdq.userId +"'";
			ResultSet rs = stm.executeQuery(sql);
			while(rs.next()){
				refreshToken = rs.getString("refreshToken");
				//logger.info(refreshToken);
			}
			m.closeDb(con);			
			
	 		GoogleOauth2 goauth2 = new GoogleOauth2();
	 		Credential credential = goauth2.getCredetionl(refreshToken);
	 		com.google.api.services.calendar.Calendar service = goauth2.getCalendarService(credential);
	 		
		 	service.events().delete("primary", sdq.eventId).execute();				
	 		
			sds.result = Result.OK;
			
		} catch(Exception e) {
			e.printStackTrace();
			sds.result = Result.NG;
			sds.errMessage = "system error";
		}
		
		return sds;

	}	

	private static String replace(String date){
		date = date.replace(" ", "T");
		date = date + ".000+09:00";
		return date;
	}
	
	private static String[] returnDate(String date){
		String[] d = date.split("T", 0);
		d[1] = d[1].substring(0, 8);
		return d;
	}
	
}		


