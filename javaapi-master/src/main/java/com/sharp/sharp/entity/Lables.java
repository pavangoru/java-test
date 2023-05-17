package com.sharp.sharp.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "lables")
public class Lables {
	@Id
	@GeneratedValue
	@Column(name = "lableid")
	private int lableId;
	
	@Column(name = "lablename")
	private String lablename;
	@Column(name = "gameamount")
	private long gameAmount;
	@Column(name = "totalplayers")
	private int totalPlayers;
	@Column(name = "totalamount")
	private long totalAmount;
	@Column(name = "poolamount")
	private BigInteger poolamount;

	@Column(name = "craeteddate")
	private Timestamp craeteddate;
	@Transient
	private String userid;
	
	@Transient
	private String lableStatus;
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getLableId() {
		return lableId;
	}

	public void setLableId(int lableId) {
		this.lableId = lableId;
	}

	

	public String getLablename() {
		return lablename;
	}

	public void setLablename(String lablename) {
		this.lablename = lablename;
	}

	public long getGameAmount() {
		return gameAmount;
	}

	public void setGameAmount(long gameAmount) {
		this.gameAmount = gameAmount;
	}

	public int getTotalPlayers() {
		return totalPlayers;
	}

	public void setTotalPlayers(int totalPlayers) {
		this.totalPlayers = totalPlayers;
	}

	public long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

	

	public BigInteger getPoolamount() {
		return poolamount;
	}

	public void setPoolamount(BigInteger poolamount) {
		this.poolamount = poolamount;
	}



	@Transient
	private int joinedStatus;
	

	@Transient
	private int joinedplayers;
	@Transient
	private int playersLeft;

	public int getJoinedplayers() {
		return joinedplayers;
	}

	public void setJoinedplayers(int joinedplayers) {
		this.joinedplayers = joinedplayers;
	}

	public int getPlayersLeft() {
		return playersLeft;
	}

	public void setPlayersLeft(int playersLeft) {
		this.playersLeft = playersLeft;
	}

	public int getJoinedStatus() {
		return joinedStatus;
	}

	public void setJoinedStatus(int joinedStatus) {
		this.joinedStatus = joinedStatus;
	}

	public Timestamp getCraeteddate() {
		return craeteddate;
	}

	public void setCraeteddate(Timestamp craeteddate) {
		this.craeteddate = craeteddate;
	}

	public String getLableStatus() {
		return lableStatus;
	}

	public void setLableStatus(String lableStatus) {
		this.lableStatus = lableStatus;
	}

	
}
