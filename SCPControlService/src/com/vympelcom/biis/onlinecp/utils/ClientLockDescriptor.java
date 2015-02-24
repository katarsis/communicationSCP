package com.vympelcom.biis.onlinecp.utils;

import java.util.concurrent.locks.Lock;

public class ClientLockDescriptor {
	
	public String ctn;
	public Lock lock;
	public int clientCount;

	public ClientLockDescriptor(String ctn, int count,Lock locker) {
		this.ctn =ctn;
		this.clientCount = count;
		this.lock = locker;
	}

	@Override
	public String toString() {
		return "ClientLockDescriptor [clientDwhId=" + ctn + ", lock="
				+ lock + ", clientCount=" + clientCount + "]";
	}
}
