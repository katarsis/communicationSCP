package com.vympelcom.biis.onlinecp.rules;

import java.util.List;

import com.vympelcom.biis.onlinecp.domain.*;

public interface RuleFamily {

	public CPCheckResult applyRuleFamily (String ctn, Campaign checkedCampaign, List<ContactHistoryRecord> previousContacts);
	
	public long getMaxDayInterval();
	
}
