package com.assignment2.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import javax.ws.rs.core.Response;
import com.sun.jersey.api.client.Client;


public class MainInServer {
	static String output;
	static String tt;
	public static void main(String[] args) {
		try
		{
			System.out.println("Welcome to LWM2M Server Management Console!");
			do {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Choose one of the following : \n1.Device Management \n2.Information Reporting \n3.Exit");
				int val1 = Integer.parseInt(br.readLine());
				int choosedValue ;
				switch (val1){
				case 1 : 
					do
					{
						//System.out.println("\nChoose one of the following : \n\t1. Read \n\t2. Discover "
						//		+ "\n\t3. Write \n\t4. Write Attributes \n\t5. Execute \n\t6. Create "
						//		+ "\n\t7. Delete \n\t8. Exit ");
						System.out.println("\nChoose one of the following : \n\t1. Read \n\t2. Write \n\t3. Execute "
								+ "\n\t4. Create \n\t5. Discover \n\t6. Write Attributes "
								+ "\n\t7. Delete \n\t8. Exit");
						choosedValue = Integer.parseInt(br.readLine());

						// before moving to switch case, lets see if we can insert current client in currentclientdB and also retrieve it.

						switch (choosedValue)
						{
						case 1: //Read
							String sensorChoice="";
							System.out.println("Case 1: Read ");				
							System.out.println("Select the attribute value to be read:");
							System.out.println("\t1. Temperature ");
							System.out.println("\t2. Pressure ");
							Scanner number = new Scanner(System.in);
							int choice = number.nextInt();
							if(choice == 1){
								sensorChoice = "Temperature";
							}
							else if(choice == 2){
								sensorChoice = "Pressure";
							}
							Client client = Client.create();
							WebResource webResource = client.resource("http://localhost:3000/com.rest.client/rest/jsonservicesClient/read/"+sensorChoice);
							ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
							if (response.getStatus() != 200) {
								throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
							}
							output = response.getEntity(String.class); 					// Ouput for jsonServices.
							System.out.println("The current "+sensorChoice+" value is :...."+output);
							break;

						case 5: // discover
							System.out.println("Case 2: Discover");			
							System.out.println("Discovering all registered client devices..");
							client = Client.create();
							webResource = client.resource("http://localhost:3000/com.rest.client/rest/jsonservicesClient/discover");
							response = webResource.accept("application/json").get(ClientResponse.class);
							if (response.getStatus() != 200) {
								throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
							}
							output = response.getEntity(String.class); 					// Ouput for jsonServices.
							System.out.println("Output from Client .... \n");
							System.out.println(output);
							break;

						case 2: 
							System.out.println("Case 3: Write ");
							System.out.println("Choose : \n\t1. Temperature\n\t2. Pressure\n\t3. Mode of Operation"); // this is wrt Fridge
							int val = Integer.parseInt(br.readLine());
							String changingAttribute = "";
							if (val ==1)
								changingAttribute ="Temperature";
							else if (val ==2)
								changingAttribute="Pressure";
							else if (val ==3)
								changingAttribute="Mode";
							System.out.println("Enter the new value ");
							String what = br.readLine();

							client = Client.create();
							URL url = new URL("http://localhost:3000/com.rest.client/rest/jsonservicesClient/write");
							HttpURLConnection conn = (HttpURLConnection) url.openConnection();
							conn.setDoOutput(true);
							conn.setRequestMethod("POST");
							conn.setRequestProperty("Content-Type", "application/json");
							OutputStream os = conn.getOutputStream();
							String k = changingAttribute+","+what;
							os.write(k.getBytes());
							os.flush();
							BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
							String output;
							System.out.println("Output from Client .... \n");
							while ((output = br1.readLine()) != null) {
								System.out.println(output);
							}

							conn.disconnect();				
							break;

						case 6: // write attributes
							System.out.println("Case 4: Write Attributes ");
							System.out.println("Choose : \n\t1. Minimum Period \n\t2. Maximum Period ");
							val = Integer.parseInt(br.readLine());
							changingAttribute = "";
							if (val ==1)
								changingAttribute ="MinimumPeriod";
							else if (val ==2)
								changingAttribute="MaximumPeriod";
							System.out.println("Enter the new period ");
							what = br.readLine();
							client = Client.create();
							url = new URL("http://localhost:3000/com.rest.client/rest/jsonservicesClient/writeAttribute");
							conn = (HttpURLConnection) url.openConnection();
							conn.setDoOutput(true);
							conn.setRequestMethod("POST");
							conn.setRequestProperty("Content-Type", "application/json");
							os = conn.getOutputStream();
							k = changingAttribute+","+what;
							os.write(k.getBytes());
							os.flush();
							br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
							System.out.println("Output from Client .... \n");
							while ((output = br1.readLine()) != null) {
								System.out.println(output);
							}
							conn.disconnect();				
							break;

						case 3: // execute of fridge
							System.out.println("Sending execute instructions to the Device...\n");
							Client client1 = Client.create();
							Client client2 = Client.create();
							WebResource webResource1 = client1.resource("http://localhost:3000/com.rest.client/rest/jsonservicesClient/read/Temperature");
							ClientResponse response1 = webResource1.accept("application/json").get(ClientResponse.class);
							if (response1.getStatus() != 200) {
								throw new RuntimeException("Failed : HTTP error code : "+ response1.getStatus());
							}
							String output1 = response1.getEntity(String.class); 					// Ouput for jsonServices.
							int outputTemp = Integer.parseInt(output1);
							//pressure part
							WebResource webResource2 = client2.resource("http://localhost:3000/com.rest.client/rest/jsonservicesClient/read/Pressure");
							ClientResponse response2 = webResource2.accept("application/json").get(ClientResponse.class);
							if (response2.getStatus() != 200) {
								throw new RuntimeException("Failed : HTTP error code : "+ response2.getStatus());
							}
							String output2 = response2.getEntity(String.class); 					// Ouput for jsonServices.
							int outputPres = Integer.parseInt(output2);
							if (outputPres>57){
								client = Client.create();
								url = new URL("http://localhost:3000/com.rest.client/rest/jsonservicesClient/write");
								conn = (HttpURLConnection) url.openConnection();
								conn.setDoOutput(true);
								conn.setRequestMethod("POST");
								conn.setRequestProperty("Content-Type", "application/json");
								os = conn.getOutputStream();
								k= "Pressure"+","+"40";
								os.write(k.getBytes());
								os.flush();
								br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
								System.out.println("Output from Client .... \n");
								while ((output = br.readLine()) != null) {
									System.out.println(output);
								}
								conn.disconnect();
							}
							else {
								System.out.println("The refrigerator is working on Appropriate Pressure.");
							}

							//temp part
							if (outputTemp<15){			
								client = Client.create();
								URL url1 = new URL("http://localhost:3000/com.rest.client/rest/jsonservicesClient/write");
								URL url2 = new URL("http://localhost:3000/com.rest.client/rest/jsonservicesClient/write");
								HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
								HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
								conn1.setDoOutput(true);
								conn1.setRequestMethod("POST");
								conn1.setRequestProperty("Content-Type", "application/json");
								conn2.setDoOutput(true);
								conn2.setRequestMethod("POST");
								conn2.setRequestProperty("Content-Type", "application/json");
								OutputStream os1 = conn1.getOutputStream();
								OutputStream os2 = conn2.getOutputStream();
								String k1 = "Temperature"+","+"17";
								String k2 = "Mode"+","+"Power Saving";
								os1.write(k1.getBytes());
								os2.write(k2.getBytes());
								os1.flush();
								os2.flush();
								br1 = new BufferedReader(new InputStreamReader((conn1.getInputStream())));
								BufferedReader br2 = new BufferedReader(new InputStreamReader((conn2.getInputStream())));

								System.out.println("Output from Client .... \n");
								while ((output = br1.readLine()) != null) {
									System.out.println(output);
								}

								while ((output = br2.readLine()) != null) {
									System.out.println(output);
								}
								conn1.disconnect();
								conn2.disconnect();
							}
							else {
								System.out.println("The refrigerator is working on Appropriate Temperature.");
							}
							break;

						case 4: //create
							System.out.println("Choose one of the OMA objects to create \n");
							System.out.println("\t1. Security Object \n\t2. Server Object \n\t3.Location Monitoring Device Object\n");
							int opn = Integer.parseInt(br.readLine());
							if(opn == 3){
							client = Client.create();
							webResource = client.resource("http://localhost:3000/com.rest.client/rest/jsonservicesClient/create");
							response = webResource.accept("application/json").get(ClientResponse.class);
							if (response.getStatus() != 200) {
								throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
							}
							output = response.getEntity(String.class); 					// Ouput for jsonServices.
							System.out.println("Response from Client .... \n");
							System.out.println(output);
							}
							else{								
							}
							break;

						case 7: //delete
							client = Client.create();
							webResource = client.resource("http://localhost:3000/com.rest.client/rest/jsonservicesClient/delete");
							response = webResource.accept("application/json").get(ClientResponse.class);
							if (response.getStatus() != 200) {
								throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
							}
							output = response.getEntity(String.class); // Ouput for jsonServices.
							System.out.println("Response from Client .... \n");
							System.out.println(output);
							break;
						case 8 :
							break;
						}
					}while(true&&choosedValue!=8);					

				case 2 : 
					System.out.println("case 2. Information Reporting");					
					System.out.println("Choose one of the following : \n1.Observe \n2.Cancel Observation \n3.Exit");
					int k = Integer.parseInt(br.readLine());					
					switch(k){
					case 1 : //observe
						Client client = Client.create(); 
						WebResource webResource = client.resource("http://localhost:3000/com.rest.client/rest/jsonservicesClient/observe");
						ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
						if (response.getStatus() != 200) {
							throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
						}
						output = response.getEntity(String.class);		// Ouput for jsonServices.						
						
						System.out.println("Observation completed..\n");
						break;
					case 2 : // cancel observation
						 client = Client.create();
						 
						 webResource = client.resource("http://localhost:3000/com.rest.client/rest/jsonservicesClient/cancel");
						 response = webResource.accept("application/json").get(ClientResponse.class);
						if (response.getStatus() != 200) {
							throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
						}
						output = response.getEntity(String.class); 					// Ouput for jsonServices.
						
						
						System.out.println("Cancelled Observation..\n");
						break;
					case 3 : 
						System.out.println("Out of Information Reporting..");
						break;
					
					}
					

				case 3 : 
					System.out.println("You choose to exit.\nThank you !");
					System.exit(0);
					break;
				}
			}while(true);
		}// end of try
		catch(Exception e){
			e.printStackTrace();
		}//end of catch

	}// end of main


}//end of class
