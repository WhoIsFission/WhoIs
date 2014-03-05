package com.e8Security.cloudChamber.whois.resource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e8Security.cloudChamber.whois.cache.WhoisCacheTree;
import com.e8Security.cloudChamber.whois.configuration.WhoIsConfiguration;
import com.e8Security.cloudChamber.whois.dao.DAOFactory;
import com.e8Security.cloudChamber.whois.dao.IpWhoisDAO;
import com.e8Security.cloudChamber.whois.exceptionHandling.WhoIsException;
import com.e8Security.cloudChamber.whois.model.Organisation;
import com.e8Security.cloudChamber.whois.model.OrganisationAbuse;
import com.e8Security.cloudChamber.whois.model.OrganisationAdmin;
import com.e8Security.cloudChamber.whois.model.OrganisationTech;
import com.e8Security.cloudChamber.whois.model.Route;
import com.e8Security.cloudChamber.whois.model.WhoIsNode;
import com.e8Security.cloudChamber.whois.parser.WhoIsParser;
import com.yammer.dropwizard.config.Configuration;

/***
 * Resource Cache DB is used for querying local cache tree and return JAXB marshalled whois node. 
 * Otherwise query respective RIR for getting response before persisting them to DB and to build
 *  cache.
 * 
 * @author Abhijit
 *
 */
public class ResourceCacheDB {
	
	private final static Logger logger=LoggerFactory.getLogger(ResourceCacheDB.class);
	private Configuration conf;
	
	/**
	 * Gets response from cache tree for a given IP address. If not found then searches in RIRs and finally store them in DB and cache.
	 * 
	 * 
	 * @param ipAddress
	 * @param conf
	 * @return response xml
	 * @throws WhoIsException 
	 * 
	 */
	public static String getResponseFromCache(String ipAddress,final WhoIsConfiguration conf) throws WhoIsException{
		if(logger.isDebugEnabled())
		logger.debug("Started building response object from cache tree");
		
		final WhoisCacheTree cache=WhoisCacheTree.getCacheInstance();
		
		Long ip=ipToLong(ipAddress);
		WhoIsNode node=cache.searchSpecificInterval(ip);
		if(node!=null){
		//return node;
			return buildResponse(node);
		}
		
		if(logger.isDebugEnabled())
		logger.debug("Cache miss. Parsing whois information by calling whois client");
		
		final WhoIsNode<Long> responseNode=WhoIsParser.parseWhoIsResponse(ipAddress);

		
		if(responseNode!=null){
			// To start thread for persisting into DB
			if(logger.isDebugEnabled())
			logger.debug("Persisting to DB by spawning a new thread");
			
			new Thread(new Runnable(){

				public void run() {
					// TODO Auto-generated method stub
					try {
						persistToDB(responseNode,conf);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}).start();

			if(logger.isDebugEnabled())
			logger.debug("Inserting whois node into cache tree");
			//To start thread for building cache
			new Thread(new Runnable(){

				public void run() {
					// TODO Auto-generated method stub
					buildCache(responseNode,cache);
				}

			}).start();
			
			if(logger.isDebugEnabled())
			logger.debug("Returning Built response object");
			
		return buildResponse(responseNode);
		}
		
		if(logger.isDebugEnabled())
		logger.debug("Returning Built response object");
		
		return null;
	}
	
	/**
	 * Persisting into DB
	 * 
	 * @param responseNode
	 * @throws WhoIsException 
	 * 
	 */
	private static void persistToDB(WhoIsNode<Long> aNode,WhoIsConfiguration conf) throws WhoIsException {
		if(logger.isDebugEnabled())
		logger.debug("Started persisting into DB");
		
		IpWhoisDAO whoisDAO=DAOFactory.getDAO(conf);
		if(whoisDAO!=null){
		whoisDAO.updateWhoisByIpToHistoric(aNode);
		whoisDAO.insertWhoisByIp(aNode);
		
		if(logger.isDebugEnabled())
		logger.debug("Node information has been persisted.");
		}
	}
	/**
	 * Building cache
	 * 
	 * @param responseNode
	 */
	private static void buildCache(WhoIsNode<Long> aNode,WhoisCacheTree<Long> cache){
		if(logger.isDebugEnabled())
		logger.debug("Node information is being inserted into cache tree");
		
		cache.insert(aNode);
	}
	
	/**
	 * To convert IP to long
	 * @param ipAddress as String in X.X.X.X
	 * @return ip in Long
	 * 
	 */
	 private static Long ipToLong(String ipAddress) {
		 
			long result = 0;
		 
			String[] ipAddressInArray = ipAddress.split("\\.");
		 
			for (int i = 3; i >= 0; i--) {
		 
				long ip = Long.parseLong(ipAddressInArray[3 - i]);
		 
				//left shifting 24,16,8,0 and bitwise OR
		 
				//1. 192 << 24
				//1. 168 << 16
				//1. 1   << 8
				//1. 2   << 0
				result |= ip << (i * 8);
		 
			}
		 
			return result;
		  }
	 
	/**
	 * 
	 * To convert Long to ip address
	 * 
	 * @param Ip in Long
	 * @return ip address as String
	 */
	 private static String longToIp(long i) {
			 
			return ((i >> 24) & 0xFF) + 
	                   "." + ((i >> 16) & 0xFF) + 
	                   "." + ((i >> 8) & 0xFF) + 
	                   "." + (i & 0xFF);
	 
		}
	 
	 /*
	  * Private method to build response by using JAXB marshalling.
	  * 
	  */
	 private static String buildResponse(WhoIsNode<Long> node) throws WhoIsException{
		 
		 //TODO building response based on the format of responseNode
			if(logger.isDebugEnabled()) 
		 logger.debug("Started building response.");
		try{	
		 JAXBContext jaxbContext=JAXBContext.newInstance(new Class[]{WhoIsNode.class,Organisation.class,
				 												OrganisationAbuse.class,OrganisationTech.class,OrganisationAdmin.class,Route.class});
		 Marshaller marshaller=jaxbContext.createMarshaller();
		 ByteArrayOutputStream os=new ByteArrayOutputStream();
		 marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		 marshaller.marshal(node, os);
		 byte[] bytes=os.toByteArray();
		 os.close();
		 BufferedReader bufReader=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
		 StringBuffer strBuf=new StringBuffer();
		 String str;
		 while((str=bufReader.readLine())!=null){
			 strBuf.append(str);
			 strBuf.append(System.getProperty("line.separator"));
		 }
		 
			if(logger.isDebugEnabled())
		 logger.debug("Built response object");
			
		 return strBuf.toString();
		 }catch(IOException e){
			 if(logger.isErrorEnabled())
					logger.error("I/O exception while writing/reading to/from output source during xml conversion");
				throw new WhoIsException("I/O exception while writing/reading to/from output source during xml conversion",e);
		 }catch(JAXBException e){
			 if(logger.isErrorEnabled())
					logger.error("JAXB marshalling exception");
				throw new WhoIsException("Exception converting object to xml",e);
		 }
	 }

}
