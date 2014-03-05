package com.e8security.cloudchamber.whois;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
String ipAddress="192.149.252.255";
		 
		long result = 0;
	 
		String[] ipAddressInArray = ipAddress.split("\\.");
	 
		for (int i = 3; i >= 0; i--) {
	 
			long ip = Long.parseLong(ipAddressInArray[3 - i]);
	 
			//left shifting 24,16,8,0 and bitwise OR
	 
			//1. 192 << 24
			//1. 168 << 16
			//1. 1   << 8
			//1. 2   << 0
			result |= ip << (i * 8);
	 
		}
	 
		System.out.println(result);
	  
        
    }
}
