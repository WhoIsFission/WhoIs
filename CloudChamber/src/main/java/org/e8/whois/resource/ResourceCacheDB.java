package org.e8.whois.resource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;

import org.e8.whois.cache.WhoisCacheTree;
import org.e8.whois.configuration.WhoIsConfiguration;
import org.e8.whois.dao.DAOFactory;
import org.e8.whois.dao.IpWhoisDAO;
import org.e8.whois.dao.impl.IpWhoisDAOImpl;
import org.e8.whois.model.Organisation;
import org.e8.whois.model.OrganisationAbuse;
import org.e8.whois.model.OrganisationTech;
import org.e8.whois.model.WhoIsNode;
import org.e8.whois.parser.WhoIsParser;

import com.yammer.dropwizard.config.Configuration;

public class ResourceCacheDB {
	
	private Configuration conf;
	//WhoIsNode<Long>
	public static String getResponseFromCache(String ipAddress,final WhoIsConfiguration conf) throws IOException, JAXBException{
		
		final WhoisCacheTree cache=WhoisCacheTree.getCacheInstance();
		
		Long ip=ipToLong(ipAddress);
		WhoIsNode node=cache.searchSpecificInterval(ip);
		if(node!=null){
		//return node;
			return buildResponse(node);
		}
		final WhoIsNode<Long> responseNode=WhoIsParser.parseWhoIsResponse(ipAddress);

		// To start thread for persisting into DB
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
		
		//To start thread for building cache
		new Thread(new Runnable(){

			public void run() {
				// TODO Auto-generated method stub
				buildCache(responseNode,cache);
			}
			
		}).start();
		
		
		//return responseNode;
		
		return buildResponse(responseNode);
		
	}
	
	/**
	 * Persisting into DB
	 * 
	 * @param responseNode
	 * @throws Exception 
	 */
	private static void persistToDB(WhoIsNode<Long> aNode,WhoIsConfiguration conf) throws Exception{
		IpWhoisDAO whoisDAO=DAOFactory.getDAO(conf);
		if(whoisDAO!=null){
		whoisDAO.updateWhoisByIpToHistoric(aNode);
		whoisDAO.insertWhoisByIp(aNode);
		}
	}
	/**
	 * Building cache
	 * 
	 * @param responseNode
	 */
	private static void buildCache(WhoIsNode<Long> aNode,WhoisCacheTree<Long> cache){
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
	 
	 private static String buildResponse(WhoIsNode<Long> node) throws JAXBException, IOException{
		 
		 //TODO building response based on the format of responseNode
		 JAXBContext jaxbContext=JAXBContext.newInstance(new Class[]{WhoIsNode.class,Organisation.class,
				 												OrganisationAbuse.class,OrganisationTech.class});
		 Marshaller marshaller=jaxbContext.createMarshaller();
		 ByteArrayOutputStream os=new ByteArrayOutputStream();
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
		 return strBuf.toString();
	 }

}
