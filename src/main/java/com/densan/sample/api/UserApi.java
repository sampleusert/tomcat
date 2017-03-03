package com.densan.sample.api;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.densan.sample.bean.UserInfo;
import com.densan.sample.bean.UserList;
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
	public UserList userGet() {
				
		UserList ul = new UserList();
		List<UserInfo> userList = new ArrayList<UserInfo>();
		
		try {
			// 未実装 ユーザ情報をDBから取得
			UserInfo ui = new UserInfo();
			ui.userId = "test";
			
			userList.add(ui);
			userList.add(ui);
			/////////
			
			ul.result = Result.OK;
			ul.hitCount = Integer.toString(userList.size());
			ul.userList = userList;
			
		} catch(Exception e) {
			e.printStackTrace();
			ul.result = Result.NG;
		}
		
		return ul;
    }
	
	@GET
	@Path("userDel")
	@ApiOperation(value = "ユーザ削除")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserList userDel(UserInfo ui) {
		
		// userID
		logger.info(ui.userId);
		
		UserList ul = new UserList();
		
		try {
			ul.userId = ui.userId;
			// 未実装 ユーザ情報をDBから削除
			int result = 1;
			/////////
			
			// 成功
			if (result == 1) {
				ul.result = Result.OK;
				
			// 失敗
			} else {
				ul.result = Result.NG;
				ul.errMessage = "";
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
			ul.result = Result.NG;
			ul.errMessage = "";
		}
		
		return ul;
    }

}

