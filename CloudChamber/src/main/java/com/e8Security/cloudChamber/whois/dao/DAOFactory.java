package com.e8Security.cloudChamber.whois.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e8Security.cloudChamber.whois.configuration.WhoIsConfiguration;
import com.e8Security.cloudChamber.whois.dao.impl.IpWhoisDAOImpl;
import com.e8Security.cloudChamber.whois.exceptionHandling.WhoIsException;


/***
 * 
 * DAO factory is used to obtain DAO object of respective DB.
 *
 */
public class DAOFactory {

	private final static String MYSQL="mysql";
	private final static Logger logger=LoggerFactory.getLogger(DAOFactory.class);
	
	/**
	 * Getting DAO object for a given Database configuration. 
	 * 
	 * @param conf
	 * @return DAO object
	 * @return null in case of non configured DAO
	 * @throws WhoIsException 
	 */
	public static IpWhoisDAO getDAO(WhoIsConfiguration conf) throws WhoIsException{
		if(logger.isDebugEnabled())
		logger.debug("Obtaining DAO object for a given DB configuration.");
		
		if(MYSQL.equalsIgnoreCase(conf.getDatabase().getName()))
			return IpWhoisDAOImpl.getInstance(conf);
		else
			return null;
	}
	
}
