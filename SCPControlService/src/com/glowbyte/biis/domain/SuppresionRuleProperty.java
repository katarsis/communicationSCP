package com.glowbyte.biis.domain;

public class SuppresionRuleProperty {

	String histCampaign;
	String checkedCampaign;
	String countOfSuppresionDay;
	
	public String getHistCampaign() {
		return histCampaign;
	}
	public void setHistCampaign(String histCampaign) {
		this.histCampaign = histCampaign;
	}
	public String getCheckedCampaign() {
		return checkedCampaign;
	}
	public void setCheckedCampaign(String checkedCampaign) {
		this.checkedCampaign = checkedCampaign;
	}
	public String getCountOfSuppresionDay() {
		return countOfSuppresionDay;
	}
	public void setCountOfSuppresionDay(String countOfSuppresionDay) {
		this.countOfSuppresionDay = countOfSuppresionDay;
	}
	
	public SuppresionRuleProperty(String histCamp, String checkCamp, String countSuppressionDay){
		this.checkedCampaign = histCamp;
		this.checkedCampaign =checkCamp;
		this.countOfSuppresionDay = countSuppressionDay;
	}
	
}
