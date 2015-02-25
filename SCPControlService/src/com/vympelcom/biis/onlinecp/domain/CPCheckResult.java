package com.vympelcom.biis.onlinecp.domain;

public class CPCheckResult 
{
	public static final int COMMUNICATION_APPROVED =1;
	public static final int COMMUNICATION_ABORTED =0;
	
	private boolean isContactAllowed;
	
	public CPCheckResult(boolean answer){
		this.isContactAllowed = answer;
	}

	public boolean isContactAllowed() {
		return isContactAllowed;
	}

	public void setContactAllowed(boolean isContactAllowed) {
		this.isContactAllowed = isContactAllowed;
	}

	public int isContactAllowedInt(){
		if(isContactAllowed)
			return COMMUNICATION_APPROVED;
		else
			return COMMUNICATION_ABORTED;
	}
}
