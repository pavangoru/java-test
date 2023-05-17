package com.sharp.sharp.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "modelquestions")
public class ModelQuestions {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	@Column(name = "contestid")
	private String contestId;
	@Column(name = "contestname")
	private String contestName;
	@Column(name = "question")
	private String question;
	@Column(name = "contestdate")
	private String contestDate;
	@Column(name = "contesttime")
	private String contestTime;
	@Transient
	private List<String> listOfModelQuestions;
	@Column(name = "createddate")
	private Timestamp createdDate;
	@Column(name = "statusflag")
	private boolean statusFlag;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContestId() {
		return contestId;
	}
	public void setContestId(String contestId) {
		this.contestId = contestId;
	}
	public String getContestName() {
		return contestName;
	}
	public void setContestName(String contestName) {
		this.contestName = contestName;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public List<String> getListOfModelQuestions() {
		return listOfModelQuestions;
	}
	public void setListOfModelQuestions(List<String> listOfModelQuestions) {
		this.listOfModelQuestions = listOfModelQuestions;
	}
	
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public boolean isStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(boolean statusFlag) {
		this.statusFlag = statusFlag;
	}
	public String getContestDate() {
		return contestDate;
	}
	public void setContestDate(String contestDate) {
		this.contestDate = contestDate;
	}
	public String getContestTime() {
		return contestTime;
	}
	public void setContestTime(String contestTime) {
		this.contestTime = contestTime;
	}
	
	
	
	
}
