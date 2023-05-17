package com.sharp.sharp.entity;

import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "contestdetails")
public class Contestdetails {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "contestid")
	private String contestid;

	private String showid;
	@Column(name = "show_name")
	private String showname;
	private String languageid;
	private int categoryid;
	private String subcategoryid;
	private String channelname;
	private String statusid;
	@Transient
	private String userid;
	private String channelid;
	private String teamid;

	private String contestdate;
	private String contesttime;
	private Timestamp contestDateandTime;
	private String language;
	@Column(name = "teamname1_shortname")
	private String teamname1_shortname;
	@Column(name = "teamname2_shortname")
	private String teamname2_shortname;
	private String createddate;

	@Transient
	private BigInteger poolamount;

	@Transient
	private boolean joinedInLables = false;

	/*
	 * @OneToMany(cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "contestid") private List<Lables> lables;
	 */
	@Transient
	private String contestStatus;

	private String Contestname2;
	private String Contestname1;
	/*
	 * @Column(name = "share_money") private int share_money;
	 */

	private String subcategoryname;
	private String categoryname;
	private String team1imageid;
	private String team2imageid;

	@Transient
	private String statusname;

	@Transient
	private String statusdesc;

	@Transient
	private String temanme;

	@Transient
	private String teamdesc;

	@Column(name = "qreviwstatusflag")
	private int qreviwstatusflag;

	@Column(name = "bannerimage")
	private String bannerImage;

	@Column(name = "bannervalue")
	private int bannerValue;

	@Transient
	private int answerSubmit;

	public String getTemanme() {
		return temanme;
	}

	public void setTemanme(String temanme) {
		this.temanme = temanme;
	}

	public String getTeamdesc() {
		return teamdesc;
	}

	public void setTeamdesc(String teamdesc) {
		this.teamdesc = teamdesc;
	}

	public String getContestname2() {
		return Contestname2;
	}

	public void setContestname2(String contestname2) {
		Contestname2 = contestname2;
	}

	public String getContestname1() {
		return Contestname1;
	}

	public void setContestname1(String contestname1) {
		Contestname1 = contestname1;
	}

	public String getStatusname() {
		return statusname;
	}

	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}

	public String getStatusdesc() {
		return statusdesc;
	}

	public void setStatusdesc(String statusdesc) {
		this.statusdesc = statusdesc;
	}

	public String getContestid() {
		return contestid;
	}

	public void setContestid(String contestid) {
		this.contestid = contestid;
	}

	public String getShowid() {
		return showid;
	}

	public void setShowid(String showid) {
		this.showid = showid;
	}

	public String getLanguageid() {
		return languageid;
	}

	public void setLanguageid(String languageid) {
		this.languageid = languageid;
	}

	public int getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}

	public String getSubcategoryname() {
		return subcategoryname;
	}

	public void setSubcategoryname(String subcategoryname) {
		this.subcategoryname = subcategoryname;
	}

	public String getStatusid() {
		return statusid;
	}

	public void setStatusid(String statusid) {
		this.statusid = statusid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getTeamid() {
		return teamid;
	}

	public void setTeamid(String teamid) {
		this.teamid = teamid;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTeamname1_shortname() {
		return teamname1_shortname;
	}

	public void setTeamname1_shortname(String teamname1_shortname) {
		this.teamname1_shortname = teamname1_shortname;
	}

	public String getTeamname2_shortname() {
		return teamname2_shortname;
	}

	public void setTeamname2_shortname(String teamname2_shortname) {
		this.teamname2_shortname = teamname2_shortname;
	}

	public String getCreateddate() {
		return createddate;
	}

	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}

	public String getContestStatus() {
		return contestStatus;
	}

	public void setContestStatus(String contestStatus) {
		this.contestStatus = contestStatus;
	}

	public String getShowname() {
		return showname;
	}

	public void setShowname(String showname) {
		this.showname = showname;
	}

	public String getSubcategoryid() {
		return subcategoryid;
	}

	public void setSubcategoryid(String subcategoryid) {
		this.subcategoryid = subcategoryid;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public String getTeam1imageid() {
		return team1imageid;
	}

	public void setTeam1imageid(String team1imageid) {
		this.team1imageid = team1imageid;
	}

	public String getTeam2imageid() {
		return team2imageid;
	}

	public void setTeam2imageid(String team2imageid) {
		this.team2imageid = team2imageid;
	}

	public BigInteger getPoolamount() {
		return poolamount;
	}

	public void setPoolamount(BigInteger poolamount) {
		this.poolamount = poolamount;
	}

	@Transient
	private boolean inReview;

	@Transient
	private long gameamount;
	@Transient
	private long totalamount;
	@Transient
	private int totalplayers;

	@Transient
	private List<Lables> lableList;

	@Column(name = "lableidlist")
	private String lableidList;

	public String getLableidList() {
		return lableidList;
	}

	public void setLableidList(String lableidList) {
		this.lableidList = lableidList;
	}

	public List<Lables> getLableList() {
		return lableList;
	}

	public void setLableList(List<Lables> lableList) {
		this.lableList = lableList;
	}

	public boolean isInReview() {
		return inReview;
	}

	public void setInReview(boolean inReview) {
		this.inReview = inReview;
	}

	public long getGameamount() {
		return gameamount;
	}

	public void setGameamount(long gameamount) {
		this.gameamount = gameamount;
	}

	public long getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(long totalamount) {
		this.totalamount = totalamount;
	}

	public int getTotalplayers() {
		return totalplayers;
	}

	public void setTotalplayers(int totalplayers) {
		this.totalplayers = totalplayers;
	}

	public int getQreviwstatusflag() {
		return qreviwstatusflag;
	}

	public void setQreviwstatusflag(int qreviwstatusflag) {
		this.qreviwstatusflag = qreviwstatusflag;
	}

	public Timestamp getContestDateandTime() {
		return contestDateandTime;
	}

	public void setContestDateandTime(Timestamp contestDateandTime) {
		this.contestDateandTime = contestDateandTime;
	}

	public boolean isJoinedInLables() {
		return joinedInLables;
	}

	public void setJoinedInLables(boolean joinedInLables) {
		this.joinedInLables = joinedInLables;
	}

	public int getAnswerSubmit() {
		return answerSubmit;
	}

	public void setAnswerSubmit(int answerSubmit) {
		this.answerSubmit = answerSubmit;
	}

	public String getBannerImage() {
		return bannerImage;
	}

	public void setBannerImage(String bannerImage) {
		this.bannerImage = bannerImage;
	}

	public int getBannerValue() {
		return bannerValue;
	}

	public void setBannerValue(int bannerValue) {
		this.bannerValue = bannerValue;
	}

}
