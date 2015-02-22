package com.vympelcom.biis.onlinecp.rules;

import java.util.List;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;

/*TODO: нружен ли он?*/

public class ContactPolicyRuleManager {

	/*TODO Нет.  На этом уровне уходит гибкость, работаем явно с тремя видами семейств правил*/
	
	/*В конструкторе: = new.... [3], и явно создать сами семьи, по конкретным именам классов*/
	private static RuleFamily ruleFamilyCampTypeSuppression;
	private static RuleFamily ruleFamilyMaxFrequency;
	private static RuleFamily ruleFamilyContactType;
	private static boolean isInit =false;
	
	/*
	 * конструктор закрытый для доступа 
	 */
	private ContactPolicyRuleManager(){
		
	}
	
	public ContactPolicyRuleManager getInstance() throws Exception{
		if(!isInit){
			synchronized (this) {
				if(!isInit){
					ruleFamilyCampTypeSuppression = new CampTypeSuppressionRuleFamily();
					ruleFamilyContactType = new CampTypeSuppressionRuleFamily();
					ruleFamilyMaxFrequency = new MaxFrequencyRuleFamily();
					isInit = true;
				}
			}
		}
		return this;
	}
	
	/*TODO: Implement*/
	public int getMaxSuppressionInterval()
	{
		return -1;
	}
	
	/*TODO: Implement*/
	public void initialize()
	{
		
	}
	
	
	
	public CPCheckResult checkContactPolicyAndStoreContact (String ctn, int campaignId, int channelId)
	{
		/*TODO
		 * 			* Определение интервала дат
		 * 			* Наша блокировка по клиенту
		 * 			* Подтягивание массива контактов
		 * 			* Применение трех правил по очереди
		 * 			* Создание нового контакта (как объекта ContactHistoryRecord !
		 * 			* Запись его через DAO
		 * 			* Снятие нашей блокировки
		 * */
		return null;
	}
}
