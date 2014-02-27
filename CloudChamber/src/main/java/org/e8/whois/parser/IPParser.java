package org.e8.whois.parser;

import org.apache.commons.net.util.SubnetUtils;

public abstract class IPParser implements Parser{

	/**
	 * To convert IP to long
	 * @param ipAddress as String in X.X.X.X
	 * @return ip in Long
	 * 
	 */
	   Long ipToLong(String ipAddress) {
		 
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
		 
			return result;
		  }
	 
	/**
	 * 
	 * To convert Long to ip address
	 * 
	 * @param Ip in Long
	 * @return ip address as String
	 */
	  String longToIp(long i) {
			 
			return ((i >> 24) & 0xFF) + 
	                   "." + ((i >> 16) & 0xFF) + 
	                   "." + ((i >> 8) & 0xFF) + 
	                   "." + (i & 0xFF);
	 
		}
	
	  
	  /**
	   * 
	   * Convert CIDR block to Range of IP addresses.
	   * 
	   * @param aCIDR
	   * @return
	   */
	  String cidrToRange(String aCIDR){
		  
		 String subnet=validateSubnet(aCIDR);
		  SubnetUtils utils = new SubnetUtils(subnet);
utils.setInclusiveHostCount(true);
return (utils.getInfo().getLowAddress()+"-"+utils.getInfo().getHighAddress());
	  }
	  
	  
	  /**
	   * 
	   *Validate CIDR block 
	   * 
	   * @param aCIDR
	   * @return
	   */
	  
	  private String validateSubnet(String aCIDR) {
		// TODO Auto-generated method stub
		String subnets[]=aCIDR.split("\\.");
		if(subnets.length>3)return aCIDR;
		else{
			String[] splitLast=subnets[2].split("/");
			return (subnets[0]+"."+subnets[1]+"."+splitLast[0]+".0/"+splitLast[1]);
		}
	}
	/**
	   * Check whether given range is CIDR block
	   * 
	   * @param aRange
	   * @return
	   */
	  boolean isCidrNotation(String aRange){
		    return (aRange.indexOf("/")>0);

	  }
	  
}
