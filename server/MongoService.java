package com.assignment2.server;

import org.codehaus.jettison.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoService {

 	static String clientInServer;
	static String result;
	static String lastTuple;
	static String getResourceDetails(String deviceId){
		try {
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds053380.mongolab.com:53380/cmpe273");
			MongoClient client;
			BasicDBObject query = new BasicDBObject();
			client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");
			DBCollection collection = database.getCollection("bootdB"); 
			query.put("endPoint",deviceId);
			DBCursor dcursor = collection.find(query);    
			
			if(dcursor.hasNext()){
				result = dcursor.next().toString();	 
				return result;
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return result;	
	}
  
	static String postValues(String id, String serial, String uri){ // for Registration
		try
		{
			
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds053380.mongolab.com:53380/cmpe273");
			MongoClient client;
			client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");
			
			DBCollection collection = database.getCollection("regdB");
			DBCollection collectionCC = database.getCollection("currentClientsdB");
			JSONObject partsData = new JSONObject(id);
			String end = partsData.optString("endPoint");
			
			BasicDBObject qu = new BasicDBObject();
			qu.put("serialNumber", serial);
			qu.put("endPoint", end);
			
			DBCursor db = collection.find(qu);
			
			if (!db.hasNext()){
				BasicDBObject document = new BasicDBObject();				
				document.put("endPoint", partsData.optString("endPoint"));
				document.put("serialNumber", serial);
				document.put("DeviceName","Fridge");
				//document.put("Mode", partsData.optString("Mode")); 
				document.put("sensor", partsData.optString("sensor"));
				document.put("manufacturer", partsData.optString("manufacturer"));
				document.put("firmware", partsData.optString("firmware"));
				document.put("currentDate", partsData.optString("currentDate"));			
				document.put("LifeTime", "86400"); 		// new attributes
				document.put("BindingMode", "UDP");
				document.put("ClientURI", uri);
				
				collection.insert(document);
				
				DBCursor dbc = collectionCC.find();
				if (dbc.hasNext()){
				DBObject removeFirst = collectionCC.findOne();
				collectionCC.remove(removeFirst);
				}
				collectionCC.insert(document);
				
				return "Success : Values inserted !!"; 
			}
			else 
				return "The device with this endpoint and serial number is already present !";
			}
		catch(Exception e)	 {
			e.printStackTrace();
		}
		return "Values Inserted successfully !!";
	}

	public static String updateLifetime(String id, String newLifetime){
		try
		{					
			
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds053380.mongolab.com:53380/cmpe273");
			MongoClient client;
			client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");
			DBCollection collection = database.getCollection("regdB");

			// { "$set" : { "LifeTime" : "232231"}}
			DBCursor c = collection.find();
			DBObject last = collection.findOne();
			while (c.hasNext()){
				last = c.next();
			}
			String k = last.toString();
			BasicDBObject document = new BasicDBObject();
			document.append("$set", new BasicDBObject().append("LifeTime",newLifetime));
			JSONObject parts = new JSONObject(k);
			BasicDBObject searchQuery1 = new BasicDBObject();
			searchQuery1.put("serialNumber", parts.optString("serialNumber"));
			searchQuery1.put("endPoint", parts.optString("endPoint"));

			collection.update(searchQuery1, document);
		
			c = collection.find();
			last = collection.findOne();
			while (c.hasNext()){
				last = c.next();
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
//		
		return "LifeTime Value Updated !! ";

	}
	
	public static String updateBindingMode(String id, String newBindingMode){
		try
		{
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds053380.mongolab.com:53380/cmpe273");
			MongoClient client;
			client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");
			DBCollection collection = database.getCollection("regdB");
			DBCollection collectionCC = database.getCollection("currentClientsdB");

			DBCursor c = collection.find();
			DBObject last = collection.findOne();
			while (c.hasNext()){
				last = c.next();
			}
			String k = last.toString();

			JSONObject partsData = new JSONObject(k);

			BasicDBObject document = new BasicDBObject();
			document.append("$set", new BasicDBObject().append("BindingMode",newBindingMode));
			BasicDBObject searchQuery = new BasicDBObject().append("endPoint", partsData.optString("endPoint"))
					.append("serialNumber", partsData.optString("serialNumber"));
			collection.update(searchQuery, document);
			
			last = collectionCC.findOne();
			collectionCC.remove(last);
			
			c = collection.find();
			last = collection.findOne();
			while (c.hasNext()){
				last = c.next();
			}
			collectionCC.insert(last);


		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "Binding Mode Updated !!";

	}

	public static String deleteValue(String id) {
		try
		{
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds053380.mongolab.com:53380/cmpe273");
			MongoClient client;
			client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");
			DBCollection collection = database.getCollection("regdB");
			DBCollection collectionCC = database.getCollection("currentClientsdB");

			
			DBCursor c = collection.find();
			DBObject last = collection.findOne();
			while (c.hasNext()){
				last = c.next();
			}

			collection.remove(last);
			last = collectionCC.findOne();
			collectionCC.remove(last);
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "De-registered Successfully !!";
	}
	
	

}
