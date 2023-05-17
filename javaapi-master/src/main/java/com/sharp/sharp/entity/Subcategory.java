package com.sharp.sharp.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name = "subcategory")
public class Subcategory implements Serializable {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String subcategoryid;

	private String subcategoryname;

	private String subcategorydesc;
	private Timestamp createddate;

	/*
	 * @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 * 
	 * // @JoinColumn(name = "categoryid")
	 * 
	 * // @JoinColumn(name = "categoryid", referencedColumnName = "categoryid")
	 * private Category categoryId;
	 */

	public String getSubcategoryid() {
		return subcategoryid;
	}

	public void setSubcategoryid(String subcategoryid) {
		this.subcategoryid = subcategoryid;
	}

	public String getSubcategoryname() {
		return subcategoryname;
	}

	public void setSubcategoryname(String subcategoryname) {
		this.subcategoryname = subcategoryname;
	}

	public String getSubcategorydesc() {
		return subcategorydesc;
	}

	public void setSubcategorydesc(String subcategorydesc) {
		this.subcategorydesc = subcategorydesc;
	}

	public Timestamp getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Timestamp createddate) {
		this.createddate = createddate;
	}

	/*
	 * public Category getCategoryId() { return categoryId; }
	 * 
	 * public void setCategoryId(Category categoryId) { this.categoryId =
	 * categoryId; }
	 */

}
