package com.assignment2.server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.codehaus.jettison.json.JSONObject;


@Path("/jsonservices")
public class JSONService {
	//	public static HashMap<String, user> hash = new HashMap<String, user>();

	@GET
	@Path("/print/{id}")					// 1 for bootstrap
	@Produces(MediaType.APPLICATION_JSON)
	public String produceJSON( @PathParam("id") String id ) throws Exception {
		String details = MongoService.getResourceDetails(id);
		JSONObject n = new JSONObject(details);						//to JSON
		return n.toString();		
	}

	@POST
	@Path("/register/registerDevice")					// 2 for registration
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerDevice( String id ) throws Exception {

		int find = id.lastIndexOf("}");
		int find2=id.lastIndexOf(",");
		String serialNumber = id.substring(find+1, find2);
		String clientURI=id.substring(find2+1,id.length());
		String sub = id.substring(0, find+1);
		String tem = MongoService.postValues(sub, serialNumber, clientURI);

		return Response.ok(tem).build();




	}

	@PUT
	@Path("/register/updateDevice/lifetime")					// 2 for Updating lifetime attribute
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateLifetime(  String id ) throws Exception {

		int find = id.lastIndexOf("}");
		String newLifetime = id.substring(find+1, id.length());
		String sub = id.substring(0, find+1);
		String tem = MongoService.updateLifetime(sub, newLifetime);

		return Response.ok(tem).build();
	}


	@PUT
	@Path("/register/updateDevice/bindingMode")					// 2 for Updating bindingMode attribute
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateBindingMode(  String id ) throws Exception {

		int find = id.lastIndexOf("}");
		String newBinding = id.substring(find+1, id.length());
		String sub = id.substring(0, find+1);
		String tem = MongoService.updateBindingMode(sub, newBinding);
		return Response.ok(tem).build();
	}


	@DELETE
	@Path("/register/delete/{id}")		
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	public Response deleteItem(@PathParam("id") String id) throws Exception {

		String tem = MongoService.deleteValue(id);
		return Response.ok(tem).build();
	}

	@GET
	@Path("/notify/{id}")					// 1 for bootstrap
	@Produces(MediaType.APPLICATION_JSON)
	public Response notify( @PathParam("id") String id ) throws Exception {	
		java.util.Date date= new java.util.Date();
		System.out.println("Notifying from Client to the Server: "+new Timestamp(date.getTime())+" > "+"Temperature : "+id);
		System.out.println("Continue receiving notifications from server ? Y/N");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String ch = br.readLine();
		Thread.sleep(3000);
		return Response.ok(ch).build();
	}


}
