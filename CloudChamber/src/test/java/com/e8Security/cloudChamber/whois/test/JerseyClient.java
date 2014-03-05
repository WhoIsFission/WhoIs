package com.e8Security.cloudChamber.whois.test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class JerseyClient {
	
	public static void main(String[] args) {
		
		        try {
		            Client client = Client.create();
		
		 
		
		            WebResource webResource = client
		
		                    .resource("http://localhost:8080/whois/ip?ip=183.83.137.50");
		
		            ClientResponse response = webResource.accept("application/xml")
		
		                    .get(ClientResponse.class);
		
		            if (response.getStatus() != 200) {
				                throw new RuntimeException("Failed : HTTP error code : "
		
		                        + response.getStatus());
		
		            }
		            String output = response.getEntity(String.class);
		
		 
		
		            System.out.println("Server response : \n");
		
		            System.out.println(output);
	
		 
	
		        } catch (Exception e) {
	
		 
	
		            e.printStackTrace();
	
		 
	
		        }
	
		 
	
		    }


}
