package com.sharp.sharp.entity;



import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "channel")
public class Channel {
	
	@Id
	private String channelid;
	private String channelname;
	private String channeldesc;
	
	private Timestamp createddate;
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getChannelname() {
		return channelname;
	}
	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}
	public String getChanneldesc() {
		return channeldesc;
	}
	public void setChanneldesc(String channeldesc) {
		this.channeldesc = channeldesc;
	}
	public Timestamp getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Timestamp createddate) {
		this.createddate = createddate;
	} 
	
	
	

}
