package com.vympelcom.biis.onlinecp.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

//TODO переименовать в OnlineCPDatabaseConnection

public class DatabaseConnection {

	private Properties props;
    private ComboPooledDataSource cpds;
    private static volatile DatabaseConnection datasource;
    static boolean DEBUG = true;

    private static final Logger log = Logger.getLogger(DatabaseConnection.class);
    

    private DatabaseConnection() throws Exception {

    /*TODO переделать на явный initialize. Т.е. у нас стартует сревлет, должен инициализовать все что ему нужно
    	- не ждем первого контакта.*/
    	
    	log.debug("Start initalization of DatabaseConnection");
        
        Connection testConnection = null;
        Statement testStatement = null;
        
        try{
	        props = new Properties();
	        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/resuorce/datasource.properties");
	        props.load(is);

	        //TODO о чем эта запись в лог? Как ее трактовать администратору?*/
	        log.debug("ComboPooledDataSource");
	        cpds = new ComboPooledDataSource();
	        
	        //TODO логируется не то что нужно. Эти три метода провалиться не могут. Что гораздо интереснее логировать - собстьвенно, какие свойства прочитаны из конфига (значения)*/ 
	        
	        log.debug("setJdbcUrl");
	        cpds.setJdbcUrl(props.getProperty("jdbcUrl"));
	        cpds.setUser(props.getProperty("username"));
	        cpds.setPassword(props.getProperty("password"));
	
	        log.debug("setInitialPoolSize");
	        cpds.setInitialPoolSize(new Integer((String) props.getProperty("initialPoolSize")));
	        cpds.setAcquireIncrement(new Integer((String) props.getProperty("acquireIncrement")));
	        cpds.setMaxPoolSize(new Integer((String) props.getProperty("maxPoolSize")));
	        cpds.setMinPoolSize(new Integer((String) props.getProperty("minPoolSize")));
	        cpds.setMaxStatements(new Integer((String) props.getProperty("maxStatements")));
	        
	        log.debug(cpds.getJdbcUrl());
	       
	        // test connectivity and initialize pool
	        Locale.setDefault(Locale.ENGLISH);
		    testConnection = cpds.getConnection();
		    testStatement = testConnection.createStatement();
		    testStatement.executeQuery("select 1+1 from DUAL");
		    //TODO вот это залогировать получше. "Тестируем..."   "Запрос select 1+1 from DUAL выполнен успешно"
		    
		    log.debug("Initalization of DatabaseConnection is sucessfuly ended");
        } catch (Exception e) {
        	log.error("Initalization of DatabaseConnection aborted :" + e.getMessage());
            throw e;
        }     		
        finally {
            testStatement.close();
            testConnection.close();
        }
    }
    
    
    public static DatabaseConnection getInstance() throws Exception {
        if (datasource == null) {
        	synchronized (DatabaseConnection.class) {
        		if(datasource==null)
        			datasource = new DatabaseConnection();
                return datasource;
			}
         } else {
              return datasource;
         }
    }
    
    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }
    
}
