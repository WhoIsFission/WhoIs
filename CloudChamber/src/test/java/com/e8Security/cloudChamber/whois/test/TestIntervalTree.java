package com.e8Security.cloudChamber.whois.test;

import java.net.UnknownHostException;
import java.util.Set;

import com.e8Security.cloudChamber.whois.test.CacheTest.TreeNode;



public class TestIntervalTree {

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		// Used to build the tree
		
		//subset 1
	Long ip11=ipToLong("192.168.72.50");
	Long ip12=ipToLong("192.168.72.128");
	
	//subset 2
	Long ip21=ipToLong("192.168.72.60");
	Long ip22=ipToLong("192.168.72.100");
	
	//subset 3
	Long ip31=ipToLong("192.168.72.65");
	Long ip32=ipToLong("192.168.72.90");
	
	//subset 4
	Long ip41=ipToLong("192.168.72.130");
	Long ip42=ipToLong("192.168.72.224");
	
	
	
	
	CacheTest<Long> tree=new CacheTest<Long>();
	tree.insert(ip11, ip12);
	tree.insert(ip21, ip22);
	tree.insert(ip31, ip32);
	tree.insert(ip41, ip42);
	
	
	//ip key to search
	String inputIpKey="192.168.72.69";
	
	Long ipToSearch=ipToLong(inputIpKey);
	TreeNode<Long> nodSpecific=tree.searchSpecificInterval(ipToSearch);
	TreeNode<Long> nodGeneric=tree.searchGenericInterval(ipToSearch);
	System.out.println("For Ip address : "+inputIpKey);
	System.out.println();
	if(nodSpecific!=null)
	System.out.println("Most specific interval is "+longToIp(nodSpecific.getLow())+"-"+longToIp(nodSpecific.getHigh()));
	else
		System.out.println("Most specific interval is out of bound and we need to contact RIR");
	System.out.println();
	if(nodGeneric!=null)
	System.out.println("Most General interval is "+longToIp(nodGeneric.getLow())+"-"+longToIp(nodGeneric.getHigh()));
	else
		System.out.println("Most General interval is out of context and we need to query RIR");
	System.out.println();
	Set<TreeNode> setAllIntervals=tree.setOfIntervalSearch(ipToSearch);
	System.out.println("Intervals in descending order:");
	for(TreeNode<Long> nod:setAllIntervals){
		System.out.println(longToIp(nod.getLow())+"-"+longToIp(nod.getHigh()));
		}
	
	System.out.println("Height of the tree: "+tree.getHeight());
	
	
	tree.deleteCacheEntry(ipToLong("192.168.72.63"));
	
	 setAllIntervals=tree.setOfIntervalSearch(ipToSearch);
	System.out.println("Intervals in descending order:");
	for(TreeNode<Long> nod:setAllIntervals){
		System.out.println(longToIp(nod.getLow())+"-"+longToIp(nod.getHigh()));
		}
	
	System.out.println("Height of the tree: "+tree.getHeight());
	//+" right is :"+tree.root.right==null?"":longToIp(tree.root.right.getLow())+ " left is "+
		//longToIp(tree.root.left.getLow()));

	}

	/**
	 * To convert IP to long
	 * @param ipAddress as String in X.X.X.X  changed: updateddate
	  
	  change handling of email.
	 * @return ip in Long
	 * 
	 */
	 public static long ipToLong(String ipAddress) {
		 
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
	 public static String longToIp(long i) {
			 
			return ((i >> 24) & 0xFF) + 
	                   "." + ((i >> 16) & 0xFF) + 
	                   "." + ((i >> 8) & 0xFF) + 
	                   "." + (i & 0xFF);
	 
		}
	
}
