package org.e8.whois.service;

import java.util.List;

import org.e8.whois.cache.WhoisCacheTree;
import org.e8.whois.configuration.WhoIsConfiguration;
import org.e8.whois.dao.IpWhoisDAO;
import org.e8.whois.dao.impl.IpWhoisDAOImpl;
import org.e8.whois.model.WhoIsNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BuildCache {
	
	
private static Logger cacheBuildLog=LoggerFactory.getLogger(BuildCache.class);

	public static void cacheBuild(WhoIsConfiguration conf) throws Exception{
		//TODO : query DAO to get list of WhoIs nodes and call 
		IpWhoisDAO daoInstance=IpWhoisDAOImpl.getInstance(conf);
	List<WhoIsNode<Long>> lstNodes=daoInstance.findAllWhoisByIpData();
		WhoisCacheTree<Long> cacheTree=WhoisCacheTree.getCacheInstance();
			for(WhoIsNode<Long> node:lstNodes){
			cacheTree.insert(node);
		}
	}

}
