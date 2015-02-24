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

	/*public static List<ContactHistoryRecord> getHistoryByClientAndTimeRange(String ctn,Date startDate, Date endDate) throws Exception {
		List<ContactHistoryRecord> result = new ArrayList<ContactHistoryRecord>();
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("select * from contact_history where ctn = ? and contact_date between ? and ? order by contact_date desc");
			callstmt.setString(1,ctn);
			java.sql.Date startDateSql = new java.sql.Date(startDate.getTime());
			java.sql.Date endDateSql = new java.sql.Date(endDate.getTime());
			callstmt.setDate(2, startDateSql);
			callstmt.setDate(3, endDateSql);
			ResultSet rs = callstmt.executeQuery();
			
			while (rs.next()) {
				ContactHistoryRecord currentItem = new ContactHistoryRecord(rs.getString("ctn"),
						rs.getDate("contact_date"), rs.getString("contact_type"),rs.getString("contact_source"), "",rs.getInt("camp_id"));
				result.add(currentItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return result;
	}*/
	
	
	public static List<ContactHistoryRecord> getHistoryByClient(String ctn) throws Exception {
		List<ContactHistoryRecord> result = new ArrayList<ContactHistoryRecord>();
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("select * from contact_history left join campaigns on contact_history.camp_id = campaigns.camp_id where ctn = ? order by contact_date desc");
			callstmt.setString(1,ctn);
			ResultSet rs = callstmt.executeQuery();
			while (rs.next()) {
				ContactHistoryRecord currentItem = new ContactHistoryRecord(rs.getString("ctn"),
						rs.getDate("contact_date"), rs.getString("contact_type"),rs.getString("contact_source"), rs.getString("contact_id"),rs.getInt("camp_id"),rs.getInt("camp_type"));
				result.add(currentItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return result;
	}
	
	@SuppressWarnings("unused")
	public static void writeRecordToContactHistory(ContactHistoryRecord savedRecord) throws Exception{
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("insert into contact_history (contact_id,ctn,contact_date,contact_type,contact_source,camp_id) values (contact_id_sequence.nextval,?,?,?,?,?)");
			callstmt.setString("1", savedRecord.getCTN());
			callstmt.setDate("2", new java.sql.Date(savedRecord.getContactDate().getTime()));
			callstmt.setInt("3", savedRecord.getContactType());
			callstmt.setString("4", savedRecord.getContactSource());
			callstmt.setInt("5", savedRecord.getCampaignId());
			
			ResultSet rs = callstmt.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connection.close();
		}
	}

}
