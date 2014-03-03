package org.e8.whois.parser;

import java.io.IOException;

import org.e8.whois.client.WhoIsClient;
import org.e8.whois.client.WhoIsClientCommand;
import org.e8.whois.model.WhoIsNode;

public class WhoIsParser {

	
	public static WhoIsNode<Long> parseWhoIsResponse(String ipAddress) throws IOException{
		String response=WhoIsClient.callCommandRestClient(ipAddress);
		WhoIsNode<Long> responseNode=null;
		Parser parser=ParserBuilder.getParser(response);
		if(parser!=null){
		responseNode=parser.parse(response);
		responseNode.setRawResponse(response);
		responseNode.setIsCurrentData(true);
		for(int i=0;i<responseNode.getOrgAbuse().size();i++)
			responseNode.getOrgAbuse().get(i).setCurrentdata(true);
		
		for(int i=0;i<responseNode.getOrgTech().size();i++)
			responseNode.getOrgTech().get(i).setCurrentdata(true);
		}
		return responseNode;
	}
}
