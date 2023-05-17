package com.sharp.sharp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Language")
public class Language {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "languageid", nullable = false)
	private int id;

	@Column(name = "languagename")
	private String languageName;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "languagecode")
	private String languagecode;

	@Column(name = "createddate")
	private String createddate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}