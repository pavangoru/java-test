package com.sharp.sharp.entity;

import java.util.Date;

import com.google.cloud.Timestamp;

public class BenInfo {

	private String beneId;
	private String name;
	private String email;
	private String phone;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String pincode;
	private String bankAccount;
	private String ifsc;
	private String status;
	private String maskedCard;
	private String vpa;
	private String addedOn;
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
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
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
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
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
	
	public String getMaskedCard() {
		return maskedCard;
	}
	public void setMaskedCard(String maskedCard) {
		this.maskedCard = maskedCard;
	}
	public String getVpa() {
		return vpa;
	}
	public void setVpa(String vpa) {
		this.vpa = vpa;
	}
	public String getAddedOn() {
		return addedOn;
	}
	public void setAddedOn(String addedOn) {
		this.addedOn = addedOn;
	}
	
	
	
}
