package org.e8.whois.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.e8.whois.configuration.WhoIsConfiguration;
import org.e8.whois.model.WhoIsNode;



@Path("/whois")
//@Produces(MediaType.TEXT_XML)
public class IPResource {

	private WhoIsConfiguration conf;
	
	public IPResource(WhoIsConfiguration conf){
		this.conf=conf;
	}
	
	
	@GET
	@Path("/ip")
	//@Produces(MediaType.TEXT_XML)
	public String  getWhoIsIP(@QueryParam("ip") String ipAddress) throws Exception{
		return ResourceCacheDB.getResponseFromCache(ipAddress,conf);
	}
}
