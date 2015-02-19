package com.glowbyte.biis.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.glowbyte.biis.domain.HistoryItem;
import com.glowbyte.biis.utils.DatabaseConnection;

public class ContactHistoryDAOImpl implements ContactHistoryDAO{

	@Override
	public List<HistoryItem> getContactHistoryByCTN(String ctn) throws Exception {
		List<HistoryItem> result = new ArrayList<HistoryItem>();
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("select * from contact_history where ctn = ?");
			callstmt.setString(1,ctn);
			ResultSet rs = callstmt.executeQuery();
			
			while (rs.next()) {
				HistoryItem currentItem = new HistoryItem(rs.getString("ctn"),
						rs.getString("contact_date"), rs.getString("contact_type"),rs.getString("contact_source"), "");
				result.add(currentItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return result;
	}

	@Override
	public List<HistoryItem> getHistoryByClientAndTime(String ctn,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
