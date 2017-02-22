package com.densan.sample;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("sample")
public class SampleService {
	final Logger logger = Logger.getLogger(this.getClass().getName());

     @GET
     @Path("{param}")
     public Response getMsg(@PathParam("param") String msg) {
          String output = "Jersey say3 : " + msg;
          logger.info("logtest" + output);
          return Response.status(200).entity(output).build();
     }

}