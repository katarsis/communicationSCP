package com.vympelcom.biis.onlinecp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.utils.OnlineCPDatabaseConnection;

public class CPCheckLogDAO {
	
	static final Logger log = Logger.getLogger(CPCheckLogDAO.class);
	
	public static void writeToCPChekResult(String ctn, int camp_id, int contact_type, CPCheckResult result, String errorText, Date startTimestamp) throws Exception{
		Date endRequestHandleTime  = new Date();
		OnlineCPDatabaseConnection databaseConnection = OnlineCPDatabaseConnection.getInstance();
		Connection connection=null;
		try{
			connection = databaseConnection.getConnection();
			CallableStatement callstmt = connection.prepareCall("insert into cp_check_log (id,ctn, contact_type,camp_id,time_receive, time_response, result, error_message) values (cp_check_rule.nextval,?,?,?,?,?,?,?)");
			callstmt.setString(1, ctn);
			callstmt.setInt(2, contact_type);
			callstmt.setInt(3, camp_id);
			callstmt.setTimestamp(4, new Timestamp(startTimestamp.getTime()));
			callstmt.setTimestamp(5, new Timestamp(endRequestHandleTime.getTime()));
			callstmt.setString(6,String.valueOf(result.isContactAllowedInt()));
			callstmt.setString(7, errorText);
			callstmt.executeQuery();
		} catch (Exception e) {
			log.error("Не удалось записать в CP_CHECK_RESULT "+ ctn.toString()+" "+e.getMessage());
		}finally{
			connection.close();
		}
	}

}
