package com.vympelcom.biis.onlinecp.rules;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.Campaign;

public class VoiceChannelSuppresionRule {

	int campType;
	int historyCampType;
	long suppressionDay;
	String baseDate;
	
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
	
	public VoiceChannelSuppresionRule(int campType, int historyCampType, long suppressionDay, String baseDate){
		this.campType = campType;
		this.historyCampType = historyCampType;
		this.suppressionDay = suppressionDay;
		this.baseDate = baseDate;
	}
	
	public CPCheckResult applyRule(Campaign lastCompany){
		CPCheckResult result = new CPCheckResult(true);
		return result;
	}
	
}
