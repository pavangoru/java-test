package com.sharp.sharp.entity;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "leadboards")
public class LeadBoards {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	@Column(name = "rank")
	private int rank;
	@Column(name = "mymoney")
	private double myMoney;
	@Column(name = "userid")
	private String userId;
	@Column(name = "username")
	private String userName;
	@Column(name = "lableid")
	private int lableId;
	@Column(name = "contestid")
	private String contestId;
	@Column(name = "totaltime")
	private String totalTime;
	@Column(name = "totalcorrectanswers")
	private int totalCorrectAnswers;
	@Transient
	private String lableName;
	@Transient
	private BigInteger gameAmount;
	@Transient
	private BigInteger totalamount;
	@Transient
	private BigInteger poolamount;
	@Transient
	private int totalPlayers;
	@Transient
	private BigInteger joinedplayers;
	@Transient
	private int playersLeft;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getLableId() {
		return lableId;
	}

	public void setLableId(int lableId) {
		this.lableId = lableId;
	}

	public String getContestId() {
		return contestId;
	}

	public void setContestId(String contestId) {
		this.contestId = contestId;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public int getTotalCorrectAnswers() {
		return totalCorrectAnswers;
	}

	public void setTotalCorrectAnswers(int totalCorrectAnswers) {
		this.totalCorrectAnswers = totalCorrectAnswers;
	}

	public LeadBoards(int rank, String userId) {
		super();
		this.rank = rank;

		this.userId = userId;
	}

	public LeadBoards() {

	}

	public double getMyMoney() {
		return myMoney;
	}

	public void setMyMoney(double myMoney) {
		this.myMoney = myMoney;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLableName() {
		return lableName;
	}

	public void setLableName(String lableName) {
		this.lableName = lableName;
	}

	
	public BigInteger getGameAmount() {
		return gameAmount;
	}

	public void setGameAmount(BigInteger gameAmount) {
		this.gameAmount = gameAmount;
	}

	public int getTotalPlayers() {
		return totalPlayers;
	}

	public void setTotalPlayers(int totalPlayers) {
		this.totalPlayers = totalPlayers;
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

	

	

	public BigInteger getJoinedplayers() {
		return joinedplayers;
	}

	public void setJoinedplayers(BigInteger joinedplayers) {
		this.joinedplayers = joinedplayers;
	}

	

	public int getPlayersLeft() {
		return playersLeft;
	}

	public void setPlayersLeft(int playersLeft) {
		this.playersLeft = playersLeft;
	}
//factboat leadboards
	public LeadBoards(long id, int rank, double myMoney, String userId, String userName, int lableId, String contestId,
			String totalTime, int totalCorrectAnswers) {
		super();
		this.id = id;
		this.rank = rank;
		this.myMoney = myMoney;
		this.userId = userId;
		this.userName = userName;
		this.lableId = lableId;
		this.contestId = contestId;
		this.totalTime = totalTime;
		this.totalCorrectAnswers = totalCorrectAnswers;
	}

	

	

	

	
}
