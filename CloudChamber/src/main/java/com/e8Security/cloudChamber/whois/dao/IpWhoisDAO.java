package com.e8security.cloudchamber.whois.dao;

import java.util.List;

import com.e8security.cloudchamber.whois.exceptionHandling.WhoIsException;
import com.e8security.cloudchamber.whois.model.WhoIsNode;

/***
 * Template for all DAO implementation
 * 
 *
 */
public interface IpWhoisDAO {	
	
	List<WhoIsNode<Long>> findWhoisByIp(Long ipAddress, boolean isCurrentData) throws WhoIsException;
	
	List<WhoIsNode<Long>> findWhoisByCity(String city);
	
	List<WhoIsNode<Long>> findWhoisByCountry(String country);
	
	List<WhoIsNode<Long>> findAllWhoisByIpData() throws Exception;
	
	void updateWhoisByIpToHistoric(WhoIsNode<Long> whoisNode) throws WhoIsException;
	
	void insertWhoisByIp(WhoIsNode<Long> whoisNode) throws WhoIsException;
}
