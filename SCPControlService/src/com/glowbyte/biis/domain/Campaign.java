package com.glowbyte.biis.domain;

import java.text.ParseException;
import java.util.Date;

import com.glowbyte.biis.utils.GeneralUtils;

public class Campaign {
	int id;
	int campaignType;
	Date informDate;
	Date offerDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCampaignType() {
		return campaignType;
	}
	public void setCampaignType(int campaignType) {
		this.campaignType = campaignType;
	}
	public Date getInformDate() {
		return informDate;
	}
	public void setInformDate(Date informDate) {
		this.informDate = informDate;
	}
	public Date getOfferDate() {
		return offerDate;
	}
	public void setOfferDate(Date offerDate) {
		this.offerDate = offerDate;
	}
	
	public Campaign(String id, String campaignType,String informDate,String offerDate) throws ParseException
	{
		this.id =  Integer.valueOf(id);
		this.campaignType = Integer.valueOf(campaignType);
		this.informDate = GeneralUtils.getDateFromString(informDate);
		this.offerDate = GeneralUtils.getDateFromString(offerDate);
	}

}