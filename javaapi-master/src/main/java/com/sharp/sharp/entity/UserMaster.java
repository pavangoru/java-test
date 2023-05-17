package com.sharp.sharp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "users")
public class UserMaster {
	@Id
   // @GeneratedValue(strategy = GenerationType.AUTO)
	private String userid;
	@Column(name = "user_name")
	private String userName;
	// private String userfname;
	// private String userlname;
	private String password;
	@Transient
	private String confirmPassword;

	private String mobileNumber;
	private int age;
	private String address;
	private String language;
	@Column(name = "gender")
	private String gender;
	@Column(name = "email_id")
	private String emailId;
	// private String country;

	private String dob;
	private String role;
	@Column(name = "mainkales")
	private Double mainkales;
	private boolean activestatus;
	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "user_dvice_tkn")
	private String userDeviceToken;
	@Transient
	private int rankflag;
	@Column(name="admin_flag")
	private String userflag;
	public int getRankflag() {
		return rankflag;
	}

	public void setRankflag(int rankflag) {
		this.rankflag = rankflag;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isActivestatus() {
		return activestatus;
	}

	public void setActivestatus(boolean activestatus) {
		this.activestatus = activestatus;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Double getMainkales() {
		return mainkales;
	}

	public void setMainkales(Double mainkales) {
		this.mainkales = mainkales;
	}

	public String getUserDeviceToken() {
		return userDeviceToken;
	}

	public void setUserDeviceToken(String userDeviceToken) {
		this.userDeviceToken = userDeviceToken;
	}

	public String getUserflag() {
		return userflag;
	}

	public void setUserflag(String userflag) {
		this.userflag = userflag;
	}

	
	
}
