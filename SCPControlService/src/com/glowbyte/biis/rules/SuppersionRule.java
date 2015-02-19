package com.glowbyte.biis.rules;

import java.util.ArrayList;
import java.util.List;

import com.glowbyte.biis.dao.ContactHistoryDAO;
import com.glowbyte.biis.dao.ContactHistoryDAOImpl;
import com.glowbyte.biis.domain.HistoryItem;

public class SuppersionRule implements CommunicationRule{

	@Override
	public boolean checkRule(String ctn, int camp_id, int type) {
		boolean result = false;
		ContactHistoryDAO contactHistory = new ContactHistoryDAOImpl();
		List<HistoryItem> historyItemsList;
		try {
			historyItemsList = contactHistory.getContactHistoryByCTN(ctn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	

}
