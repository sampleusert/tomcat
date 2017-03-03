package com.densan.sample.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class UserGetRes {
		public String result;
		public String errMessage;
		public String hitCount;
		public List<UserGetResInfo> userList;
}
