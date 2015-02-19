package com.glowbyte.biis.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.glowbyte.biis.domain.SuppresionRuleProperty;
import com.glowbyte.biis.utils.DatabaseConnection;

public class SuppresionRuleDAO {

	static volatile HashMap<String,SuppresionRuleProperty> propertyList = null; 
	
	private synchronized HashMap<String,SuppresionRuleProperty> generateSuppresionMapping() throws Exception {
		HashMap<String,SuppresionRuleProperty> result = new HashMap<String,SuppresionRuleProperty>();
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		Connection connection=null;
		
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("select * from cp_rule_1_general_suppr_matrix");
			ResultSet rs = callstmt.executeQuery();
			
			while (rs.next()) {
				SuppresionRuleProperty  currentItem = new SuppresionRuleProperty(rs.getString("history_campaign_type"),
																				rs.getString("check_campaign_type"),
																				rs.getString("suppression_interval_days"));  
				result.put(currentItem.getCheckedCampaign(),currentItem);
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
	
	public HashMap<String,SuppresionRuleProperty> getInstance() throws Exception{
		if(propertyList.isEmpty()){
			synchronized (propertyList) {
				if(propertyList==null)
					propertyList = generateSuppresionMapping();				
			}
		}
		return propertyList;
	}
	
	public SuppresionRuleProperty getPropertyById(String id){
		return propertyList.get(id);
	}
	
	
}
