package com.densan.sample.calendar;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.densan.sample.bean.ScheduleList;
import com.densan.sample.common.GoogleOauth2;
import com.densan.sample.common.Mysql;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar.Events;
import com.google.api.services.calendar.model.Event;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


@Path("calendar")
@Api(value = "/calendar", description="スケジュールAPI")
public class Calendar {
	final Logger logger = Logger.getLogger(this.getClass().getName());
	
	@GET
	@Path("schGet")
	@ApiOperation(value = "スケジュール参照")
	@Produces(MediaType.APPLICATION_JSON)
	public ScheduleList auth(@QueryParam("userId") String userId,@QueryParam("startDate") String startDate,@QueryParam("allDay") String allDay) throws URISyntaxException, GeneralSecurityException, IOException, InterruptedException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		
		/*
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
		
		GoogleOauth2 goauth2 = new GoogleOauth2();
		Credential credential = goauth2.getCredetionl(refreshToken);
		com.google.api.services.calendar.Calendar service = goauth2.getCalendarService(credential);
		
        // List the next 10 events from the primary calendar.
		  
			startDate = replace(startDate);
			DateTime sDate = new DateTime(startDate);
			logger.info("startDate : " + sDate);
		  
        com.google.api.services.calendar.model.Events events = service.events().list("primary")
            //.setMaxResults(count)
            .setTimeZone("Asia/Tokyo")
            //.setTimeMax(now)
            .setTimeMin(sDate)
            //.setQ(subject)
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute();
        List<Event> items = events.getItems();
        String output = null;
		if (items.size() == 0) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                output = event.getSummary();
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
         }
		*/
		 ScheduleList sl = new ScheduleList();
		 //HashMap<String,String> map = new HashMap();
		 //map.put("aaa", "bbb");
		 sl.errMessage = "test";
		 //sl.schList.add(map);
		 logger.info("abc : " + sl);
		 return sl;
        //return Response.status(200).entity(sl).build();
    }

	private static String replace(String date){
		date = date.replace(" ", "T");
		date = date + ".000+09:00";
		return date;
	}
		
}

