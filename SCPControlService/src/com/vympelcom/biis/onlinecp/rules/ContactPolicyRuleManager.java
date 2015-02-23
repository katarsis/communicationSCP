package com.vympelcom.biis.onlinecp.rules;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.vympelcom.biis.onlinecp.dao.CampaignsDAO;
import com.vympelcom.biis.onlinecp.dao.ContactHistoryDAO;
import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.Campaign;
import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;

/*TODO: нружен ли он?*/

public class ContactPolicyRuleManager {

	/*TODO Нет.  На этом уровне уходит гибкость, работаем явно с тремя видами семейств правил*/
	
	/*В конструкторе: = new.... [3], и явно создать сами семьи, по конкретным именам классов*/
	private static RuleFamily ruleFamilyCampTypeSuppression;
	private static RuleFamily ruleFamilyMaxFrequency;
	private static RuleFamily ruleFamilyContactType;
	private static boolean isInit =false;
	private static List<RuleFamily> ruleFamilyList = new ArrayList<RuleFamily>();
	private static ContactPolicyRuleManager ruleManager = null;
	
	/*
	 * конструктор закрытый для доступа 
	 */
	private ContactPolicyRuleManager(){
		
	}
	
	public static ContactPolicyRuleManager getInstance() throws Exception{
		if(ruleManager==null){
			synchronized (ContactPolicyRuleManager.class) {
				if(ruleManager==null){
					ruleManager = new ContactPolicyRuleManager();
					ruleFamilyCampTypeSuppression = new CampTypeSuppressionRuleFamily();
					ruleFamilyList.add(ruleFamilyCampTypeSuppression);
					ruleFamilyContactType = new CampTypeSuppressionRuleFamily();
					ruleFamilyList.add(ruleFamilyContactType);
					ruleFamilyMaxFrequency = new MaxFrequencyRuleFamily();
					ruleFamilyList.add(ruleFamilyMaxFrequency);
					isInit = true;	
				}
			}
		}
		return ruleManager;
	}
	
	/*TODO: Implement*/
	public long getMaxSuppressionInterval()
	{
		long result =0;
		for(RuleFamily ruleFamily: ruleFamilyList){
			if(result<ruleFamily.getMaxDayInterval())
				result = ruleFamily.getMaxDayInterval();
		}
		return result;
	}
	
	/*TODO: Implement*/
	public void initialize()
	{
		
	}
	
	
	
	public CPCheckResult checkContactPolicyAndStoreContact (String ctn, int campaignId, int channelId)
	{
		Date currentDate =  new Date();
		Calendar oldDateCalendar = Calendar.getInstance();
		oldDateCalendar.setTime(currentDate);
		oldDateCalendar.add(Calendar.DATE, -1*Long.valueOf(this.getMaxSuppressionInterval()).intValue());
		Date oldDate = oldDateCalendar.getTime();
		CPCheckResult result = new CPCheckResult(true);
		try {
			List<ContactHistoryRecord> previousContacts = ContactHistoryDAO.getHistoryByClientAndTimeRange(ctn, currentDate, oldDate);
			Campaign checkedCampaign = CampaignsDAO.getCampaignById(campaignId);
			for(RuleFamily selectedRule: ruleFamilyList){
				CPCheckResult localResult = selectedRule.applyRuleFamily(ctn, checkedCampaign, previousContacts);
				if(!localResult.isContactAllowed())
				{
					result = localResult;
					break;
				}
			}
			if(result.isContactAllowed())
			{
				ContactHistoryRecord newCommunicationSaved = new ContactHistoryRecord(ctn, currentDate, String.valueOf(checkedCampaign.getCampaignType()), "Online EPK", "", checkedCampaign.getId());
				ContactHistoryDAO.writeRecordToContactHistory(newCommunicationSaved);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*TODO
		 * 			* Определение интервала дат
		 * 			* Наша блокировка по клиенту
		 * 			* Подтягивание массива контактов
		 * 			* Применение трех правил по очереди
		 * 			* Создание нового контакта (как объекта ContactHistoryRecord !
		 * 			* Запись его через DAO
		 * 			* Снятие нашей блокировки
		 * */
		return result;
	}
}
