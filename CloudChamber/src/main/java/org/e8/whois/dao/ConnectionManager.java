package org.e8.whois.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.e8.whois.configuration.WhoIsConfiguration;
import org.e8.whois.exceptionHandling.WhoIsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * ConnectionManager is used as a connection pool manager with configured no. of connections
 * to be maintained in a queue.
 * 
 * @author Abhijit
 *
 */
public class ConnectionManager {

	private final static Logger logger=LoggerFactory.getLogger(ConnectionManager.class);
	private  static ConnectionManager CONNECTION_MANAGER;
	private static WhoIsConfiguration conf;
	private static int MAX_CONNECTION;
	private final BlockingQueue<Connection> CONNECTION_QUEUE=new LinkedBlockingQueue<Connection>();	

	private ConnectionManager(){

	}
	
	/**
	 * Getting ConnectionManager instance out of the given configuration and initializing connection queue.
	 * 
	 * @param conf
	 * @return ConnectionManager
	 * @throws WhoIsException 
	 */
	public static ConnectionManager getConnectionManager(WhoIsConfiguration conf) throws WhoIsException{
		if(logger.isDebugEnabled())
		logger.debug("Getting connection manager instance and initializing Connection queue");
		
		if(CONNECTION_MANAGER==null){
			synchronized(ConnectionManager.class){
				if(CONNECTION_MANAGER==null){
					CONNECTION_MANAGER=new ConnectionManager();
					ConnectionManager.conf=conf;
					ConnectionManager.MAX_CONNECTION=ConnectionManager.conf.getConnectionPool();
								try {
									CONNECTION_MANAGER.init();
								} catch (ClassNotFoundException e) {
									// TODO Auto-generated catch block
									if(logger.isErrorEnabled())
										logger.error("Class Not found for configured driver class in Cnfiguration.yml");
									throw new WhoIsException("Driver class not found",e);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									if(logger.isErrorEnabled())
										logger.error("SQL exception while initializing connections to pool");
									throw new WhoIsException("Exception initializing connections",e);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									if(logger.isErrorEnabled())
										logger.error("SQL exception while initializing connections to pool");
									throw new WhoIsException("Thread Interrupted initializing connections",e);
								}

					
				}
			}
		}
		return CONNECTION_MANAGER;
	}

	/*
	 * initialize Connection manager queue
	 * 
	 */
	private void init() throws ClassNotFoundException, SQLException, InterruptedException{
		if(logger.isDebugEnabled())
		logger.debug("Fetching DB connection objects and stores them in Connection queue.");
		
		Class.forName(conf.getDatabase().getDriverClass());	
		for(int i=0;i<MAX_CONNECTION;i++){
			Connection connection=DriverManager.getConnection(conf.getDatabase().getUrl(), conf.getDatabase().getUser(),
					conf.getDatabase().getPassword());	
			CONNECTION_QUEUE.put(connection);

		}
	}

/**
 * Fetching Connection object from Connection queue.
 * 
 * @return Connection
 * @throws WhoIsException
 * 
 */
	public Connection getConnection() throws WhoIsException {
		if(logger.isDebugEnabled())
		logger.debug("Retrieving connection object from connection queue.");
				try {
					return CONNECTION_QUEUE.take();
				} catch (InterruptedException e) {

					// TODO Auto-generated catch block
					if(logger.isErrorEnabled())
						logger.error("Exception retreiving connection from queue");
					throw new WhoIsException("Process interrupted while retreiving connection by DAO layer",e);
				
				}
	
	}

	/**
	 * Closing connection : Putting connection object back into the queue.
	 * 
	 * @param Connection
	 * @throws WhoIsException 
	 * 
	 */

	public void closeConnection(Connection conn) throws WhoIsException{
		if(logger.isDebugEnabled())
		logger.debug("Putting connection object back into queue.");
		
		boolean flag=true;
		synchronized (CONNECTION_MANAGER) {
			if(CONNECTION_QUEUE.size()>=MAX_CONNECTION){
				flag=false;
			}
		}
		if(flag){
				try {
					CONNECTION_QUEUE.put(conn);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					if(logger.isErrorEnabled())
						logger.error("Exception putting connection to connection queue");
					throw new WhoIsException("Process interrupted while putting connections back to queue",e);
				}		
		}
	}
}
