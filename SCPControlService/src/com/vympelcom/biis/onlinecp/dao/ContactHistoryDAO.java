package com.vympelcom.biis.onlinecp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;
import com.vympelcom.biis.onlinecp.utils.DatabaseConnection;

public class ContactHistoryDAO {

	public List<ContactHistoryRecord> getHistoryByClientAndTimeRange(String ctn,
		Date startDate, Date endDate) throws Exception {
		List<ContactHistoryRecord> result = new ArrayList<ContactHistoryRecord>();
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("select * from contact_history where ctn = ? and contact_date ? and ?");
			callstmt.setString(1,ctn);
			java.sql.Date startDateSql = new java.sql.Date(startDate.getTime());
			java.sql.Date endDateSql = new java.sql.Date(endDate.getTime());
			callstmt.setDate(2, startDateSql);
			callstmt.setDate(3, endDateSql);
			ResultSet rs = callstmt.executeQuery();
			
			while (rs.next()) {
				ContactHistoryRecord currentItem = new ContactHistoryRecord(rs.getString("ctn"),
						rs.getString("contact_date"), rs.getString("contact_type"),rs.getString("contact_source"), "",rs.getInt("camp_id"));
				result.add(currentItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return result;
	}

}
