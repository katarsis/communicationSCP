package com.vympelcom.biis.onlinecp.domain;

public class CPCheckResult 
{
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

}
