package com.e8security.cloudchamber.whois.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e8security.cloudchamber.whois.configuration.WhoIsConfiguration;
import com.e8security.cloudchamber.whois.exceptionHandling.WhoIsException;




@Path("/whois")
public class IPResource {

	private WhoIsConfiguration conf;
	private static Logger Resource_Logger=LoggerFactory.getLogger(IPResource.class);
	public IPResource(WhoIsConfiguration conf){
		this.conf=conf;
	}
	
	
	@GET
	@Path("/ip")
	public String getWhoIsIP(@QueryParam("ip") String ipAddress) throws WhoIsException  {
		if(Resource_Logger.isDebugEnabled())
		Resource_Logger.debug("Executing Whois IP Resource");
			return ResourceCacheDB.getResponseFromCache(ipAddress,conf)	;
			}
}
