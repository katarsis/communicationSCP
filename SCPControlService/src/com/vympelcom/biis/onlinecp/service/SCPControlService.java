package com.vympelcom.biis.onlinecp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
	static final int COMMUNICATION_APPROVED =1;
	static final int COMMUNICATION_ABORTED =0;
	
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
	public int communicateSCP(@WebParam(name="ctn")String ctn,
							  @WebParam(name="camp_id")int camp_id,
							  @WebParam(name="contact_type")int contact_type 
							 ) throws Exception{
		int result =COMMUNICATION_APPROVED;
		log.debug("Recive request with parameters: CTN:"+ctn+" camp_id:"+camp_id+" contact_type:"+contact_type);
		ContactPolicyRuleManager ruleManager = ContactPolicyRuleManager.getInstance();
		CPCheckResult resultApplyingRule = ruleManager.checkContactPolicyAndStoreContact(ctn, camp_id, contact_type);
		if(!resultApplyingRule.isContactAllowed())
			result = COMMUNICATION_ABORTED;
		return result;
	}
}
