package org.e8.whois.client;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpAddressTest {
	
	private static String patternPrefix="(([0-9][0-9][0-9])/[0-9][0-9]?).*(whois\\..*\\.net).*";
	private static String patternWhois=".*(whois\\..*\\.net).*";
public static void main(String... args) throws FileNotFoundException{
//	System.out.println(isMaksed("192.168.7.9-192.168.8.255"));
//getpattern("  000/8  IANA - Local Identification              whois.arin.net      1981-09                    RESERVED    [2]");
	
	System.out.println(RegistryLoader.getInstance().getWhoIsServer(192));
}


private static void getpattern(String line){
	Pattern pattern=Pattern.compile(patternPrefix);
	Matcher matcher=pattern.matcher(line);
	
	if(matcher.find()){
		System.out.println(matcher.group(1));
		System.out.println(matcher.group(2));
		System.out.println(matcher.group(3));
	
	}
	
}

private static boolean isMaksed(String aRange){
	String netrange[]=aRange.split("-");
	  if(netrange!=null&&netrange.length>1){
System.out.println("came inside");
		  // Computing Net address and mask 
		  String netAddr1[]=netrange[0].trim().split("\\.");
		  String netAddr2[]=netrange[1].trim().split("\\.");
		  
		  int addr00=Integer.valueOf(netAddr1[0]);
		  int addr01=Integer.valueOf(netAddr1[1]);
		  int addr10=Integer.valueOf(netAddr2[0]);
		  int addr11=Integer.valueOf(netAddr2[1]);
		  
		  System.out.println("addr00"+addr00);
		  System.out.println("addr00"+addr01);
		  System.out.println("addr00"+addr10);
		  System.out.println("addr00"+addr11);
		  
		  if((addr00^addr10)==0&&(addr01^addr11)==0){return true;
		  }else{
			 return false  ;
		  }
	  
	  }
return false;
}
}
