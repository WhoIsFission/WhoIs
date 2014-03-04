package org.e8.whois.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.e8.whois.configuration.WhoIsConfiguration;
import org.e8.whois.exceptionHandling.WhoIsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




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
