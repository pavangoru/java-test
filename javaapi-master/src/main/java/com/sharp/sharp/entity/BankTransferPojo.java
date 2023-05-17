package com.sharp.sharp.entity;

public class BankTransferPojo {
	private double amount;
	private String transferId;
	private String transferMode;
	private BeneInfo beneDetails;
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	public String getTransferMode() {
		return transferMode;
	}
	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}
	public BeneInfo getBeneDetails() {
		return beneDetails;
	}
	public void setBeneDetails(BeneInfo beneDetails) {
		this.beneDetails = beneDetails;
	}
	
}
