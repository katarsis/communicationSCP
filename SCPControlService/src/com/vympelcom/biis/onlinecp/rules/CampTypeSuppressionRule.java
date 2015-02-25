package com.vympelcom.biis.onlinecp.rules;

import java.util.Date;

import org.apache.log4j.Logger;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.utils.GeneralUtils;

public class CampTypeSuppressionRule {

	static final Logger log = Logger.getLogger(CampTypeSuppressionRule.class);

	private String histCampaign;
	private String checkedCampaign;
	private Long countOfSuppresionDay;
	
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
	public Long getCountOfSuppresionDay() {
		return countOfSuppresionDay;
	}
	public void setCountOfSuppresionDay(Long countOfSuppresionDay) {
		this.countOfSuppresionDay = countOfSuppresionDay;
	}
	
	public CampTypeSuppressionRule(String histCamp, String checkCamp, Long countOfSuppresionDay){
		this.histCampaign = histCamp;
		this.checkedCampaign =checkCamp;
		this.countOfSuppresionDay= countOfSuppresionDay;
	}
	
	public CPCheckResult applyRule(Date historyDate, Date currentDate)
	{
		log.trace("Приеменяем правило CampTypeSuppressionRule "+toString());
		CPCheckResult result = new CPCheckResult(true);
		long factPeriodIndays = GeneralUtils.getDateDifferenceInDay(historyDate, currentDate);
		if(countOfSuppresionDay>factPeriodIndays)
		{
			result = new CPCheckResult(false);
		}
		log.trace("Приеменяем правило CampTypeSuppressionRule "+toString());
		return result;
	}
	
	@Override
	public String toString() {
		return "CampTypeSuppressionRule [histCampaign=" + histCampaign
				+ ", checkedCampaign=" + checkedCampaign
				+ ", countOfSuppresionDay=" + countOfSuppresionDay + "]";
	}

	
}
