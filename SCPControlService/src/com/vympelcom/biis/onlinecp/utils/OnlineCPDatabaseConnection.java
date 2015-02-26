package com.vympelcom.biis.onlinecp.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class OnlineCPDatabaseConnection {

	private Properties props;
    private ComboPooledDataSource cpds;
    private static volatile OnlineCPDatabaseConnection datasource;
    static boolean DEBUG = true;

    private static final Logger log = Logger.getLogger(OnlineCPDatabaseConnection.class);
    

    private OnlineCPDatabaseConnection() throws Exception {

    /*TODO переделать на явный initialize. Т.е. у нас стартует сревлет, должен инициализовать все что ему нужно
    	- не ждем первого контакта.*/
    	
    	log.info("Инициализация соединения с базой данных ");
        
        Connection testConnection = null;
        Statement testStatement = null;
        
        try{
	        props = new Properties();
	        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/resuorce/datasource.properties");
	        props.load(is);

	       
	        cpds = new ComboPooledDataSource();
	        cpds.setJdbcUrl(props.getProperty("jdbcUrl"));
	        cpds.setUser(props.getProperty("username"));
	        cpds.setPassword(props.getProperty("password"));
	        cpds.setInitialPoolSize(new Integer((String) props.getProperty("initialPoolSize")));
	        cpds.setAcquireIncrement(new Integer((String) props.getProperty("acquireIncrement")));
	        cpds.setMaxPoolSize(new Integer((String) props.getProperty("maxPoolSize")));
	        cpds.setMinPoolSize(new Integer((String) props.getProperty("minPoolSize")));
	        cpds.setMaxStatements(new Integer((String) props.getProperty("maxStatements")));
	        
	        log.info("Пытаемся установить соединение с базой данных со следующими параметрами JDBC URL:" + cpds.getJdbcUrl()+""
	        		+ " Пользователь: "+ cpds.getUser()+" Пароль: "+cpds.getPassword()+" размер начального пула соединений:"+ cpds.getInitialPoolSize()+""
	        		+ " шаг увеличения новых соедений с БД:"+ cpds.getAcquireIncrement()+" максимальное количество соединений: "+cpds.getMaxPoolSize()+""
	        		+ " минимальное количество поддерживаемых соединений: "+cpds.getMinPoolSize()+" максимальное количество запросов в соединении "+ cpds.getMaxStatements());
	       
	        
	        Locale.setDefault(Locale.ENGLISH);
		    testConnection = cpds.getConnection();
		    testStatement = testConnection.createStatement();
		    log.info("Тестируем соедеинение с базой данных выполняем запрос select 1+1 from DUAL");
		    testStatement.executeQuery("select 1+1 from DUAL");
		    log.info("Соединене с базой данных протестировано успешно");
        } catch (Exception e) {
        	log.error("Соединение с базой данных прервано :" + e.getMessage());
            throw e;
        }     		
        finally {
            testStatement.close();
            testConnection.close();
        }
    }
    
    
    public static OnlineCPDatabaseConnection initalize() throws Exception {
        if (datasource == null) {
        	synchronized (OnlineCPDatabaseConnection.class) {
        		if(datasource==null)
        			datasource = new OnlineCPDatabaseConnection();
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
