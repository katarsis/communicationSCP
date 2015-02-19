package com.glowbyte.biis.rules;

import java.util.List;

import com.glowbyte.biis.dao.ContactHistoryDAO;
import com.glowbyte.biis.dao.ContactHistoryDAOImpl;
import com.glowbyte.biis.dao.SuppresionRuleDAO;
import com.glowbyte.biis.domain.HistoryItem;

public class SuppersionRule implements CommunicationRule{

	
	
	
	@Override
	public boolean checkRule(String ctn, int camp_id, int type) {
		boolean result = false;
		ContactHistoryDAO contactHistory = new ContactHistoryDAOImpl();
		List<HistoryItem> historyItemsList;
		try {
			historyItemsList = contactHistory.getContactHistoryByCTN(ctn);
			for(HistoryItem checkedItem: historyItemsList)
			{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	

}
