package com.sharp.sharp.entity;

import java.math.BigInteger;

public class LeadBoardContest {
	private String contestid;
	private String showname;
	private String team1imageid;
	private String team2imageid;
	private String Contestname1;
	private String Contestname2;
	private String channelname;
	private BigInteger gameamount;
	private BigInteger totalamount;
	private BigInteger poolamount;
	private int totalplayers;
	private BigInteger joinedplayers;
	private int joinedstatus;
	public String getContestid() {
		return contestid;
	}
	public void setContestid(String contestid) {
		this.contestid = contestid;
	}
	public String getShowname() {
		return showname;
	}
	public void setShowname(String showname) {
		this.showname = showname;
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
	public String getContestname1() {
		return Contestname1;
	}
	public void setContestname1(String contestname1) {
		Contestname1 = contestname1;
	}
	public String getContestname2() {
		return Contestname2;
	}
	public void setContestname2(String contestname2) {
		Contestname2 = contestname2;
	}
	public String getChannelname() {
		return channelname;
	}
	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}
	
	public BigInteger getGameamount() {
		return gameamount;
	}
	public void setGameamount(BigInteger gameamount) {
		this.gameamount = gameamount;
	}
	public BigInteger getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(BigInteger totalamount) {
		this.totalamount = totalamount;
	}
	public BigInteger getPoolamount() {
		return poolamount;
	}
	public void setPoolamount(BigInteger poolamount) {
		this.poolamount = poolamount;
	}
	public int getTotalplayers() {
		return totalplayers;
	}
	public void setTotalplayers(int totalplayers) {
		this.totalplayers = totalplayers;
	}
	public BigInteger getJoinedplayers() {
		return joinedplayers;
	}
	public void setJoinedplayers(BigInteger joinedplayers) {
		this.joinedplayers = joinedplayers;
	}
	public int getJoinedstatus() {
		return joinedstatus;
	}
	public void setJoinedstatus(int joinedstatus) {
		this.joinedstatus = joinedstatus;
	}
	
	
	
	
	
	

}
