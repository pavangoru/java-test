package com.sharp.sharp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "revquestions")
public class ReviewQuestions {
	@Id
	private int revqid;
	private String contestdate;
	private String contesttime;
	private String contestname;
	private String subcategoryname;
	private String languagename;
	private String channelname;
	private String contestid;

	public int getRevqid() {
		return revqid;
	}

	public void setRevqid(int revqid) {
		this.revqid = revqid;
	}

	public String getContestdate() {
		return contestdate;
	}

	public void setContestdate(String contestdate) {
		this.contestdate = contestdate;
	}

	public String getContesttime() {
		return contesttime;
	}

	public void setContesttime(String contesttime) {
		this.contesttime = contesttime;
	}

	public String getContestname() {
		return contestname;
	}

	public void setContestname(String contestname) {
		this.contestname = contestname;
	}

	public String getSubcategoryname() {
		return subcategoryname;
	}

	public void setSubcategoryname(String subcategoryname) {
		this.subcategoryname = subcategoryname;
	}

	public String getLanguagename() {
		return languagename;
	}

	public void setLanguagename(String languagename) {
		this.languagename = languagename;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	public String getContestid() {
		return contestid;
	}

	public void setContestid(String contestid) {
		this.contestid = contestid;
	}

}
