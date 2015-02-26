package com.vympelcom.biis.onlinecp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;
import com.vympelcom.biis.onlinecp.utils.OnlineCPDatabaseConnection;

public class ContactHistoryDAO {

	static final Logger log = Logger.getLogger(ContactHistoryDAO.class);
		
	
	public static List<ContactHistoryRecord> getHistoryByClient(String ctn) throws Exception {
		List<ContactHistoryRecord> result = new ArrayList<ContactHistoryRecord>();
		OnlineCPDatabaseConnection databaseConnection = OnlineCPDatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			
			//TODO добавить условие, что contact_date > (сейчас - константа в днях, которую берем из конфига - сейчас 720)
			
			CallableStatement callstmt = connection.prepareCall("select * from contact_history left join campaigns on contact_history.camp_id = campaigns.camp_id where ctn = ? ");
			callstmt.setString(1,ctn);
			ResultSet rs = callstmt.executeQuery();
			
			while (rs.next()) {
				ContactHistoryRecord currentItem = new ContactHistoryRecord(rs.getString("ctn"),
						rs.getDate("contact_date"), rs.getString("contact_type"),rs.getString("contact_source"), rs.getString("contact_id"),rs.getInt("camp_id"),rs.getInt("camp_type"));
				result.add(currentItem);
				log.trace("Загружена запись из истории контактов: "+currentItem);
			}
			log.debug("Загружено "+result.size()+" записей из истории контактов");
		} catch (Exception e) {
			log.error("Не возможно загрузить историю контактов по клиенту: "+ctn+" "+e.getMessage());
			throw e;
		}finally{
			connection.close();
		}
		return result;
	}
	
	public static void writeRecordToContactHistory(ContactHistoryRecord savedRecord) throws Exception{
		OnlineCPDatabaseConnection databaseConnection = OnlineCPDatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("insert into contact_history (contact_id,ctn,contact_date,contact_type,contact_source,camp_id) values (contact_id_sequence.nextval,?,?,?,?,?)");
			callstmt.setString("1", savedRecord.getCTN());
			callstmt.setDate("2", new java.sql.Date(savedRecord.getContactDate().getTime()));
			callstmt.setInt("3", savedRecord.getContactType());
			callstmt.setString("4", savedRecord.getContactSource());
			callstmt.setInt("5", savedRecord.getCampaignId());
			callstmt.executeQuery();

		} catch (Exception e) {
			log.error("Could not write contact in contact history "+ savedRecord.toString()+" "+e.getMessage());
		}finally{
			connection.close();
		}
	}

}
