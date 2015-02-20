package com.vympelcom.biis.onlinecp.rules;

import java.util.Date;
import java.util.List;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;
import com.vympelcom.biis.onlinecp.utils.GeneralUtils;

public class MaxFrequencyRule {

	int campType;
	long countDayInPeriod;
	int maxFrequency;
	
	public long getCountDayInPeriod() {
		return countDayInPeriod;
	}
	public void setCountDayInPeriod(long countDayInPeriod) {
		this.countDayInPeriod = countDayInPeriod;
	}
	public int getMaxFrequency() {
		return maxFrequency;
	}
	public void setMaxFrequency(int maxFrequency) {
		this.maxFrequency = maxFrequency;
	}
	public int getCampType() {
		return campType;
	}
	public void setCampType(int campType) {
		this.campType = campType;
	}
	
	public MaxFrequencyRule(int campType, long countDayInPeriod,int maxFrequency ) {
		this.campType = campType;
		this.countDayInPeriod = countDayInPeriod;
		this.maxFrequency = maxFrequency;
	}
	
	/*
	 * Проверям по истории контактов не было ли превышено количество коммуникаций за данный период
	 */
	public CPCheckResult applyRule(List<ContactHistoryRecord> previsiosCommunication, Date currentDate){
		CPCheckResult result = new CPCheckResult(true);
		int localMaxFrequecy = maxFrequency;
		for(ContactHistoryRecord historyRecord: previsiosCommunication){
			if(historyRecord.getContactType() == campType){
				if(GeneralUtils.getDateDifferenceInDay(historyRecord.getContactDate(), currentDate)<countDayInPeriod)
				{
					if(localMaxFrequecy>0)
						maxFrequency --;
					else
					{
						result = new CPCheckResult(false);
						break;
					}
				}
			}
		}
		return result;
	}
}
