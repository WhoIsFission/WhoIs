package org.e8.whois.parser;

import java.io.IOException;

import org.e8.whois.client.WhoIsClient;
import org.e8.whois.model.WhoIsNode;

public class WhoIsParser {

	
	public static WhoIsNode<Long> parseWhoIsResponse(String ipAddress) throws IOException{
		String response=WhoIsClient.callCommandRestClient(ipAddress);
		WhoIsNode<Long> responseNode=null;
		Parser parser=ParserBuilder.getParser(response);
		if(parser!=null){
		responseNode=parser.parse(response);
		responseNode.setRawResponse(response);
		}
		return responseNode;
	}
}
