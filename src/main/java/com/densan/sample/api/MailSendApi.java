package com.densan.sample.api;

import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.densan.sample.bean.MailSendReq;
import com.densan.sample.bean.MailSendRes;
import com.densan.sample.constant.Constants;
import com.densan.sample.constant.Result;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


@Path("mail")
@Api(value = "/mail", description="メール送信API")
public class MailSendApi {
	final Logger logger = Logger.getLogger(this.getClass().getName());
	
	@POST
	@Path("mailSend")
	@ApiOperation(value = "メール送信")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public MailSendRes mailSend(MailSendReq msq) {
		
		MailSendRes mss = new MailSendRes();
		
		// リクエスト情報
		logger.info(msq.userId);
		logger.info(msq.mailTo);
		logger.info(msq.text);
		
		// 必須チェック
		if (msq.userId == null || msq.mailTo == null || msq.text == null) {
			mss.result = Result.NG;
			mss.errMessage = "parameter error";
			
			return mss;
		}
		
		try {
			mss.userId = msq.userId;
			
	    	Properties properties = new Constants().getConstants3();
	    	
	    	String fromMail = properties.getProperty("FROM_MAIL");
	    	String title = properties.getProperty("TITLE");
	    	String apiKey = properties.getProperty("API_KEY");
	    	String testTo = properties.getProperty("TEST_TO");


			Email from = new Email(fromMail);
			String subject = title;
			
			Email to = new Email(testTo);
			//Email to = new Email(msq.userId);
			Content content = new Content("text/plain", msq.text);
			Mail mail = new Mail(from, subject, to, content);
			
			SendGrid sg = new SendGrid(apiKey);
			Request request = new Request();
			request.method = Method.POST;
			request.endpoint = "mail/send";
			request.body = mail.build();
			
			// メール送信
			Response response = sg.api(request);
			logger.info(Integer.toString(response.statusCode));
			
			// 成功
			if (response.statusCode == 202) {
				mss.result = Result.OK;			
			
			// 失敗
			} else {
				logger.info(response.body);
				//logger.info(response.headers);
				mss.result = Result.NG;
				mss.errMessage = "sendgrid error";
			}		    			
			
		} catch(Exception e) {
			e.printStackTrace();
			mss.result = Result.NG;
			mss.errMessage = "system error";
		}
		
		return mss;
    }

}

