package com.sharp.sharp.entity;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "OTPVALIDATION")
public class OTPValidation {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "mobilenumber")
	private String mobileNumber;

	@Column(name = "OTP")
	private String otp;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "SENDDATE")
	private Timestamp sendDate;

	@Column(name = "EXPIREDATE")
	private Timestamp expireDate;

	@Column(name = "OTPVALIDATIONSTATUS")
	private char otpValidationStatus = 'N';

	@Column(name = "OTPEXPIRESTATUS")
	private char otpExpireStatus = 'N';

	@Column(name = "CREATEDBY")
	private String createdBy = "USER";

	@Column(name = "CREATEDDATE")
	private Timestamp createdDate;

	@Transient
	private String filePath;

	@Transient
	private String fileName;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the otp
	 */
	public String getOtp() {
		return otp;
	}

	/**
	 * @param otp the otp to set
	 */
	public void setOtp(String otp) {
		this.otp = otp;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the sendDate
	 */
	public Timestamp getSendDate() {
		return sendDate;
	}

	/**
	 * @param sendDate the sendDate to set
	 */
	public void setSendDate(Timestamp sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * @return the expireDate
	 */
	public Timestamp getExpireDate() {
		return expireDate;
	}

	/**
	 * @param expireDate the expireDate to set
	 */
	public void setExpireDate(Timestamp expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * @return the otpValidationStatus
	 */
	public char getOtpValidationStatus() {
		return otpValidationStatus;
	}

	/**
	 * @param otpValidationStatus the otpValidationStatus to set
	 */
	public void setOtpValidationStatus(char otpValidationStatus) {
		this.otpValidationStatus = otpValidationStatus;
	}

	/**
	 * @return the otpExpireStatus
	 */
	public char getOtpExpireStatus() {
		return otpExpireStatus;
	}

	/**
	 * @param otpExpireStatus the otpExpireStatus to set
	 */
	public void setOtpExpireStatus(char otpExpireStatus) {
		this.otpExpireStatus = otpExpireStatus;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
