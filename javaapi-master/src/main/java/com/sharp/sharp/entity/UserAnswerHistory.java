package com.sharp.sharp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sharp.sharp.util.CustomJsonDateDeserializer;

@Entity
@Table(name = "contest_and_userwise_answers")
public class UserAnswerHistory {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	@Column(name = "qid")
	private String qIds;
	@Column(name = "answer")
	private String choosenAnswers;
	@Column(name = "totalquestions")
	private int totalattemptedQuestions;
	@Column(name = "totaltime")
	private String totalTime;
	@Column(name = "contestid")
	private String contestId;
	@Column(name = "userid")
	private String userId;
	
	@Column(name = "username")
	private String userName;
	
	@Column(name = "createddate")
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	private Date createdDate;
	@Column(name = "correctanswerscount")
	private int correctanswerscount;
	@Column(name = "each_answer_time")
	private String eachAnswerTime;
	@Column(name = "bannervalue")
	private int bannervalue;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getqIds() {
		return qIds;
	}

	public void setqIds(String qIds) {
		this.qIds = qIds;
	}

	public String getChoosenAnswers() {
		return choosenAnswers;
	}

	public void setChoosenAnswers(String choosenAnswers) {
		this.choosenAnswers = choosenAnswers;
	}

	public String getContestId() {
		return contestId;
	}

	public void setContestId(String contestId) {
		this.contestId = contestId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

	/*
	 * public Timestamp getCreatedDate() { return createdDate; }
	 * 
	 * public void setCreatedDate(Timestamp createdDate) { this.createdDate =
	 * createdDate; }
	 */

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTotalTime() {
		return totalTime;
	}

	/*
	 * public Timestamp getCreatedDate() { return createdDate; }
	 * 
	 * public void setCreatedDate(Timestamp createdDate) { this.createdDate =
	 * createdDate; }
	 */
	 
	

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	
	/*
	 * public LocalDateTime getCreatedDate() { return createdDate; }
	 * 
	 * public void setCreatedDate(LocalDateTime createdDate) { this.createdDate =
	 * createdDate; }
	 */
	
	


	/*
	 * public Date getCreatedDate() { return createdDate; }
	 * 
	 * public void setCreatedDate(Date createdDate) { this.createdDate =
	 * createdDate; }
	 */
	
	



	@Transient
	private String q1answer;
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	@Transient
	private String q2answer;
	@Transient
	private String q3answer;
	@Transient
	private String q4answer;
	@Transient
	private String q5answer;
	@Transient
	private String q6answer;
	@Transient
	private String q1id;
	@Transient
	private String q2id;
	@Transient
	private String q3id;
	@Transient
	private String q4id;
	@Transient
	private String q5id;
	@Transient
	private String q6id;
	@Transient
	private String q1answertime;
	@Transient
	private String q2answertime;
	@Transient
	private String q3answertime;
	@Transient
	private String q4answertime;
	@Transient
	private String q5answertime;
	@Transient
	private String q6answertime;

	public String getQ1answer() {
		return q1answer;
	}

	public void setQ1answer(String q1answer) {
		this.q1answer = q1answer;
	}

	public String getQ2answer() {
		return q2answer;
	}

	public void setQ2answer(String q2answer) {
		this.q2answer = q2answer;
	}

	public String getQ3answer() {
		return q3answer;
	}

	public void setQ3answer(String q3answer) {
		this.q3answer = q3answer;
	}

	public String getQ4answer() {
		return q4answer;
	}

	public void setQ4answer(String q4answer) {
		this.q4answer = q4answer;
	}

	public String getQ5answer() {
		return q5answer;
	}

	public void setQ5answer(String q5answer) {
		this.q5answer = q5answer;
	}

	public String getQ6answer() {
		return q6answer;
	}

	public void setQ6answer(String q6answer) {
		this.q6answer = q6answer;
	}

	public String getQ1id() {
		return q1id;
	}

	public void setQ1id(String q1id) {
		this.q1id = q1id;
	}

	public String getQ2id() {
		return q2id;
	}

	public void setQ2id(String q2id) {
		this.q2id = q2id;
	}

	public String getQ3id() {
		return q3id;
	}

	public void setQ3id(String q3id) {
		this.q3id = q3id;
	}

	public String getQ4id() {
		return q4id;
	}

	public void setQ4id(String q4id) {
		this.q4id = q4id;
	}

	public String getQ5id() {
		return q5id;
	}

	public void setQ5id(String q5id) {
		this.q5id = q5id;
	}

	public String getQ6id() {
		return q6id;
	}

	public void setQ6id(String q6id) {
		this.q6id = q6id;
	}

	public int getCorrectanswerscount() {
		return correctanswerscount;
	}

	public void setCorrectanswerscount(int correctanswerscount) {
		this.correctanswerscount = correctanswerscount;
	}

	public int getTotalattemptedQuestions() {
		return totalattemptedQuestions;
	}

	public void setTotalattemptedQuestions(int totalattemptedQuestions) {
		this.totalattemptedQuestions = totalattemptedQuestions;
	}

	public String getQ1answertime() {
		return q1answertime;
	}

	public void setQ1answertime(String q1answertime) {
		this.q1answertime = q1answertime;
	}

	public String getQ2answertime() {
		return q2answertime;
	}

	public void setQ2answertime(String q2answertime) {
		this.q2answertime = q2answertime;
	}

	public String getQ3answertime() {
		return q3answertime;
	}

	public void setQ3answertime(String q3answertime) {
		this.q3answertime = q3answertime;
	}

	public String getQ4answertime() {
		return q4answertime;
	}

	public void setQ4answertime(String q4answertime) {
		this.q4answertime = q4answertime;
	}

	public String getQ5answertime() {
		return q5answertime;
	}

	public void setQ5answertime(String q5answertime) {
		this.q5answertime = q5answertime;
	}

	public String getQ6answertime() {
		return q6answertime;
	}

	public void setQ6answertime(String q6answertime) {
		this.q6answertime = q6answertime;
	}

	public String getEachAnswerTime() {
		return eachAnswerTime;
	}

	public void setEachAnswerTime(String eachAnswerTime) {
		this.eachAnswerTime = eachAnswerTime;
	}
	

	public int getBannervalue() {
		return bannervalue;
	}

	public void setBannervalue(int bannervalue) {
		this.bannervalue = bannervalue;
	}

	@Override
	public String toString() {
		return "UserAnswerHistory [id=" + id + ", qIds=" + qIds + ", choosenAnswers=" + choosenAnswers
				+ ", totalattemptedQuestions=" + totalattemptedQuestions + ", totalTime=" + totalTime + ", contestId="
				+ contestId + ", userId=" + userId + ", createdDate=" + createdDate + ", correctanswerscount="
				+ correctanswerscount + ", eachAnswerTime=" + eachAnswerTime + ", q1answer=" + q1answer + ", q2answer="
				+ q2answer + ", q3answer=" + q3answer + ", q4answer=" + q4answer + ", q5answer=" + q5answer
				+ ", q6answer=" + q6answer + ", q1id=" + q1id + ", q2id=" + q2id + ", q3id=" + q3id + ", q4id=" + q4id
				+ ", q5id=" + q5id + ", q6id=" + q6id + ", q1answertime=" + q1answertime + ", q2answertime="
				+ q2answertime + ", q3answertime=" + q3answertime + ", q4answertime=" + q4answertime + ", q5answertime="
				+ q5answertime + ", q6answertime=" + q6answertime + "]";
	}
	
}
