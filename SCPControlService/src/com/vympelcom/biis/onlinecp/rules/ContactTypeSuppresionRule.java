package com.vympelcom.biis.onlinecp.rules;

import java.util.Date;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.Campaign;
import com.vympelcom.biis.onlinecp.utils.GeneralUtils;

public class ContactTypeSuppresionRule {

	private int campType;
	private int communicationType;
	private int historyCampType;
	private long suppressionDay;
	private String baseDate;
	
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
	
	/**
	 * 
	 * @param campType
	 * @param historyCampType
	 * @param suppressionDay
	 * @param baseDate
	 * @param communicationType
	 */
	public ContactTypeSuppresionRule(int campType, int historyCampType, long suppressionDay, String baseDate, int communicationType){
		this.campType = campType;
		this.historyCampType = historyCampType;
		this.suppressionDay = suppressionDay;
		this.baseDate = baseDate;
		this.communicationType = communicationType;
	}
	
	public CPCheckResult applyRule(Campaign lastCompany,Date currentDate, Date lastContactDate){
		CPCheckResult result = new CPCheckResult(true);
		if(GeneralUtils.getDateDifferenceInDay(lastContactDate, currentDate)<this.suppressionDay)
			result = new CPCheckResult(false);
		return result;
	}
	
}
