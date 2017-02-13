package com.densan.sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/sample")
public class SampleService {

     @GET
     @Path("/{param}")
     public Response getMsg(@PathParam("param") String msg) {
          String output = "Jersey say : " + msg;
          return Response.status(200).entity(output).build();
     }

}