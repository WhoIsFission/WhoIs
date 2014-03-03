package org.e8.whois.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhoIsClient {

	private static final String IANA="whois.iana.org";
	private static String PATTERN="whois:\\s(.*)";


	public static String callCommandRestClient(String aIpAddress) throws IOException{
		String response=callCommandRestClient(IANA, aIpAddress);
		Pattern pattern=Pattern.compile(PATTERN);
		Matcher matcher=pattern.matcher(response);
		String whoisServer=null;
		if(matcher.find()){
			whoisServer=matcher.group(1).trim();
		}
		if(whoisServer!=null){
			return callCommandRestClient(whoisServer, aIpAddress);
		}else{
			return "No whois data found";
		}
	}
	

	private static String callCommandRestClient(String aServer,String aIpAddress) throws IOException{
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

		return strBuf.toString();
	}

}
