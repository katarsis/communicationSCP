package com.vympelcom.biis.onlinecp.rules;

import java.util.Date;
import java.util.List;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;
import com.vympelcom.biis.onlinecp.utils.GeneralUtils;

public class MaxFrequencyRule {

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
		
		//TODO логировать на trace-уровне. Применяем правило такое-то (параметры правила), Contact_id такой-то. Результат такой-то. 
		
		CPCheckResult result = new CPCheckResult(true);
		
		/*TODO заменить на переменную "количество контактов", которую инкрементировать*/
		int localMaxFrequecy = maxFrequency;
		
		for(ContactHistoryRecord historyRecord: previsiosCommunication){
			if(Long.compare(GeneralUtils.getDateDifferenceInDay(historyRecord.getContactDate(), currentDate),countDayInPeriod)<=0)
			{
					if(localMaxFrequecy>0)
						localMaxFrequecy --;
					else
					{
						result = new CPCheckResult(false);
						break;
					}
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "MaxFrequencyRule [countDayInPeriod=" + countDayInPeriod + ", maxFrequency=" + maxFrequency + "]";
	}
}
