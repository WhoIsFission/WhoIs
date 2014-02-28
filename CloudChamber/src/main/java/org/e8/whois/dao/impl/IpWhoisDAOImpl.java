package org.e8.whois.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	public List<WhoIsNode<Long>> findWhoisByIp(Long ipAddress, boolean isCurrentData) throws SQLException {
		List<WhoIsNode<Long>> whoisNodeList = null;
		Connection connection = null;
		try{
			connection = connectDataBase();
			logger.debug("Fetching IPWHOIS Master table data: findwhoisByIp:"+ipAddress);
			String selectQuery = "select * from IPWHOIS ip where is_current_data = ? and ? between ip.ip_start_address and ip.ip_end_address";
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setBoolean(1, isCurrentData);
			preparedStatement.setLong(2, ipAddress);

			ResultSet resultSet = preparedStatement.executeQuery();						
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
		logger.debug("Fetching TECHCONTACT Master table data");
		String selectTechContacts = "select * from TECHCONTACT tc where tc.ip_start_address = ? and tc.ip_end_address = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(selectTechContacts);
		preparedStatement.setLong(1, startAddress);
		preparedStatement.setLong(2, endAddress);

		ResultSet resultSet = preparedStatement.executeQuery();
		return WhoisNodeBuilder.setTechContactListValues(resultSet);	 
	}



	private List<OrganisationAbuse> getAbuseContactDetails(Connection connection,Long startAddress,Long endAddress)
			throws SQLException {

		logger.debug("Fetching ABUSECONTACT Master table data");

		String selectAbuseContacts = "select * from ABUSECONTACT ac where ac.ip_start_address = ? and ac.ip_end_address = ? ";
		PreparedStatement preparedStatement = connection.prepareStatement(selectAbuseContacts);
		preparedStatement.setLong(1, startAddress);
		preparedStatement.setLong(2, endAddress);

		ResultSet resultSet = preparedStatement.executeQuery();
		return WhoisNodeBuilder.setAbuseContactListValues(resultSet);
	}

	public List<WhoIsNode<Long>> findAllWhoisByIpData() throws Exception{

		List<WhoIsNode<Long>> whoIsNodeList = new ArrayList<WhoIsNode<Long>>();
		Connection connection = null;
		logger.debug("Fetching IPWHOIS Master table data");
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

	public void updateWhoisByIpToHistoric(WhoIsNode<Long> whoisNode) throws Exception {
		Connection connection = null;
		logger.debug("Updating IPWHOIS Master table data by Ipaddress");
		try{
			connection = connectDataBase();
			String updateQuery = "update IPWHOIS SET is_current_data = 0, LAST_UPDATED_TIME = ? WHERE ip_start_address = ?  AND ip_end_address = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

			java.sql.Timestamp time = new java.sql.Timestamp(new Date().getTime());
			preparedStatement.setTimestamp(1, time);
			preparedStatement.setLong(2, whoisNode.getLow());
			preparedStatement.setLong(3, whoisNode.getHigh());			
			preparedStatement.executeUpdate();

			String updateTechQuery = "UPDATE TECHCONTACT SET is_current_data = 0, LAST_UPDATED_TIME = ? WHERE ip_start_address = ?  AND ip_end_address = ?";
			preparedStatement = connection.prepareStatement(updateTechQuery);			
			preparedStatement.setTimestamp(1, time);
			preparedStatement.setLong(2, whoisNode.getLow());
			preparedStatement.setLong(3, whoisNode.getHigh());
			preparedStatement.executeUpdate();

			String updateAbuseQuery = "UPDATE ABUSECONTACT SET is_current_data = 0, LAST_UPDATED_TIME = ? WHERE ip_start_address = ?  AND ip_end_address = ?";
			preparedStatement = connection.prepareStatement(updateAbuseQuery);
			preparedStatement.setTimestamp(1, time);
			preparedStatement.setLong(2, whoisNode.getLow());
			preparedStatement.setLong(3, whoisNode.getHigh());
			preparedStatement.executeUpdate();
		}
		finally{
			ConnectionManager.getConnectionManager(whoisConfig).closeConnection(connection);
		}
	}	

	public void insertWhoisByIp(WhoIsNode<Long> whoisNode) throws Exception{

		Connection connection = null;
		logger.debug("Updating IPWHOIS Master table data by Ipaddress");
		try{
			connection = connectDataBase();
			String insertQuery = "INSERT INTO IPWHOIS VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			WhoisNodeBuilder.insertWhoisToDbValues(whoisNode, preparedStatement);
			preparedStatement.executeUpdate();
			
			if(whoisNode.getOrgTech() !=null && whoisNode.getOrgTech().size() > 0){
				String insertTechQuery = "INSERT INTO TECHCONTACT VALUES(?,?,?,?,?,?,?,?,?,?,?)";
				preparedStatement = connection.prepareStatement(insertTechQuery);
				WhoisNodeBuilder.insertTechContactToDbValues(whoisNode, preparedStatement);
				preparedStatement.executeBatch();
			}

			if(whoisNode.getOrgAbuse() !=null && whoisNode.getOrgAbuse().size() > 0){
				String insertAbuseQuery = "INSERT INTO ABUSECONTACT VALUES(?,?,?,?,?,?,?,?,?,?)";
				preparedStatement = connection.prepareStatement(insertAbuseQuery);
				WhoisNodeBuilder.insertAbuseContactToDbValues(whoisNode, preparedStatement);
				preparedStatement.executeBatch();
			}
		}
		finally{
			ConnectionManager.getConnectionManager(whoisConfig).closeConnection(connection);
		}

	}
}
