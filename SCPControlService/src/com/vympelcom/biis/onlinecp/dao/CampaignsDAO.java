package com.vympelcom.biis.onlinecp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.vympelcom.biis.onlinecp.domain.Campaign;
import com.vympelcom.biis.onlinecp.utils.OnlineCPDatabaseConnection;

public class CampaignsDAO {

	static final Logger log = Logger.getLogger(CampaignsDAO.class);
	
	public static Campaign getCampaignById(int id) throws Exception {
		Campaign result = null;
		OnlineCPDatabaseConnection databaseConnection = OnlineCPDatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("select * from campaigns where camp_id = ?");
			callstmt.setInt(1, id);
			ResultSet rs = callstmt.executeQuery();
			while (rs.next()) {
				result = new Campaign(rs.getString("camp_id"), rs.getString("camp_type"),
								      rs.getString("inform_date"),rs.getString("offer_date"));  
			}
		} catch (Exception e) {
			log.error("Could not get campaign camp_id = "+id+" "+e.getMessage());
		}finally{
			connection.close();
		}
		return result;
	}
		
}
