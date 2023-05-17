package com.sharp.sharp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "wallet")
public class Sharp6Wallet {

	@Id
	@Column(name = "userid")
	private String userid;
	@Column(name = "mainkales")
	private double Mainkales;
	/*
	 * @Column(name = "addmoney") private double Addmoney;
	 */

	/*
	 * @Column(name = "transfer") private double transfer;
	 */
	@Column(name = "withdrawl")
	private double withdrawl;
	@Column(name = "withdrwaltype")
	private String withdrwaltype;

	@Transient
	private String transactionid;

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

	/*
	 * public double getAddmoney() { return Addmoney; }
	 * 
	 * public void setAddmoney(double addmoney) { Addmoney = addmoney; }
	 */

	/*
	 * public double getTransfer() { return transfer; }
	 * 
	 * public void setTransfer(double transfer) { this.transfer = transfer; }
	 */

	public double getMainkales() {
		return Mainkales;
	}

	public void setMainkales(double mainkales) {
		Mainkales = mainkales;
	}

	public double getWithdrawl() {
		return withdrawl;
	}

	public void setWithdrawl(double withdrawl) {
		this.withdrawl = withdrawl;
	}

	public String getWithdrwaltype() {
		return withdrwaltype;
	}

	public void setWithdrwaltype(String withdrwaltype) {
		this.withdrwaltype = withdrwaltype;
	}

}
