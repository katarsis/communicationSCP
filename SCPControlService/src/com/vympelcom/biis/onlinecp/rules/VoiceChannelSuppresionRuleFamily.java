package com.vympelcom.biis.onlinecp.rules;

import java.util.List;

import com.vympelcom.biis.onlinecp.dao.CampaignsDAO;
import com.vympelcom.biis.onlinecp.domain.CPCheckResult;
import com.vympelcom.biis.onlinecp.domain.Campaign;
import com.vympelcom.biis.onlinecp.domain.ContactHistoryRecord;

public class VoiceChannelSuppresionRuleFamily implements RuleFamily{

	static final int VOICE_CONTACT_TYPE =1; 
	
	@Override
	public CPCheckResult applyRuleFamily(String ctn, Campaign checkedCampaign,List<ContactHistoryRecord> previousContacts)  {
		

		try {
			if(lastCommunicationIsVoice(previousContacts.get(0)))
			{
				Campaign lastCommunicationCampaign = CampaignsDAO.getCampaignById(previousContacts.get(0).getCampaignId());
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean lastCommunicationIsVoice(ContactHistoryRecord historyRecord){
		boolean result = false;
		if(historyRecord.getContactType()==VOICE_CONTACT_TYPE)
			result = true;
		return result;
	}

}
