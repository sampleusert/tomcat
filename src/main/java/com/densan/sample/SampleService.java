package com.densan.sample;

//import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path("sample")
@Api(value = "/sample", description="test")
public class SampleService {
//	final Logger logger = Logger.getLogger(this.getClass().getName());
	final Logger log = LogManager.getLogger();
     @GET
     @Path("{param}")
     @ApiOperation(value = "param")
     public Response getMsg(@PathParam("param") String msg) {
          String output = "Jersey say3 : " + msg;
          //logger.info("logtest" + output);
          	log.info("abcde");
          return Response.status(200).entity(output).build();
     }

}