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
			ResultSet rs = callstmt.executeQuery();
			
			while (rs.next()) {
				CampTypeSuppressionRule  currentItem = new CampTypeSuppressionRule(rs.getString("history_campaign_type"),rs.getString("check_campaign_type"),
						rs.getLong("SUPPRESSION_INTERVAL_DAYS"));  
				result.put(currentItem.getHistCampaign()+currentItem.getCheckedCampaign(),currentItem);
				log.info("Load CampTypeSuppressionRule: " + currentItem.toString());
			}
			
			/*TODO: 
			 		1) Вывести в лог явно что не удалось загрузить список правил такого-то семейства.
			 			(log4j, FATAL)
			 		2) Прервать инициализацию сервиса. Мы не должны начинать слушать веб сервис.
			 */
						
		}catch(Exception exp)
		{
			log.fatal("Could not load CampTypeSuppressionRuleFamily property list :"+ exp.getMessage());
			//прерывание инициализации сервиса
			System.exit(0);
		}finally{
			connection.close();
		}
		return result;
	}
	
	public CampTypeSuppressionRuleFamily () throws Exception{
		log.debug("Start initalization CampTypeSuppressionRuleFamily ");
		campTypeSuppressionMatrix = generateSuppresionMapping();
		log.debug("End initalization CampTypeSuppressionRuleFamily ");
	}

	
	@Override
	public CPCheckResult applyRuleFamily(String ctn, Campaign checkedCampaign,List<ContactHistoryRecord> previousContacts) {
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
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public long getMaxDayInterval() {
		long result =0;
		ArrayList<CampTypeSuppressionRule> campTypeSuppressionRule =new ArrayList<CampTypeSuppressionRule>(campTypeSuppressionMatrix.values());
		for(CampTypeSuppressionRule selectedRule: campTypeSuppressionRule){
			if(selectedRule.getCountOfSuppresionDay()>result)
				result = selectedRule.getCountOfSuppresionDay();
		}
		return result;
	}
	
	
	
	

}
