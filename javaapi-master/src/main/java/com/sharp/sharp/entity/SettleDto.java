package com.sharp.sharp.entity;

import java.io.Serializable;

public class SettleDto implements Serializable {

	private String userid;

	private Integer paymentid;
	private String amount;
	private BankAccounts bankAccount;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getPaymentid() {
		return paymentid;
	}

	public void setPaymentid(Integer paymentid) {
		this.paymentid = paymentid;
	}

	public BankAccounts getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccounts bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
