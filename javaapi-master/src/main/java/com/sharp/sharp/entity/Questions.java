package com.sharp.sharp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "questions")
public class Questions {
	@Id
	@GeneratedValue
	private long qid;
	private String qname;
	private String qdesc;
	private String createddate;
	private String contestid;
//	private String lableid;
	private String options;
	private String answer;

	@Transient
	private String option1;
	@Transient
	private String option2;
	@Transient
	private String option3;
	@Transient
	private String option4;
	@Transient
	private boolean answerStatus = false;
	@Transient
	private String userAnswer;

	@Column(name = "qreviwstatusflag")
	private int qreviwStatusFlag;

	@Column(name = "bannervalue")
	private int bannerValue;

	public String getQname() {
		return qname;
	}

	public long getQid() {
		return qid;
	}

	public void setQid(long qid) {
		this.qid = qid;
	}

	public void setQname(String qname) {
		this.qname = qname;
	}

	public String getQdesc() {
		return qdesc;
	}

	public void setQdesc(String qdesc) {
		this.qdesc = qdesc;
	}

	public String getContestid() {
		return contestid;
	}

	public void setContestid(String contestid) {
		this.contestid = contestid;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getOption1() {
		return option1;
	}

	public void setOption1(String option1) {
		this.option1 = option1;
	}

	public String getOption2() {
		return option2;
	}

	public void setOption2(String option2) {
		this.option2 = option2;
	}

	public String getOption3() {
		return option3;
	}

	public void setOption3(String option3) {
		this.option3 = option3;
	}

	public String getOption4() {
		return option4;
	}

	public void setOption4(String option4) {
		this.option4 = option4;
	}

	public boolean isAnswerStatus() {
		return answerStatus;
	}

	public void setAnswerStatus(boolean answerStatus) {
		this.answerStatus = answerStatus;
	}

	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

	public int getQreviwStatusFlag() {
		return qreviwStatusFlag;
	}

	public void setQreviwStatusFlag(int qreviwStatusFlag) {
		this.qreviwStatusFlag = qreviwStatusFlag;
	}

	public String getCreateddate() {
		return createddate;
	}

	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}

	public int getBannerValue() {
		return bannerValue;
	}

	public void setBannerValue(int bannerValue) {
		this.bannerValue = bannerValue;
	}

	
	
}
