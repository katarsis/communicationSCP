package com.vympelcom.biis.onlinecp.rules;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;
import com.vympelcom.biis.onlinecp.utils.GeneralUtils;

public class MaxFrequencyRule {

	static final Logger log = Logger.getLogger(MaxFrequencyRule.class);
	
	private long countDayInPeriod;
	
	private int maxFrequency;
	
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
	
	public MaxFrequencyRule(long countDayInPeriod,int maxFrequency ) {
		this.countDayInPeriod = countDayInPeriod;
		this.maxFrequency = maxFrequency;
	}
	
	/*
	 * Проверям по истории контактов не было ли превышено количество коммуникаций за данный период
	 */
	public CPCheckResult applyRule(List<ContactHistoryRecord> previsiosCommunication, Date currentDate){
		CPCheckResult result = new CPCheckResult(true);
		log.trace("Применяем правило контактной политики MaxFrequencyRule " + toString());
		int countContactInPeriod = 0;
		
		for(ContactHistoryRecord historyRecord: previsiosCommunication){
			if(Long.compare(GeneralUtils.getDateDifferenceInDay(historyRecord.getContactDate(), currentDate),countDayInPeriod)<=0)
			{
				countContactInPeriod ++;
			}
		}
		
		if(countContactInPeriod>maxFrequency)
			result.setContactAllowed(false);
		log.trace("Результат применения правила контактной политики MaxFrequencyRule " + result);
		return result;
	}
	
	@Override
	public String toString() {
		return "MaxFrequencyRule [countDayInPeriod=" + countDayInPeriod + ", maxFrequency=" + maxFrequency + "]";
	}
}
