package org.e8.whois.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.e8.whois.configuration.WhoIsConfiguration;
import org.e8.whois.dao.ConnectionManager;
import org.e8.whois.dao.IpWhoisDAO;
import org.e8.whois.dao.builder.WhoisNodeBuilder;
import org.e8.whois.exceptionHandling.WhoIsException;
import org.e8.whois.model.Organisation;
import org.e8.whois.model.OrganisationAbuse;
import org.e8.whois.model.OrganisationTech;
import org.e8.whois.model.WhoIsNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * DAO implementation class is used for fetching, inserting,
 * updating DB tables using jdbc calls.
 *
 */
public class IpWhoisDAOImpl implements IpWhoisDAO{

	private final static Logger logger = LoggerFactory.getLogger(IpWhoisDAOImpl.class);
	private static WhoIsConfiguration whoisConfig;
	private static IpWhoisDAO IPWhoIsDAO;
	private static ConnectionManager CONNECTION_MANAGER;

	private IpWhoisDAOImpl(){
	}

	private Connection connectDataBase() throws WhoIsException {
		if(logger.isDebugEnabled())
			logger.debug("Fetching connection from connection manager");
		if(CONNECTION_MANAGER!=null)
			return CONNECTION_MANAGER.getConnection();
		else
			return null;

	}

	/**
	 * Getting singleton instance for IpWhoisDAO implementation
	 * 
	 * @param conf
	 * @return
	 * @throws WhoIsException 
	 */
	public static IpWhoisDAO getInstance(WhoIsConfiguration conf) throws WhoIsException{
		if(logger.isDebugEnabled())
			logger.debug("Singleton instance of type IpWhoisDAO");

		if(IPWhoIsDAO==null){
			synchronized(IpWhoisDAOImpl.class){
				if(IPWhoIsDAO==null){
					whoisConfig=conf;
					IPWhoIsDAO=new IpWhoisDAOImpl();
					CONNECTION_MANAGER=ConnectionManager.getConnectionManager(whoisConfig);
				}
			}
		}
		return IPWhoIsDAO;
	}

	/**
	 * Finding whois information by IP address flagged by currentData state
	 * 
	 * @param IPAddress, current state
	 * @return List of whois nodes
	 * @throws WhoIsException
	 *  
	 */

	public List<WhoIsNode<Long>> findWhoisByIp(Long ipAddress, boolean isCurrentData) throws WhoIsException{
		List<WhoIsNode<Long>> whoisNodeList = Collections.emptyList();
		Connection connection = null;
		connection = connectDataBase();
		if(logger.isDebugEnabled())
			logger.debug("Fetching IPWHOIS Master table data: findwhoisByIp:"+ipAddress);
		if(connection!=null){
			try{
				String selectQuery = "select * from IPWHOIS ip where is_current_data = ? and ? between ip.ip_start_address and ip.ip_end_address";
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				preparedStatement.setBoolean(1, isCurrentData);
				preparedStatement.setLong(2, ipAddress);

				ResultSet resultSet = preparedStatement.executeQuery();						
				whoisNodeList = returnResultSet(resultSet,connection,isCurrentData);
			}
			catch (SQLException e) {
				if(logger.isErrorEnabled())
					logger.error("Exception occured while trying to find whois by IP:",e);
			}
			finally{
				if(connection!=null)
					CONNECTION_MANAGER.closeConnection(connection);
			}
		}
		return whoisNodeList;
	}

	/*
	 * Creates List of WhoIsNode out of resultSet passed as parameter. 
	 * 
	 */
	private List<WhoIsNode<Long>> returnResultSet(ResultSet resultSet,Connection connection,boolean isCurrentData) throws SQLException {
		List<WhoIsNode<Long>> whoisNodeList = new ArrayList<WhoIsNode<Long>>();
		WhoIsNode<Long> whoisNode = null;

		while (resultSet!=null&&resultSet.next()) {	     
			whoisNode = WhoisNodeBuilder.setWhoIsNodeValues(resultSet);
			Organisation org= WhoisNodeBuilder.setOrganisationValues(resultSet);
			whoisNode.setOrg(org);

			Long startAddress = whoisNode.getLow();
			Long endAddress = whoisNode.getHigh();			

			List<OrganisationAbuse> abuseContactList = getAbuseContactDetails(connection,startAddress,endAddress,isCurrentData);			
			List<OrganisationTech> techContactList = getTechContactDetails(connection,startAddress,endAddress,isCurrentData);

			whoisNode.setOrgAbuse(abuseContactList);
			whoisNode.setOrgTech(techContactList);

			whoisNodeList.add(whoisNode);
		}
		if(resultSet !=null) resultSet.close();
		return whoisNodeList;
	}

	/**
	 * find whois information by city. 
	 * 
	 * @return List of WhoIs nodes.
	 */
	public List<WhoIsNode<Long>> findWhoisByCity(String city) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * find whois information by country. 
	 * 
	 * @return List of WhoIs nodes.
	 */
	public List<WhoIsNode<Long>> findWhoisByCountry(String country) {
		// TODO Auto-generated method stub
		return null;
	}


	/*
	 * Tech contact details for given start and end address flagged by current data state.
	 * 
	 */
	private List<OrganisationTech> getTechContactDetails(Connection connection,Long startAddress, Long endAddress,boolean isCurrentData)
			throws SQLException {	
		if(logger.isDebugEnabled())
			logger.debug("Fetching TECHCONTACT Master table data");

		String selectTechContacts = "select * from TECHCONTACT tc where tc.is_current_data = ? and tc.ip_start_address = ? and tc.ip_end_address = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(selectTechContacts);
		preparedStatement.setBoolean(1, isCurrentData);
		preparedStatement.setLong(2, startAddress);
		preparedStatement.setLong(3, endAddress);

		ResultSet resultSet = preparedStatement.executeQuery();
		return WhoisNodeBuilder.setTechContactListValues(resultSet);	 
	}

	/*
	 * Abuse contact details for given start and end address flagged by current state.
	 * 
	 */

	private List<OrganisationAbuse> getAbuseContactDetails(Connection connection,Long startAddress,Long endAddress,boolean isCurrentData)
			throws SQLException {
		if(logger.isDebugEnabled())
			logger.debug("Fetching ABUSECONTACT Master table data");

		String selectAbuseContacts = "select * from ABUSECONTACT ac where ac.is_current_data = ? and ac.ip_start_address = ? and ac.ip_end_address = ? ";
		PreparedStatement preparedStatement = connection.prepareStatement(selectAbuseContacts);
		preparedStatement.setBoolean(1, isCurrentData);
		preparedStatement.setLong(2, startAddress);
		preparedStatement.setLong(3, endAddress);

		ResultSet resultSet = preparedStatement.executeQuery();
		return WhoisNodeBuilder.setAbuseContactListValues(resultSet);
	}

	/**
	 * find whois information for all IP ranges with current state.
	 * 
	 * @param 
	 * @return List of WhoIsNode
	 * @throws WhoIsException 
	 */
	public List<WhoIsNode<Long>> findAllWhoisByIpData() throws WhoIsException{

		List<WhoIsNode<Long>> whoIsNodeList = new ArrayList<WhoIsNode<Long>>();
		Connection connection = null;
		if(logger.isDebugEnabled())
			logger.debug("Fetching IPWHOIS Master table data");

		connection = connectDataBase();
		if(connection!=null){
			try{
				String selectQuery = "select * from IPWHOIS ip where is_current_data = 1 ";
				Statement statement = connection.createStatement();

				ResultSet resultSet = statement.executeQuery(selectQuery);						
				whoIsNodeList = returnResultSet(resultSet,connection,true);//need to pass current state
			}catch(SQLException e){
				if(logger.isErrorEnabled())
					logger.error("Exception retreiving IPWHOIS records whose current flag is true.");
				throw new WhoIsException("Exception retreiving IPWHOIS records whose current flag is true ",e);
			}
			finally{

				if(connection!=null)
					CONNECTION_MANAGER.closeConnection(connection);
			}
		}
		return whoIsNodeList;
	}

	/**
	 * Updates WhoIs current state flag for existing node to false. 
	 * 
	 * @param  whoIsNode
	 * @return 
	 * @throws WhoIsException 
	 */
	public void updateWhoisByIpToHistoric(WhoIsNode<Long> whoisNode) throws WhoIsException {
		Connection connection = null;
		if(logger.isDebugEnabled())
			logger.debug("Updating IPWHOIS Master table data by Ipaddress");
		connection = connectDataBase();
		if(connection!=null){
			try{		
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
			}catch(SQLException e){
				if(logger.isErrorEnabled())
					logger.error("Exception updating historical flag for records already exist in DB Tables.");
				throw new WhoIsException("Updating historical flag failed due to exception ",e);
			}
			finally{
				if(connection!=null)
					CONNECTION_MANAGER.closeConnection(connection);
			}
		}
	}	

	/**
	 * Inserts whois node information to respective DB tables with current state as true.
	 * 
	 * @param whoisNode
	 * @return
	 * @throws WhoIsException 
	 */

	public void insertWhoisByIp(WhoIsNode<Long> whoisNode) throws WhoIsException{

		Connection connection = null;

		if(logger.isDebugEnabled())
			logger.debug("Updating IPWHOIS Master table data by Ipaddress");


		connection = connectDataBase();
		if(connection!=null){
			try{
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
			}catch(SQLException e){
				if(logger.isErrorEnabled())
					logger.error("Exception Inserting into DB Tables.");
				throw new WhoIsException("Inserting records into DB tables failed ",e);
			}
			finally{
				if(connection!=null)
					CONNECTION_MANAGER.closeConnection(connection);
			}
		}
	}
}
