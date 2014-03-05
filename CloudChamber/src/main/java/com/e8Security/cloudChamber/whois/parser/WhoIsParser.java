package com.e8security.cloudchamber.whois.parser;

import java.io.IOException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e8security.cloudchamber.whois.client.WhoIsClient;
import com.e8security.cloudchamber.whois.exceptionHandling.WhoIsException;
import com.e8security.cloudchamber.whois.model.WhoIsNode;

/***
 * 
 * WhoIsParser is used to call WhoIs client for getting response.
 * ParserBuilder is used for getting parser which is used to parse response from WhoIs client.
 * 
 *
 */
public class WhoIsParser {
	
	private final static Logger logger=LoggerFactory.getLogger(WhoIsParser.class);

	/**
	 *WhoIs client for getting response and then parse it using respective 
	 * parser. Finally returning contructed whois node.
	 * 
	 * @param ipAddress
	 * @return WhoIsNode<Long>
	 * @throws WhoIsException 
	 * 
	 */
	public static WhoIsNode<Long> parseWhoIsResponse(String ipAddress) throws WhoIsException{
		if(logger.isDebugEnabled())
		logger.debug("Started parsing response after calling WhoIs client");
		WhoIsNode<Long> responseNode=null;
		try{
		String response=WhoIsClient.callCommandRestClient(ipAddress);
		Parser parser=ParserBuilder.getParser(response);
		if(parser!=null){
		responseNode=parser.parse(response,ipAddress);
		responseNode.setRawResponse(response);
		responseNode.setCurrentData(true);
		for(int i=0;i<responseNode.getOrgAbuse().size();i++)
			responseNode.getOrgAbuse().get(i).setCurrentdata(true);
		
		for(int i=0;i<responseNode.getOrgTech().size();i++)
			responseNode.getOrgTech().get(i).setCurrentdata(true);
		
		
		for(int i=0;i<responseNode.getOrgAdmin().size();i++)
			responseNode.getOrgAdmin().get(i).setCurrentdata(true);
		}
		if(logger.isDebugEnabled())			
		logger.debug("Returning parsed response from WhoIs client");
		}catch(IOException e){
			 if(logger.isErrorEnabled())
					logger.error("I/O exception while writing/reading to/from output source in parsing process");
				throw new WhoIsException("I/O exception while writing/reading to/from output source in parsing process",e);
		} catch (ParseException e) {
			if(logger.isErrorEnabled())
				logger.error("Exception in parsing process");
			throw new WhoIsException("Exception in parsing process",e);
		}
		return responseNode;
	}
}
