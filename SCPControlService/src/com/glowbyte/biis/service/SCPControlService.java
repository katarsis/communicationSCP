package com.glowbyte.biis.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import com.glowbyte.biis.dao.CampaignsDAO;
import com.glowbyte.biis.dao.CampaignsDAOImpl;
import com.glowbyte.biis.rules.ComminictionPoliticsManager;
import com.glowbyte.biis.rules.CommunicationRule;
import com.glowbyte.biis.utils.DatabaseConnection;



@WebService
public class SCPControlService {
	static final int COMMUNICATION_APPROVED =1;
	static final int COMMUNICATION_ABORTED =0;
	@Resource
    private WebServiceContext context;
	
	@PostConstruct 
	public void init() throws Exception{
		System.out.println("/////////////////////////////init////////////////////////////////");
		DatabaseConnection.getInstance();
	}
	
	@WebMethod
	public int communicateSCP(String ctn, int camp_id, int type) throws Exception{
		int result = COMMUNICATION_APPROVED;
		CampaignsDAO campaignsDao = new CampaignsDAOImpl();
		campaignsDao.getCampaignById(5001);
		/*ComminictionPoliticsManager communicationPoliticsManager = null;
		List<CommunicationRule> communicationRuleList =  communicationPoliticsManager.getAllCommunicationRules();
		for(CommunicationRule rule : communicationRuleList){
			if(!rule.checkRule(ctn, camp_id, type)){
				result = COMMUNICATION_ABORTED;
				break;
			}
		}*/
		return result;
	}
}
