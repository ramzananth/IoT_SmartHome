package com.rest.client;

import java.sql.Timestamp;
import java.util.Random;

import org.codehaus.jettison.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.util.JSON;

public class RandomServices {
	static String min = "1";	
	static String max = "5";
	static void randomFridge(String details) throws Exception
	{
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(30);//temp
		Random randomGenerator2 = new Random();
		int randomInt2 = randomGenerator2.nextInt(115);//pressure
		MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031802.mongolab.com:31802/cmpe273");
		MongoClient client;
		client = new MongoClient(mongoLab);
		java.util.Date date= new java.util.Date();
		JSONObject partsData = new JSONObject(details);

		int find = details.lastIndexOf(",");
		String Serial = details.substring(find+1,details.length());
		String endPoint= partsData.optString("endPoint");
		String firmware= partsData.optString("firmware");
		String manufacturer=partsData.optString("manufacturer");

		BasicDBObject document = new BasicDBObject();
		document.put("serialNo", Serial);
		document.put("endPoint",endPoint );
		document.put("Mode",partsData.optString("Mode"));
		document.put("firmware",firmware );      
		document.put("manufacturer",manufacturer );
		document.put("Temperature",randomInt );
		document.put("Pressure",randomInt2 );
		document.put("MinimumPeriod", min);
		document.put("MaximumPeriod", max);
		document.put("Timestamp",new Timestamp(date.getTime()) );

		//String temp="{\"Object\":\"fridge\",\"timeStamp\":\""+new Timestamp(date.getTime())+"\",\"Resource\":{\"serial\":\"7891\",\"Temprature\":"+randomInt+",\"Pressure\":"+randomInt2+"}}";
		DB database = client.getDB("cmpe273");
		DBCollection collection = database.getCollection("ClientFridge"); 
		//	    DBObject dbObject = (DBObject)JSON.parse(temp);
		collection.insert(document);

	}

	static void randomCarbonSensor(String details) throws Exception
	{
		Random randomGenerator = new Random();//carbon
		//int randomInt = randomGenerator.nextInt(2000);
		int randomInt = randomGenerator.nextInt(2000 - 100 + 1) +100;
		Random randomGenerator2 = new Random();//nito
		int randomInt2 = randomGenerator2.nextInt(150);
		MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031802.mongolab.com:31802/cmpe273");
		MongoClient client;
		client = new MongoClient(mongoLab);
		java.util.Date date= new java.util.Date();

		//String temp="{\"Object\":\"CarbonSensor\",\"timeStamp\":\""+new Timestamp(date.getTime())+"\",\"Resource\":{\"serial\":\"7891\",\"CarbondioxideLevel\":"+randomInt+",\"NitrogendioxideLevel\":"+randomInt2+"}}";
		JSONObject partsData = new JSONObject(details);



		String endPoint= partsData.optString("endPoint");
		String firmware= partsData.optString("firmware");
		String manufacturer=partsData.optString("manufacturer");

		BasicDBObject document = new BasicDBObject();
		document.put("endPoint",endPoint );
		document.put("firmware",firmware );
		document.put("manufacturer",manufacturer );
		document.put("CarbondioxideLevel",randomInt );
		document.put("NitrogendioxideLevel",randomInt2 );
		document.put("Timestamp",new Timestamp(date.getTime()) );
		DB database = client.getDB("cmpe273");
		DBCollection collection = database.getCollection("ClientCarbonSensor"); 
		//DBObject dbObject = (DBObject)JSON.parse(temp);
		collection.insert(document);


	}
	static void randomAC(String details) throws Exception
	{
		Random randomGenerator = new Random();//temp
		int randomInt = randomGenerator.nextInt(35);

		boolean motion = Math.random() < 0.5;//motion

		MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031802.mongolab.com:31802/cmpe273");
		MongoClient client;
		client = new MongoClient(mongoLab);
		java.util.Date date= new java.util.Date();
		JSONObject partsData = new JSONObject(details);



		String endPoint= partsData.optString("endPoint");
		String firmware= partsData.optString("firmware");
		String manufacturer=partsData.optString("manufacturer");

		BasicDBObject document = new BasicDBObject();
		document.put("endPoint",endPoint );
		document.put("firmware",firmware );
		document.put("manufacturer",manufacturer );
		document.put("Temperature",randomInt );
		document.put("Motion",motion );
		document.put("Timestamp",new Timestamp(date.getTime()) );
		//String temp="{\"Object\":\"CarbonSensor\",\"timeStamp\":\""+new Timestamp(date.getTime())+"\",\"Resource\":{\"serial\":\"7891\",\"Temprature\":"+randomInt+",\"MotionSensor\":"+motion+"}}";

		DB database = client.getDB("cmpe273");
		DBCollection collection = database.getCollection("ClientAC"); 
		//DBObject dbObject = (DBObject)JSON.parse(temp);
		collection.insert(document);

	}



}