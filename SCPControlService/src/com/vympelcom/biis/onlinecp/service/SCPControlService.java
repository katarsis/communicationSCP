package com.vympelcom.biis.onlinecp.service;

import java.util.Date;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;

import com.vympelcom.biis.onlinecp.dao.CPCheckLogDAO;
import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.rules.ContactPolicyRuleManager;



@WebService
public class SCPControlService {
	
	static final Logger log = Logger.getLogger(SCPControlService.class);
	
	@Resource
    private WebServiceContext context;
	
	@WebMethod
	@WebResult(name="return")
	public int communicateSCP(@WebParam(name="ctn")String ctn,@WebParam(name="camp_id")int camp_id, @WebParam(name="contact_type")int contact_type) throws Exception{
		CPCheckResult resultApplyingRule = new CPCheckResult(true);
		Date startHandleTimeStamp = new Date();
		String errorHandleMessage = "";
		try{
			log.debug("Получен запрос с параметрами CTN:"+ctn+" camp_id:"+camp_id+" contact_type:"+contact_type);
			ContactPolicyRuleManager contactPloicyRuleManager = ContactPolicyRuleManager.getInstance();
			resultApplyingRule = contactPloicyRuleManager.checkContactPolicyAndStoreContact(ctn, camp_id, contact_type,startHandleTimeStamp);
			log.debug("Запрос с параметрами CTN:"+ctn+" camp_id:"+camp_id+" contact_type:"+contact_type+" обработан, результат запроса:"+resultApplyingRule.isContactAllowedInt());
		}catch(Exception exp)
		{
			errorHandleMessage = exp.toString();
			throw exp;
		}finally{
			CPCheckLogDAO.writeToCPChekResult(ctn, camp_id, contact_type, resultApplyingRule,errorHandleMessage, startHandleTimeStamp);
		}		
		return resultApplyingRule.isContactAllowedInt();
	}
}
