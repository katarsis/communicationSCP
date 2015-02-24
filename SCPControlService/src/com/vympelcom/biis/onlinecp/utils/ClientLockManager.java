package com.vympelcom.biis.onlinecp.utils;

import java.util.Hashtable;
import java.util.concurrent.locks.ReentrantLock;

public class ClientLockManager {

	static Hashtable<String, ClientLockDescriptor> clientLockMap = new Hashtable<String, ClientLockDescriptor>();

	public static ClientLockDescriptor GetClientLock(String ctn)
	{
		ClientLockDescriptor result = null;
		synchronized (clientLockMap) {
			if(clientLockMap.containsKey(ctn))
			{
				result =  clientLockMap.get(ctn);
				result.clientCount++;
			}
			else
			{
				result =new ClientLockDescriptor(ctn, 1, new ReentrantLock());
				clientLockMap.put(ctn, result);
			}
		}
		result.lock.lock();
		return result;
	}
	
	public static void RemoveClientLock(ClientLockDescriptor clientLockDescriptor)
	{
		synchronized (clientLockMap) {
			clientLockDescriptor.lock.unlock();
			clientLockDescriptor.clientCount--;
			if(clientLockDescriptor.clientCount==0)
			{
				clientLockMap.remove(clientLockDescriptor.ctn);
				clientLockDescriptor= null;
				
			}
			
		}		
	}
}
