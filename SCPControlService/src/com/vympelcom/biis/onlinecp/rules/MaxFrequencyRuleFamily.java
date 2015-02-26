package com.vympelcom.biis.onlinecp.rules;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.Campaign;
import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;
import com.vympelcom.biis.onlinecp.utils.OnlineCPDatabaseConnection;

public class MaxFrequencyRuleFamily implements RuleFamily
{

	private volatile List<MaxFrequencyRule> maxFrequencyRuleList =  null;
	
	static final Logger log = Logger.getLogger(MaxFrequencyRuleFamily.class);
	
	private List<MaxFrequencyRule> generateMaxFrequencyRuleMap() throws Exception{
		List<MaxFrequencyRule> result = new ArrayList<MaxFrequencyRule>();
		OnlineCPDatabaseConnection databaseConnection = OnlineCPDatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("select * from CP_RULE_2_MAX_FREQUENCY ");
			ResultSet rs = callstmt.executeQuery();
			
			while (rs.next()) {
				MaxFrequencyRule  currentItem = new MaxFrequencyRule(rs.getLong("ANALYSIS_PERIOD"), rs.getInt("MAX_CONTACT_FREQUENCY"));  
				result.add(currentItem);
				log.info("Загружено правило контактной политики MaxFrequencyRule: "+ currentItem.toString());
			}
		}catch(Exception exp)
		{
			log.fatal("Не возможно загрузить семейство правил контактной политики MaxFrequencyRuleFamily: " + exp.getMessage());
		}finally{
			connection.close();
		}
		return result;
	}
	
	public MaxFrequencyRuleFamily() throws Exception{
		log.debug("Начало инициализации семейства правил контактной политики MaxFrequencyRuleFamily");
		maxFrequencyRuleList = generateMaxFrequencyRuleMap();
		log.debug("Инициализация семейства правил контактной политики MaxFrequencyRuleFamily завершена");
	}
	
	
	
	@Override
	public CPCheckResult applyRuleFamily(String ctn, Campaign checkedCampaign,List<ContactHistoryRecord> previousContacts){
		CPCheckResult result = new CPCheckResult(true);
		Date currentDate =  new Date();
		for(MaxFrequencyRule currentRule : maxFrequencyRuleList)
		{
			CPCheckResult localResult = currentRule.applyRule(previousContacts, currentDate);
			if(!localResult.isContactAllowed()){
				result.setContactAllowed(false);
				break;
			}
		}
		return result;
	}

		
}
