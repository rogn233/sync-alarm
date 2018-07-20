package com.boco;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLUtil {
	private static final SQLUtil sqlUtil = new SQLUtil();
	public static final Logger log = Logger.getLogger(SQLUtil.class);
	
	public static SQLUtil getInstance()
	  {
		  try {
		  	  String proxoolProperties = Thread.currentThread().getContextClassLoader().getResource("proxool.properties").getPath();
			  PropertyConfigurator.configure(proxoolProperties);
		  } catch (ProxoolException e) {
			  e.printStackTrace();
		  }
		  DbUtils.loadDriver("org.logicalcobwebs.proxool.ProxoolDriver");
	    return sqlUtil;
	  }
	  
	
	public Connection getConn(String type){
		try {
			return DriverManager.getConnection("proxool." + type);
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
}
