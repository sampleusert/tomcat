package com.densan.sample.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class SchAddReq {
	public String userId;
	public String subject;
	public String startDate;
	public String endDate;
	public String location;
}
