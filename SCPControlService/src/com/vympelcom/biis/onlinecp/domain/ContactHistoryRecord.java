package com.vympelcom.biis.onlinecp.domain;

import java.text.ParseException;
import java.util.Date;

import com.vympelcom.biis.onlinecp.utils.GeneralUtils;

public class ContactHistoryRecord {

	String CTN;
	Date contactDate;
	int contactType;
	String contactSource;
	int id;
	int campaignId;
	
	public int getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(int campignId) {
		this.campaignId = campignId;
	}
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
	
	public ContactHistoryRecord (String ctn, Date date, String contactType, String contactSource, String id, int campId) throws ParseException{
		this.CTN = ctn;
		this.contactDate = date;
		this.contactType = Integer.valueOf(contactType);
		this.contactSource = contactSource;
		this.id = Integer.valueOf(id);
		this.campaignId = campId;
	}
	
}
