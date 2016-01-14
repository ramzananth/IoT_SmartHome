package com.rest.client;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.codehaus.jettison.json.JSONObject;


@Path("/jsonservicesClient")
public class JSONServiceClient {

	@GET
	@Path("/read/{choice}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAttribute (@PathParam("choice") String choice )throws Exception{		
		String details = MongoServiceClient.readAttribute(choice);
		return Response.ok(details).build();
	}
	
	@GET		// discover
	@Path("/discover")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response discover ()throws Exception{	
	String returnString = MongoServiceClient.discover();
	return Response.ok(returnString).build();
	}
	
	@GET		// observe
	@Path("/observe")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response observe ()throws Exception{	
		String re = MongoServiceClient.observe();
		return Response.ok(re).build();
	}

	@GET		// cancel
	@Path("/cancel")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancel ()throws Exception{	
		String re = MongoServiceClient.cancel();
		return Response.ok(re).build();
	}

	@POST
	@Path("/write")					// 2 for registration
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response write( String id ) throws Exception {			
		String att = id.substring(0,id.lastIndexOf(","));
		String what = id.substring(id.lastIndexOf(",")+1,id.length());
		String k = MongoServiceClient.write(att, what);		
		return Response.ok(k).build();
	}
	
	@POST
	@Path("/writeAttribute")					// 2 for registration
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response writeAttribute( String id ) throws Exception {			
		String att = id.substring(0,id.lastIndexOf(","));
		String what = id.substring(id.lastIndexOf(",")+1,id.length());
		String k = MongoServiceClient.writeAttribute(att, what);		
		return Response.ok(k).build();
	}
	
	@GET
	@Path("/create")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response create ()throws Exception{	
	MongoServiceClient.createObject();;
	return Response.ok("Object Created !!").build();
	}
	
	@GET
	@Path("/delete")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete ()throws Exception{	
	MongoServiceClient.deleteObject();;
	return Response.ok("Object Deleted !!").build();
	}
	

}
