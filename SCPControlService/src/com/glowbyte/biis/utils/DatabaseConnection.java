package com.glowbyte.biis.utils;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
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
        props.load(new InputStreamReader(new FileInputStream("/WEB_INF/datasource.properties"),"UTF-8"));
        
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
    
    public static Hashtable<String, String> selectFromDB(String queryString, String ...params)
	{
    	DatabaseConnection ds;
		Connection connection=null;
		Hashtable<String, String> result = new Hashtable<String, String>();
		try{
		ds = DatabaseConnection.getInstance();
		connection = ds.getConnection();
		CallableStatement callstmt = connection.prepareCall(queryString);
		int counter =1;
		if(params!=null)
		for(String param:params)
		{
			callstmt.setString(counter, param);
			counter++;
		}
		ResultSet rs = callstmt.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
	
		String columnName = null;
		String columnValue = null;
		
		while (rs.next()) {
			int columnCount = rsmd.getColumnCount();
			for (int i=1; i<=columnCount; i++) {
				
				columnName=rsmd.getColumnName(i);
				columnValue=rs.getString(columnName);
				
				result.put(columnName, columnValue);
			}
			
		}
		}catch(SQLException exp)
		{
			exp.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
		
	}
    
}
