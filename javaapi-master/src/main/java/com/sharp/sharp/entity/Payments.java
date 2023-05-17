package com.sharp.sharp.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "payments")
public class Payments {
	@Id
	@GeneratedValue
	private Integer paymentid;
	private String transactionid;
	private String userid;
	// private String createdDtae;
	// private String description;
	private double amount;
	private double amount_paid;
	private String created_at;
	private double amount_due;
	private String currency;
	private String receipt;
	private String id;
	private String entity;
	private String status;
	private int attempts;
	private String updated_at;
	private String razorpay_payment_id;
	private String razorpay_order_id;
	private String razorpay_signature;
	private String reciever;
	private String transcationtype;
	@Column(name = "contestid")
	private String contestId;
	@Column(name = "withdraw_status")
	private String withdrawStatus;
	@Column(name = "transaction_number")
	private String transactionNumber;

	@Transient
	private BankAccounts accountDetails;
	
	public BankAccounts getAccountDetails() {
		return accountDetails;
	}

	public void setAccountDetails(BankAccounts accountDetails) {
		this.accountDetails = accountDetails;
	}

	public String getWithdrawStatus() {
		return withdrawStatus;
	}

	public void setWithdrawStatus(String withdrawStatus) {
		this.withdrawStatus = withdrawStatus;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	@Transient
	private String keyId;
	@Transient
	private String emailId;
	/*
	 * @Transient private String[] notes;
	 */
	@Transient
	private String notes;
	@Transient
	private String totalAmountWon;

	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount_paid() {
		return amount_paid;
	}

	public void setAmount_paid(double amount_paid) {
		this.amount_paid = amount_paid;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public double getAmount_due() {
		return amount_due;
	}

	public void setAmount_due(double amount_due) {
		this.amount_due = amount_due;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getRazorpay_payment_id() {
		return razorpay_payment_id;
	}

	public void setRazorpay_payment_id(String razorpay_payment_id) {
		this.razorpay_payment_id = razorpay_payment_id;
	}

	public String getRazorpay_order_id() {
		return razorpay_order_id;
	}

	public void setRazorpay_order_id(String razorpay_order_id) {
		this.razorpay_order_id = razorpay_order_id;
	}

	public String getRazorpay_signature() {
		return razorpay_signature;
	}

	public void setRazorpay_signature(String razorpay_signature) {
		this.razorpay_signature = razorpay_signature;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getReciever() {
		return reciever;
	}

	public void setReciever(String reciever) {
		this.reciever = reciever;
	}

	public String getTranscationtype() {
		return transcationtype;
	}

	public void setTranscationtype(String transcationtype) {
		this.transcationtype = transcationtype;
	}

	public String getContestId() {
		return contestId;
	}

	public void setContestId(String contestId) {
		this.contestId = contestId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getTotalAmountWon() {
		return totalAmountWon;
	}

	public void setTotalAmountWon(String totalAmountWon) {
		this.totalAmountWon = totalAmountWon;
	}

}
