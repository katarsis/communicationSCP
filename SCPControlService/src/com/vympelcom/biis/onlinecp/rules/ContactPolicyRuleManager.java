package com.vympelcom.biis.onlinecp.rules;

import java.util.List;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;

/*TODO: нружен ли он?*/

public class ContactPolicyRuleManager {

	/*TODO Нет.  На этом уровне уходит гибкость, работаем явно с тремя видами семейств правил*/
	
	/*В конструкторе: = new.... [3], и явно создать сами семьи, по конкретным именам классов*/
	private RuleFamily ruleFamilyCampTypeSuppression;
	private RuleFamily ruleFamilyMaxFrequency;
	private RuleFamily ruleFamilyContactType;
	
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
