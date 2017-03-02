/**
 * 
 */
package com.densan.sample;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.densan.sample.common.Mysql;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @author densan
 *
 */
@Path("mysql")
public class MysqlConnector {
	
    @GET
    @Path("{param}")
    @ApiOperation(value = "param")
    public Response getMsg(@PathParam("param") String msg) throws InstantiationException, IllegalAccessException, ClassNotFoundException, GeneralSecurityException, IOException, SQLException{
         String output = "Jersey say3 : " + msg;
               
 			Mysql m = new Mysql();
 			Connection con = m.connection();
 			System.out.println("success!");
 			
 			Statement stm = con.createStatement();
 			String sql = "insert into User values('id1','test')";
 			int result = stm.executeUpdate(sql);
 			System.out.println("num : " + result);
 			
 			m.closeDb(con);
    
         
         //logger.info("logtest" + output);
         //log.info("abcde");
         return Response.status(200).entity(output).build();
    }
		
	

}
