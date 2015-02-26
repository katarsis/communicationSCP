package com.vympelcom.biis.onlinecp.utils;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.PropertyConfigurator;

import com.vympelcom.biis.onlinecp.rules.ContactPolicyRuleManager;

@WebListener("application context listener")
public class ContextInitializer implements ServletContextListener  {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Properties props = new Properties();
       	try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/resuorce/log4j.properties");
			props.load(is);
		    PropertyConfigurator.configure(props);
		    OnlineCPDatabaseConnection.getInstance();
			ContactPolicyRuleManager.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
