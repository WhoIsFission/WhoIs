package org.e8.whois.resource;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.xml.bind.JAXBException;

import org.e8.whois.configuration.WhoIsConfiguration;
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
	public String getWhoIsIP(@QueryParam("ip") String ipAddress)  {
		//response.setContentType("text/xml");
		Resource_Logger.info("executed Resources");
		try {
			return ResourceCacheDB.getResponseFromCache(ipAddress,conf)	;
		} catch (IOException e) {
			// TODO Auto-generated catch block
throw new WebApplicationException(e); 
} catch (JAXBException e) {
			// TODO Auto-generated catch block
	throw new WebApplicationException(e); 
		}
	}
}
