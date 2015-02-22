package com.vympelcom.biis.onlinecp.rules;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.Campaign;
import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;
import com.vympelcom.biis.onlinecp.utils.DatabaseConnection;

public class MaxFrequencyRuleFamily implements RuleFamily
{

	private volatile List<MaxFrequencyRule> maxFrequencyRuleList =  null;
	
	private List<MaxFrequencyRule> generateMaxFrequencyRuleMap() throws Exception{
		List<MaxFrequencyRule> result = new ArrayList<MaxFrequencyRule>();
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("select * from CP_RULE_2_MAX_FREQUENCY ");
			ResultSet rs = callstmt.executeQuery();
			
			while (rs.next()) {
				MaxFrequencyRule  currentItem = new MaxFrequencyRule(rs.getInt("CAMPAIGN_TYPE"), rs.getLong("ANALYSIS_PERIOD"), rs.getInt("MAX_CONTACT_FREQUENCY"));  
				result.add(currentItem);
			}
		}catch(SQLException exp)
		{
			exp.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return result;
	}
	
	public MaxFrequencyRuleFamily() throws Exception{
		if(maxFrequencyRuleList == null){
			synchronized (maxFrequencyRuleList) {
				if(maxFrequencyRuleList == null)
					maxFrequencyRuleList = generateMaxFrequencyRuleMap();
			}
		}
	}
	
	
	
	@Override
	public CPCheckResult applyRuleFamily(String ctn, Campaign checkedCampaign,
			List<ContactHistoryRecord> previousContacts) {
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
	
	/*TODO хранит массив Rules; Содержит метод для инициализации из БД. 
	 * 	Вызывается не из конструктора, а явно (.initialize)*/

	

}
