package com.densan.sample.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class UserList {
		public String result;
		public String hitCount;
		public List<UserInfo> userList;
		public String errMessage;
		public String userId;
}
