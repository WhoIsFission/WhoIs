package org.e8.whois.dao;

import java.sql.SQLException;
import java.util.List;

import org.e8.whois.model.WhoIsNode;

public interface IpWhoisDAO {	
	
	List<WhoIsNode<Long>> findWhoisByIp(Long intIpAddress, int isCurrentData) throws SQLException;
	
	List<WhoIsNode<Long>> findWhoisByCity(String city);
	
	List<WhoIsNode<Long>> findWhoisByCountry(String country);
	
	List<WhoIsNode<Long>> findAllWhoisByIpData() throws Exception;
	
	void updateWhoisByIp(WhoIsNode<Long> aNode);
}
