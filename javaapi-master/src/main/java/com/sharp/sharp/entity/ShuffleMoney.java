package com.sharp.sharp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "shufflemoney")
public class ShuffleMoney {
	@Id
	@GeneratedValue
	@Column(name = "shuffleid")
	private int shuffleid;
	@Column(name = "lableid")
	private int lableid;

	@Column(name = "rankstart")
	private int rankstart;
	@Column(name = "rankend")
	private int rankend;
	@Column(name = "sharedamount")
	private long sharedamount;
	@Column(name = "percent")
	private double percent;
	@Column(name = "remaining")
	private long remaining;

	public int getShuffleid() {
		return shuffleid;
	}

	public void setShuffleid(int shuffleid) {
		this.shuffleid = shuffleid;
	}

	public int getLableid() {
		return lableid;
	}

	public void setLableid(int lableid) {
		this.lableid = lableid;
	}

	public int getRankstart() {
		return rankstart;
	}

	public void setRankstart(int rankstart) {
		this.rankstart = rankstart;
	}

	public int getRankend() {
		return rankend;
	}

	public void setRankend(int rankend) {
		this.rankend = rankend;
	}

	public long getSharedamount() {
		return sharedamount;
	}

	public void setSharedamount(long sharedamount) {
		this.sharedamount = sharedamount;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public long getRemaining() {
		return remaining;
	}

	public void setRemaining(long remaining) {
		this.remaining = remaining;
	}

}
