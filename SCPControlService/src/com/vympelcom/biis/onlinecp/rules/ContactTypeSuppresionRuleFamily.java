package com.vympelcom.biis.onlinecp.rules;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.vympelcom.biis.onlinecp.dao.CampaignsDAO;
import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.Campaign;
import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;
import com.vympelcom.biis.onlinecp.utils.DatabaseConnection;

public class ContactTypeSuppresionRuleFamily implements RuleFamily{

	static final int VOICE_CONTACT_TYPE =1; 
	
	private static volatile HashMap<String,ContactTypeSuppresionRule> contactTypeRuleMatrix = null;
	
	private HashMap<String,ContactTypeSuppresionRule> generateRuleMatrix() throws Exception{
		HashMap<String, ContactTypeSuppresionRule> result = new HashMap<String, ContactTypeSuppresionRule>();
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("select * from CP_RULE_3_OFFER_SUPPR_MATRIX ");
			ResultSet rs = callstmt.executeQuery();
			
			while (rs.next()) {
				ContactTypeSuppresionRule  currentItem = new ContactTypeSuppresionRule(rs.getInt("CHECK_CAMPAIGN_TYPE"), rs.getInt("HISTORY_CAMPAIGN_TYPE"), rs.getLong("SUPPRESSION_INTERVAL_DAYS"), rs.getString("SUPPRESION_BASE_DATE"), rs.getInt("CONTACT_TYPE"));  
				String matrixKey = String.valueOf(currentItem.getCampType())+ String.valueOf(currentItem.getHistoryCampType());
				result.put(matrixKey,currentItem);
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
	
	public ContactTypeSuppresionRuleFamily() throws Exception{
		if(contactTypeRuleMatrix==null){
			synchronized (contactTypeRuleMatrix) {
				if(contactTypeRuleMatrix==null)
					contactTypeRuleMatrix = generateRuleMatrix();				
			}
		}
	}
	
	@Override
	public CPCheckResult applyRuleFamily(String ctn, Campaign checkedCampaign,List<ContactHistoryRecord> previousContacts)  {
		CPCheckResult result = new CPCheckResult(true);
		Date currentDate =  new Date();
		try {
			if(lastCommunicationIsVoice(previousContacts.get(0)))
			{
				Campaign lastCommunicationCampaign = CampaignsDAO.getCampaignById(previousContacts.get(0).getCampaignId());
				String index = String.valueOf(lastCommunicationCampaign.getCampaignType())+String.valueOf(checkedCampaign.getCampaignType());
				ContactTypeSuppresionRule selectedRule = contactTypeRuleMatrix.get(index);
				result = selectedRule.applyRule(lastCommunicationCampaign,currentDate,previousContacts.get(0).getContactDate());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean lastCommunicationIsVoice(ContactHistoryRecord historyRecord){
		boolean result = false;
		if(historyRecord.getContactType()==VOICE_CONTACT_TYPE)
			result = true;
		return result;
	}

	@Override
	public long getMaxDayInterval() {
		long result = 0;
		ArrayList<ContactTypeSuppresionRule> contactTypeRuleList = new ArrayList<ContactTypeSuppresionRule>(contactTypeRuleMatrix.values());
		for(ContactTypeSuppresionRule selectedRule: contactTypeRuleList){
			if(selectedRule.getSuppressionDay()>result)
				result = selectedRule.getSuppressionDay();
		}
		return result;
	}

}
