package com.sharp.sharp.entity;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ContestJoined")
public class ContestJoined {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	@Column(name = "lableid")
	private int lableid;

	@Column(name = "joinedstatus")
	private int joinedstatus;
	@Column(name = "Contestid")
	private String Contestid;
	@Column(name = "userid")
	private String userid;
	@Transient
	private BigInteger size;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLableid() {
		return lableid;
	}

	public void setLableid(int lableid) {
		this.lableid = lableid;
	}

	public int getJoinedstatus() {
		return joinedstatus;
	}

	public void setJoinedstatus(int joinedstatus) {
		this.joinedstatus = joinedstatus;
	}

	public String getContestid() {
		return Contestid;
	}

	public void setContestid(String contestid) {
		Contestid = contestid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public BigInteger getSize() {
		return size;
	}

	public void setSize(BigInteger size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "ContestJoined [id=" + id + ", lableid=" + lableid + ", joinedstatus=" + joinedstatus + ", Contestid="
				+ Contestid + ", userid=" + userid + ", size=" + size + "]";
	}

	

	
	

}
