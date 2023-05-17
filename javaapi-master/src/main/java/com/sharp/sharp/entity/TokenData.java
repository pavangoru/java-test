package com.sharp.sharp.entity;

public class TokenData {

	private String status;
	private String subCode;
	private String message;
	private GenData data;
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
	public GenData getData() {
		return data;
	}
	public void setData(GenData data) {
		this.data = data;
	}
	
}

