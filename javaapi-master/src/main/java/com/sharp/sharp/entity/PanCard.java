package com.sharp.sharp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pancard")
public class PanCard {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "userid")
	private String userid;
	@Column(name = "pannumber")
	private String panNumber;
	@Column(name = "fullname")
	private String fullName;
	@Column(name = "province")
	private String province;
	@Column(name = "panimage")
	private String panIMage;
	@Column(name = "validationflag")
	private int validationFlag;

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPanIMage() {
		return panIMage;
	}

	public void setPanIMage(String panIMage) {
		this.panIMage = panIMage;
	}

	public int getValidationFlag() {
		return validationFlag;
	}

	public void setValidationFlag(int validationFlag) {
		this.validationFlag = validationFlag;
	}

}
