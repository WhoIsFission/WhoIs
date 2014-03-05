package com.e8Security.cloudChamber.whois.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

/***
 * Builder class for getting various parser depending on respective RIR.
 * 
 * @author Abhijit
 *
 */
public class ParserBuilder {
	private final static String ARIN="arin";
	private final static String AFRINIC="afrinic";
	private final static String APNIC="apnic";
	private final static String LACNIC="lacnic";
	private final static String RIPE="ripe";
	private final static Logger logger=LoggerFactory.getLogger(ParserBuilder.class);
	
	/**
	 * Fetching parser for a given RIR
	 * 
	 * @param aResponse
	 * @return Parser
	 * @throws IOException
	 */
	public static Parser getParser(String aResponse) throws IOException{
		if(logger.isDebugEnabled())
		logger.debug("Fetching parser based on RIR");
		BufferedReader bufRead=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(aResponse.getBytes())));
		String str;
		while((str=bufRead.readLine())!=null){
		// TODO write parsing logic and putting into corresponding objects of entities/model
		//TODO Create ResponseObject which contains response and WhoIsNode object.
			str=str.toLowerCase().trim();
			if(str.contains(ARIN)){
				if(logger.isDebugEnabled())
					logger.debug("ARIN parser returned");
				
		return new ArinParser(); 		
			}else if(str.contains(AFRINIC)){
				if(logger.isDebugEnabled())
					logger.debug("AFRINIC parser returned");
				
				return new AfrinicParser();
			}else if(str.contains(LACNIC)){
				if(logger.isDebugEnabled())
					logger.debug("LACNIC parser returned");
				
				return new LacnicParser();
			}else if(str.contains(APNIC)){
				if(logger.isDebugEnabled())
					logger.debug("APNIC parser returned");
				
				return new ApnicParser();
			}else if(str.contains(RIPE)){
				if(logger.isDebugEnabled())
					logger.debug("RIPE parser returned");
				
				return new RipeParser();
			}
				
		
		}
		if(logger.isDebugEnabled())
			logger.debug("No parser config found");
		
		return null;
	}
}