package com.densan.sample.common;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

import com.densan.sample.constant.Constants;






public class Mysql {

	private String MYSQL_URL;
	private String MYSQL_USER;
	private String MYSQL_PW;
	
    public Mysql() throws GeneralSecurityException, IOException {
    	
    	Properties properties = new Constants().getConstants();
    	//ResourceBundle properties = new Constants().getConstants2();
    	
    	MYSQL_URL = properties.getProperty("MYSQL_URL");
    	MYSQL_USER = properties.getProperty("MYSQL_USER");
    	MYSQL_PW = properties.getProperty("MYSQL_PW");
    	
    	//MYSQL_URL = properties.getString("MYSQL_URL");
    	//MYSQL_USER = properties.getString("MYSQL_USER");
    	//MYSQL_PW = properties.getString("MYSQL_PW");    	
    	
    }	
	
	public Connection connection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
 		Connection con = null;
 		
 			Class.forName("com.mysql.jdbc.Driver");//.newInstance();
 			con = DriverManager.getConnection(MYSQL_URL,MYSQL_USER,MYSQL_PW);
 			System.out.println("success!");	
 		
 		return con;
 	}
	
	public void closeDb(Connection con) throws SQLException{
 		
 		try{
 			con.close();
 		}catch (SQLException e){
				System.out.println("mysql close fail");
				
				throw e;
		}
 	}	
	
	
}
