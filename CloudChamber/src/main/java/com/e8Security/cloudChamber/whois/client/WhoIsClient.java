package org.e8.whois.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/***
 * WhoIsClient is used for querying RIR's given an IP address. 
 * It queries IANA registry first to get authorized whois server followed by querying the server 
 * for information needed.
 *
 *
 */
public class WhoIsClient {

	private static final String IANA="whois.iana.org";
	private static String PATTERN="whois:\\s(.*)";
	private final static Logger logger=LoggerFactory.getLogger(WhoIsClient.class);

	
	/**
	 * Call IANA and then respective RIR to fetch information related to an IP address.
	 * 
	 * 
	 * @param aIpAddress
	 * @return Raw response to parsers
	 * @throws IOException
	 */
	public static String callCommandRestClient(String aIpAddress) throws IOException{
		if(logger.isDebugEnabled())
		logger.debug("Started querying IANA and getting actual whois server");
		String[] arrIPAddress=aIpAddress.split("\\.");
		Integer prefix=null;
		String whoisServer=null;
		if(arrIPAddress!=null)
		prefix=Integer.valueOf(arrIPAddress[0]);
		
		if(prefix!=null)
		whoisServer=RegistryLoader.getInstance().getWhoIsServer(prefix);
		// calls IANA when registry doesn't contain data
		if(whoisServer==null||"".equals(whoisServer)){
		String response=callCommandRestClient(IANA, aIpAddress);
		Pattern pattern=Pattern.compile(PATTERN);
		Matcher matcher=pattern.matcher(response);
		if(matcher.find()){
			whoisServer=matcher.group(1).trim();
		}
		}
		// calling whois server
		if(whoisServer!=null||!("".equals(whoisServer))){
			return callCommandRestClient(whoisServer, aIpAddress);
		}else{
			return "No whois data found";
		}
	}
	
/*
 * Overloaded method for calling actual whois server for an IP address
 * 
 */
	private static String callCommandRestClient(String aServer,String aIpAddress) throws IOException{
		if(logger.isDebugEnabled())
		logger.debug("Querying whois server: "+aServer);
		
		StringBuffer strBuf=new StringBuffer("");
		aIpAddress=aIpAddress+"\r\n";// needed as whois protocol requirements
		Socket socket=new Socket();
		socket.connect(new InetSocketAddress(aServer,43));
		DataOutputStream dsOut=new DataOutputStream(socket.getOutputStream());
		dsOut.write(aIpAddress.getBytes());
		BufferedReader bufRead=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String str="";
		while((str=bufRead.readLine())!=null){
			strBuf.append(str);
			strBuf.append(System.getProperty("line.separator"));
		}
		bufRead.close();
		socket.close();
		if(logger.isDebugEnabled())
		logger.debug("Returning response from whois server: "+aServer);
		
		return strBuf.toString();
	}

}
