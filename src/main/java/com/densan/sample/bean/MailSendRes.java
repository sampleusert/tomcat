package com.densan.sample.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class MailSendRes {
	public String userId;
	public String result;
	public String errMessage;
}
