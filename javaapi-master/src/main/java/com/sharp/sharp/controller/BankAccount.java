package com.sharp.sharp.controller;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "bankaccount")
public class BankAccount {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	@Column(name = "beneid")
	private String beneId;
	@Column(name = "userid")
	private String userid;
	@Column(name = "accountnumber")
	private String accountNumber;
	@Column(name = "bankname")
	private String bankName;
	@Transient
	private String reconfirmAccountNumber;
	@Column(name = "ifsc")
	private String ifsc;
	@Column(name = "validationflag")
	private int validationFlag;

	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "phone")
	private String phone;
	@Column(name = "bankaccount")
	private String bankAccount;
	/*
	 * @Column(name = "ifsc") private String ifsc;
	 */
	@Column(name = "address1")
	private String address1;
	@Column(name = "city")
	private String city;
	@Column(name = "state")
	private String state;
	@Column(name = "pincode")
	private String pincode;
	@Transient
	private String response;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getReconfirmAccountNumber() {
		return reconfirmAccountNumber;
	}

	public void setReconfirmAccountNumber(String reconfirmAccountNumber) {
		this.reconfirmAccountNumber = reconfirmAccountNumber;
	}

	

	public int getValidationFlag() {
		return validationFlag;
	}

	public void setValidationFlag(int validationFlag) {
		this.validationFlag = validationFlag;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBeneId() {
		return beneId;
	}

	public void setBeneId(String beneId) {
		this.beneId = beneId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
