package com.vympelcom.biis.onlinecp.rules;

import java.util.Date;

import org.apache.log4j.Logger;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.Campaign;
import com.vympelcom.biis.onlinecp.utils.GeneralUtils;

public class ContactTypeSuppresionRule {

	private int campType;
	private int communicationType;
	private int historyCampType;
	private long suppressionDay;
	private String baseDate;
	
	static final Logger log = Logger.getLogger(ContactTypeSuppresionRule.class);
	
	public int getCommunicationType() {
		return communicationType;
	}
	public void setCommunicationType(int communicationType) {
		this.communicationType = communicationType;
	}
	
	public int getCampType() {
		return campType;
	}
	public void setCampType(int campType) {
		this.campType = campType;
	}
	public int getHistoryCampType() {
		return historyCampType;
	}
	public void setHistoryCampType(int historyCampType) {
		this.historyCampType = historyCampType;
	}
	public long getSuppressionDay() {
		return suppressionDay;
	}
	public void setSuppressionDay(long suppressionDay) {
		this.suppressionDay = suppressionDay;
	}
	public String getBaseDate() {
		return baseDate;
	}
	public void setBaseDate(String baseDate) {
		this.baseDate = baseDate;
	} 
	
	public ContactTypeSuppresionRule(int campType, int historyCampType, long suppressionDay, String baseDate, int communicationType){
		this.campType = campType;
		this.historyCampType = historyCampType;
		this.suppressionDay = suppressionDay;
		this.baseDate = baseDate;
		this.communicationType = communicationType;
	}
	
	public CPCheckResult applyRule(Campaign lastCompany,Date currentDate, Date lastContactDate){
		CPCheckResult result = new CPCheckResult(true);
		log.trace("Применяем правило ContactTypeSuppresionRule "+ toString());
		if(baseDate.equals("offer_date"))
			if(GeneralUtils.getDateDifferenceInDay(lastCompany.getOfferDate(), currentDate)<this.suppressionDay)
				result = new CPCheckResult(false);
		else if(baseDate.equals("inform_date"))
			if(GeneralUtils.getDateDifferenceInDay(lastCompany.getInformDate(), currentDate)<this.suppressionDay)
				result = new CPCheckResult(false);
		log.trace("Результат применения правила ContactTypeSuppresionRule: "+result);
		return result;
	}
	
	@Override
	public String toString() {
		return "ContactTypeSuppresionRule [campType=" + campType
				+ ", communicationType=" + communicationType
				+ ", historyCampType=" + historyCampType + ", suppressionDay="
				+ suppressionDay + ", baseDate=" + baseDate + "]";
	}
	
}
