package com.sharp.sharp.entity;

public class RankVivo {

	private int rank;
	private double sharedAmount;
	private double percent;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public double getSharedAmount() {
		return sharedAmount;
	}

	public void setSharedAmount(double sharedAmount) {
		this.sharedAmount = sharedAmount;
	}
	

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	
	public RankVivo(int rank, double sharedAmount, double percent) {
		super();
		this.rank = rank;
		this.sharedAmount = sharedAmount;
		this.percent = percent;
	}

	public RankVivo(int rank, long sharedAmount) {
		super();
		this.rank = rank;
		this.sharedAmount = sharedAmount;
	}

	public RankVivo() {

	}

}
