package org.e8.whois.resource;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.e8.whois.configuration.WhoIsConfiguration;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.yammer.dropwizard.config.Configuration;



@Path("/whois")
public class IPResource {

	private WhoIsConfiguration conf;
	
	public IPResource(WhoIsConfiguration conf){
		this.conf=conf;
	}
	
	
	@GET
	@Path("/ip")
	//@Produces(MediaType.APPLICATION_XML)
	public String getWhoIsIP(@QueryParam("ip") String ipAddress) throws IOException{
		return ResourceCacheDB.getResponseFromCache(ipAddress,conf);
	}
}
