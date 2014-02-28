package org.e8.whois.dao;

import org.e8.whois.configuration.WhoIsConfiguration;
import org.e8.whois.dao.impl.IpWhoisDAOImpl;

public class DAOFactory {

	private final static String MYSQL="mysql";
	
	public static IpWhoisDAO getDAO(WhoIsConfiguration conf){
		if(MYSQL.equalsIgnoreCase(conf.getDatabase().getName()))
			return IpWhoisDAOImpl.getInstance(conf);
		else
			return null;
	}
	
}
