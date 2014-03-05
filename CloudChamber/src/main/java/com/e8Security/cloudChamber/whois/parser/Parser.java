package org.e8.whois.parser;

import java.io.IOException;
import java.text.ParseException;

import org.e8.whois.model.WhoIsNode;

/***
 * 
 * Parser interface is used as a template for custom parsers.
 * 
 * 
 */
public interface Parser {

	public WhoIsNode<Long> parse(String buf,String aIpAddress) throws IOException, ParseException;
	
}
