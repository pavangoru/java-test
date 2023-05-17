package com.sharp.sharp.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "category")
public class Category implements Serializable {

	@Id
	@GeneratedValue
	private int categoryid;

	private String categoryname;
	@Column(name = "categoryiconid")
	private long categoryiconId;

	private String categorydesc;
	@Column(name = "categoryimageurl")
	private String categoryImageUrl;
	private Timestamp createddate;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "categoryid")

	// @JoinColumn(name = "categoryid", referencedColumnName = "categoryidref")
	private List<Subcategory> subcategory;
	

	public int getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public String getCategorydesc() {
		return categorydesc;
	}

	public void setCategorydesc(String categorydesc) {
		this.categorydesc = categorydesc;
	}

	public Timestamp getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Timestamp createddate) {
		this.createddate = createddate;
	}

	public List<Subcategory> getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(List<Subcategory> subcategory) {
		this.subcategory = subcategory;
	}

	public long getCategoryiconId() {
		return categoryiconId;
	}

	public void setCategoryiconId(long categoryiconId) {
		this.categoryiconId = categoryiconId;
	}

	

	public String getCategoryImageUrl() {
		return categoryImageUrl;
	}

	public void setCategoryImageUrl(String categoryImageUrl) {
		this.categoryImageUrl = categoryImageUrl;
	}

	
}
