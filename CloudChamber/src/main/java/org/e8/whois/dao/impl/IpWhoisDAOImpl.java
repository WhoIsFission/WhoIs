package org.e8.whois.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.e8.whois.configuration.Database;
import org.e8.whois.configuration.WhoIsConfiguration;
import org.e8.whois.dao.ConnectionManager;
import org.e8.whois.dao.IpWhoisDAO;
import org.e8.whois.dao.builder.WhoisNodeBuilder;
import org.e8.whois.model.Organisation;
import org.e8.whois.model.OrganisationAbuse;
import org.e8.whois.model.OrganisationTech;
import org.e8.whois.model.WhoIsNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpWhoisDAOImpl implements IpWhoisDAO{

	final static Logger logger = LoggerFactory.getLogger(IpWhoisDAOImpl.class);
	private static WhoIsConfiguration whoisConfig;
	private static IpWhoisDAO IPWhoIsDAO;

	private IpWhoisDAOImpl(){
	}

	public Connection connectDataBase() throws Exception {

		return ConnectionManager.getConnectionManager(whoisConfig).getConnection();
		
	}

public static IpWhoisDAO getInstance(WhoIsConfiguration conf){
	if(IPWhoIsDAO==null){
		synchronized(IpWhoisDAOImpl.class){
			if(IPWhoIsDAO==null){
				whoisConfig=conf;
				IPWhoIsDAO=new IpWhoisDAOImpl();
			}
		}
	}
	return IPWhoIsDAO;
}
//why not boolean
	public List<WhoIsNode<Long>> findWhoisByIp(Long intIpAddress, boolean isCurrentData) throws SQLException {
		List<WhoIsNode<Long>> whoisNodeList = null;
		Connection connection = null;
		try{
			connection = connectDataBase();

			String selectQuery = "select * from IPWHOIS ip where is_current_data = ? and ? between ip.ip_start_address and ip.ip_end_address";
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setBoolean(1, isCurrentData);
			preparedStatement.setLong(2, intIpAddress);

			ResultSet resultSet = preparedStatement.executeQuery(selectQuery);						
			whoisNodeList = returnResultSet(resultSet,connection);
		}
		catch (Exception e) {
			logger.error("Exception occured while trying to find whois by IP:",e);
		}
		finally{
			ConnectionManager.getConnectionManager(whoisConfig).closeConnection(connection);
		}

		return whoisNodeList;
	}

	private List<WhoIsNode<Long>> returnResultSet(ResultSet resultSet,Connection connection) throws SQLException {
		List<WhoIsNode<Long>> whoisNodeList = new ArrayList<WhoIsNode<Long>>();
		WhoIsNode<Long> whoisNode = null;

		while (resultSet!=null&&resultSet.next()) {	     
			whoisNode = WhoisNodeBuilder.setWhoIsNodeValues(resultSet);
			Organisation org= WhoisNodeBuilder.setOrganisationValues(resultSet);
			whoisNode.setOrg(org);

			Long startAddress = whoisNode.getLow();
			Long endAddress = whoisNode.getHigh();

			List<OrganisationAbuse> abuseContactList = getAbuseContactDetails(connection,startAddress,endAddress);			
			List<OrganisationTech> techContactList = getTechContactDetails(connection,startAddress,endAddress);

			whoisNode.setOrgAbuse(abuseContactList);
			whoisNode.setOrgTech(techContactList);
			
			whoisNodeList.add(whoisNode);
		}
		if(resultSet !=null) resultSet.close();
		return whoisNodeList;
	}


	public List<WhoIsNode<Long>> findWhoisByCity(String city) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<WhoIsNode<Long>> findWhoisByCountry(String country) {
		// TODO Auto-generated method stub
		return null;
	}



	private List<OrganisationTech> getTechContactDetails(Connection connection,Long startAddress, Long endAddress)
			throws SQLException {	
		String selectTechContacts = "select * from TECHCONTACT tc where tc.ip_start_address = ? and tc.ip_end_address = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(selectTechContacts);
		preparedStatement.setLong(1, startAddress);
		preparedStatement.setLong(2, endAddress);

		ResultSet resultSet = preparedStatement.executeQuery(selectTechContacts);
		return WhoisNodeBuilder.setTechContactListValues(resultSet);	 
	}



	private List<OrganisationAbuse> getAbuseContactDetails(Connection connection,Long startAddress,Long endAddress)
			throws SQLException {
		String selectAbuseContacts = "select * from ABUSECONTACT ac where ac.ip_start_address = ? and ac.ip_end_address = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(selectAbuseContacts);
		preparedStatement.setLong(1, startAddress);
		preparedStatement.setLong(2, endAddress);

		ResultSet resultSet = preparedStatement.executeQuery(selectAbuseContacts);
		return WhoisNodeBuilder.setAbuseContactListValues(resultSet);
	}

	public List<WhoIsNode<Long>> findAllWhoisByIpData() throws Exception{

		List<WhoIsNode<Long>> whoIsNodeList = new ArrayList<WhoIsNode<Long>>();
		Connection connection = null;
		try{
			connection = connectDataBase();
			String selectQuery = "select * from IPWHOIS ip where is_current_data = 1 ";
			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(selectQuery);						
			whoIsNodeList = returnResultSet(resultSet,connection);
		}
		finally{
			ConnectionManager.getConnectionManager(whoisConfig).closeConnection(connection);
		}
		return whoIsNodeList;
	}

	public void updateWhoisByIp(WhoIsNode<Long> aNode) {
		// TODO Auto-generated method stub

	}
}
