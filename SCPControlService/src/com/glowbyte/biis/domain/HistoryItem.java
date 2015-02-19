package com.glowbyte.biis.domain;

import java.util.Date;

public class HistoryItem {

	String CTN;
	Date contactDate;
	int contactType;
	String contactSource;
	int id;
	
	public String getCTN() {
		return CTN;
	}
	public void setCTN(String cTN) {
		CTN = cTN;
	}
	public Date getContactDate() {
		return contactDate;
	}
	public void setContactDate(Date contactDate) {
		this.contactDate = contactDate;
	}
	public int getContactType() {
		return contactType;
	}
	public void setContactType(int contactType) {
		this.contactType = contactType;
	}
	public String getContactSource() {
		return contactSource;
	}
	public void setContactSource(String contactSource) {
		this.contactSource = contactSource;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
