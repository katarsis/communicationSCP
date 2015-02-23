package com.vympelcom.biis.onlinecp.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DatabaseConnection {

	private Properties props;
    private ComboPooledDataSource cpds;
    private static volatile DatabaseConnection datasource;
    static boolean DEBUG = true;

    private static final Logger log = Logger.getLogger(DatabaseConnection.class);
    

    private DatabaseConnection() throws Exception {
    	   
        try {           
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch (ClassNotFoundException e) {
            log.error("Oracle JDBC Driver not found");
            e.printStackTrace();
            return;
        }
        // load datasource properties
        log.info("Reading datasource.properties from classpath");
        
        Connection testConnection = null;
        Statement testStatement = null;
        
        try{
        props = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/resuorce/datasource.properties");
        props.load(is);
        
        log.debug("ComboPooledDataSource");
        cpds = new ComboPooledDataSource();
        
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
        } catch (Exception e) {
        	log.error("Test connection failed");
        	e.printStackTrace();
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
                log.trace("New pool datasource created");
                return datasource;
			}
         } else {
              log.trace("Existing connection instance is used");
              return datasource;
         }
    }
    
    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }
    
}
