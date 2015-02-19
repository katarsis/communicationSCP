package com.glowbyte.biis.dao;

import java.util.Date;
import java.util.List;

import com.glowbyte.biis.domain.HistoryItem;

public interface ContactHistoryDAO {
	
	public List<HistoryItem> getContactHistoryByCTN(String ctn);
	
	public List<HistoryItem> getHistoryByClientAndTime(String ctn, Date startDate, Date endDate);

}
