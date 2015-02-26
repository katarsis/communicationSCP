package com.vympelcom.biis.onlinecp.service;

import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.rules.ContactPolicyRuleManager;



@WebService
public class SCPControlService {
	
	static Logger log ;
	
	@Resource
    private WebServiceContext context;
	
	@PostConstruct
	private void init(){
		Properties props = new Properties();
       	try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/resuorce/log4j.properties");
			props.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		} 
        PropertyConfigurator.configure(props);
        
        log = Logger.getLogger(SCPControlService.class);
	}
	
	@WebMethod
	@WebResult(name="return")
	public int communicateSCP(@WebParam(name="ctn")String ctn,@WebParam(name="camp_id")int camp_id, @WebParam(name="contact_type")int contact_type) throws Exception{
		log.debug("Получен запрос с параметрами CTN:"+ctn+" camp_id:"+camp_id+" contact_type:"+contact_type);
		CPCheckResult resultApplyingRule = new CPCheckResult(true);
		ContactPolicyRuleManager contactPloicyRuleManager = ContactPolicyRuleManager.getInstance();
		resultApplyingRule = contactPloicyRuleManager.checkContactPolicyAndStoreContact(ctn, camp_id, contact_type);
		log.debug("Запрос с параметрами CTN:"+ctn+" camp_id:"+camp_id+" contact_type:"+contact_type+" обработан, результат запроса:"+resultApplyingRule.isContactAllowedInt());
		return resultApplyingRule.isContactAllowedInt();
	}
}
