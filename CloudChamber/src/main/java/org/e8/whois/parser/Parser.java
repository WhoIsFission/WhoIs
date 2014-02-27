package org.e8.whois.parser;

import java.io.IOException;

import org.e8.whois.model.WhoIsNode;

public interface Parser {

	public WhoIsNode<Long> parse(String buf) throws IOException;
	
}
