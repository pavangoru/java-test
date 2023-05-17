package com.sharp.sharp.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ContestastantsEntity {

	@Id
	@GeneratedValue
	private int cId;

	private String contestantName;

	private String contestTime;
	private String category;
	private String language;
	private String channel;
	private String fromDate;
	private String toDate;
	private String contstPrice;
	private String totalPlayers;
	private String joinedPlayers;
	private String pendingPlayers;
	private boolean action;

	public int getcId() {
		return cId;
	}

	public void setcId(int cId) {
		this.cId = cId;
	}

	public String getContestantName() {
		return contestantName;
	}

	public void setContestantName(String contestantName) {
		this.contestantName = contestantName;
	}

	public String getContestTime() {
		return contestTime;
	}

	public void setContestTime(String contestTime) {
		this.contestTime = contestTime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getContstPrice() {
		return contstPrice;
	}

	public void setContstPrice(String contstPrice) {
		this.contstPrice = contstPrice;
	}

	public String getTotalPlayers() {
		return totalPlayers;
	}

	public void setTotalPlayers(String totalPlayers) {
		this.totalPlayers = totalPlayers;
	}

	public String getJoinedPlayers() {
		return joinedPlayers;
	}

	public void setJoinedPlayers(String joinedPlayers) {
		this.joinedPlayers = joinedPlayers;
	}

	public String getPendingPlayers() {
		return pendingPlayers;
	}

	public void setPendingPlayers(String pendingPlayers) {
		this.pendingPlayers = pendingPlayers;
	}

	public boolean isAction() {
		return action;
	}

	public void setAction(boolean action) {
		this.action = action;
	}

}
