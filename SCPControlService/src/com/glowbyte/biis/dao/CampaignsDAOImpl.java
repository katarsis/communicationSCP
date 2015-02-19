package com.glowbyte.biis.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.glowbyte.biis.domain.Campaign;
import com.glowbyte.biis.utils.DatabaseConnection;

public class CampaignsDAOImpl implements CampaignsDAO{

	@Override
	public List<Campaign> getCampaignById(int id) throws Exception {
		List<Campaign> result = new ArrayList<Campaign>();
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("select * from campaigns where camp_id = ?");
			callstmt.setInt(1, id);
			ResultSet rs = callstmt.executeQuery();
			
			while (rs.next()) {
				Campaign currentCamaign = new Campaign(	rs.getString("camp_id"), 
														rs.getString("camp_type"), 
														rs.getString("inform_date"),
														rs.getString("offer_date"));  
				result.add(currentCamaign);
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
		
}
