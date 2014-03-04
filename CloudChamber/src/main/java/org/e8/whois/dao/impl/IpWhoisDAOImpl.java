package org.e8.whois.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.e8.whois.configuration.WhoIsConfiguration;
import org.e8.whois.dao.ConnectionManager;
import org.e8.whois.dao.IpWhoisDAO;
import org.e8.whois.dao.builder.WhoisNodeBuilder;
import org.e8.whois.exceptionHandling.WhoIsException;
import org.e8.whois.model.Organisation;
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
				String selectQuery = "select * from IP_WHOIS ip where is_current_data = ? and ? between ip.ip_start_address and ip.ip_end_address";
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
		List<WhoIsNode<Long>> whoisNodeList = Collections.emptyList();
		WhoIsNode<Long> whoisNode = null;

		while (resultSet!=null && resultSet.next()) {	     
			whoisNode = WhoisNodeBuilder.setWhoIsNodeValues(resultSet);
			Organisation org= WhoisNodeBuilder.setOrganisationValues(resultSet);
			whoisNode.setOrg(org);

			getContactDetails(connection,isCurrentData,whoisNode);

			logger.debug("Tech contactlist available? ", whoisNode.getOrgTech() !=null);

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
	 * All contact details for given start and end address flagged by current state.
	 * 
	 */

	private WhoIsNode<Long> getContactDetails(Connection connection,boolean isCurrentData,WhoIsNode<Long> whoisNode)
			throws SQLException {
		if(logger.isDebugEnabled())
			logger.debug("Fetching CONTACT Master table data");

		String selectContacts = "select * from IP_WHOIS_CONTACT ac where ac.is_current_data = ? and ac.ip_start_address = ? and ac.ip_end_address = ? ";
		PreparedStatement preparedStatement = connection.prepareStatement(selectContacts);
		preparedStatement.setBoolean(1, isCurrentData);
		preparedStatement.setLong(2, whoisNode.getLow());
		preparedStatement.setLong(3, whoisNode.getHigh());

		ResultSet resultSet = preparedStatement.executeQuery();
		return WhoisNodeBuilder.setContactListValues(resultSet,whoisNode);
	}

	/**
	 * find whois information for all IP ranges with current state.
	 * 
	 * @param 
	 * @return List of WhoIsNode
	 * @throws WhoIsException 
	 */
	public List<WhoIsNode<Long>> findAllWhoisByIpData() throws WhoIsException{

		List<WhoIsNode<Long>> whoIsNodeList = Collections.emptyList();
		Connection connection = null;
		if(logger.isDebugEnabled())
			logger.debug("Fetching IPWHOIS Master table data");

		connection = connectDataBase();
		if(connection!=null){
			try{
				String selectQuery = "select * from IP_WHOIS ip where ip.is_current_data = 1 ";
				Statement statement = connection.createStatement();

				ResultSet resultSet = statement.executeQuery(selectQuery);						
				whoIsNodeList = returnResultSet(resultSet,connection,true);
			}
			catch(SQLException e){
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
			logger.debug("Updating IPWHOIS Master table data by Ipaddress- to change to Historic");
		connection = connectDataBase();
		if(connection!=null){
			try{		
				String updateQuery = "UPDATE IP_WHOIS SET is_current_data = 0, LAST_UPDATED_TIME = ? WHERE ip_start_address = ?  AND ip_end_address = ? AND is_current_data = 1";

				PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
				java.sql.Timestamp time = new java.sql.Timestamp(new Date().getTime());
				preparedStatement.setTimestamp(1, time);
				preparedStatement.setLong(2, whoisNode.getLow());
				preparedStatement.setLong(3, whoisNode.getHigh());			
				preparedStatement.executeUpdate();

				String updateContactQuery = "UPDATE IP_WHOIS_CONTACT SET is_current_data = 0, LAST_UPDATED_TIME = ? WHERE ip_start_address = ?  AND ip_end_address = ? and is_current_data = 1";
				
				preparedStatement = connection.prepareStatement(updateContactQuery);			
				preparedStatement.setTimestamp(1, time);
				preparedStatement.setLong(2, whoisNode.getLow());
				preparedStatement.setLong(3, whoisNode.getHigh());
				preparedStatement.executeUpdate();
			}
			catch(SQLException e){
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
			logger.debug("Updating IP_WHOIS Master table data by Ipaddress");

		connection = connectDataBase();
		if(connection!=null){
			try{
				String insertQuery = "INSERT INTO IP_WHOIS VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
				WhoisNodeBuilder.insertWhoisToDbValues(whoisNode, preparedStatement);
				preparedStatement.executeUpdate();

				String insertContactsQuery = "INSERT INTO IP_WHOIS_CONTACT VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
				preparedStatement = connection.prepareStatement(insertContactsQuery);
				if(whoisNode.getOrgTech() !=null && whoisNode.getOrgTech().size() > 0){					
					WhoisNodeBuilder.insertTechContactToDbValues(whoisNode, preparedStatement);
				}

				if(whoisNode.getOrgAbuse() !=null && whoisNode.getOrgAbuse().size() > 0){
					WhoisNodeBuilder.insertAbuseContactToDbValues(whoisNode, preparedStatement);
				}
				
				if(whoisNode.getOrgAdmin() !=null && whoisNode.getOrgAdmin().size() >0){
					WhoisNodeBuilder.insertAdminContactToDbValues(whoisNode, preparedStatement);
				}
				preparedStatement.executeBatch();

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
