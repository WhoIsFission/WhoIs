package org.e8.whois.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.e8.whois.configuration.WhoIsConfiguration;

public class ConnectionManager {

	private  static ConnectionManager CONNECTION_MANAGER;
	private static WhoIsConfiguration conf;
	private static int MAX_CONNECTION;
	private final BlockingQueue<Connection> CONNECTION_QUEUE=new LinkedBlockingQueue<Connection>();	

	private ConnectionManager(){

	}
	public static ConnectionManager getConnectionManager(WhoIsConfiguration conf){
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

	private void init() throws ClassNotFoundException, SQLException, InterruptedException{

		Class.forName(conf.getDatabase().getDriverClass());	
		for(int i=0;i<MAX_CONNECTION;i++){
			Connection connection=DriverManager.getConnection(conf.getDatabase().getUrl(), conf.getDatabase().getUser(),
					conf.getDatabase().getPassword());	
			CONNECTION_QUEUE.put(connection);

		}
	}


	public Connection getConnection(){
		try{
			return CONNECTION_QUEUE.take();
		} catch (InterruptedException e) {
			throw new RuntimeException("Interrupted fetching connection : ",e);
		}
	}


	public void closeConnection(Connection conn){

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
