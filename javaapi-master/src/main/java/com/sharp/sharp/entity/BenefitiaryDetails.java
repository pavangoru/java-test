package com.sharp.sharp.entity;

public class BenefitiaryDetails {

	private String status;
	private String subCode;
	private String message;
	private BenInfo data;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSubCode() {
		return subCode;
	}
	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BenInfo getData() {
		return data;
	}
	public void setData(BenInfo data) {
		this.data = data;
	}
	
}
