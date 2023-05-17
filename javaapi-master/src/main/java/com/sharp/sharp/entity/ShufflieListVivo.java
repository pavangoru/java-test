package com.sharp.sharp.entity;

import java.math.BigInteger;
import java.util.LinkedList;

public class ShufflieListVivo {
	private String lablename;
	private BigInteger poolamount;
	private int totalplayers;
	private long totalamount;
	private long gameamount;
	
	private LinkedList<ShuffleMoney> listvivo;

	
	public String getLablename() {
		return lablename;
	}

	public void setLablename(String lablename) {
		this.lablename = lablename;
	}

	

	public int getTotalplayers() {
		return totalplayers;
	}

	public void setTotalplayers(int totalplayers) {
		this.totalplayers = totalplayers;
	}

	public long getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(long totalamount) {
		this.totalamount = totalamount;
	}

	public long getGameamount() {
		return gameamount;
	}

	public void setGameamount(long gameamount) {
		this.gameamount = gameamount;
	}

	public LinkedList<ShuffleMoney> getListvivo() {
		return listvivo;
	}

	public void setListvivo(LinkedList<ShuffleMoney> listvivo) {
		this.listvivo = listvivo;
	}

	public BigInteger getPoolamount() {
		return poolamount;
	}

	public void setPoolamount(BigInteger poolamount) {
		this.poolamount = poolamount;
	}

	
	

}
