package com.vympelcom.biis.onlinecp.rules;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.Campaign;
import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;
import com.vympelcom.biis.onlinecp.utils.DatabaseConnection;

public class CampTypeSuppressionRuleFamily implements RuleFamily{

	static final Logger log = Logger.getLogger(CampTypeSuppressionRuleFamily.class);
	//Карта настроек правила
	private volatile HashMap<String, CampTypeSuppressionRule> campTypeSuppressionMatrix = null;
	
	
	private HashMap<String,CampTypeSuppressionRule> generateSuppresionMapping() throws Exception {
		HashMap<String,CampTypeSuppressionRule> result = new HashMap<String,CampTypeSuppressionRule>();
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("select * from CP_RULE_1_GENERAL_SUPPR_MATRIX ");
			ResultSet queryResult = callstmt.executeQuery();
			
			while (queryResult.next()) {
				CampTypeSuppressionRule  currentItem = new CampTypeSuppressionRule(queryResult.getString("history_campaign_type"),queryResult.getString("check_campaign_type"),
						queryResult.getLong("SUPPRESSION_INTERVAL_DAYS"));  
				result.put(currentItem.getHistCampaign()+currentItem.getCheckedCampaign(),currentItem);
				log.info("Загружено правило CampTypeSuppressionRule: " + currentItem.toString());
			}
		}catch(Exception exp){
			log.fatal("Ошибка при загрузке правил политики коммуникаци :"+ exp.getMessage());
			System.exit(0);
		}finally{
			connection.close();
		}
		return result;
	}
	
	public CampTypeSuppressionRuleFamily () throws Exception{
		log.debug("Запуск инициализации семейства правил CampTypeSuppressionRuleFamily");
		campTypeSuppressionMatrix = generateSuppresionMapping();
		log.debug("Окончание инициализации семейства правил CampTypeSuppressionRuleFamily ");
	}

	
	@Override
	public CPCheckResult applyRuleFamily(String ctn, Campaign checkedCampaign,List<ContactHistoryRecord> previousContacts) throws Exception{
		CPCheckResult result =  new CPCheckResult(true);
		Date currentDate = new Date();
		try {
			for(ContactHistoryRecord historyRecord: previousContacts)
			{
				CampTypeSuppressionRule targetRule = campTypeSuppressionMatrix.get(String.valueOf(historyRecord.getCampaignType())+String.valueOf(checkedCampaign.getCampaignType()));
				CPCheckResult currentResult = targetRule.applyRule(historyRecord.getContactDate(), currentDate);
				if(!currentResult.isContactAllowed())
				{
					result.setContactAllowed(false);
					break;
				}
			}
		} catch (Exception e) {
			log.error("Ошибка применения семейства правил коммуникации:CampTypeSuppressionRuleFamily "+e.getMessage());
			throw e;
		}
		return result;
	}



}
