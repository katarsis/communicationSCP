package com.vympelcom.biis.onlinecp.rules;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.vympelcom.biis.onlinecp.dao.CampaignsDAO;
import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.Campaign;
import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;
import com.vympelcom.biis.onlinecp.utils.OnlineCPDatabaseConnection;

public class ContactTypeSuppresionRuleFamily implements RuleFamily{

	static final int VOICE_CONTACT_TYPE =1; 
	
	static final Logger log = Logger.getLogger(ContactTypeSuppresionRuleFamily.class);
	
	private static volatile HashMap<String,ContactTypeSuppresionRule> contactTypeRuleMatrix = null;
	
	private HashMap<String,ContactTypeSuppresionRule> generateRuleMatrix() throws Exception{
		HashMap<String, ContactTypeSuppresionRule> result = new HashMap<String, ContactTypeSuppresionRule>();
		OnlineCPDatabaseConnection databaseConnection = OnlineCPDatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("select * from CP_RULE_3_OFFER_SUPPR_MATRIX ");
			ResultSet rs = callstmt.executeQuery();
			
			while (rs.next()) {
				ContactTypeSuppresionRule  currentItem = new ContactTypeSuppresionRule(rs.getInt("CHECK_CAMPAIGN_TYPE"), rs.getInt("HISTORY_CAMPAIGN_TYPE"), rs.getLong("SUPPRESSION_INTERVAL_DAYS"), rs.getString("SUPPRESION_BASE_DATE"), rs.getInt("CONTACT_TYPE"));  
				String matrixKey = String.valueOf(currentItem.getCampType())+ String.valueOf(currentItem.getHistoryCampType());
				result.put(matrixKey,currentItem);
				log.info("Загружено правило ContactTypeSuppresionRule :"+currentItem.toString());
			}
		}catch (Exception e) {
			log.fatal("Не возможно загрузить правила ContactTypeSuppresionRuleFamily:" + e.getMessage());
			throw e;
		}finally{
			connection.close();
		}
		return result;
	}
	
	public ContactTypeSuppresionRuleFamily() throws Exception{
		log.debug("Запущена инициализация семейства правил контактной политики ContactTypeSuppresionRuleFamily ");
		contactTypeRuleMatrix = generateRuleMatrix();				
		log.debug("Инициализация семейства правил контактной политики ContactTypeSuppresionRuleFamily окончена");
	}
	
	@Override
	public CPCheckResult applyRuleFamily(String ctn, Campaign checkedCampaign,List<ContactHistoryRecord> previousContacts)throws Exception  {
		CPCheckResult result = new CPCheckResult(true);
		Date currentDate =  new Date();
		try {
			if(!previousContacts.isEmpty()&&lastCommunicationIsVoice(previousContacts))
			{
				Campaign lastCommunicationCampaign = CampaignsDAO.getCampaignById(previousContacts.get(0).getCampaignId());
				String index = String.valueOf(lastCommunicationCampaign.getCampaignType())+String.valueOf(checkedCampaign.getCampaignType());
				ContactTypeSuppresionRule selectedRule = contactTypeRuleMatrix.get(index);
				result = selectedRule.applyRule(lastCommunicationCampaign,currentDate,previousContacts.get(0).getContactDate());
			}
		}catch (Exception e) {
			log.error("Не удалось применить семейство правил к клиенту (ctn):" + ctn);
			throw e;
		}
		
		return result;
	}
	
	public boolean lastCommunicationIsVoice(List<ContactHistoryRecord> previousContacts){
		boolean result = false;
		ContactHistoryRecord lastHistoryRecord = previousContacts.get(0);
		for(ContactHistoryRecord historyRecord: previousContacts){
			if(lastHistoryRecord.getContactDate().compareTo(historyRecord.getContactDate())<0)
			{
				lastHistoryRecord = historyRecord;
			}
		}
		
		if(lastHistoryRecord.getContactType()==VOICE_CONTACT_TYPE)
			result = true;
		return result;
	}

	
}
