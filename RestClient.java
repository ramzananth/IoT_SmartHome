package com.rest.client;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

// client 
public class RestClient { 

	static int tempForRegistration;
	static String output;
	static String outputForReg;
	static String temp;
	static int val =0;


	public static void main(String[] args) {
		 String randomData="";
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String checkCondition;
			System.out.println("Welcome to Sam's IOT distributive System !!");

			Client client = Client.create();
			do
			{
				System.out.println("1. BootStrap \n2. Registration(Register, Update & De-Register) \n3. Device Management \n4. Information Reporting");
				String id = bufferRead.readLine();
				if (Check.isNumber(id))
					val = Integer.parseInt(id);


				switch(val)
				{
				case 1: //BootStrap
					System.out.println("Case 1 : Bootstrap");
					System.out.println("Enter the EndPoint of the device : "); // enter 1001
					temp = bufferRead.readLine();

					WebResource webResource = client.resource("http://localhost:8080/com.assignment2.server/rest/jsonservices/print/"+temp);
					ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
					if (response.getStatus() != 200) {
						throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
					}

					output = response.getEntity(String.class); 					// Ouput for jsonServices.
					System.out.println("Output from Server .... \n");
					System.out.println(output);
					outputForReg=output;
					randomData=output;
					
					break;

				case 2: // Registration
					System.out.println("What would you like to do ?\nPlease make a choice : "
							+ "\nPress \t1 for Registering the device\n\t2 for Updating the device\n"
							+ "\t3 for De-Register the device");
					int choice = Integer.parseInt(bufferRead.readLine());
					switch(choice)
					{
					case 1 : 
						
						System.out.println("Are you sure you want to register : Y/N ?");
						if (bufferRead.readLine().equalsIgnoreCase("y"))
						{
							System.out.println("Enter the serial number : ");
							String serial = bufferRead.readLine();
							
							URL url = new URL("http://localhost:8080/com.assignment2.server/rest/jsonservices/register/registerDevice");
							HttpURLConnection conn = (HttpURLConnection) url.openConnection();
							conn.setDoOutput(true);
							conn.setRequestMethod("POST");
							conn.setRequestProperty("Content-Type", "application/json");
							OutputStream os = conn.getOutputStream();
							String k = outputForReg+serial+",http://localhost:3000/com.rest.client/rest/jsonservicesClient/";
							os.write(k.getBytes());
							os.flush();
							BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
							String output;
							System.out.println("Output from Server .... \n");
							while ((output = br.readLine()) != null) {
								System.out.println(output);
							}							
							MongoServiceClient.insertResource(randomData+","+serial);
							int i = 0;
							while (i<5){
							RandomServices.randomFridge(randomData+","+serial);
							i++;
							}
							conn.disconnect();
						}
						else
						{Check.stopTheProgram();}
						break;

					case 2: 
						System.out.println("Press 1 to change the LifeTime attribute \nPress 2 to update the Binding Mode ");
						int g = Integer.parseInt(bufferRead.readLine()); 

						if (g==1)
						{
							System.out.println("Enter the new Lifetime attribute : ");
							int newLifetime = Integer.parseInt(bufferRead.readLine());
							String copy = outputForReg+newLifetime;

							//							outputForReg+=newLifetime;

							URL url = new URL("http://localhost:8080/com.assignment2.server/rest/jsonservices/register/updateDevice/lifetime");
							HttpURLConnection conn = (HttpURLConnection) url.openConnection();
							conn.setDoOutput(true);
							conn.setRequestMethod("PUT");
							conn.setRequestProperty("Content-Type", "application/json");
							OutputStream os = conn.getOutputStream();
							os.write(copy.getBytes());
							os.flush();
							BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
							String output;
							System.out.println("Output from Server .... \n");
							while ((output = br.readLine()) != null) {
								System.out.println(output);

							}
							conn.disconnect();
						}
						else if (g==2)
						{
							System.out.println("Enter the new Binding Mode attribute : ");
							String newBinding = bufferRead.readLine();
							String copy = outputForReg+newBinding;

							URL url = new URL("http://localhost:8080/com.assignment2.server/rest/jsonservices/register/updateDevice/bindingMode");
							HttpURLConnection conn = (HttpURLConnection) url.openConnection();
							conn.setDoOutput(true);
							conn.setRequestMethod("PUT");
							conn.setRequestProperty("Content-Type", "application/json");
							OutputStream os = conn.getOutputStream();
							os.write(copy.getBytes());
							os.flush();
							BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
							String output;
							System.out.println("Output from Server .... \n");
							while ((output = br.readLine()) != null) {
								System.out.println(output);

							}
							conn.disconnect();
						}
						break;

					case 3 : // for de-registering a device
						System.out.println("Are you sure you want to de-register the device ?");
						String value = bufferRead.readLine();

						if (value.equalsIgnoreCase("y")||value.equalsIgnoreCase("yes")){
							URL url = new URL("http://localhost:8080/com.assignment2.server/rest/jsonservices/register/delete/"+temp);
							HttpURLConnection conn = (HttpURLConnection) url.openConnection();
							conn.setDoOutput(true);
							conn.setRequestMethod("DELETE");
							conn.setRequestProperty("Content-Type", "application/json");

							BufferedReader br = new BufferedReader(new InputStreamReader(
									(conn.getInputStream())));

							String output;
							System.out.println("Output from Server .... \n");
							while ((output = br.readLine()) != null) {
								System.out.println(output);

							}
							MongoServiceClient.deleteResource();
							conn.disconnect();
						}

					}

					break;

				case 3: // Device Management
					System.out.println("In Case 2");
					break;

				case 4: // Information Reporting
					System.out.println("In Case 2");
					break;

				case 5: 
					System.out.println("You entered other than 1, 2, 3, 4");
					break;

				}
				System.out.println("Do you want to continue : Y/N ?");
				checkCondition = bufferRead.readLine();
			}while(checkCondition.equalsIgnoreCase("y"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		Check.stopTheProgram();
	}
}