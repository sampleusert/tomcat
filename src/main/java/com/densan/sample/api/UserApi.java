package com.densan.sample.api;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.densan.sample.bean.UserDelReq;
import com.densan.sample.bean.UserDelRes;
import com.densan.sample.bean.UserGetRes;
import com.densan.sample.bean.UserGetResInfo;
import com.densan.sample.common.Mysql;
import com.densan.sample.constant.Result;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


@Path("user")
@Api(value = "/user", description="ユーザAPI")
public class UserApi {
	final Logger logger = Logger.getLogger(this.getClass().getName());
	
	@GET
	@Path("userGet")
	@ApiOperation(value = "ユーザ取得")
	@Produces(MediaType.APPLICATION_JSON)
	public UserGetRes userGet() {
				
		UserGetRes ugs = new UserGetRes();
		List<UserGetResInfo> userList = new ArrayList<UserGetResInfo>();
		
		try {	
			Mysql m = new Mysql();
			Connection con = m.connection();
			java.sql.PreparedStatement ps = con.prepareStatement("SELECT userId FROM Auth");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				UserGetResInfo ui = new UserGetResInfo();
				ui.userId = rs.getString(1);
				userList.add(ui);
			}
			rs.close();
			m.closeDb(con);	
			
			ugs.result = Result.OK;
			ugs.hitCount = Integer.toString(userList.size());
			ugs.userList = userList;
			
		} catch(Exception e) {
			e.printStackTrace();
			ugs.result = Result.NG;
		}
		
		return ugs;
    }
	
	@POST
	@Path("userDel")
	@ApiOperation(value = "ユーザ削除")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserDelRes userDel(UserDelReq udq) {
				
		UserDelRes uds = new UserDelRes();
		
		// リクエスト情報
		logger.info(udq.userId);
		
		// 必須チェック
		if (udq.userId == null) {
			uds.result = Result.NG;
			uds.errMessage = "parameter error";
			
			return uds;
		}
		
		try {
			uds.userId = udq.userId;
			
			// ユーザ情報をDBから削除
			Mysql m = new Mysql();
			Connection con = m.connection();
			java.sql.PreparedStatement ps = con.prepareStatement("DELETE FROM Auth WHERE userId = ?");
			ps.setString(1, udq.userId);
			int result = ps.executeUpdate();
			ps.close();
			m.closeDb(con);	
			
			// 成功
			if (result == 1) {
				uds.result = Result.OK;
				
			// 失敗
			} else {
				uds.result = Result.NG;
				uds.errMessage = "";
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
			uds.result = Result.NG;
			uds.errMessage = "system error";
		}
		
		return uds;
    }

}

