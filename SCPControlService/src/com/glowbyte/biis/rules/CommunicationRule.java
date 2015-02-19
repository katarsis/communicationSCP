package com.glowbyte.biis.rules;

public interface CommunicationRule {

	public boolean checkRule(String ctn, int camp_id, int type);
}
