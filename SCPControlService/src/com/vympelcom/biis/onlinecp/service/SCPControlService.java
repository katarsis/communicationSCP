package com.vympelcom.biis.onlinecp.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import com.vympelcom.biis.onlinecp.utils.DatabaseConnection;



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
	public int communicateSCP(@WebParam(name="ctn")String ctn,
							  @WebParam(name="camp_id")int camp_id,
							  @WebParam(name="contact_type")int contact_type) throws Exception{
		int result = COMMUNICATION_APPROVED;
		return result;
	}
}
