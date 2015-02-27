package com.vympelcom.biis.onlinecp.rules;

import java.util.ArrayList;
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



public class ContactPolicyRuleManager {

	private static RuleFamily ruleFamilyCampTypeSuppression;
	private static RuleFamily ruleFamilyMaxFrequency;
	private static RuleFamily ruleFamilyContactType;
	
	private static List<RuleFamily> ruleFamilyList = new ArrayList<RuleFamily>();
	private static volatile ContactPolicyRuleManager ruleManager = null;
	
	static final Logger log = Logger.getLogger(ContactPolicyRuleManager.class);

	private ContactPolicyRuleManager(){
	}
	
	public static ContactPolicyRuleManager getInstance() throws Exception{
		ContactPolicyRuleManager localRuleManager = ruleManager;
		if(localRuleManager==null){
			synchronized (ContactPolicyRuleManager.class) {
				if(localRuleManager==null){
					log.info("Запуск инициализации правил контактной политики");
					localRuleManager = new ContactPolicyRuleManager();
					ruleFamilyCampTypeSuppression = new CampTypeSuppressionRuleFamily();
					ruleFamilyList.add(ruleFamilyCampTypeSuppression);
					ruleFamilyContactType = new ContactTypeSuppresionRuleFamily();
					ruleFamilyList.add(ruleFamilyContactType);
					ruleFamilyMaxFrequency = new MaxFrequencyRuleFamily();
					ruleFamilyList.add(ruleFamilyMaxFrequency);
					ruleManager = localRuleManager;
					log.info("Инициализация правил контактной политики завершена");
				}
			}
		}
		return ruleManager;
	}
	
	
	public CPCheckResult checkContactPolicyAndStoreContact (String ctn, int campaignId, int communicationType, Date currentDate)throws Exception
	{
		CPCheckResult resultApplyingAllRuleFamily = new CPCheckResult(true);
		ClientLockDescriptor clientLockDescriptor = null;
		try {
			clientLockDescriptor = ClientLockManager.GetClientLock(ctn);
			List<ContactHistoryRecord> previousContacts = ContactHistoryDAO.getHistoryByClient(ctn,currentDate);
			Campaign checkedCampaign = CampaignsDAO.getCampaignById(campaignId);
			for(RuleFamily selectedRule: ruleFamilyList){
				CPCheckResult resultApplyingRuleFamily = selectedRule.applyRuleFamily(ctn, checkedCampaign, previousContacts,currentDate);
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
			log.error("Контакт с параметрами : CTN:"+ctn+" camp_id:"+campaignId+" contact_type:"+communicationType+" ошибка обработки: "+e.getMessage());
			throw e;
		}finally{
			ClientLockManager.RemoveClientLock(clientLockDescriptor);
		}
		return resultApplyingAllRuleFamily;
	}
}
