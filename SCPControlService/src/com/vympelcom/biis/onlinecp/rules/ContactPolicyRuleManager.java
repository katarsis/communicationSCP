package com.vympelcom.biis.onlinecp.rules;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.vympelcom.biis.onlinecp.dao.CampaignsDAO;
import com.vympelcom.biis.onlinecp.dao.ContactHistoryDAO;
import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.Campaign;
import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;
import com.vympelcom.biis.onlinecp.utils.ClientLockDescriptor;
import com.vympelcom.biis.onlinecp.utils.ClientLockManager;

/*TODO: нружен ли он?*/

public class ContactPolicyRuleManager {

	/*TODO Нет.  На этом уровне уходит гибкость, работаем явно с тремя видами семейств правил*/
	
	/*В конструкторе: = new.... [3], и явно создать сами семьи, по конкретным именам классов*/
	private static RuleFamily ruleFamilyCampTypeSuppression;
	private static RuleFamily ruleFamilyMaxFrequency;
	private static RuleFamily ruleFamilyContactType;
	
	private static List<RuleFamily> ruleFamilyList = new ArrayList<RuleFamily>();
	private static volatile ContactPolicyRuleManager ruleManager = null;
	
	static final Logger log = Logger.getLogger(ContactPolicyRuleManager.class);

	private ContactPolicyRuleManager(){
	}
	
	public static ContactPolicyRuleManager getInstance() throws Exception{
		log.info("Запуск инициализации правил контатконой политики");
		if(ruleManager==null){
			synchronized (ContactPolicyRuleManager.class) {
				if(ruleManager==null){
					ruleManager = new ContactPolicyRuleManager();
					ruleFamilyCampTypeSuppression = new CampTypeSuppressionRuleFamily();
					ruleFamilyList.add(ruleFamilyCampTypeSuppression);
					ruleFamilyContactType = new ContactTypeSuppresionRuleFamily();
					ruleFamilyList.add(ruleFamilyContactType);
					ruleFamilyMaxFrequency = new MaxFrequencyRuleFamily();
					ruleFamilyList.add(ruleFamilyMaxFrequency);
						
				}
			}
		}
		log.info("Инициализация правил контактной политики завершена");
		return ruleManager;
	}
	
	
	public CPCheckResult checkContactPolicyAndStoreContact (String ctn, int campaignId, int communicationType)
	{
		Date currentDate =  new Date();
		CPCheckResult resultApplyingAllRuleFamily = new CPCheckResult(true);
		ClientLockDescriptor clientLockDescriptor = null;
		try {
			clientLockDescriptor = ClientLockManager.GetClientLock(ctn);
			List<ContactHistoryRecord> previousContacts = ContactHistoryDAO.getHistoryByClient(ctn);
			Campaign checkedCampaign = CampaignsDAO.getCampaignById(campaignId);
			for(RuleFamily selectedRule: ruleFamilyList){
				CPCheckResult resultApplyingRuleFamily = selectedRule.applyRuleFamily(ctn, checkedCampaign, previousContacts);
				if(!resultApplyingRuleFamily.isContactAllowed())
				{
					resultApplyingAllRuleFamily = resultApplyingRuleFamily;
					break;
				}
			}
			if(resultApplyingAllRuleFamily.isContactAllowed()&&checkedCampaign!=null)
			{
				ContactHistoryRecord newCommunicationRecord = new ContactHistoryRecord(ctn, currentDate, String.valueOf(communicationType), "Online EPK", "0", checkedCampaign.getId(),0);
				ContactHistoryDAO.writeRecordToContactHistory(newCommunicationRecord);
			}
		} catch (Exception e) {
			log.error("Request with parameters: CTN:"+ctn+" camp_id:"+campaignId+" contact_type:"+communicationType+" is aborted: "+e.getMessage());
		}finally{
			ClientLockManager.RemoveClientLock(clientLockDescriptor);
		}
		return resultApplyingAllRuleFamily;
	}
}
