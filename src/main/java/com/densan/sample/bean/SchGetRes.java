package com.densan.sample.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class SchGetRes {
		public String userId;
		public String result;
		public String errMessage;
		public String hitCount;
		public List<SchGetResInfo> schList;
}
