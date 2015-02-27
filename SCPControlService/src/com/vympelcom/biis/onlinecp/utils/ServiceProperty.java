package com.vympelcom.biis.onlinecp.utils;

import java.util.Properties;

public class ServiceProperty {

	private static Properties prop;	
	
	
	private ServiceProperty() {
		prop=new Properties();
		try {
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("/resuorce/service.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class ServletConfigHolder {
		private final static ServiceProperty instance = new ServiceProperty();
	}
		
	public static ServiceProperty getInstance() throws Exception {
		return ServletConfigHolder.instance;
	}
	
	public static String getProperty(String key){
		return prop.getProperty(key);
	}
	
}
