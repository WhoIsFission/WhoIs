package org.e8.whois.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParserBuilder {
	private final static String ARIN="arin";
	private final static String AFRINIC="afrinic";
	private final static String APNIC="apnic";
	private final static String LACNIC="lacnic";
	private final static String RIPE="ripe";
	
	public static Parser getParser(String aResponse) throws IOException{
		BufferedReader bufRead=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(aResponse.getBytes())));
		String str;
		while((str=bufRead.readLine())!=null){
		// TODO write parsing logic and putting into corresponding objects of entities/model
		//TODO Create ResponseObject which contains response and WhoIsNode object.
			str=str.toLowerCase().trim();
			if(str.contains(ARIN)){
		return new ArinParser(); 		
			}else if(str.contains(AFRINIC)){
				return new AfrinicParser();
			}else if(str.contains(LACNIC)){
				return new LacnicParser();
			}else if(str.contains(APNIC)){
				return new ApnicParser();
			}else if(str.contains(RIPE)){
				return new RipeParser();
			}
				
		
		}
		return null;
	}
}