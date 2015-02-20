package com.vympelcom.biis.onlinecp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vympelcom.biis.onlinecp.domain.Campaign;
import com.vympelcom.biis.onlinecp.utils.DatabaseConnection;

public class CampaignsDAO {

	
	public static Campaign getCampaignById(int id) throws Exception {
		Campaign result = null;
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("select * from campaigns where camp_id = ?");
			callstmt.setInt(1, id);
			ResultSet rs = callstmt.executeQuery();
			
			while (rs.next()) {
				result = new Campaign(	rs.getString("camp_id"), 
														rs.getString("camp_type"), 
														rs.getString("inform_date"),
														rs.getString("offer_date"));  
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
