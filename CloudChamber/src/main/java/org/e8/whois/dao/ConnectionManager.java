package org.e8.whois.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.e8.whois.configuration.WhoIsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 */
	public static ConnectionManager getConnectionManager(WhoIsConfiguration conf){
		logger.debug("Getting connection manager instance and initializing Connection queue");
		if(CONNECTION_MANAGER==null){
			synchronized(ConnectionManager.class){
				if(CONNECTION_MANAGER==null){
					CONNECTION_MANAGER=new ConnectionManager();
					ConnectionManager.conf=conf;
					ConnectionManager.MAX_CONNECTION=ConnectionManager.conf.getConnectionPool();
					try{			CONNECTION_MANAGER.init();

					}catch(Exception e){
						throw new RuntimeException(e);
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
 */
	public Connection getConnection(){
		logger.debug("Retrieving connection object from connection queue.");
		try{
			return CONNECTION_QUEUE.take();
		} catch (InterruptedException e) {
			throw new RuntimeException("Interrupted fetching connection : ",e);
		}
	}

	/**
	 * Closing connection : Putting connection object back into the queue.
	 * 
	 * @param Connection
	 */

	public void closeConnection(Connection conn){
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
				throw new RuntimeException("Interrupted fetching connection : ",e);
			}
		}
	}
}
