package com.vympelcom.biis.onlinecp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;
import com.vympelcom.biis.onlinecp.utils.DatabaseConnection;

public class ContactHistoryDAO {

	static final Logger log = Logger.getLogger(ContactHistoryDAO.class);
		
	
	public static List<ContactHistoryRecord> getHistoryByClient(String ctn) throws Exception {
		List<ContactHistoryRecord> result = new ArrayList<ContactHistoryRecord>();
		DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			
			//TODO сортировку перенести в java, в соответствующий рул. 
			
			//TODO добавить условие, что contact_date > (сейчас - константа в днях, которую берем из конфига - сейчас 720)
			
			CallableStatement callstmt = connection.prepareCall("select * from contact_history left join campaigns on contact_history.camp_id = campaigns.camp_id where ctn = ? order by contact_date desc");
			callstmt.setString(1,ctn);
			ResultSet rs = callstmt.executeQuery();
			while (rs.next()) {
				ContactHistoryRecord currentItem = new ContactHistoryRecord(rs.getString("ctn"),
						rs.getDate("contact_date"), rs.getString("contact_type"),rs.getString("contact_source"), rs.getString("contact_id"),rs.getInt("camp_id"),rs.getInt("camp_type"));
				result.add(currentItem);

				//debug-логирование, сколько контактов загружено. По-русски.

				//trace-логирование - полностью вывести все контакты прочитанные со всеми полями
				
				
			}
		} catch (Exception e) {
			//Избавляемся от английского языка, если им недостаточно владеем.
			log.error("Could not get previous contacts by ctn: "+ctn+" "+e.getMessage());
			
			//TODO. Что дальше произойдет? Сейчас выйдем на разрешенный контакт. Должны выйти на ошибку в веб сервисе.
		}finally{
			connection.close();
		}
		return result;
	}
	
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
			callstmt.executeQuery();

		} catch (Exception e) {
			log.error("Could not write contact in contact history "+ savedRecord.toString()+" "+e.getMessage());
		}finally{
			connection.close();
		}
	}

}
