package com.e8Security.cloudChamber.whois.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e8Security.cloudChamber.whois.cache.WhoisCacheTree;
import com.e8Security.cloudChamber.whois.configuration.WhoIsConfiguration;
import com.e8Security.cloudChamber.whois.dao.DAOFactory;
import com.e8Security.cloudChamber.whois.dao.IpWhoisDAO;
import com.e8Security.cloudChamber.whois.model.WhoIsNode;


public class BuildCache {
	
	
private static Logger logger=LoggerFactory.getLogger(BuildCache.class);

public static void cacheBuild(WhoIsConfiguration conf) throws Exception{
	//TODO : query DAO to get list of WhoIs nodes and call
	if(logger.isDebugEnabled())
	logger.debug("Building cache tree from currently valid DB records ");
	
	IpWhoisDAO daoInstance=DAOFactory.getDAO(conf);
	if(daoInstance!=null){
		List<WhoIsNode<Long>> lstNodes=daoInstance.findAllWhoisByIpData();
		WhoisCacheTree<Long> cacheTree=WhoisCacheTree.getCacheInstance();
		for(WhoIsNode<Long> node:lstNodes){
			cacheTree.insert(node);
		}
		if(logger.isDebugEnabled())
		logger.debug("Heihgt of tree : "+cacheTree.getHeight());	
	}
	
	if(logger.isDebugEnabled())
	logger.debug("Cache tree built");
}

}
