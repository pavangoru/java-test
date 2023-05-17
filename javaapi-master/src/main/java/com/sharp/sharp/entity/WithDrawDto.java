package com.sharp.sharp.entity;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class WithDrawDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotBlank
	@NotNull(message = "user id is required")
	@Pattern(regexp = "^[6789]\\d{9}$",message = "please provide valid userId")
	private String userID;
	@Nonnull
	@Min(10)
   	private Double amount;
   	
	private String transactionNumber;
	@Nonnull
	@NotBlank
	private String accountNumber;
	@Nonnull
	@NotBlank(message = "bank name is required")
	private String bankName;
	@Email(message = "please provide correct email address")
	@Nonnull
	@NotBlank(message = "email  is required")
	private String email;
	@Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$",message = "please provide valid ifsc code")
	@Nonnull
	@NotBlank(message = "ifsc code  is required")
	private String ifsc;
	@Nonnull
	@NotBlank(message = " name is required")
	private String name;
	@Pattern(regexp = "^[6789]\\d{9}$",message = "please provide valid phone number")
	@Nonnull
	private String phone;
	
	private String reconfirmAccountNumber;
	
	

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReconfirmAccountNumber() {
		return reconfirmAccountNumber;
	}

	public void setReconfirmAccountNumber(String reconfirmAccountNumber) {
		this.reconfirmAccountNumber = reconfirmAccountNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
