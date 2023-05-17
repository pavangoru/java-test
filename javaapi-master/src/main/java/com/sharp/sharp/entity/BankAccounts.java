package com.sharp.sharp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "bank_Accounts")
public class BankAccounts {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
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

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getReconfirmAccountNumber() {
		return reconfirmAccountNumber;
	}

	public void setReconfirmAccountNumber(String reconfirmAccountNumber) {
		this.reconfirmAccountNumber = reconfirmAccountNumber;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public int getValidationFlag() {
		return validationFlag;
	}

	public void setValidationFlag(int validationFlag) {
		this.validationFlag = validationFlag;
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

}
