package com.rest.client;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MongoServiceClient {

	public static void main(String args[]) throws Exception{ // just to test below methods
		System.out.println("first statmetn   : "+ readAttribute("Temprature"));
		System.out.println("Inside the main of MongoServicceClient!");
		System.out.println("Calling readAttribute..");
		MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds053380.mongolab.com:53380/cmpe273");
		MongoClient client = new MongoClient(mongoLab);
		DB database = client.getDB("cmpe273");		
		DBCollection collection = database.getCollection("regdB");
		BasicDBObject query = new BasicDBObject();
		query.put("endPoint", "1002");
		query.put("serialNumber", "9876");
		DBObject d = collection.findOne(query); // because there can only be one device with unique endpoint, oid
		System.out.println("output : "+d);
		if (d.containsField("Life"))
			System.out.println("yes");
		else
			System.out.println("no");
		DBObject qu = new BasicDBObject("LifeTime", new BasicDBObject("$exists", true));
		DBObject qu1 = collection.findOne(qu);
		System.out.println("printing when lifetime is present : "+qu1);

		//		DBObject a = collection.findOne(new BasicDBObject(),new BasicDBObject("serialNumber",true).append("_id", 0));
		//		System.out.println(a);

		System.out.println(d); // null 
		System.out.println(qu);
		if (d == null){
			BasicDBObject query1 = new BasicDBObject();
			query1.put("endPoint", "1002");
			query1.put("serialNumber", "9876");
			d = collection.findOne(query1);
			System.out.println("inside if"+d);
		}
		else {
			DBObject a = collection.findOne(new BasicDBObject(),new BasicDBObject("LifeTime",true).append("_id", 0));

		}
		//		String k = readAttribute("1002","9876","LifeTime");
		//		System.out.println("final syo : "+k);
		//		JSONObject jo = new JSONObject (k);
		//		System.out.println(jo);
		//		System.out.println("LifeTime"+ " : "+(String) d.get("LifeTime"));

	}

	public static String readAttribute(String end) {// can be lifeline
		try
		{
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031802.mongolab.com:31802/cmpe273");
			MongoClient client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");		
			DBCollection collection = database.getCollection("ClientFridge");
			DBCursor dcursor = collection.find();
			dcursor.sort(new BasicDBObject("Timestamp",-1));
			String d="";
			if (dcursor.hasNext()){
				d = dcursor.next().get(end).toString();
			}
			return d;           
		}// end of try block 
		catch (Exception e){
			e.printStackTrace();
			return "Error in the read function.";
		} 
	}

	public static String write (String write, String what){
		try
		{
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031802.mongolab.com:31802/cmpe273");
			MongoClient client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");		
			DBCollection collection = database.getCollection("ClientFridge");
			DBCursor dcursor = collection.find();
			dcursor.sort(new BasicDBObject("Timestamp",-1));

			DBObject t=null;
			if (dcursor.hasNext()){
				t = dcursor.next(); //latest client json				
			}
			String k = t.toString();
			BasicDBObject document = new BasicDBObject();
			document.append("$set", new BasicDBObject().append(write,what));
			JSONObject parts = new JSONObject(k);
			BasicDBObject searchQuery1 = new BasicDBObject();
			searchQuery1.put("Timestamp", parts.optString("Timestamp"));
			BasicDBObject jj = (BasicDBObject)t;
			collection.update(jj, document);
			return "Attribute Value Changed Successfully !! ";			
		}// end of try block 
		catch (Exception e){
			e.printStackTrace();
			return "Error in the write function.";
		}		
	}

	public static String writeAttribute (String write, String what){
		try
		{
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031802.mongolab.com:31802/cmpe273");
			MongoClient client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");		
			DBCollection collection = database.getCollection("ClientFridge");
			DBCursor dcursor = collection.find();
			dcursor.sort(new BasicDBObject("Timestamp",-1));

			DBObject t=null;
			if (dcursor.hasNext()){
				t = dcursor.next(); //latest client json				
			}
			String k = t.toString();
			BasicDBObject document = new BasicDBObject();
			document.append("$set", new BasicDBObject().append(write,what));
			JSONObject parts = new JSONObject(k);
			BasicDBObject searchQuery1 = new BasicDBObject();
			searchQuery1.put("Timestamp", parts.optString("Timestamp"));
			BasicDBObject jj = (BasicDBObject)t;
			collection.update(jj, document);
			return "Attribute "+write+" value is modified successfully !";			
		}// end of try block 
		catch (Exception e){
			e.printStackTrace();
			return "Error in the writeAttribute function.";
		}	

	}
	public static void deleteObject(){
		try{
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031802.mongolab.com:31802/cmpe273");
			MongoClient client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");	
			DBCollection collection = database.getCollection("ClientResourceFridge");
			DBCursor dcursor = collection.find();
			DBObject t=null;
			if (dcursor.hasNext()){
				t = dcursor.next(); //latest client json	
			}	
			BasicDBObject query = (BasicDBObject)t;

			DBObject update = new BasicDBObject();
			update.put("$unset", new BasicDBObject("LocationMonitor",""));
			WriteResult result = collection.update(query, update);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void createObject(){
		try{
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031802.mongolab.com:31802/cmpe273");
			MongoClient client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");	
			DBCollection collection = database.getCollection("ClientResourceFridge");	
			DBCursor dcursor = collection.find();
			//dcursor.sort(new BasicDBObject("Timestamp",-1));
			DBObject t=null;
			if (dcursor.hasNext()){
				t = dcursor.next(); //latest client json	
			}
			BasicDBObject query = (BasicDBObject)t;
			BasicDBObject obj = new BasicDBObject();
			obj.put("Latitude", "320.0");
			obj.put("Longitude", "452.0");
			BasicDBObject create = new BasicDBObject();
			create.put("$push", new BasicDBObject("LocationMonitor",obj));
			collection.update(query,create);
		}
		catch(Exception e){	
			System.out.println("Error in the create Object function");
			e.printStackTrace();	
		}	
	}
	public static void insertResource(String details) {
		try
		{
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031802.mongolab.com:31802/cmpe273");
			MongoClient client;
			client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");
			DBCollection collection = database.getCollection("ClientResourceFridge"); 

			int find = details.lastIndexOf(",");
			String Serial = details.substring(find+1,details.length());
			JSONObject partsData = new JSONObject(details);
			String endPoint= partsData.optString("endPoint");
			String firmware= partsData.optString("firmware");
			String manufacturer=partsData.optString("manufacturer");

			BasicDBObject document = new BasicDBObject();
			document.put("serialNo", Serial);
			document.put("endPoint",endPoint );
			document.put("Mode",partsData.optString("Mode"));
			document.put("firmware",firmware );      
			document.put("manufacturer",manufacturer );
			document.put("Temperature","23" );
			document.put("Pressure","89" );
			document.put("MinimumPeriod", "1");
			document.put("MaximumPeriod", "5");
			collection.insert(document);
		}
		catch(Exception e)
		{
			System.out.println("Error in the insert Resource function");
			e.printStackTrace();
		}
	}


	public static void deleteResource() 
	{
		try{
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031802.mongolab.com:31802/cmpe273");
			MongoClient client;
			client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");
			DBCollection collection = database.getCollection("ClientResourceFridge"); 
			BasicDBObject document = new BasicDBObject();
			collection.remove(document);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String discover() {
		try{
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031802.mongolab.com:31802/cmpe273");
			MongoClient client;
			client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");
			DBCollection collection = database.getCollection("ClientResourceFridge"); 
			DBObject one = collection.findOne();
			JsonFactory factory = new JsonFactory();
			ObjectMapper mapper = new ObjectMapper(factory);
			JsonNode rootNode = mapper.readTree(one.toString());  
			Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.getFields();
			String returnString = "Current Attributes of the device are : \n";
			while (fieldsIterator.hasNext()) {
				Map.Entry<String,JsonNode> field = fieldsIterator.next();
				returnString+=" > " + field.getKey()+"\n";				
			}
			return returnString;
		}
		catch (Exception e)		{
			e.printStackTrace();
			return "Error in the discover function..";
		}
	}
	public static String observe() {
		try{
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031802.mongolab.com:31802/cmpe273");
			MongoClient client;
			client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");
			DBCollection collection = database.getCollection("ClientFridge");		
			DBObject maxx = collection.findOne();
			String max = (String)maxx.get("MaximumPeriod");		
			String min = (String)maxx.get("MinimumPeriod");		
			int some =Integer.parseInt(min);
			int convertmax = Integer.parseInt(max);
			int i =0;
			DBCollection collectionResource=database.getCollection("ClientResourceFridge"); ;
			DBObject abc;
			collectionResource = database.getCollection("ClientResourceFridge");
			abc = collectionResource.findOne();		
			BasicDBObject query = (BasicDBObject)abc;		
			BasicDBObject doc = new BasicDBObject("$set" , new BasicDBObject().append("Mode", "ObserveMode"));
			collectionResource.update(query, doc);
			Thread.sleep(some);
			String output="";
			while(i < convertmax)
			{
				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(30);				
				Client client1 = Client.create();								
				WebResource webResource = client1.resource("http://localhost:8080/com.assignment2.server/rest/jsonservices/notify/"+randomInt);
				ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
				}
				java.util.Date date= new java.util.Date();
				output = response.getEntity(String.class); 	
				System.out.println("Notifying from Client to the Server: "+new Timestamp(date.getTime())+" > "+"Temperature : "+output);
				if(output.equalsIgnoreCase("N")){
					collectionResource = database.getCollection("ClientResourceFridge");
					abc = collectionResource.findOne();		
					BasicDBObject query1 = (BasicDBObject)abc;		
					BasicDBObject doc1 = new BasicDBObject("$set" , new BasicDBObject().append("Mode", "Default"));
					collectionResource.update(query1, doc1);
					break;
				}
				Thread.sleep(3000);
				i++;
			}
			return output;

		}
		catch(Exception e)		{
			e.printStackTrace();
			return "Error in the observe function !!";
		}
	}

	public static String cancel() {
		try
		{
			MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031802.mongolab.com:31802/cmpe273");
			MongoClient client;
			client = new MongoClient(mongoLab);
			DB database = client.getDB("cmpe273");
			DBCollection collectionResource=database.getCollection("ClientResourceFridge"); ;
			DBObject abc;

			collectionResource = database.getCollection("ClientResourceFridge");
			abc = collectionResource.findOne();		
			BasicDBObject query1 = (BasicDBObject)abc;		
			BasicDBObject doc1 = new BasicDBObject("$set" , new BasicDBObject().append("Mode", "default"));
			collectionResource.update(query1, doc1);
			return "Cancelled !";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Error in the cancel function!";
		}
	}
}
